이전 버전의 컨트롤러는 사용하지도 않는  HttpServletRequest, HttpServletResponse이 반복되는 단점이 존재하였다. 이를 제거하여 컨트롤러가 서블릿 기술을 몰라도 동작할 수 있도록 변경하고 뷰 이름이 중복되는 부분역시 제거하여 좀더 발전한 방식의 컨트롤러를 만들어 볼 것이다.

먼저 여태까지 컨트롤러에서 서블릿에 종속적인 `HttpServletRequest`을 사용했었는데, 서블릿의 종속성을 제거하기 위해 Model을 직접 만들고 View까지 직접 전달하는 객체를 만들어 볼 것이다.

#### ModelView
```java
public class ModelView {
    private String viewName;
    private Map<String, Object> model = new HashMap<>();

    public ModelView(String viewName) {
        this.viewName = viewName;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public void setModel(Map<String, Object> model) {
        this.model = model;
    }
}
```
뷰의 이름과 뷰를 렌더링 할때 필요한 model 객체를 가지고 있다. model은 단순히 Map으로 선언되어 있기 때문에 필요한 경우 컨트롤러에서 뷰에 필요한 데이터를 key, value 형식으로 넣어주기만 하면 된다.

#### Controller V3
```java
public interface ControllerV3 {

    ModelView process(Map<String, String> paramMap);
}
```
서블릿 기술을 전혀 사용하지 않기 대문에 구현이 단순해진 것을 알 수 있다. HttpServletRequest가 제공하는 파라미터는 프론트 컨트롤러가 paramMap에 담아서 호출해주면 된다. 이후, 응답 결과로 뷰 이름과 뷰에 전달할 Model 데이터를 포함하는 ModelView 객체를 반환하면 된다.

#### 회원 등록 V3

```java
public class MemberFormControllerV3 implements ControllerV3 {

    @Override
    public ModelView process(Map<String, String> paramMap) {
        return new ModelView("new-form");
    }
}
```
모델 뷰를 생성할때 view의 논리적인 이름만 지정하면 사용이 가능하다. 이후 물리적인 이름은 프론트 컨트롤러에서 처리하여 코드가 매우 단순해진다.

#### 회원 저장 V3

```java
public class MemberSaveControllerV3 implements ControllerV3 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public ModelView process(Map<String, String> paramMap) {
        String username = paramMap.get("username");
        int age = Integer.parseInt(paramMap.get("age"));

        Member member = new Member(username, age);
        memberRepository.save(member);

        ModelView mv = new ModelView("save-result");
        mv.getModel().put("member", member);
        return mv;
    }
}
```
기존의 `HttpServletRequest` 대신 `Map`을 사용한다. 파라미터의 정보는 `Map`에 담겨있기때문에 필요한 요청 파라미터를 Map에서 조회할 수 있다. 또한, 모델은 단순한 `map`이므로 모델에 뷰에서 필요한 `member` 객체를 담고 반환할 수 있다.

#### 회원 목록 V3
```java
public class MemberListControllerV3 implements ControllerV3 {

    private final MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public ModelView process(Map<String, String> paramMap) {
        List<Member> members = memberRepository.findAll();
        ModelView mv = new ModelView("members");
        mv.getModel().put("members", members);

        return mv;
    }
}
```

#### 프론트 컨트롤러 V3
```java

@WebServlet(name = "frontControllerServletV3", urlPatterns = "/front-controller/v3/*")
public class FrontControllerServletV3 extends HttpServlet {

    private Map<String, ControllerV3> controllerMap = new HashMap<>();

    public FrontControllerServletV3() {
        controllerMap.put("/front-controller/v3/members/new-form", new MemberFormControllerV3());
        controllerMap.put("/front-controller/v3/members/save", new MemberSaveControllerV3());
        controllerMap.put("/front-controller/v3/members", new MemberListControllerV3());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        ControllerV3 controller = controllerMap.get(requestURI);
        if (controller == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Map<String, String> paramMap = createParamMap(request);
        ModelView mv = controller.process(paramMap);

        String viewName = mv.getViewName();
        MyView view = viewResolver(viewName);

        view.render(mv.getModel(), request, response);
    }

    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

    private Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }
}
```
- 요청 URI를 받아와 해당 URI에 대한 컨트롤러를 저장한다.
- `paramMap`에 `HttpServletRequest`에서 파라미터 정보를 꺼내어 저장한다.
- `ModelView mv`에 `controller.process()`의 실행 결과인 모델뷰 객체를 저장한다.
- `mv`에서 뷰네임을 가져오고 뷰리졸버를 이용하여 `MyView`를 생성한다.
- 생성된 `MyView`에서 `render()` 함수를 이용하여 HTML 화면을 렌더링한다.

#### MyView
```java
public void render(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        modelToRequestAttribute(model, request);
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }

    private void modelToRequestAttribute(Map<String, Object> model, HttpServletRequest request) {
        model.forEach((key, value) -> request.setAttribute(key, value));
    }
```
Map을 받을 수 있도록 `render()` 함수를 변경하였다.





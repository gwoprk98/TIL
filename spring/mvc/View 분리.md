지난 포스팅에서도 작성하였지만 `RequestDispatcher dispatcher` 가 View로 이동하는 부분의 코드가 중복되었기 때문에 이를 해결하기 위해 View를 분리하는 과정을 진행해 볼 것이다.

#### MyView
```java
public class MyView {

    private String viewPath;

    public MyView(String viewPath) {
        this.viewPath = viewPath;
    }

    public void render(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }
}
```
이전의 코드에서 반복되는 부분을 `render()` 함수를 이용하여 따로 분리해내었다.

#### Controller V2
```java
public interface ControllerV2 {
 
 MyView process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

}
```
반환형이 MyView로 바뀐 모습을 알 수 있다.

#### 회원 등록 V2
```java
public class MemberFormControllerV2 implements ControllerV2 {

    @Override
    public MyView process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        return new MyView("/WEB-INF/views/new-form.jsp");
    }
}
```
- `Myview`를 반환한다.
- 복잡한 `dispatcher.forward()` 생성해서 호출하지 않고, MyView 객체를 생성하고 뷰 이름을 넣어 반환하면 된다.

#### 회원 저장 V2
```java
public class MemberSaveControllerV2 implements ControllerV2 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public MyView process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        Member member = new Member(username, age);
        memberRepository.save(member);

        //Model에 데이터를 보관한다.
        request.setAttribute("member", member);

        return new MyView("/WEB-INF/views/save-result.jsp");
    }
}
```

#### 회원 목록 V2

```java
public class MemberListControllerV2 implements ControllerV2 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public MyView process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Member> members = memberRepository.findAll();
        request.setAttribute("members", members);
        return new MyView("/WEB-INF/views/members.jsp");
    }
}
```

#### 프론트 컨트롤러 V2
```java
@WebServlet(name = "frontControllerServletV2", urlPatterns = "/front-controller/v2/*")
public class FrontControllerServletV2 extends HttpServlet {

    private Map<String, ControllerV2> controllerMap = new HashMap<>();

    public FrontControllerServletV2() {
        controllerMap.put("/front-controller/v2/members/new-form", new MemberFormControllerV2());
        controllerMap.put("/front-controller/v2/members/save", new MemberSaveControllerV2());
        controllerMap.put("/front-controller/v2/members", new MemberListControllerV2());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        ControllerV2 controller = controllerMap.get(requestURI);
        if (controller == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        MyView view = controller.process(request, response);
        view.render(request, response);
    }
```
- V1과 유사한 로직으로 동작한다.
- 반환타입이 `MyView`로 변화하여 프론트 컨트롤러는 컨트롤러의 호출 결과로 `MyView`를 반환 받는다.
- 이후 `view.render()`를 호출하여 `forward`로직을 수행하여 화면이 생성된다.

뷰를 분리함으로써 `Myview`객체의 `render()`를 호출하는 부분을 일관되게 처리가 가능해졌다.



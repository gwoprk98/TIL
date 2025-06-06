# [Spring MVC] HTTP 요청 파라미터


앞서 서블릿파트에서 공부했듯이, 클라이언트에서 서버로 요청 데이터를 보낼 때는 주로 3가지 방법을 사용한다.

# GET - 쿼리 파라미터
- 메시지 바디 없이, url의 쿼리 파라미터에 데이터를 포함해서 전달하는 방식.
- 검색, 필터, 페이징등에 주로 사용한다.
```java
 /**
     * 반환 타입이 없으면서 이렇게 응답에 값을 직접 집어넣으면, view 조회X
     */
    @RequestMapping("/request-param-v1")

    public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        log.info("username={}, age={}", username, age);
        response.getWriter().write("ok");
    }
```
해당 경로에 접근하여 username, age 정보를 넘겨주면 request와 response를 받아 반환한다.


# POST - HTML Form
- 메시지 바디에 쿼리 파라미터 형식으로 전달하는 방식.
- 주로 회원가입, 주문, HTML Form 사용에 이용한다.
- html의 폼 태그 이용.

## @RequestParam
- 스프링이 제공하는 애너테이션. 이를 이용하면 요청 파라미터를 간편하게 사용 가능하다.

```java
	@ResponseBody
    @RequestMapping("/request-param-v2")
    public String requestParamV2(
            @RequestParam("username") String memberName,
            @RequestParam("age") int memberAge
    ) {
        log.info("memberName={}, memberAge={}", memberName, memberAge);
        return "ok";
    }
 ```
 - `@RequestParam`: 파라미터 이름으로 바인딩하는 것.
 - `@ResponseBody`: View조회x, HTTP message body에 직접 해당 내용을 입력한다.
 - `@RequestParam`의 `name(value)`속성이 파라미터 이름으로 사용된다.
 
 > 만약에 HTTP 파라미터 이름이 변수 이름과 같다면 `@RequestParam(name="xx")`이 생략 가능하다.
 -> `@RequestParam String username`과 같은 형식.
 > 더 나아가 `String, int, Integear`등의 단순 타입이면 `@RequestParam`도 생략이 가능하나 애노테이션을 완전히 생력하면 코드를 이해하는데 문제가 발생할 수 있으므로 지나친 생략은 과할 수 있다.
 
### 파라미터 필수 여부
```java
/**
 * @RequestParam.required
 * /request-param-required -> username이 없으므로 예외
 *
 * 주의!
 * /request-param-required?username= -> 빈문자로 통과
 *
 * 주의!
 * /request-param-required
 * int age -> null을 int에 입력하는 것은 불가능, 따라서 Integer 변경해야 함(또는 다음에 나오는
defaultValue 사용)
 */
@ResponseBody
@RequestMapping("/request-param-required")
public String requestParamRequired(
 
 @RequestParam(required = true) String username,
 @RequestParam(required = false) Integer age) {
 
 log.info("username={}, age={}", username, age);
 return "ok";
}
```
- `@RequiredParam.required`는 파라미터 필수 여부를 지정한다. 기본값은 필수(true)이다.
- 파라미터 이름은 존재하고 값이 null인 경우에는 **빈 문자로 통과하여 오류가 발생하지 않는다.**

### 기본값 적용
```java
 @ResponseBody
    @RequestMapping("/request-param-default")
    public String requestParamDefault(
            @RequestParam(required = true, defaultValue = "guest") String username,
            @RequestParam(required = false, defaultValue = "-1") int age
    ) {
        log.info("username={}, age={}", username, age);
        return "ok";
    }
```
- 파라미터에 값이 없는 경우 `defaultValue`를 이용하여 기본 값을 적용 가능.
- 이를 사용하면 기본값이 존재하므로 자연스럽게 `required`는 의미가 없게된다.
- 빈 문자의 경우에도 설정한 기본값이 적용.

### 파라미터 Map으로 조회하기

```java
 @ResponseBody
    @RequestMapping("/request-param-map")
    public String requestParamMap(@RequestParam Map<String, Object> paramMap) {
        log.info("username={}, age={}", paramMap.get("username"), paramMap.get("age"));
        return "ok";
    }
```
- 파라미터를 Map, MultiValueMap으로 조회 가능.
- `@RequestParam Map` 
	- `Map(key=value)`
- `@RequestParam MultiValueMap`
	- `MultiValueMap(key=[value1, value2, ...] ex) (key=userIds, value=[id1,id2])`
- 파라미터를 여러개 사용하는 경우는 잘 없기때문에 주로 Map을 이용한 조회만 사용한다.

### @ModelAttribute
여태 학습한 내용을 바탕으로 실제 개발을 한다 했을때 다음과 같이 코드를 작성하게 될것이다.
```java
@RequestParam String username;
@RequestParam int age;
HelloData data = new HelloData();
data.setUsername(username);
data.setAge(age);
```

하지만 스프링을 이용하면 `@ModelAttribute`을 이용하여 이 과정을 간소화 할 수 있다.

`username, age`를 포함한 요청 파라미터를 바인딩 받을 객체 HelloData가 존재할 때

```java
 @ResponseBody
    @RequestMapping("/model-attribute-v1")
    public String modelAttributeV1(@ModelAttribute HelloData helloData) {
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
        return "ok";
    }
```
위 코드와 같이 `@ModelAttribute`를 적용하여 자동으로 `HelloData` 객체를 생성하고 요청 파라미터의 값을 집어넣을 수 있다.

- 스프링MVC는 @ModelAttribute 가 있으면 다음을 실행한다.
	- HelloData 객체를 생성한다.
	- 요청 파라미터의 이름으로 HelloData 객체의 프로퍼티를 찾는다. 그리고 해당 프로퍼티의 setter를 호출해서 파라미터의 값을 입력(바인딩) 한다.
	- 예) 파라미터 이름이 username 이면 setUsername() 메서드를 찾아서 호출하면서 값을 입력한다.
    
> **프로퍼티**
- 객체에 `getUsername() , setUsername()` 메서드가 있으면, 이 객체는 username 이라는 프로퍼티를 가지고 있다라고 한다.
- username 프로퍼티의 값을 변경하면 setUsername()이 호출되고, 값을 조회하면 getUsername()이 호출된다.

# HTTP message body에 직접 데이터를 담아서 요청

- HTTP API에서 주로 사용하는 방식이다. TEXT, JSON, XML등을 이용한다.
- PUT, PATCH, POST 방식 사용.
- 이경우엔 `@RequestParam`,`@ModelAttribute`를 사용할 수 없기때문에 다른 방법이 필요하다.

## 단순 텍스트
**V1**
```java
 @PostMapping("/request-body-string-v1")
    public void requestBodyString(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messaageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messaageBody: {}", messaageBody);

        response.getWriter().write("ok");
    }
   
```

- Postman으로 테스트해보면 이상없이 작동하는 모습을 볼 수 있다.

**V2**
```java
/**
 * InputStream(Reader): HTTP 요청 메시지 바디의 내용을 직접 조회
 * OutputStream(Writer): HTTP 응답 메시지의 바디에 직접 결과 출력
 */
 
@PostMapping("/request-body-string-v2")
public void requestBodyStringV2(InputStream inputStream, Writer responseWriter)
throws IOException {
 String messageBody = StreamUtils.copyToString(inputStream,StandardCharsets.UTF_8);
 
 log.info("messageBody={}", messageBody);
 responseWriter.write("ok");
}
```
- 처음의 코드를 스프링MVC가 제공하는 파라미터를 통해 간소화시킨것이다.
	- InputStreamReader(Reader): HTTP 요청 메시지 바디의 내용을 직접 조회 하는것.
	- OutputStreamReader(Writer): HTTP 응답 메시지 바디에 직접 결과를 출력 하는것.
    
**V3**

```java
/**
 * HttpEntity: HTTP header, body 정보를 편리하게 조회
 * - 메시지 바디 정보를 직접 조회(@RequestParam X, @ModelAttribute X)
 * - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
 *
 * 응답에서도 HttpEntity 사용 가능
 * - 메시지 바디 정보 직접 반환(view 조회X)
 * - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
 */
 
@PostMapping("/request-body-string-v3")
public HttpEntity<String> requestBodyStringV3(HttpEntity<String> httpEntity) {
 String messageBody = httpEntity.getBody();
 
 log.info("messageBody={}", messageBody);
 return new HttpEntity<>("ok");
 }
```
- HTTP Entity: HTTP 헤더, 바디를 편리하게 조회
	- 메시지 바디 정보를 직접 조회
    - 요청 파라미터를 조회하는 기능과는 관련이 없다.
- HTTP Entity는 응답에도 사용 가능하다.
	- 메시지 바디 정보를 직접 반환
    - 헤더 정보 포함 가능.
    - view는 조회할 수 없다.

**V4**
```java
/**
 * @RequestBody
 * - 메시지 바디 정보를 직접 조회(@RequestParam X, @ModelAttribute X)
 * - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
 *
 * @ResponseBody
 * - 메시지 바디 정보 직접 반환(view 조회X)
 * - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
 */
 @PostMapping("/request-body-string-v4")
    public HttpEntity<String> requestBodyStringV4(@RequestBody String messageBody) throws IOException {
        log.info("messaageBody: {}", messageBody);

        return new HttpEntity<>("ok");
    }
 ```
 
### @RequestBody
HTTP 바디 정보를 편리하게 조회 가능하다. 헤더 정보가 필요하다면 `HTTPEntity`나 `@RequestParm`을 사용할 수 있다.
요청 파라미터를 조회하는 기능과는 전혀 관련이 없다.(`@RequestParam, @ModelAttribute`)

### @ResponseBody
응답 결과를 HTTP 바디에 직접 담아 전달이 가능하다. view를 사용하지 않는다.

## JSON

**V1**
```java
/**
 * {"username":"hello", "age":20}
 * content-type: application/json
 */

@Slf4j
@Controller
public class RequestBodyJsonController {

    private ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/request-body-json-v1")
    public void requestBodyJsonV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody: {}", messageBody);
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        log.info("username: {}, age: {}", helloData.getUsername(), helloData.getAge());

        response.getWriter().write("ok");
    }
```

- HttpServletRequest를 사용하여 직접 메시지 바디에서 데이터를 읽어와 문자로 변환하는 방식.
- 문자로 된 json 데이터를 mapper를 이용하여 자바 객체로 변환한다.
- 포스트맨으로 테스트시 이상 없이 작동하는 것을 볼 수 있다.

**V2**
```java
@ResponseBody
    @PostMapping("/request-body-json-v2")
    public String requestBodyJsonV2(@RequestBody String messageBody) throws IOException {

        log.info("messageBody: {}", messageBody);
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        log.info("username: {}, age: {}", helloData.getUsername(), helloData.getAge());

        return "ok";
    }
```

- `@RequestBody`를 이용하여 메시지에서 데이터를 꺼내고 메시지 바디에 저장하는 방식.
- 문자로된 json 데이터를 mapper를 통해 자바 객체로 변환한다.
- 처음 코드에 비해서 개선되긴 하였으나 여전히 json 데이터를 변환하는 과정이 복잡하다. 이것을 개선해 보겠다.

**V3**
```java
/**
 * @RequestBody 생략 불가능(@ModelAttribute 가 적용되어 버림)
 * HttpMessageConverter 사용 -> MappingJackson2HttpMessageConverter (content-type:
application/json)
 *
 */
@ResponseBody
@PostMapping("/request-body-json-v3")
public String requestBodyJsonV3(@RequestBody HelloData data) {
 
 log.info("username={}, age={}", data.getUsername(), data.getAge());
 return "ok";
}
```
- `@RequestBody HelloData data` 형식처럼 직접 만든 객체를 지정할 수 있다.
- HttpEntity , @RequestBody 를 사용하면 HTTP 메시지 컨버터가 HTTP 메시지 바디의 내용을 우리가 원하는 문자나 객체 등으로 변환해준다.
- HTTP 메시지 컨버터는 문자 뿐만 아니라 JSON도 객체로 변환해주는데, V2에서 했던 작업을 대신 처리 해주는 것을 알 수 있다.

**V4**
```
@ResponseBody
@PostMapping("/request-body-json-v4")
public String requestBodyJsonV4(HttpEntity<HelloData> httpEntity) {

HelloData data = httpEntity.getBody();
 log.info("username={}, age={}", data.getUsername(), data.getAge());
 return "ok";
}
```
- 앞서 단순 텍스트를 받을 때 처럼`HttpEntity`도 사용이 가능하다.

**V5**
```java
/**
 * @RequestBody 생략 불가능(@ModelAttribute 가 적용되어 버림)
 * HttpMessageConverter 사용 -> MappingJackson2HttpMessageConverter (content-type:
application/json)
 *
 * @ResponseBody 적용
 * - 메시지 바디 정보 직접 반환(view 조회X)
 * - HttpMessageConverter 사용 -> MappingJackson2HttpMessageConverter 적용(Accept:
application/json)
 */
 
@ResponseBody
@PostMapping("/request-body-json-v5")
public HelloData requestBodyJsonV5(@RequestBody HelloData data) {
 
 log.info("username={}, age={}", data.getUsername(), data.getAge());
 return data;
}
```
- 응답의 경우에도 `@ResponseBody`를 이용하여 해당 개체를 http 메시지 바디에 직접 넣어 줄 수 있다. 이 경우에도 `httpentity`사용이 가능하다.
- @RequestBody 요청
	- JSON 요청 -> HTTP 메시지 컨버터 -> 객체
- @ResponseBody 응답
	- 객체 -> HTTP 메시지 컨버터 -> JSON 응답

스프링 mvc에서는 다음의 경우에 HTTP 메시지 컨버터를 적용한다.
- HTTP 요청 : `@RequestBody`, `HttpEntity`
- HTTP 응답 : `@ResponseBody`, `HttpEntity`

## HTTP 메시지 컨버터 

- 메시지 컨버터는 HTTP 요청, 응답 모두에 사용된다. 
- 스프링 부트의 메시지 컨버터는 그 종류가 다양하다. 대상 클래스 타입과 미디어 타입을 체크해서 사용여부를 결정한다. 
- 만약, 만족하지 않는다면 그 다음의 메시지 컨버터로 우선순위가 넘어간다.

스프링 부트의 기본 메시지 컨버터는 다음과 같다.(일부)

`0 = ByteArrayHttpMessageConverter`
`1 = StringHttpMessageConverter`
`2 = MappingJackson2HttpMessageConverter`

### HTTP 요청 데이터 읽기
- HTTP 요청이 오고, 컨트롤러에서 `@RequestBody`, `HTTP Entity` 파라미터 사용.
- 메시지 컨버터가 메시지를 읽을 수 있는지 확인하기 위해 `canRead()` 호출
	- 대상 클래스 타입 지원 여부 확인.
	- HTTP 요청의 Content-Type 미디어 타입 지원 여부 확인.
- `canRead()` 조건 만족시 `read()`를 호출하여 객체 생성 및 반환.

### HTTP 응답 데이터 생성
- 컨트롤러에서 `@ResponseBody`, `HTTP Entity`로 값 반환.
- 메시지 컨버터가 메시지를 쓸 수 있는지 확인하기 위해 `canWrite()` 호출
	- 대상 클래스 타입 지원 여부 확인.
  	- HTTP 요청의 Accept 미디어 타입 지원 여부 확인.
- `canWrite()` 조건 만족시 `write()`를 호출하여 메시지 바디에 데이터 써넣음.

## 요청 매핑 핸들러 어댑터 구조

스프링 mvc에서 HTTP 메시지 컨버터는 `@RequestMapping`을 처리하는 핸들러 어댑터인 요청 매핑 핸들러 어댑터에서 사용된다.

### 요청 매핑 핸들러 어댑터 동작 방식

![](https://velog.velcdn.com/images/gwoprk/post/506a2e8f-3887-44bf-a3da-ce11ebeb9643/image.png)

애노테이션 기반의 컨트롤러는 다양한 파라미터를 유연하게 처리 할 수 있다. 이게 가능한 이유가 바로 위 그림에서 볼 수 있는 `ArgumentResolver`덕분이다.

애노테이션 기반의 컨트롤러를 처리하는 요청 매핑 핸들러 어댑터는 `ArgumentResolver`를 호출하여 컨트롤러가 필요로 하는 다양한 파라미터의 값(객체)를 생성한다. 이렇게 모든 파라미터의 값이 준비되면 컨트롤러를 호출하며 값을 넘겨주는 것이다.

`HandlerMethodReturnValueHandler` 를 줄여서 `ReturnValueHandler` 라 부른다.
`ArgumentResolver` 와 비슷한데, 이것은 응답 값을 변환하고 처리한다.

### HTTP 메시지 컨버터의 위치

![](https://velog.velcdn.com/images/gwoprk/post/bc660ffb-c88a-4ca2-8640-7c1da21d360e/image.png)

- 요청의 경우 `@RequestBody` 를 처리하는 `ArgumentResolver` 가 있고, `HttpEntity` 를 처리하는
`ArgumentResolver` 가 있다. 이 `ArgumentResolver` 들이 HTTP 메시지 컨버터를 사용해서 필요한 객체를 생성 한다.
- 응답의 경우 `@ResponseBody` 와 `HttpEntity` 를 처리하는 `ReturnValueHandler` 가 있다. 그리고 여기에서 HTTP 메시지 컨버터를 호출해서 응답 결과를 생성한다.
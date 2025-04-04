# [Spring MVC] 스프링 MVC 구조

## 1. 기존 프로젝트 구조
![](https://velog.velcdn.com/images/gwoprk/post/f9682dd2-c8a6-4066-ac23-618de3474369/image.png)

---

## 2. Spring MVC 구조
![](https://velog.velcdn.com/images/gwoprk/post/319ace41-6759-496c-b0c8-afd225ddc2d6/image.png)


우리가 여태까지 직접 만든 프레임워크와 스프링 MVC를 비교해보면 사실상 동일한 구조임을 알 수 있다.
약간의 용어의 변화만 존재한다.

### DisPathcerServlet

- 스프링 MVC의 프론트 컨트롤러, 스프링 MVC의 핵심이다.
- DispatcherServlet -> FrameworkServlet -> HttpServletBean -> HttpServlet로 상속받고있다.
- 스프링 부트는 `DispatcherServlet`를 자동으로 등록하면서 모든 경로(`urlPatterns="/"`)에 대해 매핑한다.
	- 더 자세한 경로가 우선순위가 높기때문에 동일한 서블릿도 동작할 수 있다.
    
#### 요청 흐름
1. 서블릿이 호출되면 `HTTPServlet`가 제공하는 `service()`가 호출.
2. 스프링 MVC는 `DisPathcerServlet`의 부모인 `FrameworkServlet`에서 `service()`를 오버라이드 하여 호출.
3. `FrameworkServlet.service()` 를 시작으로 여러 메서드가 호출되면서
`DispatcherServlet.doDispatch()` 가 호출된다.
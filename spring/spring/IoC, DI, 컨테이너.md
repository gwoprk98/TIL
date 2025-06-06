### 제어의 역전;IoC

기존의 프로그램은 구현 객체가 프로그램의 제어 흐름을 스스로 조종한다. 하지만 이후 개선된 코드는 구현 객체는 자신의 로직만을 실행한다. 즉, 필요한 인터페이스를 호출하는 역할을 담당하고는 있지만 실제로 어떤 구현 객체들이 실행될지는 모르는 것이다. 이렇듯 프로그램의 **제어 흐름을 직접 제어하는것이 아니라 외부에서 관리 하는 것을 제어의 역전(IoC)**이라 한다.

```java
public class AppConfig {
	public MemberService memberService() {
		return new MemberServiceImpl(memberRepository());
	}
	
	public OrderService orderService() {
		return new OrderServiceImpl(memberRepository(), discountPolicy());
	}
	
	private MemberRepository memberRepository() {
		return new MemoryMemberRepository();
	}

	public DiscountPolicy discountPolicy() {
//		return new FixDiscountPolicy();
		return new RateDiscountPolicy();
	}
}
```
위와 같은 클래스가 존재하기 때문에 이를 구현한 코드에서는 어떠한 객체가 주입될지 알 필요가 없다.
의존 관계에 대한 고민과 프로그램의 흐름을 직접 제어하는 것이 아니라 외부의 코드에 맡기는 것이다.

### 의존관계 주입;DI
애플리케이션 **실행시점**에 외부에서 실제 구현 객체를 생성하고 클라이언트에 전달하여 클라이언트와 서버의 실제 의존관계가 연결되는 것을 **의존관계 주입**이라 한다. 이러한 의존관계 주입을 사용하면 클라이언트 코드를 변경하지 않고도 호출 대상의 타입 인스턴스를 변경 가능하다. 또한, 정적인 클래스 의존관계를 변경하지 않고도 동적인 객체 인스턴스 의존관계를 쉽게 변경할 수 있다는 장점이 있다.

### 컨테이너
객체를 생성하고 관리하며 의존관계를 연결해주는것을 IoC, DI 컨테이너라고 한다.

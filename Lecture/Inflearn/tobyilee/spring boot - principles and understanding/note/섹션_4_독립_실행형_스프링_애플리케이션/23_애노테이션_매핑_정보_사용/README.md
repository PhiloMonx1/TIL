## [섹션 IV] 23_애노테이션 매핑 정보 사용

---

서블릿 컨테이너 코드를 작성 하는 것이 아닌 컨트롤러 클래스에 어노테이션을 부여하여 매핑 정보를 집어넣는 방법을 써보려고 한다.

### 어노테이션 매핑 정보 사용
```java
@RequestMapping
public class HelloController {
	private final HelloService helloService;

    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }

    @GetMapping("/hello")
    @ResponseBody
    public String hello(String name) {
		return helloService.sayHello(Objects.requireNonNull(name));
	}
}
```
- `@RequestMapping` : HTTP 요청을 특정 컨트롤러 클래스나 핸들러 메서드에 매핑한다. 
  - 클래스 레벨에 `@RequestMapping`이 있으면, Spring은 이 클래스를 웹 요청을 처리할 수 있는 핸들러로 인식한다.
  - 그렇기에 `HelloController` 클래스는 `@Controller` 어노테이션이 없어도 디스패처 서블릿의 매핑을 지원 받을 수 있다.
  - 메소드 레벨에만 `@RequestMapping`를 부여하는 것으로는 부족한데, 스프링이 모든 클래스의 메소드를 다 검색하는 것은 비효율적이기 때문이다.
- `@GetMapping` : `@RequestMapping(value = "/hello", method = RequestMethod.GET)` 의 축약형 어노테이션이다.
- `@ResponseBody` : View 해석 과정을 건너뛰고 직접 HTTP 응답 본문에 데이터를 쓰도록 지시한다.

만약 `@ResponseBody`가 없는 상태로 애플리케이션을 실행하고 API 호출을 하면 404 에러가 발생할 것이다.

그러나 `@ResponseBody`가 없다고 해서 매핑이 실패한 것은 아니다. 매핑은 성공적으로 이루어졌고, 컨트롤러 메서드를 찾았을 것이다. 그럼에도 404 에러가 발생하는 이유는 무엇일까?

### `@ResponseBody`
기본적으로 Spring MVC 는 API 응답을 view라고 불리우는 HTML로 하는 것이 기본 값이다. 
그러나 우리가 작성한 `HelloService.sayHello()` 메서드는 HTML을 리턴하지 않는다.

즉, 404 에러는 적절한 view 를 찾지 못해서 발생하는 것이다. (우리가 리턴하는 문자열의 이름을 view를 찾으려고 시도한다.)

`@ResponseBody` 어노테이션은 기본적으로 API의 응답을 평문 문자열로 표현하도록 한다.
- 클래스 레벨에 부여하는 `@RestController` 어노테이션은 기본적으로 모든 내부 메서드에 `@ResponseBody`가 부여되어 있다고 판단한다.
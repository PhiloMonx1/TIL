## [섹션 IV] 22_DispatcherServlet으로 전환

---

지금까지 작성한 'frontController' 서블릿을 관리하거나 다루는 작업은 꽤나 번거로울 수 있다.

그리고 무엇보다 Servlet Container-less 한 방법이 아니다.

'frontController' 서블릿의 역할은 크게 두 가지이다.
1. 매핑
2. 바인딩

스프링의 도움을 받아서 이 두가지 역할을 수행하는 전략을 더 쉽게 가져갈 수 있다.

### DispatcherServlet
프론트 컨트롤러 패턴을 구현한 스프링 MVC의 핵심 컴포넌트 매핑과 바인딩을 수행하는 컴포넌트이다.

### DispatcherServlet으로 전환
```java
public class HellobootApplication {
    public static void main(String[] args) {
        GenericWebApplicationContext applicationContext = new GenericWebApplicationContext();
        applicationContext.registerBean(HelloController.class);
        applicationContext.registerBean(SimpleHelloService.class);
        applicationContext.refresh();

        ServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();
        WebServer webServer = serverFactory.getWebServer(servletContext -> {
            servletContext.addServlet("dispatcherServlet",
                    new DispatcherServlet(applicationContext) {
            }).addMapping("/*");
        });
        webServer.start();
    }
}
```
- 기존 `HttpServlet`을 `DispatcherServlet`으로 변경한 후 생성자 파라미터로 `applicationContext`를 넣어주었다.
  - `HttpServlet` 내부에 작성된 매핑 관련 코드는 모두 삭제했다.
  - `DispatcherServlet`은 `WebApplicationContext` 타입을 파라미터로 받기 때문에 기존 `GenericApplicationContext`을 `GenericWebApplicationContext`로 변경했다.

그러나 이렇게만 설정하고 스프링 애플리케이션을 실행한 후 API 요청을 보내면 404 Not Found 에러가 발생한다.
- `DispatcherServlet`에게 어떤 오브젝트가 웹 요청 정보를 가지고 들어오면 전달을 해주라는 힌트를 주지 않았기 때문이다.

`DispatcherServlet`에 매핑 정보를 설정하는 방법은 다양하게 있고, 과거에는 XML을 직접 작성하는 번거로운 방법을 사용했지만 이제는 컨트롤러 클래스에 매핑 정보를 설정하는 것이 일반적으로 많이 쓰인다.
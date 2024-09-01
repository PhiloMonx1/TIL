## [섹션 III] 17_Hello 컨트롤러 매핑과 바인딩

---

이전 시간에 프론트 컨트롤러 패턴을 적용해보았다. 그러나 이전 코드에서는 Hello 서블릿에 수행하던 로직이 프론트 컨트롤러 서블릿에 그대로 들어있어 코드의 분리가 필요하다.

```java
public class HelloController {

	public String hello(String name) {
		return "Hello " + name;
	}
}
```
우선 기존에 존재하던 `HelloController` 클래스에서 스프링 관련 어노테이션을 전부 삭제한 후 Hello 서블릿이 수행하던 로직인 "name 값을 받아서 Hello + name 리턴" 부분만 남겨두었다.

```java
public class HellobootApplication {
    public static void main(String[] args) {
        ServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();
        WebServer webServer = serverFactory.getWebServer(servletContext -> {
            HelloController helloController = new HelloController();

            servletContext.addServlet("frontController", new HttpServlet() {
                @Override
                protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
                    //인증, 보안, 다국어 등 공통 기능

                    if(request.getRequestURI().equals("/hello") && request.getMethod().equals(HttpMethod.GET.name())){
                        String name = request.getParameter("name");

                        String returnHello = helloController.hello(name);

                        response.setStatus(HttpStatus.OK.value());
                        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
                        response.getWriter().println(returnHello);
                    } else if (request.getRequestURI().equals("/user")) {
                        //유저 관련 기능
                    } else {
                        response.setStatus(HttpStatus.NOT_FOUND.value());
                    }


                }
            }).addMapping("/*");
        });
        webServer.start();
    }
}
```
- 이후 서블릿 컨테이너에서 `HelloController`의 인스턴스를 생성한 후 
- 프론트 컨트롤러 내부에서 `HelloController`의 `hello()` 메서드를 사용해주었다.
  - 그리고 `hello()` 메서드의 결과 값을 `response.getWriter()`에 담아서 응답으로 사용하였다.

여기서 중요한 점은 `request`를 통해 name 파라미터를 뽑아오는 부분이나 `setStatus`, `setHeader`와 같은 부분은 여전히 프론트 컨트롤러의 책임으로 두었다는 것이다.

### 매핑과 바인딩
우리가 만든 코드가 실제 웹 요청을 받아서 처리하는 동작을 수행하면서 중요한 두 가지 작업이 수행되는데, 그것이 바로 '**매핑**'과 '**바인딩**'이다.

- 매핑 : `if(request.getRequestURI().equals("/hello") && request.getMethod().equals(HttpMethod.GET.name()))` 부분이다.
- 바인딩 : `String returnHello = helloController.hello(name)` 부분이다.
  - 바인딩은 사실 더 복잡하지만 기본적으로는 '웹 요청을 평범한 자바 타입으로 변환하여 전달하는 기술'이다.
  - 실습 코드에서 `HelloController`는 `HttpServletRequest` 타입에 대해 알 필요가 없다. 프론트 컨트롤러가 `HttpServletRequest`의 파라미터를 String 타입으로 변환해서 전달해 주기 때문이다.
    - 폼 데이터나 JSON 데이터를 Java 객체로 자동 매핑하는 경우도 바인딩의 일종이다.
## [섹션 III] 16_프론트 컨트롤러로 전환

---

### 프론트 컨트롤러 패턴적용해서 서블릿 개선하기
```java
public class HellobootApplication {
    public static void main(String[] args) {
        ServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();
        WebServer webServer = serverFactory.getWebServer(servletContext -> {
            servletContext.addServlet("frontController", new HttpServlet() {
                @Override
                protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
                    //인증, 보안, 다국어 등 공통 기능

                    if(request.getRequestURI().equals("/hello") && request.getMethod().equals(HttpMethod.GET.name())){
                        String name = request.getParameter("name");

                        response.setStatus(HttpStatus.OK.value());
                        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
                        response.getWriter().println(String.format("Hello %s", name));
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
1. 기존 서블릿의 이름을 변경한다. hello -> frontController (사실 이름 변경은 필수도 아니고, 로직에 어떠한 영향도 주지 않는다.)
2. 서블릿의 매핑 URL 엔드포인트를 변경한다. "/hello" -> "/*" (/*를 사용해서 모든 URL 요청을 해당 서블릿에서 캐치하도록 한다.)
3. 조건문을 사용해서 URL 엔드포인트에 따라 로직을 분리한다. `request.getRequestURI().equals("/hello")` && 'GET' 메서드의 요청인 경우 기존 Hello 서블릿의 처리 로직을 수행하도록 한다.
4. 조건식에 정의한 어떠한 엔드포인트도 아닐 경우 404 NotFound Http 상태코드를 응답하도록 한다.
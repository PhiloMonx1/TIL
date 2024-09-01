package tobyspring_eh13.helloboot;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

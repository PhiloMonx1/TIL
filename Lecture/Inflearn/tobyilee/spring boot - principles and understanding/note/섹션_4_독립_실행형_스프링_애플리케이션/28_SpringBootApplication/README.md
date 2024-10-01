## [섹션 IV] 28_SpringBootApplication

---

우리가 만든 애플리케이션 코드를 리팩토링 할 것이다.

### 1. 메서드 분리
```java
//... 생략
public class HellobootApplication {
//... 생략
    public static void main(String[] args) {
        run(HellobootApplication.class);
    }

    private static void run(Class<?> applicationClass) {
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext(){
            @Override
            protected void onRefresh() {
                super.onRefresh();

                ServletWebServerFactory serverFactory = getBean(ServletWebServerFactory.class);
                DispatcherServlet dispatcherServlet = getBean(DispatcherServlet.class);

                WebServer webServer = serverFactory.getWebServer(servletContext -> {
                    servletContext.addServlet("dispatcherServlet", dispatcherServlet)
                            .addMapping("/*");
                });
                webServer.start();
            }
        };
        applicationContext.register(applicationClass);
        applicationContext.refresh();
    }
}
```
- `main()` 메서드 내부의 로직을 밖으로 추출해서 `run()` 메서드로 리팩토링 했다.

### 2. 파라미터 추가
```java
public static void main(String[] args) {
    run(HellobootApplication.class, args);
}

private static void run(Class<?> applicationClass, String... args) {
//...생략
}
```
- 커맨드 라인의 args 또한 파라미터로 넣어주었다. (String... 으로 배열을 풀어서 받도록 했다.)

### 3. 클래스 분리
```java
public class MySpringApplication {
    public static void run(Class<?> applicationClass, String[] args) {
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext(){
            @Override
            protected void onRefresh() {
                super.onRefresh();

                ServletWebServerFactory serverFactory = getBean(ServletWebServerFactory.class);
                DispatcherServlet dispatcherServlet = getBean(DispatcherServlet.class);

                WebServer webServer = serverFactory.getWebServer(servletContext -> {
                    servletContext.addServlet("dispatcherServlet", dispatcherServlet)
                            .addMapping("/*");
                });
                webServer.start();
            }
        };
        applicationContext.register(applicationClass);
        applicationContext.refresh();
    }
}

@Configuration
@ComponentScan
public class HellobootApplication {
  //...(생략)
  public static void main(String[] args) {
    MySpringApplication.run(HellobootApplication.class, args);
  }
}
```
- 별도 클래스로 분리하고 사용해주었다.

### 스프링 애플리케이션
```java
package tobyspring_eh13.helloboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HellobootApplication {

	public static void main(String[] args) {
		SpringApplication.run(HellobootApplication.class, args);
	}

}
```

우리가 스프링 프로젝트를 처음 만들었을 때의 코드이다.

```java
@Configuration
@ComponentScan
public class HellobootApplication {
    @Bean
    public ServletWebServerFactory servletWebServerFactory() {
        return new TomcatServletWebServerFactory();
    }

    @Bean
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }

    public static void main(String[] args) {
        SpringApplication.run(HellobootApplication.class, args);
    }
}
```
이제 우리가 만든 `MySpringApplication` 대신 기존 `SpringApplication`을 사용해보자. 정상적으로 동작되는 것을 볼 수 있다.

차이점이라고는 클래스에 부여한 어노테이션과 내부의 Bean 뿐이다.

만약 내부의 Bean 이 없으면 어떻게 될까? `ServletWebServerFactory`를 삭제하면 아쉽게도 애플리케이션이 실행되지 않는다. 
## [섹션 IV] 25_자바코드 구성 정보 사용

---

우리가 만든 오브젝트가 의존성 관계를 맺고 있을 때 이 관계를 어떻게 맺어줄지 스프링에게 구성 정보로 알려주어야 한다.

스프링에게 이와 같은 구성 정보를 알려주는 방법은 여러 가지가 있지만, 우리는 그 중 '팩토리 메서드'를 사용해 볼것이다.

### 팩토리 메서드
```java
public HelloController helloController(HelloService helloService) {
    return new HelloController(helloService);
}

public HelloService HelloService() {
    return new SimpleHelloService();
}
```
- 팩토리 메서드는 단순하다. 그냥 특정 클래스의 인스턴스를 생성자를 호출해서 그 값을 리턴하는 메서드를 만들면 된다.
  - 만약 리턴하고자 하는 클래스가 상속 구현체 일 경우 상위 객체나 인터페이스를 리턴하는 것이 더 유연한 방식으로 여겨진다.
- 팩토리 메서드는 특정한 클래스의 인스턴스를 생성하기 위해서 복잡한 관계의 의존성이나 설정이 필요할 경우 유용하게 사용할 수 있다.
  - 또한 직접 인스턴스 생성을 막고 팩토리 메서드를 통해서만 인스턴스가 생성되도록 해서 싱글턴 패턴을 구현하는데 사용할 수 있다.

### 스프링 어노테이션으로 구성 정보 알려주기

```java
@Configuration
public class HellobootApplication {

    @Bean
    public HelloController helloController(HelloService helloService) {
        return new HelloController(helloService);
    }

    @Bean
    public HelloService HelloService() {
        return new SimpleHelloService();
    }

    public static void main(String[] args) {
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext(){
            @Override
            protected void onRefresh() {
                super.onRefresh();

                ServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();
                WebServer webServer = serverFactory.getWebServer(servletContext -> {
                    servletContext.addServlet("dispatcherServlet",
                            new DispatcherServlet(this) {
                            }).addMapping("/*");
                });
                webServer.start();
            }
        };
        applicationContext.register(HelloController.class);
        applicationContext.refresh();
    }
}
```
- `@Bean` : 해당 어노테이션을 부여하여 스프링에게 자바 객체가 Bean으로 관리되어야 함을 알린다.
- `@Configuration` : 어노테이션을 클래스에 부여하여 해당 클래스 내부에 Bean으로 관리되어야 하는 객체나 스프링 설정이 포함되어 있음을 알린다. (스캔 대상임을 알리는 것)
- `AnnotationConfigWebApplicationContext` : 기존에 사용하던 `GenericWebApplicationContext`은 어노테이션 기반 구성 정보를 읽지 못한다.
  - `applicationContext.register()` 를 통해 Bean이 아닌 Bean을 포함하는 클래스 자체를 알려주어서 자동으로 Bean을 등록하도록 한다.
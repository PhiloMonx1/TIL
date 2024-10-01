## [섹션 IV] 24_스프링 컨테이너로 통합

---

```java
package tobyspring_eh13.helloboot;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;


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

지금까지 작성한 `HellobootApplication` 코드는 크게 두 파트로 구분되어 있다.
1. Spring Container를 생성하고, Bean을 등록해서 초기화
2. 앞서 만들어진 Spring Container를 활용해서 Dispatcher Servlet과 WebServer를 생성하고 시작
   - 메인 메서드에서 직접 웹 서버 설정과 시작을 수행하는 방식 (애플리케이션 컨텍스트와 웹 서버의 생명주기가 분리)

이번 시간에는 1, 2번 과정을 통합해서 Spring Container가 초기화될 때 Dispatcher Servlet과 WebServer의 초기화를 함께 진행하도록 리팩토링할 것이다.

### 리팩토링
```java
public class HellobootApplication {
    public static void main(String[] args) {
        GenericWebApplicationContext applicationContext = new GenericWebApplicationContext(){
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
        applicationContext.registerBean(HelloController.class);
        applicationContext.registerBean(SimpleHelloService.class);
        applicationContext.refresh();
    }
}
```
- `GenericWebApplicationContext`를 익명클래스로 확장하여 `onRefresh()` 메서드를 오버라이드했다.
  - `onRefresh()` : 컨테이너 초기화 시점에 커스텀 로직을 삽입하기 위한 hook 메서드
- 웹 서버 설정과 시작 로직을 `onRefresh()` 메서드 내부로 이동시켰다.
- `applicationContext.refresh()`를 호출하면 자동으로 `onRefresh()` 메서드가 실행되어 웹 서버가 시작된다.
  - `super.onRefresh();` 를 빼먹으면 안된다. 스프링 컨테이너에서 진행하는 로직이 있기 때문에 누락할 경우 정상 동작이 보장되지 않기 때문이다.

### 템플릿 메서드와 Hook
`applicationContext.refresh()` 내부를 살펴보면 템플릿 메서드로 이루어져 있다는 것을 알 수 있다.
- 템플릿 메서드 : 알고리즘을 단계별로 구현한 후에 이를 상속해서 하위 단계를 추가하거나 기존 단계를 변경하는 방식을 의미한다.

### 템플릿 메서드 예시
```java
abstract class CoffeeMaker {
    
    // 템플릿 메서드
    public final void makeCoffee() {
        boilWater();
        brewCoffeeGrounds();
        pourInCup();
        addCondiments();
    }

    // 공통 메서드
    private void boilWater() {
        System.out.println("물 끓이기.");
    }

    private void pourInCup() {
        System.out.println("컵에 붇기.");
    }

    // 추상 메서드 (Hook 메서드)
    protected abstract void brewCoffeeGrounds();
    protected abstract void addCondiments();
}

// 구체적인 서브 클래스: 아메리카노
class AmericanoCoffeeMaker extends CoffeeMaker {
    @Override
    protected void brewCoffeeGrounds() {
        System.out.println("에스프레소 추출.");
    }

    @Override
    protected void addCondiments() {
        System.out.println("뜨거운 물 추가.");
    }
}

// 구체적인 서브 클래스: 라떼
class LatteCoffeeMaker extends CoffeeMaker {
    @Override
    protected void brewCoffeeGrounds() {
        System.out.println("에스프레소 추출.");
    }

    @Override
    protected void addCondiments() {
        System.out.println("스팀 밀크 추가.");
    }
}
```
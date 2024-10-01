## [섹션 VI] 39_@Configuration과 proxyBeanMethods

### 팩토리 메서드와 인스턴스
```java
public class ConfigurationTest {
    @Test
    void configuration() {
        MyConfig myConfig = new MyConfig();
        Bean1 bean1 = myConfig.bean1();
        Bean2 bean2 = myConfig.bean2();

        Assertions.assertThat(bean1.common).isSameAs(bean2.common);
    }

    @Configuration
    static class MyConfig {
        @Bean
        Common common() { 
            return new Common();
        }

        @Bean
        Bean1 bean1() {
            return new Bean1(common());
        }

        @Bean
        Bean2 bean2() {
            return new Bean2(common());
        }

    }

    static class Bean1 {
        private final Common common;

        Bean1(Common common) {
            this.common = common;
        }
    }

    static class Bean2 {
        private final Common common;

        Bean2(Common common) {
            this.common = common;
        }
    }

    private static class Common {
    }
}

```
- `Assertions.assertThat(bean1.common).isSameAs(bean2.common);` 테스트 케이스는 실패한다.
  - bean1 과 2 모두 `common()` 메서드를 통해 `Common` 클래스를 주입 받는데, `common()` 메서드는 new를 통해 새로운 인스턴스를 만들기 때문이다.
    - `common()` 메서드는 일종의 팩토리 메서드이다.

### `@Configuration`와 `proxyBeanMethods`
```java
@Test
void configuration() {
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
    applicationContext.register(MyConfig.class);
    applicationContext.refresh();

    Bean1 bean1 = applicationContext.getBean(Bean1.class);
    Bean2 bean2 = applicationContext.getBean(Bean2.class);

    Assertions.assertThat(bean1.common).isSameAs(bean2.common);
}
```

그런데 이와 같이 스프링 컨테이너가 Bean으로 관리하도록 하는 경우 테스트 케이스가 성공한다. 즉, bean1 과 2 동일한 인스턴스를 주입 받은 것이다.

다른 코드는 전혀 변하지 않았다. 그저 스프링 컨테이너가 관리하도록 했을 뿐이다. `MyConfig` 클래스에 부여된 `@Configuration` 어노테이션 덕분이다.

기본적으로 `@Configuration`의 `proxyBeanMethods`가 디폴트 값인 'true' 로 설정되어 있을 때, 

`MyConfig` 클래스는 직접 Bean으로 등록되는 것이 아닌 프록시 객체를 통해 Bean으로 등록된다.

### 커스텀 프록시 객체
```java
static class MyConfigProxy extends MyConfig {
    private Common common;
    @Override
    Common common() {
        if (common == null) {
            this.common = super.common();
        }
        return this.common;
    }
}
```
- 스프링 컨테이너 처럼 우리도 `MyConfig`의 프록시 객체를 선언해보았다.
  - `MyConfig`을 확장했다.
  - `common()` 팩토리 메서드를 오버라이딩 했다.
  - `Common common` 타입의 필드를 생성했다.
    - 만약 필드가 null 이라면 부모 클래스인 `MyConfig`의 `common()` 메서드를 호출해서 필드를 채운다.
    - null이 아니라면 해당 필드를 리턴한다.
    - 즉, `new Common();`은 단 한 번 실행된다.

### MyConfigProxy 테스트 
```java
    @Test
    void proxyCommonMethod() {
        MyConfigProxy myConfigProxy = new MyConfigProxy();

        Bean1 bean1 = myConfigProxy.bean1();
        Bean2 bean2 = myConfigProxy.bean2();

        Assertions.assertThat(bean1.common).isSameAs(bean2.common);
    }
```
- Proxy는 데코레이터 처럼 타깃 오브젝트를 따로 두고 중간에 끼어드는 방식으로 동작하는 것이 아닌 아예 대체하는 방식으로 동작한다.

### 스프링 컨테이너의 `@Configuration` 프록시 객체 정리
- 스프링 컨테이너는 `MyConfig` 클래스를 직접 빈으로 등록하지 않고, 이를 상속한 프록시 클래스를 생성한다.
- 이 프록시 클래스는 `MyConfig`의 모든 `@Bean` 메서드를 오버라이드 한다.
- 프록시는 `@Bean` 메서드 호출을 가로채고 관리한다. (같은 빈에 대한 여러 번의 호출이 있어도 항상 동일한 인스턴스를 반환)

### `@Configuration(proxyBeanMethods = false)`
```java
@Configuration(proxyBeanMethods = false)
static class MyConfig {
    @Bean
    Common common() {
        return new Common();
    }

    @Bean
    Bean1 bean1() {
        return new Bean1(common());
    }

    @Bean
    Bean2 bean2() {
        return new Bean2(common());
    }

}
```

만약 `@Configuration(proxyBeanMethods = false)`를 통해 명시적으로 `proxyBeanMethods`를 사용하지 않겠다고 선언하면 이전에 진행했던 `configuration()` 메서드는 실패한다.

이전에는 `@Configuration(proxyBeanMethods = false)`를 권장하지 않았으나, 의존성이 없는 객체를 Bean으로 등록하는 경우에는 `@Configuration(proxyBeanMethods = false)`을 사용하는 것도 고려할 수 있다.(스프링부트의 클래스 중에서도 proxyBeanMethods를 false로 구현하는 경우가 있다.)
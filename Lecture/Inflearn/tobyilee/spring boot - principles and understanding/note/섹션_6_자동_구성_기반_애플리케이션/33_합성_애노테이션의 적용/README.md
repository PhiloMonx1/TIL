## [섹션 VI] 33_합성 애노테이션의 적용

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
지금까지 작성한 코드이다. 스프링부트에서 기본으로 제공하는 코드와 다른 부분을 개선해 볼 것이다,

### 1. 합성 어노테이션 적용
```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Configuration
@ComponentScan
public @interface MySpringBootAnnotation {
}
```
스프링 부트와 같이 하나의 어노테이션만 부여하도록 합성 어노테이션을 만들었다.
- `@Retention(RetentionPolicy.RUNTIME)` : 기본적으로는 Class 로 적용되는데, class 까지는 어노테이션이 살아있지만 런타임 시 어노테이션 정보가 사라진다. 그렇기에 런타임으로 명시해주었다.
- `@Target(ElementType.TYPE)` : 클래스, 인터페이스, Enum 타입에 적용을 하기 위한 메타 어노테이션이다.


### 2. Bean 메서드 클래스 분리
```java
@Configuration
public class Config {
    @Bean
    public ServletWebServerFactory servletWebServerFactory() {
        return new TomcatServletWebServerFactory();
    }

    @Bean
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }
}
```
- `@Configuration` : 내부에 `@Component`를 메타 어노테이션으로 가지고 있는 어노테이션이다. 설정 클래스라는 점을 명시하기 위해 부여하였다.

### 최종 코드
```java
@MySpringBootAnnotation
public class HellobootApplication {
    public static void main(String[] args) {
        SpringApplication.run(HellobootApplication.class, args);
    }
}
```

스프링부트 프로젝트를 처음 시작할 때의 애플리케이션 코드와 유사한 코드가 완성되었다.
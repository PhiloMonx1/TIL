## [섹션 VIII] 48_@Value와 PropertySourcesPlaceholderConfigurer

### 클래스 필드로 Property 사용하기
```java
@MyAutoConfiguration
@ConditionalMyOnClass("org.apache.catalina.startup.Tomcat")
public class TomcatWebServerConfig {
    @Value("${contextPath}")
    String contextPath;

    @Bean("TomcatWebServerConfig")
    @ConditionalOnMissingBean
    public ServletWebServerFactory servletWebServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.setContextPath(this.contextPath);
        return factory;
    }
}
```
- 기존 `Environment`를 파라미터로 받지 않고 `contextPath` 필드를 추가해서 처리해보았다.
  - `@Value` : 스프링 빈의 필드나 메서드 파라미터에 주입하는 데 사용되는 어노테이션이다. (`${...}` 문법은 'Property Placeholder' 를 나타낸다.)

```
Caused by: java.lang.IllegalArgumentException: ContextPath must start with '/' and not end with '/'
	at org.springframework.boot.web.servlet.server.AbstractServletWebServerFactory.checkContextPath(AbstractServletWebServerFactory.java:140) ~[spring-boot-2.7.6.jar:2.7.6]
	at org.springframework.boot.web.servlet.server.AbstractServletWebServerFactory.setContextPath(AbstractServletWebServerFactory.java:129) ~[spring-boot-2.7.6.jar:2.7.6]
	at tobyspring_eh13.config.autoconfig.TomcatWebServerConfig.servletWebServerFactory(TomcatWebServerConfig.java:22) ~[main/:na]
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:na]
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[na:na]
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:na]
	at java.base/java.lang.reflect.Method.invoke(Method.java:566) ~[na:na]
	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:154) ~[spring-beans-5.3.24.jar:5.3.24]
	... 21 common frames omitted
```
그러나 실제로는 이렇게 설정해도 에러가 발생한다. "ContextPath must start with '/' and not end with '/'" 'ContextPath' 는 '/' 으로 시작해야 한다고 하고 있다.

sout 을 통해 `contextPath` 필드를 콘솔에 출력해보면 `${contextPath}` 라는 문자열이 그대로 들어오는 것을 확인할 수 있다.

사실 'Property Placeholder' 를 사용하기 위해서 스프링 부트가 수행하는 설정이 있다.

### PropertySourcesPlaceholderConfigurer
```java
@MyAutoConfiguration
public class PropertyPlaceholderConfig {
    @Bean
    PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
```
- 자동 설정으로 `PropertySourcesPlaceholderConfigurer` 타입의 Bean 을 리턴하는 `PropertyPlaceholderConfig` 클래스를 등록했다.

```
tobyspring_eh13.config.autoconfig.PropertyPlaceholderConfig
```
- import 파일에도 자동 설정 클래스를 불러올 수 있도록 넣어주었다.

이제 정상적으로 서버가 동작하는 것을 확인할 수 있다.
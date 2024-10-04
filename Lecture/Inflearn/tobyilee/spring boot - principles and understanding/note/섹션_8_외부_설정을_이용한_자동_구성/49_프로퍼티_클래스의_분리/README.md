## [섹션 VIII] 49_프로퍼티 클래스의 분리

자동 구성 클래스 내부에 필드로 프로퍼티를 선언하는 것은 중복 코드가 발생할 수 있는 구조이다. 

`TomcatWebServerConfig`에 쓰이는 프로퍼티 필드가 `JettyWebServerConfig`에도 똑같이 쓰일 수 있기 때문이다.

### port 프로퍼티 추가 및 디폴트 값 적용
```java
@MyAutoConfiguration
@ConditionalMyOnClass("org.apache.catalina.startup.Tomcat")
public class TomcatWebServerConfig {
    @Value("${contextPath:}")
    String contextPath;

    @Value("${port:8080}")
    int port;

    @Bean("TomcatWebServerConfig")
    @ConditionalOnMissingBean
    public ServletWebServerFactory servletWebServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();

        factory.setContextPath(this.contextPath);
        factory.setPort(this.port);

        return factory;
    }
}
```
- `@Value("${port:8080}")` 를 통해 port 프로퍼티를 지정했으며, 8080 값을 디폴트로 부여했다.
  - 'contextPath' 의 경우 ':' 뒤에 아무것도 작성하지 않아 디폴트 값을 빈 값으로 설정했다.

이제 해당 코드를 별도 프로퍼티 클래스로 분리해보겠다.

### 프로퍼티 클래스로 분리
```java
public class ServerProperties {
    private String contextPath;

    private int port;

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
```
- `ServerProperties` 클래스를 생성하고 필드로 프로퍼티를 주었다.

```java
@MyAutoConfiguration
public class ServerPropertiesConfig {
    @Bean
    public ServerProperties serverProperties(Environment env) {
        ServerProperties serverProperties = new ServerProperties();

        serverProperties.setContextPath(env.getProperty("contextPath"));
        serverProperties.setPort(Integer.parseInt(env.getProperty("port")));

        return serverProperties;
    }
}
```
- `ServerPropertiesConfig` 클래스를 생성하고 자동 설정으로 등록했다. (import 파일 내에도 등록했다.)

```java
@MyAutoConfiguration
@ConditionalMyOnClass("org.apache.catalina.startup.Tomcat")
public class TomcatWebServerConfig {
    @Bean("TomcatWebServerConfig")
    @ConditionalOnMissingBean
    public ServletWebServerFactory servletWebServerFactory(ServerProperties properties) {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();

        factory.setContextPath(properties.getContextPath());
        factory.setPort(properties.getPort());

        return factory;
    }
}
```
- `ServerProperties`를 생성자 주입으로 처리해준다.

### Binder 사용해서 개선
```java
@MyAutoConfiguration
public class ServerPropertiesConfig {
    @Bean
    public ServerProperties serverProperties(Environment env) {
        return Binder.get(env).bind("", ServerProperties.class).get();
    }
}
```
- Binder 를 사용하면 다양한 프로퍼티를 자동으로 연결 시킬 수 있따.
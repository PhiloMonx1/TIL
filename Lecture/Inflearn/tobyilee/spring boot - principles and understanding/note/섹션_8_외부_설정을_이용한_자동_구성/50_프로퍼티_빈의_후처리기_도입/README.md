## [섹션 VIII] 50_프로퍼티 빈의 후처리기 도입

지난 시간에 생성한 클래스를 살펴보면, 간단한 변경 사항을 위해 꽤나 많은 코드를 수정했으며, 두 개의 클래스를 생성했음을 볼 수 있다.

또한 해당 추가한 프로퍼티 경우 서블릿 컨테이너를 띄울 때만 쓰이기 때문에 `ServerPropertiesConfig` 클래스에도 Conditional 을 부여해주어야 하는데 이 경우 조건이 더 복잡하다. (Tomcat, Jetty 등 여러 서블릿 컨테이너의 경우를 따져야 하기 때문이다.)

지금부터 코드를 개선해 보도록 하겠다.

```java
@Component
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

```java
@MyAutoConfiguration
@ConditionalMyOnClass("org.apache.catalina.startup.Tomcat")
@Import(ServerProperties.class)
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

이렇게 작성을 한 후 애플리케이션을 실행하면 이전과는 다른 에러가 발생한다.

```
org.springframework.context.ApplicationContextException: Unable to start web server; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'TomcatWebServerConfig' defined in class path resource [tobyspring_eh13/config/autoconfig/TomcatWebServerConfig.class]: Bean instantiation via factory method failed; nested exception is org.springframework.beans.BeanInstantiationException: Failed to instantiate [org.springframework.boot.web.servlet.server.ServletWebServerFactory]: Factory method 'servletWebServerFactory' threw exception; nested exception is java.lang.IllegalArgumentException: ContextPath must not be null
```
- `IllegalArgumentException` 에러가 발생하며 해석하자면 `ServerProperties`는 찾았으나, `getContextPath()`를 실행할 때 `ContextPath`이 null 로 들어오고 있음을 지적하고 있다.

### 1단계. Bean Post Processor
'Post Processor' 를 통해 스프링 Bean 을 생성하고, 의존성 주입까지 모두 마친 후 즉 초기화 후 Bean 오브젝트를 가공할 수 있는 기회를 가질 수 있다.
```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface MyConfigurationProperties {
}
```
- 해당 어노테이션을 생성 후 `ServerProperties`에 부여했다. (일종의 태그라고 생각하면 된다.)

```java
@MyAutoConfiguration
public class PropertyPostProcessorConfig {
    @Bean
    BeanPostProcessor propertyPostProcessor(Environment env) {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) {
                MyConfigurationProperties annotation =
                        AnnotationUtils.findAnnotation(bean.getClass(), MyConfigurationProperties.class);

                if (annotation == null) {
                    return bean;
                }

                return Binder.get(env).bindOrCreate("", bean.getClass());
            }
        };
    }
}
```
- `postProcessBeforeInitialization` : `BeanPostProcessor` 인터페이스를 구현할 때 오버라이딩 해야 하는 메서드로 Bean 의 초기화 전 프로세스를 진행한다.
- `MyConfigurationProperties` : 자바의 리플렉스를 사용해서 특정 Bean 의 어노테이션을 가져올 수 있다.
- `(annotation == null)` : 만약 Bean 에 `@MyConfigurationProperties` 어노테이션이 부여되지 않았다면 해당 Bean 을 그냥 리턴한다.
- `Binder.get(env).bindOrCreate("", bean.getClass())` : `bindOrCreate()` 를 통해 바인딩 하지 못한 경우 Bean 을 그냥 리턴하도록 했다.
  - 첫 번째 파라미터로 주어진 ""는 프로퍼티의 접두사를 의미하는데, 빈 값으로 두어 특정 접두사를 지정하지 않고 전체 프로퍼티를 대상으로 바인딩하도록 했다.

### 2단계. 접두사(prefix) 시용
```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface MyConfigurationProperties {
    String prefix();
}
```
- `MyConfigurationProperties` 어노테이션에 `prefix` 라는 어노테이션 속성를 선언했다.

```java
@MyConfigurationProperties(prefix = "server")
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
- 사용하는 쪽에서 "server"로 명시했다.

```properties
server.contextPath=/app
server.port=9090
```
- properties 를 작성할 때도 "server."를 접두사로 붙여준다.

```java
@MyAutoConfiguration
public class PropertyPostProcessorConfig {
    @Bean
    BeanPostProcessor propertyPostProcessor(Environment env) {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) {
                MyConfigurationProperties annotation =
                        AnnotationUtils.findAnnotation(bean.getClass(), MyConfigurationProperties.class);

                if (annotation == null) {
                    return bean;
                }

                Map<String, Object> attrs = AnnotationUtils.getAnnotationAttributes(annotation);
                String prefix = (String) attrs.get("prefix");

                return Binder.get(env).bindOrCreate(prefix, bean.getClass());
            }
        };
    }
}
```
- `getAnnotationAttributes()` : 어노테이션의 모든 속성를 가져온다.
- `prefix` : 속성을 바인딩 접두사에 넣어주었다.

### import 부분 개선하기
```java
@MyAutoConfiguration
@ConditionalMyOnClass("org.apache.catalina.startup.Tomcat")
@EnableMyConfigurationProperties(ServerProperties.class)
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

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(MyConfigurationPropertiesImportSelector.class)
public @interface EnableMyConfigurationProperties {
    Class<?> value();
}
```

```java
public class MyConfigurationPropertiesImportSelector implements DeferredImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        MultiValueMap<String, Object> attrs
                = importingClassMetadata.getAllAnnotationAttributes(EnableMyConfigurationProperties.class.getName());
        Class propertyClass = (Class) attrs.getFirst("value");
        return new String[] {propertyClass.getName()};
    }
}
```

`DeferredImportSelector`를 통해 동적으로 임포트할 클래스를 선택하도록 만들어. 보다 유연한 구조로 Import 를 사용할 수 있도록 개선했다. 
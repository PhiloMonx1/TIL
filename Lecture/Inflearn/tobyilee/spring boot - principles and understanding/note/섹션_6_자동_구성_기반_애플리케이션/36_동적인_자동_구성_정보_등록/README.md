## [섹션 VI] 36_동적인 자동 구성 정보 등록

우리가 이전에 만든 `@EnableMyAutoConfiguration` 어노테이션은 이름으로 자동 설정을 지원하는 것을 암시한다.

그러나 그 내부는 `@Import({DispatcherServletConfig.class, TomcatWebServerConfig.class})`을 하드코딩으로 적어놓았을 뿐이다.

이 두 가지 클래스가 모든 스프링부트 프로젝트에 사용된다면 문제가 없겠지만, 그렇지 않다. 웹 개발이 아닌 경우에는 두 클래스가 필요 없을 수 있기 때문이다.

그래서 어떠한 Configuration 이 있다고 할 때, 클래스로 만들어 놓은 구성 정보를 동적으로 추가하는 매커니즘이 필요하다.

### Import selector
```java
public interface ImportSelector {
    String[] selectImports(AnnotationMetadata importingClassMetadata);

    @Nullable
    default Predicate<String> getExclusionFilter() {
        return null;
    }
}
```
- 동적으로 설정 클래스를 선택하고 가져오는 데 사용되는 인터페이스, 런타임에 어떤 설정 클래스를 가져올지 결정할 수 있다.
- 구현을 위해서는 내부의 `selectImports()` 메서드를 오버라이드 해야 한다.
  - `selectImports()` 메서드는 가져올 설정 클래스의 이름을 문자열 배열로 반환한다.

### Import selector 구현
```java
public class MyAutoConfigImportSelector implements DeferredImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{
                "tobyspring_eh13.config.autoconfig.DispatcherServletConfig",
                "tobyspring_eh13.config.autoconfig.TomcatWebServerConfig",
        };
    }
}
```
- `selectImports()` 메서드를 오버라이드 해서 `DispatcherServletConfig`, `TomcatWebServerConfig` 클래스를 문자열로 리턴하도록 설정했다.

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(MyAutoConfigImportSelector.class)
public @interface EnableMyAutoConfiguration {
}
```
`@EnableMyAutoConfiguration` 어노테이션에서는 두 개의 클래스 대신 `@Import(MyAutoConfigImportSelector.class)`로 사용할 수 있다.
## [섹션 VI] 37_자동 구성 정보 파일 분리


### 신규 어노테이션 추가
```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Configuration
public @interface MyAutoConfiguration {
}
```
- `@Configuration`을 메타 어노테이션으로 가지는 새로운 어노테이션을 작성했다.

### `MyAutoConfigImportSelector.selectImports()` 변경
```java
@Override
public class MyAutoConfigImportSelector implements DeferredImportSelector {
    private final ClassLoader classLoader;

    public MyAutoConfigImportSelector(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        List<String> autoConfigs = new ArrayList<>();

        Iterable<String> candidates = ImportCandidates.load(MyAutoConfiguration.class, classLoader);

        for (String candidate : candidates) {
            autoConfigs.add(candidate);
        }

        return autoConfigs.toArray(new String[0]);
    }
}
```
- `ImportCandidates`의 `load()` 메서드를 사용하였다.
  - `@MyAutoConfiguration` 어노테이션 클래스와, `ClassLoader`를 인자로 넘겨주었다.
    - `ClassLoader` :  Java 런타임 환경에서 클래스와 인터페이스를 동적으로 로드하는 역할을 수행한다. 현재 코드에서는 `@MyAutoConfiguration` 어노테이션 클래스와 관련된 설정을 동적으로 로드하는 데 사용된다.

```java
@Override
public String[] selectImports(AnnotationMetadata importingClassMetadata) {
    Iterable<String> candidates = ImportCandidates.load(MyAutoConfiguration.class, classLoder);
    return StreamSupport.stream(candidates.spliterator(), false).toArray(String[]::new);
}
```
- 이와 같이 `StreamSupport`를 사용할 수도 있다.

### ImportCandidates
자동 구성(Auto-configuration)을 위한 후보 클래스들을 로드하는 데 사용되는 클래스

위 코드에서 `ImportCandidates.load()`는 `@MyAutoConfiguration` 어노테이션 클래스와 관련된 모든 자동 구성 후보들을 로드한다.

> Loads the names of import candidates from the classpath. 
> The names of the import candidates are stored in files named META-INF/ spring/ full-qualified-annotation-name. 
> imports on the classpath. Every line contains the full qualified name of the candidate class. 
> Comments are supported using the # character.

`ImportCandidates.load()` 메서드의 주석 내용이다. 해석 하자면 ` META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` 파일에서 설정 정보를 읽어온다고 한다.

### 설정 파일 생성
프로젝트 `resources` 경로에 `META-INF/spring` 경로를 생성한다. `tobyspring_eh13.config.MyAutoConfiguration.imports` 파일을 생성한다. (`imports`가 파일의 확장자이다.)
  - tobyspring_eh13.config 부분은 프로젝트의 패키지명이다.

```
tobyspring_eh13.config.autoconfig.DispatcherServletConfig
tobyspring_eh13.config.autoconfig.TomcatWebServerConfig
```
파일 내부에 위와 같이 자동 설정을 원하는 클래스를 패키지명을 포함해서 작성한다.
- 인텔리제이에서는 자동완성을 지원한다.

이렇게 설정 클래스를 외부 파일로 유연하게 등록할 수 있게 되었다.
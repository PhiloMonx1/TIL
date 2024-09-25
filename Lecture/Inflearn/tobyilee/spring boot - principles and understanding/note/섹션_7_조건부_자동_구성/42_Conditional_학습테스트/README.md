## [섹션 VII] 42_@Conditional 학습테스트

아래의 테스트를 구현해 볼 것이다. 간단하게 말하자면, `ConfigTrue`, `ConfigFalse`는 `MyBean` Bean 메소드를 가진 설정 클래스이다.

`ConfigTrue`과 `ConfigFalse`에 따라 `MyBean`은 등록될 수도 등록되지 않을 수도 있다.

```java
public class ConditionalTest {

    @Test
    void conditional() {
        // true
        ApplicationContextRunner runner = new ApplicationContextRunner();
        runner.withUserConfiguration(ConfigTrue.class)
                .run(context -> {
                    assertThat(context).hasSingleBean(MyBean.class);
                    assertThat(context).hasSingleBean(ConfigTrue.class);
                });

        // false
        new ApplicationContextRunner().withUserConfiguration(ConfigFalse.class)
                .run(context -> {
                    assertThat(context).doesNotHaveBean(MyBean.class);
                    assertThat(context).doesNotHaveBean(ConfigTrue.class);
                });
    }
}
```
- `ApplicationContextRunner`를 통해서 테스트를 편하게 진행할 수 있다.
- `hasSingleBean` : Bean 이 등록되어 있는지를 판단한다.
- `doesNotHaveBean` : Bean 이 등록되어 있지 않은지를 판단한다.

### 실습 1 : `Condition` 인터페이스를 상속하는 컨디션 클래스 생성
```java
@Configuration
@Conditional(TrueCondition.class)
static class ConfigTrue {
    @Bean
    MyBean myBean() {
        return new MyBean();
    }
}

@Configuration
@Conditional(FalseCondition.class)
static class ConfigFalse {
    @Bean
    MyBean myBean() {
        return new MyBean();
    }
}

static class MyBean {}

private static class TrueCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return true;
    }
}

private static class FalseCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return false;
    }
}
```
- `TrueCondition`, `FalseCondition` 을 생성해서 `@Conditional` 어노테이션의 파라미터로 넣어주었다.
  - `@Conditional` 어노테이션의 파라미터는 boolean 값을 판단하기 때문에 각각 true, false 를 리턴하도록 해주었다.

### 실습 2 : `@Conditional`을 메타어노테이션으로 사용하는 어노테이션을 만들어서 명시
```java
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Conditional(TrueCondition.class)
    @interface TrueConditional {

    }

    @Configuration
    @TrueConditional
    static class ConfigTrue {
        @Bean
        MyBean myBean() {
            return new MyBean();
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Conditional(FalseCondition.class)
    @interface FalseConditional {

    }

    @Configuration
    @FalseConditional
    static class ConfigFalse {
        @Bean
        MyBean myBean() {
            return new MyBean();
        }
    }

    static class MyBean {}

    private static class TrueCondition implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return true;
        }
    }

    private static class FalseCondition implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return false;
        }
    }
```
- `@Conditional()` 메타 어노테이션으로 사용하는 각각의 어노테이션을 만들어서 명시적으로 부여했다.

### 실습 3 : `AnnotatedTypeMetadata`의 `getAnnotationAttributes()` 사용해서 조건부 설정 구현
```java
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Conditional(BooleanCondition.class)
    @interface BooleanConditional {
        boolean value(); //단일 속성일 경우 속성 이름이 "value"라면 사용하는 쪽에서 속성명을 명시하지 않아도 된다.
    }

    @Configuration
    @BooleanConditional(true) //속성 명을 명시하지 않고 바로 값을 입력했다.
    static class ConfigTrue {
        @Bean
        MyBean myBean() {
            return new MyBean();
        }
    }

    @Configuration
    @BooleanConditional(false)
    static class ConfigFalse {
        @Bean
        MyBean myBean() {
            return new MyBean();
        }
    }

    static class MyBean {}

    private static class BooleanCondition implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(BooleanConditional.class.getName());
            Boolean value = (Boolean) annotationAttributes.get("value");
            return value;
        }
    }
```
- 지금까지 분리되어 있던 어노테이션을 `BooleanCondition`을 통해 `@BooleanConditional` 하나로 통일했다.
  - `BooleanCondition` 클래스의 `matches()` 메소드는 true, false 를 리턴한다.
  - `metadata.getAnnotationAttributes(BooleanConditional.class.getName())` : 어노테이션의 속성 값을 Map 타입으로 가져왔다.
  - `annotationAttributes.get("value")` : `value` 속성의 값을 반환하도록 했다.
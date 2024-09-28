## [섹션 VII] 45_스프링 부트의 @Conditional

### @Profile
Spring Framework 의 `@Profile`도 `@Conditional` 어노테이션을 메타 어노테이션으로 가지고 있다.
- Spring Boot 의 `@Conditional` 아이디어를 Spring 에서 가져와서 사용한 것.

### Class Conditions
- `@ConditionalOnClass`
- `@ConditionalOnMissingClass`

지정한 클래스의 프로젝트내 존재를 확인해서 포함 여부를 결정한다.
- 주로 `@Configuration` 클래스 레벨에서 사용하지만 `@Bean` 메소드에도 적용 가능하다. 
  - 단, 클래스 레벨의 검증 없이 `@Bean` 메소드에만 적용하면 불필요하게 @Configuration 클래스가 Bean 으로 등록되기 때문에, 클래스 레벨 사용을 우선해야 한다.

### Bean Conditions
- `@ConditionalOnBean`
- `@ConditionalOnMissingBean`

Bean 의 존재 여부를 기준으로 포함여부를 결정한다. Bean 의 타입 또는 이름을 지정할 수 있다. 지정된 Bean 정보가 없으면 메 소드의 리턴 타입을 기준으로 Bean 의 존재여부를 체크한다.
- 컨테이너에 등록된 Bean  정보를 기준으로 체크하기 때문에 자동 구성 사이에 적용하려면 @Configuration 클래스의 적용 순서가 중요하다. 
  - 개발자가 직접 정의한 커스텀 Bean 구성 정보가 자동 구성 정보 처리보다 우선하기 때문에 커스텀 Bean 구성 정보에 적용하는 것은 피해야 한다.

클래스 레벨에서 `@ConditionalOnClass`를 통해 체크하고 `@Bean` 메소드 레벨에서 `@ConditionalOnMissingBean`를 통해 체크하는 것은 가장 일반적으로 사용되는 방법이다.
- 클래스의 존재로 해당 기술의 사용 여부를 확인하고, 직접 추가한 커스텀 Bean 구성의 존재를 확인해서 자동 구성의 빈 오브젝트를 사용할지 최종 결정한다.

### Property Conditions
- `@ConditionalOnProperty`

지정된 프로퍼티가 존재하고 값이 'false' 가 아니면 포함하도록 한다. 
- 특정 값을 가진 경우를 확인하거나 프로퍼티가 존재하지 않을 때 조건을 만족하게 할 수도 있다.

### Resource Conditions
- `@ConditionalOnResource`

지정된 리소스(파일)의 존재를 확인하는 방법이다.

### Web Application Conditions
- `@ConditionalOnWebApplication`
- `@ConditionalOnNotWebApplication`

웹 애플리케이션 여부를 확인한다. (모든 Spring Boot 프로젝트가 웹 기술을 목적으로 하지 않기 때문)

### SpEL Expression Conditions
- `@ConditionalOnExpression`

스프링 SpEL(스프링 표현식)의 처리 결과를 기준으로 판단한다. (매우 상세한 조건 설정이 가능하다.)
- 리터럴 표현식: `'Hello World'`, `10`, `true`
- 변수: `#variableName`
- 프로퍼티 접근: `person.name`
- 메서드 호출: `person.getName()`
- 연산자: 산술(`+`, `-`, `*`, `/`), 비교(`==`, `>`, `<`), 논리(`&&`, `||`, `!`)
- 조건식: `condition ? trueValue : falseValue`
- 컬렉션 접근: `list`, `map['key']`
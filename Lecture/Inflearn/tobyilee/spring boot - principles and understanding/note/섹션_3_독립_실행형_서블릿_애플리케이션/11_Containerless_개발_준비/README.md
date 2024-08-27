## [섹션 III] 11_Containerless 개발 준비

---

스프링 부트의 주요 특징 중 하나인 'Containerless'가 어떤 식으로 만들어졌고, 어떻게 동작하는지 알아보자.

#### Containerless의 의미
- 서블릿 컨테이너와 관련된 번거롭고 복잡한 작업 및 작업을 위해 필요한 지식을 개발자들이 더 이상 신경 쓰지 않고, '컴포넌트 빈'만 신경 쓰면 되도록 스프링 부트가 무언가 작업을 해주는 것이다.

이전에 HelloController를 작성하고, 애플리케이션을 실행해서 API 통신을 해보았다. 애플리케이션이 실행될 때 Tomcat이 동작한다. 그러나 우리는 Tomcat을 설치하거나 설정한 기억이 없다.
- 현재 HelloController가 속한 프로젝트에서 동작할만한 코드라고 할 수 있는 것은 [HellobootApplication.java](../../../practice/섹션_2_스프링_부트_시작하기/helloboot/src/main/java/tobyspring_eh13/helloboot/HellobootApplication.java) 파일 내의 코드 뿐이다.
  ```java
  @SpringBootApplication
  public class HellobootApplication {
      public static void main(String[] args) {
          SpringApplication.run(HellobootApplication.class, args);
      }
  }
  ```
  - 이 중에서 눈에 띄는 부분은 `run()`와 `@SpringBootApplication` 어노테이션 뿐이다.

이제 저 두 코드가 어떤 역할을 하는지 알아보기 위해서 해당 코드를 지워보도록 할 것이다.
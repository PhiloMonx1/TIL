## [섹션 V] 30_DI와 단위 테스트

지금까지 작성한 테스트에는 작은 문제가 있는데, 우선 테스트를 실행하기 위해서 서버를 실행해야 한다는 것이고, 그렇기에 속도가 느리다는 것이다.

이번에는 단위테스트를 작성하여 이 문제를 해결할 것이다.

### 1. HelloServiceTest
```java
public class HelloServiceTest {

    @Test
    void simpleHelloService() {
        SimpleHelloService simpleHelloService = new SimpleHelloService();
        String result = simpleHelloService.sayHello("Test");

        Assertions.assertThat(result).isEqualTo("Hello Test");
    }
}
```
너무 간단한 코드라서 추가 설명은 하지 않을 것이고, 강의를 통해서 인텔리제이의 라이브 템플릿 기능을 배웠다. (asj 등의 약어를 입력해서 미리 설정한 소스코드를 불러올 수 있는 편의 기능 "sout"처럼 자주 쓰는 코드를 커스텀하여 저장할 수 있다.)

### 2. HelloControllerTest
```java

public class HelloControllerTest {

    @Test
    void helloController() {
        HelloController helloController = new HelloController(name -> name);

        String result = helloController.hello("Test");

        Assertions.assertThat(result).isEqualTo("Test");
    }

    @Test
    void failHelloController() {
        HelloController helloController = new HelloController(name -> name);

        Assertions.assertThatThrownBy(() -> helloController.hello(null))
                .isInstanceOf(IllegalArgumentException.class);

        Assertions.assertThatThrownBy(() -> helloController.hello(""))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
```
- `HelloController`의 인스턴스를 생성하는 시기에 람다를 사용해서 의존성 주입을 했다.
  - `HelloController`의 파라미터인 `HelloService`가 추상메서드를 단 하나만 가지는 인터페이스이기 때문에 가능한 것이다.
    ```java
    HelloController helloController = new HelloController(new HelloService() {
        @Override
        public String sayHello(String name) {
            return name;
        }
    });
    ```
    - 람다를 사용하지 않는다면 해당 코드로 진행해야 한다.
- 실패 케이스에 대한 테스트도 진행해보았다.

### 3. helloApi 실패 케이스 테스트
```java
class HelloApiTest {

    @Test
    void helloApi() {
        TestRestTemplate rest = new TestRestTemplate();

        ResponseEntity<String> response =
                rest.getForEntity("http://localhost:8080/hello?name={name}", String.class, "Spring");

        // status code : 200
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        // header content-type: text/plain
        assertThat(response.getHeaders().getFirst("Content-Type")).startsWith(MediaType.TEXT_PLAIN_VALUE);
        // body: Hello Spring
        assertThat(response.getBody()).isEqualTo("Hello Spring");
        // body: Hello Spring
    }

    @Test
    void failsHelloApi() {
        TestRestTemplate rest = new TestRestTemplate();

        ResponseEntity<String> response =
                rest.getForEntity("http://localhost:8080/hello?name=", String.class);

        // status code : 500
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```
- API를 테스트할 때도 실패 케이스에 대한 테스트를 할 수 있다.
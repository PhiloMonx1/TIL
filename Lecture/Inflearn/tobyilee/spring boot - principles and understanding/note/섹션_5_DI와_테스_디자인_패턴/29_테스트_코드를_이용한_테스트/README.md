## [섹션 V] 29_테스트 코드를 이용한 테스트

우리가 작성한 애플리케이션을 테스트 해볼 것이다.

```java
package tobyspring_eh13.helloboot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
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
}
```
- `TestRestTemplate` : Spring Boot에서 제공하는 REST 클라이언트로, HTTP 요청을 쉽게 보낼 수 있게 해준다.
  - `getForEntity()` : HTTP GET 요청을 수행하고 응답을 ResponseEntity 객체로 반환한다. 
- `ResponseEntity` : HTTP 응답을 나타내는 Spring 프레임워크의 클래스이다.
- 주피터가 아닌 `assertj`의 Assertions을 사용해서 응답을 검증했다.
  - 상태 코드가 200 (OK)인지 확인 
  - Content-Type 헤더가 "text/plain"으로 시작하는지 확인 (실제 응답 값은 "text/plain;charset=ISO-8859-1"이기 때문에 "text/plain"으로 시작하는지만 검증했다.)
  - 응답 본문이 "Hello Spring"인지 확인
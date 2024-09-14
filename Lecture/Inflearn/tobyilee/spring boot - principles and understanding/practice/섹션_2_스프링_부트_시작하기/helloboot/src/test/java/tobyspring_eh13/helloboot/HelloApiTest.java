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

    @Test
    void failsHelloApi() {
        TestRestTemplate rest = new TestRestTemplate();

        ResponseEntity<String> response =
                rest.getForEntity("http://localhost:8080/hello?name=", String.class);

        // status code : 500
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

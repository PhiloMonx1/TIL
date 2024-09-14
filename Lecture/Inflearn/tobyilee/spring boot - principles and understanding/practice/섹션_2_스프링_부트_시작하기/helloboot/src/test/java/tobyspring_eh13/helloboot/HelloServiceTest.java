package tobyspring_eh13.helloboot;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;


public class HelloServiceTest {

    @Test
    void simpleHelloService() {
        SimpleHelloService simpleHelloService = new SimpleHelloService();
        String result = simpleHelloService.sayHello("Test");

        Assertions.assertThat(result).isEqualTo("Hello Test");
    }
}

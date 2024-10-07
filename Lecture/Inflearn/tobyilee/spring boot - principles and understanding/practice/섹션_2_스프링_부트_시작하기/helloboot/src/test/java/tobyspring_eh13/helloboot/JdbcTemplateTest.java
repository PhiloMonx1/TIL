package tobyspring_eh13.helloboot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@HelloBootTest
public class JdbcTemplateTest {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void init() {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS hello(name VARCHAR(50) PRIMARY KEY, COUNT INT)");
    }

    @Test
    void insertAndSelect() {
        jdbcTemplate.update("INSERT INTO hello VALUES(?, ?)", "EH13", 3);
        jdbcTemplate.update("INSERT INTO hello VALUES(?, ?)", "Spring", 1);

        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM hello", Long.class);
        assertThat(count).isEqualTo(2);
    }
}

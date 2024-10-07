## [섹션 IX] 53_JdbcTemplate과 트랜잭션 매니저 구성

### `JdbcTemplate`, `JdbcTransactionManager` Bean 추가
```java
package tobyspring_eh13.config.autoconfig;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tobyspring_eh13.config.ConditionalMyOnClass;
import tobyspring_eh13.config.MyAutoConfiguration;

import javax.sql.DataSource;
import java.sql.Driver;

//...생략
@EnableTransactionManagement
public class DataSourceConfig {
    //...생략
    
    @Bean
    @ConditionalOnSingleCandidate(DataSource.class)
    @ConditionalOnMissingBean
    JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    @ConditionalOnSingleCandidate(DataSource.class)
    @ConditionalOnMissingBean
    JdbcTransactionManager jdbcTransactionManager(DataSource dataSource) {
        return new JdbcTransactionManager(dataSource);
    }
}

```
- `JdbcTemplate` 과 `JdbcTransactionManager`을 추가했다.
- `@ConditionalOnSingleCandidate(DataSource.class)` : `DataSource` 타입의 Bean 이 단 하나만 존재할 경우 파라미터로 사용한다.
- `@EnableTransactionManagement` : 해당 어노테이션이 있으면 이제 `JdbcTransactionManager`의 도움을 받아 의해 `@Transactional` 어노테이션을 사용할 수 있다.
  - `@EnableTransactionManagement`는 내부에서 `@Import(TransactionManagementConfigurationSelector.class)`를 메타 어노테이션으로 가지고 있다.

### 테스트 어노테이션 생성
```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = HellobootApplication.class)
@TestPropertySource("classpath:/application.properties")
@Transactional
public @interface HelloBootTest {
}
```
- 데이터베이스 테스트를 위한 어노테이션을 통합 어노테이션으로 묶었다. 
  - `DataSourceTest`에도 해당 어노테이션으로 교체를 진행했다.

```java
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
```
- `init()` 메서드를 통해 만약 데이터베이스에 'hello' 라는 이름의 테이블이 존재하지 않을 시 생성하도록 설정했다.
- 이제 `DataSourceTest` 테스트는 불필요하다. 해당 테스트에서 DB 연결을 선행적으로 수행하기 때문이다.

### 트랜잭션 테스트
```java
package tobyspring_eh13.helloboot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@HelloBootTest
@Rollback(false)
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

    @Test
    void insertAndSelect2() {
        jdbcTemplate.update("INSERT INTO hello VALUES(?, ?)", "EH13", 3);
        jdbcTemplate.update("INSERT INTO hello VALUES(?, ?)", "Spring", 1);

        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM hello", Long.class);
        assertThat(count).isEqualTo(2);
    }
}
```
- 간단하게 `insertAndSelect2()` 메서드를 추가했다. (`insertAndSelect`와 내용이 완전 똑같다.)
- 이 경우 `@Rollback(false)`을 통해 DB 롤백을 진행하지 않으면 'PRIMARY KEY' 가 동일하다는 에러가 발생하고, 롤백을 진헹하도록 하면 정상적으로 테스트 성공하는 것을 확인할 수 있다.
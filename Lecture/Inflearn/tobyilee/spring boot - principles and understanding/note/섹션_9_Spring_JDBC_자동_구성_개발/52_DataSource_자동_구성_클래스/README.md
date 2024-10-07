## [섹션 IX] 52_DataSource 자동 구성 클래스

### JDBC SimpleDriverDataSource 자동 구성
```groovy
implementation 'org.springframework:spring-jdbc'
```
- spring-jdbc 라이브러리 의존성을 추가한다.

```java
@MyConfigurationProperties(prefix = "data")
public class MyDataSourceProperties {
    private String driverClassName;
    private String url;
    private String username;
    private String password;
    
    // getter, setter
}
```
- 프로퍼티 사용을 위해 `MyDataSourceProperties` 클래스를 추가했다.

```java
@MyAutoConfiguration
@ConditionalMyOnClass("org.springframework.jdbc.core.JdbcOperations")
@EnableMyConfigurationProperties(MyDataSourceProperties.class)
public class DataSourceConfig {
    @Bean
    DataSource dataSource(MyDataSourceProperties properties) throws ClassNotFoundException {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        dataSource.setDriverClass((Class<? extends Driver>) Class.forName(properties.getDriverClassName()));
        dataSource.setUrl(properties.getUrl());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());

        return dataSource;
    }
}
```
- `DataSourceConfig` 자동 구성 클래스를 생성 후 import 파일에도 명시해주었다.

```groovy
runtimeOnly('com.h2database:h2:2.1.214')
```
- DB 연결을 위해 h2 데이터 베이스를 사용한다.

```properties
data.driverClassName=org.h2.Driver
data.url=jdbc:h2:mem:
data.username=sa
data.password=
```
- 프로퍼티를 설정했다.

### 테스트
```java
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = HellobootApplication.class)
@TestPropertySource("classpath:/application.properties")
public class DataSourceTest {
    @Autowired
    DataSource dataSource;

    @Test
    void connection() throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.close();
    }
}
```
- `@ExtendWith(SpringExtension.class)` : 확장된 테스트 기능을 사용하기 위해 `SpringExtension`을 임포트 한다.
- `@ContextConfiguration(classes = HellobootApplication.class)` : 테스트 애플리케이션을 지정했다.
- `@TestPropertySource("classpath:/application.properties")` : 프로퍼티를 불러오기 위해서 작성해주었다. (application-test.properties 파일로 별도 분리도 가능하다.)
- `connection()` : 간단하게 데이터베이스 연결만 확인했다.

### HikariDataSource 자동 구성
```groovy
implementation 'com.zaxxer:HikariCP'
```
- Hikari 사용을 위한 의존성을 추가했다.

```java
@MyAutoConfiguration
@ConditionalMyOnClass("org.springframework.jdbc.core.JdbcOperations")
@EnableMyConfigurationProperties(MyDataSourceProperties.class)
public class DataSourceConfig {
    @Bean
    @ConditionalMyOnClass("com.zaxxer.hikari.HikariDataSource")
    @ConditionalOnMissingBean
    DataSource hikariaDataSource(MyDataSourceProperties properties) {
        HikariDataSource dataSource = new HikariDataSource();

        dataSource.setDriverClassName(properties.getDriverClassName());
        dataSource.setJdbcUrl(properties.getUrl());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());

        return dataSource;
    }

    @Bean
    @ConditionalOnMissingBean
    DataSource dataSource(MyDataSourceProperties properties) throws ClassNotFoundException {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        dataSource.setDriverClass((Class<? extends Driver>) Class.forName(properties.getDriverClassName()));
        dataSource.setUrl(properties.getUrl());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());

        return dataSource;
    }
}
```
- `@ConditionalMyOnClass("com.zaxxer.hikari.HikariDataSource")` : `HikariDataSource` 가 존재하는 경우에만 `HikariDataSource` Bean 을 등록하도록 설정했다.
- `@ConditionalOnMissingBean` : 이미 같은 유형의 Bean 이 등록된 경우 등록을 하지 않도록 설정했다.
  - `HikariDataSource`에도 붙여준 이유는 커스텀 자동 구성을 통해 `DataSource`를 커스텀 Bean 으로 등록할 수도 있기 때문이다.
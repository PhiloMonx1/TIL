# Day 32. MongoDB 사용하기

3.0 레퍼런스 링크 9.2.2 : [Spring Boot 3.0.0 reference 9.2.2 : data.nosql.mongodb](https://docs.spring.io/spring-boot/docs/3.0.0/reference/htmlsingle/#data.nosql.mongodb)

---

MongoDB는 NoSQL 데이터베이스의 대표격으로 Json 형식으로 데이터를 저장하며 무관계, 비정형의 특징을 가지고 있다.
- Spring Boot는 `spring-boot-starter-data-mongodb`, `spring-boot-starter-data-mongodb-reactive` 등으로 MongoDB 사용을 지원한다.

## 0. MongoDB 데이터베이스 설정
- `org.springframework.data.mongodb.MongoDbFactory`를 사용해서 MongoDB에 접속할 수 있다.
- 해당 인스턴스는 기본적으로 `mongodb://localhost/test` 경로로 접근을 시도한다.
  - property 설정을 통해 기본 경로를 변경할 수 있다.
    ```properties
    spring.data.mongodb.uri=mongodb://user:secret@mongoserver1.example.com:27017,mongoserver2.example.com:23456/test
    ```
  - 혹은 아래와 같이 개별 속성을 통해 세부 설정을 하는 것도 가능하다.
    ```properties
    spring.data.mongodb.host=mongoserver1.example.com
    spring.data.mongodb.port=27017
    spring.data.mongodb.additional-hosts[0]=mongoserver2.example.com:23456
    spring.data.mongodb.database=test
    spring.data.mongodb.username=user
    spring.data.mongodb.password=secret
    ```
### Decker로 MongoDB 컨테이너 실행
[Docker Hub : Mongo - Official Image](https://hub.docker.com/_/mongo) : 'Start a mongo server instance' 참고 (MONGO_VERSION :
7.0.14)
```
docker  run -p 27017:27017 --name mongo -d mongo
```
- `$ docker run --name some-mongo -d mongo:tag` 공식 이미지 설명에는 해당 명령어만 제안하지만 실제로는 내부 포트와 도커의 포트를 연결해야 내부 애플리케이션에서 접근할 수 있다.

![도커 컨테이너 - MongoDB](../note_image/Day32_01.png)

### `mongo` 프로젝트 생성
> Spring Initializr (인텔리제이) 사용
> - Spring Boot Version : 3.3.3 
> - Project : Gradle - Groovy
> - Java Version : 17
> - Dependencies :
>   - Spring Boot DevTools
>   - Spring Web
>   - Spring Data MongoDB

![몽고 드라이버 디펜더시 확인](../note_image/Day32_02.png)
- Mongo Driver 5.0.1 이 들어온 것을 확인할 수 있다.

## 1. MongoDB 연결 : [MongoDatabaseFactory](https://docs.spring.io/spring-boot/docs/3.0.0/reference/htmlsingle/#data.nosql.mongodb.connecting) 사용해서 Mongo DB 연결하고 데이터 넣어보기
```properties
spring.data.mongodb.uri=mongodb://localhost/test
```
- 기본 값이기에 굳이 설정하지 않아도 되나 명시했다.

```java
@SpringBootApplication
public class DemoMongoApplication {

    @Autowired
    MongoDatabaseFactory mongoDbFactory;

    public static void main(String[] args) {
        SpringApplication.run(DemoMongoApplication.class, args);
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> {
            MongoDatabase db = mongoDbFactory.getMongoDatabase();
            db.createCollection("meetings");

            db.getCollection("meetings").insertOne(
                new Document()
                        .append("title", "new meeting")
                        .append("location", "seattle")
            );
        };
    }
}
```

#### 강의와의 차이점
- `MongoDbFactory` -> `MongoDatabaseFactory`
- `mongoDbFactory.getDb()` -> `mongoDbFactory.getMongoDatabase()`

### 데이터 확인해보기
1. MongoDB Docker Container 연결하기
   - ![Mongo DB 도커 접속](../note_image/Day32_03.png)
   - [Docker Hub : Mongo - Official Image](https://hub.docker.com/_/mongo) : 'Connect to MongoDB from another Docker container
     ' 참고 (MONGO_VERSION :
     7.0.14)
   - 여기서는 `docker exec -it mongo mongosh` 명령어로 접속했다. ('mongo'라는 이름의 컨테이너에 'mongosh' 명령어를 실행해 Mongo Shell로 접근한다.)
2. 데이터 확인
   - [Mongo 쿼리 레퍼런스](https://www.mongodb.com/ko-kr/docs/mongodb-shell/crud/read/#std-label-mongosh-read)
   - ![Mongo DB 데이터 확인](../note_image/Day32_04.png)
3. 인텔리제이 데이터베이스 툴 연결
   - ![인텔리제이 데이터베이스 툴 연결](../note_image/Day32_05.png)
   - ![meetings 컬렉션 확인](../note_image/Day32_06.png)

## 2. [Mongo Template](https://docs.spring.io/spring-boot/docs/3.0.0/reference/htmlsingle/#data.nosql.mongodb.template) 사용하기
Spring Boot는 `JdbcTemplate`과 유사한 방식을 사용하는 [MongoTemplate](https://docs.spring.io/spring-data/mongodb/docs/4.0.0/api/org/springframework/data/mongodb/core/MongoTemplate.html)을 제공한다.
- 간편한 MongoDB 작업: CRUD(Create, Read, Update, Delete) 연산을 쉽게 수행할 수 있다.
- 예외 처리: MongoDB 특화된 예외를 Spring의 일관된 데이터 접근 예외로 변환한다.
- 쿼리 지원: MongoDB 쿼리를 Java 코드로 작성할 수 있게 해주는 다양한 메서드를 제공한다.
- 객체 매핑: Java 객체와 MongoDB 문서 간의 매핑을 자동으로 처리한다.
- 집계 연산: MongoDB의 강력한 집계 기능을 사용할 수 있는 메서드를 제공한다.
- 인덱스 관리: 프로그래밍 방식으로 인덱스를 생성하고 관리할 수 있다.

```java
@SpringBootApplication
public class DemoMongoApplication {

    @Autowired
    MongoTemplate mongoTemplate;

    public static void main(String[] args) {
        SpringApplication.run(DemoMongoApplication.class, args);
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> {
            Meeting meeting = new Meeting();
            meeting.setTitle("Mongo Study");
            meeting.setLocation("Redmond");

            mongoTemplate.insert(meeting, "meetings");
        };
    }
}
```
- `MongoTemplate.insert()` : 파라미터로 객체, 컬렉션명을 줄 수 있다. (컬렉션명을 명시하지 않을 경우 객체의 클래스명으로 추론함)

![MongoTemplate으로 넣은 데이터 확인](../note_image/Day32_07.png)
- `_class` 필드가 자동으로 생성되고, 클래스가 값으로 들어간다.

## 3. [Spring Data MongoDB Repositories](https://docs.spring.io/spring-boot/docs/3.0.0/reference/htmlsingle/#data.nosql.mongodb.repositories) 사용하기
`Spring Data MongoDB`는 `JPA Repository` 처럼 사용할 수 있는 기술을 지원하며, 사용 방법 또한 비슷하다.
- [spring-data-mongodb 레퍼런스](https://spring.io/projects/spring-data-mongodb#overview)에서 자세한 내용을 확인할 수 있다.

### 객체 매핑 : 메타데이터 기반(Metadata-based Mapping)
[spring-data-mongodb 레퍼런스 : Metadata-based Mapping](https://docs.spring.io/spring-data/data-mongodb/reference/mongodb/mapping/mapping.html#mapping-usage)

```java
@Document(collection = "meetings")
public class Meeting {

    @Id
    String id;

    //... 기존 필드 & Getter/Setter
}
```
- MongoDB의 ID는 String 타입으로 설정하는 것이 일반적이다.

### Repository 생성
```java
@Repository
public interface MeetingRepository extends PagingAndSortingRepository<Meeting, String> {
}
```
- `JpaRepository`와 사용 방법이 같다.

#### PagingAndSortingRepository
Spring Data 프로젝트의 일부로, 여러 데이터 저장소에서 사용할 수 있는 공통 인터페이스
- 페이징과 정렬 기능을 제공한다.
- `JpaRepository`, `MongoRepository`는 기본적으로 해당 인터페이스를 확장한다.

그렇기에 `import org.springframework.data.mongodb.repository.MongoRepository` 를 사용하는 것도 가능하다.

그렇다면 범용적으로 사용 가능한 `PagingAndSortingRepository`가 어떻게 JPA와 MongoDB를 헷갈리지 않고 알맞은 데이터베이스의 쿼리로 전달할 수 있을까?
- 레퍼런스를 살펴보면 `PagingAndSortingRepository`에 매핑되는 클래스(Meeting)에 `@Entity` 어노테이션이 부여되어 있는지 `@Document`가 부여되어 있는지를 구분하여 JPA와 MongoDB 기술을 판단한다고 한다.
- 물론 의존성에 의거하여 판단하기도 한다. 그러나 JPA와 MongoDB를 동시에 사용하는 프로젝트에서 모든 레포지토리를 `PagingAndSortingRepository`로 작성해도 Spring Boot는 매핑된 클래스를 통해 적절한 기술을 판단할 수 있다.

### MeetingRepository 사용

```java
@SpringBootApplication
public class DemoMongoApplication {

    @Autowired
    MeetingRepository meetingRepository;

    public static void main(String[] args) {
        SpringApplication.run(DemoMongoApplication.class, args);
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> {
            meetingRepository.findByLocation("Redmond").forEach(
                    System.out::println
            );
        };
    }
}
```
- 결과
    ```
    2024-08-31T23:04:06.353+09:00  INFO 58909 --- [demo_mongo] [  restartedMain] e.til.demo_mongo.DemoMongoApplication    : Started DemoMongoApplication in 0.786 seconds (process running for 6.021)
    Meeting{id='66d3182d1aac6318dcd74d37', title='Mongo Study', location='Redmond'}
    ```
#### 대소문자 구분 없이 조회하기
강의에서는 `meetingRepository.findByLocation("redmond")`로 조회를 헸다가 대소문자 구분으로 인해 데이터를 찾지 못했었다. 대소문자를 무시하는 방법은 여러 가지가 있지만 지금은 가장 간단한 방법을 알아보겠다.
```java
@Repository
public interface MeetingRepository extends PagingAndSortingRepository<Meeting, String> {

    List<Meeting> findByLocationIgnoreCase(String location);
}
```
- `Spring Data MongoDB`는 기본적으로 메서드 이름을 분석해서 IgnoreCase에 대한 처리를 자동으로 진행한다.
- `meetingRepository.findByLocationIgnoreCase("rEdmOnd")`로 대소문자를 무시하고 location='Redmond' 데이터를 찾는 것이 가능하다.

## 4. 테스트 코드 작성
```java
@DataMongoTest
class MeetingRepositoryTest {

    @Autowired
    MeetingRepository meetingRepository;

    @Test
    void findByLocationIgnoreCase() {
        meetingRepository.findByLocationIgnoreCase("aaa");
    }

}
```
- 이와 같이 `findByLocationIgnoreCase()`에 대한 테스트 코드를 작성할 수 있다. 
- 그러나 실제 DB에 테스트를 하는 것은 권장되지 않는다.

도커에서 MongoDB 컨테이너를 중단하면 Test가 동작하지 않는다. 그렇다면 실제 DB에 영향을 주지 않고 테스트 하는 방법은 없을까? 강의에서는 임베디드 MongoDB를 사용해서 테스트 코드를 실습한다.

### 임베디드 MongoDB
Spring Boot.2.x 까지는 임베디드 MongoDB에 대한 자동 설정을 제공했었다. 그러나 Spring Boot 3.0.0부터는 이 자동 설정이 제거되었으며, 이제 임베디드 MongoDB를 사용하려면 별도의 설정이 필요하다.
- 지원 중단 이유
  - 임베디드 MongoDB의 사용 빈도가 낮아졌다.
  - 실제 MongoDB 인스턴스나 테스트 컨테이너 사용이 더 일반적이 되었다.
- 그럼에도 임베디드 MongoDB를 사용하고자 한다면 수동 설정을 진행해야 한다.
  - Flapdoodle 라이브러리를 수동으로 추가하고 설정할 수 있다.

### 테스트를 위한 TestContainers 사용
TestContainers : Spring Boot 애플리케이션이 테스트를 실행할 때 Docker 컨테이너를 직접 시작하고 관리하는 라이브러리

1. `testcontainers` 라이브러리 추가
    ```groovy
    testImplementation 'org.testcontainers:junit-jupiter'
    testImplementation 'org.testcontainers:mongodb'
    ```
    - `junit-jupiter` : JUnit 5와 TestContainers 통합을 지원하는 라이브러리
    - `mongodb` : MongoDB 특화 TestContainers 모듈
2. 테스트 클래스에서 `TestContainers` 사용
```java
@DataMongoTest
@Testcontainers
class MeetingRepositoryTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.14");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }
    
    //...테스트 코드
}
```
- `@Testcontainers` : 테스트 클래스에 부여해서 `Testcontainers`를 사용해서 테스트 할 것을 명시
- `MongoDBContainer("mongo:7.0.14")` : `@Container`와 함께 MongoDB 컨테이너 설정. 파라미터로 도커 이미지 버전을 넣어주었음
- `@DynamicPropertySource` : Spring 테스트 컨텍스트에 동적으로 프로퍼티를 추가 (테스트 실행 시 Spring 환경에 프로퍼티를 주입할 수 있게 해줌)
  - `registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl)` : "spring.data.mongodb.uri" 프로퍼티에 MongoDB 컨테이너의 연결 URL을 동적으로 설정
3. 테스트 작성 시 트러블 슈팅 : `save()`를 찾을 수 없는 경우
- `meetingRepository.save()` 를 찾을 수 없는 경우 -> `MeetingRepository`가 `PagingAndSortingRepository` 대신 `MongoRepository`를 상속하도록 한다.
  - Spring Data 3.0 부터 `PagingAndSortingRepository`는 더 이상 `CrudRepository`를 확장하지 않는다.
  - 즉, `PagingAndSortingRepository` 에는 `save()` 메서드가 없다.
  - `MongoRepository`를 상속하도록 하면 `save()` 메서드 사용이 가능하다.

## 5. spring-boot-starter-data-mongodb-reactive 사용 : 비동기식 프로그래밍
- spring-boot-starter-data-mongodb: 전통적인 동기식(synchronous) 프로그래밍 모델을 사용한다.
- spring-boot-starter-data-mongodb-reactive: 리액티브(reactive) 프로그래밍 모델을 사용한다.

즉, mongodb-reactive는 논블로킹 I/O를 사용하여 적은 수의 스레드로 많은 동시 연결을 처리하는 '리액티브' 프로그래밍이 가능하다.

Spring Framework의 리액티브 프로그래밍을 알기 위해서는 Mono, Flux 등의 리액티브 타입 및 비동기 처리에 대한 이해도가 있어야 한다.

1. 라이브러리 변경
    ```groovy
    // implementation 'org.springframework.boot:spring-boot-starter-data-mongodb' -> 삭제
    implementation 'org.springframework.boot:spring-boot-starter-data-mongodb-reactive'
    ```
2. `MeetingRepository` 변경
    ```java
    @Repository
    public interface MeetingRepository extends ReactiveMongoRepository<Meeting, String> {
    
        Flux<Meeting> findByLocationIgnoreCase(String location);
    }
    ```
   - `MongoRepository` -> `ReactiveMongoRepository`로 상속 클래스 변경
   - `findByLocationIgnoreCase()` 메서드 `List` -> `Flux`로 반환 타입 변경
3. 테스트 코드 변경
    ```java
    //... 생략
    class MeetingRepositoryTest {
        @Test
        void findByLocationIgnoreCase() {
        // Given
        Meeting meeting = new Meeting();
        meeting.setTitle("new meeting");
        meeting.setLocation("Redmond");

        meetingRepository.save(meeting);

        Meeting mongoMeeting = new Meeting();
        mongoMeeting.setTitle("mongo study");
        mongoMeeting.setLocation("Seattle");

        meetingRepository.save(mongoMeeting);

        // When
        List<Meeting> result = meetingRepository.findByLocationIgnoreCase("seattle").collectList().block();

        //Then
        assertEquals(1, result.size());
        assertTrue(result.contains(mongoMeeting));
        }
    }
    ```
   - `findByLocationIgnoreCase()` 메서드가 Flux 타입을 리턴하기 때문에 `Flux.collectList().block()`를 사용해서 List로 변환한다.
4. 테스트 실패 대응
   - 실패 원인 : `ReactiveMongoRepository`가 상속하는 `ReactiveCrudRepository`의 `save()` 메서드 자체가 비동기적으로 진행되어 `save()`를 기다리지 않고 넘어가기 때문. 즉, DB에 데이터가 저장되기 전에 호출하는 것이다.
     - When 에서 `.block()`을 사용했기 때문에 해당 라인에서 멈춘다. 그러나 혼자 너무 빨리 달려와서 DB에는 아직 데이터가 없을 것이다.
5. 해결 방법
    ```java
    //... 생략
    class MeetingRepositoryTest {
        @Test
        void findByLocationIgnoreCase() {
        // Given
        Meeting meeting = new Meeting();
        meeting.setTitle("new meeting");
        meeting.setLocation("Redmond");

        meetingRepository.save(meeting).block();

        Meeting mongoMeeting = new Meeting();
        mongoMeeting.setTitle("mongo study");
        mongoMeeting.setLocation("Seattle");

        meetingRepository.save(mongoMeeting).block();

        // When
        List<Meeting> result = meetingRepository.findByLocationIgnoreCase("seattle").collectList().block();

        //Then
        assertEquals(1, result.size());
        assertTrue(result.contains(mongoMeeting));
        }
    }
    ```
   - `meetingRepository.save().block()`을 사용해서 `save()` 작업을 기다리도록 한다. 

## 부록. ApplicationRunner 코드 리액티브 호환되도록 변경하기
강의에서는 기존 ApplicationRunner 코드를 삭제하고 진행한다. 여기서는 리액티브 프로그래밍에 대응하도록 코드를 수정할 것이다.
```java
@SpringBootApplication
public class DemoMongoApplication {

    @Autowired
    MeetingRepository meetingRepository;

    public static void main(String[] args) {
        SpringApplication.run(DemoMongoApplication.class, args);
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> {
            meetingRepository.findByLocationIgnoreCase("rEdmOnd").subscribe(
                    System.out::println,
                    error -> System.err.println("Error: " + error),
                    () -> System.out.println("Completed")
            );
        };
    }
}
```
- `subscribe()` 리액티브 스트림을 구독하여 결과를 처리한다. 내부에서 3개의 람다 함수를 인자로 받았다.
  - 첫 번째 람다 라인 : 각 Meeting 객체를 출력
  - 두 번째 람다 라인 : 에러가 발생하면 에러 메시지를 출력
  - 세 번째 람다 라인 : 스트림이 완료되면 "Completed"를 출력
- 구독 : "구독(subscribe)"이라는 표현은 리액티브 프로그래밍에서 중요한 개념이며, Observer 패턴에서 유래했다.
  - 리액티브 프로그래밍은 데이터를 연속적인 스트림으로 취급합니다. "구독"은 이 데이터 스트림에 "연결"하여 데이터를 받아보는 행위를 의미한다.
- 세 개의 람다 라인 : 리액트 비동기 처리를 경험했다면 이해가 빠를 것이다.
  - 첫 번째 람다 라인 : 리액트의 then() -> 여기서는 기본 람다식으로 표현되었다. 
  - 두 번째 람다 라인 : 리액트의 catch() -> 여기서는 error 로 표현되었다.
  - 세 번째 람다 라인 : 리액트의 finally() -> 여기서는 () 로 표현되었다.
- subscribe 메서드의 세 가지 인자와 그 순서가 중요하다.
  - 첫 번째는 항상 정상적인 데이터 처리를 담당한다.
  - 두 번째는 항상 에러 처리를 담당한다.
  - 세 번째는 항상 완료 신호를 처리한다.
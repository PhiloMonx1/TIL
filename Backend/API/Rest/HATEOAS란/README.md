# HATEOAS

'Hypermedia As The Engine Of Application State'의 약자로 '애플리케이션 상태의 엔진으로서의 하이퍼미디어'로 번역될 수 있다.
- 애플리케이션 : 특정 작업을 수행하기 위해 개발된 소프트웨어 프로그램
- 상태 : 애플리케이션의 현재 조건이나 상황
- 엔진 : 특정 기능을 수행하는 시스템의 핵심 구성 요소
- 하이퍼미디어 : 하이퍼텍스트 & 멀티미디어 (하이퍼링크와 비슷하다)

애플리케이션이 하이퍼미디어를 통해 상태를 관리하고 전환하는 방식으로 해석할 수 있다.

### RestAPI와 HATEOAS
HATEOAS는 RESTful 웹 서비스의 설계와 관련된 개념으로 쓰인다. 
- REST API의 제약 조건 중 하나이다.
- 클라이언트가 서버와 상호작용할 때 응답에 하이퍼미디어 링크를 포함하는 것을 의미한다.
- 클라이언트는 HATEOAS를 통해 다음에 수행할 수 있는 추가적인 작업을 안내 받는다.

### HATEOAS의 중요성
1. 클라이언트가 POST 요청을 통해 신규 게시물을 생성했다고 가정하자, 이 때 서버는 클라이언트의 요청을 처리한 후 200(ok) 상태를 응답한다.
2. 만약 클라이언트에서 게시물을 작성한 후 자신이 작성한 게시물 링크로 이동해야 한다고 가정하자. 이 때 클라이언트를 위해 서버는 게시물의 id를 함께 반환할 수 있다.
3. 그러나 실제로 게시물의 데이터를 받기 위해서는 GET 메서드를 사용해서 적절한 url에 요청을 보내야 한다.
4. 만약 서버가 클라이언트를 위해서 id 뿐만 아니라 GET 메서드를 사용할 url까지 함께 제공한다면 어떨까?
5. 이때 서버가 제공한 url이 HATEOAS이다.
6. 이 과정을 모든 API에 모두 적용한다고 해보자. 클라이언트는 API 전체 문서를 파악하지 않아도 서버가 제공한 흐름에 따라 API 요청을 할 수 있다.

HATEOAS는 [RESTful](../RESTful의%20개념)하다. REST API에는 구현 단계가 존재하는데, HATEOAS 까지 구현된 REST API는 가장 높은 단계로 가장 [RESTful](../RESTful의%20개념)한 API로 평가받는다.

### HATEOAS 예시
[Spring Boot에서 HATEOAS 구현 예시](https://github.com/PhiloMonx1/learning-spring-and-spring-boot-3.x/tree/main/07_Creating_a_Java_REST_API_with_Spring_Boot%2C_Spring_Framework_and_Hibernate#22%EB%8B%A8%EA%B3%84---rest-api-hateoas-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0)

```json
{
  "name" : "Adam",
  "birthDate" : "2022-08-16",
  "_links" : {
    "all-users" : {
      "href" : "http://localhost:8080/users"
    }
  }
}
```
- 현재 api는 name을 기반으로 특정 User를 GET 한 것이다.
- `_links`에서 `all-users`를 통해 모든 User을 조회하는 하이퍼링크를 포함하고 있다.

### HATEOAS는 무조건 좋을까?
HATEOAS는 의미있는 수준의 장점을 제공한다.
1. 유연성: 클라이언트는 서버의 응답에 포함된 링크를 따라가며 필요한 리소스를 탐색할 수 있어, API의 변경에 대한 적응력이 높다.
2. 자체 문서화: API 응답 자체에 다음 가능한 행동에 대한 정보를 포함하므로, 별도의 문서 없이도 클라이언트가 API를 사용할 수 있다.
3. 버전 관리 용이: 서버 측에서 API의 구조가 변경되더라도 클라이언트는 하이퍼미디어 링크를 통해 자연스럽게 새로운 구조를 사용할 수 있다.

그러나 추가적인 작업으로 인해 복잡성이 증가하고, 또한 서버의 응답이 늘어나는 만큼 트래픽 증가가 불가피하다는 단점 또한 존재한다.

모든 API HATEOAS를 적용할 필요는 없다. 흐름이 복잡한 API에만 선택적으로 재공하거나 OpenAPI에만 적용하는 등 유연하게 사용할 수 있다.
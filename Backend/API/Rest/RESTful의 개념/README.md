# RESTful의 개념

### REST
'**RE**presentational **S**tate **T**ransfer'의 약자로, '표현 상태 전이'로 번역된다.
- 표현 : 'Representational'은 미술에서 자주 사용되는 용어로 현실의 어떠한 것을 묘사하여 다시 나타내는 '재현'으로 자주 사용된다.
  - 이런 측면에서 Representational는 Java의 클래스와 비슷하다. 현실의 어떠한 것을 프로그래밍적으로 표현한 것이 Java의 클래스라고 한다면 말이다.
  - REST에서의 Representational는 서버가 가지고 있는 리소스를 클라이언트가 이해할 수 있는 형태로 표현 또는 재현하는 것을 의미한다.
- 상태 전이 : 정확히는 리소스의 상태 전이를 의미한다.
  - 클라이언트가 애플리케이션의 상태를 변경하기 위해 서버로부터 리소스의 표현을 받아오는 활동이다.

### REST API
REST 아키텍처 스타일을 따르는 애플리케이션 프로그래밍 인터페이스(API)
- HTTP 프로토콜을 사용하여 클라이언트와 서버 간의 통신을 수행한다.
- 서버가 제공하는 자원을 URI로 식별한다. 
  - 즉, url만 보고도 서버가 응답하는 자원을 추측할 수 있다. ex) `/user/1` : 사용자에 대한 리소스
- 주로 JSON이나 XML 형식으로 데이터를 주고받는다.
- HTTP 메서드(GET, POST, PUT, DELETE 등)를 사용하여 자원을 조작한다.
- 무상태성(stateless)을 유지하여 각 요청이 독립적으로 처리된다.
  - 서버는 클라이언트의 상태를 저장하지 않는다.

### RESTful
REST 원칙을 따르는 시스템을 의미한다. 
- 일관된 URI 구조와 HTTP 메서드 사용을 통해 클라이언트와 서버 간의 상호작용을 단순화하고 표준화한다.
- 잘 짜여진 REST API는 RESTful하다고 평가받으며, '**클라이언트가 사용하기 편한 API**'라고 단순화 할 수 있다.
- 클라이언트와 서버 간의 결합도를 낮추어, 시스템의 유지보수성과 확장성을 높이는 데 기여한다.

### RESTful의 단계
RESTful에는 총 4단계가 있으며 'Level 0' 부터 시작해서 높은 단계 일수록 더 RESTful 한 API로 평가받는다.
- Level 0: HTTP 사용
  - HTTP를 단순한 원격 통신 전송 시스템으로 사용한다.
  - 모든 요청이 단일 엔드포인트로 전송된다.
  - 리소스를 구분하지 않고 설계된 API이다.
- Level 1: 개별 리소스 도입
  - 모든 요청을 단일 서비스 엔드포인트로 보내는 대신, 각 리소스와 통신하게 된다. ex) `/user`, `/article` 등
  - HTTP 메서드는 주로 GET과 POST를 사용한다. (삭제, 수정 작업도 POST로 처리한다.)
- Level 2: HTTP 메서드의 활용
  - HTTP 메서드(GET, POST, PUT, DELETE 등)를 활용하여 리소스를 조작한다.
- Level 3: HATEOAS
  -  [HATEOAS](../HATEOAS란)를 적용한다.

## 참고
1. [REST란? REST API란? RESTful이란?](https://gmlwjd9405.github.io/2018/09/21/rest-and-restful.html)
2. [HATEOAS까지 사용해야 완벽한 RESTful이다.](https://dev-coco.tistory.com/187)
3. [HATEOAS를 모르면 당신이 알고 있는 REST API는 REST API가 아니라고 장담할게요.](https://wonit.tistory.com/454)
## [섹션 II] 10_HTTP 요청과 응답

---

웹 클라이언트와 웹 컨테이너 사이의 요청과 응답은 항상 쌍을 맺어서 수행한다. 즉, 요청이 없는 응답은 없다.

### HTTP
웹 요청을 어떻게 보내고, 응답은 어떻게 받아야 하는지를 정의해 놓은 표준 프로토콜.
- 웹 Request와 Response의 기본 구조를 이해하고 내용을 확인할 수 있어야 한다.

#### Request
- Request Line : Method, Path, HTTP Version
- Headers
- Message Body
#### Response
- State Line : HTTP Version, State Code, State Text
- Headers
- Message Body

### Hello Controller HTTP 요청과 응답 살펴보기
```http
> http -v "localhost:8080/hello?name=Spring"
GET /hello?name=Spring HTTP/1.1
Accept: */*
Accept-Encoding: gzip, deflate
Connction: keep-alive
Host: localhost:8080
User-Agent: HTTPie/3.2.1

HTTP/1.1 200
Connction: keep-alive
Content-Length: 12
Content-Type: text/plain;charset=UFT-8
Date: Tue, 27 Aug 2024 00:15:43 GMT
keep-alive: timeout=60

Hello Spring
```
#### Request
HTTP 메소드 GET을 사용하여 "/hello" 경로로 요청을 보낸다. "name=Spring" 쿼리 파라미터가 포함되어 있다. (파라미터도 패스에 속한다.)
- 주요 헤더
  - `Accept: */*`: 클라이언트가 모든 타입의 응답을 받아들일 수 있음을 나타낸다.
  - `Accept-Encoding: gzip, deflate`: 클라이언트가 gzip과 deflate 압축을 지원함을 나타낸다.
  - `Host: localhost:8080`: 요청이 전송되는 서버의 호스트명과 포트를 나타낸다.
  - `User-Agent: HTTPie/3.2.1`: 요청을 보내는 클라이언트 프로그램(HTTPie)과 그 버전을 나타낸다.

#### Response
HTTP/1.1 프로토콜을 사용하며, 상태 코드 200(OK)을 반환
- 주요 헤더
  - `Content-Length: 12`: 응답 본문의 길이가 12바이트임을 나타낸다.
  - `Content-Type: text/plain;charset=UFT-8`: 응답 본문이 UTF-8로 인코딩된 일반 텍스트임을 나타낸다.
  - `Date: Tue, 27 Aug 2024 00:15:43 GMT`: 응답이 생성된 날짜와 시간.
  - `keep-alive: timeout=60`: 연결을 60초 동안 유지함을 나타낸다.
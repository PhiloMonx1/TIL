## [섹션 II] 09_Hello API 테스트

---

### API 테스트를 할 때 고려할 것
단순히 브라우저에서 출력되는 화면만 확인하는 것이 아닌 눈에 보이지 않는 것들도 확인해야 한다.
- 어떠한 헤더 값을 주고 받았는지
- 바디가 어떻게 왔는지
- 매서드 타입은 무엇인지
- 상태 코드는 어떤지
- ...

### Hello API 테스트
hello() 컨트롤러 메소드를 호출할 수 있는 HTTP Request를 생성하고 리턴되는 HTTP Response를 확인한다.
- HTTP 요청을 만들고 응답을 확인하는데 사용되는 도구
  - 웹 브라우저 개발자 도구
  - curl
  - HTTPie
  - IntelliJ IDEA Ultimate - http request
  - Postman 
  - Talend Api tester
  - JUnit
  - 각종 API 테스트 도구


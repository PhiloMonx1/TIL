## 075 [통신] 쿠키를 삭제하시겠습니까?

---

### 무상태(stateless) 프로토콜
서버가 클라이언트의 요청에 대해 아무것도 기억하지 않는 프로토콜.
- HTTP는 무상태 프로토콜이다.
- 장점: 서버 확장성에 용이, 서버 자원 절약.
- 단점: 클라이언트가 매 요청마다 상태 정보를 포함해야 함

### 쿠키
서버가 클라이언트의 상태를 기억하기 위해 클라이언트에 저장하는 작은 정보 조각.
- 서버가 브라우저에 웹페이지를 보낼 때, 브라우저가 저장하게 되어 있는 추가 텍스트 덩어리(각각 최대 약 4,000바이트)를 포함할 수 있다.
    - 이 각각의 덩어리를 쿠키라고 한다.
- 사용 예시
    - 로그인 상태 유지
    - 장바구니 내용 저장
    - 사용자 설정 복원
- 쿠키의 특성
    - 저장 및 전송: 브라우저에 저장되며, 같은 서버에 요청 시 쿠키를 전송.
    - 제한: 쿠키는 자신이 생성된 도메인으로만 전송되며, 유효 기간이 있음.
    - 보안: 쿠키는 프로그램이 아니며, 액티브 콘텐츠가 없음.
- 쿠키의 문제점
    - 추적: 웹 브라우징 활동을 추적하고, 방문 기록을 만들어 표적 광고에 이용됨.
    - 개인정보 보호: 사용자의 동의 없이 개인정보가 수집될 수 있음.
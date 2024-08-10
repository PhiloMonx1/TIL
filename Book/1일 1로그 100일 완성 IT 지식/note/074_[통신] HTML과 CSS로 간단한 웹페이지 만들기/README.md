## 074 [통신] HTML과 CSS로 간단한 웹페이지 만들기

---

### 간단한 웹 페이지의 HTML
```html
<html>
    <title> My Page <</title>
    <body>
        <h2> A heading </h2>
        <p> A paragraph... </p>
        <p> Another paragraph</p>
        <img src="wikipedia.jpg" alt="Wikipedia logo" />
        <a href="http://www.wikipedia.org">link to Wikipedia</a>
        <h3> A sub-heading </h3>
        <p> Yet another paragraph </p>
    </body>
</html>
```
- 형식: HTML은 내용과 서식 정보를 결합하여 웹페이지를 구성.
- 태그 사용: <img>, <body>, <p> 등 다양한 태그로 구성되며, 시작과 끝을 표시하는 태그가 있음.
- CSS(Cascading Style Sheets) : HTML 문서의 스타일을 정의하여 일관된 서식을 제공. (HTML을 꾸미는 데 사용한다.)
- ex) 모든 h2와 h3 제목을 빨간색 이탤릭체로 표시하는 스타일 정의 가능.

HTML 과 CSS는 프로그래밍 언어가 아니다. 알고리즘을 표현할 수 없으며, 정형화된 문법과 의미 체계만 존재한다.

### CGI (Common Gateway Interface):
클라이언트에서 서버로 정보를 전달하는 메커니즘.
- `<form>` 태그를 통해 사용자 입력을 서버로 전송.
  - 자바스크립트나 서버 측 처리 없이 HTML `<form>` 만으로는 유효성 검사가 불가능하다.
  - 또한 비밀번호 입력 필드가 암호화되지 않은 채로 전송되기 때문에 주의해야 한다.
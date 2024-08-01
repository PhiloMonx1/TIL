## 045 [소프트웨어] 루프와 조건문

---

### 세 번째 자바스크립트 프로그램 : 수 합산하기
```js
var num, sum;
sum = 0;
num = prompt("추가 합산할 숫자를 입력하세요, 0을 입력할 시 종료됩니다.")
while (num != 0){
  sum = sum + parseInt(num);
  num = prompt("추가 합산할 숫자를 입력하세요, 0을 입력할 시 종료됩니다.");
}
alert(sum);
```
- [모형 컴퓨터 더하기 프로그램](../013_%5B하드웨어%5D%20모형%20컴퓨터로%20더하기%20프로그램%20만들기/README.md#일련의-수를-합산하는-모형-컴퓨터-프로그램) 실습에서 작성한 것과 동일한 프로그램이다.
- `while (num != 0)` : num이 0일 때까지 무한 반복한다.
  - 모형 컴퓨터와 달리 레이블을 별도로 만들지 않아도 된다.
  - 검사 조건이 참, 거짓으로 평가될 수 있는 것이라면 어떤 것이든 while문의 조건으로 사용 가능하다.

### 네 번째 자바스크립트 프로그램 : 일련의 수 중에서 가장 큰 수 찾기
```js
var num, max;
max = 0;
num = prompt("숫자를 입력하세요, 0을 입력할 시 종료됩니다.")
while (num != 0){
  if(parseInt(num) > parseInt(max)){
    max = num;
  }
  num = prompt("숫자를 입력하세요, 0을 입력할 시 종료됩니다.");
}
alert(max);
```
- sum 대신 max를 사용해서 가장 큰 수가 max에 담기도록 했다.
- `if(parseInt(num) > parseInt(max))` : num이 max 보다 클 경우
# StringTokenizer는 왜 split()보다 더 빠른가?

## 개요
[[백준] 11659번 : 구간 합 구하기 4 (실버3) 💭다시풀기](https://github.com/PhiloMonx1/CodingTest/commit/4e571c028966fe2230f45ca4ef91a9d8a2401447)를 하면서

`split()`을 `StringTokenizer`로 개선해보았다. 결과적으로

- 메모리 : 60436 KB -> 53960 KB
- 시간 : 584 ms -> 536 ms 

의 개선 효과를 볼 수 있었다.

## 왜 더 빠른가?
`split()`은 새로운 String 배열을 생성한다. 그러나 `StringTokenizer`는 구분자의 위치를 기억하기만 한다.
- split(): 전체 문자열을 분석하여 새로운 String 배열을 생성 
  - 복잡한 정규 표현식을 사용하여 문자열을 분리하는게 가능하다.
- StringTokenizer: 문자열을 실제로 나누지 않고, 구분자 위치만 기억. nextToken() 호출 시 해당 부분 문자열을 반환
  - 단일 문자 구분자만 사용할 수 있다.

StringTokenizer는 마치 문자열의 '뷰(view)'를 제공하는 것과 비슷하게 작동하며, 메모리와 처리 시간을 절약한다.
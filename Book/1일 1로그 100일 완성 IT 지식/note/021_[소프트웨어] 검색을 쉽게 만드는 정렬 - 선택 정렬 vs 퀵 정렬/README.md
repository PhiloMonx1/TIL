## 021 [소프트웨어] 검색을 쉽게 만드는 정렬 : 선택 정렬 vs 퀵 정렬

---

이진 검색을 사용하기 위해서는 이름이 알파벳 순으로 배치가 되어 있어야 한다. 여기서 알고리즘의 핵심 문제인 '정렬'의 개념이 등장한다.

### 정렬
항목을 순서대로 배열해서 검색이 빨리 실행될 수 있도록 하는 것.

### 선택 정렬
아직 정렬되지 않은 항목 중에서 다음 항목을 계속해서 선택하는 방식
1. 배열을 살펴보고 가장 낮은 값을 찾는다.
2. 배열의 정렬되지 않은 부분의 앞으로 가장 낮은 값을 옮긴다.
3. 배열에 있는 값의 수만큼 배열을 다시 살펴본다.
4. 위 과정을 반복한다.

[링크](https://www.w3schools.com/dsa/dsa_algo_selectionsort.php)에서 작동 방식을 애니메이션으로 확인할 수 있다.

### 이차 시간 알고리즘
배열의 크기가 2배 증가할 때 연산 횟수가 4배 증가하는 형태
- 16개의 과일을 이름순으로 정렬한다는 가정
  1. 16개 모두 확인한다.
  2. 정렬된 한 개의 과일을 제외하고 15개를 확인한다.
  3. 정렬된 두 개의 과일을 제외하고 14개를 확인한다.
  4. ... 반복한다.
  5. 최종적으로 16 + 15 + 14 + 13 ... + 1 = 126번의 연산이 필요하다.
- N * (N+1) / 2
  - 대략 일의 양은 배열의 크기의 제곱에 거의 비례한다.

### 퀵 정렬
분할 정복의 아이디어를 사용해서 정렬을 하는 방식
1. 배열 값 중 하나를 '피벗'으로 정한다. (피벗은 기준이라고 생각하면 된다.)
2. 다른 값을 이동하여 피벗보다 낮은 값은 왼쪽, 높은 값은 오른쪽에 오도록 한다.
3. 피벗과 더 높은 그룹의 첫 번째 요소의 자리를 바꾼다. (이 때 피벗은 두 그룹 사이에 위치하게 된다.)
4. 남은 그룹에서도 피벗을 정하고 과정을 반복한다.

[링크](https://www.w3schools.com/dsa/dsa_algo_quicksort.php)에서 작동 방식을 애니메이션으로 확인할 수 있다.


### 선형 로그 시간 알고리즘
열의 크기가 2배 증가할 때 연산 횟수가 약 2배보다 조금 더 증가하는 형태
- 16개의 과일을 정렬한다는 가정 (병합 정렬을 예로 들면)
  1. 16개를 8개씩 2그룹으로 나눈다.
  2. 각 8개 그룹을 4개씩 2그룹으로 나눈다.
  3. 각 4개 그룹을 2개씩 2그룹으로 나눈다.
  4. 2개씩 짝지어 정렬한다.
  5. 정렬된 2개 그룹을 4개로 합친다.
  6. 정렬된 4개 그룹을 8개로 합친다.
  7. 정렬된 8개 그룹을 16개로 합친다.
- 각 단계에서 n개의 비교가 필요하고, 총 log n 단계가 필요하다.
  - 따라서 총 연산 횟수는 약 n * log n에 비례한다.
  - 16개일 때: 16 * log₂16 = 16 * 4 = 64번의 연산
  - 32개일 때: 32 * log₂32 = 32 * 5 = 160번의 연산
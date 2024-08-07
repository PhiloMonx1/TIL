## 014 [하드웨어] 프로세서는 무조건 빠른 게 좋을까

---

### 모형 컴퓨터와 실제 프로세서의 차이
1. 실제 프로세서는 성능을 중심으로 훨씬 복잡하게 구성되어 있다.
2. 실제 프로세서의 인출, 해석, 실행 사이클은 최적화가 되어있다. 
3. 실제 프로세서에는 모형 컴퓨터 보다 많은 명령어가 들어 있다.
4. 실세 프로세서에는 누산기가 여러 개 존재한다.

### 명령어 집합 (Instruction Set)
프로세서가 제공하는 명령어 레퍼토리
- 명령어 수와 복잡도에 대한 트레이드오프 존재
  - 많은 명령어: 다양한 계산 가능
  - 적은 명령어: 작성 용이, 빠른 실행

기능성, 속도, 복잡도, 전력 소모, 프로그램 가능성 간의 트레이드오프를 고려해야 한다. 
- 많은 명령어가 이상적이나 현실적으로 고려해야 할 사항이 많기 때문

### 프로세서 성능 향상 기법
- 캐시 (Cache): 고속 메모리로 최근 사용된 명령어와 데이터 저장
- 파이프라이닝 (Pipelining): 명령어 실행 단계 겹치기
- 병렬 실행: 다수의 명령어를 동시에 처리
- 다중 코어: 여러 프로세서 코어를 단일 칩에 통합

### 사용 목적에 따른 설계 고려사항
- 데스크톱: 높은 성능 중심
- 노트북: 성능과 전력 효율성의 균형
- 모바일 기기 (휴대전화, 태블릿): 전력 효율성 중심 (예: ARM 프로세서)
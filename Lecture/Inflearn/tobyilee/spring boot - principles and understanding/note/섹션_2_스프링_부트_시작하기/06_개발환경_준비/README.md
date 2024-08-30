## [섹션 II] 06_개발환경 준비

---

### 스프링 부트 개발 환경
SpringBoot 2.7.6 기준
- 공개 JDK 다운로드 후 설치
   - Eclipse Temurin
   - Microsoft OpenJDK
   - Amazon Corretto
   - Azul JDK
   - Oracle JDK
- [SDKMAN](https://sdkman.io/) : Unix 기반 시스템에서 여러 버전의 SDK를 병렬로 관리할 수 있게 해주는 도구 (여러 자바 버전을 사용해야 하는 경우 유용하다.)
   - Java JDK의 여러 버전을 쉽게 설치하고 전환할 수 있다.
   - 명령줄 인터페이스(CLI)를 통해 SDK의 설치, 변경, 목록 조회, 제거 등을 쉽게 수행할 수 있다.
   - Windows에서는 WSL(Windows Subsystem for Linux)을 통해 사용할 수 있다.
- [Jabba](https://github.com/shyiko/jabba) : SDKMAN과 유사한 목적을 가진 Java 버전 관리 도구. 오직 Java 버전 관리에만 초점을 맞추고 있다.
   - Windows, macOS, Linux 등 다양한 운영 체제에서 사용 가능하다.
   - 명령줄 인터페이스(CLI)를 통해 Java 버전을 쉽게 관리할 수 있다.
   - 여러 Java 버전을 설치하고 전환할 수 있는 기능을 제공한다.
   - SDKMAN는 Java 외의 다른 개발키트도 관리를 할 수 있으나, Jabba는 오직 자바만 관리 대상이다.
- IDE
   - IntelliJ IDEA : Ultimate 버전과 Community 버전이 있으며 Ultimate 버전은 유료이다.
   - STS : 이클립스 기반의 IDE 이다.
   - VS code : 비추천. 상당이 불편하다.
- SpringBoot
  - [Spring Boot CLI](https://docs.spring.io/spring-boot/installing.html#getting-started.installing.cli) : Spring Boot 애플리케이션을 빠르게 프로토타이핑하고 개발할 수 있게 해주는 명령줄 도구이다.
    - 빠른 프로토타이핑: IDE 없이도 Spring 애플리케이션을 빠르게 만들 수 있다.
    - 명령줄 완성: BASH와 zsh 셸에서 명령어 자동 완성 기능을 제공한다.
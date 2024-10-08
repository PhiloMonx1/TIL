# MySQL과 MariaDB의 차이

### 개요
회사에서 시놀로지 NAS에 DEV 환경을 구축할 때 MySQL 데이터베이스를 Maria DB로 마이그레이션 하는 작업을 했다.

당시에는 MySQL 라이선스에 대한 오해가 있어서 상업적으로 사용이 불가한 줄 알고 있었다. 또한 시놀로지 NAS에서 기본적으로 MariaDB를 제공했고, MySQL 관련 설정을 검색할 때 MariaDB를 기준으로 알려주기도 했다.

마이그레이션 작업이 처음인 만큼 MySQL과 MariaDB의 차이점 및 호환성을 제대로 알고 진행해야 한다는 생각이 들어 차이점을 붕석해보았다.

### MySQL의 특징
- 간단한 구조와 쉬운 설정 : 사용자 친화적인 인터페이스와 간단한 설치 과정을 제공한다.
- 전통적인 순수 관계형 데이터베이스 : 관계형 데이터베이스의 원칙을 따르며, 데이터 간의 관계를 정의하고 관리하는 데 강점을 가지고 있다.
- 읽기 성능이 뛰어남 : 인덱스와 캐싱을 활용하여 데이터 조회 성능을 극대화한다.
- 강력한 호환성
  - 디양한 운영 체제와의 호환성 지원
  - 다양한 언어와 호환성 지원

### MariaDB
MySQL을 포크한 오픈 소스 관계형 데이터베이스
- MySQL이 Sun Microsystems에 인수된 후, 오픈 소스로서의 지속 가능성에 대한 우려로 인해 개발되었다.
- MySQL의 소스 코드를 기반으로 개발되었기에 MySQL과 높은 호환성을 유지하고 있다.
- 추가 기능
  - MySQL에 없는 기능을 추가하고 성능을 개선하는 데 중점을 두고 있다.
  - MySQL과 거의 동일한 스토리지 엔진을 지원하지만, 자체적으로 개발한 고성능 엔진도 포함한다.
- MySQL과의 높은 호환성으로 인해 마이그레이션이 간단하다.
  - 물론 버전 및 일부 기능에 따라 100% 호환이 안될 수도 있다. 그러나 우리가 처한 상황은 아니었다.

MariaDB는 MySQL과 높은 호환성을 보장하며, MySQL을 개선한 데이터베이스로 평가받는다. 

### MySQL 라이선스
- MySQL은 GPL 하에 제공되기 때문에 소스 코드를 공개하고, GPL과 호환되게 소프트웨어를 배포할 경우에만 무료로 사용할 수 있다.
- 그러나 웹 애플리케이션의 경우 MySQL을 단순히 백엔드 데이터베이스로 사용하는 것이므로 소스코드를 공개할 의무가 없다.
  - MySQL을 DB로 사용하면서 웹사이트를 운영하며, 내부 로직을 비공개로 유지하는 것은 GPL 라이선스 조건에 부합하는 것
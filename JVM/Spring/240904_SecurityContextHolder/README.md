# SecurityContextHolder와 principal (JWT 토큰은 컨트롤러나 서비스 로직에서 검증할 필요가 없다.)

## 개요
스프링 시큐리티에 JWT 토큰 사용하는 프로젝트에서 Header 로 액세스 토큰을 받아 토큰에서 UserId를 뽑아내서 User을 조회하곤 했었다.

회사에서 해당 방법으로 토큰을 핸들링 하다가 받은 피드백이 `SecurityContextHolder.getContext().getAuthentication().principal` 값을 가져오면 토큰에서 클레임을 뽑지 않아도 된다는 것이었다.

### `SecurityContextHolder`
스프링 시큐리티에서 현재 보안 컨텍스트에 대한 세부 정보를 저장하는 곳 여기에는 현재 인증된 사용자의 세부 정보가 포함된다.
- 스프링 시큐리티의 필터체인이 HTTP 요청의 헤더에서 토큰을 추출하고 검증한 후 `SecurityContextHolder`에 인증 객체를 생성하는 시점은 컨트롤러에 도달하기 전에 이루어진다.
- 그렇기에 컨트롤러에 도달한 순간 이미 헤더에서 토큰을 추출하고 검증을 마쳤다는 의미가 되며 이는 컨트롤러나 서비스 계층에서 토큰의 유효성 검사 등을 따로 진행할 필요가 없다는 뜻이다.

### `getAuthentication()`
현재 인증된 사용자의 `Authentication` 객체를 반환하는 메서드
- JWT를 사용하는 세션리스 서버의 경우 각 요청마다 JWT 토큰이 검증되고, 해당 토큰의 정보로 `Authentication` 객체가 생성된다.
- 이 `Authentication` 객체는 해당 요청의 SecurityContext에 저장된다.
- 따라서 `getAuthentication()`을 호출하면 현재 요청에 대한 단일 `Authentication` 객체가 반환되며, 일반적으로 `SecurityContextHolder`에는 오직 하나의 `Authentication` 객체만이 존재한다.

### `principal`
인증된 사용자의 주요 식별자를 반환한다. 이는 구현에 따라 다를 수 있으며, 개발자가 직접 어떤 값을 `principal`으로 사용할지 넣어준다. (마치 JWT의 클레임을 넣어주듯이 직접 설정해야 한다.)
- 일반적으로 UserDetails 인터페이스를 구현한 객체를 반환하나, 사용자의 id를 직접 넣는 경우도 있다.
  - UserDetails 구현체를 사용하면 여러 필드를 포함할 수 있다.
- JWT에서는 클레임에 여러 정보를 포함시킬 수 있고, 이를 파싱하여 사용할 수 있다.
  - ex) JWT에 userId 외에도 username, roles 등을 포함시키고, 이를 파싱하여 사용자 정의 Principal 객체를 만들어 사용할 수 있다.

#### 예시 코드
Authentication 객체 생성:
```java
UsernamePasswordAuthenticationToken authentication = 
    new UsernamePasswordAuthenticationToken(principal, credentials, authorities);
```

SecurityContext에 Authentication 설정:
```java
SecurityContext context = SecurityContextHolder.createEmptyContext();
context.setAuthentication(authentication);
```

SecurityContextHolder에 SecurityContext 설정:
```java
SecurityContextHolder.setContext(context);
```

한 번에 Authentication 설정:
```java
SecurityContextHolder.getContext().setAuthentication(authentication);
```
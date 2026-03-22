
## `@AutoConfigureTestDatabase`

### 정의
- 테스트가 실행될때 실제 DataSource 연결을 가로채고 빠르게 격리된 임베디드 DB로 교체해 주는 역할

- 실제 DB 정보를 무시하고 임베디드 DB 라이브러리가 존재하면 자동으로 Db를 띄우고 그곳으로 DataSource를 연결해 버림

### 속성

- replace = Replace.ANY (기본값): 무조건 DataSource를 임베디드 DB로 교체
- replace = Replace.NONE: 실제 DataSource를 그대로 사용 (임베디드 DB로 교체하지 않음)
- replace = Replace.AUTO_CONFIGURED: 스프링 부트가 자동으로 구성한 DataSource만 교체 (명시적으로 DataSource를 구성한 경우는 교체하지 않음)
- connection: 어떤 종류의 임베디드 DB를 사용할지 지정 (H2, HSQL, Derby 등)


### 주요 실수

1. `@DataJpaTest` 에 `@AutoConfigureTestDatabase(replace = Replace.ANY)` 가 포함되어 있음
2. `@SpringBootTest`는 `@Transactional`이 미포함
3. 임베디드 DB 의존성을 추가하지 않으면 `Cannot load driver class: org.testcontainers.jdbc.ContainerDatabaseDriver` 오류 발생함

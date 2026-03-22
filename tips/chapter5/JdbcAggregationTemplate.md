
# JdbcAggregationTemplate

### 정의

- Aggregate 단위로 DB에 데이터를 저장, 조회, 삭제를 수행하는 템플릿
  - Aggregate: 도메인 모델의 일관성을 유지하는 단위 (DDD)
  - 애그리거트 루트(Aggregate Root): 애그리거트의 진입점이 되는 엔티티


### 흔한 실수 3가지
- 더티 체킹을 믿고 save() 호출 안하는 것
  - JdbcAggregationTemplate는 영속성 컨텍스트가 없음.
- 너무 거대한 애그리거트(Huge Aggregate)를 구성하는 것
  - 보통 기존의 자식 데이터들을 전부 DELETE 하고 현재 메모리에 있는 리스트를 통째로 다시 INSERT 함
- 식별자(ID) 생성 전략을 무시하고 직접 ID를 세팅하여 저장하는 것

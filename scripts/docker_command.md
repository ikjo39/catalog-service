
### PostSQL 실행 명령어

```
docker run -d \
  --name polar-postgres \
  --net catalog-network \
  -e POSTGRES_USER=$POSTGRES_USER \
  -e POSTGRES_PASSWORD=$POSTGRES_PASSWORD \
  -e POSTGRES_DB=polardb_catalog \
  -p 5432:5432 \
  postgres:14.4
```

```shell
docker build -f docker/Dockerfile -t catalog-service .
```

### Maven을 사용해 빌드했다면

```shell
docker build --build-arg JARFILE=target/*.jar -t catalog-service .
```


### testdata 스프링 프로파일

```shell
docker run -d \
  --name catalog-service \
  --net catalog-network \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://polar-postgres:5432/polardb_catalog \
  -e SPRING_PROFILES_ACTIVE=testdata \
  -e POSTGRES_DB=polardb_catalog \
  -p 9001:9001 \
  catalog-service
```

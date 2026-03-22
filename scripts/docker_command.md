
### PostSQL 실행 명령어

```dockerfile
docker run -d \
    --name polar-postgress \
    -e POSTGRES_USER={POSTGRES_USER} \
    -e POSTGRES_PASSWORD={POSTGRES_PASSWORD} \
    -e POSTGRES_DB=polardb_catalog \
    -p 5432:5432 \
    postgres:14.4
```

```
docker build -t postgres-ply .
docker run -it --rm -e POSTGRES_PASSWORD=docker -p 5432:5432 postgres-ply
```
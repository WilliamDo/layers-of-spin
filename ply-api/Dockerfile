FROM gradle:7-jdk11
EXPOSE 8080:8080

WORKDIR /usr/src/app

COPY . .

RUN gradle installDist

CMD ["./build/install/ply-api/bin/ply-api"]
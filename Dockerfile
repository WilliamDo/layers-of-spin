FROM rust:1.53

WORKDIR /usr/src/myapp
COPY . .

RUN cargo install --path .

CMD ["./target/release/hello-rust"]
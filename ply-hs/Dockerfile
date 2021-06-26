FROM haskell:8

RUN apt-get update
RUN apt-get install -y libpq-dev

WORKDIR /usr/src/app

COPY . .

RUN stack build --system-ghc --dependencies-only 

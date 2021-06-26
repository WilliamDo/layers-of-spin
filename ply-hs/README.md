# ply-hs

On macOS, the docker build needs around 4GB of memory.

```
docker build -t ply-hs .
docker run -it --rm ply-hs stack ghci
```

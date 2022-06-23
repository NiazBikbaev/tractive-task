# Tractive Test Task

## How to build and test

```shell
./gradlew clean build
```

## How to run 
* Required JVM 17

```shell
./gradlew bootRun
```

Request to aggregate products
```shell
curl -X POST --location "http://localhost:8080/aggregate" \
    -H "Content-Type: application/json" \
    -d "{
          \"products\": [
            \"CVCD\",
            \"SDFD\",
            \"DDDF\",
            \"SDFD\"
          ],
          \"productDescription\": {
            \"CVCD\": {
              \"version\": 1,
              \"edition\": \"X\"
            },
            \"SDFD\": {
              \"version\": 2,
              \"edition\": \"Z\"
            },
            \"DDDF\": {
              \"version\": 1
            }
          }
        }"
```

# Docker

```shell
./gradlew clean bootJar && docker build . -t tractive && docker run -p 8080:8080 tractive
```
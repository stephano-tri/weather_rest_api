## Weather Service

### Stack
Java 17    
SpringBoot 3.1.1    
Mysql    
ElasticSearch    
Webflux

## Before you install
`if you need to volume mounting`                         
`edit docker-compose.yml(mysql, elasticsearch)`    
`remove comment and change path(/Users/Eomjihwan <- this part)`

## How to run
_**remember this project need JAVA17 !!**_
```
./gradlew build && docker-compose build && docker-compose up -d
```

## DB Install
_**db_password -> stephano-tri(infer docker-compose.yml)**_

```
docker exec -it mysql bin/bash
mysql -uroot -p --port 3306 < init.sql
```
_**Jasync not support caching_sha2_password**_
```
ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY 'stephano-tri';
```

## Architecture
```
@PostConstructor를 통하여 Spring Application 시작시 공공API에서 날씨 정보를 수집합니다(-> MYSQL)

Spring Webflux를 통하여 비동기적으로 날씨 정보를 제공합니다(<- MYSQL)

SpringBoot(log4j2)를 통하여 로그를 수집합니다(-> FILESYSTEM)

실패한 Request들에 대하여 로그를 수집합니다(-> ELASTICSEARCH)

```

### Spring Boot log read(file system)

```
docker exec -it weather-service bin/bash
tail -f logs/weather-service.log
```

### Request log(only failed request collect)
```
curl --location --request GET 'localhost:9200/request.log/_search'

OR

localhost:9200/request.log/_search
```

### DB diagram
![image](https://github.com/stephano-tri/weather_rest_api/assets/62496713/c3c9df7b-89a9-460d-9bee-505db609ca7d)


### API Docs
```
Notion Links
```

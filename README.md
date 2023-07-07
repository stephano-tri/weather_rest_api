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
```
docker exec -it mysql bin/bash
mysql -u root -p --port 3306 < init.sql
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

## Spring Boot log read(file system)

```
docker exec -it weather-service bin/bash
tail -f logs/weather-service.log

db_password -> stephano-tri(infer docker-compose.yml)
```

## Request log read(only failed request collect)
```
curl --location --request GET 'localhost:9200/request.log/_search'

OR

localhost:9200/request.log/_search
```

## DB Backup
_**If you want to daily backup , use this command in cron, crontab**_
```
docker exec mysql bash -c 'exec mysqldump --databases "weather" -h mysql -u"root" -p"stephano-tri"' > gzip > backup/snapshot-001
```

## DB diagram
![image](https://github.com/stephano-tri/weather_rest_api/assets/62496713/c3c9df7b-89a9-460d-9bee-505db609ca7d)


## API Docs
#### i) 주소 코드 조회
**RequestParam**    
 > **type** : short, mid, mid-temp    
  **regionName** : 지역명(short의 경우 동이름까지도 지원합니다. mid는 시,도명만 지원합니다)

**Example**
```
curl --location --request GET 'localhost:8080/api/v1/forecast/address/search?type=short&regionName=인천'
```

* * *

#### ii) 단기 예보 조회
**RequestParam**    
>  **regionCode** : 지역 코드(주소 코드 조회에서 불러온 코드를 사용합니다.)   

**Example**
```
curl --location --request GET 'localhost:8080/api/v1/today/forecast/load?regionCode=11A34CB00'
```

* * *

#### iii) 중기 예보 조회
**RequestParam**
>  **regionCode** : 지역 코드(주소 코드 조회에서 불러온 코드를 사용합니다.)    

**Example**
```
curl --location --request GET 'localhost:8080/api/v1/midterm/forecast/load?regionCode=11A34CB00'
```

* * *

#### iv) 중기-기온 예보 조회
**RequestParam**
>  **regionCode** : 지역 코드(주소 코드 조회에서 불러온 코드를 사용합니다.)
 
**Example** 
```
curl --location --request GET 'localhost:8080/api/v1/midterm/temperature/forecast/load?regionCode=11A34CB00'
```

* * *


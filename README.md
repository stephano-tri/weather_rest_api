## Weather Service

### Stack
Java 17    
SpringBoot 3.1.1    
Mysql    
ElasticSearch    
Webflux

### How to run
_**remember this project need JAVA17 !!**_
```
./gradlew build && docker-compose build && docker-compose up -d
```

### DB authenticate issue
_**Jasync not support caching_sha2_password**_
```
ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY 'stephano-tri';
```

### Architecture

### DB Schema

### Spring Boot log read(file system)

```
docker exec -it {container_id} bin/bash
tail -f logs/my-app.log
```

### API Docs
```
Notion Links
```

### 이 프로젝트는 강의를 들어가면서 원본 소스에 설명과 하나하나 코딩해 가면서 연습한 스프링부트 + JPA 프로젝트입니다.
- 원본은 최주호(겟인데어) 강사님 코드와 강의를 참고하였습니다.
- 포토그램 - 인스타그램 클론 코딩
- 원본 github : https://github.com/codingspecialist/EaszUp-Springboot-Photogram-Start

### STS 툴 버그가 발견되었습니다.
- 아래 주소로 가서 4.0.6 버전으로 설치해주세요. 아니면 의존성 다운로드 79프로에서 무한루프가 발생합니다.
- https://github.com/spring-projects/sts4/wiki/Previous-Versions

### STS 툴에 세팅하기 - 플러그인 설정
- https://blog.naver.com/getinthere/222322821611

### 의존성

- Sring Boot DevTools
- Lombok
- Spring Data JPA
- MariaDB Driver
- Spring Security
- Spring Web
- oauth2-client

```xml
<!-- 시큐리티 태그 라이브러리 -->
<dependency>
	<groupId>org.springframework.security</groupId>
	<artifactId>spring-security-taglibs</artifactId>
</dependency>

<!-- JSP 템플릿 엔진 -->
<dependency>
	<groupId>org.apache.tomcat</groupId>
	<artifactId>tomcat-jasper</artifactId>
	<version>9.0.43</version>
</dependency>

<!-- JSTL -->
<dependency>
	<groupId>javax.servlet</groupId>
	<artifactId>jstl</artifactId>
</dependency>
```

### 데이터베이스

```sql
create user 'cos'@'%' identified by 'cos1234';
GRANT ALL PRIVILEGES ON *.* TO 'cos'@'%';
create database photogram;
```

### yml 설정

```yml
server:
  port: 8080
  servlet:
    context-path: /
    encoding:
      charset: utf-8
      enabled: true
    
spring:
  mvc:
    view:
      prefix: /WEB-INF/views/
      suffix: .jsp
      
  datasource:
    driver-class-name: org.mariadb.jdbc.Driver
    url: jdbc:mariadb://localhost:3306/cos?serverTimezone=Asia/Seoul
    username: cos
    password: cos1234
    
  jpa:
    open-in-view: true
    hibernate:
      ddl-auto: update
      naming:
        physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
    show-sql: true
      
  servlet:
    multipart:
      enabled: true
      max-file-size: 2MB

  security:
    user:
      name: test
      password: 1234   

file:
  path: C:/src/springbootwork-sts/upload/
```

### 태그라이브러리

```jsp
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
```


### 프로젝트 관련 확인쿼리는 참고용 파일로 소스코드에서 사용하진 않습니다.
```/src/main/resources/phtogram_query.sql```

### 페이스북 로그인 연동
-  페이스북 개발자 센터에서 앱을 신규로 등록하고 해당 정보 참고해서
-  application.yml 안에 client-id, client-secret, scope 항목을 추가로 설정해 줍니다.
```
    oauth2:
      client:
        registration:
           facebook:
              client-id: 페북 개발자 센터에 등록한 앱아이디 #git 에 올릴때 주석처리 필요
              client-secret: 페북 개발자 센터에 등록한 앱의 비밀정보  #git 에 올릴때 주석처리 필요
              scope:
              - public_profile
              - email
 ```
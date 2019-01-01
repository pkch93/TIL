## 2018년 12월 (December 2018)

***목표***

1. Spring으로 Restful API 서버 만들기
    - [백기선 님의 스프링 기반 REST API 개발](https://www.inflearn.com/course/spring_rest-api/)
2. Node.js && Express.js && MongoDB
3. Numpy && Pandas & matplotlib

### 19일 수요일 (Wendesday)

- SSAFY

1. Flask로 웹 서버 개발하는 방법

[SSAFY에서 배운 것 보러가기](https://github.com/chulsea/TIL/tree/master/2018/December/1219)

- Spring Restful API 만들기

    HAL (Hypertext Application Language) / [참고](http://stateless.co/hal_specification.html)

HAL은 API의 resource 사이에 hyperlink를 제공해주는 간단한 format이다.

### 20일 목요일 (Thursday)

- SSAFY

1. Flask request / redirect

[SSAFY에서 배운 것 보러가기](https://github.com/chulsea/TIL/tree/master/2018/December/1220)

- Spring Restful API 만들기

- 데이터 사이언스 by python
    - pandas groupby

[학습노트](20181220.md)

### 21일 금요일 (Friday)

- SSAFY

[SSAFY에서 배운 것 보러가기](https://github.com/chulsea/TIL/tree/master/2018/December/1221)

- Spring Restful API 만들기

- 데이터 사이언스 by python
    - pandas (다양한 종류의 데이터 handling)

[학습노트](20181221.md)

### 22일 토요일 (Saturday)

- Spring Restful API 만들기 (O)

- Spring Security와 JWT 조합해보기 (->)

- 데이터 사이언스 by python (O)
    - pandas
        1. pivot table / crosstab
        2. merg & concat
        3. DB connection & persistance

[학습노트](20181222.md)

### 23일 일요일 (Sunday)

- Spring Restful API 만들기
    - Spring HATEAOS

- Spirng Security와 JWT 토큰 인증 적용해보기

- 데이터 사이언스 by python
    - pandas 복습
    - matplotlib

[학습노트](20181223.md)

### 24일 월요일 (Monday)

- Spring Restful API 만들기

- Spring Security & OAuth2
    authorization_code 방식으로 OAuth2를 적용하는 것에 대해 공부가 더 필요해보인다.

    특히 web front 서버에서 oauth 방식으로 구글, 페이스북 등의 로그인을 할 때 어떻게 해야할 지 감이 오지않아 추가적인 공부가 필요

- 데이터 사이언스 by python
    - data handling

[학습노트](20181224.md)

### 25일 화요일 (Tuesday)

- Card 공유 사이트 (toy project)

    Python / Flask를 활용한 toy project

[CardShare](https://github.com/pkch93/cardshare)

### 26일 수요일 (Wendesday)

- SSAFY

[SSAFY에서 배운 것 보러가기](https://github.com/chulsea/TIL/tree/master/2018/December/1226)

### 31일 월요일 (Monday)

25일부터 31일 간은 cardshare 개발을 함

[cardshare 홈페이지](http://cardshare.space) / [cardshare github](https://github.com/pkch93/cardshare)

개발 후기

1. flask

    이번에 ssafy에서 학습하고 까먹기전에 개발에 사용하기 위해 이번 프로젝트에서 사용

    > flask는 python의 web micro framework

    - sqlalchemy
    
        python flask에서 사용하는 ORM 툴
        가볍게 CRUD는 구현할 수 있으나 아직 작동원리 등을 파악하지는 못했다.
        
        추후에 한번쯤 작동원리에 대해 학습해보고 싶다.

    - blueprint

        flask에서 다른 코드와 관련된 뷰를 그룹화 하는 방법이다.

        blueprint를 통해 라우팅을 단순화 할 수 있으며 좀 더 깔끔한 코드를 작성할 수 있다고 생각한다.
        
2. AWS

    - S3

        python에서는 boto3 모듈로 S3에 접근한다.

    - Cors 권한

        Cropper 문제 때문에 python에서는 S3 Upload를 하지 못하고 javascript에서 적용하였다. 때문에 Cors 처리를 해주어야 했다.

    - 정책 설정

        S3 버킷을 제어하는 방식에 대한 정책, 정책 생성기로 손쉽게 정책을 만들어낼 수 있다.

3. heroku

    - deploy

        Heroku app을 만들고 `git push heroku master`를 통해 손쉽게 배포가 가능하다는게 헤로쿠의 가장 큰 장점이라고 생각한다.

    - Procfile

        Heroku 배포시 실행 명령을 적는 파일, gunicorn을 사용하여 서버를 실행시켰다.

    - postgresql

        Heroku에서 기본으로 제공해주는 DBMS, addons 명령으로 손쉽게 사용할 수 있었다.

4. cropper.js

    사진을 자르는 js 모듈

5. Cross-browsing

    - safari
        
        -webkit-transform: rotateY(-180deg);

        ~~위 css 속성이 왜 먹는지 아직도 이해가 안된다...~~
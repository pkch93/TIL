# 2022.03.20 TIL - Spring Boot 앱 Heroku에 배포하기

- [2022.03.20 TIL - Spring Boot 앱 Heroku에 배포하기](#20220320-til---spring-boot-앱-heroku에-배포하기)
  - [system.properties](#systemproperties)
  - [Procfile](#procfile)
  - [배포](#배포)
  - [Trouble Shooting](#trouble-shooting)
    - [****H10 - App crashed****](#h10---app-crashed)
    - [****H14 - No web dynos running****](#h14---no-web-dynos-running)
    - [****H20 - App boot timeout****](#h20---app-boot-timeout)
  - [참고](#참고)

> Java 11, Spring Boot 2.6.4를 기반으로 테스트하였음.

먼저 Spring Boot 앱을 세팅한다. 세팅 후 `/health` 가 제대로 호출되는 지 확인해 볼 수 있도록 배포해본다.

```groovy
package dev.pkch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
```

## system.properties

기본적으로 Heroku는 Java 8버전을 사용한다. 만약 다른 Java 버전이 필요하다면 별도 설정이 필요하다.

이를 위해서 system.properties를 사용할 수 있다.배포

```
java.runtime.version=11
```

`java.runtime.version`으로 띄울 애플리케이션의 Java 버전을 설정할 수 있다. 위와 같이 11로 설정하면 JDK 11을 기반으로 애플리케이션이 띄워진다.

## Procfile

dyno 설정이 필요하다. 지금 띄우는 Spring Boot 앱은 웹 애플리케이션이므로 web으로 설정한 Procfile을 프로젝트 최상위에 다음과 같이 만든다.

```
web: java -jar -Dserver.port=$PORT build/libs/application.jar
```

web dyno로 설정을 해야 HTTP를 통해 통신이 가능하다.

> Procfile은 `<process type>: <command>` 형식으로 작성한다. 오른쪽 커맨드가 process type에 해당하는 애플리케이션을 띄울때 사용하는 명령이다.

Procfile 참고
> 
> 
> [Procfile](https://www.notion.so/Procfile-7ca9219256e149e3bc5b25c9a9cd3dfe)
> 

jar를 실행할때 두 가지 포인트가 있다.

1. `-Dserver.port=$PORT`
    
    먼저 서버가 띄워지는 Port를 변경해야한다. Heroku는 애플리케이션이 띄워지는 포트를 `$PORT`로 지정한다.
    
2. `build/libs/application.jar`
    
    보통 Spring Boot의 bootJar task는 `<archiveBaseName>-<version>.jar` 방식으로 생성된다. 다만, Spring Boot 2.5부터는 jar 명령을 통해 ``<archiveBaseName>-<version>-plain.jar`가 추가된다.
    
    빌드한 jar가 생성되는 폴더인 `build/libs`에 여러 jar가 생성되는 상황이므로 bootJar의 이름은 `application.jar`로 변경하고 application.jar를 실행하도록 변경한 것이다.
    
    ```groovy
    // build.gradle
    
    ...
    
    bootJar {
        archiveFileName = 'application.jar'
    }
    
    ...
    ```
    

## 배포

> 배포 방식은 Heroku CLI를 통한 Heroku Git 방법을 사용한다.
> 

이제 모든 설정은 끝났다. 이를 배포하기 위해서는 먼저 Heroku에 로그인해야한다.

```bash
$ heroku login
```

그리고 Heroku에 올라간 애플리케이션의 repository를 클론해야한다.

```bash
$ heroku git:clone -a pkch-dev-root
```

위 명령을 사용하면 Heroku의 repostiory를 바라보는 remote 설정이 추가된다. 이제 push를 하면 배포가 된다.

```bash
$ git push heroku main
```

remote 원격 브랜치는 heroku로 설정된다. 따라서 heroku에 배포할 브랜치를 push하면 배포가 진행된다.

## Trouble Shooting

Heroku로 배포시 문제가 생겼을 경우 로그를 통해 어떤 에러인지 확인할 수 있다.

> 로그는 `heroku logs` 명령으로 확인할 수 있다.

로그에는 [Heroku에서 정의한 에러코드](https://devcenter.heroku.com/articles/error-codes)도 함께 남는다.

### ****[H10 - App crashed](https://devcenter.heroku.com/articles/error-codes#h10-app-crashed)****

애플리케이션이 제대로 뜨지 않았을때 발생하는 에러코드이다.

`buildpacks` 설정이 잘못된 경우에 발생하기도 하는데 이경우는 buildpacks를 삭제하고 다시 설정한다.

```bash
$ heroku buildpacks:clear
$ heroku buildpacks:set heroku/gradle
```

### ****[H14 - No web dynos running](https://devcenter.heroku.com/articles/error-codes#h14-no-web-dynos-running)****

dyno 설정이 제대로 되지 않은 경우에 H14 에러가 발생할 수 있다. 다음과 같이 해결할 수 있다.

1. Procfile 설정
    
    ```
    web: java -jar -Dserver.port=$PORT build/libs/application.jar
    ```
    
    참고로 Heroku는 내부적으로 애플리케이션이 뜨는 포트를 동적으로 할당하기 때문에 애플리케이션이 뜨는 포트로 매핑을 해주어야한다. `-Dserver.port=$PORT`로 설정할 수 있다.
    
2. dyno 세팅 명령 실행 
    
    ```bash
    heroku ps:scale web=1
    ```

### ****[H20 - App boot timeout](https://devcenter.heroku.com/articles/error-codes#h20-app-boot-timeout)****

앱은 제대로 떠서 H10 에러는 발생하지 않는데 앱 부팅 체크가 타임아웃나는 경우이다. 기본적으로 애플리케이션이 뜨고 앱부팅 요청을 보내는데 이 타임아웃 시간이 75초이다. 75초 이내에 정상 응답이 나오지 않으면 Heroku 에러 페이지를 띄운다.

앱은 빌드, 띄우는거까지 성공하는데 앱이 띄워진 포트가 매핑이 되지 않아서 발생한다. 따라서 포트를 매핑해주면 잘 동작한다.

jar 실행시 `-Dserver.port=$PORT`를 추가하여 설정할 수 있다.

## 참고

Spring Boot 앱 배포하기: [https://devcenter.heroku.com/articles/deploying-spring-boot-apps-to-heroku](https://devcenter.heroku.com/articles/deploying-spring-boot-apps-to-heroku)
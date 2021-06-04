# 2021.06.04 TIL - Spring Boot Profile Config

Spring Boot 2.4부터 애플리케이션 구성 파일의 작동 방식이 변경되었다.

> 애플리케이션 구성 파일이란 `application.yml`, `application.properties`를 의미

## Spring Boot 2.4 미만 `as-is`

SpringApplication은 다음 위치의 애플리케이션 구성 파일을 읽어 설정들을 로드한다.

1. 현재 디렉터리의 `/config`
2. 현재 디렉터리
3. classpath의 `/config`
4. classpath root

위 순서는 애플리케이션 구성 파일의 우선순위이다.

> ConfigFileApplicationListener를 보면 `file:./`와 `file:./config/`가 있는 걸 볼 수 있다. 여기서 file의 기본 경로는 현재 프로젝트 폴더이다. 따라서 현재 프로젝트 폴더의 `/config` 하위나 프로젝트 폴더의 애플리케이션 구성 파일을 의미한다.
>
> current directory 참고: [https://stackoverflow.com/questions/40894087/spring-boot-external-properties-file-in-current-directory-is-ignored](https://stackoverflow.com/questions/40894087/spring-boot-external-properties-file-in-current-directory-is-ignored)
>
> Java classpath 참고: [https://velog.io/@_anna/classpath](https://velog.io/@_anna/classpath)
>
> 자바 스프링 자원 읽기 혹은 경로 알기 참고: [https://syaku.tistory.com/342](https://syaku.tistory.com/342)

### profile-specific properties

`application.properties`나 `application.yml`에 특정 profile에서만 동작하도록 만들고 싶다면 다음과 같이 `application-{profile}.properties` 또는 `application-{profile}.yml`로 `-` 로 구분하여 profile를 작성을 명시하면 된다. 만약 `application-prod.yml`이라면 prod profile에서 동작한다. 만약 `application.yml`과 같이 아무 profile을 명시하지 않으면 default로 동작한다.

여러 profile이 명시된 경우 last-wins 전략이 사용된다. 만약 `spring.profiles.active`에 명시된 profile은 가장 나중에 추가되므로 최우선 순위의 profile이 된다.

### Multi-profile YAML

yaml의 경우 한 파일에 여러 profile에 대한 설정을 할 수 있도록 지원한다.

```yaml
server:
  address: 192.168.1.100
---
spring:
  profiles: development
server:
  address: 127.0.0.1
---
spring:
  profiles: production & eu-central
server:
  address: 192.168.1.120
```

위와 같은 application.yml이 있다고 가정한다. 이 경우 `development`의 `server.address`는 `127.0.0.1`, `production`과 `eu-central`의 `server.address`는 `192.168.1.120`, 그외 profile은 `192.168.1.100`이 된다. 즉, 맨 위에 명시된 설정이 기본 설정으로 사용되는 것이다.

### Adding Active Profiles

때때로 특정 profile에 대한 설정이 추가되도록 active profile을 추가하는 방법이 유용할 수 있다. 이를 위해서 `spring.profiles.include`를 통해 지원한다.

```yaml
spring.profiles: prod
spring.profiles.include:
  - proddb
  - prodmq 
```

위와 같이 `prod` profile로 애플리케이션을 실행하면 `prod`와 함께 `proddb`와 `prodmq`가 함께 활성화된다.

## Spring Boot 2.4 이상 `to-be`

Spring Boot 2.4에서 profile 설정 관련하여 약간의 변화가 생겼다.

1. Optional Locations
2. `spring.config.import`
3. `spring.profiles` deprecated
4. `spring.profiles.include` deprecated
5. 외부 jar의 애플리케이션 설정 파일 로드시 우선순위 변경
6. multi-profile YAML의 설정 값 로드 순서 변경

### Optional Locations

기본적으로 `spring.config.location`에 정의된 애플리케이션 구성 파일이 실제로 존재하지 않으면 `ConfigDataLocationNotFoundException`가 발생한다. 만약 구성 파일이 존재할 지 확신할 수 없다면 구성 파일의 prefix로 `optional:`을 붙여서 존재하지 않더라도 예외가 발생하지 않도록 할 수 있다.

### spring.config.import

기존에는 구성 파일의 위치에 대해서만 처리할 수 있었다. `spring.config.location`와 `spring.config.additional-location`로 구성 파일의 위치와 우선순위를 지정할 수 있었다.

Spring Boot 2.4부터는 원하는 구성 파일을 사용할 수 있도록 `spring.config.import`를 지원한다.

```yaml
spring:
  application:
    name: myapp
  config:
    import: file:./dev.properties
```

위와 같이 정의하면 `file:./dev.properties`의 설정을 가져온다.

### spring.profiles deprecated

Spring Boot 2.4부터 `spring.profiles`가 `@Deprecated`되었다. 향후 2.6에 삭제될 예정이다.
이제 `spring.profiles`를 `spring.config.activate.on-profile`로 대체해야한다.

- as-is

    ```yaml
    spring:
        profiles: prod
    ```

- to-be

    ```yaml
    spring:
      config:
        activate:
          on-profile: prod
    ```

### spring.profiles.group

Spring Boot 2.4부터는 `spring.profiles.group`을 지원한다. 그룹별로 로드될 profile을 명시하도록 바뀐것이다.

```yaml
spring:
  config:
    activate:
      on-profile: "debug"
  profiles:
    include: "debugdb,debugcloud"
```

위와 같은 `application.yml`이 있다고 가정한다. 위 구성파일에 `spring.profiles.include`는 더이상 유효하지 않다. 이를 `spring.profiles.group`으로 변경해야한다.

```yaml
spring:
  profiles:
    group:
      "debug": "debugdb,debugcloud"
```

위와 같이 group에 debug를 정의할 수 있다.

참고로 `spring.profiles.group`은 profile-specific documents에서는 동작하지 않는다. 즉, `spring.config.activate.on-profile`과는 동작하지 않는다. 따라서 group을 따로 정의하고 해당 group을 사용하도록 변경해야한다.

```yaml
spring:
  profiles:
    group:
      debug: "debugdb,debugcloud"

---
spring:
  config:
    activate:
      on-profile: debug

```

### 외부 jar의 애플리케이션 설정 파일 로드시 우선순위 변경

Spring Boot는 외부 jar의 애플리케이션 구성 파일을 로드할 수 있도록 지원한다. 이때 Spring Boot 2.4 이전에는 외부 구성 파일이 애플리케이션 jar의 애플리케이션 구성 파일의 설정을 override하지 못했다.

Spring Boot 2.4부터는 외부에서 로드하는 애플리케이션 구성 파일이 패키징된 jar의 애플리케이션 구성 파일을 override하도록 지원한다.

### multi-profile YAML의 설정 값 로드 순서 변경

Spring Boot 2.4 미만에서는 profile의 우선순위에 따로 설정값을 로드했다. 다만 이 방식이 2.4부터는 더 높은 우선순위를 가지도록 하고 싶다면 파일의 더 하위에 위치해야한다.

```yaml
app.duration-field: 5s

---
spring.config.activate.on-profile: filter
app.duration-field: 5m

---
app.duration-field: 5h
```

즉, 위와 같이 정의하는 경우 제일 하위의 `app.duration-field: 5h`가 적용된다. 단, 이 경우 위 `spring.config.activate.on-profile` 선언도 통하지 않는다. 맨 아래 `app.duration-field: 5h`의 우선순위가 가장 높기 때문이다.

### 참고

Spring Boot 2.3.11.RELEASE: [https://docs.spring.io/spring-boot/docs/2.3.11.RELEASE/reference/html/spring-boot-features.html#boot-features-external-config](https://docs.spring.io/spring-boot/docs/2.3.11.RELEASE/reference/html/spring-boot-features.html#boot-features-external-config)

Spring Boot 2.4.6: [https://docs.spring.io/spring-boot/docs/2.4.6/reference/html/spring-boot-features.html#boot-features-external-config](https://docs.spring.io/spring-boot/docs/2.4.6/reference/html/spring-boot-features.html#boot-features-external-config)

Spring Boot 2.4 Release Notes: [https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.4-Release-Notes](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.4-Release-Notes)

2.4 부터 변경된 구성파일 처리방식 살펴보기: [http://honeymon.io/tech/2021/01/16/spring-boot-config-data-migration.html](http://honeymon.io/tech/2021/01/16/spring-boot-config-data-migration.html)

Spring Boot Config Data Migration Guide: [https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-Config-Data-Migration-Guide](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-Config-Data-Migration-Guide)

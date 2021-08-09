# 2021.08.03 TIL - 여러 Spring Data 모듈을 사용했을 때

- [2021.08.03 TIL - 여러 Spring Data 모듈을 사용했을 때](#20210803-til---여러-spring-data-모듈을-사용했을-때)
  - [참고](#참고)

Spring Data 프로젝트에서는 다양한 데이터 저장소와의 연동을 지원한다.

RDBMS와의 연동을 지원하는 Spring Data JDBC와 ORM을 위한 Spring Data JPA, Spring Data MongoDB, Spring Data Redis 등을 지원한다.

> [Spring Data 모듈 참고](https://spring.io/projects/spring-data)

때로는 프로젝트의 구성에 따라 하나의 프로젝트에서 여러 Spring Data 모듈을 사용할 수 있다.

캐시를 위해 redis를 사용하고 데이터 저장소로 RDBMS를 사용하는 경우가 많은데 만약 이 시나리오대로 `spring-data-jpa`와 `spring-data-redis` 의존성을 설정하고 애플리케이션을 실행하면 다음과 같은 INFO 로그가 남는 것을 확인할 수 있다.

```
Spring Data Redis - Could not safely identify store assignment for repository candidate interface {repository 패키지}. If you want this repository to be a Redis repository, consider annotating your entities with one of these annotations: org.springframework.data.redis.core.RedisHash (preferred), or consider extending one of the following types with your repository: org.springframework.data.keyvalue.repository.KeyValueRepository.
```

이는 여러 Spring Data 의존성을 가지고 실행하는 경우에 찍히는 로그이다. Spring 애플리케이션은 여러 Repository Factory를 찾게 되면 Strict Repository Configuration Mode로 실행을 하게 되는데 Strict Repository Configuration Mode가 위 로그를 찍는 것이다.

만약 redis를 캐시로 `RedisTemplate`을 활용하고 있다면 Repository 설정이 필요없을 수 있다.

이 경우에 위 로그가 나오지 않게 만들고 싶다면 `spring.data.redis.repositories.type = none`으로 설정하면된다. 명시적으로 `spring-data-redis`의 repository를 사용하지 않겠다는 의미이다.

## 참고

[https://stackoverflow.com/questions/39432764/info-warnings-about-multiple-modules-in-spring-boot-what-do-they-mean](https://stackoverflow.com/questions/39432764/info-warnings-about-multiple-modules-in-spring-boot-what-do-they-mean)

[https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.multiple-modules](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.multiple-modules)

[https://www.baeldung.com/spring-multiple-data-modules](https://www.baeldung.com/spring-multiple-data-modules)
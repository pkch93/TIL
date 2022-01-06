# 2022.01.06 TIL - Spring Validation @Valid vs @Validated

- [2022.01.06 TIL - Spring Validation @Valid vs @Validated](#20220106-til---spring-validation-valid-vs-validated)
  - [@Valid](#valid)
  - [@Validated](#validated)
  - [참고](#참고)

JSR-303에는 Bean Validation에 대한 규약이 정의되어있다. 

Spring 프레임워크에서도 JSR-303 규약을 따라서 Bean Validation을 지원한다.

Spring Boot를 사용한다면 `org.springframework.boot:spring-boot-starter-validation` 의존을 사용하여 Bean Validation을 사용할 수 있다.

```
// build.gradle
implementation 'org.springframework.boot:spring-boot-starter-validation'
```

> 참고로 Spring Boot 2.3 이하에서는 spring-boot-starter-web 의존성에 spring-boot-starter-validation 의존이 포함되어 있다.

## @Valid

JSR-303에 정의된 어노테이션이다. 즉, 자바 표준에서 정의된 어노테이션이다.

위 어노테이션을 사용하여 유효성 검사를 하고자하는 객체의 유효성을 검사할 수 있다.

```java
@Target({ METHOD, FIELD, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Documented
public @interface Valid {
}
```

메서드, 필드, 생성자, 파라미터 그리고 타입 사용시에 사용이 가능하다.


```java
public class PostRequest {
    @NotNull
    private Long memberNumber;

    @NotNull
    @Size(min = 5, max = 100)
    private String title;

    @NotNull
    private String contents;

    private PostRequest() {}

    public PostRequest(Long memberNumber, String title, String contents) {
        this.memberNumber = memberNumber;
        this.title = title;
        this.contents = contents;
    }

    // getter...
}
```

위 PostRequest 객체의 유효성을 검사하고자 한다. 이를 위해서 위 객체를 받는 메서드 핸들러에서 `@Valid`를 붙여 검사할 수 있다.

```java
@Slf4j
@RestController
@RequestMapping("/api/posts")
public class PostApiController {

    @PostMapping("/valid")
    public void createPostByValid(@Valid @RequestBody PostRequest request) {
        log.info("request: {}", request);
    }
}

```

위와 같이 `@RequestBody`로 받는 PostRequest에 `@Valid` 붙여 PostRequest에 정의한 규약들을 검사할 수 있다.

## @Validated

`@Validated`는 스프링에서 제공하는 어노테이션이다. 즉, 스프링에 특화된 어노테이션이다.

`@Valid`의 기능에 더해 그룹으로 묶어 유효성 검사를 할 수 있는 기능을 제공한다.

```java
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Validated {

    Class<?>[] value() default {};

}
```

`@Valid`와 마찬가지로 기능을 제공한다. 앞서 PostReuqest를 검사할 때 사용했던 `@Valid`를 `@Validated`로 대체해도 정상 동작한다.

```java
@Slf4j
@RestController
@RequestMapping("/api/posts")
public class PostApiController {

    @PostMapping("/validated")
    public void createPostByValidated(@Validated @RequestBody PostRequest request) {
        log.info("request: {}", request);
    }
}
```

`@Validated`는 클래스, 메서드, 파라미터에 붙일 수 있다. 단, 클래스, 메서드에 붙인 경우에는 유효성 검사의 그룹을 설정하는 정도로만 기능한다.

```java
@Slf4j
@RestController
@RequestMapping("/api/posts")
public class PostApiController {

    @PostMapping @Validated
    public void createPost(@RequestBody PostRequest request) {
        log.info("request: {}", request);
    }
}
```
위 createPost에서 PostRequest에 대한 유효성 검사는 하지 않는다.

그 밖에 컨트롤러에서는 클래스에 `@Validated`가 없어도 스프링의 유효성 검사 기능을 지원받을 수 있지만 서비스 빈, 컴포넌트 빈에서 유효성 검사를 지원받으려면 클래스 수준에서 `@Validated` 어노테이션을 붙여야한다.


## 참고

[Spring Java Bean Validation](https://docs.spring.io/spring-framework/docs/5.3.9/reference/html/core.html#validation-beanvalidation)

[Validation 어디까지 해봤니?](https://meetup.toast.com/posts/223)

[Validation in Spring Boot](https://www.baeldung.com/spring-boot-bean-validation)
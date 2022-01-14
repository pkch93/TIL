# 2022.01.14 TIL - 컨트롤러와 컴포넌트 빈에서의 ConstraintValidator

- [2022.01.14 TIL - 컨트롤러와 컴포넌트 빈에서의 ConstraintValidator](#20220114-til---컨트롤러와-컴포넌트-빈에서의-constraintvalidator)
  - [컨트롤러에서 ConstraintValidator 사용](#컨트롤러에서-constraintvalidator-사용)
  - [컴포넌트 빈에서 ConstraintValidator](#컴포넌트-빈에서-constraintvalidator)

`@Validated`가 컨트롤러와 컴포넌트 빈에서 동작이 달랐듯이 ConstraintValidator도 약간 상이한 동작을 보이는 듯하다.

## 컨트롤러에서 ConstraintValidator 사용

기존에 Bean Validation을 사용했던 것과 마찬가지로 `@Valid`를 검증하고자하는 파라미터에 붙이면 Bean Validation이 동작한다.

```java
@Constraint(validatedBy = SeoulPhoneNumberValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckSeoulPhoneNumber {
    String INVALID_SEOUL_PHONE_FORMAT_MESSAGE = "서울지역 번호가 아닙니다.";

    String message() default INVALID_SEOUL_PHONE_FORMAT_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomValidRequest {

  @CheckSeoulPhoneNumber
  private String a;

  @Min(10)
  private int b;
  
  private boolean c;

  @Builder
  public CustomValidRequest(String a, int b, boolean c) {
      this.a = a;
      this.b = b;
      this.c = c;
  }
}
```

다음과 같이 클래스 전체가 아닌 `a` 필드만 `@CheckSeoulPhoneNumber`으로 검증하는 경우에도 가능하다.

```java
@Slf4j
@RestController
@RequestMapping("/api/custom/validation")
public class CustomValidationApiController {

  @GetMapping
  public void customValidation(@Valid CustomValidRequest request) {
      log.info("request: {}", request);
  }
	// ...
}
```

다음과 같이 `@Valid`를 붙이면 `a` 에 대해서 `SeoulPhoneNumberValidator`가 Bean Validation 동작한다.

```java
public class SeoulPhoneNumberValidator implements ConstraintValidator<CheckSeoulPhoneNumber, String> {
  private static final Pattern SEOUL_PHONE_REGEX = Pattern.compile(
      "^02-[0-9]{3,4}-[0-9]{3,4}$"
  );

  @Override
  public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
      return Objects.nonNull(phoneNumber) && SEOUL_PHONE_REGEX.matcher(phoneNumber).matches();
  }
}
```

```java
@Test
void customValidation_thenFalied() throws Exception {
    // given & when
    MockHttpServletResponse response = mockMvc.perform(get("/api/custom/validation")
        .queryParam("a", "054-000-0000") // 서울 지역 번호 포멧이 아니므로 실패
        .queryParam("b", "10")
        .queryParam("c", "true")
    )
        .andDo(print())
        .andReturn()
        .getResponse();

    // then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
}
```

위 테스트에서 `a`의 값을 `02`로 시작하는 번호가 아닌 `054`로 시작하는 번호로 전달하기 때문에 `ConstraintViolationException` 예외가 발생하고 400 응답을 한다.

위와 같이 컨트롤러에서 객체에 대해 ConstraintValidator로 커스텀 Bean Validation을 하기 위해서는 `@Valid`가 필요하다.

CustomValidRequest에서 `@CheckSeoulPhoneNumber`로 `a` 필드 하나만 체크한 것과 마찬가지로 파라미터 객체 전체를 검증할때도 위와 동일하게 할 수 있다.

```java
@Constraint(validatedBy = CustomObjectValidator.class)
@Target({ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckValidation {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

public class CustomObjectValidator implements ConstraintValidator<CheckValidation, CustomObjectValidRequest> {

    @Override
    public boolean isValid(CustomObjectValidRequest request, ConstraintValidatorContext context) {
        String a = request.getA();
        int b = request.getB();
        return a.length() < 10 && b >= 10;
    }
}

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@CheckValidation
public class CustomObjectValidRequest {

    private String a;

    private int b;

    @Builder
    public CustomObjectValidRequest(String a, int b) {
        this.a = a;
        this.b = b;
    }
}

@Slf4j
@RestController
@RequestMapping("/api/custom/validation")
public class CustomValidationApiController {
  @GetMapping("/object-valid")
  public void customObjectValidationWithValid(@Valid CustomObjectValidRequest request) {
    log.info("request: {}", request);
  }
}
```

```java
@Test
void customObjectValidationWithValid() throws Exception {
  // given & when
  MockHttpServletResponse response = mockMvc.perform(get("/api/custom/validation/object-valid")
          .queryParam("a", "12345678910") // 길이가 10이 넘으므로 실패
          .queryParam("b", "10")
  )
          .andDo(print())
          .andReturn()
          .getResponse();

  // then
  assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
}
```

`CustomObjectValidRequest` 클래스에 커스텀 어노테이션을 붙여주고 이에 대한 `ConstraintValidator`를 지정하여 Bean Validation을 할 수 있다.

## 컴포넌트 빈에서 ConstraintValidator

`@Validated` 어노테이션이 붙은 컴포넌트 빈에서는 Bean Validation 기능을 사용할 수 있다. 이때 컨트롤러에서와 같이 검증하고자 하는 파라미터에 `@Valid` 를 붙여서 Bean Validation을 사용할 수 있고 추가로 ConstraintValidator에서 사용하는 커스텀 어노테이션을 붙여서도 동일하게 적용할 수 있다.

```java
@Slf4j
@Service
@Validated
public class CustomValidationService {
  public void validateObjectWithCustomAnnotation(@CheckValidation CustomObjectValidRequest request) {
    log.info("request: {}", request);
  }
}

```

앞서 사용한 `@CheckValidation`, `CustomObjectValidator`, `CustomObjectValidRequest`를 그대로 사용한다.

`CustomValidationService#validateObjectWithCustomAnnotation`는 CustomObjectValidRequest에 대해서 `@CheckValidation` 가 붙어있으므로 `CustomObjectValidator`의 유효성 검증 기능을 사용한다.

```java
@Test
void validateObjectWithValid() {
  // given
  CustomObjectValidRequest request = CustomObjectValidRequest.builder()
          .a("12345678910")
          .b(10)
          .build();

  // when & then
  assertThatThrownBy(() -> customValidationService.validateObjectWithValid(request))
          .isInstanceOf(ConstraintViolationException.class);
}
```

`CustomObjectValidator`에서 `a`는 길이 10이 넘지 않아야 통과를 하는데 위 테스트의 `a`는 길이가 10이 넘으므로 `ConstraintViolationException`가 발생한다.

객체 말고도 단일 String, int 등에서도 동일하게 동작한다.

```java
@Slf4j
@Service
@Validated
public class CustomValidationService {
	public void validatePrimitiveWithoutValid(@CheckSeoulPhoneNumber String phoneNumber) {
    log.info("phoneNumber: {}", phoneNumber);
  }
}
```

서울 지역의 전화번호를 체크하는 `@CheckSeoulPhoneNumber` 와 `SeoulPhoneNumberValidator` 를 활용한다.

```java
@Constraint(validatedBy = SeoulPhoneNumberValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckSeoulPhoneNumber {
  String INVALID_SEOUL_PHONE_FORMAT_MESSAGE = "서울지역 번호가 아닙니다.";

  String message() default INVALID_SEOUL_PHONE_FORMAT_MESSAGE;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}

public class SeoulPhoneNumberValidator implements ConstraintValidator<CheckSeoulPhoneNumber, String> {
  private static final Pattern SEOUL_PHONE_REGEX = Pattern.compile(
      "^02-[0-9]{3,4}-[0-9]{3,4}$"
  );

  @Override
  public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
    return Objects.nonNull(phoneNumber) && SEOUL_PHONE_REGEX.matcher(phoneNumber).matches();
  }
}
```

`CustomValidationService#validatePrimitiveWithoutValid` 를 테스트해보면 파라미터 phoneNumber가 서울 지역이 번호가 아니라면 `ConstraintViolationException`가 발생한다.

```java
@Test
void validatePrimitive_thenFailed() {
  assertThatThrownBy(() -> customValidationService.validatePrimitiveWithoutValid("054-000-0000"))
      .isInstanceOf(ConstraintViolationException.class)
      .hasMessage("validatePrimitive.phoneNumber: " + CheckSeoulPhoneNumber.INVALID_SEOUL_PHONE_FORMAT_MESSAGE);
}
```

위와 같이 String, int와 같은 Java 기본 자료형에도 잘 동작하는 것을 알 수 있다.
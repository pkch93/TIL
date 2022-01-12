# 2022.01.12 TIL - 컴포넌트 빈에서의 Bean Validation

컴포넌트 클래스에 `@Validated`를 붙인다면 MethodValidationBeanPostProcessor에 의해 메서드에서 Bean Validation을 사용할 수 있도록 만들 수 있다.

```java
@Slf4j
@Service
@Validated
public class GroupingValidationService {
    // ...
}
```

위와 같이 빈 컴포넌트에 `@Validated`를 붙이면 활용할 수 있다.

여기에 Bean Validation으로 유효성 검증을 하고자하는 파라미터에 `@Valid`를 붙이면 유효성 검증을 할 수 있다. 빈 유효성 검증 로직은 MethodValidationInterceptor에서 담당한다. 메서드 AOP를 통해 MethodValidationInterceptor에서 파라미터 유효성 검증을 하도록 MethodValidationBeanPostProcessor가 포인트컷을 등록하기 때문에 가능하다.

단, 파라미터에 `@Validated`를 붙이는 경우에는 유효성 검증이 불가능하다. 이는 MethodValidationInterceptor에서 표준 Bean Validation의 구현체로 검증하기 때문으로 보인다. 때문에 Spring의 어노테이션인 `@Validated`로는 검증을 하지 못하는 것이다.

```java
@Override
@Nullable
public Object invoke(MethodInvocation invocation) throws Throwable {
    // ...

    Class<?>[] groups = determineValidationGroups(invocation);

    // Standard Bean Validation 1.1 API
    ExecutableValidator execVal = this.validator.forExecutables();
    Method methodToValidate = invocation.getMethod();
    Set<ConstraintViolation<Object>> result;

    // ...
}
```

> `Standard Bean Validation 1.1 API` 위 주석으로 표준 Bean Validation에서 지원하는 `@Valid`로 유효성 검증을 지원할 것으로 추측된다.
> 

그렇다면 `@Validated`는 단지 빈 컴포넌트가 유효성 검증을 사용할 수 있도록 만들기만 할까? 그렇지는 않다. 그룹을 지정하는 용도로도 사용이 가능하다.

```java
public @interface GroupA {
}

public @interface GroupD {
}

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GroupingRequest {
    public static final String A_INVALID_MESSAGE = "a는 5 이상이어야 합니다.";
    public static final String B_INVALID_MESSAGE = "b는 10 이상이어야 합니다.";
    public static final String D_INVALID_MESSAGE = "d는 100이하여야 합니다.";

    @Min(value = 5, groups = GroupA.class, message = A_INVALID_MESSAGE)
    private int a;

    @Min(value = 10, message = B_INVALID_MESSAGE)
    private int b;

    private int c;

    @Max(value = 100, groups = GroupD.class, message = D_INVALID_MESSAGE)
    private int d;

    @Builder
    public GroupingRequest(int a, int b, int c, int d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }
}
```

```java
@Slf4j
@Service
@Validated(GroupD.class)
public class GroupDValidationService {

  public void validateForGroupD(@Valid GroupingRequest request) {
      log.info("grouping request: {}", request);
  }

}
```

위와 같이 클래스 레벨에 `GroupD.class`로 그룹을 지정하면 GroupingRequest에서 `GroupD.class`로 지정된 필드만 유효성 검증을 한다.

`@Validated`는 메서드에서 지정할 수 있다. 메서드 지정하는 `@Validated`는 빈 컴포넌트에서 검증할 그룹을 지정할 때 사용할 수 있다.

```java
@Slf4j
@Service
@Validated(GroupD.class)
public class GroupDValidationService {
	
	@Validated(GroupA.class)
  public void validateForGroupAAndD(@Valid GroupingRequest request) {
      log.info("grouping request: {}", request);
  }

}
```

위와 같이 `GroupDValidationService#validateForGroupAAndD`에 `@Validated`를 달 수 있다. 단, 위 예시만 보면 `validateForGroupAAndD`에서는 GroupA와 GroupD에 대해서 유효성 검증을 할 것으로 보인다. 하지만 위 경우는 GroupA에 대해서만 유효성 검증을 한다. 즉, GroupingRequest의 필드 a만 검증을 한다.

이는 `MethodValidationInterceptor`에서 그룹을 판단하는 로직 때문이다.

```java
protected Class<?>[] determineValidationGroups(MethodInvocation invocation) {
    Validated validatedAnn = AnnotationUtils.findAnnotation(invocation.getMethod(), Validated.class);
    if (validatedAnn == null) {
        Object target = invocation.getThis();
        Assert.state(target != null, "Target must not be null");
        validatedAnn = AnnotationUtils.findAnnotation(target.getClass(), Validated.class);
    }
    return (validatedAnn != null ? validatedAnn.value() : new Class<?>[0]);
}
```

위 메서드 `determineValidationGroups`는 `MethodValidationInterceptor`에서 유효성 검증을 위한 그룹을 판단하기 위한 로직이다.

이때 처음에 호출하는 메서드의 `@Validated` 어노테이션을 찾는다. `invocation.getMethod()` 만약 존재한다면 메서드의 `@Validated` 에서 value인 그룹을 반환한다.

메서드 레벨의 `@Validated`가 존재하지 않는다면 호출되는 메서드의 클래스를 찾는다. 그리고 그 클래스에 붙은 `@Validated`의 value를 그룹으로 판단한다.

즉, 메서드 `@Validated`의 그룹을 우선으로 사용하며 존재하지 않으면 클래스 `@Validated`의 그룹을 유효성 검증에 사용할 그룹으로 판단한다.
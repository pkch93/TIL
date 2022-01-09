# 2021.01.09 TIL - ArgumentResolver에서의 @Valid와 @Validated

컨트롤러의 메서드 핸들러에서 요청값 검증을 위해 `@Valid`와 `@Validated`를 주로 사용한다. 즉, ArgumentResolver에서 `@Valid`와 `@Validated`가 어떻게 사용되는지 확인해본다.

## validateIfApplicable

`validateIfApplicable`을 사용하여 `@Valid`와 `@Validated` 유효성 검증을 지원하는 ArgumentResolver가 있다.

- @RequestPart `RequestPartMethodArgumentResolver`
- @RequestBody `RequestResponseBodyMethodProcessor`
- @ModelAttribute `ModelAttributeMethodProcessor`

위 어노테이션에 대한 ArgumentResolver들은 resolveArgument 메서드 내부에 `validateIfApplicable`를 호출한다. `validateIfApplicable` 내부에는 `ValidationAnnotationUtils#determineValidationHints` 를 호출하는데 다음과 같다.

```java
protected void validateIfApplicable(WebDataBinder binder, MethodParameter parameter) {
    for (Annotation ann : parameter.getParameterAnnotations()) {
        Object[] validationHints = ValidationAnnotationUtils.determineValidationHints(ann);
        if (validationHints != null) {
            binder.validate(validationHints);
            break;
        }
    }
}
```

```java
@Nullable
public static Object[] determineValidationHints(Annotation ann) {
    Class<? extends Annotation> annotationType = ann.annotationType();
    String annotationName = annotationType.getName();
    if ("javax.validation.Valid".equals(annotationName)) {
        return EMPTY_OBJECT_ARRAY;
    }
    Validated validatedAnn = AnnotationUtils.getAnnotation(ann, Validated.class);
    if (validatedAnn != null) {
        Object hints = validatedAnn.value();
        return convertValidationHints(hints);
    }
    if (annotationType.getSimpleName().startsWith("Valid")) {
        Object hints = AnnotationUtils.getValue(ann);
        return convertValidationHints(hints);
    }
    return null;
    }
```

즉, resolveArgument를 하고자하는 대상에 `@Valid`와 `@Validated`가 붙어있는지를 확인하는 과정이 위 `validateIfApplicable`와 `ValidationAnnotationUtils#determineValidationHints`에서 이뤄진다. `determineValidationHints`에서 반환되는 값은 그룹에 대한 힌트로 처리한다.

위 로직을 통해 `@Valid`나 `@Validated`가 존재하는지, 즉, 빈 유효성 검사의 대상인지 확인한 후에 `javax.validation.Validator`의 구현체인 ValidatorImpl의 validate를 통해 실제 유효성을 체크한다.

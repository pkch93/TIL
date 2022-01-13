# 2022.01.13 TIL - ConstraintValidator

- [2022.01.13 TIL - ConstraintValidator](#20220113-til---constraintvalidator)
  - [ConstraintValidator 사용하기](#constraintvalidator-사용하기)

Spring에서는 `@Valid`를 통해 JSR-303 스팩에 정의된 Bean Validation을 지원한다. 즉, `javax.validation`에 정의된 `@Size`, `@Min`, `@Max` 등의 Validation 기능을 제공한다.

하지만 기본으로 제공하는 Bean Validation 기능만으로 복잡한 유효성 검사를 충족하지 못할 수 있다. 이를 위해서 Spring에서는 ConstraintValidator를 통해 유효성 검사를 커스텀할 수 있도록 지원한다.

## ConstraintValidator 사용하기

`java.validation.ConstraintValidator`는 다음과 같다.

```java
public interface ConstraintValidator<A extends Annotation, T> {

    default void initialize(A constraintAnnotation) {
    }

    boolean isValid(T value, ConstraintValidatorContext context);
}
```

ConstraintValidator에는 두가지 제네릭을 정의한다. 첫번째 제네릭 A는 검증 대상을 판단하기 위한 어노테이션, 두번째 제네릭 T는 검증하고자하는 객체의 클래스이다.

즉, 첫번째 제네릭의 어노테이션을 기준으로 ConstraintValidator가 검증할 대상을 판단한다.

ConstraintValidator는 initialize와 isValid를 정의할 수 있다.

initialize는 default 메서드로 재정의하지 않아도 된다. ConstraintValidator가 유효성 검증시에 생성이 되고 initialize를 호출한다. 이때 어노테이션에 있는 정보를 저장하는 동작을 수행할 수 있다.

```java
@Constraint(validatedBy = DataConstraintValidator.class)
@Target({ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckRegex {
    String value();

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

@Getter
@Setter
public class Data {

    @CheckRegex("010-[0-9]{3,4}-[0-9]{3,4}")
    private String phone;

    @CheckRegex("[\w]+")
    private String nickname;
}

public DataConstraintValidator implements ConstraintValidator<CheckRegex, String> {
    private Pattern regex;

    @Override
    public void initialize(A constraintAnnotation) {
        regex = Pattern.compile(constraintAnnotation.value())
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return regex.matcher(value).matches();
    }
}
```

위와 같이 특정 파라미터에 따라 유효성 검증을 달리 하고 싶은 경우 initialize를 활용할 수 있다.

ConstraintValidator에 사용되는 어노테이션에는 `@Constraint` 어노테이션의 validatedBy로 어떤 ConstraintValidator를 사용하는지 명시해야한다.

```java
@Constraint(validatedBy = DataConstraintValidator.class)
```

그리고 어노테이션은 반드시 message, groups, payload를 정의해야한다. 그렇지 않으면 다음과 같은 에러가 발생한다.

```java
javax.validation.ConstraintDefinitionException: HV000074: edu.pkch.validation.custom.CheckValidation contains Constraint annotation, but does not contain a payload parameter.
```

> 위 에러는 payload가 없는 경우에 `but does not contain a payload parameter.`와 같은 문구가 나타나며 만약 message나 groups가 존재하지 않더라도 `but does not contain a message parameter.`나 `but does not contain a groups parameter.` 문구를 확인할 수 있다.

isValid는 첫번째 value 파라미터를 가지고 유효성 검증을 하는 메서드이다. true라면 다음 로직을 수행하는 반면 false라면 `ConstraintViolationException`을 던진다.
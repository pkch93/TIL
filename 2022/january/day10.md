# 2022.01.10 TIL - MethodValidationBeanPostProcessor

컨트롤러에서 `@PathVariable`, `@RequestParam` 등으로 입력값 검증을 하거나 스프링 컨테이너에 생성하는 빈이 호출하는 메서드의 파라미터에 대해서도 Spring Validation을 사용하여 검증할 수 있다. 이를 도와주기 위해 스프링에서는 MethodValidationBeanPostProcessor를 제공한다.

MethodValidationBeanPostProcessor는 빈에서 사용하는 메서드의 파라미터를 Spring Validation으로 검증할 수 있도록 도와주는 BeanPostProcessor이다.

> BeanPostProcessor는 스프링 컨테이너에서 제공하는 생성로직을 추가하는 등 커스텀하게 변경할 수 있도록 지원한다. 빈이 생성되기 전후로 BeanPostProcessor의 `postProcessBeforeInitialization`와 `postProcessAfterInitialization`를 호출하여 빈에 기능을 추가하도록 도와준다.
> 

MethodValidationBeanPostProcessor는 빈 클래스에 `@Validated` 어노테이션이 붙은 빈에 프록시를 활용하여 Spring Validation을 사용할 수 있도록 지원한다.

```java
// MethodValidationBeanPostProcessor

private Class<? extends Annotation> validatedAnnotationType = Validated.class;

// ...

@Override
public void afterPropertiesSet() {
	Pointcut pointcut = new AnnotationMatchingPointcut(this.validatedAnnotationType, true);
	this.advisor = new DefaultPointcutAdvisor(pointcut, createMethodValidationAdvice(this.validator));
}

protected Advice createMethodValidationAdvice(@Nullable Validator validator) {
	return (validator != null ? new MethodValidationInterceptor(validator) : new MethodValidationInterceptor());
}
```

MethodValidationBeanPostProcessor은 MethodValidationBeanPostProcessor 생성 직후 `afterPropertiesSet` 콜백을 통해 `@Validated` 포인트컷을 가지고 MethodValidationInterceptor를 어드바이스로 가지는 어드바이저를 할당한다.

이를 기반으로 빈이 생성되었을때 타겟 빈이 `@Validated`가 매핑되어있는지 확인하고 Validation을 위한 프록시 팩토리를 생성한다.

> 위 부분은 `MethodValidationBeanPostProcessor`의 상위 클래스 `AbstractAdvisingBeanPostProcessor`의 `postProcessAfterInitialization` 를 참고한다. 빈이  `MethodValidationBeanPostProcessor`에서 정의한 포인트 컷에 할당이 가능한지를 판단하는데 이 기준이 `@Validated`이다.

이와 같은 이유로 `@Validated`를 사용하여 Spring Validation 기능을 적용하는 경우에는 AOP의 기능을 사용한다. 그리고 빈 클래스에 `@Validated`가 붙어있어야 AOP의 지원을 받을 수 있기 때문에 메서드 레벨의 `@Validated`는 Spring Validation을 사용할 수 없다.
> 

실제 Validation을 처리하는 로직은 MethodValidationInterceptor를 참고해야한다. MethodValidationInterceptor에서 Hibernate Validator 구현체를 활용하여 자바 표준의 Bean Validation 1.1을 지원한다.

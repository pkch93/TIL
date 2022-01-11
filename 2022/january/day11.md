# 2021.01.11 TIL - 컨트롤러에서의 Bean Validation

앞서 ArgumentResolver에서의 `@Valid`와 `@Validated`에서 봤듯이 몇몇 ArgumentResolver에서 Bean Validation을 지원한다.

`RequestPartMethodArgumentResolver`,  `RequestResponseBodyMethodProcessor`, `ModelAttributeMethodProcessor`에서 resolveArgument를 할 때 Bean Validation이 이뤄진다.

이때 ArgumentResolver에서 파라미터에 붙은 `@Valid`와 `@Validated` 어노테이션만 보고 Bean Validation을 한다. 이때 `@Validated`에는 group을 지정할 수 있는데 파라미터의 `@Validated`의 group만 적용을 한다. `ValidationAnnotationUtils#determineValidationHints 참고`

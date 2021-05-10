# 2021.05.10 TIL - Mockito Strict & Lenient

참고: [https://javadoc.io/static/org.mockito/mockito-core/3.9.0/org/mockito/Mockito.html#strict_mockito](https://javadoc.io/static/org.mockito/mockito-core/3.9.0/org/mockito/Mockito.html#strict_mockito)

Mockito는 기본적으로 Strict 모드로 사용하지 않는다.

## Strict Mode

[https://javadoc.io/static/org.mockito/mockito-core/3.9.0/org/mockito/quality/Strictness.html#STRICT_STUBS](https://javadoc.io/static/org.mockito/mockito-core/3.9.0/org/mockito/quality/Strictness.html#STRICT_STUBS)

Mockito는 Strict Mode 사용을 추천한다. Strict Mode는 테스트 코드의 중복을 줄여주며, 디버깅을 용이하게 해준다. 기본적으로 `MockitoExtension`를 사용하면 Strict Mode로 동작한다.

Strict Mode는 다음과 같은 동작을 추가한다.

1. stubbing한 메서드에 다른 인자로 호출이 되는 경우 테스트가 실패한다. `PotentialStubbingProblem`
2. 사용하지 않는 stubbing이 존재하면 테스트가 실패한다. `UnnecessaryStubbingException`
3. verifyNoMoreInteractions를 호출한다면 더이상 명시적으로 verify를 호출할 필요가 없다.

## Lenient

Strict Mode를 키지 않은 모드를 Lenient라고 한다. `MockitoExtension`을 사용하지 않으면 기본적으로 적용되는 모드이다.

만약 `MockitoExtension`을 사용한 상태에서 Lenient 모드를 사용하고자 한다면 `@MockitoSettings(strictness = Strictness.LENIENT)`를 붙여주어야한다.

```java
@MockitoSettings(strictness = Strictness.LENIENT)
class MockTest {
    // ...
}
```

```java
@ExtendWith(MockitoExtension.class)
@Inherited
@Retention(RUNTIME)
public @interface MockitoSettings {

    Strictness strictness() default Strictness.STRICT_STUBS;
}
```

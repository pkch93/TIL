# 2021.06.02 TIL - AutoParams

C#의 테스트 데이터 생성 라이브러리인 [AutoFixture](https://github.com/AutoFixture/AutoFixture)에 영감을 받아 작성된 라이브러리이다.

AutoFixture와 같이 테스트 데이터를 자동으로 생성하도록 만들어주는 라이브러리이다. Junit의 `@ParameterizedTest`와 함께 사용되어 테스트의 파라미터로 필요한 데이터를 주입받아 사용할 수 있도록 지원해준다.

`junit-jupiter-params`의 `@ValueSource`, `@CsvSource` 같이 `@AutoSource`를 테스트 메서드에 붙임으로써 테스트 데이터 자동 생성을 지원한다.

## 사용하기

AutoParams는 JDK 1.8 이상부터 지원한다. 의존성은 다음과 같이 주입하면 된다.

```groovy
// build.gradle
testImplementation 'io.github.javaunit:autoparams:0.2.2'
```

## 기능

Primitive부터 Java에서 지원하는 기본 Object `String, UUID, BigInteger 등`, Enum, Collection 등의 자동 데이터 생성을 지원한다. 지원하는 타입은 다음과 같다.

- int
- long
- float
- double
- boolean
- char
- String
- UUID
- BigInteger

> 위 기준은 AutoParams의 github에서 소개한 타입을 기준으로 함.
위 타입말고도 LocalDateTime, BigDecimal 등도 지원하는 듯 보임.

- Arrays
- List
- Set
- Map
- Stream

```java
@ParameterizedTest
@AutoSource
void integerAutoParams(int x, int y) {
  // given & when
  int actual = Integer.sum(x, y);
  System.out.println(String.format("x: %d / y: %d", x, y));

  // then
  assertThat(actual).isEqualTo(x + y);
}
```

위와 같이 테스트 메서드에 `@AutoSource`를 사용하여 int 값을 자동으로 주입받을 수 있다. 위 타입들은 위와 같이 사용가능하다.

참고로 int와 Integer의 경우는 `javax.validation.constraints.Max`와 `javax.validation.constraints.Min`으로 생성되는 값의 최대 최소 값을 설정할 수 있다.

```java
@ParameterizedTest
@AutoSource
void integerAutoParamsWithMinMax(@Min(1) @Max(10) int x) {
    assertThat(x >= 1).isTrue();
    assertThat(x <= 10).isTrue();

    System.out.println(String.format("x: %d", x));
}
```

그밖에 `junit-jupiter-params`에서 지원하는 `@ValueSource`나 `@CsvSource`도 각각 `@ValueAutoSource`와 `@CsvAutoSource`로 지원한다.

```java
@ParameterizedTest
@CsvAutoSource({"16, foo"})
void testMethod(int arg1, String arg2, String arg3) {
    assertEquals(16, arg1);
    assertEquals("foo", arg2);
    assertNotEquals(arg2, arg3);
}

class ValueContainer {

    private final String value;

    public ValueContainer(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}

@ParameterizedTest
@ValueAutoSource(strings = {"foo"})
void testMethod(@Fixed String arg1, String arg2, ValueContainer arg3) {
    assertEquals("foo", arg2);
    assertEquals("foo", arg3.getValue());
}
```

`@Fixed`는 `@Fixed`가 붙은 파라미터가 이후에 나타나는 파라미터의 값을 고정시킨다. 만약 중간에 타입이 달라지거나 `@Fixed`가 붙은 파라미터 이전의 인자들은 값을 고정시키지 않는다.

위 예시에서는 ValueContainer의 value가 String이므로 이 값이 `foo`로 고정된다.


### 참고

AutoParams github: [https://github.com/JavaUnit/AutoParams](https://github.com/JavaUnit/AutoParams)
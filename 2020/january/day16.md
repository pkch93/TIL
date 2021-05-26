# 20.01.16 Thursday - Mockito argument matcher, verify call count\`\`

### Table of Contents

[Mockito API](day16.md#Mockito_API)

1. [argument matcher](day16.md#argument_matcher)
2. [verify call count](day16.md#verify_call_count)

## Mockito API

### argument matcher

mockito에서는 인자를 설정하여 stubbing 할 수 있다. 다만, 인자를 특정할 수 없는 경우 특정 타입의 인자가 들어간다고 알려줄 수 있다. 이를 위해 mockito에서는 argument matcher를 지원한다.

```java
@Test
@DisplayName("mockito matcher 사용 테스트")
void matcher_test() {
    String word = "hello world";

    when(words.get(anyInt())).thenReturn(word);

    assertThat(words.get(0)).isEqualTo(word);
    assertThat(words.get(1)).isEqualTo(word);
    assertThat(words.get(2)).isEqualTo(word);

    verify(words).get(anyInt());
}
```

위는 `List.get()`메서드에 int가 들어가기 때문에 `anyInt()`를 사용하였다. matcher로 원하는 객체에 타입에 대해 `anyX` 형태의 메서드로 지원한다.

### verify call count

객체 간 상호작용시 한번만 메서드가 불리는 경우도 있지만 여러번 호출되는 경우도 있다. 이를 위해 mockito에서는 다양한 횟수 검증 메서드를 지원한다.

* times
* nerver
* atMostOnce
* atLeastOnce
* atMost
* atLeast


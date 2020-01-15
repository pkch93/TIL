# 20.01.15 Wendesday = Mockito

### Table of Contents

[Mockito API](#Mockito_API)

1. [mock 기본 사용](#mock_basic)
2. [stubbing](#stubbing)

## Mockito API

### mock basic

[참고](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html#verification)

Mockito의 mock은 기본적으로 호출된 모든 method를 기억한다. 이는 객체 간의 상호작용을 검증하기 위해 기억하는 것이기도 하다.

mock의 행동 검증은 `verify` 메서드를 통해서 할 수 있다.

```java
@Test
@DisplayName("mockito 기본 사용, mock은 mock으로 수행한 행동을 모두 기억한다.")
void mockito_basic() {
    mockWords = mock(List.class);

    mockWords.add("hello");
    mockWords.clear();

    verify(mockWords).add("hello");
    verify(mockWords).clear();
}
```

위와 같이 `verify` 메서드 인자로 mock 객체를 넣어둔 후 호출한 메서드를 불러 확인 가능하다.

```java
    @Test
    @DisplayName("mockito mock 객체가 하지 않은 행위 검증")
    void mockito_basic_notBehavior_test() {
        mockWords = mock(List.class);

        mockWords.add("hello");

        verify(mockWords).add("hello");
        /**
         *  하지 않은 행위로 오류가 나타남
         *  verify는 기본적으로 해당 mock 객체가 다음에 나타나는 행위를 했는지 검증한다.
         */
        verify(mockWords).clear();
    }
```

위와 같이 호출하지 않은 메서드를 검증하는 경우 테스트가 실패한다.

### stubbing

**stub**란 mock 객체에서 특정 메서드를 호출할 때 리턴 값을 원하는 객체, 값으로 설정할 수 있다. 이때 호출하는 메서드의 인자까지도 설정할 수 있다.

```java
@Test
@DisplayName("stub 기본 사용법")
void stub_basic() {
    when(words.get(0)).thenReturn("hello");

    assertThat(words.get(0)).isEqualTo("hello");
}
```

위와 같이 `words.get(0)`을 호출하면 `hello`가 나타난다.

단, `words.get()` 메서드를 부르더라도 인자가 설정한 값이 아니라면 `null`이 리턴된다. 즉, 위 예시에서는 `words.get(1)`을 호출하는 경우 `null`이 나타난다.

```java
@Test
@DisplayName("stub 기본 사용법")
void stub_basic() {
    when(words.get(0)).thenReturn("hello");

    assertThat(words.get(1)).isEqualTo(null);
}
```

### 
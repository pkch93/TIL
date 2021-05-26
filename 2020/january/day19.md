# 20.01.19 Sunday - mockito

### Table of Contents

* [Mockito API](day19.md#Mockito_API)
* [verify call count](day19.md#verify_call_count)
* [stubbing for exception](day19.md#stubbing_for_exception)
* [inorder verify](day19.md#inorder_verify)
* [verifyNoInteractons](day19.md#verifyNoInteractons)
* [verifyNoMoreInteractons](day19.md#verifyNoMoreInteractons)
* [stubbing consecutive calls](day19.md#stubbing_consecutive_calls)
* [stubbing with callbacks](day19.md#stubbing_with_callbacks)
* [spy](day19.md#spy)

## Mockito API

### verify call count

Mockito에선 mock 객체의 동작이 몇 번 이뤄졌는지 검증할 수 있는 API를 제공한다.

* times

`times()`는 인자로 들어가는 횟수만큼 메서드 실행이 이뤄졌는지 검증하는 API이다.

```java
@Test
@DisplayName("times로 mock 객체의 메서드 실행 횟수를 체크할 수 있다.")
void times_test() {
    words.add("hello");
    words.add("world");

    verify(words, times(2)).add(anyString());
}
```

위 테스트에서는 String을 인자로 사용하는 add 메서드를 2번 호출하였으므로 통과한다.

기본적으로 verify의 두번째 인자를 주지 않으면 `times(1)`과 동일하다.

* never

`never`는 해당 메서드가 한번도 불리지 않았는지 검증하는 API이다.

```java
@Test
@DisplayName("never는 한번도 호출되지 않음을 체크할 수 있다.")
void never_test() {
    verify(words, never()).add(anyString());
}
```

* atMost, atMostOnce

`atMost`는 인자로 주어지는 값보다 적게 호출되었는지 검증하는 API이다. `atMostOnce`는 `atMost(1)`과 같다.

따라서 `atMost(1)`은 1번 호출되었거나 한번도 호출되지 않은 경우를 검증한다.

```java
@Test
@DisplayName("atMost는 인자에 해당하는 값을 최대값으로 사용하여 횟수를 체크한다.")
void atMost_test() {
    words.add("hello");
    words.add("world");

    verify(words, atMost(2)).add(anyString());
}

@Test
@DisplayName("atMostOnce는 한번 호출되었거나 호출되지 않았는지 체크한다.")
void atMostOnce_test() {
    words.add("hello");

    verify(words, atMostOnce()).add(anyString()); // atMost(1)과 동일
}
```

* atLeast, atLeastOnce

`atLeast`는 인자로 주어지는 값보다 더 많이 호출되었는지 검증하는 API이다. `atLeastOnce`는 `atLeast(1)`과 같다.

따라서 `atLeast(1)`은 1번 이상 호출된 경우를 검증한다.

```java
@Test
@DisplayName("atLeast는 인자에 해당하는 값을 최솟값으로 사용하여 횟수를 체크한다.")
void atLeast_test() {
    words.add("hello");
    words.add("world");

    verify(words, atLeast(1)).add(anyString());
}

@Test
@DisplayName("atLeastOnce는 한번 호출되었거나 호출되지 않았는지 체크한다.")
void atLeastOnce_test() {
    words.add("hello");

    verify(words, atLeastOnce()).add(anyString()); // atMost(1)과 동일
}
```

### stubbing for exception

특정 메서드에 대해 exception이 발생하도록 stubbing할 수 있다.. 바로 `doThrow()` 메서드를 활용하면 된다.

```java
@Test
@DisplayName("doThrow()로 exception stub 하기")
void stub_exception() {
    doThrow(IllegalArgumentException.class).when(words).get(0);

    assertThatThrownBy(() -> words.get(0)).isInstanceOf(IllegalArgumentException.class);
}
```

위와 같이 `when`으로 stub 할때 exception이 발생하도록 doThrow를 쓸 수 있다.

### inorder verify

기존의 `verify`는 순서에 상관없이 호출되는 것만을 검증할 수 있었다. 만약에 호출 순서를 검증하기 위해서는 Mockito에서 제공하는 `Inorder` 클래스를 사용해야한다.

```java
@Test
@DisplayName("순서대로 mock이 동작하는지 테스트")
void verify_inorder_test() {
    mockWords.add("first");
    mockWords.add("second");

    InOrder inOrder = inOrder(mockWords);

    inOrder.verify(mockWords).add("first");
    inOrder.verify(mockWords).add("second");
}
```

만약 `first`와 `second`의 순서가 다르다면 test가 실패한다.

```java
// test fail
inOrder.verify(mockWords).add("second");
inOrder.verify(mockWords).add("first");
```

inorder는 여러 mock 객체를 묶어서 생성할 수 있다.

```java
@Test
@DisplayName("순서대로 여러가지의 mock을 검증하는 테스트")
void verify_multimock_inorder_whenNotInorderExecute_test() {
    List<Integer> mockNumbers = mock(List.class);

    mockWords.add("first");
    mockNumbers.add(0);
    mockWords.add("second");

    InOrder inOrder = inOrder(mockWords, mockNumbers);

    inOrder.verify(mockWords).add("first");
    inOrder.verify(mockNumbers).add(0);
    inOrder.verify(mockWords).add("second");
}
```

위 예시처럼 `InOrder` 클래스에 여러 mock 객체를 넣어 생성하면 여러 mock 객체에 대해 메서드 호출 순서를 검증할 수 있다.

### verifyNoInteractions

`verifyNoInteractions`는 인자로 들어오는 mock 객체가 한번도 호출이 없었는지를 검증하는 API이다.

```java
@Test
@DisplayName("mock 객체가 한번도 메서드 호출이 없다면 verifyNoInteractions로 검증할 수 있다.")
void verifyNoInteractions_test() {
    verifyNoInteractions(mockWords);
}
```

위와 같이 mockWords가 아무 메서드 호출이 없었으므로 위 테스트는 통과한다.

단, 메서드 호출이 있다면 테스트는 실패한다.

```java
@Test
@DisplayName("mock 객체에 메서드 호출이 있다면 verifyNoInteractions는 실패한다.")
void verifyNoInteractions_fail_test() {
    mockWords.add("hello");

    verify(mockWords).add("hello");
    verifyNoInteractions(mockWords);
}
```

만약 한 메서드 호출에 대한 `verify`를 진행한 후 더이상 호출되지 않음을 검증하고 싶다면 `verifyNoMoreInterations`를 사용해야한다.

### verifyNoMoreInteractions

앞서 언급한대로 mock 객체에 대해 메서드 호출을 검증한 후 더 이상의 메서드 호출이 없음을 검증하기 위해 사용하는 Mockito API이다.

```java
@Test
@DisplayName("mock 객체에 메서드 호출을 검증한 후 더이상의 메서드 호출이 없음을 검증한다면 verifyNoMoreInteractions를 사용할 수 있다.")
void verifyNoMoreInteractions_test() {
    mockWords.add("hello");

    verify(mockWords).add("hello");
    verifyNoMoreInteractions(mockWords);
}
```

### stubbing consecutive calls

특정 메서드를 호출했을때 연속적으로 어떤 리턴값이 호출되는지 설정할 수 있다. 즉, stubbing의 첫번째 호출시의 리턴값 혹은 exception과 두번째 호출시의 리턴값 혹은 exception 등을 지정할 수 있다.

```java
@Test
@DisplayName("연속하여 메서드 stub 하기")
void stubbing_consecutive() {
    when(mockWords.get(anyInt()))
            .thenReturn("hello")
            .thenReturn("world")
            .thenThrow(new RuntimeException());

    assertThat(mockWords.get(0)).isEqualTo("hello");
    assertThat(mockWords.get(0)).isEqualTo("world");
    assertThatThrownBy(() -> mockWords.get(0))
            .isInstanceOf(RuntimeException.class);
}
```

위와 같이 첫번째 get 메서드 호출시 `hello`를 두번째 get 메서드 호출시 `world`를 세번째는 `RuntimeException`을 던지도록 stubbing 가능하다.

위 예시는 다음과 같이 사용할 수도 있다.

```java
@Test
@DisplayName("연속하여 메서드 stub 하기")
void stubbing_consecutive() {
    when(mockWords.get(anyInt()))
            .thenReturn("hello", "world")
            .thenThrow(new RuntimeException());

    assertThat(mockWords.get(0)).isEqualTo("hello");
    assertThat(mockWords.get(0)).isEqualTo("world");
    assertThatThrownBy(() -> mockWords.get(0))
            .isInstanceOf(RuntimeException.class);
}
```

`thenReturn`, `thenThrow` 등은 여러 값을 인자로 받아 순서대로 적용할 수 있다.

단, 위와 같이 체이닝 방식으로 stubbing 하지 않고 여러번 when 메서드로 stubbing 한다면 맨 마지막에 stubbing한 결과가 나타난다.

### stubbing with callbacks

stubbing 시에 callback을 넘겨줄 수 있다. `thenAnswer` 메서드가 그 역할을 한다.

```java
@Test
@DisplayName("thenAnswer로 callback stub 하기")
void stubbing_callback() {
    when(mockWords.get(0))
            .thenAnswer(invocation -> "hello");

    assertThat(mockWords.get(0)).isEqualTo("hello");
}
```

이때 `thenAnswer`의 인자로 들어가는 Answer 인터페이스의 answer 메서드에는 **InvocationOnMock** 클래스가 인자로 들어있다. 이 인자를 통해 stubbing한 mock의 정보, 메서드, 인자 정보들을 가져올 수 있다.

### spy

**spy**는 실제 객체에 부분 mocking하는 것과 같다. 즉, 실제 객체와 동일하게 동작하되 mock과 같이 verify 가능하며 부분적으로 stubbing도 가능하다.

단, 이를 남용하는 것은 좋지 않다. 보통은 레거시 코드를 다룰때 주로 사용한다.

```java
@Test
@DisplayName("spy 객체 활용")
void spy_test() {
    when(spyWords.size()).thenReturn(100);
    spyWords.add("hello");
    spyWords.add("world");

    assertThat(spyWords.get(0)).isEqualTo("hello");
    assertThat(spyWords.get(1)).isEqualTo("world");
    assertThat(spyWords.size()).isEqualTo(100);
}
```

> 참고로 `@Spy`를 사용했을때는 통과하지 않음... 이유가 뭘까...


# 2021.05.11 TIL - Mockito static method stubbing

static method stubbing은 Mockito 3.4 이상부터 지원한다. 3.4 이하 버전부터는 PowerMockito 같은 라이브러리를 사용해야 static method를 stubbing 할 수 있다.

static method stubbing을 하기 위해서는 `org.mockito:mockito-inline` 의존성을 추가해야한다.

mockito-inline 참고: [https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html\#mockito-inline](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html#mockito-inline)

```groovy
dependencies {
    testImplementation "org.mockito:mockito-inline:3.9.0"
}
```

## mockStatic

MockedStatic을 활용해서 static 메서드 stubbing을 할 수 있다.

```java
public class StubbingUtils {
  public static int stub() {
      return 0;
  }
}
```

`stub` static 메서드를 정의한 StubbingUtils가 있다고 가정한다. `StubbingUtils#stub`을 stubbing하려면 다음과 같이 할 수 있다.

```java
@Test
void staticMethodStubbing() {
    try (MockedStatic<StubbingUtils> mockStubbingUtils = Mockito.mockStatic(StubbingUtils.class)) {
        // given
        mockStubbingUtils.when(StubbingUtils::stub).thenReturn(10);

        // when
        int actual = StubbingUtils.stub();

        // then
        assertThat(actual).isEqualTo(10);
    }
}
```

참고로 MockedStatic은 ScopedMock의 확장으로 thread-local 범위에서 static 메서드를 가진 타입을 표현하기 위한 클래스이다. ScopedMock은 사용이 끝나면 반드시 닫아주어야한다. ScopedMock은 AutoClosable을 구현하고 있으므로 try-with-resource 구문으로도 자동으로 닫을 수 있다.

MockedStatic을 사용함으로써 전역적으로 사용될 수 있는 static 메서드에 thread-local 유효범위를 줄 수 있고 이를 통해 다른 테스트에 대한 영향을 줄일 수 있다.

### 참고

[Mocking Static Methods With Mockito](https://www.baeldung.com/mockito-mock-static-methods)


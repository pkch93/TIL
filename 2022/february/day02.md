# 2022.02.02 TIL - Flyweight Pattern

- [2022.02.02 TIL - Flyweight Pattern](#20220202-til---flyweight-pattern)
  - [자원 공유](#자원-공유)
  - [상태 구분](#상태-구분)
    - [독립 객체](#독립-객체)
  - [Integer](#integer)

플라이웨이트 패턴은 객체를 공유하기 위해 구조를 변경하는 패턴이다. 객체를 공유하면 재사용이 가능하기 때문에 시스템 자원을 절약하여 사용하기 위한 패턴이다.

## 자원 공유

플라이웨이트 패턴은 중복되는 객체를 매번 생성하는 것이 아니라 생성된 객체를 공유하여 재사용하는 방법을 제시한다.

기본적으로 객체 생성은 `new` 키워드를 통해서 한다. `java 기준` 단, new 키워드는 새롭게 메모리를 할당하기 때문에 자원을 사용한다. 객체를 공유하기 위해서는 공통된 객체를 생성해야하고 이를 위해서 싱글턴 패턴과 함께 응용해야한다.

플라이웨이트 패턴은 효율적인 공유 객체 관리를 위해서 **별도의 저장소**를 갖는다. 이를 공유 저장소라고하며 이를 팩토리 클래스에 추가하여 객체의 중복 생성 동작을 제한한다.

## 상태 구분

플라이웨이트 패턴은 객체를 공유한다. 이때 객체 공유는 본질적 공유 `intrinsic`와 부가적 공유 `extrinsic`로 구분한다. 본질적 상태의 객체를 공유하는 것을 본질적 공유, 부가적 상태의 객체를 공유하는 것을 부가적 공유라고 한다.

본질적 상태는 어떤 변경도 없이 객체를 있는 그대로 참조해서 사용하는 상태를 의미한다. 객체의 상태값에 따라 달라지지 않고 동일하게 적용되는 것을 본질적 공유라고 한다.

반면, 객체를 공유할 때 상태값에 따라 달라지는 것을 부가적 상태라고 한다. 객체의 값에 따라 종속적인 상태가 되는 것인데 이와 같은 부가적 상태로 객체를 사용하는 경우는 특정 데이터 값을 변경해 참조하는 다른 객체에 영향을 주기 위해서이다. 이런 부가적 상태의 객체는 플라이웨이트 패턴으로 사용할 수 없다. 객체 상태가 변경됨으로 인해 참조하는 다른 객체에서 사이드이팩트가 발생하기 때문이다.

> 본질적 상태의 객체는 흔히 말하는 불변 객체를 의미하는 걸로 보인다. 반면 부가적 상태의 객체는 값 객체와 같이 객체의 값으로 식별하는 객체를 의미하는 것으로 보인다.

### 독립 객체

공유 객체와 반대되는 의미로 독립 객체라는 개념이 있다. 독립 객체는 공유되지 않고 각각의 상황에 맞게 생성된 객체이다. 공유 객체는 사이드이팩트를 주의해야한다. 이를 해결하기 위해서 부가적 형태로 처리해야하는 경우에는 독립 객체로 처리하는 것이 좋다.

플라이웨이트 패턴을 유용하게 사용하려면 공유 객체와 독립 객체를 명확히 구분해서 사용해야한다. 단, 실무에서는 이를 명확히 구분하여 사용하기 쉽지 않다. 이 점이 실무에서 사용하기 어려운 이유이며 잘 보이지 않는 이유이다.

> 구분이 쉽지 않은 이유로는 클래스 만으로는 공유 객체인지 독립 객체인지 한눈에 구분할 수 없기 때문이 아닐까 싶다. 언어 자체에서 공유/독립 객체에 대한 지원을 해주지 않으므로 컨벤션을 통해서 이를 제어해야하는데 이 점이 구분을 어렵게 하는 요소가 아닐까?

## Integer

Java의 값 객체 Integer가 플라이웨이트 패턴을 활용한 대표적인 예시가 아닐까 싶다. Integer는 자주사용하는 `-128`부터 `127`까지의 값을 캐싱해두고 있고 이 범위에 해당하는 값은 같은 객체를 리턴한다.

때문에 `Integer.valueOf(127) == Integer.valueOf(127)`은 true이다.

Integer를 직접 구현한다면 다음과 같은 형태가 될 것이다.

```java
public class Number {
    private static final Map<Integer, Number> CACHE;
    
    static {
        CACHE = new HashMap<>();
        IntStream.rangeClosed(-128, 127)
                .forEach(i -> CACHE.put(i, new Number(i)));
    }

    private final int value;

    private Number(int value) {
        this.value = value;
    }

    public static Number valueOf(int i) {
        Number number = CACHE.get(i);
        return Objects.requireNonNullElseGet(number, () -> new Number(i)));
    }

    public int getValue() {
        return this.value;
    }
}
```

> Integer라는 이름 대신 Number로 구현하였다.

valueOf 정적 메서드에서 사용한 `Objects#requireNonNullElseGet`는 JDK 9부터 지원한다.
> 

`CACHE`라는 공유 저장소를 통해 `-128 ~ 127`에 해당하는 값을 저장하고 있으며 해당 범위의 값을 `CACHE`에서 꺼내 반환하며 범위 이외의 값은 새로 객체를 생성하여 반환한다.

위 `Number`의 `value`가 변경이 가능하다면 어떻게 될까?

```java
@Test
void isSameNumberTest() {
    // given
    Number number1 = Number.valueOf(1);
    Number number2 = Number.valueOf(1);

    // when & then
    assertSame(number1, number2);
}

@Test
void isNotSameNumberTest() {
    // given
    Number number1 = Number.valueOf(1);
    Number number2 = Number.valueOf(1);

    int updateValue = 2;
    number2.setValue(updateValue);

    // when & then
    assertEquals(number1.getValue(), updateValue);
}
```

> 이전에 구현한 Number를 가변 객체로 만들어 `setValue` 메서드를 추가한다.
> 

`isNotSameNumberTest`를 보면 `Number#valueOf`로 가져온 `number1`과 `number2`가 같은 객체를 참조하기 때문에 `number2`만 `setValue`로 변경하더라도 `number1`도 함꼐 변경되는 것을 알 수 있다.

위 예시처럼 이미 공유된 객체들도 함께 value가 변경이 되므로 시스템에 예기치 못한 사이드 이펙트를 줄 수 있다. 따라서 플라이웨이트 패턴을 활용할 때는 객체에 어떤 변경도 할 수 없는 불변객체로 사용해야한다.

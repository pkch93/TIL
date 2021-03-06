# Day 7

## 개발자가 반드시 정복해야 할 객체 지향과 디자인 패턴

객체지향설계를 하다 보면 이전과 비슷한 상황에서 사용한 설계를 재사용하는 경우가 종종 있다. 이때 반복적으로 사용하는 설계는 클래스, 객체 구성, 객체 간 메세지 흐름에서 일정한 패턴을 갖는다.

이런 패턴들을 흔히 디자인 패턴이라 부르며 잘 활용한다면 아래와 같은 이점을 얻을 수 잇다.

* 상황에 맞는 올바른 설계를 빠르게 적용
* 각 패턴의 장단점을 통해 설계 선택에 도움을 얻을 수 있다.
* 설계 패턴에 이름을 붙임으로써 문서화, 이해, 유지보수에 도움을 줄 수 있다.

### 1. strategy Pattern \(전략 패턴\)

어떤 신선 상품을 판매하는 매장이 있다고 가정한다. 매장에서는 첫손님에게 10%의 할인, 유통기한 마감 임박 상품에는 20%의 할인을 해준다.

위 요구사항을 전략패턴 없이 작성하면 다음과 같다.

```java
public class NonPatternCalculator {

    private static final double FIRST_GUEST_DISCOUNT_RATE = 0.9;
    private static final double NON_FRESH_DISCOUNT_RATE = 0.8;

    public static int calculate(boolean isFirst, List<Item> items) {
        int sum = 0;

        for (Item item : items) {
            if (isFirst) {
                sum += (int) (item.getPrice() * FIRST_GUEST_DISCOUNT_RATE);
            } else if (!item.isFresh()) {
                sum += (int) (item.getPrice() * NOT_FRESH_DISCOUNT_RATE);
            } else {
                sum += item.getPrice();
            }
        }

        return sum;
    }
}
```

비교적 간단한 코드이지만 위 코드는 유지보수에 큰 문제점을 가지고 있다.

1. 서로 다른 정책이 한 곳에 섞여 있어 할인 정책이 추가될수록 코드 분석이 어려워진다.
2. 새로운 할인 정책이 추가될 때 마다 if 블록이 추가되어 calculate 메서드 수정이 어려워진다.

때문에 위 문제를 해결하는 방법은 할인마다 하나의 객체로 분리하는 것이다.

```java
public interface DiscountStrategy {
    int calculate(Item item);
}

public class Calculator {
    private DiscountStrategy discountStrategy;

    public Calculator(final DiscountStrategy discountStrategy) {
        this.discountStrategy = discountStrategy;
    }

    public int calculate(List<Item> items) {
        int sum = 0;

        for (Item item : items) {
            sum += discountStrategy.calculate(item);
        }

        return sum;
    }
}
```

위 DiscountStrategy는 할인 정책을 추상화한 인터페이스이다. 이를 통해 구현 클래스인 Calculator에 상황에 따라 할인 계산 정책을 제공한다.

이떄, 가격 할인 정책을 추상화하는 DiscountStrategy를 전략이라 부르고 가격 계산 자체의 책임을 가지는 Calculator를 콘텍스트 \(Context\)라고 부른다.

전략 패턴은 특정 콘텍스트에서 전략을 별도로 분리하는 방법이다.

```java
Calculator cal = new Calculator(item -> (int) (item.getPrice() * 0.9));
```

위와 같이 첫 손님 할인을 적용하는 할인 계산기를 만들 수 있다.

단, 콘텍스트의 클라이언트가 전략의 인터페이스가 아닌 상세 구현을 안다는 것이 문제처럼 보일 수 있다. 하지만, 위 경우는 클라이언트의 코드와 구현 클래스가 쌍을 이루므로 유지보수 문제가 발생할 가능성이 적다.

전략 패턴의 이점은 콘텍스트의 코드 변경 없이 새로운 전략을 계속해서 추가할 수 있다는 점이다. 계산을 하는 Calculator 코드는 변경되지 않으면서 할인에 필요한 전략 구현 클래스를 만들어 주면 된다.

주로 비슷한 코드를 실행하는 if-else 블록이 있을 경우나 동일한 기능의 알고리즘 변경이 필요할 때 전략 패턴을 적용한다.

### 2. Template Method \(템플릿 메서드\) 패턴

템플릿 메서드 패턴은 실행 과정 및 단계는 동일한데 각 단계 중 일부의 구현이 다른 경우에 사용할 수 있는 패턴이다. 구성은 다음 두 가지로 이뤄진다.

* 실행 과정을 구현한 상위 클래스
* 실행 과정의 일부 단계를 구현한 하위 클래스

상위 클래스에서는 실행 과정을 구현한다. \(공통부분\) 그 중 구현이 달라지는 일부 단계를 추상메서드로 정의하여 하위 클래스에서 일부 단계를 구현하도록 만든다.

대표적으로 인증 방식이 위 패턴을 적용하기 가장 적합한 예시이다. 인증 로직은 거의 비슷한 상태에서 어떤 방식으로 진행하느냐에 따라 달라지기 때문이다. \(ex. DB / LDAP 방식\)

템플릿 메서드의 특징은 하위 클래스가 아닌 상위 클래스가 흐름을 제어한다는 것이다. 일반적으로는 하위 클래스에서 상위 타입의 기능을 재사용할지 여부를 결정하므로 하위 클래스가 흐름을 제어한다. 반면, 템플릿 메서드 패턴은 상위 타입의 템플릿 메서드가 흐름을 제어하고 하위 탸입의 메서드는 템플릿 메서드 내부에서 호출되는 방식을 사용한다.

때문에 상위 클래스에서 제어 확장 위치를 제공해주고 하위 클래스에서 확장 기능을 구현하도록 만들 수 있다.

이때 제어 확장 위치를 제공해주는 메서드를 훅\(hook\) 메서드라고 한다.

> 상위 클래스에서 실행 시점이 제어되고 기본 구현을 제공하면서 알맞게 확장할 수 있는 메서드를 훅 메서드라고 한다.

### 3. State \(상태\) 패턴

기능이 상태에 따라 다르게 동작해야하는 경우 사용할 수 있는 패턴이 상태 패턴이다.

이떄 유념해야하는 것은 상태가 기능을 제공한다는 점이다.

상태 패턴의 장점은 새로운 상태가 추가되더라도 콘텍스트 코드가 받는 영향은 최소화된다는 점이다. 상태가 많아지면 클래스를 추가하여 적용하므로 코드의 복잡도는 증가하지 않는다.

또한 상태에 따른 동작을 구현한 코드가 상태에 있으므로 상태 별로 동작을 처리하기 용이하다.

* 유의할 점

상태 패턴에서는 콘텍스트의 상태 변경을 어디에서 할지가 관건이다. 보통 콘텍스트에서 변경할지와 상태 객체가 직접 변경할지로 구분된다.

먼저 콘텍스트에서 상태를 변경하는 방식은 비교적 상태의 갯수가 적고 상태 변경 규칙이 거의 바뀌지 않는 경우에 유리하다. 만약 규칙이 바뀌고 갯수가 증가하면 그만큼 상태 변경 코드의 복잡도가 높아지기 때문이다.

반면 상태 객체에서 콘텍스트 상태를 변경하는경우는 콘텍스트에 영향을 주지 않으면서 상태를 추가하고 상태 변경 규칙을 바꿀 수 있게된다. 다만, 상태 구현 클래스가 많아져 변경 규칙 파악이 어려워진다.

이같은 유의점 때문에 상황에 맞게 고려하여 사용해야한다.

### 4. Decorator \(데코레이터\) 패턴

상속은 기능을 확장하는 방법을 제공해준다. 다만, 다양한 조합의 기능 확장을 할 때 클래스가 불필요하게 증가하는 문제가 생긴다. 이런 경우에 사용할 수 있는 방법이 바로 데코레이터 패턴이다.

데코레이터 패턴은 상속이 아닌 위임 방식으로 기능을 확장해나간다.

Decorator는 모든 데코레이터를 위한 기반 기능을 제공하는 추상 클래스이다. 데코레이터 패턴을 사용하면 각 확장 기능들의 구현이 별도의 클래스로 분리되기 때문에 각 확장 기능과 원래 기능을 서로 영향 없이 변경할 수 있도록 만들어준다. 즉, 단일 책임 원칙을 지킬 수 있도록 만들어준다.

데코레이터는 전략 / 템플릿 메서드 / 상태 패턴과 함께 매우 흔하게 사용된다.

* 고려할 점
* 데코레이터 대상이 되는 타입의 기능 갯수
* 비정상적으로 동작할 때 어떻게 처리할 것인가
* 단점

데코레이터 객체와 실제 구현 객체가 구분이 되지 않으므로 코드만으로 기능의 동작을 알아보기 힘들다는점

### 5. Proxy \(프록시\) 패턴

프록시 패턴은 실제 객체를 대신하는 프록시 객체를 사용하여 실제 객체의 생성이나 접근 등을 제어할 수 있도록 해주는 패턴이다.

* 고려할 점

프록시 패턴을 구현할 때 실제 객체를 누가 생성할 것이냐에 대해 생각해야한다.

프록시의 경우 실제 객체를 생성해주는 가상 프록시와 접근을 제어하는 보호 프록시가 있는데 가상 프록시의 경우는 실제 생성할 객체의 타입을 가상 프록시의 경우는 실제 객체를 전달하면 되므로 추상 타입을 사용하면된다.

> ※ 참고
>
> 위임기반의 프록시 패턴 vs 테코레이터 패턴
>
> 프록시 패턴은 실제 객체에 대한 접근을
>
> 제어하는데 초점
>
> 데코레이터 패턴은 기능 확장에 초점

## Gradle

### Logging

log는 빌드 툴에서 주된 **UI**이다. Gradle에서는 6개의 log level을 두고 있다.

* Log levels

log level은 command line 또는 gradle.properties를 통해 결정 할 수 있다.

1. no option : LIFECYCLE
2. -q or --quiet : QUIET
3. -w or --warn : WARN
4. -i or --info : INFO
5. -d or --debug : DEBUG \(모든 log message 출력\)
6. stacktrace options

gradle에서는 stacktrace를 사용한다면 Full stacktrace보다 Trancated stacktrace를 사용하길 권장한다. full의 경우는 deprecation warning 등 상대적으로 중요하지 않은 로그까지 보여주기 때문에 매우 번잡한 로그를 만날 수 있다.

1. -s or --stacktrace : Truncated stacktrace
2. -S or --full-stacktrace : Full stacktrace
3. no option : 빌드 에러시에만 출력
4. Writing your own log messages

로그를 출력하는 가장 기본적인 방법은 println 등의 표준 출력을 이용하는 것이다.

```groovy
// build.gradle
println 'it is custom log'
```

또한, gradle은 logger property를 통해 log를 찍을 수 있도록 도와준다. gradle의 logger는 `SLF4J`를 확장하였다.

```groovy
logger.quiet('An info log message which is always logged.')
logger.error('An error log message.')
logger.warn('A warning log message.')
logger.lifecycle('A lifecycle info log message.')
logger.info('An info log message.')
logger.debug('A debug log message.')
logger.trace('A trace log message.')
```

logger를 사용하면 placeholder를 이용해서 동적으로 log를 작성할 수 있도록 도와준다.

```groovy
logger.info('this log level is {}', 'info');
```

위 출력 결과는 `this log level is info`가 된다.

* Logging from external tools and libraries

gradle은 기본적으로 표준 출력을 QUIET level, 표준 에러를 ERROR level로 출력한다. 이런 설정은 변경할 수 있다. gradle의 Project 인스턴스는 LoggingManager를 제공해주는데 이는 gradle이 빌드스크립트를 평가할 때 log 레벨을 선택할 수 있도록 해준다.

```groovy
logging.captureStandardOutput LogLevel.INFO
println 'A message which is logged at INFO level'
```

위와 같이 logging이라는 LoggingManager 타입의 메서드 captureStandardOutput으로 표준 출력을 INFO레벨로 출력하도록 설정할 수 있다.

* Changing what Gradle logs

Gradle의 log UI를 커스텀하여 변경할 수 있다. \(로거 정보 강조, 포멧 변경 등\) `Gradle.useLogger()`를 사용하여 기존 로거를 대체할 수 있다.

커스텀한 logger는 다음의 listener interface의 구현체를 사용할 수 있다.

1. BuildListener
2. ProjectEvalutationListener
3. TaskExecutionGraphListener
4. TaskExecutionListener
5. TaskActionListener

useLogger로 커스텀 로거를 등록했을때 기존의 logger에서 등록한 logger의 구현 interface만을 대체한다.


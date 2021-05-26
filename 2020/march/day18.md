# 2020.03.14 Saturday - Java volatile & synchronized

### Table of Contents

* [volatile](day18.md#volatile)
* [synchronized](day18.md#synchronized)

## volatile

volatile 제한자는 필드 값으로 캐시된 값이 아닌 최신 메모리 값을 가져오게 보장하는 제한자이다. 때문에 volatile 제한자가 있는 필드를 변경하면 해당 변수를 다른 스레드에서 참조하면 캐시된 이전 값이 아닌 최신 메모리 값을 사용한다.

단, volatile은 원자성을 보장하지는 않는다. 여러 스레드에서 동시에 volatile 변수를 다룬다면 예상치 못한 결과가 나타날 수 있다. 따라서 volatile 작업을 한다면 업데이트하는 스레드와 참조하는 스레드를 분리해야한다.

## synchronized

자바에서는 `synchronized`블록으로 베타 제어를 제공한다. 자신의 스레드가 `synchronized`를 처리하고 있다면 다른 스레드에서는 처리할 수 없도록 차단하는 자바 syntax이다. 이처럼 다른 스레드의 접근을 막는 것을 베타 제어라고 한다.

synchtonized를 사용하기 위해서는 처리의 소유권을 얻으려는 대상이 되는 객체, 즉, 락 `lock`객체가 필요하다. lock을 소유한 스레드만이 synchronized 블록에 들어갈 수 있다.

```java
public class Main {
    private static Object lock = new Object();

    private static int a = 0;
    private static int b = 0;

    public static void main(String[] args) {
        synchronized (lock) {
            a++;
            b++;
        }
    }

    public synchronized int getA() {
        return a;
    }

    public synchronized int getB() {
        return b;
    }
}
```

위의 main 메서드 처럼 `synchronized`블록을 사용할 수도 있고 `getA`와 `getB`처럼 메서드에 `synchronized` 키워드를 사용할 수 있다.

메서드에 `synchronized`를 붙이면 해당 메서드가 종료될 때까지 다른 스레드에서 접근할 수 없다. 이 때, 락 객체는 클래스 객체 자신이 된다.

단, `synchronized` 블록을 남용하면 성능에 영향을 줄 수 있다. 또한 `Deadlock`의 원인이 될 수 있다.


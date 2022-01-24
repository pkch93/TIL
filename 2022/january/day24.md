# 2022.01.24 TIL - Prototype Pattern

- [2022.01.24 TIL - Prototype Pattern](#20220124-til---prototype-pattern)
  - [Prototype Pattern에 대한 개인적인 생각](#prototype-pattern에-대한-개인적인-생각)

프로토타입 패턴은 기존에 생성된 객체를 기반으로 복제하여 생성하는 기법이다. 즉, 프로토타입 패턴은 객체를 생성하는 수단인 **new**를 사용하지 않고 생성하는 패턴이다. 이를 위해서 언어에서 제공하는 객체를 복사하는 방법을 사용한다. 예를 들어 Java의 Cloneable 인터페이스나 PHP의 clone 키워드이다.

단, Java와 PHP의 clone은 얕은 복사로 동작한다. 얕은 복사란 객체를 공유하는 방식으로 복사하는 것이다. int나 long, String 같은 원시값에 대해서는 얕은 복사는 문제 없이 동작한다.

```java
public class Prototype implements Cloneable {
    private int number;
    private String str;

    public Prototype(int number, String str) {
        this.number = number;
        this.str = str;
    }

    public int getNumber() {
        return number;
    }

    public String getStr() {
        return str;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setStr(String str) {
        this.str = str;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prototype prototype = (Prototype) o;
        return number == prototype.number && Objects.equals(str, prototype.str);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, str);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
```

Prototype을 Cloneable을 구현하여 위와 같이 구현한다.

> Effective Java 3/e에서는 clone 메서드의 규약을 다음과 같이 정의한다.
> 
> 1. `x.clone() ≠ x`는 `true`이다.
> 2. `x.clone().getClass() == x.getClass()`는 참이여야한다.
> 3. `x.clone().equals(x)`는 참이여야한다.
> 
> 위 Prototype 클래스는 clone 메서드의 규약을 충족하기 위해 equals, hashcode 메서드를 재정의했다.
> 

```java
@Test
void prototypeClone() throws CloneNotSupportedException {
    // given
    Prototype prototype = new Prototype(1, "str");

    // when
    Prototype actual = (Prototype) prototype.clone();

    // then
    assertTrue(prototype != actual);
    assertTrue(prototype.equals(actual));

    prototype.setNumber(2);
    prototype.setStr("str2");
    Assertions.assertNotEquals(prototype.getNumber(), actual.getNumber());
    Assertions.assertNotEquals(prototype.getStr(), actual.getStr());
}
```

따라서 위와 같은 테스트가 통과하는 것이다. clone한 객체와 원본 객체는 다른 주소값을 가지면서 equals는 동일하다. 여기에 prototype의 값을 바꾸더라도 원시값을 사용하므로 prototype과 actual은 값을 공유하지 않는다.

하지만 값을 공유하는 객체를 값으로 가진다면 얕은 복사는 값을 공유하게 된다.

```java
public class Wrapper implements Cloneable {
    private Prototype prototype;

    public Wrapper(Prototype prototype) {
        this.prototype = prototype;
    }

    public Prototype getPrototype() {
        return prototype;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
```

앞서 정의한 Prototype을 값으로 가지는 Wrapper를 위와 같이 정의한다. 위 Wrapper의 clone을 사용하는 테스트를 작성해본다.

```java
@Test
void wrapperClone() throws CloneNotSupportedException {
    // given
    Prototype prototype = new Prototype(1, "str");
    Wrapper wrapper = new Wrapper(prototype);

    // when
    Wrapper actual = (Wrapper) wrapper.clone();

    // then
    assertSame(prototype, actual.getPrototype()); // prototype의 주소값이 같음

    prototype.setNumber(2);
    assertEquals(prototype.getNumber(), actual.getPrototype().getNumber());
}
```

Wrapper의 clone은 원본 Wrapper가 가진 Prototype과 같은 객체를 공유한다. 그 증거로 `assertSame(prototype, actual.getPrototype())`로 같은 주소를 사용함을 알 수 있다. 또한 원본의 number를 2로 변경했음에도 복사본인 actual의 number 또한 2로 변경됨을 알 수 있다.

이는 얕은 복사가 객체의 경우는 같은 객체를 공유하기 때문이다. 이처럼 객체를 clone 해야하는 경우는 이와 같은 문제를 인지하고 있어야한다.

> 사실 객체를 공유하여 값이 변경되는 경우는 디버깅을 어렵게하고 버그 발견을 어렵게하므로 피하는 것이 좋다고 생각한다.

## Prototype Pattern에 대한 개인적인 생각

프로토타입 패턴은 원본 객체를 기반으로 복제하여 새로운 객체를 생성한다는 것과 new를 사용하지 않고 객체를 생성하여 생성 비용을 줄일 수 있다는 점을 특징으로 설명하고 있다.

다만, new를 사용하지 않고 복제하는 방법은 Java의 경우는 `Object#clone` 메서드로 지원하는데 이는 얕은 복사를 기반으로 한다. 즉, clone의 대상 클래스에 객체가 존재하는 경우 해당 객체를 원본과 복제본이 공유하는 문제가 발생한다. 이는 시스템에 생각하지 못한 버그를 만들 수 있기 때문에 되도록이면 자제하는 것이 좋아보인다.

> 만약 clone과 같은 복제 수단이 깊은 복사를 지원하는 언어라면 사용해도 무방할 듯하다.

프로토타입 패턴이 처음 소개된 Gof 디자인 패턴 책이 발간된 연도 1994년은 컴퓨터의 리소스가 제한적이었던 것을 생각하면 복제가 유용했을 것 같다고 생각한다. 하지만 컴퓨터 하드웨어가 발달한 요즘에는 차라리 `new`를 통해 깊은 복사를 하는 방식이 안전한 프로그램, 시스템을 만드는 방법이라고 생각한다.

만약 `new를 사용하지 않고 객체를 생성하여 생성 비용을 줄일 수 있다는 점`이 프로토타입 패턴의 필수 요소라면 사실상 사용할 일이 없는 패턴이 아닐까? 그것이 아니라 `원본 객체를 기반으로 복제하여 새로운 객체를 생성한다는 것` 만으로도 프로토타입 패턴이 의미가 있다면 종종 사용해볼 패턴이 아닐까 생각한다.
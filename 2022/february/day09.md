# 2022.02.09 TIL - Visitor Pattern

- [2022.02.09 TIL - Visitor Pattern](#20220209-til---visitor-pattern)
  - [Visitor Pattern 구성](#visitor-pattern-구성)
  - [Visitor Pattern 사용해보기](#visitor-pattern-사용해보기)
  - [참고](#참고)

방문자 패턴은 공통된 객체의 데이터 구조와 처리를 분리하는 패턴이다. 방문자 패턴을 활용하면 구조를 수정하지 않고도 실질적으로 새로운 동작을 기존의 객체 구조에 추가할 수 있다.

## Visitor Pattern 구성

방문자 패턴은 방문자를 통해 로직을 구현하는 피방문자 `Visitable`과 방문자 `Visitor`로 구성된다.

```java
public interface Visitable {
    void accept(Visitor visitor);
}

public interface Visitor {
    void visit(Visitable visitable);
}
```

피방문자와 방문자는 위와 같이 메서드로 각 객체를 전달함으로써 서로를 알게된다. 즉, Visitable이 수행해야하는 행동을 인자로 받은 Visitor를 통해 수행하도록 만들면서 필요한 정보를 사용할 수 있도록 Visitable 객체를 `Visitor#visit`의 인자로 전달한다.

## Visitor Pattern 사용해보기

기존 시스템에 지리정보를 제공하는 로직이 있다고 가정한다.

```java
public interface Shape {
    void position();
}

public class Dot implements Shape {
    // ...
}

public class Circle implements Shape {
    // ...
}

public class Rectangle implements Shape {
    // ...
}

public class Graph implements Shape {
    // ...
}
```

여기서 기존 로직에 각 지리정보에 대한 위치 정보를 XML 포멧으로 받아볼 수 있도록 기능을 구현해야한다고 가정한다.

정보를 XML으로 표현하는 로직을 `Shape` 인터페이스에 정의하고 각 `Shape` 구현체들에 XML 표현 로직을 구현할 수 있다.

```java
public interface Shape {
    void position();

    void xmlPosition();
}
```

단, 표현하는 로직이 `Shape`과 구현체들에 있어야하나라는 고민이 든다. 

특히 MVC 패턴에서는 도메인로직 `Model` 에서 `View` 표현방식을 가지지 않도록 제시하기 때문에 도메인 로직에서 뷰 표현로직이 들어가지 않도록 막아둘수도 있다.

만약 위치 정보를 json으로도 표현해야한다면 어떻게 해야할까? `Shape`에 `xmlPosition`과 같이 `jsonPosition` 메서드를 정의해야할까? 그렇다면 `Shape`이 표현 로직으로 비대해질 수 있다. 그리고 기능을 추가할때마다 `Shape`과 구현체들에 변경이 필요하므로 OCP를 위반한다.

이런 상황에서 방문자 패턴을 사용할 수 있다.

```java
public interface GeographicPresenter {
    void present(Shape shape);
}
```

> 지리 정보를 표현한다는 의미로 `Visitor` 대신 `GeopraphicPresenter`라는 명명을 하였다.

```java
public interface Shape {
    void position(GeographicPresenter geographicPresenter);
}

public class Dot implements Shape {
    @Override
    public void position(GeographicPresenter geographicPresenter) {
        geographicPresenter.present(this);
    }
}

public class Circle implements Shape {
    @Override
    public void position(GeographicPresenter geographicPresenter) {
        geographicPresenter.present(this);
    }
}

public class Rectangle implements Shape {
    @Override
    public void position(GeographicPresenter geographicPresenter) {
        geographicPresenter.present(this);
    }
}

public class Graph implements Shape {
    @Override
    public void position(GeographicPresenter geographicPresenter) {
        geographicPresenter.present(this);
    }
}
```

> 모두 같은 position 메서드 구현을 하고 있다면 상위 추상 클래스를 정의해서 중복을 방지할 수도 있다.

위와 같이 표현 방식에 대해서는 `Visitor` 역할의 `GeographicPresenter`에게 위임할 수 있다. 그리고 각 표현에 대한 처리를 `GeograpicPresenter`의 구현으로 해결할 수 있다.

```java
public class ConsoleGeographicPresenter implements GeographicPresenter {
    @Override
    public void present(Shape shape) { // ... }
}

public class XmlGeographicPresenter implements GeographicPresenter {
    @Override
    public void present(Shape shape) { // ... }
}
```

만약 콘솔 표현방식이나 XML 표현방식 이외에 Json 표현방식이 필요하다면 다음과 같이 `JsonGeographicPresenter`를 구현하여 기능을 추가할 수 있다.

```java
public class JsonGeographicPresenter implements GeographicPresenter {
    @Override
    public void present(Shape shape) { // ... }
}
```

## 참고

비지터 패턴 위키: [https://ko.wikipedia.org/wiki/비지터_패턴](https://ko.wikipedia.org/wiki/%EB%B9%84%EC%A7%80%ED%84%B0_%ED%8C%A8%ED%84%B4)
refactoring guru Visitor 참고: [https://refactoring.guru/design-patterns/visitor](https://refactoring.guru/design-patterns/visitor)
[https://stackoverflow.com/questions/6762256/how-does-double-dispatch-work-in-visitor-pattern](https://stackoverflow.com/questions/6762256/how-does-double-dispatch-work-in-visitor-pattern)
Visotor Pattern에서 어떻게 더블디스패치를 활용하는지: [https://stackoverflow.com/questions/6762256/how-does-double-dispatch-work-in-visitor-pattern](https://stackoverflow.com/questions/6762256/how-does-double-dispatch-work-in-visitor-pattern)
Java에서의 Double Dispatch: [https://stackoverflow.com/questions/43960454/does-java-support-multiple-dispatch-if-not-how-is-the-below-code-working](https://stackoverflow.com/questions/43960454/does-java-support-multiple-dispatch-if-not-how-is-the-below-code-working)
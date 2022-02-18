# 2022.02.18 TIL - State Pattern

- [2022.02.18 TIL - State Pattern](#20220218-til---state-pattern)
  - [State Pattern 사용하기](#state-pattern-사용하기)
  - [전략 패턴과의 차이](#전략-패턴과의-차이)

상태 패턴은 조건에 따른 별개의 동작을 제어문이 아닌 객체를 캡슐화하여 독립된 동작으로 구분하는 패턴이다.

> 상태 패턴은 상태 표현 패턴이라고도 말한다. 

![](https://user-images.githubusercontent.com/30178507/154706680-7ef3e19b-18ac-44f0-b88b-e51b1ef04f3f.png)

## State Pattern 사용하기

노트북을 키고 끄는 상태를 가지고 있고 이를 끄고 키는 동작이 있다고 가정한다.

```java
public class Laptop {
    private static final String ON = "ON";
    private static final String OFF = "OFF";
    private String state = OFF;

    public void power() {
        if (state.equals(ON)) {
            this.state = OFF;
            System.out.println("노트북을 끕니다.");
        } else if (state.equals(OFF)) {
            this.state = ON;
            System.out.println("노트북을 켭니다.");
        }
    }
}
```

만약 상태 패턴을 사용하지 않고 구현한다면 다음과 같이 `ON`, `OFF` 상태에 따른 분기문이 필연적으로 들어가게된다. 이를 상태 패턴으로 구현해보면 제어문을 없앨 수 있고 `ON`, `OFF` 이외의 상태가 추가되더라도 Laptop의 toggle이 변경될 일이 없어진다.

```java
public interface State {
    void power();
}

public class TurnOn implements State {
    @Override
    public void power() {
        System.out.println("노트북을 끕니다.");
    }
}

public class TurnOff implements State {
    @Override
    public void power() {
        System.out.println("노트북을 켭니다.");
    }
}

public class Laptop {
    private State state = new TurnOff();

    public void power() {
       state.power();
    }

    public void changeState(State state) {
        this.state = state;
    }
}
```

즉, State의 구현에서 각 상태에 따른 동작을 처리함으로써 제어문을 없앨 수 있다. 또 새로운 상태가 추가되면 Laptop의 state를 변경해주면 해당 상태에 따른 동작으로 유도할 수 있다.

## 전략 패턴과의 차이

가만보면 전략 패턴과 굉장히 유사해보인다. 전략 패턴과 상태 패턴 모두 런타임에 구현을 바꿔서 동작을 다르게 만들 수 있다. 전략 패턴과 상태 패턴은 어떤 목적으로 사용하느냐에 따라 다르게 구별할 수 있다.

전략 패턴은 목적성이 분명한 부분에 동작을 다양하게 주기 위해서 사용한다. 반면 상태 패턴은 상태값이 매우 중요하며 상태값에 따른 동작 처리를 상태의 구현 클래스에서 처리한다.
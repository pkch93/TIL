# 2022.02.14 TIL - Chian Pattern

- [2022.02.14 TIL - Chian Pattern](#20220214-til---chian-pattern)
  - [핸들러와 단일 책임 원칙](#핸들러와-단일-책임-원칙)
  - [행동 분리하기](#행동-분리하기)
  - [체인 패턴](#체인-패턴)

체인 패턴은 객체 메세지의 송신과 수신을 분리해서 처리하는 패턴이다.

## 핸들러와 단일 책임 원칙

30살 미만의 미혼인 박씨 성을 가진 사람을 회원으로 받는 커뮤니티가 있다고 가정한다. 이때 회원가입 시 나이, 성이 박씨 인지, 기혼인지 순으로 입력값을 확인하여 에러를 보여주는 것이 요구사항이라고 한다.

```java
public class Input {
    private final String name;
    private final int age;
    private final boolean married;

    public Input(String name, int age, boolean married) {
        this.name = name;
        this.age = age;
        this.married = married;
    }

    // getters
}

public class InputValidator {
    public void validate(Input input) {
        if (input.getAge() >= 30) {
            System.out.println("30살 이상은 가입할 수 없습니다.");
        } else if (!input.getName().startsWith("박")) {
            System.out.println("성이 박이 아니라면 가입할 수 없습니다.");
        } else if (input.isMarried()) {
            System.out.println("기혼자는 가입할 수 없습니다");
        }
    }
}
```

따라서 InputValidator를 정의하여 `validate` 메서드를 정의한다.

위와 같은 InputValidator는 분리된 로직을 처리하는 핸들러이다. 핸들러는 처리 로직을 분리하여 행동을 독립적으로 관리할 수 있다. InputValidator에서는 입력값에 따라 여러 동작을 수행한다.

단, 위와 같은 핸들러는 이벤트 처리 로직으로 다양한 동작을 선택적으로 처리한다. 이런점에서 핸들러 객체는 단일 책임 원칙을 위반하게 된다.

이런 문제를 체인 패턴을 통해 보완할 수 있다.

## 행동 분리하기

체인 패턴의 아이디어는 핸들러에서 순차적으로 이벤트를 검사하는 조건을 분리하는 것이다. 앞선 InputValidator 예시는 요청된 `validate`를 처리하며 결과에 따라 이벤트를 처리한다. 이는 핸들러가 처리 로직과 출력 동작을 모두 가지고 있기 때문이다. 때문에 핸들러의 메세지를 독립적으로 분리한다.

```java
public class AgeValidateMessage {
    public void print() {
        System.out.println("30살 이상은 가입할 수 없습니다.");
    }
}

public class LastNameValidateMessage {
    public void print() {
        System.out.println("성이 박이 아니라면 가입할 수 없습니다.");
    }
}

public class MarriedValidateMessage {
    public void print() {
        System.out.println("기혼자는 가입할 수 없습니다");
    }
}
```

그리고 이를 핸들러에 적용하면 다음과 같다.

```java
public class InputValidator {
    public void validateWithMessage(Input input) {
        if (input.getAge() >= 30) {
            new AgeValidateMessage().print();
        } else if (!input.getName().startsWith("박")) {
            new LastNameValidateMessage().print();
        } else if (input.isMarried()) {
            new MarriedValidateMessage().print();
        }
    }
}
```

다만, 위와 같은 형태도 아직은 단일 책임을 위반한다. 유효성 검증이 추가가 된다면 핸들러 InputValidator가 추가되어야하고 출력을 위한 메세지도 추가가 되어야한다.

이를 체인 패턴으로 개선해본다.

## 체인 패턴

체인으로 엮기 위한 ValidatorChain을 추상화한다.

```java
public abstract class ValidatorChain {
    protected ValidatorChain next;

    public void setNext(ValidatorChain validatorChain) {
        this.next = validatorChain;
    }

    public abstract void validate(Input input);
}
```

next는 현재 Validator 다음 우선순위의 Validator를 사용하기 위해 가진다.

```java
public class AgeValidator extends ValidatorChain {
    @Override
    public void validate(Input input) {
        if (input.getAge() >= 30) {
            System.out.println("30살 이상은 가입할 수 없습니다.");
            return;
        }

        next.validate(input);
    }
}

public class LastNameValidator extends ValidatorChain {
    @Override
    public void validate(Input input) {
        if (!input.getName().startsWith("박")) {
            System.out.println("성이 박이 아니라면 가입할 수 없습니다.");
        }

        next.validate(input);
    }
}

public class MarriedValidator extends ValidatorChain {
    @Override
    public void validate(Input input) {
        if (input.isMarried()) {
            System.out.println("기혼자는 가입할 수 없습니다");
        }
    }
}
```

그리고 각각의 Validator를 위와 같이 정의한다. 그리고 채인 패턴으로 InputValidator를 구현한다.

```java
public class InputValidator {
    //...
    public void validateForChain(Input input) {
        AgeValidator firstValidator = new AgeValidator();
        LastNameValidator secondValidator = new LastNameValidator();
        MarriedValidator lastValidator = new MarriedValidator();
        firstValidator.setNext(secondValidator);
        secondValidator.setNext(lastValidator);

        firstValidator.validate(input);
    }
}
```

> 위 validateForChain에서는 핸들러에서 다음 Validator를 추가한다. 차라리 그 순서가 정해져있다면 setNext를 없애고 Validator의 생성자에서 다음 Validator를 받아 사용하도록 만들어도 괜찮을 거 같다.

위 예시처럼 우선순위 같이 순차적 행동을 처리하는데 체인패턴이 유용할 수 있다. 다만, setNext로 다음 Chain을 결정한다면 런타임에 예상치 못한 Chain이 들어올 수 있거나 Chain을 추가할 때 순서를 햇갈려 잘못된 Chain을 등록할 수 있다는 부분이 문제가 될 수 있을 것 같다.
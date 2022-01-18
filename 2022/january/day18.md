# 2022.01.18 TIL - Factory Method Pattern와 Abstract Factory Pattern

- [2022.01.18 TIL - Factory Method Pattern와 Abstract Factory Pattern](#20220118-til---factory-method-pattern와-abstract-factory-pattern)
  - [Factory Method Pattern](#factory-method-pattern)
  - [Abstract Factory Pattern](#abstract-factory-pattern)

## Factory Method Pattern

팩토리 메서드 패턴은 팩토리 패턴과 템플릿 메서드 패턴이 결합된 패턴이다.

즉, 템플릿 메서드 패턴처럼 객체 생성에 필요한 공통 부분을 상위 클래스에 정의하고 실제 객체를 생성하는 부분을 하위클래스에서 정의하는 패턴이다.

```kotlin
public abstract class Animal {
    private final String name;
    private final int age;

    public Animal(String name, int age) {
        this.name = name;
        this.age = age;
    }

    abstract protected void cry();
}
```

동물을 표현한 추상클래스 `Animal`을 위와 같이 정의한다. 각 동물마다 울음소리는 다를 수 있기 때문에 `cry` 메서드는 추상메서드로 정의한다. 모든 동물들에 이름과 나이가 존재한다고 가정한다. 

```kotlin
public abstract class AnimalFactory {

    public Animal create(String name) {
        int age = 1;
        return createInternal(name, age);
    }

    abstract protected Animal createInternal(String name, int age);
}
```

모든 동물들에 이름과 나이가 존재하므로 위와 같이 이름과 나이를 처리하는 로직을 상위 클래스인 `AnimalFactory`에 정의한다. AnimalFactory에서 생성하는 Animal의 나이는 1이라고 가정한다. `새로 생성되는 동물들..`

그리고 이 값들을 받아서 각 Animal을 생성하는 Factory에서 실제 동물들을 생성하도록 한다.

```kotlin
public class Cat extends Animal {

    public Cat(String name, int age) {
        super(name, age);
    }

    @Override
    protected void cry() {
        System.out.println("야옹");
    }
}

public class CatFactory extends AnimalFactory {

    @Override
    protected Animal createInternal(String name, int age) {
        return new Cat(name, age);
    }
}

public class Dog extends Animal {

    public Dog(String name, int age) {
        super(name, age);
    }

    @Override
    protected void cry() {
        System.out.println("멍멍");
    }
}

public class DogFactory extends AnimalFactory {

    @Override
    protected Animal createInternal(String name, int age) {
        return new Dog(name, age);
    }
}
```

## Abstract Factory Pattern

추상 팩토리 패턴은 팩토리 메서드를 확장한 패턴이다. 팩토리 메서드 패턴의 기능에 더해서 추상화된 그룹을 형성하고 관리하는 패턴이 바로 추상 팩토리 패턴이다.

추상 팩토리는 여러 개의 팩토리 메서드를 그룹으로 묶은 것과 유사하다. 자동차를 생성하는 팩토리를 작성한다고 가정한다.

```java
public interface Tire {
}

public interface Door {
}

public interface Factory {
    Tire createTire();
    Door createDoor();
}
```

다음과 같이 타이어와 문 부품 2개를 생산하는 팩토리를 선언한다. 즉, 위 자동차 생성에서는 자동차 생산에 필요한 부품들 단위로 하나의 팩토리 그룹이 이뤄진다.

각 부품들은 한국에서 만들어 질 수 있고 미국에서 만들어 질 수 있다. 따라서 한국 팩토리, 미국 팩토리로 그룹을 나눌 수 있다.

```java
public class KoreaTire implements Tire {
}

public class KoreaDoor implements Door {
}

public class KoreaFactory implements Factory {

    @Override
    public Tire createTire() {
        return new KoreaTire();
    }

    @Override
    public Door createDoor() {
        return new KoreaDoor();
    }
}
```

```java
public class AmericaTire implements Tire {
}

public class AmericaDoor implements Door {
}

public class AmericaFactory implements Factory {

    @Override
    public Tire createTire() {
        return new AmericaTire();
    }

    @Override
    public Door createDoor() {
        return new AmericaDoor();
    }
}
```

이렇게 각 그룹마다 필요한 객체를 생성할 수 있도록 관리가 가능하다. 즉, 추상 팩토리는 복수의 객체 생성을 담당하는 그룹을 관리할 수 있도록 도와준다.

위 예시에서 만약 중국 그룹을 만들거나 영국 그룹을 만든다면 위와 같이 `ChinaFactory`나 `EnglandFactory` 등을 정의하여 그룹화 할 수 있다.

다만 추상 팩토리의 경우 그룹의 요소를 추가하는 데에는 불리하다. 이는 추상메서드 특성상 상위 클래스에 추가할 때마다 서브 클래스 그룹으로 분리된 모든 클래스에 구현을 해줘야하기 때문이다.

즉, 만약 `Seat` 를 추가해야한다고 가정하면 `KoreaFactory`와 `AmericaFactory`에도 추가해줘야한다.

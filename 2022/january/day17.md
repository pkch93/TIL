# 2022.01.17 TIL - Factory Pattern

- [2022.01.17 TIL - Factory Pattern](#20220117-til---factory-pattern)
  - [의존 관계의 문제점](#의존-관계의-문제점)
    - [강력한 의존관계](#강력한-의존관계)
    - [Factory로 느슨한 관계 만들기](#factory로-느슨한-관계-만들기)

팩토리 패턴은 객체 생성을 위한 가장 기본적인 패턴이다. 객체 생성에 대한 책임을 다른 클래스에게 위임하는 패턴이 바로 팩토리 패턴이다.

> 팩토리 패턴은 객체지향 프로그래밍과 Dependency Injection에 대해서 먼저 알아두면 도움이 된다.

## 의존 관계의 문제점

팩토리 패턴은 객체 생성에 대한 책임을 다른 클래스에게 위임하는 것이라고 했다. 객체 생성 과정에는 의존성이 발생하기 마련인데 이 과정에서 강력한 의존 관계가 형성된다. 팩토리 패턴을 활용하면 강력한 의존관계를 느슨하게 바꿀 수 있다.

### 강력한 의존관계

그렇다면 의존 관계가 강력하다, 강하다라는 말은 무엇일까? 이는 해당 객체가 다른 객체를 얼마나 알고 있느냐로 판단할 수 있다.

여기서 가장 강력한 의존관계로 만드는 방법은 의존하는 객체의 생성자를 사용하는 것이다. 이는 의존 객체에 필요한 필드를 모두 알아야한다는 말과 같다.

```java
public class Employee {
    private final String name;
    private final int age;
    private final int career;
    private final Position position;

    public Employee(String name, int age, int career, Position position) {
        this.name = name;
        this.age = age;
        this.career = career;
        this.position = position;
    }

    enum Position {
        SALES("영업"),
        DEVELOPMENT("개발"),
        MARKETING("마케팅"),
        DESIGN("디자인");

        private final String description;

        Position(String description) {
            this.description = description;
        }
    }
}
```

다음과 같이 회사원을 표현한 `Employee` 클래스가 있다. 현재는 이름 `name`, 나이 `age`, 경력 `career`, 직책 `position`을 필요로 한다.

그리고 위 `Employee` 클래스를 의존하는 회사와 헤드헌터 클래스가 다음과 같이 정의되어 있다고 가정한다.

```java
public class Company {
    private final List<Employee> employees = new ArrayList<>();

    public void joining(String name, int age, int career, Employee.Position position) {
        employees.add(new Employee(name, age, career, position));
    }
} 
```

```java
public class HeadHunter {
    private final List<Employee> clients = new ArrayList<>();

    public void addClient(String name, int age, int career, Employee.Position position) {
        clients.add(new Employee(name, age, career, position));
    }
}
```

각각 회사의 직원과 헤드헌터가 관리하는 클라이언트를 `Employee`로 가지고 있다. 여기서 직원과 클라이언트를 추가할때 각각 `joining`, `addClient`라는 메서드 내부에서 `Employee` 생성자를 부른다.

만약 위 상황에서 `Employee`에 성별 `gender` 필드가 추가된다고 하자.

```java
public class Employee {
    private final String name;
    private final int age;
    private final int career;
    private final Position position;
    private final Gender gender;

    public Employee(String name, int age, int career, Position position, Gender gender) {
        this.name = name;
        this.age = age;
        this.career = career;
        this.position = position;
        this.gender = gender;
    }

    // ...

    enum Gender {
        MALE, FEMALE
    }
}
```

이렇게 성별 `gender` 필드가 추가되고 생성자에서 받아서 처리하도록 만들면 이를 사용하고 있는 `Company#joining`과 `HeadHunter#addClient`도 영향을 받게 된다. 결국 이들 두 메서드도 수정이 필요한 상황이 된다.

만약 `Employee`의 생성자를 그대로 사용하는 클래스가 많으면 많을수록 이런 번거로움은 커진다. 따라서 `Employee`를 생성하는 역할을 담당하는 팩토리 클래스를 따로 두고 `Employee`의 생성자를 사용하고 있는 각 클래스에서는 `Employee` 자체를 주입 받아서 사용하면 강력했던 의존 관계를 느슨하게 만들 수 있다.

### Factory로 느슨한 관계 만들기

```java
public class EmployeeFactory {
    private static class LazyHolder {
        public static final EmployeeFactory INSTANCE = new EmployeeFactory();
    }

    public static EmployeeFactory getFactory() {
        return LazyHolder.INSTANCE;
    }

    public Employee createEmployee(String name, int age, int career, Employee.Position position, Employee.Gender gender) {
        return new Employee(name, age, career, position, gender);
    }
}
```

위와 같이 `Employee`를 생성하는 `EmployeeFactory`를 정의한다. 그리고 생성 메서드 createEmployee를 정의한다. 기존에 `Company`와 `HeadHunter`에서 사용하는 생성자는 제거하고 `Employee`를 받아서 추가하도록 변경한다.

```java
public class Company {
    private final List<Employee> employees = new ArrayList<>();

    public void joining(Employee employee) {
        employees.add(employee);
    }
}
```

```java
public class HeadHunter {
    private final List<Employee> clients = new ArrayList<>();

    public void addClient(Employee employee) {
        clients.add(employee);
    }
}
```

이를 통해 `Company`와 `HeadHunter`에는 `new` 키워드를 제거할 수 있다. `EmployeeFactory`의 효과로 `Employee`에서 생성에 변화가 생길때 팩토리 부분만 변경해주어 느슨한 객체 생성을 도와준다.
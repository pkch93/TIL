# java final

이전까지 `final` 키워드를 사용한 적이 드물다. 기껏해야 상수 선언으로 static과 함께 `final`을 사용한 기억이 전부라고 해도 무방할 정도로,,,



이 글을 적는 이유는 `final`의 사용처가 생각보다 많고 이점도 있다는 것을 느꼈기 때문이다.



## 사용법

java의 `final` 키워드는 클래스, 메서드, 클래스 변수, 인스턴스 변수, 지역 변수, 메서드 파리미터에 함께 쓰일 수 있다.



### 1. 클래스

```java
public final class Person {
    // body...
}
```

위와 같은 방식으로 class에 final 키워드를 사용할 수 있다.

클래스에 final을 적용하면 상속을 막는 효과를 가진다. 위 예시에서는 `Person`클래스를 다른 클래스에서 상속하지 못하게 된다.

class에 final을 붙이는 경우는 보통 Utils 클래스와 같이 상속을 통해 side-effect가 발생하는 경우 또는 상수를 모아둔 클래스처럼 굳이 상속이 필요없는 클래스에 final을 붙여 사용한다.

### 2. 메서드

메서드에 final 키워드를 적용하게 되면 override를 방지할 수 있다.

> 단, 메서드에 final 키워드를 사용하는 경우는 매우 드뭄,,

### 3. 클래스 변수

> static 키워드를 통해 클래스 변수를 선언할 수 있다.

클래스 변수에 final을 사용하는 경우는 주로 상수 선언 시에 사용한다.

```java
public static final String NAME = "pkch";
```

### 4. 인스턴스 변수

인스턴스 변수에는 원시 타입 (`int`, `double`, `boolean` 등)과 객체가 있을 수 있다.

원시 타입의 경우는 값을 변경할 수 없도록 final 키워드가 만들어준다.

```java
public class Person {
	private final int age;
    // ...
}
```

Person 클래스의 age는 final로 선언되어 있으므로 선언시에 할당이 되어야한다.

age에 바로 할당을 해주거나, 생성자를 통해 할당시킬 수 있다.

```java
// 1. 바로 할당
private final int age = 100;

// 2. 생성자
Person (final int age) {
	this.age = age;
}
```

※ 참고 : 바로 할당하여 초기화 하는 방법과 생성자 초기화의 차이

사실 큰 차이는 없다. 바로 할당하는 법은 생성자 초기화에 비해 더욱 간결한 코드를 가질 수 있도록 만들어 준다.

단, 즉시 할당하게 되면 해당 변수의 error 처리나 로직을 통한 할당이 불가능하다. 따라서, 해당 변수의 복잡한 처리가 필요한 경우에는 생성자를 통해 초기화 해주어야한다.

[Oracle java Tutorial 참고](https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html)

위 age는 `100`이라는 값이 할당된다. 이후에 다른 값을 할당하게 되면 오류를 내뿜게된다.

```java
age = 0; // error
```

반면, 객체를 대상으로도 final 키워드를 사용할 수 있다.

```java
private final School school;

Person (final School school) {
    this.school = school;
}
```

위와 같이 Person 클래스에 `School` 정보를 담는 school 인스턴스변수가 있다고 가정하자.

객체는 주소값을 가지게 된다.  객체의 final은 처음 할당된 주소값이 아닌 다른 주소값의 객체가 할당되는 경우 error를 뿜게된다.

단, 해당 객체의 내부 인스턴스는 접근하여 변경할 수 있다. (final 키워드가 없다면)

### 4. 지역변수

메서드 내부의 지역변수에서도 final 키워드를 활용할 수 있다.

주로 `lambda`를 활용할 때 지역변수에 final을 활용했던것 같다,,

### 5. 메서드 파라미터

메서드 파라미터에도 final 키워드를 할당할 수 있다.

```java
public void setName(final String name){
    // ...
    name = "hello"; // error
}
```

위 예시의 name(파라미터)에 final 키워드가 붙어있다. 이는 setName 메서드의 인자가 불변이라는 뜻이다. 따라서, setName 메서드 내부에서 name 값을 바꿀 경우(재할당) 에러나 발생한다.

[method parameter final 참고](https://stackoverflow.com/questions/2236599/final-keyword-in-method-parameters)

## 왜 사용해야할까?

final은 불변, 즉 클래스, 함수, 변수가 함부로 변하지 못하도록 만들고 싶을 때 사용한다.

[자바의 final은 언제 사용할까?](<https://blog.lulab.net/when-should-i-use-final-in-java/>)


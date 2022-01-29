# 2022.01.29 TIL - Decorator Pattern

- [2022.01.29 TIL - Decorator Pattern](#20220129-til---decorator-pattern)
  - [Java I/O Stream](#java-io-stream)
    - [실제 사용 예시](#실제-사용-예시)
  - [투명성](#투명성)
  - [Decorator Pattern에 대한 개인적인 생각](#decorator-pattern에-대한-개인적인-생각)

> 객체를 동적으로 서브 클래스를 이용하여 확장하는 패턴

서브 클래스를 만들어 동적으로 기능을 유연하게 확장할 수 있다는 점이 데코레이터 패턴의 장점이다.

반면, 서브 클래스가 많아질 수 있고 중첩된다면 그 정체를 알기 어려울 수 있다는 점이 단점이다.

## Java I/O Stream

데코레이터 패턴을 활용하는 대표적인 클래스가 바로 java.io의 Stream이다.

InputStream과 OutputStream은 추상클래스로써 각 추상클래스의 구현클래스들은 같은 추상클래스를 구현하고 있으며 생성자로 이를 받고 있다. 즉, InputStream의 구현은 생성자로 InputStream을 받고 OutputStream은 생성자로 OutputStream을 받는다.

장식자 패턴으로 파생된 객체는 요청된 행위를 중간에서 가로채 기능을 확장한다.

### 실제 사용 예시

다음과 같이 FileInputStream fis를 정의한다.

```java
FileInputStream fis = new FileInputStream("hello.txt");
```

> FileInputStream은 생성자로 입력된 경로의 파일을 읽는 기능의 InputStream이다.
> 

여기에 InputStream의 성능 향상을 원한다면 BufferedInputStream의 생성자에 fis를 전달한다.

```java
BufferedInputStream bis = new BufferedInputStream(fis);
```

> 참고로 BufferedInputStream은 버퍼만큼의 데이터를 미리 읽어 버퍼에 데이터를 넣어둔 뒤에 읽어서 디스크 호출을 줄이는 InputStream이다. 이를 통해 Input의 성능 향상 효과를 준다.
> 

위와 같이 fis에 BufferedInputStream를 감싸서 기존의 FileInputStream의 기능에 성능향상 효과를 줄 수 있다.

만약 여기에서 bis의 값을 역직렬화하고자 한다면 ObjectInputStream을 추가할 수 있다.

```java
ObjectInputStream ois = new ObjectInputStream(bis);
```

이런식으로 InputStream에 원하는 기능을 추가하고자 한다면 다른 기능의 InputStream을 래퍼로 감싸서 기능을 추가할 수 있다.

## 투명성

위와 같이 장식자 패턴도 각 클래스들이 객체의 투명성을 부여한다. 즉, 복합체 패턴처럼 일반화가 필요하다. 이렇게 투명성을 부여하는 이유로는 원본 객체에 영향을 주지 않고 새로운 책임을 줄 수 있기 때문이다. 장식자 패턴은 투명성을 활용하여 계속 객체를 감싸서 기능을 확장할 수 있다.

이렇게 객체를 감싸서 꾸미는 형식을 띄기 때문에 장식이라는 말로 불린다.

## Decorator Pattern에 대한 개인적인 생각

사실 장식자 패턴은 장점보다는 단점이 두드러지는 패턴이 아닐까 생각한다.

일반화된 클래스의 추가 기능을 주기 위해 서브 클래스를 두어 기능을 정의하는 것이 특징인데 계속 기능을 추가하기 위해 클래스를 덧붙이는 것은 가독성을 해치는 단점이 크게 두드러진다고 생각한다. 차라리 기존의 클래스에 기능을 확장하는 것이 단순하면서 합리적인 방식이 아닐까 싶다.

하지만 장식자 패턴이 효과적인 경우가 있는데 이는 기존에 존재하는 API를 유지해야하는 경우일 것이다. 앞서 소개한 Java의 I/O Stream도 이에 해당하는 경우라고 생각한다. 즉, 기존에 이미 사용하고 있는 API인 I/O Stream을 해치지 않으면서 기능을 확장하기 위한 방법으로 장식자 패턴을 활용한 것이 아닐까 싶다.
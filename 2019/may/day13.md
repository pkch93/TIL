# 2019 05.13 Monday

## java

1. String이 immutable인 이유

String을 `immutable`로 만들어주는 이유는 `caching`, `security`, `synchronization`, `performance`이다.

> immutable Object
>
> `immutable 객체`는 객체로 완전히 생성된 후 내부 상태 값의 변화없이 남아있는 객체를 의미한다. 이는 변수에 `immutable 객체`를 할당한 후에는 객체의 참조를 바꾸거나 내부 상태값을 변경할 수 없음을 의미한다.

* String pool

`String` 문자열을 캐싱하고 재사용하는 것은 heap 영역의 메모리를 효율적으로 사용할 수 있는 방안이다. 이는 다른 `String` 변수들도 `String pool`의 같은 객체를 참조하여 사용하므로 가능하다.

Java String pool은 JVM에서 관리하는 특별한 메모리 공간이다. JVM은 pool 내부의 String 문자열을 하나만 저장하여 메모리를 최적화한다.

```java
String s1 = "Hello World";
String s2 = "Hello World";
```

때문에 위 예시에서 s1과 s2는 String pool에서 같은 객체를 가리키므로 같다.

* Security

String은 Java에서 `username`, `password`, `connection URL` 등 민감한 정보를 담기도 한다. 때문에 String class의 보안을 고려하는 것은 애플리케이션 전체의 보안을 고려하는 것 만큼 중요한 사항이다.

이때 String 객체가 mutable하다면 해당 정보를 update할 때 입력받은 String이 안전하다는 보장이 없다. 심지어는 보안 검사를 했을때도 마찬가지이다. 신뢰할 수 없는 caller의 method가 여전히 String 값을 참조하고 있기 때문이다.

immutable하다면 해당 String 값이 연산 결과에 영향을 미칠 요소가 적어지므로 쉽게 복구가 가능하다.

* Synchronization

String이 immutable하다면 멀티스레드 환경에서 thread safe하게 다룰 수 있다.

> 일반적으로 immutable 객체는 멀티스레드 환경에서 동시에 값을 공유하여 사용한다. 값이 바뀔 이유가 없기 때문이다.

* Hashcode Caching

String은 자료구조로 충분히 사용될 수 있다. 실제로 `HashMap`, `HashTable`, `HashSet` 등의 hash 자료구조 구현체에서 널리 사용된다.

위와 같은 hash 자료구조 구현체에서 `bucketing`을 위해 hashcode 메서드가 자주 호출된다.

이때 String이 immutable하다는 것은 그 값이 변하지 않음을 보장하는 것이다. 따라서 hash가 계산되고 첫 hashcode\(\) 메서드가 호출되면 캐싱이 된다. \(문자열이 변하지 않기 때문\) 이후에 값은 문자열에 hashcode 메서드가 불려지면 같은 값이 return 된다. 이는 성능상으로도 이점이 된다.

* Performance

성능상에서 이점이 된다. String pool의 존재로 heap memory를 효율적으로 사용할 수 있으며 hash와 같은 자료구조에서도 성능 향상에 도움을 줄 수 있다.

[String이 immutable인 이유 - Baeldung](https://www.baeldung.com/java-string-immutable)

[String이 immutable이면 보안에 좋은 이유](https://stackoverflow.com/questions/15274874/how-does-java-string-being-immutable-increase-security)

1. String pool

String pool을 String 객체를 효율적으로 관리하기 위한 JVM의 메모리 공간이다.

String pool이 왜 효율적이냐면 String 문자열 단 하나만을 pool 내부에 저장하여 메모리에 할당하기 때문이다.

```java
String s1 = "Hello World";
String s2 = "Hello World";
```

위 예시에서 s1과 s2는 다른 변수지만 `Hello World`로 가지고 있는 문자열 그 자체는 같다. 이때 Java에서는 String pool에서 `Hello World` 단 하나만 메모리에 저장한 후 같은 문자열에 대해서는 같은 객체를 할당하므로 메모리를 절약할 수 있다.

위 과정을 `String interning`이라고 한다.

> 이때 객체를 할당한다는 것은 해당 객체의 주소값을 할당한다는 의미

1. new로 String 생성

```java
String str1 = new String("String");
String str2 = new String("String");
```

만약 `new`로 String을 생성한다면 어떻게 될까?

이경우는 heap 영역에 객체로써 저장된다. 때문에 위 `str1`과 `str2`는 다른 주소를 가리키게된다. \(String pool과는 다른 경우\)

때문에 new 연산자를 사용하게 되면 `String interning` 과정 없이 계속해서 heap 영역에 새로운 객체를 생성하게 된다.

이와 같은 이유로 String을 생성할 때는 new 보다는 String literal\(""로 String을 만드는 것\)로 만드는 것을 권장한다.

1. intern\(\)

String literal을 사용하면 자동으로 String interning이 되어 String pool 내부에 해당 값이 저장된다.

이와 별도로 intern\(\) 메서드를 호출하여 수동으로 interning 하는 방법도 있다.

```java
String str1 = new String("String");
String str2 = str1.intern();

assertThat(str1).isEqualTo(str2); // true
```

[String pool 알아보기](https://www.baeldung.com/java-string-pool)


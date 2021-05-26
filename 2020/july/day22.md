# Day22

## 2020.07.22 Wednesday - slf4j

#### Table of Contents

* [Java 진영의 Logging Framework](day22.md#Java_진영의_Logging_Framework)
  * [SLF4J](day22.md#SLF4J)
    * [SLF4J의 제공 기능](day22.md#SLF4J의_제공_기능)
    * [vs Jakarta Commons Logging](day22.md#vs_Jakarta_Commons_Logging)

## Java 진영의 Logging Framework

slf4j: [https://kwonnam.pe.kr/wiki/java/slf4j](https://kwonnam.pe.kr/wiki/java/slf4j) logback, log4j, log4j2 비교: [https://stackify.com/compare-java-logging-frameworks/](https://stackify.com/compare-java-logging-frameworks/)

자바 진영에는 다양한 로깅 프레임워크가 존재한다. 여기서 주로 사용하는 프레임워크는 logback, log4j, log4j2가 있다. 단, 이들을 알기 전에 먼저 slf4j를 먼저 알 필요가 있다.

### SLF4J

slf4j란 다양한 로깅 프레임워크를 퍼사드 형태로 제공하는 역할을 한다. 배포 시점에 원하는 로깅 프레임워크를 끼워 사용할 수 있도록 slf4j가 지원한다.

여기서 알아둬야 할 것은 slf4j가 지원하는 로깅 프레임워크는 `slf4j-api-2.0.0-alpha2-SNAPSHOT.jar` 의존성을 가진다.

`slf4j 2.0.0`부터는 java 8 이상을 지원하며 `backward-compatible fluent logging API`라고 소개하고 있다. 즉, 여러 로깅 라이브러리에 호환이 가능한 로깅 API라는 것이다.

> 참고로 spring boot 2.3.1에서는 slf4j 1.7.3 버전을 사용한다.

#### SLF4J의 제공 기능

* Fluent Logging API

  ```java
    int newT = 15;
    int oldT = 16;

    // using traditional API
    logger.debug("Temperature set to {}. Old temperature was {}.", newT, oldT);

    // using fluent API, add arguments one by one and then log message
    logger.atDebug().addArgument(newT).addArgument(oldT).log("Temperature set to {}. Old temperature was {}.");

    // using fluent API, log message with arguments
    logger.atDebug().log("Temperature set to {}. Old temperature was {}.", newT, oldT);

    // using fluent API, add one argument and then log message providing one more argument
    logger.atDebug().addArgument(newT).log("Temperature set to {}. Old temperature was {}.", oldT);

    // using fluent API, add one argument with a Supplier and then log message with one more argument.
    // Assume the method t16() returns 16.
    logger.atDebug().addArgument(() -> t16()).log(msg, "Temperature set to {}. Old temperature was {}.", oldT);
  ```

  `fluent logging API`를 사용하면 `org.slf4j.Logger`에 다양한 타입의 데이터를 표현할 수 있게된다.

* Binding with a logging framework at deployment time

  slf4j는 다양한 로깅 프레임워크를 지원한다.

  ![https://s3-us-west-2.amazonaws.com/secure.notion-static.com/f0597db2-c5d8-4743-98a2-84f8a39bb7ce/\_2020-07-22\_\_8.55.19.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/f0597db2-c5d8-4743-98a2-84f8a39bb7ce/_2020-07-22__8.55.19.png)

  > 특히 logback이 slf4j의 직접적인 구현체이기 때문에 slf4j와 logback 조합으로 보통 쓰이는 걸로 보인다.

slf4j에서는 원하는 로깅 라이브러리를 사용하기 위해서는 단순히 사용하고자 하는 로깅 라이브러리를 class path에 추가하기만 하면 된다.

#### vs Jakarta Commons Logging

JCL도 slf4j와 비슷한 역할을 하지만 클래스 로더를 알아야한다는 문제와 Memory Leak 문제로 slf4j를 더 선호.


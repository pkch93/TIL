# 2020.06.15 Wendesday - Java Thread

### Table of Contents

* [Java Thread](day15.md#Java_Thread)

## Java Thread

자바 Thread는 Green Thread로써 JVM에서 관리한다. 즉, CPU의 Native Thread와는 다른 개념이다. 따라서 컴퓨터가 가진 스레드보다 더 많은 스레드를 가질 수 있다. 참고: [https://stackoverflow.com/questions/5713142/green-threads-vs-non-green-threads](https://stackoverflow.com/questions/5713142/green-threads-vs-non-green-threads)

단, Java 1.3부터 자바의 Thread는 Native Thread를 사용한다. 이는 CPU의 발전 때문이다. Green Thread는 싱글코어일 때 그 효과를 발휘한다. 싱글코어의 활용을 극대화하기 위해서 Green Thread로 JVM에서 관리하기 위함인데 멀티코어, 쿼드코어, 옥타코어 등 CPU의 발전으로 Green Thread로는 멀티 프로세싱의 이점을 살릴 수가 없는 것이다. 따라서 자바는 1.3부터 Native Thread를 사용하기 시작한다.

### Java Thread와 Native Thread

참고: [https://medium.com/@unmeshvjoshi/how-java-thread-maps-to-os-thread-e280a9fb2e06](https://medium.com/@unmeshvjoshi/how-java-thread-maps-to-os-thread-e280a9fb2e06) 참고2: [https://medium.com/@leeyh0216/distributed-system-processes-1-9101d5c5ceee](https://medium.com/@leeyh0216/distributed-system-processes-1-9101d5c5ceee)

Thread의 `start()`를 호출하는 순간 native 메서드로 정의된 `start0`을 호출된다. 이때 native가 붙은 메서드는 Java Native Interface `JNI`를 통해 호출되어야 한다는 것을 의미한다. Java Native Interface는 JVM과 통합하기 위한 Native Code 명세이다.


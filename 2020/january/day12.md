# 2020 01.12 Sunday - classicist vs mockist

### Table of Contents

[classicist vs mockist](day12.md#classicist_vs_mockist)

## classicist vs mockist

참고: [https://medium.com/@adrianbooth/test-driven-development-wars-detroit-vs-london-classicist-vs-mockist-9956c78ae95f](https://medium.com/@adrianbooth/test-driven-development-wars-detroit-vs-london-classicist-vs-mockist-9956c78ae95f)

classicist의 대표격으로는 켄트백, 로버트 c 마틴이 있고 mockist의 대표격은 Sandi Metz, J.B Rainsberger, Steve Freeman이 있다.

classicist는 TDD를 상태 검증, Inside-out, black-box 테스트 방식으로 이야기한다. 반면 mockist는 TDD를 행위 검증, Outside-in, white-box 테스트 방식으로 이야기한다.

* classicist

classicist의 TDD는 단위테스트에서 시작한다. 그리고 이들의 설계는 테스트에서부터 스멀스멀 등장한다. 단, 테스트에서 좋은 설계로 이끌기 위해서는 설계에 대한 학습이 선행되면 더 좋다.

classcist의 핵심 키워드는 **Mock 사용을 피하는 것**이다. classist들은 mock의 사용이 애플리케이션이 동작하는 상태를 전혀 확인할 수 없다고 주장한다. 예를 들어 CalculatorService라고 일컫는 클래스의 메서드를 리펙토링하는 경우 그 동작은 똑같이 동작할 것이다. 즉, 이전과 같은 방식으로 같은 인자를 넣으면 같은 결과가 정상적으로 나타나겠지만 동작이 달라졌으므로 테스트는 실패하는 것이다.

classicist들은 상태 기반으로 테스트를 바라본다.

* mockist

반면 mockist는 밖에서 안으로 검증을 한다고 했다.

mockist는 단위 테스트는 고립되어 동작해야하므로 다른 호출자들 사이에서 하나의 단일 객체가 테스트를 실패로 이끄는 것을 원하지 않는다.

mockist는 행위 중심으로 테스트를 바라본다. 따라서 객체 사이의 상호작용과 정확하게 메세지가 전송하는지를 중요시본다. 따라서 메서드를 호출하고 반환 값을 테스트할 다른 객체와 상호작용하므로 메서드의 정확성에는 큰 관심이 없다. 즉, 객체 간 상호작용에 더 큰 관심을 갖는 것이다.

### 내 생각

둘 다 어느정도 일리가 있는 테스트 설계 방법이다. 프로덕션 코드가 여러 군대의 테스트 코드에 의존하는 것도 좋지는 않으며 이는 이미 검사한 내용을 중복해서 검사하는 의미와 같다.

내가 내린 결론은 비즈니스 로직에 해당하는 도메인 영역은 classicist의 의견과 같이 상태 기반으로 테스트해야한다고 생각한다. 도메인 영역은 데이터 값이 매우 중요하므로 해당 데이터가 로직에 따라 잘 수행하는지 확인해야한다고 생각한다. 그 외에도 자신의 상태 값을 바꾸거나 계산과 같은 로직에서는 행위가 아닌 값을 꼭 확인해야 할 것이다.

반면, 이들 객체들을 모아서 로직을 수행하는 객체도 있다. 이미 단위테스트로 검증한 로직을 그대로 사용하여 전달하는 것이라면 굳이 classicist의 방식으로 검증할 필요는 없다고 생각한다. 오히려 검증을 위한 의존성 준비과정이 매우 크게 느껴질 수 있다.

위와 같은 예시는 바로 Spring 프레임워크에서 자주 사용되는 Service 객체가 되겠다. Service 객체는 보통 Presentation 영역 \(Controller 등\)에서 전달 받은 사용자 입력값을 토대로 요청을 수행해준다. 이때 주로 로직은 도메인을 통해 수행한다. 따라서, Service의 메서드는 각 도메인 객체들이 잘 상호작용했는지 수행여부 등을 판단하는 것이 Service 객체에 알맞는 테스트라고 생각한다.

Service 같은 Facade 역할을 하는 객체들에게는 Mock을 사용해도 나쁘지 않다는 생각이다. 오히려 의존 문제를 간소화하여 객체 간의 테스트를 보다 유연하게 하도록 도움을 준다는 것이 내 생각이다. 다만 Facade 역할이 아닌 상태 값을 다룬다면 Mocking을 하지 않는게 맞는 것 같다.


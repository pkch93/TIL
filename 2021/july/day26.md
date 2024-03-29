# 2021.07.26 TIL - Spring Bean vs static

Spring bean은 Spring에서 관리하는 자바 객체를 의미한다. 이를 통해 bean이 bean을 의존할 수 있고 의존받을 수 있다.

다만, 다른 빈을 의존할 필요가 없을때 `static` 메서드를 정의한 클래스를 두고 이를 사용하는 경우도 종종 있다. 어떤 방식이 옳을까?

## bean과 static의 차이

bean을 사용하면 다음과 같은 이점이 있다.

1. 빈을 의존 받을 수 있고 다른 빈에서 의존을 할 수 있다.

    다른 빈에서 구현한 기능을 재활용해서 사용할 수 있다. 그 반대도 가능하다.

2. 타입 계층을 구성할 수 있다.

    빈은 인터페이스를 구현할 수 있고 클래스 확장(상속) 이 가능하다.

    이를 통해 타입계층을 구성하여 보다 더 객체지향적인 설계가 가능하다.

반면 static 메서드를 사용하면 어디서든 메서드 호출이 가능하다.

다만, static 메서드는 다음과 같은 단점이 있다.

1. static 메서드는 어디서든 호출이 가능하다.

    방금 언급했던 장점아다. 즉, 장점이 될 수도 단점이 될 수도 있다는 의미이다.

    보통 Spring을 사용하는 웹 애플리케이션을 기준으로 보면 3-tier `Controller - Service - Infrastructure` 애플리케이션으로 구성한다.

    즉, 각 레이어를 나눠서 레이어 별 객체들을 사용하게 되는데 static 메서드를 무분별하게 사용하면 레이어 별 구분을 무시한채로 static 메서드를 구성할 여지가 많다.

    따라서 개인적인 기준으로는 전 레이어에서 활용될 수 있는 유틸성 메서드만 static 메서드로 활용을 해야한다고 생각한다.

    > 버전 확인, 숫자 비교 등

2. 객체 의존을 받을 수 없다.

    Spring 환경에서 객체 의존을 받을 수 없다는 의미는 다른 Spring bean이 구현한 기능을 static 메서드에서는 사용할 수 없다는 의미이다.

    다른 bean이 구현하고 있는 기능을 static 메서드에서 필요로 한다면 static 메서드로 따로 한번 더 구현을 해야한다.

3. 상속을 받을 수 없다.

    물론 static 메서드의 클래스에 상속을 할수는 있지만 상위 클래스 / 인터페이스의 메서드를 활용할 수는 없다. 즉, 의미 없는 상속이다.

## 결론

bean과 static 메서드를 사용을 비교할때는 static 메서드의 단점이 많다고 생각한다.

현재는 의존받는 빈이 없어서 static을 사용하더라도 추후에 빈 의존을 받으려고 한다면 결국 Spring IoC 컨테이너에 Bean으로 등록해야만한다.

따라서 유틸성 기능 이외에는 bean으로 등록하여 사용하는 것이 더 낫다고 생각한다.
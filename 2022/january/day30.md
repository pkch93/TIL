# 2022.01.30 TIL - Facade Pattern

위키에서 퍼사드 패턴은 다음과 같이 정의하고 있다.

> 클래스 라이브러리 같은 어떤 소프트웨어의 다른 커다란 코드 부분에 대한 간략화된 인터페이스를 제공하는 객체이다.

퍼사드 패턴을 사용함으로써 의존하고 있는 여러 의존성을 하나의 인터페이스 `퍼사드 클래스`로 완화할 수 있다.

![퍼사드 예시](https://user-images.githubusercontent.com/30178507/151698921-579cf512-186c-4cc6-83fe-f02670a89875.png)

위 그림과 같이 Facade 객체가 없다면 Client 클래스들에서 Class1, Class2, Class3을 직접 의존하여 기능을 구현하게 된다. 즉, Class2, Class3의 변경은 이들 클래스의 변경점이 될 수 있음을 시사한다.

이를 보완하기 위해 Facade 객체를 두어 Client에서는 Facade 객체가 제공하는 인터페이스만을 사용하도록 한다. (public 메서드 등)

이렇게 되면 Class1, Class2, Class3의 변경은 Facade에만 영향을 미치므로 유지보수 측면에도 유리하다.

위 예시와 같이 새로운 계층이 생겨 관리해야할 클래스가 하나 더 늘어난다는 단점이 있지만 강력한 객체 간의 결합도를 낮추고 유연한 구조를 가지도록 만들 수 있다.

> 만약 Spring MVC를 사용한다면 서비스 `@Service` 객체가 퍼사드에 해당한다.
>
> 컨트롤러 `@Controller`에서 직접 사용하는 객체들을 서비스 객체라는 퍼사드를 두어 코드의 재사용성을 높이고 느슨한 결합으로 만든다.
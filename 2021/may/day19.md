# 2021.05.19 TIL - Mixins

믹스인이란 코드 일부를 클래스 안에 섞어 넣어 재사용하는 기법을 의미한다.

## OOP에서의 Mixins

OOP에서 믹스인이란 다른 클래스의 부모 클래스가 되지는 않으면서 다른 클래스에서 사용할 수 있는 메서드를 포함하는 클래스를 의미한다.

상속과 믹스인은 비슷해보인다. 다만, 상속이 클래스를 작성하는 단계에서 관계를 고정시키는 반면 믹스인은 적용할 수 있는 범위에 제약을 둘 뿐 실제 어떤 코드에 믹스인 되는지는 실행 시점에 가서야 알 수 있다. 이점이 상속과 믹스인의 큰 차이이다.

믹스인은 상속 계층 안에서 확장한 클래스보다 더 하위에 위치한다. 다시 말해서 믹스인은 대상 클래스의 자식 클래스처럼 사용될 용도로 만들어지는 것이다. 따라서 믹스인은 추상 서브클래스라고 부르기도 한다. 믹스인은 결론적으로는 슈퍼클래스로부터 상속될 클래스를 명시하는 메커니즘을 표현한다. 따라서 하나의 믹스인은 매우 다양한 클래스를 도출하면서 서로 다른 서브클래스를 이용해 인스턴스화 할 수 있다.

### Ruby와 Mixin

Ruby에서는 `include`, `prepend`, `extend` 키워드를 통해 믹스인을 지원한다. 이때 모듈 `Module` 타입만 믹스인에 사용가능하다.

```ruby
module Test
  def hello
    return "hello"
  end
end

class Hello
  include Test
end

puts Hello.new.kind_of? Test # true
```

> 참고로 Ruby의 모듈은 Java 8 이상의 인터페이스와 거의 동일한 기능을 한다.

Hello 클래스에서는 Test 모듈을 `include` 했기 때문에 Test에서 정의한 hello 메서드를 사용할 수 있다.

### Java와 Mixin

Ruby의 모듈처럼 동일한 역할을 하는 요소가 Java에도 있다. 바로 인터페이스이다. 인터페이스도 상속처럼 `is-a` 관계로 고정시키지 않으며 인터페이스에 정의된 메서드를 그대로 사용할 수 있다. `default 메서드`

> Java 8 이후부터 인터페이스에 구현을 추가할 수 있도록 default 메서드 기능이 추가되었다.

이 default 메서드를 활용하여 Java에서도 믹스인을 활용할 수 있다.

```java
interface Test {
    default String hello() {
        return "hello";
    }
}

public class Hello implements Test {
}
```

## Mixin의 장점

믹스인은 기능 재사용에 중점을 맞춘 패턴이다. 애플리케이션이 객체 전반적으로 공유된 동작을 해야하는 경우에 믹스인을 통해 공유된 기능을 유지하여 중복을 방지할 수 있다.

## Mixin의 단점

믹스인에는 다음 3가지 이슈가 있다.

1. 암묵적인 의존

   믹스인의 특정 메서드에 의존하는 경우 암시적 의존성으로 인해 코드를 작성하기 힘들 수 있다. Javascript와 같은 특정 언어에서는 믹스인에 정의된 메서드를 파악하기 위해서 모든 믹스인을 열어봐야할 수도 있다. 믹스인은 종종 다른 믹스인에 의존할 때도 있는데 이 경우 의존성 그래프가 어떻게 이뤄져있는지 알기 매우 어렵다.

2. name clash

   이름 충돌이 간간히 발생할 수 있다. 미리 정의된 믹스인 2개에 `hello` 라는 메서드가 정의되어 있는 경우 믹스인 이름이 충돌하므로 이를 해결해야한다. 믹스인의 메서드 이름을 변경하는 방법도 있지만 만약 특정 믹스인의 메서드가 널리 쓰이고 있다면 쉽지 않은 선택이 될 것이다.

3. 복잡도 증가

   믹스인은 단순하게 시작했더라도 시간에 따라 복잡해지는 경향이 있다.

### 참고

오브젝트 11장. 합성과 유연한 설계: [http://www.yes24.com/Product/Goods/74219491](http://www.yes24.com/Product/Goods/74219491)

믹스인이란: [https://ko.wikipedia.org/wiki/믹스인](https://ko.wikipedia.org/wiki/%EB%AF%B9%EC%8A%A4%EC%9D%B8)

루비 mixin: include, prepend, extend 그리고 Concern: [https://spilist.github.io/2019/01/17/ruby-mixin-concern](https://spilist.github.io/2019/01/17/ruby-mixin-concern)

객체지향에서 믹스인의 문제점: [https://raganwald.com/2016/07/16/why-are-mixins-considered-harmful.html](https://raganwald.com/2016/07/16/why-are-mixins-considered-harmful.html)

믹스인 문제점 해결 - 믹스인에서 조합으로 전환: [https://raganwald.com/2016/07/20/prefer-composition-to-inheritance.html](https://raganwald.com/2016/07/20/prefer-composition-to-inheritance.html)

React에서 믹스인을 안티패턴으로 인식하는 이유: [https://reactjs.org/blog/2016/07/13/mixins-considered-harmful.html](https://reactjs.org/blog/2016/07/13/mixins-considered-harmful.html)


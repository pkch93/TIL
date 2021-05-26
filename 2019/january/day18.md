# 2019 01.19 Saturday

* Spring에서 `@Autowired`를 생략하고 주입 받는 방법

`@Autowired`를 활용하면 Spring IOC에 들어있는 빈을 주입 받아 사용할 수 있다.

단, 해당 클래스에서 특정 빈을 반드시 주입받아 사용해야한다면 필드 `@Autowired`보다는 생성자 주입이 좋다. 이는 Spring 4.3 버전 이상에서 지원한다.

반드시 해당 빈을 주입해야한다면 특정 빈을 받는 변수를 final로 선언한 후 생성자로 주입 받는 방법이다. \(단, 생성자가 하나 있어야함\)

위 방식은 빈 생성시 반드시 필요한 객체를 안전하게 받을 수 있다는 점에서 추천된다. \(Spring 팀의 권고 사항\)

단, 주의할 점은 생성자로 주입 받는 빈이 상호참조 하는 경우는 문제가 될 수 있으므로 이 경우는 필드에서 `@Autowired`해주어야 한다.

```java
// PostService
public class PostService{

    public final PostRepository postRepository;

    public PostService(PostRepository postRepository){
        this.postRepository = postRepository;
    }
    // ...
}
```

위와 같은 방식으로 `@Autowired` 없이도 주입을 받을 수 있다.


# 2021.09.05 TIL - Spring WebSocket with STOMP (Annotated Controllers)

## Annotated Controllers

Spring WebSocket에서도 Spring MVC에서처럼 `@Controller`를 통해 클라이언트로부터 전송된 메세지를 처리할 수 있도록 지원한다. 다만 Spring WebSocket에서는 `@MessageMapping`, `@SubscribeMapping`, `@MessageExceptionHandler`를 사용해야한다.

- @MessageMapping

    Spring WebSocket에서 메세지를 라우팅할 때 사용할 수 있는 어노테이션이다. Spring MVC의 `@RequestMapping`과 유사하다. `@RequestMapping`과 같이 type level에 매핑도 가능하다. 타입에 매핑하는 경우 해당 타입 하위의 모든 메서드에 적용된다.

    Ant 스타일로 경로를 지정할 수 있으며 `/thing/{id}`와 같이 템플릿 형태로 경로 지정이 가능하다. 템플릿에 지정한 변수는 `@DestinationVariable`인자로 받아올 수 있다.

    Spring WebSocket에서 인자로 받을 수 있도록 지원하는 MethodArgumentResolver는 다음과 같다.

    - Message
    - MessageHeaders
    - MessageHeaderAccessor
    - SimpMessageHeaderAccessor
    - StompHeaderAccessor
    - @Payload
    - @Header
    - @Headers
    - @DestinationVariable
    - Principal

    기본적으로 `@MessageMapping`의 반환값은 MessageConverter를 통해 직렬화되며 BrokerChannel로 직렬화한 메세지를 전송한다. Message 또한 Spring MVC처럼 ListenableFuture, CompletableFuture, CompletionStage와 같은 비동기객체들에 대한 반환도 지원한다.

    이때 반환값을 보내는 지점을 커스터마이징할 수 있는데 이를 위해서 `@SendTo`와 `@SendToUser`를 사용할 수 있다. `@SendTo`는 반환값을 보내는 지점이나 여러 반환 지점을 지정하기 위해서 사용하며 `@SendToUser`는 요청받은 메세지와 관련된 유저에게 메세지를 전송하기 위해서 사용한다.

    `@SendTo`와 `@SendToUser`는 같은 메서드에 매핑이 가능하며 type level 매핑도 지원한다. 다만 유의해야할 점은 method level의 `@SendTo`와 `@SendToUser`는 type level의 어노테이션을 덮어쓴다.

    ```java
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface SendTo {

    	String[] value() default {};

    }
    ```

    ```java
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface SendToUser {

    	@AliasFor("destinations")
    	String[] value() default {};

    	@AliasFor("value")
    	String[] destinations() default {};

    	boolean broadcast() default true;

    }
    ```

    Spring STOMP에서는 `/user` prefix를 가진 반환 지점에 대해서 특정 유저에게 메세지를 전송하는 엔드포인트로 인식한다. `/user/*`에 대해서는 UserDestinationMessageHandler가 메세지를 처리하고 유저 세션에 맞춰 반환 지점을 변환한다. 만약 `/user/queue/position-updates`로 요청이 온다면 UserDestinationMessageHandler가 요청온 유저의 세션에 따라서 `/queue/position-updates-user1`과 같이 변환한다.

- @SubscribeMapping

    subscription 요청을 처리하기위한 어노테이션이다. `@MessageMapping`에서 지원하는 ArgumentResolver와 동일하게 지원하지만 반환값은 기본적으로 brokerChannel에 보내지 않고 clientOutboundChannel을 통해 직접 클라이언트에 보낸다. 

- @MessageExceptionHandler

    `@MessageMapping`에서 발생한 Exception을 처리하는 핸들러를 구현할 때 사용한다.

## WebSocket Scope

각 웹소켓 세션에는 여러 attribute를 가지고 있다. 이 attribute 값들은 요청 header에 함께 전송이 되는데 이를 Controller의 메서드에서 접근이 가능하다. `ex. SimpMessageHeaderAccessor`

`SimpMessageHeaderAccessor`와 같은 값들은 `websocket` scope을 가지는 빈으로 스프링에서 관리한다. 이런 `websocket` scope의 빈은 직접 정의할 때도 직접 지정이 가능하다.

```java
@Component
@Scope(scopeName = "websocket", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MyBean {

    @PostConstruct
    public void init() {
        // Invoked after dependencies injected
    }

    // ...

    @PreDestroy
    public void destroy() {
        // Invoked when the WebSocket session ends
    }
}
```

위와 같이 `websocket`으로 scopeName을 지정하면 컨트롤러의 핸들러 메서드나 Channel Interceptor에서 주입받아 사용이 가능하다. 위와 같은 `websocket` scope의 빈들은 컨트롤러에 접근했을때 생성되며 WebSocket 세션의 attribute로 값을 저장한다. 이 인스턴스는 세션이 종료되었을때 함께 삭제된다.

## 참고

[Spring WebSocket with STOMP](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#websocket-stomp)

[Messaging Stomp WebSocket](https://spring.io/guides/gs/messaging-stomp-websocket/)

[SendToUser Annotation Baeldung](https://www.baeldung.com/spring-websockets-sendtouser)
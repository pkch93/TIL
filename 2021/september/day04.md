# 2021.09.04 TIL - Spring WebSocket with STOMP (broker)

WebSocket은 text와 binary 두가지 타입으로 정의할 수 있도록 규약되어 있지만 content에 대해서는 별도 규약이 없다. 따라서 WebSocket은 sub-protocol을 정의하여 전송하는 메세지의 포멧 등을 알려주는데 활용할 수 있다.

STOMP는 WebSocket을 사용할 때 sub-protocol로 주로 사용되는 프로토콜이다.

Spring WebScoket에서 STOMP를 사용하도록 설정하면 메세지브로커를 활용한 pub-sub 매커니즘을 활성화하게된다. Spring MVC에서 사용하는 것처럼 `@Controller`를 통해 메세지 라우팅을 하거나 애플리케이션 내에 in-memory STOMP 메세지 브로커를 두어 메세지 처리를 할 수 있다. 물론 STOMP 메세지 브로커로 RabbitMQ나 ActiveMQ 등을 활용할 수 있다.

Spring은 메세지 브로커에 대한 TCP 연결을 유지하고 연결된 WebSocket 클라이언트에 메세지를 전달하는 역할을 한다.f

## Spring WebSocket with STOMP 사용하기

Spring WebSocket 환경에서 STOMP를 사용하기 위해서는 `@EnableWebSocketMessageBroker` 설정이 필요하다.

```java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig {
}
```

위와 같이 `@Configuration`에 `@EnableWebSocketMessageBroker` 설정이 필요하다.

```java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends WebSocketMessageBrokerConfigurer {

	@Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
      registry.addEndpoint("/test").setHandshakeHandler(handshakeHandler());
  }

  @Bean
  public DefaultHandshakeHandler handshakeHandler() {

      WebSocketPolicy policy = new WebSocketPolicy(WebSocketBehavior.SERVER);
      policy.setInputBufferSize(8192);
      policy.setIdleTimeout(600000);

      return new DefaultHandshakeHandler(
              new JettyRequestUpgradeStrategy(new WebSocketServerFactory(policy)));
  }
}
```

WebSocketMessageBrokerConfigurer를 확장하여 WebSocket MessageBroker에 대한 상세설정이 가능하다. DefaultHandshakeHandler 빈을 구현하여 웹소켓 핸드쉐이킹 로직에 대해 정의할 수 있으며 registerStompEndpoints를 오버라이드하여 STOMP 엔드포인트를 노출할 수 있다.

이렇게 STOMP 엔드포인트를 노출한 순간부터 Spring 애플리케이션은 STOMP 브로커로써 역할을 한다.

> `spring-websocket`은 `spring-messaging` 모듈과 함께 사용하여 STOMP 메세지를 처리하는 메세지브로커 애플리케이션을 만들 수 있도록 지원한다.

`org.springframework.boot:spring-boot-starter-websocket` 스타터 의존성에는 `spring-websocket`과 `spring-messaging` 의존성이 포함되어있다.

다음은 Spring에서 제공하는 Message 관련 추상 `abstraction` 들이다.

- Message

    헤더와 페이로드를 포함한 메세지의 추상

- MessageHandler

    메세지 처리를 담당하는 추상

- MessageChannel

    메세지를 보내는 것을 담당하는 추상. 이를 통해 producer와 consumer 사이의 결합도를 줄여줄 수 있다.

- SubscribableChannel

    MessageHandler를 구독하는 MessageChannel

- ExecutorSubscribableChannel

    메세지를 전달하기 위해 Executor를 사용하는 SubscribableChannel

### STOMP 메세지의 흐름

아래 다이어그램은 메세지브로커를 사용했을때 메세지를 어떻게 처리하는지 보여준다.

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/dfb8b67c-277a-4bfd-98a7-a011b2ce33ec/Untitled.png)

위 다이어그램에서는 다음과 같은 MessageChannel을 사용하고 있다.

- clientInboundChannel

    웹소켓 클라이언트에게서 메세지를 받는 Channel

- clientOutboundChannel

    웹소켓 클라이언트에게 메세지를 보내는 Channel

- BrokerChannel

    서버에서 메세지 브로커로 메세지를 보내는 Channel

위와 같이 내부에 in-memory 메세지 브로커 사용도 가능하지만 RabbitMQ 같은 외부 메세지브로커를 활용할 수 있다. 만약 외부 브로커를 사용한다면 다음과 같은 형태가 될 것이다.

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/c37f5b31-3142-4c2b-a343-1013a5237343/Untitled.png)

외부 브로커를 사용한다면 기존에 SimpleBroker를 사용한 것에 반해 StompBrokerRelay를 활용해야한다. 

WebSocket 클라이언트로부터 메세지를 받았을 때 이를 STOMP 메세지로 디코딩을 하고 clientInBoundChannel로 보낸다. 위 그림에서 `/app`으로 요청이 오는 경우 컨트롤러를 통해 처리되거나 `/topic`으로 요청이 오는 경우 직접 MessageBroker로 전송하도록 만들 수 있다.

### Example

```java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/portfolio");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic");
    }
}
```

위와 같이 WebSocketMessageBrokerConfigurer를 설정했다고 가정한다. WebSocket 클라이언트는 `/portfolio` 엔드포인트로 연결을 시도할 수 있다.

커넥션이 맺어진 이후에는 `/app`이나 `/topic` 엔드포인트로 STOMP 메세지를 보낼 수 있다. `/app`으로 요청이 온다면 `setApplicationDestinationPrefixes` 설정으로 인해 Annotated Controller 설정으로 처리가 되는 반면 `/topic`으로 요청이 온다면 `enableSimpleBroker` 설정으로 인해 Broker로 직접 메세지가 전송된다.

이렇게 전송되는 메세지는 각 엔드포인트 핸들러에서 처리가 이뤄진 뒤에 clientOutboundChannel로 옮겨져 클라이언트로 전송된다.

## Broker

Spring WebSocket에서는 인메모리 메세지브로커와 외부 메세지브로커 사용을 지원한다.

### Simple Broker

SimpleBroker는 `WebSocketMessageBrokerConfigurer#configureMessageBroker`에서 `MessageBrokerRegistry#enableSimpleBroker`로 활성화할 수 있다.

```java
@Override
public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry.enableSimpleBroker("/topic");
}
```

위와 같이 `/topic` 엔드포인트로 요청이 오면 SimpleBrokerMessageHandler를 통해 STOMP 메세지가 처리된다.

SimpleBrokerMessageHandler는 clientInboundChannel, clientOutboundChannel, brokerChannel을 빈으로 받는다. spring-websocket 스타터에서는 AbstractMessageBrokerConfiguration에서 ExecutorSubscribableChannel의 `clientInboundChannel`과 `clientOutboundChannel`, `brokerChannel` 빈을 만들어준다.

> Executor는 기본적으로 현재 CPU의 2배의 스레드를 가지며 maxPoolSize, QueueCapacity는 `Integer.MAX_VALUE`를 가진다. keepAliveSeconds는 60초, alllowCoreThreadTimeOut을 true로 설정한다.

만약 clientInboundChannel, clientOutboundChannel 빈들에 커스텀이 필요하다면 `WebSocketMessageBrokerConfigurer#configureClientInboundChannel`과 커스텀이 필요하다면 `WebSocketMessageBrokerConfigurer#configureClientOutboundChannel` 오버라이드가 필요하다.

`configureClientInboundChannel`과 `configureClientOutboundChannel`에서는 각 MessageChannel에서 사용할 Executor와 Interceptors `ChannelInterceptor`를 추가할 수 있다.

```java
public interface ChannelInterceptor {

	@Nullable
	default Message<?> preSend(Message<?> message, MessageChannel channel) {
		return message;
	}

	default void postSend(Message<?> message, MessageChannel channel, boolean sent) {
	}

	default void afterSendCompletion(
			Message<?> message, MessageChannel channel, boolean sent, @Nullable Exception ex) {
	}

	default boolean preReceive(MessageChannel channel) {
		return true;
	}

	@Nullable
	default Message<?> postReceive(Message<?> message, MessageChannel channel) {
		return message;
	}

	default void afterReceiveCompletion(@Nullable Message<?> message, MessageChannel channel,
			@Nullable Exception ex) {
	}

}
```

기본적으로 STOMP 커넥션의 라이프사이클에 따라서는 관련 ApplicationEvent를 생성하지만 클라이언트의 메세지에 대해서는 생성하지 않는다. 이를 위해 ChannelInterceptor를 등록하여 메세지의 라이프사이클에 따라 원하는 로직을 제공할 수 있다.

### External Broker

Simple Broker는 바로 시작하기 좋은 도구이지만 STOMP 이외에 다른 것들은 지원하지 않는다. 오직 메세징 전송에만 사용이 가능하다.

따라서 Spring WebSocket에서는 Simple Broker의 대안으로 External Broker를 지원한다. 이는 RabbitMQ, ActiveMQ 등과 같은 메세지 브로커를 사용하는 방법이다. External Broker를 사용하기 위해 Spring WebSocket은 StompBrokerRelay를 활용한다.

```java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/portfolio").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableStompBrokerRelay("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
    }

}
```

위와 같이 `MessageBrokerRegistry#enableStompBrokerRelay`를 통해 활성화할 수 있다. StompBrokerRelay는 외부 메세지브로커로 메세지를 전송하고 웹소켓 클라이언트에 메세지를 전달하는 역할을 한다.

> 참고로 StompBrokerRelay를 통해 외부 메세지 브로커와 TCP 연결을 관리하기 위해서는 `io.projectreactor.netty:reactor-netty`와 `io.netty:netty-all` 의존성이 필요하다.

StompBrokerRelay는 Broker에 하나의 "system" TCP 커넥션을 맺고 유지한다. 이때 STOMP credentials로 STOMP 프레임에 login과 passcode 헤더를 포함하도록 설정할 수 있다. 기본적으로 login은 guest, passcode도 guest로 설정되어있으며 systemLogin과 systemPasscode로 값을 설정할 수 있다.

또한 StompBrokerRelay는 연결을 맺은 모든 웹소켓 클라이언트와 커넥션을 맺을 수 있다. 웹소켓 클라이언트와도 credentials를 설정할 수 있다. system 커넥션과 동일하게 login과 passcode의 기본값은 guest이며 clientLogin과 clientPasscode로 값을 설정할 수 있다.

StompBrokerRelay의 system TCP 커넥션은 메세지 브로커와 heartbeats를 주기적으로 주고받는다. 기본으로 10초 간격으로 주고받으며 만약 브로커와 연결이 끊어지는 경우 5초마다 연결이 다시 맺어질때까지 재연결을 시도한다. 재연결 시도시에 기본적으로 동일한 호스트와 포트에 연결을 시도한다. 만약 여러 호스트에 대해 연결을 시도하도록 설정하고 싶다면 다음과 같이 TcpClient를 생성하여 설정할 수 있다.

```java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    // ...

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableStompBrokerRelay("/queue/", "/topic/").setTcpClient(createTcpClient());
        registry.setApplicationDestinationPrefixes("/app");
    }

    private ReactorNettyTcpClient<byte[]> createTcpClient() {
        return new ReactorNettyTcpClient<>(
                client -> client.addressSupplier(() -> ... ), // 여기에 시도하고자하는 호스트를 제공할 수 있다.
                new StompReactorNettyCodec());
    }
}
```

## 참고

[Spring WebSocket with STOMP](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#websocket-stomp)

[Messaging Stomp WebSocket](https://spring.io/guides/gs/messaging-stomp-websocket/)
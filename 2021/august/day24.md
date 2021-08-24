# 2021.08.24 TIL - Spring WebSocket

- [2021.08.24 TIL - Spring WebSocket](#20210824-til---spring-websocket)
  - [Spring WebSocket API](#spring-websocket-api)
    - [@EnableWebSocket](#enablewebsocket)
    - [WebSocketHandler](#websockethandler)
    - [WebSocket Handshake](#websocket-handshake)
    - [Advanced](#advanced)

WebSocket 프로토콜은 하나의 TCP 커넥션으로 client-server간 양방향 통신을 지원한다. 이는 일반적인 HTTP로 사용하는 TCP 프로토콜과는 다르지만 WebSocket은 HTTP/HTTPS와 호환이 되도록 설계되었다.

WebSocket은 기본적으로 HTTP의 `upgrade` 헤더를 사용한다. 이를 통해 HTTP는 WebSocket 프로토콜로 전환한다.

```
GET /spring-websocket-portfolio/portfolio HTTP/1.1
Host: localhost:8080
Upgrade: websocket 
Connection: Upgrade 
Sec-WebSocket-Key: Uc9l9TMkWGbHFD2qnFHltg==
Sec-WebSocket-Protocol: v10.stomp, v11.stomp
Sec-WebSocket-Version: 13
Origin: http://localhost:8080
```

위 예시와 같이 WebSocket Request는 Upgrade 헤더를 `websocket`으로 Connection 헤더를 `Upgrade`로 사용한다.

반면 HTTP 프로토콜과는 다르게 다음과 같이 응답을 준다.

```
HTTP/1.1 101 Switching Protocols 
Upgrade: websocket
Connection: Upgrade
Sec-WebSocket-Accept: 1qVdfYHU9hPOl4JYYNXF623Gzn0=
Sec-WebSocket-Protocol: v10.stomp
```

TCP handshake 이후에 TCP 소켓은 HTTP upgrade를 한다. 그 후 request는 client-server간에 메세지를 주고 계속해서 주고 받을 수 있도록 남아있는다.

이처럼 WebSocket 서버가 웹 서버를 기반으로 동작한다면 WebSocket 서버는 Upgrade 요청을 보내도록 구성해야한다. 만약 클라우드 환경이라면 클라우드 제공자가 WebSocket을 지원하는지도 확인해야한다.

## Spring WebSocket API

> Spring Boot 2.3.4를 기준으로 작성

Spring Boot에서 WebSocket을 구현하기 위해서는 다음 스타터의존성이 필요하다.

```groovy
implementation 'org.springframework.boot:spring-boot-starter-websocket'
```

참고로 WebSocket은 `'org.springframework.boot:spring-boot-starter-web'`에 포항되어있지 않다. 이와 같이 Spring WebSocket은 Spring MVC에 의존적이지 않다. 

### @EnableWebSocket

```groovy
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new ChatMessageHandler(), "/chat");
    }
}
```

Spring WebSocket을 사용하기 위해서는 `@Configuration` 설정 클래스 안에서 `@EnableWebSocket`을 사용해야한다.

WebSocket Handler를 스프링에 등록하기 위해서는 `WebSocketConfigurer#registerWebSocketHandlers`를 사용한다. 인자로 받는 WebSocketHandlerRegistry의 addHandler로 등록할 수 있다.

### WebSocketHandler

WebSocket 서버를 생성하기 위해서는 **WebSocketHandler**이나 ****이를 구현한 TextWebSocketHandler나 BinaryWebSocketHandler를 확장해야한다.

WebSocketHandler를 직간접적으로 사용할 때 애플리케이션은 반드시 메세징에 대한 동기화를 해주어야한다. 이는 표준 WebSocket session이 concurrent sending을 지원하지 않기 때문이다.

이를 구현하기 위해서는 WebSocketSession을 ConcurrentWebSocketSessionDecorator로 감싸는 것이다.

```groovy
public class ChatMessageHandler extends TextWebSocketHandler {

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
    }
}
```

### WebSocket Handshake

request시 WebSocket handshake를 맺을 때 커스터마이징을 위한 가장 쉬운 방법으로 Spring Websocket에서는 `HandshakeInterceptor`를 제공한다.

```java
public interface HandshakeInterceptor {

	boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
			WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception;

	void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
			WebSocketHandler wsHandler, @Nullable Exception exception);

}
```

HandshakeInterceptor는 handshake 이전에 상황을 처리할 beforeHandshake와 이후의 상황을 처리할 afterHandshake를 가지고 있다.

beforeHandshake는 boolean을 반환하며 만약 false를 반환하는 경우 해당 요청을 abort된다. 대표적으로 `HttpSessionHandshakeInterceptor`를 지원한다.

### Advanced

`HandshakeInterceptor`이외에 다른 옵션으로 `DefaultHandshakeHandler`를 확장하는 것이다. 이를 통해 client origin의 검증 단계, sub-protocol negotiating와 같은 WebSocket handshake 의 단계를 구현할 수 있다.

특히, WebSocket에서 만약 커스텀한 ReuqestUpgradeStrategy를 필요로 한다면 `DefaultHandshakeHandler`를 확장해야한다.

```java
public class DefaultHandshakeHandler extends AbstractHandshakeHandler implements ServletContextAware {

	public DefaultHandshakeHandler() {
	}

	public DefaultHandshakeHandler(RequestUpgradeStrategy requestUpgradeStrategy) {
		super(requestUpgradeStrategy);
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		RequestUpgradeStrategy strategy = getRequestUpgradeStrategy();
		if (strategy instanceof ServletContextAware) {
			((ServletContextAware) strategy).setServletContext(servletContext);
		}
	}

}
```

# 2021.09.12 TIL - SockJS

SockJS는 애플리케이션이 WebSocket API를 사용하도록 지원하면서 애플리케이션의 코드를 변경할 필요 없이 런타임에 WebSocket API를 사용하지 않는 대안으로 전환하도록 지원한다.

Spring에서는 `spring-websocket` 모듈에 SockJS 서버 구현을 지원하며 spring 4.1 이상부터 SockJS 클라이언트를 지원한다.

기본적으로 SockJS는 브라우저를 대상으로 설계가 되어있다. 다양한 버전의 브라우저를 지원하고 있으며 [다음 링크](https://github.com/sockjs/sockjs-client/#supported-transports-by-browser-html-served-from-http-or-https)에서 확인할 수 있다. 참고로 SockJS의 transport 방식에는 WebSocket, HTTP Streaming, HTTP Long Polling이 있다.

> transport란 SockJS를 통한 서버 대상의 요청을 의미

SockJS 클라이언트는 서버에 대한 정보를 얻기 위해 `/info` 요청을 보내는 것으로 시작한다. 그 후 transport에 사용할 방식을 결정한다. 웹소켓 사용이 가능하다면 웹소켓을 사용하고 그렇지 않은 경우 대부분의 브라우저에서는 HTTP Streaming, 이마저도 안되면 HTTP Long Polling을 사용한다.

SockJS의 transport 요청은 다음과 같은 형태를 띈다.

```
https://host:port/myApp/myEndpoint/{server-id}/{session-id}/{transport}
```

Spring 웹소켓 서버를 SockJS 요청을 받을 수 있도록 만들려면 다음과 같이 설정이 필요하다.

```java
@Override
public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/ws").withSockJS();
}
```

위와 같이 endpoint 설정시에 `withSockJS` 메서드를 호출하면 해당 엔드포인트로 들어오는 요청은 SockJS에 의해 처리된다.

위와 같이 SockJS로 엔드포인트를 열면 `/info` 엔드포인트가 자동으로 생성된다.

```bash
$ curl http://localhost:8080/ws/info
{"entropy":205202405,"origins":["*:*"],"cookie_needed":true,"websocket":true}%
```

# 참고

Spring WebSocket SockJS: [https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#websocket](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#websocket)

sockjs-client: [https://github.com/sockjs/sockjs-client](https://github.com/sockjs/sockjs-client)

sockjs-protocol: [https://sockjs.github.io/sockjs-protocol/sockjs-protocol-0.3.3.html](https://sockjs.github.io/sockjs-protocol/sockjs-protocol-0.3.3.html)

websocket with android: [https://stackoverflow.com/questions/31817135/connect-with-ios-and-android-clients-to-sockjs-backend](https://stackoverflow.com/questions/31817135/connect-with-ios-and-android-clients-to-sockjs-backend)
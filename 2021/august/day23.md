# 2021.08.23 TIL - WebSocket

- [2021.08.23 TIL - WebSocket](#20210823-til---websocket)
  - [웹소켓 통신](#웹소켓-통신)
    - [웹소켓 핸드쉐이크](#웹소켓-핸드쉐이크)
    - [웹소켓 HeartBeat](#웹소켓-heartbeat)
    - [웹소켓 커넥션 종료 `Closing the Connection`](#웹소켓-커넥션-종료-closing-the-connection)
  - [참고](#참고)

웹소켓 프로토콜은 클라이언트와 서버 간 양방향 소통을 가능하게 만들어준다. 보안 모델로는 웹 브라우저에서 활용하는 origin 기반의 보안 모델을 사용한다.

여러 HTTP 커넥션을 열지 않더라도 서버와 클라이언트간 양방향 소통을 할 수 있도록 하는 것이 주된 목적이다.

웹소켓은 HTTP를 사용하여 클라이언트-서버간 양방향 소통을 하는 기술을 대체하기 위해 설계되었다. 따라서 HTTP를 사용하는 환경에서 80 `http`, 443 `https` 포트로 동작하고 HTTP 프록시를 지원하도록 설계되었다. 더 나아가서는 HTTP로 제한하지 않고 전용 포트를 통해 더 간단한 핸드셰이크를 사용할 수 있도록 확장가능하다.

## 웹소켓 통신

### 웹소켓 핸드쉐이크

클라이언트 측에서 서버와 웹소켓 연결을 맺기 위해서는 `CONNECTING` 상태의 커넥션을 열고 handshake 요청을 보내야한다. 클라이언트는 host, port, resource name, secure 플래그, protocol, extension, 웹소켓 URI와 같은 정보를 전달한다. 만약 클라이언트가 웹 브라우저라면 origin도 추가로 전달한다.

프록시를 거쳐서 웹소켓 서버에 연결을 해야하는 경우라면 host와 port 헤더를 추가하여 전달해야한다.

만약 스마트폰의 브라우저와 같이 통제된 환경에서 실행되는 클라이언트는 네트워크의 다른 에이전트에 대한 네트워크 커넥션 관리 부담을 내려놓을 수 있다.

> 네트워크 에이전트란 네트워크의 패킷과 프로토콜을 확인하여 프로토콜을 관리하는 주체이다.

정리하면 다음과 같은 조건을 만족해야 웹소켓 연결 요청이 가능하다.

1. HTTP 프로토콜에 맞춰서 요청을 해야한다.
2. HTTP 메서드 중 GET 메서드만 허용되며 HTTP 1.1 이상이어야한다.
3. host 헤더를 반드시 추가해야하고 필요에 따라 `:` 로 구분하여 포트를 설정할 수 있다.
4. Upgrade 헤더에 반드시 websocket으로 설정하여 요청해야한다.
5. Connection 헤더에 반드시 Upgrade로 설정하여 요청해야한다.
6. Sec-WebSocket-Key 헤더를 포함해서 요청해야한다. Sec-WebSocket-Key 헤더는 base64 인코딩을 한 16바이트의 임의의 값으로 구성되어야한다.

    참고로 Sec-WebSocket-Key는 상대방을 인증하는데 사용이 된다. Sec-WebSocket-Key를 받은 서버는 이 값을 토대로 Sec-WebSocket-Accept 헤더 값을 만들어 응답한다.

7. 브라우저에서 웹소켓 요청을 하는 경우라면 Origin 헤더를 추가해야한다.
8. Sec-WebSocket-Version 헤더를 포함하여 요청해야한다. 값은 반드시 13으로 세팅한다.
9. Sec-WebSocket-Protocol 헤더를 포함하여 요청할 수 있다. 콤마로 구분하여 클라이언트가 소통하고자하는 여러 프로토콜을 설정할 수 있다.

    정의할 수 있는 서브 프로토콜은 [IANA 명세](https://www.iana.org/assignments/websocket/websocket.xml#subprotocol-name)를 참고

10. Sec-WebSocket-Extensions 헤더를 포함하여 요청할 수 있다.

    정의할 수 있는 Extensions는 [IANA 명세](https://www.iana.org/assignments/websocket/websocket.xml#extension-name)를 참고

이렇게 클라이언트가 Open Handshaking 요청을 보내고나면 클라이언트는 반드시 서버에서 응답을 받기 전까지 기다려야한다. 연결이 완료되면 서버에서는 101 status code로 핸드쉐이크에 응답한다.

### 웹소켓 HeartBeat

웹소켓 핸드쉐이크가 끝난 시점부터 서버와 클라이언트는 언제든지 ping 패킷을 보낼 수 있다. ping 패킷을 수신했을 때 pong으로 최대한 빨리 응답해야한다. 이렇게 ping-pong 방식으로 주기적으로 클라이언트가 아직 연결되어있는 상태인지 확인할 수 있다.

### 웹소켓 커넥션 종료 `Closing the Connection`

서버나 클라이언트 측에서 커넥션 종료를 원한다면 커넥션 종료 핸드쉐이크를 위한 control frame을 상대측에 전달한다. 이 control frame을 받은 즉시 close frame을 응답한다. 이렇게 되면 연결했던 커넥션이 끊어지며 이후에 커넥션 종료 이후에 전달하는 데이터는 무시된다.

## 참고

rfc6455 명세: [https://datatracker.ietf.org/doc/html/rfc6455](https://datatracker.ietf.org/doc/html/rfc6455)
웹소켓 서버 작성하기: [https://developer.mozilla.org/ko/docs/Web/API/WebSockets_API/Writing_WebSocket_servers](https://developer.mozilla.org/ko/docs/Web/API/WebSockets_API/Writing_WebSocket_servers)
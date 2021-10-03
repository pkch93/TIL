# 2021.10.01 TIL - Mockito ArgumentCaptor

- [2021.10.01 TIL - Mockito ArgumentCaptor](#20211001-til---mockito-argumentcaptor)
  - [ArgumentCaptor 사용해보기](#argumentcaptor-사용해보기)
  - [참고](#참고)

종종 Stub한 메서드의 인자가 어떤 값이 들어왔는지 확인해야 할 필요가 있을 수 있다.
이를 위해서 Mockito에서는 ArgumentCaptor를 통해 어떤 인자가 들어왔는지 확인할 수 있도록 지원한다.

ArgumentCaptor를 사용해보기 전에 다음과 같은 코드를 작성한다.

```java
@Getter
public class Request {
    private Status status;
    private String name;

    private Request(Status status, String name) {
        this.status = status;
        this.name = name;
    }

    public static Request ofActive(String name) {
        return new Request(Status.ACTIVE, name);
    }

    public static Request ofWithdraw(String name) {
        return new Request(Status.WITHDRAW, name);
    }

    public enum Status {
        ACTIVE, WITHDRAW
    }
}

public class RequestClient {

    public void request(Request request) {}
}

public class RequestService {
    private final RequestClient requestClient;

    public RequestService(RequestClient requestClient) {
        this.requestClient = requestClient;
    }

    public void requestAllStatus(String name) {
        requestClient.request(Request.ofActive(name));
        requestClient.request(Request.ofWithdraw(name));
    }

    public void requestActive(String name) {
        requestClient.request(Request.ofActive(name));
    }

    public void requestWithdraw(String name) {
        requestClient.request(Request.ofWithdraw(name));
    }
}
```

Request 요청 객체와 함께 Request를 인자로 받아 요청을 보내도록 가정하는 RequestClient, 그리고 RequestClient를 사용하여 상황에 따라 요청을 보내는 RequestService가 있다.

여기서 RequestService는 모든 상태 `ACTIVE, WITHDRAW`에 대해 요청을 보낼수도 있고 `requestAllStatus` 각각에 상태 `requestActive / requestWithdraw`에 대해 요청을 보낼 수 있다.

참고로 RequestClient는 Mocking 용으로 사용하기 때문에 따로 로직은 구현하지 않았다.

## ArgumentCaptor 사용해보기

```java
@ExtendWith(MockitoExtension.class)
class ArgumentCaptorEduTest {

    @InjectMocks
    private RequestService requestService;

    @Mock
    private RequestClient requestClient;

    @Test
    void argumentCaptor() {
        // given
        willDoNothing().given(requestClient).request(any(Request.class));

        // when
        requestService.requestActive("pkch");

        // then
        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);
        then(requestClient).should().request(requestCaptor.capture());

        Request actualRequest = requestCaptor.getValue();
        assertThat(actualRequest.getStatus()).isEqualTo(Request.Status.ACTIVE);
    }
}
```

요청에 대한 검증시 `Mockito.verify, BDDMockito.then` 메서드의 인자에 `ArgumentCaptor.capture`를 해주면 로직상에서 인자로 들어간 값을 가져와서 검증할 수 있다. 값을 가져올때는`ArgumentCaptor.getValue`를 사용하여 가져올 수 있다.

```java
@Test
void argumentCaptor_multipleValueCapture() {
    // given
    willDoNothing().given(requestClient).request(any(Request.class));

    // when
    requestService.requestAllStatus("pkch");

    // then
    ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);
    then(requestClient).should(times(2)).request(requestCaptor.capture());

    assertThat(requestCaptor.getAllValues()).hasSize(2)
            .extracting(Request::getStatus)
            .containsExactly(Request.Status.ACTIVE, Request.Status.WITHDRAW);
}
```

ArgumentCaptor는 여러번의 호출에 대해서도 인자를 가져올 수 있다. 마찬가지로 `ArgumentCaptor.capture`로 검증하고자 하는 인자에 사용하고 `ArgumentCaptor.getAllValues`로 호출된 순서대로 인자 값을 검증할 수 있다.

참고로 여러번 호출했을때 `ArgumentCaptor.getValue`를 호출하는 경우 가장 마지막에 호출된 인자의 값을 반환한다.

```java
@Test
void argumentCaptor_multipleValueCapture_getValue() {
    // given
    willDoNothing().given(requestClient).request(any(Request.class));

    // when
    requestService.requestAllStatus("pkch");

    // then
    ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);
    then(requestClient).should(times(2)).request(requestCaptor.capture());

    Request request = requestCaptor.getValue();
    assertThat(request.getStatus()).isEqualTo(Request.Status.WITHDRAW);
}
```

## 참고

baeldung docs: [https://www.baeldung.com/mockito-argumentcaptor](https://www.baeldung.com/mockito-argumentcaptor)
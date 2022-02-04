# 2021.11.17 TIL - Internet Protocol
- [2021.11.17 TIL - Internet Protocol](#20211117-til---internet-protocol)
  - [Internet Protocol의 역할](#internet-protocol의-역할)
  - [Internet Protocol의 한계](#internet-protocol의-한계)
    - [비연결성](#비연결성)
    - [비신뢰성](#비신뢰성)
  - [Internet Protocol의 특징](#internet-protocol의-특징)
    - [IP Header](#ip-header)
    - [MTU](#mtu)
    - [IP fragmentation](#ip-fragmentation)
  - [참고](#참고)

IP `Internet Protocol`이란 OSI 7 레이어의 4번째 레이어인 네트워크 계층에 속하는 프로토콜이다.

![OSI 7 Layers](https://user-images.githubusercontent.com/30178507/142201329-1d198e50-f491-4d4c-abe0-9a27fa55eb8f.png)

과거에 네트워크 계층에는 여러 프로토콜이 존재했지만 현재는 Internet Protocol이 유일하다고 할 수 있다.

네트워크 계층은 패킷을 IP 데이터그램에 포함한 후 데이터그램 헤더 및 트레일러에 넣고 데이터그램 전송 위치를 결정한 후 네트워크 인터페이스 계층으로 데이터그램을 전달한다.

## Internet Protocol의 역할

네트워크 계층의 인터넷 프로토콜은 패킷을 IP 데이터그램에 포함한 후 데이터그램 헤더 및 트레일러에 넣고 데이터그램 전송 위치를 결정한 후 네트워크 인터페이스 계층으로 데이터그램을 전달한다. 이를 통해 인터넷 프로토콜은 다음과 같은 역할을 한다.

1. 지정된 IP 주소에 데이터를 전달
2. 패킷이라는 통신 단위로 데이터를 전달

## Internet Protocol의 한계

Internet Protocol에는 **비연결성과 비신뢰성** 특징을 가지고 있다.

### 비연결성

비연결성이란 데이터 전송 이전에 네트워크 통신을 위한 연결 과정을 거치지 않는 것이다. 즉, 패킷을 받을 대상이 없거나 서비스 불능 상태라도 패킷을 전송한다.

### 비신뢰성

Internet Protocol은 전송한 데이터가 정확하게 전달했는지 보장하지 못한다. 패킷이 소실되어 전달한 내용이 누락되거나 내용이 뒤바껴서 전송될 수 있다.

네트워크 대역폭이 좁아서 `hello world` 메세지를 나눠서 전달해야하는 경우 Internet Protocol은 순서를 보장하지 않기 때문에 메세지를 받는 쪽에서는 `world hello` 와 같은 메세지를 받을수도 있다. 이 때문에 신뢰성을 보완하는 프로토콜인 TCP와 함께 많이 사용된다.

## Internet Protocol의 특징

### IP Header

Internet Protocol의 payload 앞에 붙는 IP header는 다음과 같이 생겼다.

![IP header)](https://user-images.githubusercontent.com/30178507/142201323-d872579c-1033-46ab-8b03-38e72a8fff42.png)

출처: [https://ddongwon.tistory.com/89](https://ddongwon.tistory.com/89)

기본적으로 IP header는 **패킷을 어디로 보내야하는지에 대한 정보**를 담고 있다. 즉, IP header에는 패킷 처리에 필요한 정보들이 담겨있다. 기본적으로 IP header의 크기는 20 bytes이다.

- ver: IP 프로토콜의 버전 정보
- header len: 헤더의 길이
- type of service: 네트워크 계층에서 제공해주는 서비스에 필요한 정보
- identifier, flags, offset: IP fragmentation에 필요한 정보
- time to live: 데이터의 유효기간. 즉, IP 데이터그램이 인터넷 시스템에서 존재할 수 있는 시간을 의미한다.
- upper layer: payload를 상위 계층의 누가 처리할지를 담은 정보

### MTU

MTU `Maximum Transmission Unit` 은 네트워크 계층에서 보낼 수 있는 payload의 최대 크기이다. payload의 크기이므로 IP header의 크기는 제외하고 계산한다.

### IP fragmentation

큰 IP 패킷보다 작은 MTU를 갖는 링크를 통해 전송하기 위해서는 MTU에 맞게 여러 조각으로 패킷을 나누어 전송해야한다. 이렇게 IP 패킷을 여러 조각으로 나누는 것을 IP fragmentation 이라고 한다.

> 링크란 네트워크 노드 사이에 패킷을 전달하기 위한 물리경로를 의미한다.

IPv4의 경우 최초 발신지 뿐만 아니라 중간 라우터에서도 IP fragmentation이 발생하는 반면 IPv6는 최초 발신지에서만 IP fragmentation이 발생한다. 재조립은 최종목적지에 도달할 때까지 이뤄지지 않으며 최종목적지에서만 단편화된 패킷을 재조립한다.

## 참고

[https://www.ibm.com/docs/ko/aix/7.1?topic=protocol-tcpip-protocols](https://www.ibm.com/docs/ko/aix/7.1?topic=protocol-tcpip-protocols)

[https://ddongwon.tistory.com/89](https://ddongwon.tistory.com/89)

IP time to live: [https://ko.wikipedia.org/wiki/Time_to_live](https://ko.wikipedia.org/wiki/Time_to_live)

IP 단편화: [http://www.ktword.co.kr/test/view/view.php?m_temp1=5236](http://www.ktword.co.kr/test/view/view.php?m_temp1=5236)
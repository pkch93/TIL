# 2021.08.07 TIL - Amazon Route 53 추가 정리

> [Route 53 정리](../july/day20.md)에 이어 추가 보충 정리 입니다.

## 리소스의 상태 확인

![](https://user-images.githubusercontent.com/30178507/129717727-4291b4d0-4b64-4582-9818-a7ce44d44004.png)

1. 상태 확인을 생성하고 원하는 상태 확인 작동 방식을 정의하는 값을 지정하는 방법은 다음과 같다.

   - Route 53 이 모니터링하도록 하려는 엔드포인트의 IP 주소나 도메인 이름.

       다른 상태 확인의 상태 또는 CloudWatch 경보의 상태도 모니터링할 수 있다.

   - Amazon Route 53이 확인을 수행하는 데 사용할 프로토콜은 HTTP, HTTPS 또는 TCP이다.
   - 요청 간격은 Route 53 이 엔드포인트에 요청을 전송하는 빈도이다.
   - 장애 임계치는 Route 53 이 비정상으로 판단하게 되는, 엔드포인트의 요청 응답 연속 실패 횟수이다.
   - Route 53에 알람을 설정하면 자동으로 CloudWatch 알람이 세팅된다. CloudWatch는 Amazon SNS를 사용하여 Route 53이 호스팅하는 애플리케이션이 unhealthy할 때 알람을 보낸다.

2. Route 53 은 사용자가 상태 확인에서 지정한 시간 간격으로 엔드포인트에 요청을 전송하기 시작한다.

    엔드포인트가 요청에 응답하면 Route 53은 엔드포인트가 정상이라고 판단하고 아무런 조치를 취하지 않는다.

3. 엔드포인트가 요청에 응답하지 않으면 Route 53 은 엔드포인트가 응답하지 않는 연속적 요청 횟수를 세기 시작한다. 횟수가 장애 임계치로 지정된 값에 도달하면 Route 53 은 엔드포인트가 비정상이라고 판단한다. 횟수가 장애 임계치에 도달하기 전에 엔드포인트가 다시 응답하기 시작하면 Route 53은 횟수를 0으로 리셋하고 CloudWatch는 사용자에게 알람을 보내지 않는다.

4. Route 53 이 엔드포인트가 비정상이라고 판단하고 사용자가 상태 확인 알림을 구성한 경우, Route 53은 CloudWatch에 알린다.

5. 상태 확인 알림을 구성한 경우, CloudWatch 는 경보를 트리거하고 Amazon SNS 사용하여 지정된 수신자에게 알림을 전송한다.

Route 53에서는 health check를 통해 failover 구성을 지원한다. active-active failover와 active-passive failover를 구성할 수 있다.

## active-active failover

active-active failover는 동일한 이름, 동일한 유형 및 동일한 라우팅 정책을 보유한 모든 레코드는 Route 53에서 비정상으로 판단하지 않는 경우 활성 상태로 판단하여 처리한다.

참고: [https://brunch.co.kr/@topasvga/89](https://brunch.co.kr/@topasvga/89)

## active-passive failover

기본 리소스 그룹은 트래픽을 받도록 사용하고 리소스가 사용불가능할 때를 대비하여 대기중인 리소스를 둘때 사용할 수 있는 failover 구성이다.

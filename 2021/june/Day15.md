# 2021.06.15 TIL - VPC 피어링

- [2021.06.15 TIL - VPC 피어링](#20210615-til---vpc-피어링)
  - [VPC 피어링](#vpc-피어링)
    - [VPC 피어링 과정](#vpc-피어링-과정)
    - [참고](#참고)

## VPC 피어링

VPC 피어링 연결은 비공개적으로 두 VPC 간에 트래픽을 라우팅할 수 있도록 하기 위한 두 VPC 사이의 네트워킹 연결이다. 

상용화 서비스와 상용화전 서비스를 구분하기 위해 별도의 VPC를 생성해야 하는 경우도 있고, 부서별 또는 프로젝트 별로 서로 다른 VPC를 관리해야 하는 경우도 있다. 여러 개의 계정이 있는 경우 각기 다른 VPC를 생성하면 되는데 이때 각종 서버 간의 소통이나 리소스 공유가 필요할때 VPC 피어링을 할 수 있다.

피어링된 양 쪽 VPC의 인스턴스는 동일한 네트워크에만 있다면 서로 소통이 가능하다. 다른 리전 간 피어링도 가능하다.

![](https://user-images.githubusercontent.com/30178507/122072005-2e92ee80-ce32-11eb-83ef-9d46cb6fbdb4.png)

AWS는 VPC의 기존 인프라를 사용하여 VPC 피어링 연결을 생성한다. 이는 게이트웨이도, VPN 연결도 아니며 물리적 하드웨어 각각에 의존하지 않으므로 통신 또는 대역폭 병목에 대한 단일 지점 장애가 발생하지 않는다.

### VPC 피어링 과정

VPC 피어링 연결을 설정하려면 다음 작업을 수행한다.

1. 요청자 **VPC의 소유자가 수락자 **VPC의 소유자에게 VPC 피어링 연결을 생성하도록 요청을 보낸다. 수락자 VPC는 사용자 또는 다른 AWS 계정에서 소유할 수 있으며, 요청자 VPC의 CIDR 블록과 중첩되는 CIDR 블록은 사용할 수 없다.

2. 수락자 VPC의 소유자가 VPC 피어링 연결 요청을 수락하여 VPC 피어링 연결을 활성화한다.

3. 프라이빗 IP 주소를 사용하여 VPC 간의 트래픽 흐름을 활성화하려면 VPC 피어링 연결 내 각 VPC의 소유자가 다른 VPC `피어 VPC`의 IP 주소 범위를 가리키는 경로를 하나 이상의 VPC 라우팅 테이블에 수동으로 추가해야 한다.

4. 필요한 경우 피어 VPC에서 주고 받는 트래픽이 제한되지 않도록 인스턴스와 연결되어 있는 보안 그룹을 업데이트한다. 두 VPC가 동일한 리전에 있는 경우 보안 그룹 규칙의 수신 또는 송신 규칙에 대한 소스나 대상으로 피어 VPC의 보안 그룹을 참조할 수 있다.

5. 기본적으로, VPC 피어링 연결의 어느 한 쪽에 위치하는 인스턴스가 퍼블릭 DNS 호스트 이름을 사용하여 상대방을 참조하는 경우 호스트 이름은 인스턴스의 퍼블릭 IP 주소로 확인된다. 이 동작을 변경하려면 VPC 연결에 대해 DNS 호스트 이름 확인을 활성화한다. DNS 호스트 이름 확인을 활성화한 후, VPC 피어링 연결의 어느 한 쪽에 위치하는 인스턴스가 퍼블릭 DNS 호스트 이름을 사용하여 상대방을 참조하는 경우 호스트 이름은 인스턴스의 프라이빗 IP 주소로 확인된다.

### 참고

VPC 피어링: [https://docs.aws.amazon.com/ko_kr/vpc/latest/userguide/vpc-peering.html](https://docs.aws.amazon.com/ko_kr/vpc/latest/userguide/vpc-peering.html)

VPC 피어링 가이드: [https://docs.aws.amazon.com/ko_kr/vpc/latest/peering/what-is-vpc-peering.html](https://docs.aws.amazon.com/ko_kr/vpc/latest/peering/what-is-vpc-peering.html)
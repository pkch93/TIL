# 2021.06.10 TIL - Amazon VPC 기본

- [2021.06.10 TIL - Amazon VPC 기본](#20210610-til---amazon-vpc-기본)
  - [VPC](#vpc)

## VPC

Amazon VPC는 EC2의 네트워크 레이어이다. VPC는 다음과 같은 컨셉을 따른다.

- Virtual Private Cloud

    AWS 계정 내에서 클라우드를 퍼블릭과 프라이빗으로 클라우드를 분리할 수 있다. 어떠한 리소스라도 논리적으로 분리된 영역에 격리가 가능하며 네트워크에 대한 완전한 통제권을 가질 수 있다.

- Subnet

    VPC는 IP 주소 범위로 체계로 관리한다. VPC 내에서 다수의 서브넷 설정도 가능하다. 인터넷 접근을 위한 퍼블릭 서브넷과 격리된 접근을 위한 프라이빗 서브넷을 생성할 수 있다.

- Route Table

    VPC를 통해 네트워크 트래픽의 방향을 결정하는데 사용할 수 있다.

- Internet gateway

    VPC내의 리소스들과 외부 인터넷과의 커뮤니케이션을 할 수 있도록 역할을 할 수 있다. 

- VPC endpoint

    S3와 같은 AWS 서비스와 VPC endpoint를 연결하여 프라이빗하게 리소스를 다룰 수 있도록 지원한다.

- CIDR block

    Amazon VPC는 Private IP Address 사용을 추천하고 Private Ip Address를 할당하기 위해 CIDR를 활용한다.

    > CIDR란 클래스 없는 도메인 간 라우팅 기법으로 IP 주소 할당 방법이다.
    참고: [https://dev.classmethod.jp/articles/vpc-3/](https://dev.classmethod.jp/articles/vpc-3/)
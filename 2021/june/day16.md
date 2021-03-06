# 2021.06.16 TIL - DHCP

## DHCP란?

DHCP는 동적으로 IPv4 주소를 일정 기간 임대하는 프로토콜이다. DHCP는 UDP 프로토콜을 사용하며 67, 68 포트번호를 사용한다. IPv4 주소를 할당할 때 수동으로 지정할 수도 있고, DHCP를 통해 자동으로 지정할 수도 있다. IP주소를 임대하는 개념으로 임대 시간이 존재하며 임대 시간이 만료되면 반환하거나 갱신을 수행한다.

## DHCP 옵션 세트가 필요한 이유

DHCP 옵션 세트는 기본 도메인 네임, DNS 서버 등 VPC에 있는 인스턴스의 호스트 환경 설정에 사용된다.

각 VPC는 반드시 하나의 DHCP 옵션 세트를 지녀야 하며, 일단 생성된 DHCP 옵션 세트는 수정할 수 없다. 수정이 필요하다면 새로운 DHCP 옵션 세트를 생성 후 연결해야한다.

### 참고

[DHCP란?](https://jwprogramming.tistory.com/35)
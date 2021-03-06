# 2021.06.20 TIL - EC2 인스턴스의 주요 특징

- [2021.06.20 TIL - EC2 인스턴스의 주요 특징](#20210620-til---ec2-인스턴스의-주요-특징)
  - [프로세서 특징](#프로세서-특징)
  - [네트워크 특징](#네트워크-특징)
    - [배치 그룹](#배치-그룹)
  - [스토리지 특징](#스토리지-특징)

## 프로세서 특징

EC2 인스턴스는 전통적으로 인텔 `x86` 프로세서를 사용했다. 최근에는 AWS Graviton2를 통해 ARM 프로세서를 지원하기도 한다.

> 클라우드 환경에서는 ARM이 비용 효율성이 높다고 한다. 특히 기본 운영체제에 가까운 가벼운 런타임일수록 비용 효율성이 높다.

ARM vs x86 in cloud: [https://www.infoq.com/articles/arm-vs-x86-cloud-performance/](https://www.infoq.com/articles/arm-vs-x86-cloud-performance/)

EC2 인스턴스에서의 인텔 프로세서의 특징은 다음과 같다.

- 인텔 AES 신규 기능 `AES-NI`

    AES 알고리즘에 적용된 신규 암호화 기능을 지원한다. 모든 현 세대 인스턴스에서 이 기능을 지원한다.

- 인텔 고급 벡터 확장 기능 `AVX`

    인텔 AVX는 이미지 및 오디오/비디오 프로세싱 애플리케이션의 성능을 높여준다. HVM AMI 인스턴스에서 제공한다.

- 인텔 터보 부스트 기술

    인텔 터보 부스트 기술을 통해 트래픽 급증에 대응하는 등 필요시 즉각적으로 성능을 높일 수 있도록 해준다.

AWS Graviton 프로세서 참고: [https://aws.amazon.com/ko/ec2/graviton/](https://aws.amazon.com/ko/ec2/graviton/)

## 네트워크 특징

EC2-Classic `Legacy`은 Amazon EC2의 오리지널 배포판으로 다른 사용자와 공유하는 단일 계층 네트워크에서 인스턴스 실행이 가능하다. 단, EC2-Classic에서는 최신의 인스턴스 타입은 사용할 수 없으며, 이들 인스턴스는 Amazon VPC에서만 론칭 가능하다.

EC2 인스턴스는 Amazon VPC를 통해 다양한 네트워크 기능을 제공한다.
VPC 참고: [https://www.notion.so/3-VPC-8f407431b20d41288d233b22b985f2b2](https://www.notion.so/3-VPC-8f407431b20d41288d233b22b985f2b2)

EC2에서는 대역폭을 최대화하고 더 높은 네트워크 성능을 위해서 배치 그룹 `Placement Group`에서 인스턴스를 띄울 수 있다. 배치 그룹은 단일 AZ 내에서 인스턴스를 논리적인 그룹으로 묶는 방법으로 낮은 지연과 높은 수준의 네트워크 처리성능을 제공한다.

### 배치 그룹

EC2 배치 그룹으로는 클러스터, 파티션, 분산 3가지 전략을 제공하며 각각 다음과 같은 워크로드 유형에 따라 선택할 수 있다.

- 클러스터 **– 인스턴스를 가용 영역 안에 서로 근접하게 패킹합니다. 이 전략은 워크로드가 HPC `High-Performance Computing` 애플리케이션에서 일반적인 긴밀히 결합된 노드 간 통신에 필요한 낮은 지연 시간의 네트워크 성능을 달성할 수 있다.
- 파티션 – 인스턴스를 논리적 파티션에 분산해, 한 파티션에 있는 인스턴스 그룹이 다른 파티션의 인스턴스 그룹과 기본 하드웨어를 공유하지 않게 한다. 이 전략은 일반적으로 Hadoop, Cassandra, Kafka 등 대규모의 분산 및 복제된 워크로드에 필요하다.
- 분산 – 소규모의 인스턴스 그룹을 다른 기본 하드웨어로 분산하여 상호 관련 오류를 줄인다.

각 배치 그룹 전략에는 규칙과 제한사항이 있으며 이들 규칙과 제한사항을 충족해야한다.

기본적으로 배치 그룹 생성은 무료이다. 하나의 배치 그룹에서 다양한 타입의 인스턴스를 사용할수도 있지만 가급적이면 같은 타입의 인스턴스 타입을 사용하는 것이 좋다.

배치 그룹은 여러 개의 AZ로 확장해서 사용할 수는 없다. 또한 AWS에서 유일무이한 이름을 사용해야하며 이미 생성되거나 실행중인 인스턴스를 배치 그룹에 옮길수도 없다.

EC2 배치 그룹 참고: [https://docs.aws.amazon.com/ko_kr/AWSEC2/latest/UserGuide/placement-groups.html](https://docs.aws.amazon.com/ko_kr/AWSEC2/latest/UserGuide/placement-groups.html)

## 스토리지 특징

EC2에서는 활용하는 애플리케이션의 특징에 따라 범용 SSD나 I/O 최적화된 스토리지, 마그네틱 스토리지 등 다양한 스토리지를 선택할 수 있다.

# 2021.06.19 TIL - EC2 인스턴스 타입

- [2021.06.19 TIL - EC2 인스턴스 타입](#20210619-til---ec2-인스턴스-타입)
  - [범용 인스턴스](#범용-인스턴스)
    - [M5 및 M5a 인스턴스](#m5-및-m5a-인스턴스)
    - [M5zn](#m5zn)
    - [M6 및 M6gd 인스턴스](#m6-및-m6gd-인스턴스)
    - [T2, T3 인스턴스](#t2-t3-인스턴스)
  - [컴퓨팅 최적화](#컴퓨팅-최적화)
    - [C5 및 C5n 인스턴스](#c5-및-c5n-인스턴스)
    - [**C6g, C6gd 인스턴스**](#c6g-c6gd-인스턴스)
    - [메모리 최적화](#메모리-최적화)
    - [**R5, R5a, R5b 및 R5n 인스턴스**](#r5-r5a-r5b-및-r5n-인스턴스)
    - [R6g 및 R6gd 인스턴스](#r6g-및-r6gd-인스턴스)
    - [X1 인스턴스](#x1-인스턴스)
    - [X1e 인스턴스](#x1e-인스턴스)
    - [z1d 인스턴스](#z1d-인스턴스)
    - [고용량 메모리 인스턴스](#고용량-메모리-인스턴스)
    - [SAP HANA 참고](#sap-hana-참고)
  - [스토리지 최적화 인스터스](#스토리지-최적화-인스터스)
    - [D2 인스턴스](#d2-인스턴스)
    - [**D3 및 D3en 인스턴스**](#d3-및-d3en-인스턴스)
    - [H1 인스턴스](#h1-인스턴스)
    - [**I3 및 I3en 인스턴스**](#i3-및-i3en-인스턴스)
    - [고성능 컴퓨팅 인스턴스](#고성능-컴퓨팅-인스턴스)
    - [공통사항](#공통사항)

EC2는 애플리케이션에 환경에 맞는 CPU, 메모리, 네트워킹, 스토리지 조합을 고를 수 있다. EC2 생태계에서 사용 가능한 인스턴스 타입은 현 새대 인스턴스와 이전 세대 인스턴스가 있다.

현 새대 인스턴스는 최신의 칩셋, 메모리, 프로세서등 서버 관련 최신 기술이 반영된 인스턴스인 반면 이전 세대 인스턴스는 현 세대보다 이전 세대의 기술이 반영된 인스턴스이다. 이전 세대 인스턴스는 AWS가 하위 호환성을 유지하기 위해 지원하는 것으로 처음 시작하는 인스턴스라면 현 세대 인스턴스를 활용하는 것을 권장한다.

EC2의 인스턴스 타입은 다음과 같이 구분할 수 있다.

- 범용 인스턴스
- 컴퓨팅 최적화 인스턴스
- 메모리 최적화 인스턴스
- 스토리지 최적화 인스턴스
- 고성능 컴퓨팅 인스턴스

## 범용 인스턴스

범용 인스턴스는 일반적인 목적으로 설계된 다수의 애플리케이션에 적용할 수 있도록 컴퓨팅 파워와 메모리, 네트워크 등 리소스의 균형을 맞춘 인스턴스 타입이다.

범용 인스턴스 릴리즈 정보: [https://docs.aws.amazon.com/ko_kr/AWSEC2/latest/UserGuide/general-purpose-instances.html#general-purpose-instances-limits](https://docs.aws.amazon.com/ko_kr/AWSEC2/latest/UserGuide/general-purpose-instances.html#general-purpose-instances-limits)

### M5 및 M5a 인스턴스

라우드에 배포된 광범위한 애플리케이션을 위해 컴퓨팅, 메모리, 네트워킹 리소스의 균형을 유지해 주는 이상적인 클라우드 인프라를 구축할 수 있다. 다음의 경우에 적합하다.

- 중소 규모 데이터베이스
- 추가 메모리가 필요한 데이터 처리 작업
- 캐싱 집합
- SAP, Microsoft SharePoint, 클러스터 컴퓨팅 및 기타 엔터프라이즈 애플리케이션을 위한 백엔드 서버

베어 메탈 인스턴스 `m5.metal`에서는 애플리케이션이 프로세서, 메모리 등 호스트 서버의 물리적 리소스에 직접 엑세스 할 수 있다.

### M5zn

매우 높은 단일 스레드 성능, 높은 처리량 및 짧은 지연 시간 네트워킹이 도움이 되는 애플리케이션에 적합하다. 이러한 인스턴스는 다음의 경우에 적합하다.

- 게임
- 고성능 컴퓨팅
- 시뮬레이션 모델링

### M6 및 M6gd 인스턴스

AWS Graviton2 프로세서로 구동되며 광범위한 범용 워크로드에 균형 잡힌 컴퓨팅, 메모리 및 네트워킹을 제공한다. 이러한 인스턴스는 다음의 경우에 적합하다.

- 애플리케이션 서버
- 마이크로서비스
- 게임 서버
- 중간 규모 데이터 스토어
- 캐싱 집합

AWS Graviton 프로세서 참고: [https://aws.amazon.com/ko/ec2/graviton/](https://aws.amazon.com/ko/ec2/graviton/)

> 참고로 AWS Gravition 프로세서ㄷ는 다음과 같은 요구사항을 가진다.

- 64비트 Arm 아키텍처용 AMI를 사용
- ACPI 테이블을 사용하여 UEFI를 통해 부팅을 지원하고 PCI 디바이스의 ACPI 핫플러그를 지원한다.

### T2, T3 인스턴스

기본 수준의 CPU 성능 외에 버스트 기능이 있어 워크로드에 필요한 만큼 성능을 높일 수 있다. 무제한 인스턴스는 필요할 때마다 원하는 기간 동안 높은 CPU 성능을 유지할 수 있다. 이러한 인스턴스는 다음의 경우에 적합하다.

- 웹 사이트 및 웹 애플리케이션:
- 코드 리포지토리
- 개발, 빌드, 테스트 및 스테이징 환경
- 마이크로서비스

## 컴퓨팅 최적화

컴퓨팅 최적화 인스턴스는 고성능 프로세서의 이점을 활용하는 컴퓨팅 집약적 애플리케이션에 적합하다.

### C5 및 C5n 인스턴스

다음과 같은 경우에 적합하다.

- 일괄 처리 작업
- 미디어 트랜스코딩
- 고성능 웹 서버
- 고성능 컴퓨팅(HPC)
- 과학 모델링
- 전용 게임 서버, 광고 서비스 엔진
- 기계 학습 추론 및 기타 컴퓨팅 집약적 애플리케이션

### **C6g, C6gd 인스턴스**

이러한 인스턴스는 AWS Graviton2 프로세서로 구동되며 다음과 같이 컴퓨팅 집약적인 고급 워크로드를 실행하는 데 적합하다.

- 고성능 컴퓨팅(HPC)
- 배치 처리
- 광고 지원
- 비디오 인코딩
- 게임 서버
- 과학 모델링
- 분산 분석
- CPU 기반 기계 학습 추론

### 메모리 최적화

고성능 메모리가 요구되는 워크로드를 처리 하기 위한 인스턴스 타입이다. 메모리에서 막대한 양의 데이터를 처리하는 애플리케이션의 경우 메모리 최적화 인스턴스가 적합하다.

메모리 최적화 인스턴스는 고용량 메모리를 보유하고 있는데 이를 잘 활용하기 위해서는 64비트 HVM AMI가 필요하다.

### **R5, R5a, R5b 및 R5n 인스턴스**

다음과 같은 경우에 적합하다.

- 고성능, 관계형 및 NoSQL
- 키-값 유형 데이터의 인 메모리 캐싱을 제공하는 분산된 웹 규모 캐시 저장소 `Memcached 및 Redis`
- 비즈니스 intel리전스를 위해 최적화된 데이터 스토리지 형식과 분석을 사용하는 인 메모리 데이터베이스  `SAP HANA`
- 대용량 비정형 데이터를 실시간으로 처리하는 애플리케이션 `Hadoop/Spark 클러스터`
- HPC(고성능 컴퓨팅) 및 EDA(전자 설계 자동화) 애플리케이션.

### R6g 및 R6gd 인스턴스

AWS Graviton2 프로세서로 구동되며 다음과 같이 메모리 집약적인 워크로드를 실행하는 데 적합하다.

- 오픈 소스 데이터베이스 `MySQL, MariaDB 및 PostgreSQL`
- 인 메모리 캐시 `Memcached, Redis 및 KeyDB`

### X1 인스턴스

이 인스턴스는 다음과 같은 경우에 적합하다.

- SAP HANA와 같은 인 메모리 데이터베이스 `Business Suite S/4HANA, Business Suite on HANA(SoH), Business Warehouse on HANA(BW) 및 Data Mart Solutions on HANA에 대한 SAP 인증 지원 포함`
- Apache Spark나 Presto와 같은 빅데이터 처리 엔진.
- HPC(고성능 컴퓨팅) 애플리케이션.

### X1e 인스턴스

이 인스턴스는 다음과 같은 경우에 적합하다.

- 고성능 데이터베이스.
- SAP HANA와 같은 인 메모리 데이터베이스.
- 메모리 집약적인 엔터프라이즈 애플리케이션.

### z1d 인스턴스

컴퓨팅 용량과 메모리가 대형이며 다음의 경우 적합하다.

- EDA(전자 설계 자동화)
- 관계형 데이터베이스 워크로드

### 고용량 메모리 인스턴스

고용량 메모리 인스턴스 `u-6tb1.metal, u-9tb1.metal, u-12tb1.metal, u-18tb1.metal 및 u-24tb1.metal` 는 각각 6TiB, 9TiB, 12TiB, 18TiB, 24TiB 메모리를 제공한다. 대규모 인 메모리 데이터 베이스를 실행하도록 설계되었으며 베어 메탈 성능으로 호스트 하드웨어에 대한 직접 엑세스를 제공한다.

### SAP HANA 참고

참고로 SAP HANA는 거의 모든 데이터를 메모리에 저장하여 처리하고 영구 스토리지 위치에 데이터를 저장하여 데이터를 보호한다. 때문에 메모리 최적화 인스턴스의 예시로 자주 등장한다.

SAP HANA: [https://www.sap.com/korea/products/hana/features.html](https://www.sap.com/korea/products/hana/features.html)
SAP HANA를 위한 스토리지 구성 참고: [https://docs.aws.amazon.com/ko_kr/quickstart/latest/sap-hana/storage.html](https://docs.aws.amazon.com/ko_kr/quickstart/latest/sap-hana/storage.html)

## 스토리지 최적화 인스터스

스토리지 최적화 인스턴스는 로컬 스토리지의 초대형 데이터 세트에 대한 순차적 읽기 및 쓰기 액세스가 많이 필요한 작업에 적합하도록 설계되어있다. 낮은 지연 시간의 임의의 IOPS(초당 I/O 작업)를 만 단위 수준으로 애플리케이션에 제공할 수 있도록 최적화되어 있다.

D2, D3, D3en 인스턴스의 스토리지는 기본적으로 HDD 인스턴스 스토어 볼륨이다. I3와 I3en은 NVMe `Non-Volatile Memory Express` SSD 인스턴스 스토어 볼륨이다.

Linux 인스턴스에서 최상의 디스크 처리량 성능을 보장하려면 Amazon Linux 2 AMI를 사용하는 것이 좋다.

Xen 지속적 권한 부여 참고: [https://blog.xenproject.org/2012/11/23/improving-block-protocol-scalability-with-persistent-grants/](https://blog.xenproject.org/2012/11/23/improving-block-protocol-scalability-with-persistent-grants/)

### D2 인스턴스

다음과 같은 경우에 적합하다.

- 대량 병렬 처리(MPP) 데이터 웨어하우스
- MapReduce 및 Hadoop 분산 컴퓨팅
- 로그 또는 데이터 처리 애플리케이션

D2 인스턴스가 최상의 디스크 성능을 제공하도록 만들고 싶으면 지속적 권한 부여 기능을 지원하는 Linux 커널을 사용하는 것이 좋다.

### **D3 및 D3en 인스턴스**

D3 및 D3en 인스턴스는 인스턴스 스토리지의 확장을 제공하며 다음 경우에 적합하다.

- 하둡 워크로드를 위한 분산 파일 시스템
- GPFC 및 BeeFS와 같은 파일 스토리지 워크로드
- HPC 워크로드를 위한 대규모 데이터 레이크

### H1 인스턴스

다음과 같은 경우에 적합하다.

- MapReduce 및 분산형 파일 시스템 같은 데이터 집약적 워크로드
- 직접 연결 인스턴스 스토리지에서 대량 데이터에 순차적으로 액세스해야 하는 애플리케이션
- 대량의 데이터에 대해 고속 액세스가 필요한 애플리케이션

### **I3 및 I3en 인스턴스**

다음과 같은 경우에 적합하다.

- 빈도가 높은 온라인 트랜잭션 처리(OLTP) 시스템
- 관계형 데이터베이스
- NoSQL 데이터베이스
- 인 메모리 데이터베이스의 캐시(예: Redis)
- 데이터 웨어하우징 애플리케이션
- 분산 파일 시스템

### 고성능 컴퓨팅 인스턴스

고성능 컴퓨팅 인스턴스는 머신러닝, 세포 모델링, 유전공학, 유체역학, 금융공학 등 탁월한 수준의 컴퓨팅 파워가 요구되는 워크로드를 처리하기 위한 인스턴스 타입이며, 높은 비용을 지불할수록 더 높은 수준의 컴퓨팅 파워를 활용할 수 있다. 높은 처리 성능이 필요한 경우 GPU, AWSFPGGA, Inferentia 같은 하드웨어 기반의 엑세스를 제공하는 엑셀러레이티드 컴퓨팅 인스턴스를 사용할 수 있다.

> AWS Inferentia는 아마존의 사용자 지정 AI/ML 칩으로 기계 학습을 가속화하도록 설계되어있다.

GPU 인스턴스는 G4ad, G4dn, G3, G2, P3, P2가 있다. AWS inferentia는 Inf1, FPGA 인스턴스는 F1 인스턴스 타입이 있다.

### 공통사항

- 인스턴스 성능

    EBS에 최적화된 인스턴스를 사용하면 Amazon EBS I/O와 인스턴스의 다른 네트워크 간의 경합을 제거하여 EBS 볼륨에 대해 일관되게 우수한 성능을 제공할 수 있다.

    또한 Linux에서 프로세서 C 상태 및 P 상태를 제어할 수 있는 기능을 제공한다. C 상태는 유휴 상태일 때 코어가 진입하는 절전 수준을 제어하고, P 상태는 코어의 성능 `CPU 주파수`을 제어한다.

    Amazon EBS 최적화 인스턴스: [https://docs.aws.amazon.com/ko_kr/AWSEC2/latest/UserGuide/ebs-optimized.html](https://docs.aws.amazon.com/ko_kr/AWSEC2/latest/UserGuide/ebs-optimized.html)
    CPU C 상태 / P 상태 참고: [https://scottlang.tistory.com/3](https://scottlang.tistory.com/3)
    EC2 인스턴스에 대한 프로세서 상태 제어: [https://docs.aws.amazon.com/ko_kr/AWSEC2/latest/UserGuide/processor_state_control.html](https://docs.aws.amazon.com/ko_kr/AWSEC2/latest/UserGuide/processor_state_control.html)

- 네트워크 성능

    지원되는 인스턴스 유형에 대해 향상된 네트워킹을 활성화하면 지연 시간을 줄이고 네트워크 지터를 낮추며 PPS `Packet Per Second`성능을 높일 수 있다. 대부분의 애플리케이션은 항시 높은 수준의 네트워크 성능을 필요로 하지 않지만, 데이터를 주고 받을 때 증가된 대역폭에 액세스할 수 있을 경우 유익할 수 있다.

    > T2 인스턴스를 제외한 모든 인스턴스 유형에 대해 향상된 네트워킹을 지원한다.

    Linux에서 향상된 네트워킹 참고: [https://docs.aws.amazon.com/ko_kr/AWSEC2/latest/UserGuide/enhanced-networking.html](https://docs.aws.amazon.com/ko_kr/AWSEC2/latest/UserGuide/enhanced-networking.html)

- SSD I/O 성능

    인스턴스에 대한 SSD 기반 인스턴스 스토어 볼륨에 데이터가 있는 경우, 달성 가능한 쓰기 IOPS의 수는 감소한다. 이는 SSD 컨트롤러가 가용 공간을 찾고 기존 데이터를 다시 쓰고 미사용 공간을 삭제하여 다시 쓸 수 있는 공간을 마련하기 위해 추가적인 작업을 해야 하기 때문이다. 즉, SSD에 대한 내부 쓰기 작업이 증폭되는 결과를 낳기 때문에 달성 가능한 IOPS가 감소한다.

    인스턴스 스토어 볼륨을 사용하는 경우 SSD 인스턴스 스토리지에 예약 공간을 마련해서 SSD 컨트롤러가 쓰기 작업에 사용 가능한 공간을 보다 효율적으로 관리할 수 있게 하는 오버-프로비저닝을 통해 최적화를 할 수 있다. 

    > 인스턴스 스토어 볼륨보다는 EBS를 사용하는 것이 좋아보인다.
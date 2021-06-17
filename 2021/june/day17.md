# 2021.06.17 TIL - VPC 엔드포인트

- [2021.06.17 TIL - VPC 엔드포인트](#20210617-til---vpc-엔드포인트)
  - [VPC 엔드포인트](#vpc-엔드포인트)
    - [VPC 엔드포인트 유형](#vpc-엔드포인트-유형)

## VPC 엔드포인트

S3와 같은 VPC 외부에서 실행되는 AWS 서비스 및 VPC 엔드포인트 서비스에 비공개로 연결할 수 있도록 지원한다. VPC의 인스턴스는 리소스와 통신하는데 퍼블릭 IP 주소를 필요로 하지 않으며 VPC와 기타 서비스 간의 트래픽은 Amazon 네트워크를 벗어나지 않는다.

프라이빗 서브넷에 EC2 인스턴스가 있고 S3와 연결해야 한다면, VPC 엔드포인트를 이용해 EC2와 S3를 프라이빗 서브넷에서 바로 연결할 수 있으므로 별도의 데이터 전송 비용이 들지 않는다. VPC 엔드포인트가 없다면 퍼블릭 서브넷에 있는 S3의 데이터를 EC2 인스턴스가 있는 프라이빗 서브넷에 전송해야한다. 이때 트래픽은 인터넷으로 연결된 리전별 서비스인 S3에 접속하기 위해 VPC를 벗어나야 하고, 데이터를 포함한 트래픽이 다시 VPC로 들어오는 비용이 발생하게 되는것이다.

AWS PrivateLink를 지원하는 서비스라면 VPC 엔드포인트와 연결이 가능하다.

AWS PrivateLink를 지원하는 서비스 참고: [https://docs.aws.amazon.com/ko_kr/vpc/latest/privatelink/integrated-services-vpce-list.html](https://docs.aws.amazon.com/ko_kr/vpc/latest/privatelink/integrated-services-vpce-list.html)

### VPC 엔드포인트 유형

VPC 엔드포인트는 연결 대상 서비스에 따라 엔드포인트와 엔드포인트 서비스로 구분 지을 수 있다. 엔드포인트는 S3, DynamoDB와 같은 AWS 퍼블릭 서비스를 대상으로 연결하는 반면 엔드포인트 서비스는 사용자가 직접 생성한 서비스에 연결한다는 차이가 있다.

엔드포인트에는 또 게이트웨이 엔드포인트와 인터페이스 엔드포인트가 있다. 게이트웨이 엔드포인트는 AWS 퍼블릭 서비스 중 S3와 DynamoDB에 대해 사용하고 인터페이스 엔드포인트는 그외 나머지 AWS 퍼블릭 서비스에 연결한다.

![](https://user-images.githubusercontent.com/30178507/122676578-9de55580-d219-11eb-8d42-ed8816c44ef6.png)

> S3는 게이트웨이, 인터페이스 두 엔드포인트 유형을 제공한다.

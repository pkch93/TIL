# 2021.07.14 TIL - AWS Lambda, AWS API Gateway

## AWS Lambda

Amazon S3 버킷 이미지 업로드, DynamoDB 테이블 갱신, Kinesis Data Streams 데이터 입력, 인앱 액티비티 등 애플리케이션의 각종 이벤트에 반응해 백엔드 코드를 실행하는 컴퓨팅 서비스다.

코드를 업로드하기만 하면 코드 실행에 필요한 용량 및 확장성 관리, 패치 업무, 인프라 관리 업무 등 제반 서버 관련 업무를 처리한다. Lambda의 모니터링은 CloudWatch를 통해 할 수 있다.

AWS Lambda에서 실행되는 코드를 Lambda 함수라 부르며, 개발자는 자신이 작성한 코드를 ZIP 파일 형식으로 업로드하거나 AWS 관리 콘솔에 자신만의 통합 개발환경을 구축할 수 있고, 이미지 변환, 파일 압축, 변경 알림 등 다양한 Lambda 함수 샘플을 이용해 개발 시간을 줄이고 목표한 코드를 신속하게 업로드할 수 있으며, AWS SDK를 이용해 간편하게 다른 AWS 서비스를 호출할 수 있다. Lambda 함수를 업로드한 뒤에 S3 버킷 또는 DynamoDB 테이블 등을 모니터링할 수 있는 이벤트 소스를 선택하면, 이벤트 발생 시 자동으로 해당 함수가 실행된다.

AWS Lambda는 사용한 만큼만 비용을 지불한다. 함수에 대한 요청 수와 기간, 코드를 실행하는 데 걸리는 시간에 따라 요금이 청구된다. 코드의 실행 횟수 `요청 1백만 건당 0.20 USD` 및 100ms 단위로 시간을 측정하여 과금하며 코드 실행에 따른 메모리 요금도 부과되지만 코드를 실행하지 않을때는 과금되지 않는다. 

AWS Lambda Price 참고: [https://aws.amazon.com/ko/lambda/pricing/](https://aws.amazon.com/ko/lambda/pricing/)

AWS Lambda는 리소스 제한량이 있으므로 이에 유의해야한다.

- 컴퓨팅 및 스토리지 할당량

리소스 | 기본 할당량 | 최대 한도 증가
:--: | :--: | :--:
동시 실행 | 1,000 | 수십만
업로드된 함수 .zip 파일 아카이브 및 계층을 위한 스토리지 | 75 GB | TB
컨테이너 이미지로 정의된 함수에 대한 스토리지 | Amazon ECR 서비스 할당량을 참조	| -
Virtual Private Cloud(VPC)별 탄력적 네트워크 인터페이스 | 250 | 수백

> 참고로 VPC 별 탄력적 네트워크 인터페이스 할당량은 Amazon Elastic File System `Amazon EFS`과 같은 다른 서비스와 공유된다.

- 함수 구성, 배포, 실행 할당량

리소스 | 할당량
:--: | :--:
함수 메모리 할당 | 128MB ~ 10,240 MB, 1MB씩 증분됨
함수 제한 시간 | 900초 (15 minutes)
함수 환경 변수 | 4 KB
실행 프로세스/스레드 | 1,024
파일 설명자 | 1,024
/tmp 디렉터리 스토리지 | 512 MB
테스트 이벤트(콘솔 편집기) | 10
컨테이너 이미지 코드 패키지 크기 | 10GB
배포 패키지 (.zip 파일 아카이브) 크기 | 50 MB(직접 업로드용 압축 파일) / 250 MB(계층을 포함해 압축 해제됨) / 3 MB(콘솔 편집기)
호출 페이로드(요청 및 응답) | 6 MB(동기식) / 256 KB(비동기식)
함수 버스트 동시성 | 500~3000(리전에 따라 달라짐)
함수 계층 | 5개의 계층
함수 리소스 기반 정책 | 20 KB

- Lambda API 할당량

리소스 | 할당량
:--: | :--:
리전당 호출 요청(초당 요청 수) | 10배 동시 실행 할당량(동기식, 모든 소스) / 10배 동시 실행 할당량(비동기식, AWS 외의 소스)
비동기식 AWS 서비스 소스에 대한 리전별 호출 요청(초당 요청 수) | 무제한 요청이 허용. 실행 속도는 함수에 사용할 수 있는 동시성에 따라 달라진다.
함수 버전 또는 별칭당 호출 요청(초당 요청 수) | 10배 할당된 프로비저닝된 동시성
나머지 제어 플레인 API 요청(호출, GetFunction 및 GetPolicy 요청 제외) | 초당 요청 15개
GetPolicy API 요청 | 초당 요청 15개
GetFunction API 요청 | 초당 요청 100개

> 참고로 IAM, Lambda@Edge, Amazon VPC 등과 같은 서비스 할당량이 Lambda 함수에 영향을 줄 수 있다.

AWS Lambda 할당량 참고: [https://docs.aws.amazon.com/ko_kr/lambda/latest/dg/gettingstarted-limits.html](https://docs.aws.amazon.com/ko_kr/lambda/latest/dg/gettingstarted-limits.html)

21.07.14 현재 AWS Lambda에서는 Node.js, Python, Ruby, Java, Go, C#, PowerShell로 코드 작성이 가능하다.

### AWS Lambda 참고

AWS Lambda docs: [https://docs.aws.amazon.com/ko_kr/lambda/latest/dg/getting-started.html](https://docs.aws.amazon.com/ko_kr/lambda/latest/dg/getting-started.html)

AWS Lambda FAQ: [https://aws.amazon.com/ko/lambda/faqs/](https://aws.amazon.com/ko/lambda/faqs/)

AWS Lambda Release: [https://docs.aws.amazon.com/ko_kr/lambda/latest/dg/lambda-releases.html](https://docs.aws.amazon.com/ko_kr/lambda/latest/dg/lambda-releases.html)

## Amazon API Gateway

Amazon API Gateway는 규모와 관계없이 REST 및 WebSocket API를 생성, 게시, 유지, 모니터링 및 보호하기 위한 완전 관리형 서비스이다. Amazon EC2, ECS, AWS Lambda, 온프레미스 환경의 모든 웹 애플리케이션과 연결할 수 있는 창구를 제공해준다.

Amazon API Gateway는 수십만 건에 이르는 동시적 API 호출을 처리하도록 지원하며 API 관리를 위한 접근 권한 부여, 트래픽 모니터링, 버전 컨트롤 등의 문제도 대신 처리해준다.

### Amazon API Gateway 참고

Amazon API Gateway docs: [https://docs.aws.amazon.com/ko_kr/apigateway/latest/developerguide/welcome.html](https://docs.aws.amazon.com/ko_kr/apigateway/latest/developerguide/welcome.html)

Amazon API Gateway 유의사항: [https://docs.aws.amazon.com/ko_kr/apigateway/latest/developerguide/api-gateway-known-issues.html](https://docs.aws.amazon.com/ko_kr/apigateway/latest/developerguide/api-gateway-known-issues.html)
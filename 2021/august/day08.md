# 2021.08.08 TIL - Amazon CloudFront 엣지 컴퓨팅

- [2021.08.08 TIL - Amazon CloudFront 엣지 컴퓨팅](#20210808-til---amazon-cloudfront-엣지-컴퓨팅)
  - [CloudFront 함수](#cloudfront-함수)
  - [Lambda@Edge](#lambdaedge)
  - [CloudFront 함수 vs Lambda@Edge](#cloudfront-함수-vs-lambdaedge)

Amazon CloudFront는 엣지 로케이션에서 코드를 실행할 수 있는 기능을 제공한다. CloudFront 함수는 Javascript 코드를 실행할 수 있는 서버리스 엣지 컴퓨팅 기능을 제공하는 반면 Lambda@Edge는 AWS Lambda를 확장하여 글로벌 엣지 로케이션에서 코드를 실행할 수 있도록 도와준다.

## CloudFront 함수

CloudFront 함수는 최신 웹 애플리케이션에 필요한 성능 및 보안과 함께 전체 프로그래밍 환경의 유연성을 고객에게 제공하기 위한 목적별 기능이다.

보통 간단한 단기 실행 함수가 적합하며 다음과 같이 활용할 수 있다.

- 캐시 키 정규화
- 헤더 조작
- URL Redirection
- 요청 권한 부여

## Lambda@Edge

Lambda@Edge는 Node.js 또는 Python 환경에서 실행된다. 단일 AWS 리전에 함수를 개시하고 CloudFront에 이를 배포하면 Lambda@Edge가 자동으로 전세계 엣지 로케이션에 복제한다.

Lambda@Edge는 다음과 같은 CloudFront의 이벤트에 대해 트리거할 수 있다.

- **시청자 요청**: 최종 사용자나 인터넷상의 디바이스가 CloudFront에 HTTP 요청을 하고 해당 사용자에게 가장 가까운 엣지 로케이션에 요청이 도착할 때 이 이벤트가 발생한다.
- **시청자 응답**: 엣지에 있는 CloudFront 서버가 요청한 최종 사용자나 디바이스에 응답할 준비가 될 때 이 이벤트가 발생한다.
- **오리진 요청**: CloudFront 엣지 서버에 캐시에서 요청된 객체가 아직 없고 백엔드 오리진 웹 서버 `Amazon EC2, Application Load Balancer, Amazon S3`로 요청을 보낼 준비가 되어 있을 때 이 이벤트가 발생한다.
- **오리진 응답**: 엣지에 있는 CloudFront 서버가 백엔드 오리진 웹 서버에서 응답을 수신할 때 이 이벤트가 발생한다.

## CloudFront 함수 vs Lambda@Edge

언뜻보면 두 엣지 컴퓨팅이 대체 가능하다고 볼 수 있지만 CloudFront 함수는 Lambda@Edge을 보완하는 기능이며 대체 가능하지 않다.

따라서 함께 사용했을때 더욱 강력한 기능을 제공할 수 있다.

CloudFront 함수 및 Lambda@Edge를 결합하면 CloudFront 이벤트에 대한 응답으로 코드를 실행할 때 강력하면서도 유연한 두 가지 옵션을 제공한다. 둘 다 인프라를 관리하지 않고도 CloudFront 이벤트에 대한 응답으로 코드를 실행하는 안전한 방법을 제공한다. CloudFront 함수는 지연 시간에 민감한 경량의 대규모 요청/응답 변환 및 조작을 위해 특별히 설계된 반면 Lambda@Edge는 다양한 컴퓨팅 요구 사항 및 사용자 지정을 지원하는 범용 런타임을 사용한다. 즉, Lambda@Edge는 컴퓨팅 집약적 작업에 사용해야한다.

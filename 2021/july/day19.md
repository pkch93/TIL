# 2021.07.19 TIL - Amazon CloudFront

Amazon CloudFront는 AWS의 글로벌 CDN 서비스로서 저지연성과 고속 전송 속성을 지닌 콘텐츠 배포 기능을 제공하며 콘텐츠 사용자와 가까운 지역에 네트워크망을 구축하고 지역별 엣지 캐시를 통해 사용자 경험 수준을 높여준다.

Amazon CloudFront는 최소 사용량 같은 사용량 설정을 할 필요없이 사용자가 실제로 요청, 전송한 데이터에 대한 비용만 부담하면 된다. 참고로 AWS 리전과 CloudFront 엣지 로케이션 간의 데이터 전송 비용은 부담하지 않는다.

Amazon CloudFront로 정적 콘텐츠 뿐만 아니라 동적 콘텐츠, 소프트웨어 배포, 비디오 스트리밍 등에 활용가능하다.

## Amazon CloudFront 배포 생성

> CloudFront에서 배포 `Distribution`란 제공하려고 하는 콘텐츠를 CloudFront를 통해 제공하도록 만드는 것을 의미.

Amazon CloudFront를 통해 컨텐츠를 제공하기 위해서는 배포 생성이 필요하다. 배포 생성에서 오리진 `Origin` 설정, CDN 캐시 설정, 제어 설정 등을 할 수 있다.

HTTP와 HTTPS를 사용하는 정적, 동적 컨텐츠와 Apple HTTP Live Streaming(HLS) 및 Microsoft Smooth Streaming 등과 같은 다양한 형식의 온디맨드 비디오 실시간으로 발생하는 모임, 회의 또는 콘서트 같은 라이브 이벤트 제공 등에 CloudFront를 활용할 수 있다.

CloudFront에는 할당량 기준이 있다. [참고](https://docs.aws.amazon.com/ko_kr/AmazonCloudFront/latest/DeveloperGuide/cloudfront-limits.html#limits-web-distributions)

### 오리진 `Origin` 설정

![](https://user-images.githubusercontent.com/30178507/126328602-6c7f17c1-8e0b-4e42-82b2-48c3a3f3abba.png)

오리진으로는 Amazon S3, ELB, [MediaStore Container](https://ap-northeast-2.console.aws.amazon.com/mediastore/home/landing), [MediaPackage Container](https://ap-northeast-2.console.aws.amazon.com/mediapackage/home?region=ap-northeast-2#/landing)를 지정할 수 있다. 만약 AWS 리소스나 사용자 지정 오리진에 있는 디렉터리에서 콘텐츠 요청을 하게 만드려면 `Origin Path`에 `/` 뒤 디렉터리 경로를 입력한다.

> 이때 Origin Path 끝에는 `/`를 추가하면 안된다.

Name은 배포 내에서 오리진을 고유하게 만드는 문자열이다. 이때 기본 캐시 동작에 추가로 캐시 동작을 만들경우, Name 값을 사용하여 요청이 해당 캐시 동작의 경로 패턴과 일치할 때 CloudFront에서 요청을 라우팅하려는 오리진이나 오리진 그룹을 식별한다.

그외 위 CDN에 요청 할 때 추가할 헤더와 `Origin Shield`를 사용할 지 설정을 할 수 있다.

> `Origin Shield`는 캐싱 인프라의 추가 레이어를 얹는 것으로 이를 통해 캐시 적중률을 높이고 오리진 부하 감소, 네트워크 성능을 높일 수 있다. 다만 추가 요금이 발생한다.

Origin Shield 참고: [https://docs.aws.amazon.com/ko_kr/AmazonCloudFront/latest/DeveloperGuide/origin-shield.html?icmpid=docs_cf_help_panel](https://docs.aws.amazon.com/ko_kr/AmazonCloudFront/latest/DeveloperGuide/origin-shield.html?icmpid=docs_cf_help_panel)

![](https://user-images.githubusercontent.com/30178507/126328606-122859ca-2089-4bc7-a177-c151b571929d.png)

`Additional Settings`에서는 오리진 연결 시도 횟수, 커넥션 타임아웃, 응답 타임아웃, keep-alive 타임아웃을 지정할 수 있다.

### 캐시 동작 설정

![](https://user-images.githubusercontent.com/30178507/126328612-425fc4c8-fdcb-482e-8966-b4adb5c4bb74.png)

CloudFront의 캐시 동작은 웹 사이트에 있는 파일의 지정된 URL 경로 패턴에 대해 다양한 CloudFront 기능을 구성할 수 있도록 도와준다.

기본적으로 캐시 동작은 콘솔에서 설정한 순서에 따라 동작하며 기본 캐시 동작은 만약 추가 캐시 동작이 있는 경우 항상 마지막으로 동작한다.

> CloudFront API로는 DistributionConfig에 설정한 순서대로 동작한다.

### 배포 설정

![](https://user-images.githubusercontent.com/30178507/126328614-a972c9ba-8332-4839-8d49-0cf0a94e2d4e.png)

배포 설정으로는 요금 정책, 보안 설정 `ACL / WAF`, 도메인 설정, 로깅, IPv6 사용 설정 등을 할 수 있다.

배포 지정하는 값 참고: [https://docs.aws.amazon.com/ko_kr/AmazonCloudFront/latest/DeveloperGuide/distribution-web-values-specify.html#DownloadDistValuesOriginPath](https://docs.aws.amazon.com/ko_kr/AmazonCloudFront/latest/DeveloperGuide/distribution-web-values-specify.html#DownloadDistValuesOriginPath)
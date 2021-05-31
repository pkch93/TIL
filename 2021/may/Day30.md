# 2021.05.30 TIL - AWS S3

## S3 `Amazon Simple Storage Service`

S3는 AWS에서 지원하는 객체 스토리지로 전 세계 어디서나 대규모 데이터를 저장하고 인출할 수 있는 인터페이스를 제공한다. S3의 저장용량은 **무제한**이며 99.999999%에 이르는 고신뢰성을 제공한다.

S3는 파일 시스템이 존재하지 않으며 모든 객체는 S3 버킷에 단순 네임스페이스만으로 저장된다. 또한 S3는 지역별 서비스로서 지역별 재난 상황에 대비하여 자동으로 반복 저장한다.

### S3의 기본 개념

S3에는 **버킷**이라는 개념이 등장한다. 버킷은 객체를 담기 위한 컨테이너 역할을 하는데 이는 파일 시스템에서 폴더의 역할과 비슷하다고 할 수 있다.

버킷 사용시에 주의점이 있다면 **버킷의 이름을 모든 리전을 통틀어 유일무이하게 지어야한다는 점**이다. 만약 `pkch`라는 버킷에 `profile.png` 객체를 저장하면 `http://pkch.s3.amazonaws.com/profile.png`라는 URL이 생성된다.

버킷은 명시적으로 복제작업을 수행하거나 크로스-리전 복제를 하지 않는 이상 **다른 리전에 특정 버킷의 데이터가 복제되지 않는다.** 또한 S3 버킷은 버 전 부여 기능을 제공하므로 객체가 버킷에 추가될 때마다 해당 객체에 유일한 ID가 할당된다.

#### S3 객체

S3에 저장되는 데이터는 모두 객체라고 부른다. 각 객체는 데이터와 메타데이터를 지니는데 S3 버킷에 올리는 데이터가 바로 데이터이고 최종 수정일, 파일 타입 등의 데이터를 메타데이터라고 한다. 메타데이터는 네임-벨류 쌍으로 이뤄진다.

객체는 키를 통해서 버킷에서 유일한 것으로 식별될 수 있으며 , 버킷에 존재하는 모든 객체는 단 하나의 키를 지닌다. 따라서 S3 내에서 버킷, 키, 버전 ID를 통해 특정 객체를 파악할 수 있다.

> `http://s3.amazonaws.com/2017-02/pictures/photo1.gif`라고 S3 객체 URL이 있을때 `2017-02/pictures/photo1.gif`가 객체의 키가 된다.

#### S3에 접근하기

S3는API를 통해 접근할 수 있으며 , 개발자는 S3 기반의 애플리케이션을 개발할 수 있다. S3의 기본 인터페이스는 REST API이다. https에서는 SOAP API를 지원한다.

> 참고로 HTTP 기반의 SOAP API를 지원하지는 않으며 향후에는 SOAP 지원이 되지 않을 예정이므로 REST API 사용을 권장한다.

REST API를 통해 S3 버킷에서 파일 생성, 읽기, 갱신, 삭제, 목록 조회 등 모든 작업을 수행할 수 있고, 표준 http/https 요청과 관련된 모든 작업도 수행할 수 있다.

REST API 이외에도 웹 브라우저, Android, iOS와 같은 플랫폼이나 Java, .NET, Node.js, PHP, Python, Ruby, Go, C++과 같은 언어에 SDK를 제공한다. 각 플랫폼과 언어에서는 SDK를 통해서도 S3에 접근할 수 있다.

마지막으로 AWS CLI를 통해서도 s3를 다룰 수 있다.

```
$ aws s3 mb s3://bucket-name
$ aws s3 rb s3://bucket-name
```

위와 같이 s3를 다루기 위해서는 `aws s3` 명령으로 실행할 수 있으며 버킷 생성은 `mb`, 삭제는 `rb`로 수행할 수 있다.

AWS CLI S3 명령 참고: [https://docs.aws.amazon.com/ko_kr/cli/latest/userguide/cli-services-s3-commands.html](https://docs.aws.amazon.com/ko_kr/cli/latest/userguide/cli-services-s3-commands.html)

### AWS S3 데이터 일관성 모델

AWS S3는 특정 OS 기반의 파일 시스템이 아닌 **웹 기반의 데이터 저장소**이다. 따라서 S3의 아키텍처는 전통적인 파일 시스템이나 SAN 아키텍처와는 차이가 있다.

스토리지 구성 방식 참고: [http://www.incodom.kr/스토리지_구성_방식](http://www.incodom.kr/%EC%8A%A4%ED%86%A0%EB%A6%AC%EC%A7%80_%EA%B5%AC%EC%84%B1_%EB%B0%A9%EC%8B%9D)

#### S3 아키텍처 `한 번 기록하고, 여러 번 읽는 아키텍처`

S3의 인프라는 기본적으로 복수의 AZ 위에 다수의 로드 벨런서, 웹 서버, 스토리지로 구성된다. 전체 아키텍처는 신뢰성을 위해 중복 구현되며 각 데이터는 위치를 서로 달리하는 복수의 AZ에 중복 저장된다.

위 캡처는 데이터가 S3에 기록되는 과정을 보여준다. 객체를 S3에 올릴때 가장 먼저 로드 벨런서와 연결되고, 다음으로 웹 서버의 API에 연결되며 마지막으로 다수의 AZ에 있는 다수의 스토리지에 중복 저장된다.

저장이 이뤄지면 인덱싱 작업이 진행되고, 그 내용 또한 다수의 AZ와 스토리지에 중복 저장된다. 이때 로드 벨런서나 웹 서버가 다운되는 경우 S3는 중복 구현된 또 다른 로드 벨런서 또는 웹 서버에 요청을 보내고 스토리지 유닛 또는 인덱싱 스토리지가 다운되면 중복 구현한 또 다른 스토리지에서 저장이 이뤄진다.

연동된 전체 AZ가 다운되어 시스템 페일오버가 발생할 경우 전체 시스템이 복제되어 있는 또 다른 복수의 AZ를 통해 서비스를 제공한다.

> 참고로 S3-One Zone Infrequent Access 서비스는 단일 AZ에만 데이터를 저장한다.

#### S3 일관성 모델

새 객체를 작성하면 동기적으로 다수의 클라우드 설비에 저장된다. 이를 통해 기록 후 판독 일관성을 제공한다. `read-after-write consistent` 기록 후 판독 일관성은 모든 사용자가 동일한 결과를 받아볼 수 있도록 도와준다.

S3는 기존의 다른 모든 객체를 위해서 종국적 일관성 모델`eventually consistent`을 제공한다. 종국적 일관성 모델에서는 데이터가 자동으로 복제되어 다수의 시스템과 AZ로 확산되므로 최신의 내용으로 변경한 내용이 즉각적으로 반영되지 않거나, 업데이트 직후 데이터를 읽으려 할 때 변경된 내용을 확인할 수 없게 될 가능성이 있다.

> 참고로 현재는 기록 후 판독 일관성을 제공한다.

참고: [https://aws.amazon.com/ko/blogs/aws/amazon-s3-update-strong-read-after-write-consistency/](https://aws.amazon.com/ko/blogs/aws/amazon-s3-update-strong-read-after-write-consistency/)

일관성 모델 관련하여 다음과 같은 예시가 있다.

- S3에 새 객체를 작성하고, 즉시 읽기를 시도해보자.
변경 사항이 완전히 확산되기 전까지 S3는 key does not exist라는 메시지를 출력 할 것이다.
- S3에 새 객체를 작성하고, 즉시 버 킷의 키 리스트를 출력 해보자.
변경 사항이 완전히 확산되기 전까지 해당 객체가 리스트에 나타나지 을 것이다.
- 기존의 객체를 대체한 뒤, 즉시 읽기를 시도해보자.
변경 사항이 완전히 확산되 기 전까지 S3는 기존의 데이 터를 반환할 것이다.
- 기존의 객체를 삭제한 뒤, 즉시 읽기를 시도해보자.
변경 사항이 완전히 확산되기 전까지 S3는 삭제를 명령한 데이터를 반환할 것이다.
- 기존의 객체를 삭제한 뒤 즉시 버킷의 키 리스트를 출력해보자.
변경 사항이 완전히 확산되기 전까지 S3는 삭제를 명령한 객체도 리스트로 출력할 것이다.

업데이트`PUT`의 경우 단일 키에 대한 업데이트는 아토믹 특성을 보인다. 즉, 최종 읽기 실행의 결과는 업데이트 된 결과나 업데이트 되기 전 결과만 있다는 의미이다. 일부만 수정될 가능성은 없다.

S3 사용시 주의사항으로는 객체 잠금 기능을 제공하지 않는다는 것이다. 동일 파일에 대해서 동시다발적으로 업데이트 요청이 오는 경우 최종 타임스템프를 지닌 요청 `제일 나중에 온 요청`을 따른다.

### 버킷 워크로드 파티셔닝 가이드

S3 버킷에 **초당 100회 이상의 PUT/LIST/DELETE 요청**을 하거나 **300회 이상의 GET 요청**을 해야 한다면 작업을 분산할 수 있는 방법을 고려해야한다.

> 참고로 S3 버킷은 분할된 접두사 하나당 초당 3,500개의 PUT/COPY/POST/DELETE 또는 5,500개의 GET/HEAD 요청을 지원할 수 있다.
>
> [https://aws.amazon.com/ko/premiumsupport/knowledge-center/s3-object-key-naming-pattern/](https://aws.amazon.com/ko/premiumsupport/knowledge-center/s3-object-key-naming-pattern/)

**S3 버킷 이름은 유일무이한 것**이어야 하며, 버킷 이름과 객체를 통해 글로벌에서 단 하나뿐인 주소를 가진다. 이때 **객체 키는 해당 버킷에서 유일무이해야한다**. 최대 1,024 바이트 용량의 UTF-8 바이너리 코드로 저장된다.

만약 다음과 같이 챕터별 이미지 파일을 각 챕터별 폴더에 저장한다고 가정한다.

```
chapter2/image/image2.1.jpg
chapter2/image/image2.2.jpg
chapter2/image/image2.3.jpg
chapter3/image/image3.1.jpg
chapter3/image/image3.2.jpg
chapter3/image/image3.3.jpg
chapter3/image/image3.4.jpg
chapter4/image/image4.1.jpg
chapter4/image/image4.2.jpg
chapter4/image/image4.3.jpg
chapter4/image/image4.4.jpg
chapterS/image/imageS.1.jpg
chapterS/image/imageS.2.jpg
chapter6/image/image6.1.jpg
chapter6/image/image6.2.jpg
chapter7/image/image7.1.jpg
chapter7/image/image7.2.jpg
chapter7/image/image7.3.jpg
chapter8/image/image8.1.jpg
chapter8/image/image8.2.jpg 
```

> 참고로 객체 키는 파일 명인 `image2.1.jpg`나 `image3.4.jpg`가 아니라 `chapter2/image/image2.1.jpg`와 `chapter3/image/image3.4.jpg`가 된다.

위와 같이 저장하는 경우 S3는 객체키의 가장 앞 글자인 `c`를 기준으로 파티셔닝한다. 만약 위와 같이 이미지를 저장한다면 챕터별로 파티셔닝 되는 것이 아니라 책 한권의 이미지가 하나의 파티션에 저장될 것이다. 따라서 객체키를 바꿔서 파티셔닝 최적화를 할 수 있다.

```
2chapter/image/image2.1.jpg
2chapter/image/image2.2.jpg
2chapter/image/image2.3.jpg
3chapter/image/image3.1.jpg
3chapter/image/image3.2.jpg
3chapter/image/image3.3.jpg
3chapter/image/image3.4.jpg
4chapter/image/image4.1.jpg
4chapter/image/image4.2.jpg
4chapter/image/image4.3.jpg
4chapter/image/image4.4.jpg
5chapter/image/imageS.1.jpg
5chapter/image/imageS.2.jpg
6chapter/image/image6.1.jpg
6chapter/image/image6.2.jpg
7chapter/image/image7.1.jpg
7chapter/image/image7.2.jpg
7chapter/image/image7.3.jpg
8chapter/image/image8.1.jpg
8chapter/image/image8.2.jpg 
```

위와 같이 객체 키를 바꾸면 객체 키의 첫 번째 값이 `2, 3, 4, 5, 6, 7, 8`과 같은 챕터 번호로 나눠지게 되므로 파티셔닝의 기준 또한 달라지게 된다.

따라서 위와 같이 객체 키를 변경하면 챕터별 숫자를 기준으로 분산되어 저장한다.

#### 키 이름 문자열의 역순 배열

위와 같은 S3의 파티셔닝 방식을 참고하면 다양한 최적화 방식이 있다. 그 중 하나가 Hex Hash를 키 이름 프리픽스로 추가하는 방법이 있다.

```
applicationid/5213332112/log.text
applicationid/5213332112/error.text
applicationid/5213332113/log.text
applicationid/5213332113/error.text
applicationid/5213332114/log.text
applicationid/5213332114/error.text
applicationid/5213332115/log.text
applicationid/5213332115/error.text
```

위와 같이 `applicationid` 버킷에 log나 error와 같은 업로드 세트마다 applicationId를 1씩 증가하여 추가하도록 설계했다고 가정한다. 이렇게 설계한다면 당분간 추가되는 로그들이 모두 `applicationId/5` 파티션으로 몰리게 될 것이다. 이 문제는 객체 키를 역순으로 변경하는 것만으로도 파티션 고르게 저장하도록 만들 수 있다.

```
applicationid/2112333125/log.text
applicationid/2112333125/error.text
applicationid/3112333125/log.text
applicationid/3112333125/error.text
applicationid/4112333125/log.text
applicationid/4112333125/error.text
applicationid/5112333125/log.text
applicationid/5112333125/error.text
```

as-is에서는 모든 객체가 `applicationId/5`파티션에 저장되는 것과 달리 to-be에서는 `applicationId/2`, `applicationId/3`, `applicationId/4`, `applicationId/5` 파티션에 고르게 저장된다.

#### 키 이름에 Hex Hash prefix 추가하기

결국 객체의 prefix를 고르게 주는 방법이 S3 파티셔닝을 최적화하는 방법이다. 이를 위해서 16진수 계열의 Hex Hash를 prefix로 추가하는 방법도 있다.

```
applicationid/112a5213332112/log.text
applicationid/c9125213332112/error.text
applicationid/2a825213332113/log.text
applicationid/7a2d5213332113/error.text
applicationid/c3dd5213332114/log.text
applicationid/8ao95213332114/error.text
applicationid/z91d5213332115/log.text
applicationid/auw85213332115/error.text
```

단, 무작위성의 해시 키를 사용할 때는 해시 알고리즘 특유의 랜덤 속성에 주의해야한다. 객체가 너무 많은 경우에 너무 많은 파티션이 생성될 수 있다.

위 예시의 경우만 봐도 4개의 prefix를 가지므로 총 65,536개의 파티션이 만들어 질 수 있다. 보통은 2~3개의 prefix 문자열로도 충분하며 이 경우 초당 100회의 요청 처리 및 파티션별 2500만개의 객체 저장 업무가 가능하다. 4개의 prefix는 초당 수백만건의 요청을 처리하기 위한 것으로 일반적으로는 불필요하다.

### S3 암호화

AWS S3에서 데이터를 암호화 하는 방법은 크게 두가지가 있다. 하나는 전송 중인 데이터를 암호화하는 것이고, 하나는 저장된 데이터를 암호화하는 것이다.

#### 전송 중인 데이터 암호화

전송 중인 데이터 암호화란 데이터가 한 지점에서 다른 지점으로 이동할 때의 암호화를 의미한다. https 또는 SSL 암호화 종단점에서 데이터를 업로드하면 모든 업로드 및 다운로드는 자동으로 암호화되고 전송 중에도 암호화를 유지한다.

또한 S3 암호화 클라이언트를 사용하여 S3에 업로드하는 경우 전송 중에도 암호화 상태가 유지된다. S3 암호화 클라이언트는 S3에 안전하게 데이터를 저장하기 위한 클라이언트 측 암호화 방식으로 각 S3 객체마다 1회성 랜덤 CEK `콘텐츠 암호화 키`를 이용해 암호화한다.

#### 저장된 데이터 암호화

저장된 데이터 암호화란 데이터 또는 객체가 S3 버킷에 저장되어 대기상태에 있을 때의 암호화를 의미한다.

SSE `Server Side Encryption`을 이용하며 데이터를 작성할 때 자동으로 암호화되고 데이터를 인출할 때 자동으로 복호화한다. 이때 AES 256-비트 대칭키를 사용하며 다음과 같이 키를 관리한다.

- AWS S3 키 매니지먼트 기반의 SSE `SSE-SE`
- 고객 자체 생성 키 기반의 SSE `SSE-C`
- AWS KMS 기반의 SSE `SSE-KMS`

### S3 접근성 통제

접근성 통제 `Access Control`이란 S3 버킷에 누가, 어떻게 접근 할 것인지 정의하는 것이다. 이를 통해 S3에 저장된 객체에 대해 매우 세분화된 통제가 가능하다.

#### 접근 정책

IAM을 통해 세분화된 통제가 가능하다. 특정 버킷에 접근을 허용하거나 일부 회원만 접근하도록 만들 수 있다.
접근 제어 정책을 작성하기 위해서는 ARN을 알아야한다. ARN은 AWS 리소스를 위한 유일무이한 이름이며 AWS 사용자는 IAM 정책 수립과 API 호출 등 특정 리소스에 접근하기 위해서 해당 이름을 정확히 알고 있어야한다.

ARN은 아래와 같은 형식을 가진다.

```
arn:partition:service:region:account-id:resource
arn:partition:service:region:account-id:resourcetype/resource
```

참고로 partition은 해당 리소스가 포함된 파티션을 의미하며 표준 AWS 리전에서 partition은 aws이다.

```
arn:aws:s3:::test-pkch
```

#### 버킷 정책

버킷 정책이란 버킷 레벨에서 생성한 정책을 의미하며 S3 버킷을 세분화된 방식으로 제어할 수 있게 해준다. IAM 없이 버킷 정책 만으로도 상황에 따른 사용자 접근 제한이 가능하며 다른 AWS 계정이나 IAM 유저에게 특정 객체 또는 폴더의 접근 권한 부여가 가능하다.

```json
{
  "Id": "Policy1622386293295",
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "Stmt1622386291375",
      "Action": [
        "s3:GetObject"
      ],
      "Effect": "Allow",
      "Resource": "arn:aws:s3:::test-pkch",
      "Principal": "*"
    }
  ]
}
```

다음과 같이 정책을 작성할 수 있다. 정책은 JSON 형식이다.

위 정책은 `test-pkch` 버킷에 read-only만 허용하는 정책이다. Condition을 추가하여 허용할 ip나 허용하지 않을 ip도 추가할 수 있다.

그외에 MFA 인증을 거치게 하거나 AWS CloudFront를 통한 접근만 허용하게 만들수도 있다.

AWS 정책 생성기 참고: [https://awspolicygen.s3.amazonaws.com/policygen.html](https://awspolicygen.s3.amazonaws.com/policygen.html)

#### 접근 제어 목록 `ACL`

각각의 버킷과 그 속에 포함된 객체는 ACL과 연동된다. 따라서 ACL로 S3 버킷이나 객체의 접근을 제어하는게 가능하다. 단, ACL은 IAM이나 버킷 정책에 비해 더 넓은 범위에서 제어할 수 있으며 단지, 접근 승인을 한 곳과 접근 승인을 받은 곳으로만 나타낼 수 잇다.

### AWS S3 스토리지 클래스

AWS S3는 다양한 상황에 대응할 수 있도록 다양한 스토리지 클래스를 제공한다. 상황에 따라 필요한 스토리지 클래스를 사용할 수 있고 하나의 스토리지 클래스에서 다른 클래스로 데이터를 이동할 수도 있다.

참고로 객체를 생성할 때 객체의 스탠다드 클래스를 설정할 수 있으며 객체 라이프사이클을 설정하여 스토리지 클래스를 변경할 수 있다.

AWS S3 스토리지 클래스는 다음과 같다.

- AWS S3 스탠다드

    AWS S3 스탠다드는 AWS의 기본형 스토리지이며 빈번하게 접근하는 데이터를 위한 고신뢰성, 고가용성, 고성능을 제공한다.

- AWS S3 스탠다드 IA

    Infrequent Access의 약자로 다른 클래스에 비해 상대적으로 접근 빈도가 낮은 데이터를 위한 스토리지 클래스이다. 스탠다드와 동일하게 고가용성, 고신뢰성, 고성능을 제공한다.

    접근 빈도가 낮은만큼 스탠다드에 비해 비용이 매우 저렴해서 경제성이 중요한 장기저장, 백업, 재해 복구와 같은 목적으로 활용할 수 있다.

- AWS S3 RRS

    반복작업 감소 스토리지의 의미를 가지는 스토리지 클래스이다. 중요성이 높지 않고 기업 생산성과 직접적인 연관성이 낮은 데이터를 저장하는데 사용한다.

    만약 비디오의 해상도에 따라 저장할 필요가 있을때 원본은 스탠다드 클래스 S3에 저장하고 1080p, 720p, 480p와 같이 다양한 파일을 반복적으로 저장할 필요가 있을때 사용한다.

    > 원래 RRS는 스탠다드보다도 저렴한게 장점이지만 최근에는 S3 스탠다드가 더더욱 저렴하기 때문에 현재는 레거시 스토리지로 인식
    비용 절감을 위해서는 AWS S3 스탠다드 IA를 사용하는 것이 효율적이다.

- AWS S3 One Zone IA

    평소에는 낮은 빈도로 접근하지만, 때에 따라서 매우 신속하게 접근할 수 있는 스토리지 클래스이다. 스탠다드 클래스와는 달리 단일 AZ에 저장되지만 스탠다드 클래스와 동일하게 고가용성, 고신뢰성, 고성능을 제공한다.

    > 단일 AZ에만 데이터를 저장하므로 AZ 장애에는 취약하다.

    가격은 스탠다드 클래스나 스탠다드 IA 클래스보다 20% 저렴하다.

- 아마존 클래이셔

    아마존 글래이셔는 데이터 아카이브 목적으로 활용된다. S3와 마찬가지로 99.999999999%의 신뢰성을 제공하고 데이터 전송 및 저장시 SSL 암호화 기능을 제공한다. 아카이빙 목적의 클래스이므로 가격도 타 클래스 대비 매우 저렴하다.

#### AWS S3 객체 버전 관리

버저닝 `versioning`은 동일한 파일의 다양한 업데이트를 관리하는 방법이다. 버저닝 기법을 통해서 동일 파일의 서로 다른 10가지 버전을 업로드 할 수 있으며 이 파일들 모두 S3에 저장된다. 버저닝된 파일은 모두 고유한 버전 번호를 할당받지만 특정 파일을 찾을 때는 단 하나의 파일만 찾는다.

버저닝은 보험과 같은 역할을 하며 파일을 안전하게 보호해주는 역할도 한다. 버저닝을 통해 파일을 보존하고 인출하는 것뿐만 아니라 모든 버전의 파일을 안전하게 복원하도록 돕는다.

새로운 파일 뿐만 아니라 기존 파일에 PUT/POST/COPY/DELETE 등의 작업이 이뤄질때도 복원 가능 하도록 버저닝을 지원한다.

GET은 기본적으로 최신 버전의 파일을 가져오며 이전 버전의 파일이 필요하다면 GET 요청시 세부적인 버전 정보를 추가하면 된다.

```json
aws s3api get-object --bucket DOC-EXAMPLE-BUCKET --key example.txt --version-id example.d6tjAKF1iObKbEnNQkIMPjj
```

> 위와 같이 `--version-id`를 주어 해당 버전의 파일을 조회할 수 있다.

#### AWS S3 객체 라이프사이클 관리

라이프사이클을 사용하여 다음 주요 임무를 할 수 있다.

- 파일 이동

    서로 다른 클래스 간에 객체를 이동시키는 규칙을 추가할 수 있다. 예를 들어 로그 파일 생성 일주일 후 S3 스탠다드 IA 클래스로 이동시킬 수 있다.

- 파일 소멸

    객체가 소멸된 후의 사항을 정의할 수 있다. S3에서 파일 삭제한 경우, 삭제한 파일을 일정 기간 동안 별도의 폴더에 임시 보관하는 규칙을 추가할 수 있다.

#### AWS S3 크로스 리전 복제

기본적으로 한 리전의 S3 저장된 객체는 다른 리전에 저장되지 않는다. 다만 크로스 리전 복제를 사용한다면 복제가 가능하다. 이를 사용하기 위해서는 크로스 리전 복제 기능을 활성화 해야 한다.

> 크로스 리전 복제를 활용하기 위해서는 복제 규칙 생성이 필요하다. 복제 규칙 생성은 기본적으로 버저닝이 활성화 되어 있어야한다.
> 

#### AWS S3 정적 웹사이트 호스팅하기

AWS S3에서 바로 정적 웹사이트를 호스팅 할 수 있다. 버킷의 **정적 웹 사이트 호스팅 편집** 설정을 통해 호스팅 설정이 가능하다. 이때 웹 브라우저로 접속한 사용자들이 버킷의 객체에 접근할 수 있도록 버킷 정책을 열어두어야한다.

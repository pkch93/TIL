# 2021.06.05 TIL - AWS S3 관련 보충

> [2021.05.30 TIL](./../may/Day30.md)의 보충

## S3 일관성 모델

S3는 기존에는 종국적 일관성 모델 `eventually consistent`를 지원했다. 종국적 일관성 모델에서는 데이터가 자동으로 복제되어 다수의 시스템과 AZ로 확산되므로 최신의 내용으로 변경한 내용이 즉각적으로 반영되지 않거나, 업데이트 직후 데이터를 읽으려 할 때 변경된 내용을 확인할 수 없게 될 가능성이 있다.

최근에는 기록 후 판독 일관성 `read-after-write consistent`을 제공한다. 이를 통해 변경한 내용이 기록된 이후에만 변경된 내용을 조회할 수 있도록 보장한다.

S3가 고신뢰성을 위해 여러 AZ에 저장하기 때문에 변경이 모두 완료된 후에만 변경된 내용을 조회할 수 있는 기록 후 판독 일관성을 제공한다. 이는 고신뢰성을 위해 여러 AZ에 복제하는 경우에 보통 기록 후 판독 일관성을 제공한다. `ex. EFS`

참고: https://aws.amazon.com/ko/blogs/aws/amazon-s3-update-strong-read-after-write-consistency/

## Hex Hash prefix

S3는 객체의 키를 통해 파티셔닝을 한다. 기본적으로 키의 첫번째 값이 파티셔닝의 기준이 되는데 이외에 Hex Hash prefix도 파티셔닝 방법으로 적용할 수 있다.

S3에서는 Hex Hash를 partition-enabling hash로 판단하여 파티셔닝하는 것으로 보인다.

참고: https://aws.amazon.com/ko/blogs/aws/amazon-s3-performance-tips-tricks-seattle-hiring-event/

## S3 암호화

AWS S3가 저장된 데이터를 암호화하고 암호화 키 또한 관리한다. `SSE-S3`는 각각의 객체를 유니크한 키로 암호화하며 추가적인 보호 장치로 정기적으로 변경되는 마스터키를 활용하여 키 자체를 암호화한다. `SSE-S3`는 `AES-256`으로 키를 암호화한다.

> 기존에는 `SSE-SE`라는 이름이었던 걸로 보인다.

`SSE-S3`로 객체를 암호화하는 것은 추가적인 과금이 있는건 아니다. 다만, `SSE-S3`로 설정을 하거나 S3로 요청을 하는 경우 과금이 발생할 수 있다.

`SSE-S3`로 객체를 암호화하는 것은 추가적인 과금이 있는건 아니다. 다만, `SSE-S3`로 설정 요청이나 사용 요청은 과금이 발생할 수 있다.

만약 현재 버킷의 모든 객체를 암호화해야한다면 버킷 정책을 수정한다.

```json
{
  "Version": "2012-10-17",
  "Id": "PutObjectPolicy",
  "Statement": [
    {
      "Sid": "DenyIncorrectEncryptionHeader",
      "Effect": "Deny",
      "Principal": "*",
      "Action": "s3:PutObject",
      "Resource": "arn:aws:s3:::test-sse-s3-pkch/*",
      "Condition": {
        "StringNotEquals": {
          "s3:x-amz-server-side-encryption": "AES256"
        }
      }
    },
    {
      "Sid": "DenyUnencryptedObjectUploads",
      "Effect": "Deny",
      "Principal": "*",
      "Action": "s3:PutObject",
      "Resource": "arn:aws:s3:::test-sse-s3-pkch/*",
      "Condition": {
        "Null": {
          "s3:x-amz-server-side-encryption": "true"
        }
      }
    }
  ]
}
```

위와 같이 `s3:x-amz-server-side-encryption`를 설정해둔다.

SSE-S3 docs: [https://docs.aws.amazon.com/AmazonS3/latest/userguide/UsingServerSideEncryption.html](https://docs.aws.amazon.com/AmazonS3/latest/userguide/UsingServerSideEncryption.html)

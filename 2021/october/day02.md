# 2021.10.02 TIL - Amazon SNS 메세지 필터 정책과 원시 메세지 전송 활성화

- [2021.10.02 TIL - Amazon SNS 메세지 필터 정책과 원시 메세지 전송 활성화](#20211002-til---amazon-sns-메세지-필터-정책과-원시-메세지-전송-활성화)
  - [메세지 필터 정책](#메세지-필터-정책)
  - [Amazon SNS Raw Message Delivery](#amazon-sns-raw-message-delivery)

## 메세지 필터 정책

Amazon SNS는 메세지 속성을 통해 각 구독의 메세지를 필터링할 수 있도록 메세지 필터 정책을 지원한다.

<img width="980" alt="sns_message_filter" src="https://user-images.githubusercontent.com/30178507/135744449-8949a592-9fd4-420f-b7f0-133afbb8ee7f.png">

SNS의 구독을 보면 구독 필터 정책에서 메세지 필터 정책을 설정할 수 있다.
JSON 형식으로 설정하며 정책으로 지정한 속성이 모두 메세지 속성에 포함이 되어있어야만 메세지를 받을 수 있다.

```json
{
  "type": [
    "B"
  ]
}
```

만약 위와 같이 구독 필터 정책 `메세지 필터 정책`을 설정하면 메세지 속성의 타입중 type이 B인 메세지가 있어야만 메세지를 받을 수 있다.

참고: [https://docs.aws.amazon.com/ko_kr/sns/latest/dg/sns-subscription-filter-policies.html](https://docs.aws.amazon.com/ko_kr/sns/latest/dg/sns-subscription-filter-policies.html)

> 참고로 원시 메세지 전송 활성화와 함께 적용해도 문제없이 동작한다. 

## Amazon SNS Raw Message Delivery

SNS가 전송하는 메세지의 JSON 포메팅 과정을 피하고 싶은 경우가 있다. 이를 위해 Amazon SNS는 raw message delivery 기능을 제공한다. `원시 메세지 전송 활성화`

<img width="1066" alt="sns_raw_message_delivery" src="https://user-images.githubusercontent.com/30178507/135744436-aff7bd41-0f02-4b45-8e51-a8f1473162dd.png">

위 화면에서 `원시 메세지 전송 활성화`를 체크하여 활성화 할 수 있다.

```json
{
  "Type": "Notification",
  "MessageId": "dc1e94d9-56c5-5e96-808d-cc7f68faa162",
  "TopicArn": "arn:aws:sns:us-east-2:111122223333:ExampleTopic1",
  "Subject": "TestSubject",
  "Message": "This is a test message.",
  "Timestamp": "2021-02-16T21:41:19.978Z",
  "SignatureVersion": "1",
  "Signature": "FMG5tlZhJNHLHUXvZgtZzlk24FzVa7oX0T4P03neeXw8ZEXZx6z35j2FOTuNYShn2h0bKNC/zLTnMyIxEzmi2X1shOBWsJHkrW2xkR58ABZF+4uWHEE73yDVR4SyYAikP9jstZzDRm+bcVs8+T0yaLiEGLrIIIL4esi1llhIkgErCuy5btPcWXBdio2fpCRD5x9oR6gmE/rd5O7lX1c1uvnv4r1Lkk4pqP2/iUfxFZva1xLSRvgyfm6D9hNklVyPfy+7TalMD0lzmJuOrExtnSIbZew3foxgx8GT+lbZkLd0ZdtdRJlIyPRP44eyq78sU0Eo/LsDr0Iak4ZDpg8dXg==",
  "SigningCertURL": "https://sns.us-east-2.amazonaws.com/SimpleNotificationService-010a507c1833636cd94bdb98bd93083a.pem",
  "UnsubscribeURL": "https://sns.us-east-2.amazonaws.com/?Action=Unsubscribe&SubscriptionArn=arn:aws:sns:us-east-2:111122223333:ExampleTopic1:e1039402-24e7-40a3-a0d4-797da162b297"
}
```

만약 raw message delivery 기능을 사용하지 않는 경우 위와 같이 Type, MessageId 등 SNS 메세지의 metadata를 포함한 메세지를 각 구독 주체 `SQS, HTTP endpoint, Kinesis 등`가 전달받게된다.

원하는 값은 `Message` 인데 Message만 받아서 사용하려면 raw message delivery를 활성화해야한다. 만약 활성화하는 경우 위 메세지는 다음과 같이 구독 주체에게 전달한다.

```json
This is a test message.
```

즉, `Message`의 내용만 전달한다.

참고: [https://docs.aws.amazon.com/ko_kr/sns/latest/dg/sns-large-payload-raw-message-delivery.html](https://docs.aws.amazon.com/ko_kr/sns/latest/dg/sns-large-payload-raw-message-delivery.html)
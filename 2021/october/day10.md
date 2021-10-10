# 2021.10.10 TIL - Redis Pub/Sub

- [2021.10.10 TIL - Redis Pub/Sub](#20211010-til---redis-pubsub)
  - [관련 명령어](#관련-명령어)
  - [참고](#참고)

Redis는 Redis Pub/Sub이라는 기능을 사용하여 메세지 브로커로써의 기능도 제공한다.
따라서 구독 `SUBSCRIBE`, 구독 해지 `UNSUBSCRIBE`, 메세지 전송 `PUBLISH` 명령이 존재한다.

## 관련 명령어

- SUBSCRIBE
    
    특정 채널 `토픽`에 구독하기 위해 사용하는 명령어
    
    ```
    SUBSCRIBE foo bar
    ```
    
    위와 같이 `SUBSCRIBE` 명령어를 입력하면 foo와 bar 채널에 구독한다는 의미가 된다.
    구독 이후 위 채널로 보내진 `PUBLISH` 메세지들은 구독된 **모든 클라이언트**에게 전달된다.
    
- PSUBSCRIBE
    
    패턴에 일치하는 채널에 구독하는 명령어
    
    `?`, `*`, `[]` 패턴을 지원하며 `?` 는 하나의 문자, `*` 는 여러 문자, `[]`는 해당하는 문자에 대한 패턴을 의미한다.
    
    `h?llo`는 `hallo / hxllo / hello`, `h*llo`는 `heeeello`, `h[ae]llo`는 `[]` 사이에 있는 `a`와 `e` 만 허용한다. 즉, `hallo / hello`를 구독한다.
    
- UNSUBSCRIBE
    
    `SUBSCRIBE`나 `PSUBSCRIBE`로 구독된 채널을 구독 해제하는 명령어이다.
    
    ```
    UBSUBSCRIBE foo bar
    ```
    
    위와 같이 명령하면 구독된 foo와 bar 채널의 구독을 해제한다.
    
- PUNSUBSCRIBE
    
    `PSUBSCRIBE`와 마찬가지로 패턴에 일치하는 채널의 구독을 헤제하는 명령어이다.
    
- PUBLISH
    
    구독한 채널에 메세지를 발행하는 명령어.
    참고로 Redis Cluster에 대해서 PUBLISH를 하면 클러스터의 모든 노드에 메세지를 전달한다.
    
- PUBSUB
    
    Redis 2.8.0 버전부터 지원하는 명령어이다. Redis Pub/Sub 시스템의 상태를 확인하기 위해 지원하는 명령어이다.
    
    `CHANNELS`, `NUMSUB`, `NUMPAT` 이라는 서브 명령어가 있으며 서브명령어와 함께 사용해야한다.
    
    ```
    PUBSUB <subcommand> ...args
    ```
    
    위와 같이 PUBSUB 명령을 사용할 수 있다.
    
    참고로 PUBSUB 명령을 Redis Cluster에 사용하는 경우 Redis Cluster에 구성된 모든 노드에 대한 정보를 주는 것이 아니라 해당 클라이언트가 통신하고 있는 Redis Node에 대한 정보를 반환한다.
    

> 참고로 redis-cli는 Subscribe 모드에서 한 번만 명령을 받아들이지 않고 Ctrl-C로 Subscribe 모드를 종료할 수 있다. 

## 참고

[https://redis.io/topics/pubsub](https://redis.io/topics/pubsub)

[https://charsyam.wordpress.com/2013/03/12/입-개발-redis-pubsub-시스템은-일반적인-message-queue와-다르다/](https://charsyam.wordpress.com/2013/03/12/%ec%9e%85-%ea%b0%9c%eb%b0%9c-redis-pubsub-%ec%8b%9c%ec%8a%a4%ed%85%9c%ec%9d%80-%ec%9d%bc%eb%b0%98%ec%a0%81%ec%9d%b8-message-queue%ec%99%80-%eb%8b%a4%eb%a5%b4%eb%8b%a4/)
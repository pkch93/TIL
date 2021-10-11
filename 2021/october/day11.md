# 2021.10.11 TIL - Spring Data Redis PubSub

Spring Data Redis를 사용하면 PubSub 기능을 지원하는 각종 클래스들을 이용할 수 있다.

## Subscribe

일반적으로 Redis를 사용하는 것처럼 먼저 RedisConnectionFactory 설정이 필요하다.

```java
@Bean
public RedisConnectionFactory redisConnectionFactory() {
    return new LettuceConnectionFactory();
}
```

그리고 구독할 채널과의 연결과 메세지의 전달을 관리하는 RedisMessageListenerContainer 정의가 필요하다.

```java
@Bean
public RedisMessageListenerContainer redisMessageListenerContainer() {
    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(redisConnectionFactory());
    container.addMessageListener(messageListener(), topic());

    return container;
}
```

이때 `addMessageListener` 부분이 해당 토픽에 메세지를 전달받았을 때 어떤 로직을 처리할 지 정의하는 `MessageListener`와 토픽 `ChannelTopic`을 설정하는 부분이다.

`MessageListener`는 아래와 같이 메세지를 어떻게 처리할지 정의하는 `onMessage` 메서드로 구성되어있다.

```java
public interface MessageListener {

    void onMessage(Message message, @Nullable byte[] pattern);
}
```

그리고 필요하다면 `org.springframework.data.redis.listener.adapter.MessageListenerAdapter`도 지원한다. 만약 delegate로 전달할 MessageListener 없이 그냥 사용한다면 기본적인 MessageListener로써의 역할을 한다.

마지막으로 `org.springframework.data.redis.listener.ChannelTopic`은 구독하고자하는 토픽을 정의하는 클래스이다.

위 클래스들을 사용하여 RedisMessageListenerContainer를 정의하면 구독에 대한 설정은 끝이다.

## Publish

Publish를 위해서 `RedisTemplate#convertAndSend`을 사용할 수 있다. 만약 Spring Messaging 모듈을 사용하는 경우라면 `org.springframework.messaging.simp.SimpMessagingTemplate`을 활용할 수 있다.

## 참고

redis pubsub support: [https://docs.spring.io/spring-data/data-redis/docs/current/reference/html/#pubsub](https://docs.spring.io/spring-data/data-redis/docs/current/reference/html/#pubsub)

redis reactive pubsub support: [https://docs.spring.io/spring-data/data-redis/docs/current/reference/html/#redis:reactive:pubsub](https://docs.spring.io/spring-data/data-redis/docs/current/reference/html/#redis:reactive:pubsub)

baeldung PubSub Messaging with Spring Data Redis: [https://www.baeldung.com/spring-data-redis-pub-sub](https://www.baeldung.com/spring-data-redis-pub-sub)

spring data redis pub/sub: [https://brunch.co.kr/@springboot/374](https://brunch.co.kr/@springboot/374)
# 2021.10.05 TIL - WebClient DataBufferLimitException

WebClient는 기본적으로 buffer size가 256KB로 고정되어 있다. 때문에 응답이 256KB 이상인 경우 `DataBufferLimitException`이 발생한다.

이를 해결하기 위해서 WebClient를 생성할 때 codec 쪽 설정을 수정해야한다.

```java
ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
    .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1)) 
    .build(); 

this.webClient = webClientBuilder 
    .exchangeStrategies(exchangeStrategies)
    .build();
```

위와 같이 ExchangeStrategies를 설정하여 MemorySize를 설정해주어야한다. -1로 설정하면 unlimited로 설정이 가능하다.

참고: [https://garyj.tistory.com/22](https://garyj.tistory.com/22)
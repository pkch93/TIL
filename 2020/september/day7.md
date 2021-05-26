# Day7

## 20.09.07 Monday - redis 운영상 주의점 \(1\)

참고: [https://sehajyang.github.io/2019/12/11/how-to-operate-redis/](https://sehajyang.github.io/2019/12/11/how-to-operate-redis/) Toast 기술블로그: [https://meetup.toast.com/posts/227](https://meetup.toast.com/posts/227)

## 메모리 관리 잘하자!

메모리를 실제 메모리 사용량 이상으로 사용하게 되면 swap을 사용하게 된다. swap은 디스크 영역을 사용하는 것으로 매우 느려진다. 따라서 swap을 사용하지 않도록 메모리 관리하는 것이 중요. 보통 Redis가 느려지는 이유가 바로 swap을 사용하기 때문이다.

큰 메모리를 사용하는 하나의 Redis 인스턴스보다는 적은 메모리의 여러 Redis 인스턴스를 사용하는 것이 안전하다.

### Maxmemory를 설정하더라도 이보다 더 사용할 가능성이 크다.

> maxmemory의 권장 값은 가용 메모리의 60~70%이다.

Redis는 메모리 allocator에 의존하는데 `jemaloc` 이는 1바이트만 사용한다고 해도 페이지 단위에 따라 메모리를 할당하므로 4KB를 할당하게 된다. 이처럼 메모리 파편화가 일어나는 경우 레디스 사용량과 jemaloc 할당량이 달라진다.

Redis는 4버전부터 메모리 파편화가 일어날 수 있으므로 주의가 필요하다.

메모리를 효율적으로 사용하기 위해서는 Hash, Sorted Set, List보다 가능하다면 ZipList를 사용하는 것이 보다 효율적으로 메모리를 사용할 수 있다. `30%까지 메모리를 아낄 수 있다.`

### RSS 값 모니터링 필요

rss란 운영체제에서 봤을 때 Redis가 할당한 byte의 수를 의미한다.

특히 Redis는 메모리 파편화가 발생하기 쉬우므로 항상 RSS 실제 물리 메모리 사용량을 모니터링한 후 어느 수준의 증가가 보인다면 다른 장비로 데이터를 이전해야한다.


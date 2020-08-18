# 2020.08.18 Tuesday - redis

In-Memory Data Structure Store `BSD 3 License - 오픈소스`

다양한 자료구조들을 지원한다. `Strings, set, sorted-set, hashes, list, Hyperloglog, bitmap, geospatial index, Stream`

Remote Data Store로서 여러 서버에서 데이터를 공유할 때 주로 사용한다. (ex. Session 등으로 사용)
Redis는 싱글스레드로 동작하여 Atomic을 보장한다.
주로 인증 토큰 저장, Ranking 보드 `Sorted Set`, 유저 API Limit 등에 사용한다.

## Data Structure

redis data-type: [https://redis.io/topics/data-types](https://redis.io/topics/data-types)

Redis는 단순 key-value 저장소가 아닌 value에 다양한 형태의 자료구조를 지원하는 서버이다. 즉, 전통적인 key-value 저장소에서 벗어나 여러 복잡한 자료구조의 형태를 value로 가질 수 있는 데이터 저장소이다.

### Redis Keys

Redis는 key-value 저장소이다. 따라서 Redis에 값을 저장하기 위해서는 무조건 key를 지정해야한다. Redis 공식문서에서는 key를 지정하기 위한 몇몇 룰을 소개하고 있다.

1. 너무 긴 key는 자제할 것.

    너무 긴 key는 메모리 낭비일 뿐 아니라 데이터에서 키를 탐색할 때 오버헤드를 불러일으킨다. 만약 어쩔 수 없이 크기가 큰 key를 사용해야 한다면 해싱을 추천한다.

2. 매우 짧은 key도 자제.

    매우 짧은 key는 가독성 측면에서 문제를 일으킬 수 있다. 짧은 key가 메모리를 작게 잡아먹음은 분명하지만 가독성을 해칠 정도로 너무 짧은 key는 피할 것.

3. 스키마를 고정시킬 것.

    예를 들어 `object-type:id` 방식이 있다. 이 양식을 따른다면 `user:1000`과 같이 key로 사용할 수 있다. redis에서는 multi-words 필드를 사용할 때는 `.`이나 `-`를 주로 사용한다. `comment:1234:reply.to or comment:1234:reply-to`

4. key 길이는 512MB를 넘지말 것.

    key의 자료구조가 string이므로 최대 512MB까지 지원한다.

### Redis Data Type

- Strings `Key/Value`

    > binary-safe strings
    참고로 binary-safe String은 길이가 정해지지 않은 문자열을 구현하기 위해 사용하는 방법이며 Redis에서는 한 String에 512MB까지 지정할 수 있다.

    참고: [https://stackoverflow.com/questions/44377344/what-is-a-binary-safe-string](https://stackoverflow.com/questions/44377344/what-is-a-binary-safe-string)

    Key-Value 형태로 값을 저장하는 형태. 이때 Key를 어떻게 저장할 지가 중요하다.
    prefix를 앞으로 붙일지, 뒤로 붙일지 등으로 key의 분산이 바뀔 수 있다.

    > 아마 key 분산이 중요한 이유는 redis cluster에서 key의 범위를 기반으로 노드 할당을 지원하기 때문으로 보임. `좀더 서치 필요`
    [http://redisgate.kr/redis/cluster/cluster_introduction.php](http://redisgate.kr/redis/cluster/cluster_introduction.php)

    string은 value의 타입으로 많이 사용되는 타입이다. HTML을 캐싱하는 등으로 유용하게 사용되고 있다.

    ![](https://user-images.githubusercontent.com/30178507/90517500-6f818e80-e1a0-11ea-8248-c65d16464ec2.png)

    위와 같이 `set <key> <value>`로 값을 등록하고 `get <key>`로 값 조회를 할 수 있다.
    이때 주의할 점은 `set` 명령을 했을 때 이미 등록된 key를 대상으로 한다면 해당 값을 대체한다.

    ![](https://user-images.githubusercontent.com/30178507/90517538-7f00d780-e1a0-11ea-8b30-c92d361fb1f2.png)

    그리고 set 명령에는 특별한 옵션이 있다. set은 기본적으로 이미 존재하는 키가 있다면 value를 대체한다. 이때 `nx` 옵션으로 이미 존재하는 키가 있다면 대체하지 못하도록 만들 수 있다.

    ![](https://user-images.githubusercontent.com/30178507/90517574-8b853000-e1a0-11ea-9704-df8549af6bb3.png)

    이렇게 set 명령 뒤에 `nx`를 넣어주는 경우 이미 있는 값이면 `nil`이 나타나게 된다.

    value는 모든 종류의 string `binary 포함`을 지원한다. 즉, 512MB를 넘지 않는 jpeg 이미지 등도 저장이 가능하다.

    또한 `mset`, `mget`으로 여러 `key-value`를 저장, 조회할 수 있다.

    ![](https://user-images.githubusercontent.com/30178507/90517602-95a72e80-e1a0-11ea-9873-21c08d431e13.png)

    위와 같이 `mset <key1> <value1> <key2> <value2> ... <keyN> <valueN>`으로 여러 `key-value`를 저장할 수 있다.

    마찬가지로 `mget <key1> <key2> ... <keyN>`으로 여러 key의 값을 조회할 수 있다. 위 예시에서 이전에 저장했던 name도 함께 조회할 수 있음을 알 수 있다.

    참고로 mget을 사용하면 Redis에서는 array로 값을 전달한다.
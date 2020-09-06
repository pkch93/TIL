# 20.09.06 Sunday - Redis Data Structure (Hash, Sets, Sorted Sets, Bitmap, HyperLogLogs)

- Hash

    Hash는 Objects를 표현하기 위한 방법이다. hash에는 메모리가 허용하는 한계까지 `즉, 무한대` 값을 넣을 수 있다. Hash는 field-value 쌍으로 이뤄진다.

    - Hash에 여러 값을 넣을때는 `HMSET`

        ```
        hmset <key> <field> <value> {...<field> <value>}
        ```

        위와 같이 hash로 값을 넣을때 하나의 key와 해당 key에 담을 field-value 쌍을 넣어준다.

        ![https://s3-us-west-2.amazonaws.com/secure.notion-static.com/d8f994ea-ed0a-46df-9392-17a68a67942c/_2020-09-06__6.53.41.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/d8f994ea-ed0a-46df-9392-17a68a67942c/_2020-09-06__6.53.41.png)

        위와 같이 `person:1`이라는 key에 `name-pkch`, `age-28`로 값을 넣을 수 있다.

    - Hash 값 조회에는 `HGET`과 `HMGET`

        Hash를 조회하기 위한 명령으로는 `HGET`과 `HMGET`이 있다. `HGET`은 조회하려는 key의 하나의 field만 조회할 수 있는 반면 `HMGET`은 여러 field를 array형태로 조회하는 명령어이다.

        ![https://s3-us-west-2.amazonaws.com/secure.notion-static.com/8c1039d4-f242-45d2-8459-e2eeb0ddd65d/_2020-09-06__7.00.23.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/8c1039d4-f242-45d2-8459-e2eeb0ddd65d/_2020-09-06__7.00.23.png)

        위와 같이 `HGET`은 하나의 field의 value를 조회하는 반면 `HMGET`은 여러 필드의 값을 array로 조회할 수 있다.

        만약 없는 field를 조회한다면 `nil`을 반환한다.

- Sets

    Redis의 Sets은 HashTable을 사용하고 정렬이 되어있지 않은 strings 컬랙션이다.

    > 참고로 set에 들어가는 element를 member라고 한다.

    - Sets에 값 넣기 `SADD`

        ```
        sadd <key> <member> {...member}
        ```

        ![https://s3-us-west-2.amazonaws.com/secure.notion-static.com/a896e364-f1cd-40e3-8186-b2bbad61ca7f/_2020-09-06__7.19.17.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/a896e364-f1cd-40e3-8186-b2bbad61ca7f/_2020-09-06__7.19.17.png)

        위 예시는 set이라는 key에 `1,2,3` member를 넣은 명령이다.

        SMEMBERS: 해당 키에 저장된 모든 값 가져오기
        SISMEMBER: 존재하는지 안하는지 파악

    - 저장된 모든 값 조회하기 `SMEMBERS`

        ```
        smemebers key
        ```

        ![https://s3-us-west-2.amazonaws.com/secure.notion-static.com/e01fea58-047e-4063-ac2a-d3c51e5cbc5a/_2020-09-06__7.26.00.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/e01fea58-047e-4063-ac2a-d3c51e5cbc5a/_2020-09-06__7.26.00.png)

        위 예시는`sadd`로 저장한 set이라는 key를 조회하는 명령이다. 

    - Sets에 값이 있는지 확인하기 `SISMEMBER`

        ```
        sismember key member
        ```

        해당 key에 member가 있는지 없는지 확인하기 위한 명령어이다.

        ![https://s3-us-west-2.amazonaws.com/secure.notion-static.com/31f433df-fcdb-4835-b2f6-e1f5d9f1d85f/_2020-09-06__9.26.56.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/31f433df-fcdb-4835-b2f6-e1f5d9f1d85f/_2020-09-06__9.26.56.png)

        위와 같이 해당 key에 값이 있다면 1, 없다면 0을 반환한다.

    - 다른 Set 사이의 교집합 구하기 `SINTER`

        ```
        sinter key <...key>
        ```

        위와 같이 여러 key와 함께 `SINTER`를 사용하면 해당 key를 가진 Sets들의 교집합을 구할 수 있다.

    - 랜덤으로 Sets의 값 뽑아내기 `SPOP`

        ```
        spop key count
        ```

        해당 key를 가진 Sets에서 count 만큼 값을 뽑아내는 Command

    - 다른 Sets에서 합집합 구하기 `SUNION`

        ```
        sunion key <...key>
        ```

        여러 key를 가진 Sets 사이의 합집합을 구하는 Command

        > 차집합의 경우는 `SDIFF`를 사용한다.

    - 다른 Sets의 합집합을 원하는 Sets에 저장하기 `SUNIONSTORE`

        ```
        sunionstore destination key <...key>
        ```

        key로 주어진 Sets들의 합집합을 destination의 Sets으로 저장하는 Command

        > 차집합의 경우는 `SDIFFSTORE`를 사용한다.

    - Sets 내부의 element 갯수를 알고싶을땐 `SCARD`

        ```
        scard key
        ```

        key의 Sets이 가진 element의 갯수를 반환한다.

    - Sets에서 값을 제거하지 않고 랜덤으로 하나 조회할 때는 `SRANDMEMBER`

        ```
        srandmember key <count>
        ```

        key의 Sets에서 count만큼 값을 가져오는 Command
        `SPOP`과 차이점은 조회하는 값을 Sets에서 제거하지 않는다는 점이다.

- Sorted Sets

    Sorted Sets는 Set과 Hash의 기능을 섞은 것과 유사하다. Sorted Sets은 Sets 내부의 element가 정렬이 되어있는 자료구조이다. 정렬의 기준은 모든 element에 매겨져있는 score를 기준으로 한다.

    Sorted Sets는 Skiplist와 HashTable을 이용하여 구현되었다. 따라서 sorted sets에 값을 추가할 때마다 `logn` 만큼의 시간복잡도를 가지는 연산이 수행된다. 때문에 많을 양을 update하는데 적합한 자료구조이다.

    이런 특징으로 Sorted Sets는 leader board를 구현하는데 적합하다. 

    - Sorted Sets에 값 넣기 `ZADD`

        ```
        zadd key <NX|XX> <CH> <INCR> score member <...score member>
        ```

        `ZADD`은 key, score, member를 필수적으로 주어야한다. 이때 Sets와 다른 차이점이 바로 score이다. 해당 member에 제공하는 score에 따라 정렬이 된다.

        그외 `ZADD`에는 다른 옵션들이 있다. 이 옵션들은 redis 3.0.2 이상에서 사용가능하다.

        - XX: 이미 있는 element에만 update만 한다. add 하지 않는다.
        - NX: 이미 있는 element라면 update를 하지 않는다. 오로지 새로운 값만 add한다.
        - CH: 몇개의 member가 수정되었는지 출력하는 옵션
        - INCR: ZADD가 ZINCRBY와 같이 동작하도록 하는 옵션이다. INCR 옵션에서는 오직 하나의 score-member에만 사용할 수 있다.
    - Sorted Sets에 있는 값들 조회하기 `ZRANGE` `ZREVRANGE`

        List의 `LRANGE`와 거의 비슷한 명령어이다.

        ```
        zrange key start stop <withscores>
        ```

        따라서 `zrange ss 0 -1` 과 같이 사용하면 ss라는 Sorted Sets에 있는 모든 값을 조회할 수 있다.

        또한 반대순서로 조회할 수 있는 기능도 Sorted Sets에서 제공해준다. 이는 `ZREVRANGE`로 가능하다. 사용방법은 `ZRANGE`와 동일하다.

        withscores 옵션은 element와 함께 score도 조회하는 기능이다.

    - Sorted Sets의 Score 범위를 활용한 명령어
        - `ZRANGEBYSCORE`

            ```
            zrangescore key start end
            ```

            start부터 end까지에 해당하는 score를 가진 element를 조회하는 명령이다.

            이때 사용할 수 있는 특수표현들이 존재한다.
            `inf`는 무한대를 의미하며 `-inf`로 마이너스 무한대를 `+inf`로 양수 무한대를 표현할 수 있다. 또한 그냥 숫자를 사용하면 해당 숫자를 포함한다는 의미인데 `(`와 함께 숫자를 사용하면 해당 숫자를 포함하지 않는다는 의미가 된다.

            ```
            ZRANGEBYSCORE myzset (1 2
            ```

            위 경우 `1 < score <= 2`의 의미가 된다.

        - `ZREMRANGEBYSCORE`

            또한 score 범위에 있는 값들을 지우고 싶을 수 있다. 이를 위해 `ZREMRANGEBYSCORE`를 지원한다.

            ```
            zremrangebyscore hackers 1940 1960
            ```

            다음과 같이 사용하면 hackers라는 Sorted Sets에서 1940 ~ 1960이라는 score를 가진 값들을 제거한다.

        참고로 Redis에서는 2.8버전 이후부터 lexicographical Scores를 지원한다. 즉, 사전식 Score를 지원한다. 이를 위해 `ZRANGEBYLEX`, `ZREVRANGEBYLEX`, `ZREMRANGEBYLEX`, `ZLEXCOUNT` 등의 명령어를 지원한다.

- Bitmaps

    Bitmap은 실제 data type은 아니지만 String type에 정의된 bit 대상의 연산 집합이다. 실제로 Redis에서 String은 binary safe하며 최대 512MB의 길이를 가질 수 있다.

    Bit 연산에는 하나의 single bit에 대한 연산과 bit 그룹에 대한 연산 2가지 그룹으로 나눌 수 있다.

    - single bit 연산 `SETBIT` `GETBIT`

        ```
        setbit key offset value<0 or 1>
        ```

        `SETBIT` 연사는 key의 element에 offset만큼 떨어진 bit에 value를 할당하는 Command이다.

        `setbit key 10 1`이라는 명령은 key에 10번째 떨어진 bit에 1을 할당한다는 의미이다.

        ```
        getbit key offset
        ```

        반면 `GETBIT`는 해당 key에 offset만큼 떨어진 bit의 값을 반환한다. 만약 `setbit key 10 1`이라는 명령 후에 `getbit key 10`을 호출하면 `1`이 나타나게 된다. `getbit key 1`을 호출하면 `0`이 나타난다.

    - bit group 연산 `BITOP` `BITCOUNT` `BITPOS`

        `BITOP`는 서로 다른 strings 간에 bit 연산을 하는 연산이다. `AND`, `OR`, `XOR`, `NOT` 연산을 할 수 있다.

        `BITCOUNT`는 해당 bit에 얼마나 1이 세팅되어있는지 알려주는 연산이다.

        `BITPOS`는 0 또는 1을 가진 첫번째 비트를 확인할 때 사용한다. 이때 `BITPOS`는 시작 비트와 끝 비트를 지정할 수 있다.

    Bitmaps의 장점 중 하나는 정보를 저장할 때 극도로 저장 공간을 아낄 수 있다는 것이다. 만약 다양한 user들의 incremental id를 저장한다고 가정할 때 하나의 bitmap으로 최대 40억의 user id를 기억할 수 있는 것이다. `512MB`

- HyperLogLogs

    HyperLogLogs는 유니크한 것을 카운트하기 위해 사용하는 확률적 자료구조이다. 기술적으로는 집합의 카디널리티를 추정하는 것을 의미한다. 일반적으로 이런 계산은 동일한 element를 똑같이 새지 않기 위해 elements의 갯수만큼 메모리를 소모해야하지만 Redis에서는 표준오차 1% 미만으로 표준 오차로 추정된 측정치로 끝나는 알고리즘을 사용한다. 이를 통해 최악의 경우 12KB를 사용하도록 설계되었다.

    이러한 종류의 자료구조는 매일 유저들이 수행하는 쿼리를 카운트하는데 주로 사용된다.

    HyperLogLogs의 연산들은 Sets를 사용하는 것과 같다.

    - 값 추가 `PFADD`

        HyperLogLogs에 값을 추가하는 방식은 `PFADD`를 호출하는 것이다. 이는 `SADD`와 동일하다.

        ```
        pfadd key element <...element>
        ```

    - 값의 갯수 조회 `PFCOUNT`

        ```
        pfcount key <...key>
        ```

        key에 존재하는 값들의 카디널리티를 측정하여 반환한다.

    - 여러 HyperLogLogs에 있는 값들을 합쳐서 저장 `PFMERGE`

        ```
        pfmerge destination key <...key>
        ```

        N개의 HyperLogLogs의 값들을 하나로 합쳐서 destination에 해당하는 key에 저장하는 Command
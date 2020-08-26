# 2020.08.26 Wendesday - redis list

> 우아한 타입스크립트 강의 시청, 정리 후 곧 올릴 예정

## List

참고로 List라는 자료구조는 순서를 가지는 값들의 집합이다.

기본적으로 Redis에서는 Linked List를 사용한다. 즉, 삽입, 삭제에 강력한 성능을 보인다. 반면, 조회 성능에 단점을 가진다. Array로 구현된 List라면 인덱스를 통해 O(1)의 시간복잡도를 가지지만 Linked List로 구현된 List는 O(n)의 시간복잡도를 가진다.

- 값을 List에 삽입할 때 `lpush와 rpush`

    lpush는 원소를 List의 head `앞부분`에서부터 삽입할 때, rpush는 tail `뒷부분`에서부터 삽입할 때 사용한다.

    ![https://user-images.githubusercontent.com/30178507/91317027-7dfd2500-e7f4-11ea-8d2b-0356f2122848.png](https://user-images.githubusercontent.com/30178507/91317027-7dfd2500-e7f4-11ea-8d2b-0356f2122848.png)

    위와 같이 lpush로 `1, 2`를 넣고 rpush로 `3`을 넣는 경우 `2, 1, 3`의 결과를 볼 수 있다. 

    > 참고로 리스트의 값을 확인할 때는 `lrange 0 -1`로 할 수 있다.
    여기서 -1은 마지막을 의미한다.
    `rrange`는 존재하지 않음

- 값을 List에서 빼낼 때 `lpop과 rpop`

    Redis의 pop은 List에서 값을 빼냄과 동시에 빼낸 값을 반환하는 명령이다.
    push와 마찬가지로 `lpop`은 List의 head부분 부터 빼낼 때, `rpop`은 반대로 tail 부분부터 빼낼 때 사용한다.

    ![https://user-images.githubusercontent.com/30178507/91317490-1398b480-e7f5-11ea-98dc-dde50bf1888c.png](https://user-images.githubusercontent.com/30178507/91317490-1398b480-e7f5-11ea-98dc-dde50bf1888c.png)

    아까 `2, 1, 3`의 순서로 들어간 mylist에 `lpop`과 `rpop`을 한번씩 수행하면 각각 2와 3의 값이 도출되는 것과 mylist에 1만 남아있는 것을 확인할 수 있다.

    Blpop/Brpop: 데이터를 push하기 전까지 대기

- 여러 값 잘라내기 `ltrim`

    List는 위와 같이 값을 저장하는 용도로도 사용할 수 있지만 최신의 값들을 관리하는데 사용하기도 한다. Redis는 이런 기능의 List를 지원한다. 이를 사용하기 위해서는 `ltrim` 명령어가 필요하다.

    ![https://user-images.githubusercontent.com/30178507/91317540-24e1c100-e7f5-11ea-85b4-f1707afca0dd.png](https://user-images.githubusercontent.com/30178507/91317540-24e1c100-e7f5-11ea-85b4-f1707afca0dd.png)

    방금 1만 남은 mylist에 2부터 10까지 값을 `rpush`로 넣었다.

    > 참고로 여러 값을 입력하면 multi insert가 된다.

    `ltrim`은 간단하다. `lrange`가 시작 인덱스부터 끝 인덱스까지 값을 보여주는 반면 `ltrim`은 시작-끝 인덱스의 값을 제외하고는 모두 버린다. 따라서 `ltrim mylist 0 2` 명령으로 `1, 2, 3`만 mylist에 남게되었다.

    > `lrange`와 마찬가지로 `ltrim`도 `rtrim`은 존재하지 않는다.

- List의 Blocking Operation `blpop과 brpop`

    polling의 개념을 따르는 `pop` 명령이다.
    polling 위키 참고: [https://ko.wikipedia.org/wiki/폴링_(컴퓨터_과학)](https://ko.wikipedia.org/wiki/%ED%8F%B4%EB%A7%81_(%EC%BB%B4%ED%93%A8%ED%84%B0_%EA%B3%BC%ED%95%99))

    즉, 값이 없는 경우 기존의 `lpop`이나 `rpop`은 null을 반환한다. 이를 해소하기 위해 값을 `pop`할 수 있을때까지 `pop` 명령을 계속해서 시도하는 명령이다.

    단, `blpop`과 `brpop`은 이런 단점을 가지고 있다.

    1. List가 비어있는 경우 Redis와 client에게 쓸모없는 명령을 하도록 강요한다. 즉, 비어있는 List에 오는 모든 명령을 null로 반환한다.
    2. null을 수신한 이후에 일정 시간 기다리므로 지연을 유발한다. 또한 polling으로 쓸데없는 요청을 유발한다.

    `blpop`과 `brpop`은 다음과 같이 사용한다.

    ```
    blpop(또는 brpop) <list 명> <timeout>
    ```

    이때, timeout의 기본 시간 단위는 초이다.

    ![https://user-images.githubusercontent.com/30178507/91317696-5490c900-e7f5-11ea-83cd-7ae448efaf2d.png](https://user-images.githubusercontent.com/30178507/91317696-5490c900-e7f5-11ea-83cd-7ae448efaf2d.png)

    다음과 같이 `1, 2, 3`이 있는 mylist에 `brpop`을 사용하면 바로 마지막에 위치한 3을 반환한다.

    ![https://user-images.githubusercontent.com/30178507/91317754-64101200-e7f5-11ea-80d2-5252acb44481.png](https://user-images.githubusercontent.com/30178507/91317754-64101200-e7f5-11ea-80d2-5252acb44481.png)

    반면 아무것도 없는 mylist에 1초의 타임아웃 기간동안 아무 값이 없으면 null을 반환한다.

    ![https://user-images.githubusercontent.com/30178507/91317764-65d9d580-e7f5-11ea-9347-554f2a5c6c19.png](https://user-images.githubusercontent.com/30178507/91317764-65d9d580-e7f5-11ea-9347-554f2a5c6c19.png)

    만일 아무것도 없는 mylist에 100초의 타임아웃을 줄 경우 다음과 같이 block 상태로 기다린다.

    ![https://user-images.githubusercontent.com/30178507/91317769-683c2f80-e7f5-11ea-8d3a-af1e1c34918d.png](https://user-images.githubusercontent.com/30178507/91317769-683c2f80-e7f5-11ea-8d3a-af1e1c34918d.png)

    ![https://user-images.githubusercontent.com/30178507/91317782-6c684d00-e7f5-11ea-8acf-005393f546c4.png](https://user-images.githubusercontent.com/30178507/91317782-6c684d00-e7f5-11ea-8acf-005393f546c4.png)

    이때 mylist에 값을 넣어준다면 `brpop`이 동작하면서 block에서 풀리게된다.
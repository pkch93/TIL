# 2021.07.11 TIL - MVCC

- [2021.07.11 TIL - MVCC](#20210711-til---mvcc)
  - [MySQL의 MVCC 동작 방식](#mysql의-mvcc-동작-방식)
  - [참고](#참고)

MVCC `Multi Version Concurrency Control`란 락을 걸지 않고 읽기 작업을 수행할 수 있도록 만드는 원천 기능이다. MySQL에서는 InnoDB 스토리지 엔진부터 MVCC를 지원한다. 

> MVCC는 Oracle DB에서 지원하던 기능인데 이를 InnoDB에서 차용하여 구현되었다.

MVCC는 일반적으로 레코드 레벨의 트랜잭션을 지원하는 DBMS가 제공하는 기능이다. MVCC는 락을 사용하지 않으면서 일관된 읽기를 제공하는 것이 목적인데 InnoDB는 이를 Undo 로그를 통해 구현한다.

> 멀티 버전 `Multi Version` 이란 하나의 레코드에 대해 여러 개의 버전이 동시에 관리된다는 의미이다.

## MySQL의 MVCC 동작 방식

다음과 같이 member 테이블에 insert 문으로 아래와 같이 로우를 저장한다고 가정한다. 이때 DB의 트랜잭션 격리수준은 `READ_COMMITED`로 가정한다.

![](https://user-images.githubusercontent.com/30178507/125186825-7ce7b180-e267-11eb-9bcb-31e665543185.png)

그 경우 DB는 위와 같은 상태가 된다. 이때 위 `m_id`가 12인 로우에 Update 문으로 `m_area`를 `경기`로 바꿔본다.

![](https://user-images.githubusercontent.com/30178507/125186826-7e18de80-e267-11eb-8370-7e3a714c7b0d.png)

만약 Update 문이 실행되면 커밋 실행 여부와 상관없이 InnoDB 버퍼 풀은 새로운 값인 `경기`로 업데이트된다. 그리고 디스크의 데이터 파일은 업데이트 되어있을수도 있고 아닐수도 있다.

> InnoDB는 ACID를 보장하기 때문에 일반적으로 InnoDB의 버퍼 풀과 데이터 파일은 동일하다고 봐도 무방

이 상황에서 `m_id`가 12인 데이터를 조회하면 어떻게 될까?

> 아직 Commit이나 Rollback이 되지 않은 상태이다.

이는 MySQL의 트랜잭션 격리수준에 따라 결과가 달라진다. 만약 격리 수준이 `READ_UNCOMMITED`라면 InnoDB 버퍼 풀이나 데이터 파일에서 변경된 상태의 데이터를 반환한다.

반면 `READ_COMMITED`나 `REPEATABLE_READ`, `SERIALIZABLE`의 경우는 아직 커밋이 되지 않은 상태이므로 InnoDB의 버퍼 풀이나 디스크에 저장된 값이 아닌 변경 이전의 데이터를 가지고 있는 Undo 로그를 읽어 반환한다.

이런 과정을 MVCC라고 한다.

이 상태에서 Commit을 하면 InnoDB는 그대로 영구적인 데이터로 만든다. 커밋된다고 Undo 로그를 바로 삭제하는 것이 아니라 Undo 로그를 필요로하는 트랜잭션이 없을 때 삭제한다. 반면 Rollback을 하게되면 Undo에 백업된 데이터를 다시 InnoDB 버퍼 풀로 복구하고 Undo 로그에 추가했던 데이터를 삭제한다.

## 참고

Real MySQL 3.2.6: [http://www.kyobobook.co.kr/product/detailViewKor.laf?mallGb=KOR&ejkGb=KOR&barcode=9788992939003](http://www.kyobobook.co.kr/product/detailViewKor.laf?mallGb=KOR&ejkGb=KOR&barcode=9788992939003)
# 2021.08.11 TIL - MySQL InnoDB History

InnoDB의 MVCC는 History를 기반으로 이뤄진다.

> MVCC는 [21.07.11 TIL](../july/day11.md) 참고

짧게 MVCC를 요약하면 row 조회시 트랜잭션 격리 수준을 맞추기 위해서 undo log에 존재하는 해당 트랜잭션의 당시 데이터를 반환한다. 때문에 만약 트랜잭션이 길어질수록 더 많은 DB 리소스가 든다.

## History? History List Length?

History는 undo log에 저장되는 오래된 버전들의 snaphot 데이터들을 위한 데이터를 의미한다.

History List는 말 그대로 여러 히스토리, 즉, undo log 리스트를 의미한다.

History List Length는 이 History List의 갯수를 의미한다.

## History List Length가 길어지는 경우

History List Length가 길어지면 아주 긴 History를 purge할 때 순간적으로 CPU가 튀게된다. 아주 많은 양의 데이터를 purge 하므로 CPU에 영향을 줄 수 밖에 없다. 또한 snapshot 데이터를 만드는 것은 상당히 비싼 작업이며 장시간 유지를 하는 겅우 disk flush가 필요하므로 write I/O 부하가 생긴다.

## 참고

[https://blog.doosikbae.com/130](https://blog.doosikbae.com/130)
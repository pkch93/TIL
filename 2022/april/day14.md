# 2022.04.14 TIL - MySQL과 datetime

- [2022.04.14 TIL - MySQL과 datetime](#20220414-til---mysql과-datetime)
  - [Java 8 LocalDateTime과의 차이](#java-8-localdatetime과의-차이)
  - [참고](#참고)

MySQL에는 날짜와 관련된 date, datetime, timestamp 타입을 지원한다.

date는 날짜 `yyyy-MM-dd`, datetime은 날짜와 시간 `yyyy-MM-dd hh:mm:ss.SSS`, timestamp는 현재 타임존 `UTC, Asia/Seoul`에 대한 시간값을 나타낸다.

단, 주의할 점은 날짜 범위가 `1000-01-01`부터 `9999-12-31` 까지를 MySQL에서 지원한다는 것이다. 즉, date의 범위는 `1000-01-01`부터 `9999-12-31`이고 datetime은 `1000-01-01 00:00:00`부터 `9999-12-31 23:59:59`가 된다.

## Java 8 LocalDateTime과의 차이

Java 8에서는 Java에 존재하던 기존의 Date, Calendar와 같은 날짜 클래스의 문제점을 보완하기 위해 LocalDate, LocalDateTime 등의 날짜관련 API를 제공한다.

이때 날짜의 최대 `LocalDate.MAX, LocalDateTime.MAX`와 최소 `LocalDate.MIN, LocalDateTIme.MIN`를 지원하는데 이 값들이 MySQL datetime의 범위와 상이하다.

날짜의 최대 `LocalDate.MAX, LocalDateTime.MAX`는 `999_999_999` 년이고 최소 `LocalDate.MIN, LocalDateTIme.MIN`는 `-999_999_999`년이다. 따라서 만약 LocalDate와 LocalDateTime의 최대, 최소값을 MySQL에 저장하면 뜻하지 않는 결과를 야기한다.

따라서 MySQL에 날짜 최대, 최소를 저장해야한다면 `9999-12-31`을 최대, `1000-01-01`를 최소로 정의해서 저장해야한다.

## 참고

[https://dev.mysql.com/doc/refman/8.0/en/datetime.html](https://dev.mysql.com/doc/refman/8.0/en/datetime.html)
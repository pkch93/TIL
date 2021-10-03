# September

- [September](#september)
  - [목표](#목표)
  - [2021년 9월의 TIL](#2021년-9월의-til)
    - [9월 1째주 `21.09.01 ~ 21.09.05`](#9월-1째주-210901--210905)
    - [9월 2째주 `21.09.06 ~ 21.09.12`](#9월-2째주-210906--210912)
    - [9월 3째주 `21.09.13 ~ 21.09.19`](#9월-3째주-210913--210919)
    - [9월 4째주 `21.09.20 ~ 21.09.26`](#9월-4째주-210920--210926)
  - [9월 TIL 회고](#9월-til-회고)

## 목표

9월에는 업무에 필요할 것으로 생각되는 WebSocket에 대해 알아볼 예정이다.

WebSocket과 함께 [Spring 핵심 원리 기본편](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8/)과 [HTTP](https://www.inflearn.com/course/http-%EC%9B%B9-%EB%84%A4%ED%8A%B8%EC%9B%8C%ED%81%AC/dashboard) 강의를 들을 기회가 생겨서 Spring과 HTTP 기초 지식을 다시 한 번 정리해보고자 한다.

그 외 시간이 된다면 이전에 진행하고 있었던 Desserts 사이드 프로젝트를 계속 진행해보려고 한다.

## 2021년 9월의 TIL

### 9월 1째주 `21.09.01 ~ 21.09.05`

* [Day 04 - Spring WebSocket with STOMP (broker)](day04.md)
* [Day 05 - Spring WebSocket with STOMP (Annotated Controllers)](day05.md)

### 9월 2째주 `21.09.06 ~ 21.09.12`

* [Day 12 - SockJS](day12.md)

### 9월 3째주 `21.09.13 ~ 21.09.19`

* [Day 19 - BeanFactory와 ApplicationContext](day19.md)

### 9월 4째주 `21.09.20 ~ 21.09.26`

* [Day 20 - @Configuration](day20.md)
* [Day 22 - Spring vs Spring Boot](day22.md)

## 9월 TIL 회고

기존에 9월 학습 목표는 다음과 같았다.

- WebSocket 학습
- [Spring 핵심 원리 기본편](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8/) 강의 완강
- [모든 개발자를 위한 HTTP 웹 기본 지식](https://www.inflearn.com/course/http-%EC%9B%B9-%EB%84%A4%ED%8A%B8%EC%9B%8C%ED%81%AC/dashboard) 강의 완강

위 목표 중 완료한 건 Spring 핵심 원리 기본편을 완강한 것이다. 김영한님의 Spring 핵심 원리 기본편 강의를 통해 Spring 핵심 개념에 대해 좀 더 알 수 있었던 시간이었던거 같다.

특히 `@Configuration`이 프록시로 씌워져 빈으로 등록된다는 점과 싱글톤 빈과 싱글톤 빈보다 짧은 라이프사이클을 가진 빈 스코프와 함께 사용할 때 문제점은 잘 알지 못했던 부분인데 확실히 집고 갈 수 있어서 좋았다.

회사에서 웹소켓 프로젝트를 하게 되어서 WebSocket이 무엇인지, Spring WebSocket과 WebSocket MassageBroker 등을 학습하였다. SockJS 서버로 구성하여 서버 코드는 작성해보았으나 앱에서는 SockJS Client가 따로 없는 것으로 보여 순수 WebSocket으로 코드를 작성하고 동작하는 부분을 한번 실습해봐야 할 것 같다.

또한 Redis의 pub/sub 기능을 사용할 것으로 보이는데 이에 대해 학습을 하고자 한다.

HTTP 강의는 아직 보지 못했는데 시간이 나는대로 하나씩 들으려 한다.

9월은 프로젝트 오픈, 추석, 백신 접종 등으로 모든 날에 학습에 집중하지 못했던 거 같다. 10월에는 다시 페이스를 찾아서 꾸준히 학습하고자 한다.

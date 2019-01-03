# 2019 01.02 Thursday

## ※ SSAFY에서 배운 것들

1. python 기초

    - control flow (if문, while문, for문...)
    - 함수 / 네임스페이스 / 변수 스코프

[SSAFY에서 배운 것 보러가기](https://github.com/chulsea/TIL/blob/master/2019/January/days/0103.md)

## 1. Spring Restful API

- Toto class / Account class / Review class 정의

- lombok 사용시 주의사항

    lombok을 사용하는데 이렇게 많은 주의사항을 내포하는 줄을 몰랐다...

    Rest API에도 `@Data`와 `@AllArgsConstuctor` 등을 사용했는데 주의사항을 꼼곰히 읽어보고 다시 적용해야겠다.

    [참고](http://kwonnam.pe.kr/wiki/java/lombok/pitfall)

    [실무에서 lombok 사용법](https://www.popit.kr/%EC%8B%A4%EB%AC%B4%EC%97%90%EC%84%9C-lombok-%EC%82%AC%EC%9A%A9%EB%B2%95/)

- given / when / then

    unit test를 작성할 때 다음 규칙으로 작성

    - given

        given에서는 scenario를 실행하기 전에 실행할 객체나 data를 생성 / 정의
        즉, scenario 실시 전에 존재하는 세계 (world)의 상태를 정의하는 단계

    - when

        when에서는 실제 scenario에서 행동을(behavior) 명시

    - then

        행동 후 내가 예상하는 객체 / data의 상태 명시

    ```java
    @Test
    public void isJavaBean(){
        // given
        String study = "RestAPI 공부";
        String comment = "빨리 하자...";

        // when
        Todo todo = new Todo();
        todo.setTitle(study);
        todo.setComment(comment);

        // then
        assertThat(todo.getTitle()).isEqualTo(study);
        assertThat(todo.getComment()).isEqualTo(comment);
    }
    ```
    위 예시를 given / when / then에 맞춰 보면

    `study`, `comment`를 정의 (given), 할 일을 만드는데 title을 study로, comment를 달아준다 (when), 그 결과를 String 변수인 study와 comment로 체크 (then)

    [참고](https://martinfowler.com/bliki/GivenWhenThen.html)

## 2. Node.js

Node.js는 자바스크립트를 브라우저 외에 다른 환경 (서버, 응용프로그램, 블록체인, 임베디드 등)에서도 사용할 수 있게 해주는 `런타임`

> 런타임? (runtime?)
>
> 런타임이란 특정 언어로 만든 프로그램을 **실행 할 수 있는 환경**을 의미한다. 즉, `Node.js`는 javascript 프로그램을 실행할 수 있는 환경이라는 뜻이다.

주로 `Node.js`는 `http 서버`가 내장되어있기 때문에 서버로 많이 사용된다.

```javascript
var http = require("http"); // http 모듈 가져오기
var server = http.createServer(function(req, res){
   console.log("server start!") 
}); // 서버 생성
server.listen(3000); // port 3000번으로 서버 실행
```

### Chrome V8 Engine

Node.js는 구글에서 만든 `javascript` 엔진으로 웹 브라우저를 만드는 데 기반을 제공하는 오픈소스 엔진이다. (구글 크롬 브라우저와 안드로이드 브라우저에 탑재)

- V8 엔진은 C++로 만들어졌고 Chrome과 Node.js에 사용된다.

- V8은 ECMA-262에 명시된 ECMA Script를 구현한다.

- V8은 독립적으로 실행되며, C++ 프로그램에도 Embed 할 수 있다.

[위키백과](https://ko.wikipedia.org/wiki/%ED%81%AC%EB%A1%AC_V8)

[Chrome V8 엔진 참고](http://thd0011.tistory.com/20)

[captain pangyo님의 어플리케이션 성능 향상 기법](https://joshuajangblog.wordpress.com/2016/07/30/chrome-v8-javascript-perf/)

[javascript 작동 원리 + 최적화된 코드 작성 팁](https://engineering.huiseoul.com/%EC%9E%90%EB%B0%94%EC%8A%A4%ED%81%AC%EB%A6%BD%ED%8A%B8%EB%8A%94-%EC%96%B4%EB%96%BB%EA%B2%8C-%EC%9E%91%EB%8F%99%ED%95%98%EB%8A%94%EA%B0%80-v8-%EC%97%94%EC%A7%84%EC%9D%98-%EB%82%B4%EB%B6%80-%EC%B5%9C%EC%A0%81%ED%99%94%EB%90%9C-%EC%BD%94%EB%93%9C%EB%A5%BC-%EC%9E%91%EC%84%B1%EC%9D%84-%EC%9C%84%ED%95%9C-%EB%8B%A4%EC%84%AF-%EA%B0%80%EC%A7%80-%ED%8C%81-6c6f9832c1d9)

### Node.js 기능

1. js 파일 실행

node는 자바스크립트 `런타임`이다. 자바스크립트 프로그램을 실행할 수 있는 환경으로 당연히 js 파일 (프로그램)을 실행할 수 있다.

command 창에 `node <실행 파일 이름>`으로 js 파일을 실행시킬 수 있다. 이때 확장자(.js)는 생략가능하다.

2. 모듈 만들기

Node가 브라우저에서 작동하는 Client-Side javascript와 다른 점은 js 파일을 모듈화 할 수 있다는 점이다.

> 모듈?
>
> 쉽게 말해 어떤 기능을 하는 함수 / 변수의 집합을 모듈이라고 한다.
>
> 정보통신기술용어해설에는 모듈을 소프트웨어 묶음을 만들고, 코드를 네임스페이스로써 구분하는 매커니즘으로 소개한다.
>
> 모듈을 사용하면 수정이 쉽고 재사용성이 증가하며 유지관리에 탁월하다.
>
> 모듈화 원칙으로는 `1. 개별 기능별로 하나의 완결된 구조를 가져야한다.`, `2. 각 모듈별로 독립성을 가진다.`, `3. 각 모듈은 반드시 입구 / 출구가 존재해야한다.` 가 있다.

[참고](http://www.ktword.co.kr/abbr_view.php?m_temp1=2226)

보통은 파일 하나가 하나의 모듈이 되어 파일별로 모듈화시킨다.

> ES6의 모듈
> 
> ECMA Script(ES6)에서 import / export로 모듈을 지원해준다. 다만, 브라우저에는 구현되지 않아서 사용할 수 없었다. 하지만 크롬 60 버전부터 브라우저에서도 모듈을 사용할 수 있게 되었다. (점차 많은 브라우저에서도 사용할 수 있을거라 기대)


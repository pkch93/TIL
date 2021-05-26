# Day 9

## 1. Desserts

> dissert 프로젝트를 진행하면서 redux-saga에 대한 지식이 필요하다고 생각하여 정리

* redux-saga

saga를 사용하면 다음과 같은 장점이 있다.

1. 비동기 코드를 동기 코드처럼 쓸 수 있다.
2. 테스트가 편하다.
3. 복잡한 데이터 처리를 도와준다.

![redux-saga-logic](https://user-images.githubusercontent.com/30178507/59155138-984ac600-8abd-11e9-9093-3e6bd77c4cfd.PNG)

saga는 action이 일어나는 순간 그 action을 take한다. 그리고 side effect를 통해 결과를 put해준다.

saga가 take로 액션을 기다릴 수 있는 핵심은 ES6 generator이다.

* ES6 generator

ES6에 추가된 코루틴\(coroutine\) 함수로 function 키워드에 `*`를 선언하여 사용할 수 있다.

> 코루틴이란 제어를 caller에게 넘겨주는 것을 의미

yield 키워드를 만나면 스스로 멈추고 컨트롤을 callee에게 넘긴다.

redux-saga에서 핵심은 제너레이터와 이펙트!

1. 제너레이터

redux saga에서 saga는 제너레이터함수를 뜻한다. saga를 제너레이터함수로 구현하여 redux-saga의 이벡트를 사용할 수 있다. 즉, redux-saga 미들웨어에 커스텀 saga를 등록하고 수행시키는 로직이다.

```javascript
// saga의 초기화, 시작 코드에는 항상 run이 필요
middleware.run(rootSaga);
```

saga는 제너레이터함수이고 미들웨어는 saga에게 yield 값을 받아 다른 로직을 수행할 수 있다. 즉, saga는 명령을 내리는 역할, 실제 직접적인 동작은 미들웨어가 처리한다. \(이는 `redux-thunk`와의 차이점\)

1. 이펙트

이펙트란 미들웨어에 의해 수행되는 명령을 담고 있는 자바스크립트 객체이다.

`redux-saga/effects`의 put, call 등이 이펙트 생성자이다. 이펙트 생성자는 항상 일반 객체를 만들 뿐 그 이후의 동작은 하지 않는다.

> 이펙트에서 Promise를 yield하는 것도 가능!
>
> 단, 이런 비동기 로직을 이펙트에서 처리하게 되면 redux-thunk를 쓰는 것과 다를바없음
>
> 때문에 이펙트\(일반 자바스크립트 객체\)만을 yield하는 saga를 작성하는 것 추천!

* 자주 사용하는 이펙트 생성자
* put\(action\)

  redux 스토어에 액션을 dispatch하기 위해 미들웨어에 지시하기 위한 메서드 put

  기본적으로 non-blocking으로 동작한다.

* call\(fn, ...args\)

  put과 마찬가지 역할. 단, 첫번째 인자로 들어가는 function에 두번째부터 들어오는 인자를 전달하여 나타나는 결과를 이팩트로 생성

* fork\(fn, ...args\)

  non-blocking call을 수행하기 위해 middleware에 이펙트를 전달하는 이펙트 생성자

  이 때, fn은 Promise를 반환하는 일반함수 또는 제너레이터함수여야한다.

* spawn\(fn, ...args\)

  fork와 동일한 기능. 단, detached task를 생성한다.

  detached task는 top-level task와 같이 행동하거나 parent와는 독립적이다.

  즉, parent\(caller\)는 detached task의 종료를 기다려주지 않는다.

* takeEvery

  takeEvery는 기본적으로 동시성 제어를 허용한다. 즉, 현재 작업이 진행되더라도 새로운 작업이 실행될 수 있다는 의미이다. 단, takeEvery는 응답의 순서를 제어하지는 않는다. 이는 작업 순서대로 끝난다는 보장은 없다는 뜻. 만약 제일 최근에 실행된 결과를 얻어야한다면 takeLatest를 사용해야한다.

* takeLatest

  takeLatest와는 달리 만약 현재 작업 진행중에서 다른 작업 요청이 들어오면 현재 작업을 취소하고 들어온 요청을 처리한다.

[참고 영상: Redux-thunk에서 Redux-saga로 이사하기](https://www.youtube.com/watch?v=VHszuWZk0JA>)

[redux-saga: 제너레이터와 이팩트 참고](https://meetup.toast.com/posts/140>)

[redux-saga effect api 참고](https://redux-saga.js.org/docs/api/>)


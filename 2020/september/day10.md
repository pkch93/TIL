# Day10

## 20.09.10 Thursday - 우아한 테크러닝 4일차

```jsx
// App.js

import React from 'react'

export default ({ sessions }) => {
    return (
      <div>
        <header>
          <h1>React & Typescript</h1>
        </header>
        <ul>
                    {
                        sessions.map(session => (<li>{session}</li>))
                    }
        </ul>
      </div>
    )
}
```

따라서 데이터로 사용되는 세션 목록들을 상위 컴포넌트에서 props로 받아올 수 있다. `위 경우 세션 정보들을 api 통신으로 받아온다고 가정`

이때 처음 할 수 있는 고민이 `어느 정도로 컴포넌트를 쪼갤것인가`이다. 최대한 작은 단위로 컴포넌트를 쪼개는 것이 좋다. 그리고 `언제 쪼갤까` 라고 생각이 들었다면 바로 실행하는 것이 좋다. 추후에는 이런 작은 것들이 쌓이고 너무 범위가 많아져 수정할 엄두가 안날 수 있기 때문이다.

따라서 위 코드에서 반복되는 `<li>{session}</li>`를 하나의 컴포넌트로 도출할 수 있다.

```jsx
import React from 'react'

const SessionItem = ({ title }) => <li>{title}</li>

export default ({ sessions }) => {
    return (
      <div>
        <header>
          <h1>React & Typescript</h1>
        </header>
        <ul>
                    {
                        sessions.map(session => (<SessionItem title={session.title} />))
                    }
        </ul>
      </div>
    )
}
```

`SessionItem`을 정의하여 위와 같이 정의할 수 있다.

## state

위 React 앱에서 `session의 order 값에 따라 보여주는 방식이 달라질수있다`는 요구사항이 추가되었다고 가정한다.

이런 상황에서 정렬 방식이 달라질때마다 뷰를 다시 랜더링해줘야한다. 때문에 React의 컴포넌트 리랜더링 매커니즘을 이용하기 위해 state를 사용해야한다.

일단 정렬을 한 세션들을 보여줄 컴포넌트를 뽑아야한다.

```jsx
const OrderedSessions = ({ order, sessions }) =>
  sessions
    .sort((session1, session2) => {
      return order === "ASC"
        ? session1.order - session2.order
        : session2.order - session1.order;
    })
    .map((session) => <SessionItem title={session.title} />
)
```

### Class

```jsx
import React from 'react'

const SessionItem = ({ title }) => <li>{title}</li>

class ClassApp extends React.Component {
    constructor(props) {
        super(props)

        this.onToggleDisplayOrder = this.onToggleDisplayOrder.bind(this)
        this.state = { order: 'ASC' }
    }

    onToggleDisplayOrder() {
        this.setState(
            { order: this.state.order === 'ASC' ? 'DESC' : 'ASC' }
        )
    }

    render() {
        return (
      <div>
        <header>
          <h1>React & Typescript</h1>
        </header>
                <span>정렬상태: {this.state.order}</span>
                <button onClick={this.onToggleDisplayOrder}>재정렬</button>
        <ul>
                    {
                        this.props.sessions.map(session => (<SessionItem title={session.title} />))
                    }
        </ul>
      </div>
    )

    }
}

export default ClassApp
```

hooks가 없었던 시절에는 state를 관리할 수 있는 방법이 위와 같이 class 컴포넌트였다. 따라서 위와 같이 상태를 가져야하는 경우 class로 변경해야했다.

이런 문제때문에 hooks 이전에는 최상위 컴포넌트 `주로 class 컴포넌트`에서 props를 관리하고 하위 컴포넌트에서 상위 컴포넌트에서 전달하는 props를 가지고 뷰를 그려주는 방식을 많이 사용했었다.

#### function과 arrow function의 this

참고로 자바스크립트에서 function과 arrow function의 this 컨택스트가 달라진다.

function으로 정의하는 경우 this는 호출되는 환경에 따라서 this가 달라진다. 때문에 this가 콜백으로 전달하는 경우 콜백을 호출하는 컨텍스트의 this가 해당 function의 this가 되는 것이다.

따라서 위 `onToggleDisplayOrder`로 선언하는 경우 constructor에서 `this.onToggleDisplayOrder = this.onToggleDisplayOrder.bind(this)`위와같이 해당 function의 this를 맡춰줘야했다.

이런 이유로 예전 React 코드에서는 bind 관련 코드가 많았다.

하지만 arrow function의 경우 this는 정의된 컨텍스트의 this를 따르게 된다.

만약 위 `onToggleDisplayOrder`을 arrow function으로 변경하게 되면 this는 `ClassApp`에서 정의되었으므로 classApp의 인스턴스가 된다.

```jsx
const onToggleDisplayOrder = () => {
    this.setState(
        this.state.order === 'ASC' ? 'DESC' : 'ASC'    
    )
}
```

이 경우 this는 ClassApp의 인스턴스가 된다. 때문에 ES6 이후로 React에서 arrow function을 사용하게 되면서 위 bind 코드가 많이 사라졌다.

### Function

React 16.8.x 이후로 Hooks가 생기면서 Class 뿐만 아니라 Function에서도 상태를 관리할 수 있게 되었다.

```jsx
import React, { useState } from 'react'

const SessionItem = ({ title }) => <li>{title}</li>

export default ({ sessions }) => {
        const [order, setOrder] = useState('ASC')

        const onToggleDisplayOrder = () => {
            setOrder(order === 'ASC' ? 'DESC' : 'ASC')
        }

    return (
      <div>
        <header>
          <h1>React & Typescript</h1>
        </header>
                <span>정렬상태: {order}</span>
                <button onClick={onToggleDisplayOrder}>재정렬</button>
        <ul>
                    {
                        sessions.map(session => (<SessionItem title={session.title} />))
                    }
        </ul>
      </div>
    )
}
```

위와 같이 `useState`와 같은 Hooks API로 함수에서 state를 관리할 수 있다.

이때 `setOrder`와 같은 상태를 바꾸는 Hooks API는 `onToggleDisplayOrder`와 같이 함수에 감싸서 사용되어야한다.

## 비동기와 generator

```jsx
function* foo() {
    // ...
}
```

위와 같이 `function` 오른쪽에 `*`가 붙은 함수를 generator라고 한다.

```jsx
async function bar() {
    // ...
}
```

반면 `async` 키워드를 사용하면 비동기 함수를 정의할 수 있다. `async` 내에서 사용할 수 있는 `await`는 Promise와 함께 사용하여야한다.

이런 함수들은 Promise와 관련이 있다.

### Promise

```jsx
const p = new Promise(function(resolve, reject) {
    // ...
})
```

Promise는 callback을 인자로 받는데 이때 callback의 첫번째 인자는 `resolve`, 두번째 인자는 `reject`가 된다.

### generator

```jsx
function* make() {
    return 1
}

const i = make()
```

generator 함수를 호출하면 GeneratorFunctionPrototype을 리턴한다. 즉, 일반적인 함수와는 다르다. generator는 코루틴의 개념을 일부 차용하여 구현한 것이다.

```jsx
function* makeNumber() {
    let num = 1

    while(true) {
        yield num++        
    }
}

const i = makeNumber()
```

기존의 return문은 함수를 종료시키는 명령이다. generator는 전달할 값을 모두 제공할 때까지 함수가 종료되면 안된다. 따라서 generator용의 return을 따로 정의해두었다. 이것이 바로 `yield`이다.

위와 같이 `makeNumber`를 정의하고 호출을 하면 `i`에게 generator를 실행할 객체를 전달해주는 것이다. `GeneratorFunctionPrototype`

실행을 위해서는 next를 호출하는 것이다.

```jsx
i.next()
```

이렇게 호출하면 makeNumber에서 yield된 값을 객체형태로 받는다.

```jsx
{
    value: 1,
    done: false
}
```

value는 yield된 값, done은 generator가 마지막인지 여부를 알려주는 것이다. false라면 generator가 yield 할 값이 있다는 의미이다.

#### generator 활용

이렇게 generator는 내부의 상태를 계속 유지한다는 특징이 있다. 이런 특징을 통해 비동기를 동기적으로 사용할 수 있다.

```jsx
const delay = ms => new Promise(() => setTimeout(resolve, ms))

function* main() {
    console.log('시작')
  yield delay(3000)
    console.log('3초 뒤')
}

const m = main()

const { value } = m.next()
value.then(() => {
    m.next()
})

delay(3000).then(() => {
    console.log('3초 뒤')
})
```

마치 순서대로 진행되는 동기적 코드를 만들때 generator를 사용하기도 한다. 이를 통해 바깥에서 내부 동작을 동기적으로 제어할 수 있도록 만들 수 있다.

실제로 async를 babel로 트랜스파일링하면 generator와 Promise로 되어있다.

```jsx
async function main2() {
    console.log('시작')
    await delay(3000)
    console.log('3초 뒤')
}

main2()
```

위 main2는 main과 동일하게 동작한다.


# 20.09.08 Tuesday - 우아한 테크러닝 3일차 React 기본

# Virtual DOM 만들어보기

Real DOM은 Virtual DOM보다 더 Low 레벨이라 복잡도가 높다. 또한 이런 Real DOM을 직접 다루는 것은 성능 저하를 유발한다. 이를 보완하여 좀 더 DOM을 다루기 쉽게 하기위해 React가 등장했다.
Virtual DOM이 왜 필요한지 참고: [https://velopert.com/3236](https://velopert.com/3236)

```jsx
function createElement(type, props = {}, ...children) {
	return { type, props, children }
}
```

babel의 기능중에 아래와 같이 JSX로 Virtual DOM을 만드는 함수를 변경할 수 있다. 기본적으로 createElement이다.

```jsx
/* @jsx H */
```

위와 같은 옵션을 제공해주는데 이는 virtual DOM을 만드는 함수를 H로 변경하는 옵션이다.

어떤 것이 바벨과 같은 트랜스파일링을 해주는 것인지, 어떤 것이 리엑트가 해주는 것인지 알아야하고 컴파일타임과 런타임에 일어나는 일을 알아야한다.

이런 가상돔이 현제 실제로 랜더링된 DOM과 비교하여 ReactDOM.render에서 실제 DOM에 랜더한다.

### JSX와 Builtin 컴포넌트

JSX에서 사용자 컴포넌트는 반드시 대문자로 해야한다. 이것이 JSX에서 `div`, `h1`과 같은 builtin 컴포넌트들과 사용자 커스텀 컴포넌트를 구분하는 방법이다.

따라서 위 createElement에서 type이 함수라면 분기처리를 해주어야한다.

```jsx
function createElement(type, props = {}, ...children) {
	if (typeof type === 'function') {
		return type.apply(null, [props, ...children])
	}

	return { type, props, children }
}
```

이렇게 createElement를 구현하면 다음과 같이 App을 확인할 수 있다.

![https://user-images.githubusercontent.com/30178507/92488081-0e8e2900-f229-11ea-87ff-2a32a3a8f694.png](https://user-images.githubusercontent.com/30178507/92488081-0e8e2900-f229-11ea-87ff-2a32a3a8f694.png)

### render

```jsx
function render(virtualDom, container) {
	container.appendChild(renderElement(virtualDom))
}
```

render는 DOM tree와 container를 인자로 받는다. render는 단순히 virtualDom을 container에 붙이는 역할이다.

> 참고로 renderElement는 가상돔을 실제 container에 붙이는 역할을 한다.

### renderElement

```jsx
function renderElement(node) {
	const el = document.createElement(node.type)

	return el
}
```

먼저 인자로 받는 node의 타입으로 element를 만든다.
그리고 node의 children이 존재한다. 따라서 node의 children들도 모두 실제 DOM element로 만들어주어야한다.

```jsx
function renderElement(node) {
	const el = document.createElement(node.type)

	node.children.map(renderElement).foreach(element => {
		el.appendChild(element)
	})

	return el
}
```

단, JSX에서 맨 마지막 요소는 String이다. 따라서 이를 방어하는 코드가 필요하다.

```jsx
function renderElement(node) {
	if (typeof node === 'string') {
		return document.createTextNode(node)
	}

	const el = document.createElement(node.type)

	node.children.map(renderElement).foreach(element => {
		el.appendChild(element)
	})

	return el
}
```

이런식으로 React의 가장 기초를 구현하였다.

> 라이브러리 코드 보는 요령
초기 release 버전을 먼저 확인하여 라이브러리 초기 컨셉을 확인한 후 한 뎁스씩 요령껏 볼 것.

# React의 기본

react 컴포넌트를 정의하는 방법은 class 방식과 function 방식이 존재한다.
이때 react의 컴포넌트는 render가 필수적으로 구현되어 있어야한다. 즉, JSX `createElement`를 통해 virtualDOM을 만들어야하기 때문이다. `react 컴포넌트 자체는 UI를 만드는 능력이 없다.`

class 컴포넌트는 다음과 같은 구조를 가진다.

```jsx
class Hello extends React.Component {
	constructor(props) {
		super(props)
	}

	render() {
		// ...
	}
} 
```

## 상태

React hook 전까지는 class에서만 상태를 가질 수 있었다.

```jsx
function App() {
	let state = 10;

	return (
		<div>
			<h1>상태</h1>
		</div>
	)
}
```

위와 같이 함수형 컴포넌트에서 상태를 구현하려면 내부에 변수를 두어 사용해야할텐데 함수는 호출될 때마다 내부 변수가 새롭게 생겨나므로 상태를 가질 수 없었다.

단, 클래스 컴포넌트의 경우는 객체이므로 인스턴스 생성한 후 계속 변수에 상태를 유지할 수 있다. 따라서 초기 리액트에서 상태를 다루는 경우 클래스 컴포넌트, 화면을 다루는 경우 함수형 컴포넌트로 주로 사용했었다.

> command - presentational component 패턴

상태와 마찬가지로 라이프사이클도 함수와 클래스의 차이를 그대로 가진다.
함수형 컴포넌트는 함수 생성 후 return만이 존재하므로 라이프사이클이 존재하지 않았다. 반면, 클래스 컴포넌트는 처음 인스턴스를 생성한 후 이 인스턴스를 다루므로 라이프사이클이 존재한다.

## React Hooks

hook은 함수형 컴포넌트에서 상태를 가질수 있도록 지원해주는 스팩이다.

```jsx
function App() {
	const result = useState(1)

	return (
		<div>
			<h1>상태</h1>
		</div>
	)
}
```

위와 같이 `useState`를 사용하면 함수형 컴포넌트에서 state를 사용할 수 있다.
`useState`는 배열로 나타나며 첫번째 값은 초기값, 두번째 값은 상태를 바꾸는 dispatch 함수이다.

### Hooks의 상태를 update하면 render가 되는 이유

react가 컴포넌트를 만들때 createElement가 호출된다. 그리고 createElement의 최상단이 함수인 경우와 함수를 호출하는 시점에 hook 코드가 호출되는 경우 hook 전역 배열에 초기값과 dispatch를 넣어둔다. 그리고 2번째 호출일때는 hook state를 초기화하는 과정을 무시한다.

컴포넌트가 중첩된 순서대로 hook 전역 배열에서 관리하기 때문에 반드시 최상위 레벨에서만 hook을 사용해야한다. 그리고 전역 배열은 리엑트 컴포넌트를 키로 가지고 있기 때문에 일반함수에서는 사용할 수 없다.

그리고 해당 컴포넌트에 hook이 호출되었다는 것을 리액트가 알고 있기 때문에 클래스 컴포넌트와 같이 라이프사이클을 가질 수 있는 것이다. `useEffect 등`
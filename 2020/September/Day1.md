# 20.09.01 Tuesday - 우아한 테크러닝 1

[https://www.typescriptlang.org/play](https://www.typescriptlang.org/play)
[https://codesandbox.io/index2](https://codesandbox.io/index2)
[https://reactjs.org/](https://reactjs.org/)
[https://redux.js.org](https://redux.js.org/)
[https://mobx.js.org/README.html](https://mobx.js.org/README.html)
[https://redux-saga.js.org/](https://redux-saga.js.org/)
[https://blueprintjs.com/](https://blueprintjs.com/)
[https://testing-library.com/](https://testing-library.com/)

# Typescript

추론은 가독에 악영향을 준다. 또한 추론이 원하는대로 이뤄지지 않을 가능성이 있다. 따라서 가능하면 명시적으로 타입을 지정하는 것이 좋다!

타입스크립트에는 컴파일 타임에만 사용하는 메커니즘과 런타임까지 동작하는 메커니즘이 있다. 대표적으로 type alias가 컴파일 타임에서만 사용되는 메커니즘이다. 따라서 타입스크립트를 트랜스파일링했을때 type alias는 남지 않는다.

type과 interface

# React

## CRA `create-react-app`

리액트앱을 빠르게 구성할 수 있는 방법.

```
yarn create react-app tech-hello --template typesctipt
```

### React를 항상 import하는 이유

jsx를 바벨로 트랜스파일링하면 `createElement` 같은 React의 함수를 호출한다. 이때 `import React from 'react'`를 하지않으면 컴파일에러가 난다.

## CRA의 단점

- CRA가 제공하지 않는 구성을 세팅하는데 매우 힘들다.
- React를 처음 사용하기에는 훌륭할 뿐 아니라 초기에는 큰 문제가 없어보임. 때문에 프로젝트 완성단계에서 문제를 발견하는 경우가 많다.
- 다양한 환경에 대한 대응이 어려움

따라서 Production용으로 사용한다면 CRA는 추천하지 않음.

CRA에서는 커스텀을 위해서 eject라는 CRA의 환경을 구성을 풀어서 고칠수 있도록 하는 기능을 제공하지만 너무 복잡하기 때문에 권하지는 않음.

## 전역 상태 관리

mobx는 redux의 대체품이라기보다는 상태관리를 하는 새로운 패러다임! 실제 프로덕션에서도 많이 사용하고 있는 라이브러리

단, mobx는 많은 기능을 내포하고 있어 잘못 사용할 수 있는 여지가 있다. 어떤 부분이 실수 할 수 있는 여지가 있는지 고민해볼 것!

# 2021.05.26 TIL - React Native Navigation

React Native에서 일반적으로 내비게이션을 구현할 때 `react-navigation`과 `react-native-navigation` 라이브러리를 주로 사용한다.

`react-navigation`은 React Native 팀에서 네비게이션 구현시 추천하는 라이브러리이며 javascript 스레드에서 내비게이션이 동작한다.

반면 `react-native-navigation`은 Wix 개발자들이 관리하는 라이브러리로 네이티브 방식으로 내비게이션이 동작한다.

보통의 경우 React Native 앱은 Javascript 스레드에서 동작하기 때문에 `react-navigation`으로 구현해ㅗ 무방하다. 다만, 네이티브 방식으로 내비게이션을 처리해야할 경우도 존재하는데 이때는 `react-native-navigation`을 사용하는 것이 적절해보인다.

## Getting Started

> 일반적으로 사용하는 react-navigation을 기준.
21.05.26 기준으로 react-navigation/native는 `5.9.4` 버전

react-navigation을 사용하기 위해서는 `@react-navigation/native`, `react-native-reanimated`, `react-native-gesture-handler`, `react-native-screens`, `react-native-safe-area-context`,  `@react-native-community/masked-view`가 필요하다.

```text
npm install @react-navigation/native react-native-reanimated react-native-gesture-handler react-native-screens react-native-safe-area-context @react-native-community/masked-view
```

설치 후 만약 iOS를 대상으로 개발을 하고 있다면 다음과 같이 `pod-install`이 필요하다.

```text
npx pod-install ios
```

설치가 완료되었다면 `index.js` 또는 `App.js`에 `react-native-gesture-handler`를 import한다.

```tsx
import 'react-native-gesture-handler';
```

> react-native-gesture-handler는 React Native에서 네이티브에 대한 제스처를 지원하기 위해 필요한 라이브러리다.

공식문서: [https://docs.swmansion.com/react-native-gesture-handler/](https://docs.swmansion.com/react-native-gesture-handler/)

그리고 NavigationContainer를 전체 App에 감싸면 이제 react-navigation을 사용할 준비가 끝났다.

```tsx
import 'react-native-gesture-handler';
import * as React from 'react';
import { NavigationContainer } from '@react-navigation/native';

export default function App() {
  return (
    <NavigationContainer>{/* Rest of your app code */}</NavigationContainer>
  );
}
```

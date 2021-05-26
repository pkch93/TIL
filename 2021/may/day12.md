# 2021.05.12 TIL - React Native Navigation

React Native에서 일반적으로 내비게이션을 구현할 때 `react-navigation`과 `react-native-navigation` 라이브러리를 주로 사용한다.

`react-navigation`은 React Native 팀에서 네비게이션 구현시 추천하는 라이브러리이며 javascript 스레드에서 내비게이션이 동작한다.

반면 `react-native-navigation`은 Wix 개발자들이 관리하는 라이브러리로 네이티브 방식으로 내비게이션이 동작한다.

보통의 경우 React Native 앱은 Javascript 스레드에서 동작하기 때문에 `react-navigation`으로 구현해ㅗ 무방하다. 다만, 네이티브 방식으로 내비게이션을 처리해야할 경우도 존재하는데 이때는 `react-native-navigation`을 사용하는 것이 적절해보인다.

## React Native 내비게이션 vs Web 내비게이션

웹에서는 URI를 통해서 새로운 화면으로 라우팅한다. 반면 모바일 앱에서는 컴포넌트를 기준으로 라우팅을 한다. 때문에 내비게이션을 통해서 컴포넌트를 새로운 화면으로 라우팅해야한다.

### 참고

[React Native Docs](https://reactnative.dev/docs/navigation)

[React Navigation vs react-native-navigation](https://blog.logrocket.com/react-navigation-vs-react-native-navigation-which-is-right-for-you-3d47c1cd1d63/)

[리엑트 네이티브 인 액션 6장 내비게이션](http://www.yes24.com/Product/Goods/79184153)

[react-native-navigation](https://wix.github.io/react-native-navigation/docs/installing)


# 2021.06.08 TIL - React Native Linear Gradient

- [2021.06.08 TIL - React Native Linear Gradient](#20210608-til---react-native-linear-gradient)
  - [react-native-linear-gradient](#react-native-linear-gradient)
    - [참고](#참고)

## react-native-linear-gradient

React Native에서는 기본적으로 linear-gradient 스타일링을 지원하지 않는다. linear-gradient 스타일링을 사용하기 위해서는 서드 파티 라이브러리인 `react-native-linear-gradient` 의존성을 받아서 사용해야한다.

> 참고로 linear-gradient 스타일은 두 개 이상의 색이 직선을 따라 점진적으로 변화하는 스타일을 의미한다.
> 
> 이를 통해 텍스트에 형광펜 효과를 표현하는 등의 스타일링이 가능하다.
>
> 참고: [https://lifenourish.tistory.com/843](https://lifenourish.tistory.com/843)

```shell
npm i react-native-linear-gradient
```

위와 같이 `react-native-linear-gradient` 의존성 설치가 필요하다.

> 설치 후 iOS의 경우는 ios 폴더에서 `npx pod install` 실행이 필요하다.

`react-native-linear-gradient`에서는 `LinearGradient` 컴포넌트를 지원한다. 이를 통해 linear-gradient 스타일을 적용하고 싶은 컴포넌트에 감싸서 스타일링을 적용할 수 있다.

```tsx
import LinearGradient from 'react-native-linear-gradient'

<LinearGradient colors={['rgba(255, 255, 255, 0)', 'rgba(250, 240, 105, 1)']}>
    <Text>hello world!</Text>
</LinearGradient>
```

위와 같이 `hello world!` 텍스트에 노란색 형광펜 효과를 linear-gradient로 구현하고 싶다면 위와 같이 구현할 수 있다.

colors에 linear-gradient를 어떤 색상으로 적용할 것인지 배열 형태로 제공할 수 있다. `rgba` 형태 뿐만 아니라 `Hex` 형태로도 제공할 수 있다.

start와 end를 LinearGradient의 props로 제공할 수 있는데 `{ x: number, y: number }`의 형태를 띈다. start는 말그대로 그라데이션을 시작할 시작 지점을 명시하는 것이다. end는 그라데이션이 끝나는 지점을 명시하는 것으로 start와 동일하게 `{ x: number, y: number }` 형태를 띈다.

start와 end는 `0`부터 `1`까지의 값을 가지며 `{ x: 0.1, y: 0.1 }`가 start에 props라면 이는 상단 `10%`, 왼쪽 `10%`에서 gradient가 시작한다는 의미가 된다.

start와 end 이외에도 locations를 props로 줄 수 있는데 locations는 colors로 전달한 각 color들의 gradient가 어느 지점에서 종료될지를 설정하는 props이다. 만약 colors가 `['#4c669f', '#3b5998', '#192f6a']`이고 locations가 `[0,0.5,0.6]`라면 첫 `#4c669f`는 `0% ~ 50%`까지 차지, 두번째 `#3b5998`는 `50% ~ 60%`를 차지, 마지막 `#192f6a`는 `60% ~ 100%`를 차지한다.

그밖에 앵글 효과를 주기 위해서 useAngle / angle / angleCenter props를 전달할 수 있다.

### 참고

react-native-linear-gradient docs: [https://github.com/react-native-linear-gradient/react-native-linear-gradient](https://github.com/react-native-linear-gradient/react-native-linear-gradient)

그라데이션(Gradient): [https://dev-yakuza.posstree.com/ko/react-native/react-native-linear-gradient/](https://dev-yakuza.posstree.com/ko/react-native/react-native-linear-gradient/)
# 2021.05.20 TIL - React Native Platform Specific Code

플랫폼 별 `android, ios` 스타일링을 다르게 가져가야할 필요가 있을 수 있다. 이를 위해서 Platform 모듈을 사용할 수 있다.


```javascript
import { Platform, StyleSheet } from 'react-native';

const styles = StyleSheet.create({
  height: Platform.OS === 'ios' ? 200 : 100
});
```

다음과 같이 `Platform.OS`로 기기의 OS를 판단하여 height를 달리 줄 수 있다. `ios`라면 200, `android`라면 100을 주는 것이다.

`Platform.OS` 말고 `Platform.select`를 사용할 수도 있다.

```jsx
import { Platform, StyleSheet } from 'react-native';

const styles = StyleSheet.create({
    container: {
        ...Platform.select({
            ios: {
                height: 200,
            },
            android: {
                height: 100,
            }
        })
    }
});
```

이는 `styled-components`에서도 동일하게 적용할 수 있다.

![ios](https://user-images.githubusercontent.com/30178507/118995714-55d8d600-b9c2-11eb-9443-4b3a63eab39a.png)

![android](https://user-images.githubusercontent.com/30178507/118995789-5ffad480-b9c2-11eb-818c-d3fd0770bc1e.png)

```jsx
import styled from 'styled-components/native'

const Header = styled.View`
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    margin-top: 16px;
`
```

위 예시는 `margin-top`을 `16px`로 주었을 때 iOS, Android에 각각 랜더링되는 화면이다. Android는 정상적으로 랜더링되었지만 iOS의 경우 status bar에 상단 헤더가 걸치는 것을 볼 수 있다. 이를 처리하기 위해서 iOS에 `margin-top`을 좀더 주어야할 것이다.

```jsx
import { Platform } from 'react-native'
import styled, { css } from 'styled-components/native'

const Header = styled.View`
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    ${Platform.select({ios: css`margin-top: 48px`, android: css`margin-top: 16px`})};
`
```

다음과 같이 `styled-components`를 사용할 때는 `${}`에 js 코드를 사용하여 제공할 수 있다.

![ios](https://user-images.githubusercontent.com/30178507/118995701-540f1280-b9c2-11eb-81c0-99f1019b1358.png)

![android](https://user-images.githubusercontent.com/30178507/118995789-5ffad480-b9c2-11eb-818c-d3fd0770bc1e.png)

`Platform.select`를 사용하여 iOS에 `margin-top: 48px`를 주고 Android에는 `margin-top: 16px`를 줄 수 있다. 화면에도 적용된 것을 볼 수 있다.

## Platform-specific extensions

위와 같이 Platform module을 활용할수도 있지만 플랫폼에 맞는 확장자를 사용하여 처리할 수 있다.

```
Header.ios.tsx
Header.android.tsx
```

위와 같이 `.{platform}.`을 추가하여 각 플랫폼에 맞게 컴포넌트를 로딩할 수 있다.

```jsx
import Header from './Header'
```

위와 같이 사용하면 OS에 맞는 확장자 파일을 자동으로 선택하여 로드한다.

### 참고

platform module: [https://reactnative.dev/docs/platform-specific-code](https://reactnative.dev/docs/platform-specific-code)
# 2021.05.22 TIL - React Native android에서 이미지 깨지는 이슈

React Native에서 이미지 처리를 위해 Fresco 라이브러리를 사용한다.
그런데 iOS에서는 문제가 없는데 Android에서 해상도보다 이미지가 큰 경우 자동으로 줄여주는 기능이 있다. 이때문에 Android에서는 이미지가 깨져보인다.

### 참고

[https://reactnativeseoul.org/t/topic/923](https://reactnativeseoul.org/t/topic/923)
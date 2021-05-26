# Day 8

1100 ~ 1400 : PWA codelab

## 1. PWA

* manifest.json

manifest.json을 적용하면 다음과 같은 효과를 얻을 수 있다.

1. 앱 시작시 배치를 지정할 수 있다. `display`로 적용
2. 앱 시작시 처음 보이는 주소. `start_url`로 적용
3. 앱 실행기나 dock에서 보여지는 이미지를 정의할 수 있다. `short_name`, `icons`

   > 이때 icons에서 32x32는 iOS를 위한 것이다.

4. splash screen을 만들 수 있다. `name`, `icons`, `colors`

`manifest.json`은 chrome에서는 지원해주지만 아직 iOS와 Safari에서는 지원하지 않는다. 이를 적용하기 위해서는 `<meta>`테크를 사용하여야 한다.

* offline experience 제공하기

online 상태에서 data를 caching하여 자동으로 오프라인 경험 제공

이를 위해서 offline일 때 200 응답을 네트워크가 끊기기 전의 현재 페이지와 시작 페이지에서 제공해주어야 한다.

또한 `controls page`와 `start_url`에는 서비스 워커가 등록되면 안됨

여기서 중요한 점은 offline 경험을 제공해주는 것 뿐만아니라 local에 대부분의 html, css, js를 저장하므로 빠르게 화면에 띄워줄 수 있으므로 성능상으로도 유리하다.

* Service Worker

[참고](https://developers.google.com/web/fundamentals/primers/service-workers/)

> service worker는 오직 https \(localhost는 예외\)에서만 사용 가능하다.

Service Worker로 제공되는 기능들은 `progressive enhancement`에 따라 오직 브라우저에서 지원되는 것만 적용된다.

angular, react, vue와 같은 js 라이브러리의 경우는 bundle.js만 캐싱하면 편하게 캐싱이 가능하다.

## 2. redux


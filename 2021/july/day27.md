# 2021.07.27 TIL - java-test-fixture overview

- [2021.07.27 TIL - java-test-fixture overview](#20210727-til---java-test-fixture-overview)
  - [사용하기](#사용하기)
  - [참고](#참고)

java-test-fixtures는 gradle 5.6부터 추가된 플러그인이다.

test를 작성하다보면 테스트를 위한 더미데이터를 만드는 것도 리소스를 소모한다. 테스트 더미 데이터를 어디에서 관리할 지를 정하는 것도 큰 리소스이다.

`src/main`에서 테스트 더미를 관리하면 해당 모듈을 의존하는 다른 모듈에서 모르고 사용할 가능성도 있고 `src/test`는 다른 모듈에서 의존할 방법이 없으므로 중복해서 더미 데이터를 만들수 밖에 없다.

이런 문제를 해결하기 위해 실 애플리케이션에 사용할 코드가 있는 `main`과는 별도로 의존받을 수 있도록 **java-test-fixtures** 플러그인이 도와준다.

## 사용하기

먼저 java-test-fixture 플러그인을 등록한다.

```groovy
plugins {
    id 'java-library'
    id 'java-test-fixtures'
}
```

참고로 java-test-fixtures는 java나 java-library 플러그인을 필요로한다.

위와 같이 플러그인 설정하면 testFixtures라는 sourceSet이 자동으로 생성되며 해당 폴더 하위에 테스트 더미를 작성할 수 있다.

## 참고

Using test fixtures 참고: [https://docs.gradle.org/current/userguide/java_testing.html#sec:java_test_fixtures](https://docs.gradle.org/current/userguide/java_testing.html#sec:java_test_fixtures)
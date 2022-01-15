# 2022.01.15 TIL - gradle Kotlin DSL apply false가 존재하는 이유

- [2022.01.15 TIL - gradle Kotlin DSL apply false가 존재하는 이유](#20220115-til---gradle-kotlin-dsl-apply-false가-존재하는-이유)
  - [참고](#참고)

Gradle Kotlin DSL로 Gradle 멀티모듈을 구성해보고 있는데 다음과 같이 `plugins` 블럭에 apply false를 붙인 경우를 많이 봤다.

```kotlin
plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.21"
    id("org.jetbrains.kotlin.kapt") version "1.3.21"
    id("org.springframework.boot") version "2.1.4.RELEASE" apply false
    id("org.jetbrains.kotlin.plugin.spring") version "1.3.21" apply false
    id("com.gorylenko.gradle-git-properties") version "1.5.1" apply false
}
```

groovy DSL에서는 못본 문법이라서 어떤 기능을 하는지 궁금했다. 왜 저렇게 사용하는 걸까?

이는 race condition 문제 때문이라고 한다. gradle이 빌드 스크립트를 평가하기 전에 plugin에 정의된 플러그인들이 아직 classpath에 포함되지 않을 수 있다고 한다. 때문에 classpath에 추가하기 위해서 `apply false`를 사용한다고 한다.

참고로 이는 plugins 블럭이 빌드스크립트 최상단에 위치해야만 수행된다.


## 참고

[https://stackoverflow.com/questions/48290389/different-ways-to-apply-plugins-gradle-kotlin-dsl](https://stackoverflow.com/questions/48290389/different-ways-to-apply-plugins-gradle-kotlin-dsl)
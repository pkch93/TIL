# 2021.07.31 TIL - java-test-fixture advanced

- [2021.07.31 TIL - java-test-fixture advanced](#20210731-til---java-test-fixture-advanced)
  - [testFixtures 의존성 설정](#testfixtures-의존성-설정)
  - [testFixtures 함께 원격 저장소에 올리기](#testfixtures-함께-원격-저장소에-올리기)
  - [with gradle build](#with-gradle-build)

java-test-fixtures는 싱글 프로젝트 뿐만 아니라 다른 프로젝트에서도 Fixture를 사용할 수 있도록 지원한다.

java-test-fixtures는 jar를 통해 Fixture를 사용할 수 있도록 지원한다. 따라서 해당 test-fixture jar를 사용하고자하는 프로젝트에서 사용하도록 설정하면 사용할 수 있다.

## testFixtures 의존성 설정

java-test-fixtures 플러그인는 다른 프로젝트, 모듈에서 Fixtures를 사용할 수 있도록 `testFixtures` classifier를 지원한다.

```groovy
dependencies {
    testImplementation testFixtures("com.google.code.gson:gson:2.8.5")
}
```

위와 같이 maven repository에 올라가있는 프로젝트의 testFixtures를 사용하고 싶다면 위와 같이 `testFixtures` classifier에 해당 의존성을 넣어주면된다.

위 설정을 하면 `com.google.code.gson:gson-test-fixture`로 된 라이브러리 `jar`를 찾는다. java-test-fixtures에서는 기본적으로 메인 컴포넌트의 이름에 `-test-fixtures`를 붙인 라이브러리를 test-fixtures 의존성으로 찾는다.

```groovy
dependencies {
    testImplementation testFixtures(project(":lib"))
}
```

만약 다른 모듈에서 정의한 Fixtures를 사용하고 싶다면 다른 모듈을 의존성으로 정의하는 것과 동일하게 `testFixtures`로 감싸서 정의하면된다.

## testFixtures 함께 원격 저장소에 올리기

java-test-fixtures는 기본적으로 메인 컴포넌트 이름에 `-test-fixtures` postfix를 붙여 test-fixtures jar를 생성한다. 아무 설정없이 사용하면 test-fixtures jar도 함께 올라간다.

만약 test-fixtures jar가 올라가는 것을 원치 않는다면 다음과 같은 설정을 `build.gradle`에 추가하면 된다.

```groovy
components.java.withVariantsFromConfiguration(configurations.testFixturesApiElements) { skip() }
components.java.withVariantsFromConfiguration(configurations.testFixturesRuntimeElements) { skip() }
```

## with gradle build

`java-test-fixtures` 플러그인을 사용하면 gradle build시에 `testFixturesJar` task를 사용한다. `testFixturesJar`는 해당 프로젝트에서 정의한 `test-fixtures` 클래스를 담은 jar를 만든다. `testFixturesJar`는 jar를 확장해서 정의한 task이기 때문에 `build/lib` 폴더 내에 `-test-fixtures` jar가 생성된다.

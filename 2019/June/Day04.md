# Day 4

## 1. Gradle

> Gradle Build Script 작성법에 대해서 학습

### Writing Build Scripts

gradle은 기본적으로 groovy를 DSL (domain specific language)로 사용한다. gradle 5버전 이후로는 kotlin도 DSL로써 사용할 수 있다.

- Project

  build script는 프로젝트의 설정 사항을 기록해 놓은 것이다. 이때 Project는 library나 application과 같이 빌드되야하는 소프트웨어 컴포넌트를 의미한다. 각각의 build script는 Project 객체로써 접근할 수 있다.

  build script에 명시한 property나 block들은 거의 모두 Project API의 일부분이다. 예를 들어 Project.name을 하면 해당 프로젝트의 name에 접근할 수 있다.

   Project는 아래와 같은 standard properties를 가진다.

  1. project : Project instance
  2. name : project 디렉터리 이름
  3. path : 프로젝트의 절대 경로
  4. description : 프로젝트의 description
  5. projectDir : build script를 포함하는 디렉터리
  6. buildDir : projectDir/build
  7. group
  8. version
  9. ant : antBuilder instance

- script

  gradle이 build script를 실행하면 Script를 구현한 class로 각 build script를 컴파일한다.

  때문에 Gradle이 제공하는 Script 메서드와 properties를 사용할 수 있다. 

  참고로 build script가 실행되면 `Project` 객체가 Script 객체로 위임되고, `Initialization script` (init.gradle)의 경우는 `Gradle` 객체가 Script 객체로 위임된다. `settings.gradle`은 `Settings` 객체가 위임.

  > kotlin의 경우는 KotlinBuildScript의 subclass로 컴파일된다.

- Declaring variables

  build script에서 변수선언은 2가지로 가능하다. `local variable`과 `extra variable`이 있다.

  - local variable

    `def` 키워드를 통해 선언 가능하다. 이는 오직 해당 build script에서만 scope를 가지게 된다.

    ```groovy
    def dest = "dest"
    
    task copy(type: Copy) {
        from "source"
        into dest
    }
    ```

    위와 같이 def 키워드로 dest 변수를 선언할 수 있다.

  - extra properties

    build script에서 ext 블록을 이용하여 정의할 수 있다. ext 블록에 선언된 properties는 다른 build script에서도 사용가능하다.

### ※ Window에서 gradle 인코딩이 UTF-8로 안되는 경우 해결법

gradle 실행시 실행 OS의 시스템 설정이 그대로 적용된다. 때문에 UNIX / LINUX는 기본적으로 UTF-8, 윈도우는 MS949로 인코딩된다. 때문에 컴파일이 안되는 문제가 발생할 수 있다.

이를 해결하기 위해 gradle 실행시 file.encoding 옵션을 추가하는 것이다.

```groovy
GRADLE_OPTS=-Dfile.encoding=UTF-8
```

위와 같이 옵션을 추가하여 인코딩 문제를 해결할 수 있다.

[gradle 컴파일 인코딩 오류 해결방법](<https://java.ihoney.pe.kr/335>)



## Gradle Plugin

plugin은 새로운 task를 추가하거나, domain 객체를 추가하거나, 컨벤션을 추가하는 등 유용한 기능들을 제공해준다. 즉, plugin을 사용하여 현재 project에 기능을 확장할 수 있다.

- Plugin의 type

  gradle의 plugin은 2가지 타입으로 나뉜다. 바로 `script`와 `binary`이다. script는 빌드에 대한 추가적인 설정을 담은 추가적인 build script이다. 보통은 외부에 위치한 build script에 접근해야할 경우 script type의 plugin을 사용한다.

  반면 binary의 경우는 Plugin의 구현체로써 프로그래밍적으로 빌드를 조작할 수 있도록 도와준다. binary plugin은 build script에 선언한다.

- using plugin

  plugin은 2가지 과정을 거쳐 적용한다. 먼저 plugin을 resolve하고 (1단계) 해당 plugin을 apply하여 적용할 수 있다.

  이때 plugin을 resolve한다는 의미는 해당 plugin을 포함하고 있는 jar의 버전을 찾고 script의 classpath에 추가하는 것을 의미한다. 이렇게 resolve되면 build script에서 사용가능하다.

  보통 script plugin은 직접 resolve를 해야하지만 binary의 경우 gradle이 설치되면 함께 자동으로 resolve 된다.

  plugin을 apply한다는 의미는 해당 plugin을 실제로 build script에 적용한다는 의미이다. 이때 Project의 Plugin.apply()를 사용하여 적용한다.

  또한 plugins {} 블록을 통해서도 plugin을 적용할 수 있다.

- subproject에 plugin 적용하기

  기본적으로 plugins {}는 등록한 plugin을 resolve 및 apply하는 역할을 한다. 이때 apply false를 plugin에 적용하면 resolve 할 뿐 apply는 적용하지 않는다.

  따라서 root / master project에서 resolve한 plugin을 subproject에서 apply할 수 있다.

- Plugin Management

  pluginManagement {}는 오직 `settings.gradle`의 첫번째 줄이나 `init.gradle`에만 존재할 수  있다.

  ```groovy
  // settings.gradle
  pluginManagement {
      resolutionStrategy {
      }
      repositories {
      }
  }
  
  // init.gradle
  settingsEvaluated { settings ->
      settings.pluginManagement {
          resolutionStrategy {
          }
          repositories {
          }
      }
  }
  ```

  위와 같이 `settings.gradle`과 `init.gradle`에 pluginManagement를 적용할 수 있다.

  특히 resolutionStrategy를 통해 plugin의 rule을 적용할 수 있다.

  ```groovy
  pluginManagement {
      resolutionStrategy {
          eachPlugin {
              if (requested.id.namespace == 'org.gradle.sample') {
                  useModule('org.gradle.sample:sample-plugins:1.0.0')
              }
          }
      }
      repositories {
          maven {
              url '../maven-repo'
          }
          gradlePluginPortal()
          ivy {
              url '../ivy-repo'
          }
      }
  }
  ```

  


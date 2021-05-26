# Day 03

## 1. Gradle

Gradle은 차세대 빌드 툴로 자바 진영에서 Spring 관련 제품, 안드로이드 관련 제품\(2013년부터 안드로이드 공식 빌드 툴\) 등에 사용되는 개발 빌드 툴이다.

※ 빌드 툴이란?

테스트 실행, 패키징, 배포 등 정형화된 작업을 자동화하기 위한 SW이다.

※ DevOps

SW 개발\(Development\)과 운영\(Operations\)의 합성어로서, 소프트웨어 개발자와 정보 기술 전문가 간의 소통, 협업 및 통합을 강조하는 개발환경이나 문화를 말한다.

이를 원할하게 만들기 위해서는 작성한 코드를 빠르게 배포하여 사용자의 대변인인 운영자에게 피드백을 받고 그것을 반영해야한다. 이를 위해서는 빌드가 자동화되어야한다.

Gradle은 스크립트 언어를 이용한 규칙 기반 빌드 툴이다. 또한 JVM 언어인 Groovy / kotlin을 이용하여 스크립트를 작성한다.

※ Maven vs Gradle

메이븐의 규칙 기반 빌드 접근법은 프로젝트 종류별로 디렉터리 구조와 빌드 순서를 표준화한다. 이를 따르면 빌드 내용을 상세히 지시하지 않아도 빌드가 된다.

단, 이 경우에 복잡한 메이븐 규칙을 이해해야하고 규칙에 맞지 않는 프로젝트에서는 사용하기 힘든 경우가 나타나는 단점이 있다.

또한, 메이븐은 XML 기반의 DSL\(도메인 특화 언어\)이 사용되어 빌드 스크립트 유연성에 제약이 생긴다. 이 때문에 스크립트에 적을 수 있는 내용은 어떤 플러그인으로 실행대상을 구성할지, 플러그인에 전달할 파라미터는 무엇인지 밖에 없다.

스크립트에 원칙적으로 로직을 넣을 수 없기 때문에 기존 플러그인이 구현하지 않는 기능을 넣기 위해서는 직접 플러그인을 만들어야한다. \(어려움\)

이를 Gradle에서는 로직을 직접 작성하고 Java 클래스를 직접 이용하므로 API를 통해 Gradle의 기능을 직접 이용할 수 있다.

> gradle의 주요 컨셉 : projects와 tasks

gradle의 빌드는 한개 이상의 `project`로 이뤄진다. 그리고 각각의 `project`는 한개 이상의 `task`로 이뤄진다.

task는 빌드를 수행하는 다양한 작업 중 하나의 작업을 나타낸다.

> ex\) 컴파일 작업, JAR 생성, Javadoc 생성 등

gradle을 다운로드 받고 환경변수 설정을 해주면 gradle 명령어를 command에서 실행할 수 있다.

[gradle 다운로드](https://gradle.org/install/>)

gradle 명령은 현재 폴더 안에 있는 `build.gradle`을 찾는다. 이 `build.gradle`을 빌드스크립트 \(build configuration script\)라고 부른다. 이 빌드스크립트는 project와 task들을 정의하는 역할을 한다.

```groovy
task hello {
    doLast {
        println 'Hello World!'
    }
}
```

위 스크립트는 gradle로 `Hello World!`를 출력하는 스크립트이다. `gradle -q hello`를 command창에 입력하면 된다.

> -q 옵션
>
> -q는 gradle 명령시 자동으로 출력되는 gradle 로그 메세지를 보이지 않게 하기 위해 사용한다.

### gradle dsl

gradle은 기본적으로 groovy를 사용하여 빌드스크립트를 작성할 수 있다. 또한, gradle 5버전 이후 부터는 확장자명 `kts`를 붙여 kotiln을 빌드스크립트 언어로 활용할 수도 있다.

groovy와 kotlin은 프로그래밍 언어이므로 빌드스크립트를 프로그래밍 적으로 작성할 수 있다는 뜻이 된다!

```groovy
task upper {
    doLast {
        String someString = 'Hello World!'
        println "Original: $someString"
        println "Upper case: ${someString.toUpperCase()}"
    }
}
```

`gradle -q upper`를 통해 나타나는 결과는 다음과 같다.

```text
Original: Hello World!
Upper case: HELLO WORLD!
```

이처럼 변수와 메서드를 활용하여 빌드 스크립트를 짤 수 있다.

반복문은 다음과 같이 활용할 수 있다.

```groovy
task count {
    doLast {
        4.times {
            print "$it "
        }
    }
}
```

times로 반복을 구현할 수 있다. 참고로 `it`은 기본적으로 반복 count에 접근하는 변수이다.

만약 `it`대신 다른 변수이름으로 접근하고 싶다면 다음과 같은 방법이 있다.

```groovy
task count {
    doLast {
        4.times { count ->
            print "$count "
        }
    }
}
```

위 결과는 다음과 같다.

```text
0 1 2 3
```

### Gradle task 의존관계설정

task끼리 의존 관계를 설정할 수 있다. `dependsOn`을 통해 설정 가능하다.

```groovy
task hello {
    doLast {
        println 'Hello world!'
    }
}
task intro {
    dependsOn hello
    doLast {
        println "I'm Gradle"
    }
}
```

위 예시는 hello와 intro task가 존재한다. 이때 intro에는 hello task를 dependsOn으로 설정하여 intro를 실행할 경우 먼저 hello가 실행되도록 만들었다. 위 예시의 결과는 다음과 같다.

```text
Hello world!
I'm Gradle
```

### Extra task properties

task에서 자신만의 프로퍼티를 추가하는 방법이다.

```groovy
task myTask {
    ext.myProperty = 'myValue'
}

task printTaskProperties {
    doLast {
        println myTask.myProperty
    }
}
```

위와 같이 myTask라는 task에 변수를 추가할 수 있다. \(지역변수가 아닌 외부에서도 접근 가능한\)

### Default tasks

gradle은 다른 특정 task를 실행하지 않아도 하나 이상의 task가 기본적으로 실행하도록 기능을 제공한다. `defaultTasks`라는 키워드를 통해 수행 가능하다.

```groovy
defaultTasks 'clean', 'run'

task clean {
    doLast {
        println 'Default Cleaning!'
    }
}

task run {
    doLast {
        println 'Default Running!'
    }
}

task other {
    doLast {
        println "I'm not a default task!"
    }
```

위 예시에서는 clean과 run이 기본 실행 task로 지정되어있다.

이를 `gradle -q`명령으로 실행해보면 다음과 같은 결과가 나온다.

```text
Default Cleaning!
Default Running!
```

이는 기본적으로 clean과 run을 기본 실행 task로 설정하였으므로 `gradle -q clean run`과 같은 결과가 된다. 먼약 mulit-project를 빌드하는 경우 subproject에서 기본 실행 task가 없는 경우 부모 프로젝트에서 정의한 기본 실행 task를 사용한다.

### Configure by DAG

Gradle은 기본적으로 설정단계\(Configuration phase\)와 실행단계\(Execute phase\)를 가진다. 설정 단계 이후로는 Gradle은 실행해야하는 모든 task를 아는 상태가 된다. Gradle은 이 정보를 사용할 수 있도록 hook을 제공해준다.

```groovy
task distribution {
    doLast {
        println "We build the zip with version=$version"
    }
}

task release {
    dependsOn 'distribution'
    doLast {
        println 'We release now'
    }
}

gradle.taskGraph.whenReady { taskGraph ->
    if (taskGraph.hasTask(":release")) {
        version = '1.0'
    } else {
        version = '1.0-SNAPSHOT'
    }
}
```

whenReady를 통해 release 테스트가 taskGraph에 없음 경우에만 `SNAPSHOT`을 붙여주도록 만든 예제이다.

## Authoring Task

### Task outcomes

task 실행할 때 gradle은 task마다 라벨링을 한다. 이 라벨링은 task가 실행중에 어떤 action이 있는지, 어떤 action을 취해야 하는지, action을 실행했는지, 어떤 action이 일련의 변화를 만들었는지 등에 기초한다.

label에는 다음과 같은 종류가 있다.

1. \(no label\) / EXECUTED

   task가 action들을 실행했을경우

   * 위 task는 action들을 가지고 빌드의 한부분으로 실행되야하는 action들을 Gradle이 결정한다. 
   * task는 action을 가지지 않을 수도 있다. 단지 일련의 의존성을 가지고 의존성이 실행 될 수 있다.

2. UP-TO-DATE

   task의 결과물에 변화가 없는 경우

   * task의 output과 input에 변화가 없는 경우
   * task가 action을 가지는데 output에 변화가 없다고 판단하는 경우
   * task가 action은 없고 일종의 의존성을 가지지 만 의존성 모두가 `UP-TO-DATE`, `SKIPPED`, `FROM-CACHE` 인 경우
   * 어떠한 action과 의존성이 없는 경우

3. FROM-CACHE

   task의 결과물이 이전 실행 결과와 같은 경우

4. SKIPPED

   task가 실행되지 않는 경우

   * 명시적으로 command-line에서 배재된 경우
   * onlyIf 등으로 제어된 경우

5. NO-SOURCE

   task가 실행될 필요가 없는 경우

   * task에 input과 output이 있는데  source가 없는 경우

### Locating tasks

task에 대한 설정이나 의존 관계를 위해서 task가 어디에 위치하고 있는지 알 필요가 있다.

이를 위한 방법은 Gradle에서 제공해준다.

1. basic

```groovy
task hello
task copy(type: Copy)

// Access tasks using Groovy dynamic properties on Project

println hello.name
println project.hello.name

println copy.destinationDir
println project.copy.destinationDir
```

1. tasks collection 사용

```groovy
task hello
task copy(type: Copy)

println tasks.hello.name
println tasks.named('hello').get().name

println tasks.copy.destinationDir
println tasks.named('copy').get().destinationDir
```

1. getByPath\(\) 사용

   `getByPath()`는 task 이름, 상대경로, 절대경로를 가지고 호출할 수 있다.

```groovy
project(':projectA') {
    task hello
}

task hello

println tasks.getByPath('hello').path
println tasks.getByPath(':hello').path
println tasks.getByPath('projectA:hello').path
println tasks.getByPath(':projectA:hello').path
```

### Ordering tasks

실행 순서를 제어하는데는 두가지방법이 있다. `mustRunAfter`와 `shouldRunAfter`가 있다.

두 방법은 공통적으로 task 간 실행되는 순서를 강제적으로 지정하는 방법이다.

task 간 순환참조가 발생하는 경우에는 예외가 발생한다. 반면 `shouldRunAfter`의 경우는 순환참조를 무시하고 `shouldRunAfter`로 지정된 순서대로 실행된다. 그리고 병렬 실행 시 `shouldRunAfter` task와는 별도로 모든 의존성을 충족하면 `shouldRunAfter`를 무시한다.

[mustRunAfter와 shouldRunAfter의 차이](https://stackoverflow.com/questions/36794690/what-is-the-difference-between-mustrunafter-and-shouldrunafter-in-task-ordering)

> 의존관계와 순서 지정 간의 차이
>
> 의존관계는 해당 task가 실행되기 전 관계를 가진 task를 모두 실행한 후 실행한다.
>
> 반면 `mustRunAfter`나 `shouldRunAfter`의 경우는 선후 관계를 가진 task가 모두 실행 task에 있어야 적용된다.

### Up-to-date checks \(Incremental Build\)

gradle과 같은 빌드 툴에서 가장 중요한 부분 중 하나는 이미 진행한 일의 중복을 피하는 것이다. ex\) `compilation`

* Task inputs and outputs

대부분의 경우 task는 `input`을 가지고 `output`을 만들어낸다.

이때 `input`의 중요한 특징은 하나 또는 여러 `output`에 영향을 미친다는 것이다. source file \(`.java`\)로부터 java bytecode 파일\(`.class`\)을 만들어낸다.

compile시 memory 최대 사용량을 `memoryMaximumSize` property로 결정할 수 있다. 단, 이 memory 최대 사용량은 bytecode를 생성하는데는 영향을 미치지 않는다.

* build 작성에서 생각해야 할 점

빌드 작성에 있어서 Gradle에게 어떤 task property가 input이고 어떤 것이 output인지 알려줘야한다.

만약, task property가 output에 영향을 준다면 반드시 input을 등록해야한다. 그렇지 않으면 task는 `UP-TO-DATE`상태로 인식하게 된다.

반면, output에 영향을 끼치지 않는다면 input을 등록하면 안된다. 이는 input이 필요하지 않을때에도 실행될 가능성이 있기 때문이다.

마지막으로 같은 input에도 다른 output이 나타나는 결정적이지 않은 task는 사용에 유의해야한다. 이는 up-to-date check를 수행하지 않기 때문이다.

* input / output으로서의 task property 등록하기
  1. Custom task

     만약 custom task를 사용하고 싶다면 다음의 두단계를 거쳐야 incremental build를 수행할 수 있다.

     * input / output으로 사용할 typed properties 생성
     * property 각각에 적절한 어노테이션 추가

     gradle은 input과 output으로 세가지 범주의 type을 지원한다.

     * Simple values

       String과 numbers와 같은 타입, 일반적으로 simple value는 Serializable을 구현한 타입이 된다.

     * FileSystem types

       FileCollection, Project.file, Project.files 객체

     * Nested values

       task의 input과 output이 custom type의 properties로 만들 수 있다.

     > 추후 내용은 필요에 따라 학습할 예정

### Task Rules

task를 호출하는 데 rule을 지정할 수 있다. tasks.addRule을 통해 가능하다.

```groovy
tasks.addRule("Pattern: ping<ID>") { String taskName ->
    if (taskName.startsWith("ping")) {
        task(taskName) {
            doLast {
                println "Pinging: " + (taskName - 'ping')
            }
        }
    }
}
```

위 예시와 같이 rule은 String 파라미터로 설명\(description\)할 수 있다. 세부 내용은 closer로 작성. rule은 또한 command line에서도 호출할 때도 똑같이 적용된다.

### Finalized

task graph

> task간의 의존관계를 시각적으로 표현한 그래프 \(`방향성 비순환 그래프`\)

gradle task에서 `dependsOn`으로 의존관계를 지정했다면 해당 task간의 의존성이 생긴다.

따라서, `dependsOn`으로 선언된 task를 먼저 실행해야만 해당 task를 실행할 수 있다.

이때, 선행 task에서 exception이 발생한다면 실행이 중지되는데 만약 `finallzedBy`로 종료시 반드시 실행하는 task를 지정하면 task graph가 바뀌게 된다.

### Lifecycle tasks

Lifecycle task는 그 자체로 동작하지 않는 task이다. 일반적으로 어떠한 action을 가지지 않는다.

이런 Lifecyle task는 다음과 같은 컨셉을 가진다.

1. work-flow step
2. a buildable thing
3. 같은 로직의 task들을 실행하기 위한 편의성 task

위와 같은 Lifecycle task들로는 build, assemble, check 등이 있다. 이외에 다른 plugin을 적용한다면 추가적으로 더 등록될 수 있다.

Lifecyle task는 action을 가지지 않으므로 전적으로 의존하는 task에 의해 output이 결정된다. 만약, 의존 task 중 어떤 것이라도 실행된다면 `EXCUTED` 상태가 된다. 반대로 모든 의존 task들이 이미 반영되었거나, `SKIPPED` 또는 `FROM CACHE`라면 해당 Lifecycle task는 `UP-TO-DATE` 상태가 된다.


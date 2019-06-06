# Day 06

## 1. 개발자가 반드시 정복해야 할 객체지향과 디자인패턴

> DI와 서비스 로케이터

### DI

DI는 Dependency Injection의 약자로 의존성 주입이라는 뜻을 가진다.

소프트웨어는 어플리케이션 영역과 메인 영역으로 나눠지는데 메인 영역에서 객체를 연결하기 위해 사용하는 방법이 바로 **DI**이다.

- 어플리케이션 영역

고수준 정책과 저수준 구현을 모두 포함한 영역

- 메인 영역

소프트웨어가 동작하도록 각 객체를 연결해주는 영역

> 어플리케이션 영역과 메인 영역은 Robert C. Martin이 소프트웨어를 설명하기 위해 두 구분으로 나눈 영역

1. 어플리케이션 영역과 메인 영역

어플리케이션 영역은 이전에 학습했던 것처럼 요구사항의 비즈니스 로직을 구현한 고수준 정책과 저수준 구현 클래스/패키지로 이뤄진다.

반면 메인 영역은 위 어플리케이션 영역의 객체를 연결해주는 역할을 한다.

메인영역은 다음과 같은 작업을 한다.

- 어플리케이션 영역에서 사용할 객체 생성
- 각 객체 간의 의존 관계 설정
- 어플리케이션 실행

메인 영역은 어플리케이션 영역의 객체를 생성, 설정, 실행하는 책임을 가진다. 때문에 어플리케이션 영역에서 사용할 하위 수준의 모듈을 변경하고 싶다면 메인 영역을 수정해야한다.

메인 영역은 어플리케이션 영역을 의존한다. 이는 메인 영역의 변화는 어플리케이션 영역에는 독립적이라는 뜻이 된다. 즉, 아무런 영향을 끼치지 않는다.

이 때, 메인영역에 의존성을 제공해주는 방법으로 DI와 서비스 로케이터가 있다. 서비스 로케이터는 메인영역에서 사용할 객체를 제공해 줄 책임을 갖는 객체이다. 단, 서비스 로케이터에는 몇 가지 단점이 존재하므로 실제로는 DI를 사용하는 것이 일반적이다.

2. DI를 이용한 의존 객체 사용

구현 클래스를 직접 사용하여 객체를 생성하게되면 의존 역전 원칙을 위반하며 곧 확장 폐쇄 원칙을 위반하게된다. 

이런 문제를 해결하기 위해 필요한 객체를 직접 생성하거나 찾지 않고 외부에서 할당해주는 DI를 사용하게 된 것이다.

DI를 적용하는 것은 간단하다. 사용할 객체를 전달받을 수 있는 방법을 제공해주면 된다. 

```java
public class Lotto {
	private List<LottoNumber> numbers;
	
	public Lotto(List<LottoNumber> numbers) {
		this.numbers = numbers;
	}
	
	// ...
}
```

위 Lotto 클래스와 같이 생성자로 사용할 객체를 전달하여 DI를 구현할 수 있다.

생성자방식과 함께 setter를 활용하여 DI를 구현할 수도 있다.

이렇게 DI는 생성자 또는 setter를 통해서 해당 객체에서 사용해야하는 객체를 전달받는 것으로 구현할 수 있다.

- 각 방식의 장단점

보통은 생성자 방식을 선호한다. 그 이유는 객체 생성 시점에 필요한 모든 의존 객체를 준비할 수 있기 때문이다. 이런 이유로 생성 시점에서 의존 객체가 정상인지 확인할 수 있다. 그리고 생성 시점에서 필요한 의존 객체를 모두 할당받으므로 한 번 객체가 생성되면 정상적으로 동작할 수 있다.

단, 생성자 방식은 의존 객체가 먼저 생성해야하기 때문에 의존 객체를 먼저 생성할 수 없는 상황이라면 setter 방식을 써야만한다. setter 방식은 의존 객체가 많을 경우 메서드 이름으로 어떤 DI를 실행하는지 확인할 수 있으므로 코드 가독성을 높여주는 효과를 가진다.

### Service Locator

프로그램 개발 환경이나 프레임워크의 제약으로 DI를 사용하지 못하는 경우는 서비스 로케이터를 이용할 수 있다.

서비스 로케이터는 어플리케이션에서 필요로 하는 객체를 제공해주는 책임을 갖는다.

- 서비스 로케이터의 구현

서비스 로케이터는 의존 대상이 되는 객체 별로 제공 메서드를 정의하여 구현한다.

의존 객체가 필요한 곳에서 (주로 메인 영역) 서비스 로케이터를 불러와 필요한 객체를 제공하는 방식으로 사용한다. 단, 서비스 로케이터가 제대로 동작하기 위해서는 스스로 어떤 객체를 제공해야하는지 알아야한다.

서비스 로케이터는 어플리케이션 영역의 객체에서 직접 접근하므로 어플리케이션 영역에 위치한다. 메인 영역에서는 서비스 로케이터를 불러오고 이를 이용해서 초기화하는 방식을 사용한다.

- 객체 등록 방식의 서비스 로케이터 구현

1. 서비스 로케이터 생성시 사용할 객체 전달
2. 서비스 로케이터 인스턴스를 지정하고 참조하기 위한 static 메서드 제공의존 역전 원칙을 위배한다.

- 상속으로 서비스 로케이터 구현

1. 객체를 구하는 추상 메서드를 제공하는 상위 타입 구현
2. 상위 타입을 상속받은 하위 타입에서 사용할 객체 설정

- 제네릭 / 템플릿을 이용한 서비스 로케이터 구현

서비스 로케이터의 단점 중 하나는 SOLID에서 인터페이스 분리 원칙을 위반한다는 점이다. 이를 해결하기 위해서는 의존 객체마다 서비스 로케이터를 작성하는 것이다. 단, 이경우는 중복된 코드를 양산하는데 이를 제네릭 / 템플릿으로 해결 할 수 있다.

- 단점

서비스 로케이터는 인터페이스 분리 원칙, 개방 폐쇄 원칙을 위배한다. 때문에 유지보수가 힘든 코드를 만들 가능성이 높다. 따라서 특별한 경우가 아니라면 변경의 유연함이 좋은 DI를 사용하는 것이 좋다.

## 2. gradle

### settings.gradle vs init.gradle

`init.gradle`은 초기화 스크립트로 빌드가 시작되기 전에 실행된다.

`init.gradle`은 다음과 같은 기능을 수행한다.

1. 시스템 전역 설정. 예를들면 커스텀 플러그인 찾을 위치 지정
2. 현재 환경에 따른 프라퍼티 설정. 개발 장비, 지속적 통합 장비 등에 따른 프라퍼티 값 바꿔지기 같은 경우
3. 빌드에 필요한 개발자의 개인적 설정 정보
4. JDK 위치 등 장비 전용 설정
5. 빌드 리스너(build listener)를 등록하여 Gradle 빌드 이벤트를 받는다.
6. 빌드 로거를 등록
7. `buildSrc` 프로젝트의 클래스에는 접근할 수 없다.

실행은 `-I`나 `--init-script`옵션을 통해서 init.gradle의 경로를 지정하면 빌드 전에 실행된다.

여러 초기화 스크립트를 등록하여 사용할 수도 있으며 이 경우 동일한 경로에 있을때 알파뱃 순서대로 실행한다.

[gradle 초기화 스크립트 init.gradle 참고](<http://kwonnam.pe.kr/wiki/gradle/init_scripts>)

`settings.gradle`도 `init.gradle`, `build.gradle`과 마찬가지고 Groovy Script이다.

`settings.gradle`은 주로 multi-project build시에 설정사항을 적용하기 위해 작성한다.

따라서, `build.gradle`과는 다르게 빌드당 1번만 실행하게 된다.

`settings.gradle`은 보통 multi-project build시에 사용한다. 따라서, 해당 프로젝트가 multi-project라면 반드시 작성하여 gradle에게 알려주어야하며 single-project라면 선택적으로 작성가능하다.

아래는 gradle-settings라는 멀티 프로젝트에 a와 b라는 프로젝트를 추가하는 스크립트이다.

```groovy
rootProject.name = 'gradle-settings'

include 'a', 'b'
```

[settings.gradle 참고](<https://www.baeldung.com/gradle-build-settings-properties>)

### understanding the build lifecycle

gradle은 task와 dependancy들로 이뤄진다.

gradle은 dependancy 순서대로 task 실행 순서를 보장하며 이들 task는 한번만 실행된다. 이때 gradle은 task 실행을 관리하기 위해 DAG (Directed Acyclic Graph)를 만든다.

DAG는 build를 위해 등록된 task를 수행하기 위한 일종의 dependancy graph이다. 빌드시 task를 실행하기 전 dependancy graph를 먼저 그린 후에 task를 실행하게 된다.

- build phases

gradle의 빌드는 다음 3가지 단계로 이뤄진다.

1. Initialization (초기화)

   초기화 단계에서 gradle은 현재 빌드하려는 프로젝트들을 결정하고 각 프로젝트에 `Project` 인스턴스를 생성한다.

2. Configuration (설정)

   설정 단계에서는 Project 객체의 설정사항을 구성한다. 빌드에 참여하는 모든 프로젝트의 build.gradle을 실행하는 단계이다.

3. Execution (실행)

   설정 단계에서 실행하기 위한 task의 부분 집합을 구성한다. task 부분집합은 현재 폴더와 gradle 커멘드 라인에서 전달된 task 이름으로 결정된다. gradle은 이 task 집합을 실행하게된다.

- settings file

settings.gradle은 초기화 단계에서 실행된다.

multi-project에서는 root 폴더에 반드시 필요하며 single-project의 경우는 선택적으로 사용가능하다.

multi-project build시에 settings.gradle에서 체 프로젝트 빌드를 구성하는 서브프로젝트들에 대한 설정을 해야한다. 

```groovy
// settings.gradle
println 'This is executed during the initialization phase.'

// build.gradle
println 'This is executed during the configuration phase.'

task configured {
    println 'This is also executed during the configuration phase.'
}

task test {
    doLast {
        println 'This is executed during the execution phase.'
    }
}

task testBoth {
	doFirst {
	  println 'This is executed first during the execution phase.'
	}
	doLast {
	  println 'This is executed last during the execution phase.'
	}
	println 'This is executed during the configuration phase as well.'
}
```

위 결과는 다음과 같다.

```
> gradle test testBoth
This is executed during the initialization phase.

> Configure project :
This is executed during the configuration phase.
This is also executed during the configuration phase.
This is executed during the configuration phase as well.

> Task :test
This is executed during the execution phase.

> Task :testBoth
This is executed first during the execution phase.
This is executed last during the execution phase.

BUILD SUCCESSFUL in 0s
2 actionable tasks: 2 executed
```

test와 testBoth task를 실행한 결과이다.

먼저 build 실행의 첫 단계로 초기화 단계가 실행된다. 따라서 settings.gradle의 스크립트 내용이 실행된다.

`This is executed during the initialization phase`가 출력되는 것이다.

다음 설정 단계에서는 각 프로젝트의 build.gradle에 있는 task를 모두 실행한다. 때문에 실행 대상이 아닌 configured task의 내용 `This is also executed during the configuration phase.`이 출력되는 것이다. 

단, 이 단계에서는 task 내부의 doFirst와 doLast 블럭에 있는 내용은 실행되지 않는다. 이들 내용은 실행 단계에서 실행된다.

이런 과정을 거치기 때문에 위와 같은 결과가 나오게 된 것이다.

- Multi-project builds

Multi-project builds란 gradle 실행시 하나 이상의 프로젝트를 빌드하는 것을 의미한다.

1. Project location

Multi-project builds는 항상 1개의 root로 이뤄진 tree 형식으로 표현된다. 이때 tree의 node들은 하나의 프로젝트로 구성한다. 각 프로젝트들은 path를 가지는데 이는 project의 위치를 나타낸다. 이 위치는 해당 project의 실제 위치이다.

tree는 settings.gradle에 설정한 내용대로 구성된다. 

2. Building the tree

settings.gradle에서 계층구조적(`Hierarchical`)이거나 수평적인(`flat`) 레이아웃의 tree를 구성할 수 있다.

include를 사용하여 Hierarchical한 구조를 includeFlat으로 flat 구조를 구성할 수 있다.

```groovy
// Hierarchical layout
// settings.gradle
include 'project1', 'project2:child', 'project3:child1:leaves'
```

include는 인자로 project의 경로를 받는다. 이때 해당 인자는 기본적으로 상대 경로로 인식된다. 

위의 예시에서 `project2:child`는 `project2/child`와 동일한 경로의 폴더가 된다. 

여기서 알아야 할 점은 인자로 tree의 leave를 줘야한다는 점이다. 즉, `project3:child1:leaves`는 project3, project3:child1, project3:child1:leaves라는 프로젝트를 생성하게된다.

includeFlat은 프로젝트 구조를 Flat 구조로 만들어준다.

```groovy
// Flat layout
// settings.gradle
includeFlat 'project3', 'project4'
```

includeFlat은 폴더명을 인자로 받는다. 단, 등록하는 프로젝트명은 root project의 하위 프로젝트여야한다.

3. modifying elements of the project tree

settings.gradle로 만들어지는 multi-project tree는 project descriptor로 구성되어있다. project descriptor를 수정하여 빌드 설정을 변경할 수 있다.

```groovy
// settings.gradle
rootProject.name = 'main'
project(':projectA').projectDir = new File(settingsDir, '../my-project-a')
project(':projectA').buildFileName = 'projectA.gradle'
```

[Project Descriptor 더 알아보기](<https://docs.gradle.org/current/javadoc/org/gradle/api/initialization/ProjectDescriptor.html>)

- Initialization

Gradle은 다음과 같은 방식으로 settings.gradle을 찾는다.

1. 현재 폴더와 같은 수준의 폴더들 중에서 master 폴더를 찾는다. 
2. 없다면 상위 폴더를 탐색한다.
3. 없다면 single project로 빌드한다.
4. 만약 settings.gradle이 발견되면  settings.gradle을 분석하여 현재 프로젝트가 계층형 구조를 가지는지 확인한다. 구조를 가진다면 multi-project로 아니라면 single-project로 빌드한다.

Gradle이 이런 과정을 거치는 이유는 해당 폴더가 multi project의 하위 프로젝트인지 확인할 필요가 있기 때문이다. 이렇게 자동적으로 settings.gradle을 찾는 이유는 결국 현재 프로젝트가 multi-project 구조를 가지는지 아닌지 확인하여 빌드하기 위함이다.

- Responding to the lifecycle in the build script

빌드 진행중 lifecycle을 통해 빌드 과정에 대한 알림을 받을 수 있다. 일반적으로 2가지 방법으로 받을 수 있는데 바로 특정 listener interface를 구현하거나 알림에 대한 closure를 제공하는 것이다.

프로젝트에 대한 평가 전후로 알림을 build script에 보낼 수 있다. 이를 통해 빌드 과정에서 추가적인 설정을 제공하거나 로깅, 프로파일링 등을 적용할 수 있다.

```groovy
// build.gradle
allprojects {
    afterEvaluate { project ->
        if (project.hasTests) {
            println "Adding test task to $project"
            project.task('test') {
                doLast {
                    println "Running tests for $project"
                }
            }
        }

    }
}
```

위와 같이 `afterEvaluate() `를 사용하여 project에 대한 평가가 끝난 후의 logic을 closure로 추가할 수 있다.

`afterEvaluate()`와 더불어 `afterProject()` 도 있다. 이는 프로젝트 평가가 성공했던 실패했던 상관없이 이후의 작업을 하는데 사용한다. 때문에 커스텀 logging을 추가할 때 많이 사용한다.

 [추가 내용 참고](<https://docs.gradle.org/current/javadoc/org/gradle/api/ProjectEvaluationListener.html>)

- task events

project 이외에도 gradle의 핵심 구성요소인 task도 notification을 받아 추가 기능을 구현할 수 있다.

```groovy
// build.gradle
tasks.whenTaskAdded { task ->
    task.ext.srcDir = 'src/main/java'
}

task a

println "source dir is $a.srcDir"
```

위와 같이 tasks라는 TaskContainer 타입의 객체에 Action을 추가할 수 있다. 위는 whenTaskAdded를 추가하여 task를 추가할 때마다 수행할 작업을 closure로 제공한다.

[추가적인 task graph events](<https://docs.gradle.org/current/javadoc/org/gradle/api/execution/TaskExecutionGraph.html>)

[Build Lifecycle 참고](https://docs.gradle.org/current/userguide/build_lifecycle.html)
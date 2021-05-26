# 2019.04.18 Thursday

## 1. Gradle

Gradle은 차세대 빌드 툴로 자바 진영에서 Spring 관련 제품, 안드로이드 관련 제품\(2013년부터 안드로이드 공식 빌드 툴\) 등에 사용되는 개발 빌드 툴이다.

※ 빌드 툴이란? 테스트 실행, 패키징, 배포 등 정형화된 작업을 자동화하기 위한 SW이다.

※ DevOps SW 개발\(Development\)과 운영\(Operations\)의 합성어로서, 소프트웨어 개발자와 정보 기술 전문가 간의 소통, 협업 및 통합을 강조하는 개발환경이나 문화를 말한다.

이를 원할하게 만들기 위해서는 작성한 코드를 빠르게 배포하여 사용자의 대변인인 운영자에게 피드백을 받고 그것을 반영해야한다. 이를 위해서는 빌드가 자동화되어야한다.

Gradle은 스크립트 언어를 이용한 규칙 기반 빌드 툴이다. 또한 JVM 언어인 Groovy / kotlin을 이용하여 스크립트를 작성한다.

※ Maven vs Gradle

메이븐의 규칙 기반 빌드 접근법은 프로젝트 종류별로 디렉터리 구조와 빌드 순서를 표준화한다. 이를 따르면 빌드 내용을 상세히 지시하지 않아도 빌드가 된다.

단, 이 경우에 복잡한 메이븐 규칙을 이해해야하고 규칙에 맞지 않는 프로젝트에서는 사용하기 힘든 경우가 나타나는 단점이 있다.

또한, 메이븐은 XML 기반의 DSL\(도메인 특화 언어\)이 사용되어 빌드 스크립트 유연성에 제약이 생긴다. 이 때문에 스크립트에 적을 수 있는 내용은 어떤 플러그인으로 실행대상을 구성할지, 플러그인에 전달할 파라미터는 무엇인지 밖에 없다.

스크립트에 원칙적으로 로직을 넣을 수 없기 때문에 기존 플러그인이 구현하지 않는 기능을 넣기 위해서는 직접 플러그인을 만들어야한다. \(어려움\)

이를 Gradle에서는 로직을 직접 작성하고 Java 클래스를 직접 이용하므로 API를 통해 Gradle의 기능을 직접 이용할 수 있다.

### Gradle 시작

1. Groovy 특유의 문법
   * 문자열

     문자열 표기 방법은 2가지가 있다.

     작은따옴표\('\) : 자바의 문자열과 거의 같은 용도 큰따옴표\("\) : 문자열 내부에 $ 기호로 동적인 내용을 넣을 수 있다. 내부적으로 그루비의 Gstring 클래스가 사용된다.

     또한 작은따옴표 또는 큰따옴표를 연달아 3개 쓰면 문자열을 여러 줄 표기할 수 있다. \(python과 비슷\)

   * 메서드 호출시 괄호 생략 인수가 있을 때 1개든 2개든 몇개든 괄호 없이 메서드를 호출할 수 있다. 다만, 인수가 없으면 괄호가 있어야한다.
   * def를 사용한 형 지정 생략 이는 변수 뿐만 아니라 메서드의 인수나 반환 값에도 사용가능하다. \(duck typing\)
   * 클로저 클로저는 중괄호{} 를 이용하여 정의하고 클로저명.call\(\) 이나 클로저명\(\) 처럼 일반함수처럼 실행할 수 있다.
2. 주요 명령줄 옵션
   * -i \(--info\) 로그 레벨이 INFO로 설정되며 빌드 스크립트의 파일명 등이 출력된다.
   * -s \(--stacktrace\) 예외 발생 시 사용자 예외 부분만 스택 트레이스로 표시한다. \(문제 발생 지점이 사용자 코드에 있다면 대부분 이 로그 레벨로 해결 가능하다.\)
   * -S \(--full-stacktrace\) 예외 발생시 모든 스택 트레이스를 표시 \(문제 발생 지점이 그레이들에 있을 때\)
   * -d \(--debug\) 로그 레벨이 DEBUG로 설정, 매우 상세한 정보가 출력된다.
   * -q \(--quiet\) 디버그와 반대로 불필요한 메세지를 보고 싶지 않을 경우, 이 경우 테스크의 명시적 출력 외에는 메세지가 표시되지 않는다.

     ※ 자주 사용하는 옵션 : -b \(--build-file\) 이것은 기본 build.gradle 파일을 다른 이름으로 지정할 때 사용한다.
3. 프로젝트 자동화와 빌드
   * Init 테스크를 이용한 프로젝트 자동 생성 gradle init --type java-library 라는 명령어를 치면 Library라는 자바코드가 나타나면서 프로젝트의 디렉터리 구조를 자동 생성할 수 있다.

     이때 gradle은 maven의 규칙을 따르므로 프로덕션 코드는 src/main/java 아래에, 테스트코드는 src/test/java 아래에 위치한다.

     ※ 자동 생성된 build.gradle의 내용

     11: apply plugin: 'java' Java Plugin을 적용하겠다는 내용이다. 이를 통해 자바 프로젝트 규칙과 자바 프로젝트 빌드에 필요한 테스크가 추가된다.

     14: repositories{ 17: mavenCentral\(\) 18: }

     의존관계를 해결하기 위한 저장소로 maven 중앙 저장소로 지정하는 구문 이를 통해 dependencies 블록에서 지정한 의존 라이브러리를 메이븐 중앙 저장소에서 내려받을 수 있다.

     21: dependencies { 23: compile 'org.slf4j:slf4j-api:1.7.5' 29: testCompile 'junit:junit:4.11' 30: }

     의존 라이브러리로 SLF4J와 JUnit을 지정한다.

     이때 의존 라이브러리 지정은 compile 또는 testCompile을 사용한다. 이는 Java Plugin으로 정의된 환경 구성이라는 의미이다.

     complie은 프로덕션 코드를 컴파일하고 실행할 때, testCompile은 테스트 코드를 컴파일하고 실행할 때 필요하다.

     ※ providedComplie : 빌드에 참고는 하지만 빌드 결과물에는 넣지 않는다는 의미

     ※ 테스크 목록 쉽게 확인 : gradle --gui

     실제 빌드할 때는 gradle build 명령어를 실행한다.

     빌드시에는 의존 라이브러리 다운로드 - 프로덕션 코드 컴파일 - JAR파일로 압축 - 테스트코드 컴파일 및 실행 순으로 진행한다. 빌드 실행 결과물은 build 디렉터리 아래에 생성된다. classes 폴더에는 컴파일한 class파일들이 있고 libs 폴더에는 프로덕션 코드를 압축한 jar 파일이 있다.

     또한 Gradle은 효율적인 빌드를 위해 불필요한 테스크는 실행하지 않는다. 즉, 각 테스크의 입출력 파일의 변경 유무를 확인하여 이전과 같은 상태면 테스크 실행을 건너뛴다.

     빌드 실행 결과를 지우고 싶다면 gradle clean 명령어를 실행한다.

   * 테스트 실행과 결과확인

     build를 실행하면 test 테스크도 실행한다. 이때 테스트 리포트는 build/reports/tests 디렉터리에 생성된다.

     참고로 테스트만 필요한 경우는 gradle test로 실행하면 된다.

   * Gradle deamon 과 Gradle wrapper

     Gradle deamon은 JVM을 사용할 때 한번만 JVM을 실행하고 그 다음부터는 JVM을 상주시켜 명령 실행에 걸리는 시간 문제를 해결하는 방법이다.

     gradle --deamon  으로 실행할 수 있다.

     deamon을 끄기 위해서는 gradle --stop 명령어를 사용한다. 기본적으로 자동 종료되는데는 3시간정도 소요된다.

     gradle wrapper는 이미 존재하는 프로젝트를 새로운 환경에 설치할 때 별도의 설치나 설정과정없이 곧 바로 빌드 할 수 있게 하기 위해 사용한다. Java 및 Gradle의 설치 또는 버전도 신경 쓸 필요가 없다는 점이 장점이다.

     gradle wrapper 명령어로 wrapper 파일을 생성할 수 있다.

     참고로 그레이들 래퍼를 사용할 때는 gradle이 아닌 gradlew로 테스크를 실행해야한다.

### Gradle 자세히 알아보기

gradle script는 설정 script로써 script가 실행될 때, 특정 타입의 객체로 구성된다. 예를 들어 build.gradle 스크립트가 실행되면 Project 타입의 객체가 구성된다.

> Build 시에는 Project / init 시에는 Gradle / Settings 시에는 Settings 객체로 delegate

1. Settings

settings는 build하는 Project 파일들의 계층 구조를 설정하고 인스턴스화하는 용도로 설정하는 데 사용한다.

Settings 인스턴스와 settings.gradle은 1:1로 매칭된다. Gradle이 빌드를 하기위한 프로젝트를 한데 모으기 전에 Settings 인스턴스를 생성하고 상응하는 settings 파일을 실행한다.

* Assembling a Multi-Project Build

Settings 인스턴스의 목적 중 하나는 빌드에 필요한 프로젝트를 명시할 수 있도록 만드는 것이다.

> 보통 Settings.include\(java.lang.String\[\]\) 메서드를 사용하여 추가

빌드시에 root project는 항상 존재한다. Settings 인스턴스가 생성될 때 자동으로 추가된다. Root project의 이름은 기본적으로 settings 파일을 포함하는 폴더의 이름으로 설정된다. 즉, settings 파일을 포함하는 폴더가 프로젝트의 루트 프로젝트가 된다.

프로젝트가 빌드되면 ProjectDescriptor가 생성된다. 프로젝트의 몇몇 기본값을 변경하기 위해서 ProjectDescriptor를 사용할 수 있다.

* Using Settings in a Settings File

Dynamic Properties

Settings 객체는 settings 스크립트에서 사용할 수 있는 read-only의 properties를 추가할 수 있다. 이는 다음 사항의 properties를 포함한다.

```text
a. settings 폴더에 위치한 gradle.properties 파일에 정의한 properties들
b. 유저의 .gradle 폴더에 위치한 gradle.properties에 정의한 properties들
c. Command-Line에서 -P 옵션을 사용하여 제공한 properties들
```

[Settings Properties / Method 참고](https://docs.gradle.org/current/dsl/org.gradle.api.initialization.Settings.html#N1918A)

1. Gradle

> Gradle 객체는 Project.getGradle\(\)을 통해 얻을 수 있다.

[Init task를 위한 gradle properties / Method 참고](https://docs.gradle.org/current/dsl/org.gradle.api.invocation.Gradle.html#N17AF8)

1. Project

build file에서 gradle과의 상호작용에 사용할 수 있는 주요 API. Project 객체에서부터 gradle의 주요 기능에 프로그래밍적으로 접근할 수 있다.

* Lifecycle

  Project 객체와 build.gradle 스크립트는 1:1의 관계로 구성된다. 초기 빌드 동안 Gradle는 빌드하는 각각의 프로젝트에게서 Project 객체를 다음과 같은 순서로 모은다.

  1. Settings 인스턴스를 생성한다.
  2. 만약 settings.gradle이 있다면 Settings 객체를 settings.gradle의 설정사항에 맞춘다.
  3. Settings 객체의 설정사항을 사용하여 Project 객체의 계층구조를 만든다.
  4. 마지막으로 각 프로젝트의 build.gradle을 실행한다. 

* Task

  Gradle에서 Project는 Task의 집합체이다. Task란 클래스 컴파일, 유닛테스트 실행, war 압축 등의 어떤 일의 조각이다. 기존에 존재하는 Task 이외에도 TaskContainer의 create 메서드를 사용하여 task를 추가할 수 있다. TaskCollection의 getByName 메서드와 같은 TaskContainer의 lookup 메서드로 존재하는 task를 찾을 수 있다.

* Dependencies

  > artifacts?
  >
  > Build의 결과물로써 나타나는 실행파일을 뜻하며 보통은 .jar / .war / 그외의 실행파일이 된다. Archive나 file의 이름을 할당하기 위해서 build.gradle에 반드시 artifacts 블록을 설정해야한다.  
  > 해당 프로젝트에서 사용하는 의존 프로젝트를 정의한 블록. GroupId, ArtifactId, Version 순으로 명시하여 Dependency를 가져올 수 있다.

[Project의 Property / method 참고](https://docs.gradle.org/current/dsl/org.gradle.api.Project.html#N14ECC)

### 자바 프로젝트 빌드

1. 자바 프로젝트에 플러그인이 필요한 이유

Gradle의 핵심 기능은 반복 작업을 자동화하기 위한 구조 / 자동화된 처리를 실행하기 위한 부품 으로 구성되어 있다. 따라서 자동화 대상이 되는 구체적인 작업들은 모드 플러그인으로 구성되어 있으며, 핵심 기능과 분리되어 있다. 따라서 Java 플러그인을 사용하여 프로젝트를 빌드해야한다.

1. Java 플러그인이란?

Java 플러그인은 자바 빌드에 필요한 'Task', 설정을 단순화하는 '규칙', 그것을 구현하기 위한 '속성' 및 '소스 세트'가 패키지로 구성된 빌드 기능 컴포넌트이다.

* 소스 세트

소스 세트는 동시에 컴파일해야 하는 자바 코드와 리소스를 논리적으로 그룹화한 개념이다. 다음과 같은 특징이 있다.

• 빌드 스크립트에 자유롭게 추가 가능하다. • 고유한 이름이 있다. • 컴파일 입력 대상인 자바 코드와 리소스를 세트로 지정할 수 있다. • 컴파일 출력 경로를 지정할 수 있다. • 컴파일 시 클래스패스나 실행 시 클래스패스를 지정할 수 있다. • 소스 세트 단위로 전용 태스크와 전용 속성을 제공한다.

참고로 Java 플러그인에는 이미 main과 test라는 두 가지 소스 세트가 정의되어있다.

* 태스크

자바를 빌드할 때 코드 파일 컴파일 - 테스트 - Javadoc 생성 - jar 파일 생성의 작업을 수행한다.

자바 플러그인에서는 이 모든 작업이 태스크로 구현되어있다.

그 중 주요 태스크는 compile&lt;소스세트 명&gt;Java\(임의의 소스세트를 컴파일\), process&lt;소스 세트 명&gt;Resources \(임의의 소스세트의 리소스를 해당 소스 세트의 클래스패스에 복사\), &lt;소스 세트 명&gt;Classes \(위 두 작업 진행\) 이 있다.

* 규칙

Java 플러그인에는 프로덕션 코드는 src/main에 테스트 관련 코드는 src/test에 작성하자는 규칙을 가지고 있다.

* 속성

태스크를 제어하거나 규칙을 구현하기 위해 필요한 속성을 제공한다. 이 때 Gradle 규칙에서 벗어난 환경에서는 필요에 따라 속성 값을 변경하여 커스터마이징할 수 있다.

[Gradle 공식문서 참고](https://docs.gradle.org/current/dsl/)


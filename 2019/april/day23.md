# 2019.04.23 Tuesday

## 1. Gradle

### - copying a single file

파일을 복사하는 Gradle의 내장 Task인 `Copy`를 사용하여 파일을 복사할 수 있다.

```groovy
task copySingleFile(type: Copy){
    File file = file('practice.txt')
    from file
    into "$buildDir/copy"
}
```

위와 같이 from에 복사할 파일의 경로, 아래의 into에는 복사한 파일이 위치할 경로를 지정한다.

> Copy task의 경우 doLast에 from, into를 사용하는 경우 copy되지 않음

### - copying a multiple files

단일 파일 복사와 마찬가지로 파일을 복사하는 Gradle의 내장 `task`인 Copy를 사용한다.

from에서 복사할 파일의 경로를 나열하여 여러 파일을 복사할 수 있다.

```groovy
task copyMultipleFiles(type: Copy){
    from 'practice.txt', 'practice2.txt', 'practice3.txt'
    into "$buildDir/copy"
}
```

위와 같이 `from`에 복사할 파일들의 경로를 나열하여 `into`에 설정한 경로에 복사한다.

### - copy using filter

`Copy` task에서 include를 사용하여 복사하고자 하는 파일을 판단하여 필터링할 수 있다.

```groovy
task copyCsvFile(type: Copy){
    from '.'
    include '*.csv'
    into "$buildDir/copy"
}
```

위 `copyCsvFile`task는 현재 경로에 `csv` 확장자 파일을 복사하는 task이다. 이때 `include`에 `*.csv`를 통해서 파일명 상관없이 `csv` 파일을 모두 복사한다.

### - copying directory hierarchies

Gradle의 `Copy` task는 파일 뿐만 아니라 `directory`도 복사할 수 있다.

```groovy
task copyDirectory(type: Copy){
    from './test'
    into "$buildDir/copy/test"
}
```

from에 복사하고자 하는 폴더 경로를 설정하여 복사할 수 있다. 이때, 해당 폴더 내부의 모든 폴더와 파일을 복사한다.

단, 위 `copyDirectiory` task의 경우 `from`의 경로에 포함되는 directory는 copy에 포함하지 않는다. 만약 해당 directory를 포함하고 싶다는 다음과 같이 copy task를 작성해야한다.

```groovy
task copyDirectoryWithItself(type: Copy){
    from ('./tests') {
        include 'test1/**'
    }
    into "$buildDir/copy/test"
}
```

[Copy task 참고](https://docs.gradle.org/current/dsl/org.gradle.api.tasks.Copy.html#org.gradle.api.tasks.Copy)

### - Creating archives \(zip, tar, etc..\)

압축을 위한 task로 Gradle에서는 `Zip` 내장 task를 지원해준다.

```groovy
task packageDistribution(type: Zip) {
    archiveFileName = "my-distribution.zip"
    destinationDirectory = file("$buildDir/dist")

    from "$buildDir/toArchive"
}
```

위와 같이 `Zip` task로 압축할 수 있다.

> archiveFileName : 압축결과물 이름
>
> destinationDirectory : 결과물이 저장될 공간 \(instead of into\)
>
> from : 압축할 폴더의 위치

위는 zip 형태의 압축을 지원한다. Gradle에서는 `Zip`이외에도 tar는 `Tar`, jar는 `Jar` task로 압축을 지원해준다.

위와 같이 하나의 폴더를 압축하는 상황 말고 압축된 폴더의 서브 폴더를 copy하는 경우도 있다. 이는 압축상황에서 일반적으로 있을 수 있는 시나리오이다.

아래는 공식문서에 존재하는 예시이다.

어떤 폴더를 압축할 때 압축 파일의 docs라는 서브 폴더의 모든 pdf 파일을 `package`하길 원하는 상황이 있다. 이때 현재 docs 폴더는 `source location`에는 존재하지 않기 때문에 `archive`의 부분으로 생성해주어야한다. 이를 위해서 pdf를 package할 때 `into`를 지정해주어야한다.

```groovy
plugins {
    id 'base'
}

version = "1.0.0"

task packageDistribution(type: Zip) {
    from("$buildDir/toArchive") {
        exclude "**/*.pdf"
    }

    from("$buildDir/toArchive") {
        include "**/*.pdf"
        into "docs"
    }
}
```

위 예시로 첫번째 from으로 `$buildDir/toArchive`에 pdf 파일을 제외하고 압축한다. 그리고 pdf 파일을 두번째 from으로 압축하여 `docs` 폴더에 복사 및 압축한다.

### - Unpacking archives

Gradle은 `FileTree`를 통해서 압축파일을 풀 수 있도록 도와준다.

`FileTree`의 메서드 중 `zipTree`와 `tarTree`가 압축파일을 풀 수 있도록 도와준다.

```groovy
task unpackFiles(type: Copy) {
    from zipTree("src/resources/thirdPartyResources.zip")
    into "$buildDir/resources"
}
```

만약 zipTree의 대상이 zip 파일이라면 `Copy` task를 통해서 압축파일을 풀어준다. 이때 `Copy`의 `filter` 기능과 `rename` 기능을 적용할 수 있다.

만약 zip 파일의 일부분에 대해 압축을 제거할 때는 `eachFile()`을 사용한다.

```groovy
task unpackLibsDirectory(type: Copy) {
    from(zipTree("src/resources/thirdPartyResources.zip")) {
        include "libs/**"  // (1)
        eachFile { fcd ->
            fcd.relativePath = new RelativePath(true, fcd.relativePath.segments.drop(1))  // (2)
        }
        includeEmptyDirs = false  // (3)
    }
    into "$buildDir/resources"
}
```

> \(1\): libs 폴더 내부의 폴더 및 파일을 추출
>
> \(2\): 경로에서 libs 부분을 제거하여 추출 경로를 다시 설정
>
> \(3\): 빈 폴더의 경우는 무시

_주의!_ 단, 위 예시로는 빈폴더의 추출 경로를 바꿀 수 없다.

zip 뿐만 아니라 jar, war, ear에 대해서도 `zipTree()` 메서드를 사용한다.

### - File paths in depth

`File`에 대해 어떤 특정 logic을 수행하기 위해서는 파일이 어디있는지 파일의 위치를 알아야한다. Gradle에서는 단일 파일은 Java의 File 클래스를 사용하고 path collections를 다루는 데는 Gradle의 새로운 API를 사용한다.

단, 이때 String으로 하드코딩한 `file path`에 대해 유의해야한다.

#### On hard-coded file paths

String path는 빌드 시에는 좋지 못한 선택이다. 어떤 파일의 위치가 바뀐다면 Gradle이 해당 파일을 놓칠 수 있으며 이는 빌드 실패로 이어질 수 있기 때문이다.

따라서, 가능하면 task의 `local variable`이나 `project properties`를 사용하여 변수로 path를 지정하는 것이 바람직하다.

```groovy
ext {
    archivesDirPath = "$buildDir/archives"
}

task packageClasses(type: Zip) {
    archiveAppendix = "classes"
    destinationDirectory = file(archivesDirPath)

    from compileJava
}
```

위 예시에서는 class를 패키징하기 위해 `packageClasses` task를 정의했다. 이때 `compileJava`로 패키징하기 위한 파일의 source를 가져오고 `archivesDirPath` project property를 정의하여 사용했다.

#### Single files and directories

Gradle은 `Project.file` 메서드를 제공하여 해당 경로의 단일 파일이나 폴더의 위치를 참조할 수 있다.

```groovy
// Using a relative path
File configFile = file('src/config.xml')

// Using an absolute path
configFile = file(configFile.absolutePath)

// Using a File object with a relative path
configFile = file(new File('src/config.xml'))

// Using a java.nio.file.Path object with a relative path
configFile = file(Paths.get('src', 'config.xml'))

// Using an absolute java.nio.file.Path object
configFile = file(Paths.get(System.getProperty('user.home')).resolve('global-config.xml'))
```

위와 같이 file 메서드에 `File` class나 `Path` 인스턴스를 인자로 제공하여 File 클래스를 만들 수 있다.

단, file 메서드는 항상 상대경로를 현재 프로젝트 폴더 \(child project\)에 상대경로로 바꾼다. 만약 root project 폴더의 상대경로로 파일 경로를 사용하고 싶다면 `Project.getRootDir()`로 절대경로로 만들어 낼 수 있다.

```groovy
File configFile = file("$rootDir/shared/config.xml")
```

위와 같이 `$rootDir`을 사용하여 file의 경로를 지정하면 절대경로로 `resolve` 하여 사용할 수 있다.

### - Filtering file content \(token substitution, templating, etc.\)

> 위 내용은 앞서 include 등을 사용하여 file 이름으로 filter를 하는 것과 다름

file content filtering은 복사하는 파일의 내용을 변형하도록 허용하는 것을 의미한다.

```groovy
import org.apache.tools.ant.filters.FixCrLfFilter
import org.apache.tools.ant.filters.ReplaceTokens

task filter(type: Copy) {
    from 'src/main/webapp'
    into "$buildDir/explodedWar"
    // Substitute property tokens in files
    expand(copyright: '2009', version: '2.3.1')
    expand(project.properties)
    // Use some of the filters provided by Ant
    filter(FixCrLfFilter)
    filter(ReplaceTokens, tokens: [copyright: '2009', version: '2.3.1'])
    // Use a closure to filter each line
    filter { String line ->
        "[$line]"
    }
    // Use a closure to remove lines
    filter { String line ->
        line.startsWith('-') ? null : line
    }
    filteringCharset = 'UTF-8'
}
```

expand의 경우 해당 파일을 `groovy template`로 취급하여 tokens의 `key`에 해당하는 부분을 `value`로 바꾸는 메서드이다.

[groovy template 참고](http://docs.groovy-lang.org/latest/html/api/groovy/text/SimpleTemplateEngine.html)

### Using `CopySpac` class

copy spec은 파일 / 폴더가 어디에 복사되는지, 복사되는 동안 어떤 일이 일어나는 지 등을 정의하는 것이다. copy spec은 `1. tasks들과 독립적이라는 점`과 `2. 계층구조를 가진다는 점`이 특징이다.

```groovy
CopySpec webAssetsSpec = copySpec {
    from 'src/main/webapp'
    include '**/*.html', '**/*.png', '**/*.jpg'
    rename '(.+)-staging(.+)', '$1$2'
}

task copyAssets (type: Copy) {
    into "$buildDir/inPlaceApp"
    with webAssetsSpec
}

task distApp(type: Zip) {
    archiveFileName = 'my-app-dist.zip'
    destinationDirectory = file("$buildDir/dists")

    from appClasses
    with webAssetsSpec
}
```

위 예시외 같이 다양한 task들에서 공유하여 사용할 수 있다. task에서 `copySpec`을 적용할 때는 `with` 메서드를 이용한다.

[공식문서 : working with files](https://docs.gradle.org/current/userguide/working_with_files.html)


# 2019.04.21 Sunday

## 1. Gradle

### - 파일참조

Gradle에서는 파일을 참조하기 위해서 `File` 객체를 사용한다.

`file()` 메서드는 상대경로나 File 객체를 인수로 사용한다.

> 예시
>
> File refFile = file('src/main/java/Main.java')
>
> File refFile = file(new File('src/Main.java'))

또한, URL, URI 객체를 참조하여 file 객체를 참조할 수도 있다.

파일 객체를 검증하는데는 `PathValidation` 객체를 사용한다.

- `PathValidation.DIRECTORY`는 디렉터리 경로가 올바른지 검증하는데 쓰인다. (디렉터리의 유무)
- `PathValidation.FILE`은 파일 경로가 올바른지 검증한다. (파일의 유무)
- `PathValidation.EXISTS`는 파일 또는 디렉터리의 존제 여부를 확인한다.
- `PathValidation.NONE`은 파일 검증을 하지않는다는 의미이다.

> 예시
>
> File checkFile = file('/index.html', PathValidation.FILE)
>
> File checkDirectory = file('src', PathValidation.DIRECTORY)

### - 다중 파일 참조

여러 파일을 참조하여 다룰때는 `FileCollection` 객체를 사용한다. 단일 파일은 `file()` 메서드를 사용한 것과 달리 `files()`를 사용하여 `FileCollection` 객체를 만들 수 있다.

```groovy
FileCollection fileCollection = files('index.txt', 'home.txt')

println 'file: ' + fileCollection[0].path
println 'file: ' + fileCollection[1].path

fileCollection.each {
    println 'file: ' + it.path
}
```

`FileCollection` 객체는 배열과 같이 index로 접근 가능하다. 또한, groovy에서 제공하는 each 메서드로 loop 가능하다.

`files()` 메서드에서도 `file()`과 마찬가지로 File 객체, URI, URL, 상대경로 등으로 참조할 파일을 지정할 수 있다.

또한, list형이나 배열을 `files()`를 사용하여 `FileCollection` 객체를 생성할 수 있다.

> ※ 참고 : FileCollection 객체는 List, Set, File[]로 형변환 할 수 있다. as 연산자를 사용하여 형변환
>
> List fileList = fileCollection as List
>
> Set fileSet = fileCollection as Set
>
> File[] fileArray = fileCollection as File[]

여러 파일을 `FileCollection`뿐 아니라 `FileTree`를 사용하여 여러 파일을 참조할 수 있다.

```groovy
FileTree fileTreeInclude = fileTree('src'){
    include '**/*.java'
} // java 확장자 파일을 포함하여 FileTree 객체 생성

FileTree fileTreeExclude = fileTree('src'){
    exclude '**/Test????.java'
} // java 확장자 파일 중 Test로 시작하는 8글자 파일을 제외하고 FileTree 객체 생성 
```

위 방식은 `Map` 자료구조를 사용하여 똑같이 구현할 수 있다.

```groovy
FileTree fileTreeMap = fileTree(dir: 'src', include: '**/*.java', exclude: '**/Test????.java')
```

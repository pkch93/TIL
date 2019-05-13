# 2019.04.24 Wendesday

## 1. Gradle

> + task 관련 모르는 개념 익히기
>
> + 의존관계 익히기

### Task

- Passing arguments to a task constructor

task에 대해 정해진 `mutable properties`를 설정하는 것 이외에도 arguments를 전달할 수 있다.

이를 위해서 관련된 생성자`constructor`에 `@javax.inject.Inject` 어노테이션을 붙여 받을 인자를 설정한다.

```groovy
class CustomTask extends DefaultTask {
    final String message
    final int number

    @Inject
    CustomTask(String message, int number) {
        this.message = message
        this.number = number
    }
}
```
위와 같이 설정하면 task 생성시 `message`와 `number`에 해당하는 인자를 전달할 수 있다.

```groovy
task mytask(type: CustomArgTask, constructorArgs: ['this is custom task', 1]){
    doLast {
        println message
        println number
    }
} // 1

tasks.create('mytask', CustomArgTask, 'this is custom task', 1) // 2
```
1, 2번 둘다 동일한 `mytask`를 생성한다. 

> tasks를 통해 현재 빌드 스크립트의 TaskContainer에 접근 가능
>
> 공식문서에서는 TaskContainer를 사용하여 constructor arg를 전달하는 것을 추천

이때, 생성자 인자는 반드시 `non-null`이어야만 한다. 때문에 null을 전달하게 되면 Gradle에서는 `NullPointerException`을 일으킨다.

- replacing tasks

기존의 task를 대체하고 싶을 수 있다. 이 경우 `overwrite`를 true로 주면 같은 이름의 task를 재정의할 수 있다.

```groovy
task copy(type: Copy)

task copy(overwrite: true) {
    doLast {
        println('I am the new one.')
    }
}
```
위와 같이 같은 이름의 task를 정의할 때 overwrite를 true로 하지 않으면 `exception`이 발생한다. 이미 있는 task이기 때문이다.

- skipping task

모든 task에는 `enabled`라는 flag를 가진다. 이 값은 기본적으로 `true`값을 가진다. 만약 `enabled`를 false로 설정하면 task action에서 실행되는 것을 막을 수 있다. `disabled task`는 `SKIPPED`로 라벨링된다.

```groovy
task disableMe {
    doLast {
        println 'This should not be printed if the task is disabled.'
    }
}
disableMe.enabled = false
```

- task timeouts

`enabled`와 마찬가지로 모든 task에는 `timeout`이라는 property를 가진다. 이는 설정한 시간이 지나면 강제로 task를 종료시키는 방법이다. 만약 `--continue`가 옵션으로 붙어있다면 이후에 실행 스케줄에 있는 task를 계속 실행한다.

### Up-to-date checks (Incremental Build)

gradle과 같은 빌드 툴에서 가장 중요한 부분 중 하나는 이미 진행한 일의 중복을 피하는 것이다. ex) `compilation`

- Task inputs and outputs

대부분의 경우 task는 `input`을 가지고 `output`을 만들어낸다.

이때 `input`의 중요한 특징은 하나 또는 여러 `output`에 영향을 미친다는 것이다. source file (`.java`)로부터 java bytecode 파일(`.class`)을 만들어낸다.

compile시 memory 최대 사용량을 `memoryMaximumSize` property로 결정할 수 있다. 단, 이 memory 최대 사용량은 bytecode를 생성하는데는 영향을 미치지 않는다.
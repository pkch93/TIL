# 2018 12.20 Thursday - Spring ResponseEntity, pandas groupby

## 학습내용

1. Spring Restful API 만들기 학습
2. ResponseEntity와 URI

```java
@PostMapping
    public ResponseEntity createEvents(@RequestBody Event event){
        Event newEvent = this.eventRepository.save(event);
        URI uri = linkTo(EventController.class).slash("{id}").toUri();
        return ResponseEntity.created(uri).body(event);
    }
```

위 createEvents 핸들러에서 **ResponseEntity**를 사용하여 응답을 하고 있다. 여기서 ResponseEntity는 **HttpEntity**를 상속받은 클래스이다.

HttpEntity는 통신 메시지 관련 header와 body의 값을 하나의 객체로 저장하는 객체이다.

반면, Request 부분인 경우는 RequestEntity가 Response 부분인 경우에는 ResponseEntity가 담당한다. ResponseEntity는 **Http의 상태 코드를 함께 전송해주기 위해 사용**한다.

[참고](https://okky.kr/article/311196)

**URI**는 linkTo 메서드로 생성했다. linkTo는 Spring HATEOAS에서 지원해주는 메서드이며 URI를 생성하는데 도와주는 역할을 한다. ResponseEntity의 created는 URI를 필요로 하는데 위 HATEOAS의 URI를 사용하여 ResponseEntity를 만들어낼 수 있다.

> ※ Repository를 Mock 객체로 사용시 유의사항!
>
> Mock 객체로 받은 Repository는 쿼리시 \(save, update 등\) return이 null로 나타나게 된다.
>
> 따라서 해당 쿼리 메서드를 사용하기 위해 Mockito의 when, then을 이용하여 해당 메서드가 호출되면 일어나야 하는 일에 대해 명시해주어야한다.

```java
Mockito.when(eventRepository.save(event)).thenReturn(event);
```

* 입력값 제한하기

자동 설정되는 Id, 계산되어야하는 값 등은 입력으로 설정되면 안된다.

이를 처리하기 위해 Dto 객체를 이용할 수 있다.

```java
@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter @EqualsAndHashCode(of = "id")
@Entity
public class Event {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location;
    private int basePrice;
    private int maxPrice;
    private int limitOfEnrollment;
    private boolean offline;
    private boolean free;
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus;
}
```

위의 필드를 입력으로 받으면 안되는 id, offline, free, eventStatus를 제거한 EventDto를 생성한다.

```java
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EventDto {
    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location;
    private int basePrice;
    private int maxPrice;
    private int limitOfEnrollment;
}
```

위와 같이 Dto를 사용하면 입력으로 id, offline 등이 들어오더라도 Dto에는 존재하지 않는 필드이므로 무시된다. 이렇게 입력값 제한이 가능하다.

※ Modelmapper

Object를 mapping하여 다른 Class의 객체로 만들어주는 모듈

```text
<dependency>
    <groupId>org.modelmapper</groupId>
    <artifactId>modelmapper</artifactId>
    <version>2.3.2</version>
</dependency>
```

```text
implementation('org.modelmapper:modelmapper:2.3.2')
```

```java
Event event = modelMapper.map(eventDto, Event.class);
```

위의 modelMapper의 map함수로 첫번째 인자로 들어온 eventDto 객체를 Event 클래스의 객체로 바꿔줄 수 있다.

[공식문서참고](http://modelmapper.org/)

* 입력값 이외의 에러

입력값 이외의 값이 들어왔을 때 에러를 발생시킬 수 있다.

spring boot가 제공하는 프로퍼티 확장 기능에서 spring.jackson.deserializaion.fail-on-unknown-properties 프로퍼티를 true로 설정하면 된다.

이 때, 받을 수 없는 properties, 즉 Dto에 정의되지않은 필드가 있다면 badrequest에러\(400\)를 내뿜는다.

```text
spring.jackson.deserialization.fail-on-unknown-properties=true
```

위 fail-on-unknown-properties 이외에도 다양한 serialization과 deserialization 프로퍼티들이 있다.

1. Pandas Groupby & Casestudy
   * Groupby

     SQL groupby 쿼리와 같다. **split -&gt; apply -&gt; combine**의 과정으로 연산한다.

     ```python
     df.groupby("TEAM")["Points"].sum()
     ```

     위 파라미터로 들어가는 값은 묶음의 기준, 대괄호 안에 들어가는 값은 적용받는 컬럼, 뒤 sum은 연산을 나타낸다!

     한 개뿐만 아니라 그 이상의 column도 묶을 수 있다.

     ```python
     df.groupby[("TEAM", "Year"])["Points"].sum()
     ```

     ※ Groupby - grouped Groupby에 의해 Split된 상태를 추출 가능하다. grouped는 보통 groupby에 의해 split된 상태를 말한다.

     grouped 상태는 Tuple 형태로 group의 key:value 값이 추출된다.

     또한 grouped 상태에서는 특정 key값을 가진 그룹의 정보만 추출 가능하다.

     ```python
     grouped.get_group("Devils")
     # Devils 팀의 정보만 추출
     ```

     이렇게 추출된 group 정보에는 세 가지 유형의 apply가 가능하다.

   * Aggregation : 요약된 통계정보 추출

     ```python
     grouped.agg(sum/np.mean 등의 연산함수)
     grouped["Points"].agg([sum, max, min])
     # 특정 컬럼에 여러개의 연산도 가능하다!
     ```

   * Transformation : 해당 정보를 변환 \(개별 데이터의 변환 지원!\)

     ```python
     grouped.transform(score) # 개별 데이터에 그룹별로 적용된다. 중요한 것은 그룹별로 각각의 데이터에 적용한다.
     ```

   * Filtration : 특정 정보를 제거하여 보여주는 필터링

     ```python
     df.groupby("Team").filter(lambda x : len(x) >= 3)
     # len(x)는 grouped된 dataframe 개수!
     ```

* Hierarchical index

  Groupby 명령의 결과물도 결국은 DataFrame이다. 만약, 두 개의 column으로 groupby를 한다면 index가 두개 생성된다.

  ※ unstack\(\) Groub으로 묶여진 데이터를 matrix 형태로 변환할 수 있다.

  ※ swaplevel\(\) index의 level을 변경하는 메서드 이떄 sortlevel\(\)을 사용할 수 있는데 이 경우 index를 sort해서 변경해준다.

  ※ operations 기본적으로 index level을 기준으로 기본 연산을 해준다.

  ```python
  h_index.sum(level=0) # Team을 기준
  h_index.sum(level=1) # Year을 기준
  ```


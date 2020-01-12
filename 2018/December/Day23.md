# 2018 12.23 Sunday - Spring JUnitParams, HATEOAS, Rest Docs, pandas

## - Spring Restful API

1. 매개변수를 이용한 테스트

**JUnitParams**로 매개변수 테스트 하기

[참고 / JUnitParams github](https://github.com/Pragmatists/JUnitParams)

```groovy
testImplementation('pl.pragmatists:JUnitParams:1.1.1')
```

파라미터를 사용한 테스트 코드를 쉽게 만들어주는 프로젝트
JUnit은 기본적으로 Method Parameter를 가질 수 없는데 Method Parameter를 넣어 파라미터 값이 변할 때 테스트 코드를 작성하기 용이하도록 도와준다.

```java
@RunWith(JUnitParamsRunner.class)
public class EventTest {
// ...
@Test
@Parameters({
    "0, 0, true",
    "100, 0, false",
    "0, 100, false"
})
public void testFree(int basePrice, int maxPrice, boolean isFree){
    // given
    Event event = Event.builder()
            .basePrice(basePrice)
            .maxPrice(maxPrice)
            .build();
    // when
    event.update();
    // then
    assertThat(event.isFree()).isEqualTo(isFree);
}
// ...
}
```

JUnitParams를 사용하기 위해서는 **@Runwith(JUnitParamsRunner.class)**를 명시하여 Runner를 설정해야한다.

이렇게 설정하면 같은 테스트 코드에 파라미터만 다른 경우를 중복을 제거하여 손쉽게 테스트할 수 있다.

위 예제처럼 JUnitParams가 제공하는 **@Parameter** 어노테이션을 붙여주고 method parameter에 들어가야하는 값들을 문자열 형태로 제공할 수 있다.

![testFree test 결과](img/junitparams1.PNG)

이렇게 @Parameters에 넣은 3개에 문자열에 대해 각각 순서대로 메서드 파라미터에 매핑되고 테스트 결과를 각각의 파라미터에 대하여 보여준다.

하지만 위 @Parameters에 들어간 문자열은 type safe 하지 못하다. 이를 해결하기 위해서 method 형태로 parameter를 제공해 줄 수 있다.

```java
private Object[][] parametersForTestFree(){
    new Object[][]{
        new Object[]{0, 0, true},
        new Object[]{100, 0, false},
        new Object[]{0, 100, false}
    }
}
// ...
@Parameters(method = "parametersForTestFree")
```

위와 같이 parameter를 정의한 Object 배열로 제공가능하다.

이 때, 위 예제는 @Parameters에 method 값 없이 그냥 @Parameters를 붙인 것과 같은 결과가 나온다. 이는 JUnitParams의 convention이 적용되기 때문이다.

> method 이름이 `parametersFor{test method 이름}`이라면 @Parameters가 자동으로 해당 메서드를 찾아 파라미터로 등록한다. 

2. Spring HATEOAS

[공식문서](https://docs.spring.io/spring-hateoas/docs/0.25.0.RELEASE/reference/html/)

Spring HATEOAS는 Restful API를 만들 때 보다 쉽게 제공하기 위한 일종의 tool을 제공해주는 일종의 라이브러리

> HATEOAS?
>
> REST application architecture 중 하나의 구성요소로 hypermedia를 이용하여 server의 정보를 client가 동적으로 정보를 주고받을 수 있는 방법

```
<!-- 잔액이 있는 경우 -->
HTTP/1.1 200 OK
Content-Type: application/xml
Content-Length: ...

<?xml version="1.0"?>
<account>
    <account_number>12345</account_number>
    <balance currency="usd">100.00</balance>
    <link rel="deposit" href="https://bank.example.com/accounts/12345/deposit" />
    <link rel="withdraw" href="https://bank.example.com/accounts/12345/withdraw" /> 
    <link rel="transfer" href="https://bank.example.com/accounts/12345/transfer" />
    <link rel="close" href="https://bank.example.com/accounts/12345/status" />
</account>

<!-- 잔액이 마이너스인 경우 -->
HTTP/1.1 200 OK
Content-Type: application/xml
Content-Length: ...

<?xml version="1.0"?>
<account>
    <account_number>12345</account_number>
    <balance currency="usd">-25.00</balance>
    <link rel="deposit" href="https://bank.example.com/accounts/12345/deposit" />
</account>
```

***이렇게 Restful API는 상태에 따라 전이할 수 있는 동적 링크를 다르게 제공해주어야한다!***

[참고](https://en.wikipedia.org/wiki/HATEOAS)

이렇게 **Spring HATEOAS**는 리소스를 만들어 주는 기능 / 링크를 만들어 주는 기능 / 링크를 찾아주는 기능을 제공해준다.

링크를 만드는 방법은 직접 Spring HATEOAS의 Link 객체를 추가하는 방법과 LinkBuilder를 이용하는 방법이 있다.

```java
linkTo("http://localhost:8089/event/1")
linkTo(Controller.class).slash(event.getId()).withSelfRel();
```
위 예시처럼 ControllerBuilder에 linkTo 메서드로 링크 정보를 Rest API에 담아 줄 수 있다.

> 그외 methodOn() 방법과 EntityLinks 활용 방법은 추후...

리소스를 만드는 방법은 ResourceSupport 상속과 Resource<T>를 상속하는 방법 2가지가 있다.

```java
public class EventResource extends ResourceSupport{
    @JsonUnwrapped
    private Event event;

    public EventResource(Event event){
        this.event = event;
    }

    public Event getEvent(){
        return this.event;
    }
}
```
위 방식으로 EventResource를 정의하여 Event 정보에 Link 정보를 담은 리소스를 생성할 수 있다. 다만, 문제점은 객체를 json으로 Serialize 해주는 ObjectMapper의 BeanSerializer가 Event객체의 event 이름을 그대로 사용한다. 따라서 event 정보가 "event"에 한번 감싸져서 restful api로 나타난다. 이를 해결하기 위해서 **@JsonUnwrapped**를 Event 위에 붙여주면 해결되긴 한다. 이 외 다른 방법으로는 ResourceSupport를 상속받은 Resource<T>를 상속해줄 수도 있다.

```java
public class EventResource extends Resource<Event>{
    public EventResource(Event event, Link... links){
        super(event, links);
    }
}
```
위와 같이 정의하면 ResourceSupport를 상속받아 Resource를 만든 코드와 똑같이 적용된다. 이때, Resource<T>의 getContent() 메서드에 @JsonUnwrapped가 붙어있기 때문에 @JsonUnwrapped 어노테이션 없이도 똑같이 Resource가 나오는 것이다.

이렇게하면 Restful API 요건에서 HATEOAS를 충족할 수 있다.

Restful API에서 self-descriptive message를 충족하는 방법은 **Spring REST Docs** 라이브러리를 이용하여 해결할 수 있다.

[Spring REST Docs 공식문서](https://docs.spring.io/spring-restdocs/docs/2.0.3.RELEASE/reference/html5/)

Spring REST Docs는 **Spring MVC Test**를 이용해서 REST API 문서의 조각(Snippet)을 생성해내는 라이브러리

```java
private MockMvc mockMvc;

@Autowired
private WebApplicationContext context;

@Before
public void setUp() {
	this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
			.apply(documentationConfiguration(this.restDocumentation)) 
			.build();
}
```

연동하는 방법은 mockMvc를 만들때 documentationConfiguration을 설정하면 된다.

Spring Boot를 이용하면 **@AutoConfigureRestDocs**를 붙여 손쉽게 사용할 수 있다.

이렇게 설정을 등록하면 Test에서 **andDo** 메서드의 인자로 **document()**를 통해 REST Docs를 생성할 수 있다.

단, 이렇게 만들어 진 문서는 일렬로 보여주기 때문에 가독성이 좋지 못하다. 이를 해결하기 위해 **RestDocsMockMvcConfigurationCustomizer**를 빈으로 등록하여 설정해주면 된다.

```java
@TestConfiguration
public class RestDocsConfiguration{
    
    @Bean
    public RestDocsMockMvcConfigurationCustomizer restDocsMockMvcConfigurationCustomizer(){
        return configurer => 
                configurer.operationPreprocessor().withRequestDefaults(prettyPrint())
                .withResposeDefaults(prettyPrint()); 
    }
}
```

위와 같이 설정하면 Request와 Response에 대해 Json을 일렬이 아닌 사람이 보기 좋게 보여줄 수 있다.

위는 응답과 요청에 대한 문서화만 진행했다. 아래 예시는 Link, Request & Response 필드, Header를 문서화하는 방법이다.

```java
...
.andDo(document("create-event",
    links(
        linkWithRel("self").description("link to self"),
        linkWithRel("query-events").description("link to query events"),
        linkWithRel("update-event").description("link to update event")
    ),
    requestHeaders(
        headerWithName(HttpHeader.ACCEPT).description("aceept header"),
        headerWithName(HttpHeader.CONTENT_TYPE).description("content type header")
    ),
    requestFields(
        fieldwithPath("name").description("Name of new event"),
        fieldwithPath("description").description("description of new event"),
        ...
        , fieldwithPath("maxPrice").description("maxPrice of new event")
    ),
    responseHeaders(
        ...
    ),
    responseFields(
        ...
    )
))
```
위 방법으로 REST Docs를 만들어 낼 수 있다.

만약에 응답 / 요청 필드의 **일부분만 문서화**하고 싶다면 relaxed 메서드를 사용할 수 있다.

다만, 정확한 문서를 생성하지 못하는 문제점이 생긴다.

> 요청 문서 문서화
>
> requestFields() + fieldWithPath()
>
> responseFields() + fieldWithPath()
>
> requestHeaders() + headerWithName()
>
> responseHeaders() + headerWithName()
>
> links() + linkWithRel()

이렇게 만들어진 문서 조각을 사용하여 하나의 완성된 문서를 만들 수 있다. (plugin 추가 필요, ascii-doctor & maven-resources)

**src/main/asciidoc** 이라는 directory를 만든 후 완성된 문서의 이름을 자유롭게 만든다.(확장자는 adoc)

## - 데이터 사이언스 by python

- pandas

    - Selection & Drop
    - lambda & map & apply
    - replace
    - pandas 내장함수 (describe, unique, isnull, sort_value)
    - groupby
    - pivot_table & crosstab
    - merge & concat
    - DB connection & persistence

- matplotlib python의 대표적인 시각화 라이브러리

[공식 문서](https://matplotlib.org/)

그 외 seaborn, plot.ly 등이 있다.

다양한 graph를 지원하고 pandas와 연동이 되기 때문에 pandas에서 다루는 데이터를 쉽게 시각화 할 수 있다.

기본적으로 matplotlib는 **pyplot** 객체를 이용하여 데이터를 표시해준다.
pyplot 객체에 그래프를 쌓은 다음 **show로 flush**한다.

만약 show를 통해 그래프를 이미 그렸다면 show를 하더라도 이미 pyplot에 있는 데이터를 flush 했기 때문에 새로운 그래프가 나타나지 않는다.

```python
import matplotlib.pyplot as plt

X = range(100)
Y = [value ** 2 for value in X]
plt.plot(X, Y)
plt.show()
```
위는 matplotlib의 기본사용 예제이다.

Graph는 원래 figure 객체에 생성된다. pyplot 객체를 사용할 때는 기본 figure에 그래프가 그려진다.

```python
X_1 = range(100)
Y_1 = [np.cos(value) for value in X_1]

X_2 = range(100)
Y_2 = [np.sin(value) for value in X_2]

plt.plot(X_1, Y_1)
plt.plot(X_2, Y_2)
plt.show()
```

또한 그래프를 그리는 figure 객체를 pyplot에서 받아올 수 있다. **figure()**을 통해 figure 객체를 가져올 수 있다.

```python
fig = plt.figure()
fig.set_size_inches(10, 5) # figure의 크기 지정

ax_1 = fig.add_subplot(1,2,1) # figure에 첫 번째 plot 추가
ax_2 = fig.add_subplot(1,2,2) # figure에 두 번째 plot 추가

ax_1.plot(X_1, Y_1, c="b")
ax_2.plot(X_2, Y_2, c="g")
plt.show()
```

위와 같이 pyplot에 graph를 그리는 figure 객체를 가져와서 크기 조정(set_size_inches)과 여러 개의 subplot 그리기 (add_subplot)가 가능하다.

또한 위와 같이 **c(color)** 속성을 주어 plot에 색깔을 줄 수 있다. 값은 rgb, 흑백, predefined color를 사용할 수 있다.

color 이외에도 ls(linestyle)을 주어 선의 속성을 바꿀 수 있다.

```python
plt.plot(X_1, Y_1, c="b", ls="dashed")
plt.plot(X_2, Y_2, c="r", ls="dotted")

plt.title("two lines")
plt.show()
```

위와 같이 ls나 linestyle 속성으로 `dashed(굵은 점선)`, `dotted(빽빽한 점선)` 등의 선 스타일을 구성할 수 있다.

마찬가지로 해당 plot의 제목은 `title()`을 통해 지정할 수 있다.
특이한 점은 figure의 subplot마다 title을 지정할 수도 있다.
title은 **Latex 타입의 표현**도 가능하며 이는 수식의 표현이 가능하다는 것을 의미한다.

```python
plt.title('$y = \\frac{ax + b}{test}$')
```

또한 **legend** 함수로 범례를 표시할 수 있으며 plot마다 label을 지정하여 범례에 들어갈 이름을 정할 수 있다.

```python
plt.plot(X_1, Y_1, c="b", ls="dashed", label="line 1")
plt.plot(X_2, Y_2, c="r", ls="dotted", label="line 2")
plt.xlabel('$x_line$')
plt.ylabel('y_line')
plt.legend(shadow=True, fancybox=True, loc="lower right")
```

이때 shadow 속성은 범례 박스의 그림자 여부를 fancybox는 모서리를 둥글게 만들지 여부를 지정하는 속성이다. loc은 범례 상자의 위치를 지정하는 방법으로 문자열로 best, upper / lower, left / right, center 등으로 지정할 수 있다.

이 외에도 x축과 y축에 각각 label을 달 수 있는데 이는 **xlabel**과 **ylabel** 함수로 적용할 수 있다.

plot 내부에 text를 집어 넣거나 annotate를 이용하여 화살표 및 텍스트를 지정할 수도 있다.

```python
plt.text(50, 70, "Line_1") # x: 50, y: 70 위치에 Line_1 문자열 넣기
plt.annotate("line_2", xy=(50, 150), xytext=(20, 175),
            arrowprops=dict(facecolor="black", shrink=0.05))
```

plot에는 Graph 보조선을 긋는 `grid`와 xy축 범위 한계를 지정하는 `xlim()`과 `ylim()`이 있다.

```python
plt.grid(True, lw=0.4, ls="--", c=".90")
plt.xlim(-100, 200)
plt.ylim(-200, 200)
```

> lw는 line weight(굵기)

참고로 x나 y값을 늘리거나 줄이더라도 figure 객체의 set_size_inches를 통해 크기를 바꾸지 않는 이상은 해당 크기에서 범위를 변화한다.

마지막으로 savefig를 이용하면 해당 graph를 png 파일로 저장할 수 있다.

```python
plt.savefig("test.png", c="a")
```

- matplotlib Graph

    1. Scatter (산포도)

    scatter 함수를 이용하여 Scatter 그래프를 보여줄 수 있다.

    ```python
    data_1 = np.random.rand(512, 2)
    data_2 = np.random.rand(512, 2)

    plt.scatter(data_1[:, 0], data_1[:, 1], color="b" ,marker="x")
    plt.scatter(data_2[:, 0], data_2[:, 1], c="r" ,marker="^")

    plt.show()
    ```

    이 때 marker를 이용하여 scatter **graph에 나타나는 모양을 지정**할 수 있다.

    ```python
    N = 50
    x = np.random.rand(N)
    y = np.random.rand(N)
    colors = np.random.rand(N)
    area = np.pi * (15 * np.random.rand(N)) ** 2
    plt.scatter(x, y, s=area, c=colors, alpha=0.5)
    plt.show()
    ```

    > alpha는 투명도를 뜻하는 속성, s는 size

    2. Bar chart (막대 그래프)

    ```python
    X = np.arange(4)
    plt.bar(X + 0.00, data[0], c="h", width=0.25)
    plt.bar(X + 0.25, data[1], c="g", width=0.25)
    plt.bar(X + 0.50, data[2], c="r", width=0.25)
    plt.xticks(X + 0.25, ("A", "B", "C", "D"))
    plt.show()
    ```

    위는 막대 그래프를 그리는 예제이며 bar에 bottom 속성을 통해 누적 bar chart (아래에서 위로 쌓는 bar chart)를 그릴 수 있다.

    ```python
    women_pop = np.array([5, 30, 45, 22])
    men_pop = np.array([5, 25, 50, 20])
    X = np.arange(4)

    plt.barh(X, women_pop, c="r")
    plt.barh(X, -men_pop, c="b")
    plt.show()
    ```

    위와 같이 **barh**(h는 horizontal이라는 뜻) 함수로 수직으로 비교하는 bar chart를 그릴 수 있다. 이 때, 2번째 인자에 +값이 들어가면 오른쪽으로, -값이 들어가면 왼쪽으로 그래프가 진행하게 된다. 

    3. histogram

    ```python
    X = np.random.randn(1000)
    plt.hist(X, bins=10)
    plt.show()
    ```

    위의 bins 속성으로 histogram의 구간을 나눌 수 있다.

    4. boxplot

    ```python
    data = np.random.randn(100, 5)
    plot.boxplot(data)
    plt.show()
    ```

- matplotlib pandas

pandas 0.7 버전 이상부터 matplotlib를 이용한 그래프를 그릴 수 있도록 지원해주고 있다. DataFrame / Series 별로 그래프 작성이 가능하다.

> ※ Boston House Price Dataset
>
> 머신러닝 등 데이터 분석을 처음 배울 때, 가장 대표적으로 사용하는 Example dataset
>
> [데이터](http://lib.stat.cmu.edu/datasets/boston)

데이터의 **상관관계**를 볼 때 scatter graph를 사용가능하다.**

```python
fig = plt.figure()
ax = []
for i in range(1,5):
    ax.append(fig.add_subplot(2,2,i))
ax[0].scatter(df_data["CRIM"], df_data["MEDV"])
ax[1].scatter(df_data["PTRATIO"], df_data["MEDV"])
ax[2].scatter(df_data["AGE"], df_data["MEDV"])
ax[3].scatter(df_data["NOX"], df_data["MEDV"])

plt.show()
```

이 떄, 만약에 조금씩 떨어져 있는 subplot들을 붙여서 보기 위해서는 subplots_adjust를 통해 붙여서 볼 수 있다.

```python
plt.subplots_adjust(wspace=0, hspace=0)
```

위는 matplotlib를 이용하여 pandas 데이터를 그린 예시이다.

pandas에서도 자체 plot() 함수를 통해 전체 데이터이 graph를 그릴 수 있다.

```python
df.plot()
```

또한 전체 데이터에 대해 상관관계를 알아보는 방법으로 scatter_matrix를 통해 graph를 그릴 수 있다.

```python
pd.scatter_matrix(df, diagonal="kde", alpha=1, figsize=(20, 20))
plt.show()
```

전체 데이터 간의 상관관계를 보여주는 방법이다.

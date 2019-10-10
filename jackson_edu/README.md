# Jackson Library Study

참고: [https://www.baeldung.com/jackson-object-mapper-tutorial](https://www.baeldung.com/jackson-object-mapper-tutorial)
공식 사이트: [https://github.com/FasterXML/jackson-databind/wiki](https://github.com/FasterXML/jackson-databind/wiki)

## ObjectMapper

Jackson 라이브러리에서 제공하는 ObjectMapper 클래스는 JSON String을 Java Object로 바꾸거나 그 반대의 경우에 사용할 때 핵심적인 요소이다.

JSON String을 읽을 때는 ObjectMapper의 readValue 메서드로 간단히 읽을 수 있다.

### ReadValue API

다음 Person 클래스가 있다고 가정한다.

```java
public class Person {

    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // getter, setter, equals and hashcodes...
}
```  

위 Person 클래스를 기반으로 다음 `person.json`을 파싱하고자 한다.

```json
{
  "name": "pkch",
  "age": 27
}
```

위 Person 클래스를 바탕으로 `person.json`을 파싱하기 위해서는 어떻게 해야할까?
다음과 같이 `objectMapper.readValue()` 메서드를 사용한다.

```java
objectMapper.readValue(new File("src/test/resources/person.json"), Person.class);
```

단, 현재의 Person 클래스로는 다음과 같은 에러가 발생한다.

```
com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Cannot construct instance of `sample.Person` (no Creators, like default construct, exist): cannot deserialize from Object value (no delegate- or property-based Creator)
 at [Source: (File); line: 2, column: 3]
``` 

즉, Person 클래스에 default constructor가 없어서 생기는 문제이다. Person에 기본 생성자를 정의해주면 자연스럽게 파싱된다.

### 그렇다면 getter, setter도 있어야하나?

실험결과 변수가 public 등으로 열려있거나 변수에 대해 getter 또는 setter 한가지라도 있다면 파싱이 적용되는 것으로 보인다.
추측컨데 먼저 변수가 접근 가능한지를 파악해 접근이 가능하다면 바로 그 변수에 json 값을 할당하고 private, default 접근 제어자 등으로 접근이 불가능하다면
해당 변수의 getter나 setter를 사용하여 값을 할당해 주는 것으로 보인다.

// TODO: objectMapper 값 할당 부분 코드 살펴보기

### write API

json 파일을 자바 Object로 만들 수 있다면 반대로 자바 Object를 json으로 만들 수 있어야한다.

ObjectMapper는 `writeValue*` 메서드로 File, String, bytes 등으로 변환을 지원하고있다.

```java
objectMapper.writeValue(new File(TEST_RESOURCE + "/write.json"), person);
String personJsonString = objectMapper.writeValueAsString(person);
byte[] personJsonBytes = objectMapper.writeValueAsBytes(person);
``` 

위와 같이 writeValue 메서드에 첫번째 인자로 File 객체와 변환하고자 하는 인스턴스를 넣으면 해당 경로의 파일로 변환 결과를 생성한다.
writeValueAsString은 인자로 들어오는 변수를 `String`으로, writeValueAsBytes는 `byte[]`로 파싱해준다.

### JsonNode API

그외에 Jackson 라이브러리에서는 Json 파일을 특정 Node 형태로 바꿔주는 JsonNode API를 제공해준다.
변환할 마땅한 클래스가 없을 경우 JsonNode를 사용할 것으로 추측한다.

```java
Person person = new Person("pkch", 27);

JsonNode jsonNode = objectMapper.readTree(new File(TEST_RESOURCE + "/person.json"));
assertThat(jsonNode.get("name").asText()).isEqualTo("pkch");
assertThat(jsonNode.get("age").asInt()).isEqualTo(27);
```
위 예시와 같이 `readTree`메서드를 사용하여 json 파일을 JsonNode로 변환할 수 있다.

솔직히 ObjectMapper를 주로 raw한 타입의 데이터를 특수한 목적의 객체로 사용하기 때문에 JsonNode는 그리 많이 사용할 것 같지는 않다.

### Json Array to Java List

Json Array를 List로 바꿀때도 readValue 메서드를 사용한다. 다만, 두번째 인자로 들어가는 ValueType에 TypeReference로 List를 반환하도록 해야한다.

```java
List<Person> persons = objectMapper.readValue(new File(TEST_RESOURCE + "/list.json"), new TypeReference<List<Person>>(){});
```

### Json String to Java Map

Json String을 객체가 아닌 Map으로 반환하는 것 역시 readValue 메서드를 사용한다. 여기서도 두번째 인자 ValueType에는 TypeReference를 준다. 

```java
Map<String, String> persons = objectMapper.readValue(new File(TEST_RESOURCE + "/list.json"), new TypeReference<Map<String, String>>(){});
```
이렇게 봤을 때 Java Collection API를 반환하도록 만들때는 TypeReference를 ValueType으로 사용해야하는 것 같다.

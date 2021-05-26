# 2018 12.22 Saturday - Spring \(Bed Request 처리\), pandas \(pivot table, merge, concat\)

* Spring Restful API
  1. Bad Request 처리

     cilent로부터 받은 data 중 이름이 잘못됐거나 필수 값이 없는 경우 status code 400 처리를 해주는 방법

     Spring MVC에서 제공해주는 **@Vaild**와 **BindingResult**를 이용하여 Bad Request 처리를 할 수 있다.

     ```java
     public ResponseEntity createEvents(@RequestBody @Valid EventDto eventDto, Error errors){
       // body....
     }
     ```

     위와 같이 Request에서 받는 값을 통해 EventDto에 binding할 때 **@Valid** 어노테이션이 있다면 EventDto에서 설정한 BindingResult 정보를 가지고 입력값 검증을 할 수 있다. 만약 error가 있다면 @Valid 바로 뒤에 존재하는 Error 객체에 Error 정보가 담긴다.

     > Binding Result 정보로는 @NotEmpty, @NotNull, @Max, @Min 등이 있다.

     다만, Binding Result 정보만으로는 검증 방식이 제한적일 수 있다. 이 경우 Validator 객체를 정의하여 사용할 수 있다.

     ```java
     @Component
     public class EventValidator {

      public void validate(EventDto eventDto, Errors errors){
          if ((eventDto.getBasePrice() > eventDto.getMaxPrice()) && eventDto.getMaxPrice() != 0){
              errors.rejectValue("basePrice", "wrongValue", "basePrice is wrong");
              errors.rejectValue("maxPrice", "wrongValue", "maxPrice is wrong");
          }

          LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
          if (endEventDateTime.isBefore(eventDto.getBeginEventDateTime()) ||
              endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime()) ||
              endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())){
          errors.rejectValue("endEventDateTime", "wrongValue", "endEventDateTime is wrong");
          }
      }
     }
     ```

     위와 같이 Validator 객체를 만들어 Controller에서 입력값 검증을 할 수 있다.

     Controller에서 EventValidator를 주입받은 후 정의한 validate 메서드를 통해 입력값 검증이 가능하다.

     > ※ Test 이름으로 해당 테스트를 파악하기 힘든 경우
     >
     > 1. 주석으로 설명하기
     > 2. annotation으로 추가 설명하기
     >
     > Junit5는 Test에 대해 설명을 할 수 있는 annotation이 있지만 Junit4에는 없다. 따라서 커스텀한 annotation을 만들어 해당 테스트에 부연설명을 할 수 있다.

     ```java
     @Target(ElementType.METHOD)
     @Retention(RetentionPolicy.SOURCE)
     public @interface TestDescription {
      String value();
     }
     ```

     이렇게 만든 Bad Request에 현재는 응답 본문이 존재하지 않는다. 응답 본문을 ResponseEntity.BadRequest\(\).body\(\)에서 body의 인자로 errors를 메세지로 주게 되면 runtime 오류가 발생한다. 이유는 **errors는 Java Bean Spec을 따르지 않기 때문에** JSON으로 반환하는 **BeanSerializer**가 errors를 응답 본문으로 만들때 오류가 발생하는 것이다.

     이를 해결하기 위해서는 다양한 방법 중 하나로 errors를 serialize 할 수 있도록 **JsonSerializer&lt;?&gt;**를 상속받은 객체를 **@JsonComponent**로 등록하는 방법이 있다.

     ```java
     @JsonComponent
     public class ErrorsSerializer extends JsonSerializer<Errors> {
      @Override
      public void serialize(Errors errors, JsonGenerator gen, SerializerProvider serializers) throws IOException {
          gen.writeStartArray();
          errors.getFieldErrors().stream().forEach(e -> {
              try{
                  gen.writeStartObject();
                  gen.writeStringField("field", e.getField());
                  gen.writeStringField("objectName", e.getObjectName());
                  gen.writeStringField("code", e.getCode());
                  gen.writeStringField("defaultMessage", e.getDefaultMessage());
                  Object rejectedValue = e.getRejectedValue();
                  if (rejectedValue != null){
                      gen.writeStringField("rejectedValue", rejectedValue.toString());
                  }
                  gen.writeEndObject();
              } catch (IOException exception){
                  exception.printStackTrace();
              }
          });

          errors.getGlobalErrors().stream().forEach(e -> {
              try{
                  gen.writeStartObject();
                  gen.writeStringField("objectName", e.getObjectName());
                  gen.writeStringField("code", e.getCode());
                  gen.writeStringField("defaultMessage", e.getDefaultMessage());
                  gen.writeEndObject();
              } catch (IOException exception){
                  exception.printStackTrace();
              }
          });
          gen.writeEndArray();
      }
     }
     ```

     errors에는 2가지 경우의 error가 존재한다. **Field 에러**와 **Global 에러**가 있다.

     Field 에러는 errors에서 **rejectValue 메서드**를 사용하는 경우 field 에러로 간주한다. 이는 주로 하나의 field에 입력값 문제가 있는 경우 사용한다.

     반면 Global 에러는 **reject 메서드**를 이용한다. 여러 복합적인 필드에서 문제가 발생한 경우 사용한다.

     Spring boot가 제공하는 **@JsonComponent**는 ObjectMapper에 JsonSerializer로 등록한 객체를 Json으로 반환하도록 등록해주는 역할을 한다.
* 데이터 사이언스 by python
  1. pivot table / crosstab

     pivot table은 Excel에서 쓴 것과 동일하다.

     index 축은 groupby와 동일하며 column에 추가로 labeling 값을 추가해서 value에 numeric type의 값을 aggregation 하는 형태가 바로 **pivot table**!

     ```python
     import pandas as pd, dateutil

     df = pd.read_csv("phone_data.csv")
     df["date"] = df["date"].apply(dateutil.parser.parse, dayfirst=True)

     df.pivot_table(["duration"],
                  index=[df.month, df.item],
                  columns=df.network, aggfunc="sum", fill_value=0)
     ```

     pivot\_table 메서드로 첫 번째 인자는 집계를 하고 싶은 데이터 목록을 넣어준다. 축이 되는 부분은 **index** 인자로 기준을 잡을 때는 **columns** 인자로 넣어준다. **aggfunc**은 집계 함수, **fill\_value**는 NaN 값을 대체할 값을 넣어준다.

     crosstab은 두 칼럼에 교차 빈도, 비율, 덧셈 등을 구할 때 사용한다. pivot table의 특수한 형태로 User-Item Rating Matrix를 만들 때 사용할 수 있다.

     ```python
     pd.crosstab(index=df.critic, columns=df.title, values=df.rating, aggfunc="first").fillna(0)
     ```

     pivot table과 비슷한 방법으로 사용할 수 있다. 다만 index에 들어가는 기준과 columns에 해당하는 인자가 1:1로 대응된다는 점이 차이점이다.

     > fillna메서드는 NaN값을 대체하는 함수로 위 예시에서는 0이 인자로 들어왔기 때문에 NaN을 0으로 대체한다.

  2. Merge & Concat

     Merge는 SQL의 join과 같은 기능으로 두 개의 데이터를 하나로 합치는 방법이다.

     ```python
     # data
     scores = pd.DataFrame([[1,51],[2, 15], [3, 15], [4, 61], [5, 16], [7, 14]], columns=["subject_id", "test_score"])
     students = pd.DataFrame([[4, "Billy", "Bonder"], [5, "Brian", "Black"], [6, "Bran", "Balwner"], [7, "Bryce", "Brice"], [8, "Betty", "Btisan"]], columns=["subject_id", "first_name", "last_name"])

     # merge
     pd.merge(scores, students, on="subject_id") # inner join
     ```

     위 예시와 같이 pandas의 merge 함수를 이용하여 두 DataFrame을 합칠 수 있다. merge함수의 첫 번째, 두 번째 인자로는 dataframe의 이름, on 인자는 합치는 기준이 되는 column의 이름을 넣어준다.

     ```python
     pd.merge(scores, students, left_on="subject_id", right_on="subject_id")  
     pd.merge(scores, students,"subject_id", how="left") # left join  
     pd.merge(scores, students, on="subject_id", how="right") # right join
     pd.merge(scores, students, on="subject_id", how="outer") # outer join
     ```

     위 예시 같이 on 대신 left\_on과 right\_on으로 대체할 수 있다. 주로 두 dataframe에 합칠 기준이 되는 column의 이름이 다를때 사용한다.

     또한, join 유형을 how 인자를 통해 선택할 수 있다.

     그 외에도 index를 기준으로 join도 가능하다. index의 key값을 이용하여 join을 하는 방법이다.

     ```python
     pd.merge(scores, students, left_index=True, right_index=True)
     ```

     Concat은 numpy의 concat과 마찬가지로 두 DataFrame을 합치는 방법이다.

     ```python
     name1 = pd.DataFrame({
      "subject_id": [1,2,3],
      "first_name": ["Alex", "Amy", "Allen"],
      "last_name": ["Anderson", "Ackerman", "Ali"]
     })
     name2 = pd.DataFrame({
      "subject_id": [4,5,6],
      "first_name": ["Billy", "Brian", "Bran"],
      "last_name": ["Bonder", "Black", "Balwner"]
     })

     df = pd.concat([name1, name2])
     df.reset_index()
     ```

     pandas에서 제공하는 concat 함수로 dataframe을 합칠 수 있다. 위 예시의 concat은 두 DataFrame을 위아래로 합친다.

     이 때, 합쳐지고 나면 name1과 name2의 index가 해당 dataframe에 있던대로 그대로 나타나기 때문에 reset\_index\(\)를 해주는 것이 좋다.

     > ※ pd.concat\(\[name1, name2\]\)은 name1.append\(name2\)와 동일한 기능을 한다.

     위에서는 위아래로\(axis=0\) 합쳤지만 양옆으로도 합칠 수 있다. 이는 axis=1 인자를 concat 함수 내부에 넣어주면 된다.

     참고로 위 groupby된 데이터를 sort하는 경우에는 reset\_index\(\)를 해준 후에 sort하는 것이 좋다. reset\_index\(\)를 하지 않은 경우는 groupby 형태가 깨져서 나타나기 때문에 먼저 reset\_index\(\)를 하고 sort\_value를 부르면 약간의 꼼수로 groupby로 그루핑을 한 것과 같이 sorting을 할 수 있다.

     ```python
     df.groupby(["status", "name_x"])["quantity", "ext price"].sum().reset_index().sort_value(by=["status", "quantity"], ascending=False)
     ```

  3. Database connection & persistance

     sqlite3 기준 db 연결 방법

     > sqlite는 파일 기반의 가벼운 RDBMS이다.
     >
     > [참고 : 공식 사이트](https://www.sqlite.org/index.html)

     ```python
     conn = sqlite3.connect("./data/students.db") # 해당 db 파일 연결
     cur = conn.cursor() # 실행 cursor 가져오기
     cur.execute("select * from students limit 5;") # sql문 실행
     result = cur.fetchall()
     ```

     pandas로 불러오기

     ```python
     df = pd.read_sql_query("select * from students", conn)
     ```

     pd.read\_sql\_query로 첫 인자는 sql문을 두번째 인자로는 db에 연결한 connection 객체를 넣어준다. 이를 통해 sql문의 결과를 pandas의 dataframe으로 받을 수 있다.

     XLS persistence는 Dataframe의 엑셀 추출 코드이다. xls 엔진으로는 보통 openpyxl나 xlsxWrite를 이용한다. \(pip로 설치 필요\)

     ```python
     writer = pd.ExcelWriter("./data/df_routes.xlsx", engine="xlsxwriter") # 첫번째 인자로 들어온 경로에 xlsxwriter 엔진을 이용하여 적겠다는 의미
     df.to_excel(writer, sheet_name="sheet1") # 위 writer 객체를 이용하여 sheet1에 df의 내용을 쓰는 방법
     ```

     Pickle persistance는 가장 일반적인 python 파일 persistence이다. 보통 전처리를 하고 임시 저장하는 경우 pickle 형태로 많이 저장한다.

     ```python
     df.to_pickle("./data/df_routes.pickle") # df의 내용을 pickle 형태로 쓰는 방법
     df = pd.read_pickle("./data/df_routes.pickle") # pickle 파일 불러오는 방법
     ```


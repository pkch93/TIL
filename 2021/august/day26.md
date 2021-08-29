# 2021.08.26 TIL - Spring Batch Transaction

- [2021.08.26 TIL - Spring Batch Transaction](#20210826-til---spring-batch-transaction)
  - [DefaultBatchConfigurer](#defaultbatchconfigurer)
  - [참고](#참고)

Spring Batch에서 Step을 정의하는 StepBuilderFactory를 보면 TransactionManager를 설정하는 부분이 있다.

```java
@Bean(STEP_NAME)
public Step inactiveUserBatchStep(StepBuilderFactory stepBuilderFactory) {
    return stepBuilderFactory.get(STEP_NAME)
            .transactionManager(transactionManager)
            .<String, Integer>chunk(chunkSize)
            .reader(reader())
            .processor(processor())
            .writer(writer())
            .build();
}
```

기본적으로 Spring Batch에서 사용하는 TransactionManager는 transactionManager 이름의 빈을 찾는다. 만약 transactionManager 이름의 빈이 존재하지 않으면 에러가 난다.

이때 사용하는 transactionManager는 2가지 역할을 한다.

1. JobRepository가 배치 관련 메타데이터를 저장할 때 사용할 트랜잭션 관리
2. 각 Step에서 사용할 트랜잭션 관리

    즉, 위 Step의 transactionManager가 청크단위의 트랜잭션 관리하는 역할을 한다.

## DefaultBatchConfigurer

만약 TransactionManager, JobExplorer 등 배치 설정을 커스텀할 필요가 있다면 `org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer`을 확장한 Configuration을 정의할 수 있다.

만약 transactionManager 재정의가 필요하다면 getTransactionManager를 재정의하여 커스터마이징 할 수 있다. 반면 트랜잭션이 필요하지 않는 경우라면 `org.springframework.batch.support.transaction.ResourcelessTransactionManager`를 지정할 수 있다.

## 참고

Spring Batch in Action: [https://livebook.manning.com/book/spring-batch-in-action/chapter-9/38](https://livebook.manning.com/book/spring-batch-in-action/chapter-9/38)
Why is setting TransactionManager as JPATransactionManager in a Step not correct? 참고: [https://stackoverflow.com/questions/66104734/why-is-setting-transactionmanager-as-jpatransactionmanager-in-a-step-not-correct](https://stackoverflow.com/questions/66104734/why-is-setting-transactionmanager-as-jpatransactionmanager-in-a-step-not-correct)
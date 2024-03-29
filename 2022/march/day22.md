# 2022.03.22 TIL - 좋은 단위 테스트

- [2022.03.22 TIL - 좋은 단위 테스트](#20220322-til---좋은-단위-테스트)
  - [회귀 방지](#회귀-방지)
  - [리팩터링 내성](#리팩터링-내성)
    - [거짓 양성을 줄이는 방법](#거짓-양성을-줄이는-방법)
  - [빠른 피드백](#빠른-피드백)
  - [유지보수성](#유지보수성)
    - [테스트가 얼마나 이해하기 어려운가](#테스트가-얼마나-이해하기-어려운가)
    - [테스트가 얼마나 실행하기 어려운가](#테스트가-얼마나-실행하기-어려운가)

좋은 단위 테스트에는 다음 4가지 특성이 있다.

- 회귀 방지 `버그 방지, 사이드이펙트 예방`
- 리팩터링 내성
- 빠른 피드백
- 유지보수성

## 회귀 방지

회귀란 소프트웨어 버그를 의미한다. 즉, 회귀 방지 특성은 기능이 추가/수정됨으로써 발생하는 사이드이펙트를 방지하는 특성이다.

코드베이스가 커질수록 잠재적인 버그에 더 많이 노출된다. 따라서 회귀에 대해 효과적으로 보호할 수 있도록 개발해야한다. 일반적으로 실행되는 코드가 많을수록 테스트에서 회귀가 나타날 가능성이 높다.

테스트에서 실행되는 코드의 양 뿐만아니라 복잡도 및 도메인 유의성도 고려해야한다. 복잡한 비즈니스를 다루는 코드가 보일러플레이트 코드보다 중요하므로 이에 대한 테스트를 잘 작성해야한다.

여기에 애플리케이션에서 사용하는 라이브러리, 프레임워크나 연동하는 외부 시스템도 중요하다. 애플리케이션을 구동하는데 필수적인 요소이기 때문에 이들을 테스트 범주에 포함시켜서 이런 의존성들에 대한 검증을 해주어야한다.

> 회귀 방지 지표는 테스트 중 실행되는 코드의 양, 코드 복잡도, 코드 도메인 유의성 이 3가지 사항을 고려하여 만들어진다.

회귀 방지 지표를 극대화하려면 테스트가 가능한 많은 코드를 실행하는 것을 목표로 해야 한다.

## 리팩터링 내성

리팩터링 내성이란 기존에 존재하는 테스트를 빨간색 `실패` 으로 바꾸지 않고 기존 애플리케이션을 리펙터링 할 수 있는지에 대한 척도이다.

만약 기존 기능을 리팩터링 했다고 가정한다. 리팩터링을 통해 좀 더 보기 좋은 코드로 만든 것 같다. 다만, 기존에 존재하던 테스트가 깨지고 있다. 리팩터링한 코드도 이전과 마찬가지로 기능은 정상으로 동작하지만 테스트가 실패하고 있는 것이다. 이런 상황을 **거짓 양성**이라고 한다.

> 거짓 양성은 구현을 수정하지만 식별할 수 있는 동작은 유지할 때 발생한다.

리팩터링 내성을 높이기 위해서는 거짓 양성을 줄여야한다. 거짓 양성은 전체 테스트 스위트의 신뢰성에 영향을 줄수 있다. 거짓 양성이 많을수록  테스트를 더이상 믿지 못하게 되며 종국에는 테스트가 실패하더라도 그대로 운영환경에 반영하는 지경에 이를 수 있다.

### 거짓 양성을 줄이는 방법

결국 리팩터링 내성을 높이는 방법은 거짓 양성을 줄이는 것이다. 보통 테스트에서 발생하는 거짓 양성의 수는 테스트 구성 방식과 연관있다.

테스트와 SUT 간의 구현 세부 사항이 많이 결합될수록 허위 정보가 더 많이 생기게 된다. 때문에 구현 세부 사항으로부터 테스트를 분리하는 것이 거짓 양성을 줄이는 방법이다. 테스트는 **최종 사용자의 관점에서 SUT를 검증**해야하고 최종 사용자에게는 의미 있는 결과만 확인하도록 작성해야한다.

> 참고
테스트에서 내부 구현 검증 피하기 [https://jojoldu.tistory.com/614?category=1036934](https://jojoldu.tistory.com/614?category=1036934)

## 빠른 피드백

빠른 피드백은 단위 테스트의 필수적인 요소이다. 테스트 속도가 빠를수록 테스트 스위트에서 더 많은 테스트를 수행할 수 있고 더 자주 수행이 가능하다. 테스트가 빠르게 수행되어야 코드 결함에 대한 피드백을 더 빨리 받을 수 있으므로 버그를 수정하는 비용을 대폭 줄일 수 있게 된다.

반면 실행에 시간이 오래 걸리는 테스트는 자주 실행을 하지 못하므로 피드백을 느리게 하고 버그를 뒤늦게 발견하게 만든다.

## 유지보수성

유지보수성은 **테스트가 얼마나 이해하기 어려운지**와 **테스트가 얼마나 실행하기 어려운지**로 판단할 수 있다.

### 테스트가 얼마나 이해하기 어려운가

테스트는 보통 코드라인이 적을수록 읽기가 용이하다. 테스트를 작성할 때 각 테스트를 일급 시민으로 생각하고 독립적으로도 잘 동작이 되도록 만들어야한다. 따라서 테스트 절차를 생략하지 않고 각 테스트에 필요한 것들을 하나의 테스트에 모두 작성해주어야 한다.

### 테스트가 얼마나 실행하기 어려운가

테스트가 프로세스 외부 종속성으로 작동하게 되면 데이터베이스 서버 구동, 네트워크 연결 등의 문제가 발생하고 이로 인해 테스트가 깨지고 실행하기 어려운 문제가 있다. 따라서 이런 외부 의존성을 관리하는데에도 신경을 써야한다.

> Spring Boot에서 제공하는 `@SpringBootTest`의 경우도 Spring 컨텍스트가 변하게 되면 애플리케이션을 다시 띄우므로 테스트가 느리게 만드는 원인이 된다. 따라서 모든 `@SpringBootTest`를 사용하는 테스트들의 컨텍스트를 동일하게 만들어 애플리케이션을 두 번 이상 띄우지 않도록 만들어 실행시간을 줄여줄 수 있다.

> 각 테스트들은 외부 환경에 관계 없이 항상 동일한 결과를 얻을 수 있어야한다. 따라서 실제 API 서버나 DB를 사용하기보다는 실제 환경에 맞는 인프라를 docker나 testContainers 같은 도구를 연결하여 사용할 수 있다.

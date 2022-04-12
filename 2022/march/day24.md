# 2022.03.24 TIL - 이상적인 단위 테스트

좋은 단위 테스트의 네 가지 특성 중 회귀 방지, 리펙터링 내성, 빠른 피드백은 상호 베타적이다. 두 가지 특성은 극대화할 수 있지만 이렇게 되면 하나의 특성은 반드시 희생해야한다. 때문에 세 가지 특성 모두를 완벽하게 만족할 수는 없다.

네 번째 특성인 유지 보수성은 엔드 투 엔드 테스트를 제외하고는 위 세 가지 특성과는 상관관계가 없다. 엔드 투 엔드의 경우는 모든 의존성을 설정해야하기 때문에 엔드 투 엔드에서는 유지 보수성이 낮아질 수 밖에 없다.

그리고 리팩터링 내성은 최대한 많이 가지는 것을 목표로 해야 추후 테스트를 작성하고 코드 품질을 높이는 데 장애물 없이 진행할 수 있다. 리팩터링 내성은 보통 내성이 있는지/없는지 이진 선택이다. 즉, 있다/없다로 나뉘기 때문에 리팩터링 내성을 갖추는 쪽으로 테스트를 작성해야한다.

이런 이유로 회귀 방지와 빠른 피드백 사이에서 선택을 해야한다.

> 사실 테스트 스위트를 단단하게 하려면 테스트의 불안정성인 **거짓 양성**을 제거해야한다.

## 테스트 피라미드

테스트 피라미드는 테스트 스위트에서 테스트 유형 간의 일정한 비율을 보여주는 개념이다.

테스트를 작성하기 쉬운 단위 테스트의 테스트 수가 가장 많고 간단하게 테스트할 수 있으므로 의존성을 준비해야할 코드의 수가 적다. 윗 단계인 통합 테스트와 엔드 투 엔드 테스트로 올라갈수록 의존성 준비에 드는 코드가 많아지고 테스트의 수도 적어진다.

이렇게 피라미드에서 나타내는 테스트의 유형에 따라 빠른 피드백과 회귀 방지 사이에서 선택이 필요하다. 상단으로 갈수록 회귀 방지를 강조하고 하단은 빠른 피드백을 강조한다.
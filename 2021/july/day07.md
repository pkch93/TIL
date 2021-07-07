# 2021.07.07 TIL - Auto Scaling 동적 조정 정책

- [2021.07.07 TIL - Auto Scaling 동적 조정 정책](#20210707-til---auto-scaling-동적-조정-정책)
  - [Auto Scaling 동적 조정 정책](#auto-scaling-동적-조정-정책)
    - [동적 조정 정책 유형](#동적-조정-정책-유형)
      - [대상 추적 조정 `Target tracking scaling`](#대상-추적-조정-target-tracking-scaling)
      - [단계 조정 `Step scaling`와 간편 조정 `Simple scaling`](#단계-조정-step-scaling와-간편-조정-simple-scaling)

## Auto Scaling 동적 조정 정책

Auto Scaling 동적 조정이란 수요 변화에 따라 Auto Scaling Group의 용량을 조정하는 방법을 의미한다. 동적 조정 정책은 Auto Scaling Group이 특정 CloudWatch 지표를 추적하여 연결된 CloudWatch 알림이 경보 상태에 있을 때 수행할 작업을 정의하는 것을 의미한다. 이때 경보를 울리는데 사용하는 지표는 Auto Scaling Group에 연결된 모든 인스턴스의 지표를 집계한 것이다.

### 동적 조정 정책 유형

동적 조정 정책에는 대상 추적 조정 `Target tracking scaling`, 단계 조정 `Step scaling`, 간편 조정 `Simple scaling`이 있다.

#### 대상 추적 조정 `Target tracking scaling`

대상 추적 조정 정책은 조정 측정 단위를 선택하여 Auto Scaling Group을 확장/축소하도록 만드는 정책이다.

대상 추적 조정 정책은 다음과 같은 특성으로 동작한다.

- 대상 추적 조정 정책은 **지정한 측정치가 목표 값을 초과할 때 Auto Scaling Group을 확장**하도록 되어 있다. 즉, 대상 추적 조정 정책을 사용하여 지정된 측정치가 목표 값보다 작을 때 Auto Scaling Group을 확장할 수 없습니다.
- 대상 값과 실제 지표 데이터 포인트 사이에는 차이가 발생할 수 있다. 추가하거나 제거할 인스턴스 수를 결정할 때마다 **항상 반올림 또는 내림을 통해 어림짐작으로 동작**하기 때문인데 이는 인스턴스를 부족하게 추가하거나 너무 많이 제거하는 일을 방지하기 위해서이다.

    이때 하지만 인스턴스가 줄어서 Auto Scaling Group이 작아지는 경우에는 그룹의 사용량이 목표 값에서 멀어질 수도 있다. 예를 들어 CPU 사용률 목표값을 50% 로 설정한 후 Auto Scaling Group이 목표값을 초과한다고 가정할 때 1.5개의 인스턴스를 추가하면 CPU 활용률이 50% 가까이 감소할 것을 알 수 있다. 하지만 1.5개의 인스턴스를 추가할 수 없기 때문에 반올림을 통해 인스턴스 2개를 추가하게 되는데 그러면 CPU 사용량이 50% 아래로 떨어지는 동시에 애플리케이션은 리소스를 충분히 확보하게 된다. 마찬가지로 인스턴스를 1.5개 제거하면 CPU 사용량이 증가하여 50%를 상회한다고 판단할 경우에는 인스턴스를 1개만 제거하게된다.

- 인스턴스가 더 많고 크기가 큰 Auto Scaling Group의 경우 사용률이 더 많은 수의 인스턴스에 분산되므로 인스턴스를 추가 또는 제거하면 대상 값과 실제 지표 데이터 포인트 간에 차이가 줄어든다.
- 애플리케이션 가용성을 보장하기 위해 Auto Scaling Group은 측정치에 비례하여 가능한 신속하게 확장되지만, 축소는 점진적으로 이루어진다.
- 각각 다른 측정치를 사용한다는 전제 하에 **Auto Scaling Group에 대해 다수의 대상 추적 조정 정책을 보유할 수 있다**. Auto Scaling의 목적은 항상 가용성을 우선시하므로, 대상 추적 정책이 확장 또는 축소를 허용하는지에 따라 그 동작이 달라지는데 대상 추적 정책 중 하나라도 확장을 허용할 경우 Auto Scaling Group을 확장하지만 모든 대상 추적 정책이 축소를 허용하는 경우에만 그룹을 축소하게된다.

    [다중 동적 조정 정책 제공](https://docs.aws.amazon.com/ko_kr/autoscaling/ec2/userguide/as-scale-based-on-demand.html#multiple-scaling-policy-resolution) 섹션을 참고

- **대상 추적 조정 정책에서 축소 부분을 비활성화할 수 있다**. 이 기능은 Auto Scaling Group의 크기를 다른 방법으로 조정할 수 있는 유연성을 제공한다.
- 대상 추적 조정 정책을 위해 구성된 CloudWatch 경보는 편집하거나 삭제하면 안된다. 대상 추적 조정 정책과 연결된 CloudWatch 경보는AWS이며 더 이상 필요하지 않을 때 자동으로 삭제된다.

기본적으로 Auto Scaling Group 평균 CPU 사용률 `ASGAverageCPUUtilization`,  Auto Scaling Group 별 모든 네트워크 인터페이스에서 수신한 평균 바이트 수 `ASGAverageNetworkIn`,  Auto Scaling Group 별 모든 네트워크 인터페이스에서 송신한 평균 바이트 수 `ASGAverageNetworkOut`, ALB Target Group에서 Target 별 요청 수 `ALBRequestCountPerTarget`를 사용하여 동적 조정 정책을 설정할 수 있으며 필요에 따라 직접 지정하여 정책 설정을 할 수 있다.

#### 단계 조정 `Step scaling`와 간편 조정 `Simple scaling`

단계 조정 정책과 간편 조정 정책은 CloudWatch 경보에 대한 조정 지표와 임계값을 선택하여 지정한 기간 동안 임계값을 위반했을때 Auto Scaling Group을 어떻게 조정할 지 정의하는 방법이다.

따라서 두 정책 모두 조정 정책에 대한 CloudWatch 알림을 생성해야한다. 모두 인스턴스를 추가 또는 제거할 지에 대한 여부와 추가/제거할 인스턴스 대수를 정의해야한다.

두 정책의 차이는 단계 조정 정책을 통해 단계 조절이 가능한지 불가능한지로 나뉜다. 가능하다면 단계 조정, 불가능하다면 간편 조정이 된다.

단순 조정 정책은 확장/축소가 시작된 후에 조정 활동이나 상태 확인 대체가 완료되고 휴지 기간이 만료될 때까지 기다린 후에 추가 알림에 응답해야한다는 문제가 있다. 반면 단계 조정에서는 조정 활동 또는 상태 확인 대체가 진행 중일때에도 정책이 추가 알림에 응답할 수 있다.

> 휴지 기간이란 이전 조정 활동이 적용되기 전에 인스턴스를 추가로 시작하거나 종료하지 않도록 대기하는 시간이다. 이를 통해 만약 인스턴스가 비정상적 상태인 경우 휴지 기간이 완료될 때까지 대기하지 않고 비정상적 인스턴스를 교체한다.

따라서 대부분의 경우 단계 조정이 간편 조정보다 낫다.

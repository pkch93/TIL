# 2021.10.21 TIL - effective typescript item 5. any 타입 지양하기

- [2021.10.21 TIL - effective typescript item 5. any 타입 지양하기](#20211021-til---effective-typescript-item-5-any-타입-지양하기)
  - [any 타입의 위험성](#any-타입의-위험성)
    - [any는 타입 안전성이 없다.](#any는-타입-안전성이-없다)
    - [함수 시그니처를 무시한다.](#함수-시그니처를-무시한다)
    - [any 타입은 IDE의 도움을 받을 수 없다.](#any-타입은-ide의-도움을-받을-수-없다)
    - [코드 리팩터링 시 버그를 감춘다.](#코드-리팩터링-시-버그를-감춘다)
    - [any는 타입 설계를 감춘다.](#any는-타입-설계를-감춘다)
    - [any는 타입 시스템의 신뢰도를 낮춘다.](#any는-타입-시스템의-신뢰도를-낮춘다)
  - [참고](#참고)

타입스크립트 특성상 타입은 점진적이고 선택적이다. 코드에 타입을 조금씩 추가할 수 있기 때문에 점진적이고 타입 체커를 언제든 헤제할 수 있기 때문에 선택적이다. 이를 가능하게 하는 건 any 타입이 존재하기 때문이다.

단, any 타입을 무분별하게 사용하는 것은 타입스크립트의 최대 장점을 포기하는 것과 같다. 때문에 부득이한 경우를 제외하고는 any 타입 사용을 자제해야하며 any를 사용하더라도 그 위험성을 이해하고 있어야한다.

## any 타입의 위험성

### any는 타입 안전성이 없다.

```tsx
let age = number;
age = '12' as any;
```

위와 같이 age가 number 타입으로 선언되어 있더라도 `as any` 문법으로 string 타입을 할당할 수 있다.

```tsx
age += 1 // '121'
```

위 상태에서 age에 1을 더하면 문자열 `'121'`이 된다.

> 참고로 타입스크립트의 `as` 문법은 Type Assertions로 타입스크립트가 변수의 타입 추론에 추가 정보를 전달해주는 문법이다. `as any`로 `any` 타입으로 타입스크립트가 인식하도록 만든다.

참고: [https://www.typescriptlang.org/docs/handbook/2/everyday-types.html#type-assertions](https://www.typescriptlang.org/docs/handbook/2/everyday-types.html#type-assertions)
> 

### 함수 시그니처를 무시한다.

호출하는 쪽은 함수 시그니처에 정의된대로 인자의 타입에 맞춰서 함수를 호출해야한다. 단, any 타입을 사용하게 되면 타입에 상관없이 아무 타입의 인자를 넣을 수 있게되고 특히 자바스크립트에서는 암묵적으로 타임 변환이 이뤄지는 경우가 있기 때문에 문제를 일으킬 수 있다.

### any 타입은 IDE의 도움을 받을 수 없다.

any는 타입 추론이 불가능하기 때문에 Visual Studio Code와 같은 통합 개발 환경 툴 `IDE`에서 자동완성 기능이나 도움말을 받을 수 없다.

### 코드 리팩터링 시 버그를 감춘다.

```tsx
function handleSelectitem(item: any) { 
	selectedld = item.id;
}
```

위와 같은 `handleSelectItem` 함수가 있다고 가정한다. `handleSelectItem` 는 any 타입의 Item을 인자로 가진다.

이때 item에 어떤 값이 올지 몰라 any로 선언했다고 가정한다. 이 경우 item은 어떤 타입이라도 올 수 있다. 즉, item에 id가 존재하지 않을수도 있다.

`handleSelectItem`에서는 item의 id만 필요하기 때문에 any 타입의 item이 아닌 구체적인 타입의 number를 받을 수 있다. 따라서 아래와 같이 변경하는 것이 적절하다.

```tsx
function handleSelectitem(id: number) { 
	selectedld = id;
}
```

이와 같이 any가 아닌 적절한 타입을 사용한다면 타입 체커의 도움을 받아 타입 오류를 발견할 수 잇다.

### any는 타입 설계를 감춘다.

### any는 타입 시스템의 신뢰도를 낮춘다.

## 참고

이펙티브 타입스크립트 (댄 밴더캄 저, 장원호 역) item 5: [http://www.kyobobook.co.kr/product/detailViewKor.laf?mallGb=KOR&ejkGb=KOR&barcode=9788966263134](http://www.kyobobook.co.kr/product/detailViewKor.laf?mallGb=KOR&ejkGb=KOR&barcode=9788966263134)
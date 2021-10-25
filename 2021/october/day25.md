# 2021.10.25 TIL - Effective Typescript item 6, 7

## item 6. 편집기를 사용하여 타입 시스템 탐색하기

타입스크립트를 설치하면 타입스크립트 컴파일러 `tsc`와 단독으로 실행할 수 있는 타입스크립트 서버 `tsserver`를 실행할 수 있다.

보통 언어 서비스에서 코드 자동 완성, 문법 검사, 검색, 리펙터링을 지원한다. 편집기에서 언어 서비스를 사용하는 경우가 대부분인데 타입스크립트 서버에서 이를 제공하도록 설정하는 것이 좋다. 이를 통해 변수가 어떤 타입으로 추론이 되는지, 어떻게 타입시스템이 동작하는지 등의 개념을 잡을수있다.

> 참고로 vscode의 타입스크립트 지원은 기본적으로 tsserver를 사용한다.

tsserver 참고: [https://github.com/Microsoft/TypeScript/wiki/Standalone-Server-(tsserver)](https://github.com/Microsoft/TypeScript/wiki/Standalone-Server-%28tsserver%29)
> 

## 참고

이펙티브 타입스크립트 (댄 밴더캄 저, 장원호 역) item 6: [http://www.kyobobook.co.kr/product/detailViewKor.laf?mallGb=KOR&ejkGb=KOR&barcode=9788966263134](http://www.kyobobook.co.kr/product/detailViewKor.laf?mallGb=KOR&ejkGb=KOR&barcode=9788966263134)

## item 7. 타입이 값들의 집합이라고 생각하기

런타입에 모든 변수는 각자 고유의 값을 가진다. `'hello world'`, `10`, `null`, `undefined`, `() => console.log('hello')` 와 같은 값들이 모두 변수에 할당할 수 있는 값의 예시이다.

단, 코드가 실행되기 전에 타입스크립트가 타입 체커로 코드의 오류를 체크할 때에는 타입이라는 것을 가지고 있다. 이때 타입은 **할당 가능한 값들의 집합**이다.

타입스크릡트에서 가장 작은 범위의 집합은 공집합으로 `never` 타입으로 지원한다.

```tsx
const x: never = 'hello world!' // error
```

위와 같이 x의 타입은 `never`이기 때문에 위 할당문은 에러가 발생한다.

그 다음으로 작은 집합은 하나의 값을 포함하는 리터럴 `literal` 타입이다. 이는 유닛 `unit` 타입이라고도 불린다.

```tsx
type A = 'A'
type B = 'B'
type One = 1
```

만약 리터럴 타입을 서로 조합하고자 한다면 유니온 `union` 타입을 이용할 수 있다.

```tsx
type AB = A | B
```

`|` 문법으로 여러 타입을 조합할 수 있다.

```tsx
const ab: AB = 'A' // success
const ab2: AB = 'C' // error
```

타입 `AB`는 A 또는 B로 이뤄진 타입인 반면 `C`는 단일 타입이다. 그리고 AB의 부분 집합에 해당하지 않기 때문에 이는 오류가 된다. 집합의 관점에서 타입 체커의 주요 기능은 하나의 집합이 다른 집합의 부분 집합인지 검사하는 것이라고 할 수 있다.

## Unions Types과 Intersection Types

참고: [https://www.typescriptlang.org/docs/handbook/unions-and-intersections.html](https://www.typescriptlang.org/docs/handbook/unions-and-intersections.html)

### Unions Types

Unions Types은 합집합 개념으로 이따금씩 한 변수에 여러 타입이 올 수도 있다. 즉, `number`나 `string` 타입이 올 수 있다. 이런 경우에 Unions Types를 사용할 수 있다.

```tsx
const x: number | string = 1
const y: number | string = '1'
```

### Intersection Types

Intersection Types는 교집합으로 생각할 수 있다. 때문에 만약 타입에 겹치는 필드가 없다면 공집합 `never`라고 생각할 수 있다. Intersection Types는 여러 타입을 하나의 타입처럼 사용하는 문법으로 Intersection Types에 있는 모든 기능을 하나의 타입으로사용할 수 있다.

```tsx
interface ErrorHandling {
	error?: {
		message: string;
	}
}

interface StudentData {
	students: { name: string, `class`: string }[];
}

type StudentResponse = StudentData & ErrorHandling;
```

위와 같이 StudentResponse 타입은 StudentData와 ErrorHandling의 특성을 모두 가져야한다. 따라서 StudentData의 students 필드에 접근할 수 있으며 ErrorHandling의 error 필드에도 접근가능하다.

### keyof

참고로 keyof 연산에서 Unions Types과 Intersection Types는 다음 등식이 성립한다.

```tsx
keyof (A | B) = (keyof A) & (keyof B)
keyof (A & B) = (keyof A) | (keyof B)
```

> keyof 타입 연산자는 객체의 키를 리터럴 타입으로 반환하는 연산자이다.

위 등식은 타입스크립트의 타입 시스템을 이해하는데 큰 도움이 될 것이다.
> 

### extends와 Unions Types

타입을 확장하는 방법으로 extends 키워드도 타입스크립트에서 지원한다.
타입이 집합이라는 타입스크립트의 관점에서 extends는 **부분 집합**이라는 의미로 이해할 수 있다.

```tsx
interface Vector1D {
	x: number;
}

interface Vector2D extends Vector1D {
	y: number;
}

interface Vector3D extends Vector2D {
	z: number;
}
```

위와 같이 Vector를 한 차원씩 확장한 Vector1D, Vector2D, Vector3D 인터페이스가 있다고 가정한다. 여기서 Vector3D는 Vector2D의 서브타입, 즉, 부분집합이고 Vector2D는 Vector1D의 부분집합이다.

> extends로 타입을 확장하는 Java의 경우는 왼쪽 확장 구조로 표현하는 것이 적절하지만 타입을 집합의 개념으로 보는 타입스크립트에서는 오른쪽 벤 다이어그램이 더 적절하다.

참고로 타입스크립트의 extends는 제네릭 타입에서 한정자로 사용할 수 있으며 이때의 의미는 부분 집합의 의미가 된다.

```tsx
function getKey<K extends string>(val: any, key: K) {
	// ...
}
```

위와 같은 `getKey` 함수가 있을때 제너럴 타입 `K`는 string의 부분집합이어야한다.

## item 7 참고

이펙티브 타입스크립트 (댄 밴더캄 저, 장원호 역) item 7: [http://www.kyobobook.co.kr/product/detailViewKor.laf?mallGb=KOR&ejkGb=KOR&barcode=9788966263134](http://www.kyobobook.co.kr/product/detailViewKor.laf?mallGb=KOR&ejkGb=KOR&barcode=9788966263134)
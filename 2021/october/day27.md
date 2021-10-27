# 2021.10.27 TIL - Effective Typescript item 8. 타입 공간과 값 공간의 심벌 구분하기

- [2021.10.27 TIL - Effective Typescript item 8. 타입 공간과 값 공간의 심벌 구분하기](#20211027-til---effective-typescript-item-8-타입-공간과-값-공간의-심벌-구분하기)
  - [타입과 값의 심벌](#타입과-값의-심벌)
  - [typeof](#typeof)
- [속성 접근자](#속성-접근자)
  - [그 외](#그-외)
  - [참고](#참고)

타입스크립트의 심벌 `symbol`은 타입 공간이나 값 공간 중의 한 곳에 존재한다.
심벌은 이름이 같더라도 속하는 공간 `타입 or 값`에 따라 다른 것을 나타낼 수 있다.

## 타입과 값의 심벌

```tsx
interface Cylinder {
	radius: number;
	height: number;
}

const Cylinder = (radius: number, height: number) => ({radius, height});
```

위 코드에서 타입 Cylinder와 값 Cylinder가 있다. 둘다 Cylinder로 이름은 같지만 전자는 타입, 후자는 값으로 쓰인다. 즉, 서로 아무런 관련이 없다. 이 점 때문에 오류를 만들기도 한다.

```tsx
function calculateVolume(shape: unknown) {
	if (shape instanceof Cylinder) {
		shape.radius // error! '{}' 형식에 radius 속성이 없습니다.
	}
}
```

위 코드에서는 instanceof 연산자를 통해 shape이 Cylinder 타입인지 체크하려고 한다. 하지만 **instanceof는 자바스크립트의 런타임 연산자이고 때문에 값에 대해서 연산**을 하게 된다. 때문에 위 `instanceof Cylinder`는 타입이 아니라 함수를 참조한다.

한 심벌이 타입인지 값인지 언뜻 봐서는 알 수 없기 때문에 어떤 형태로 쓰이는지 문맥을 잘 살펴야한다. 보통 type, interface, 타입 선언 `:`, 단언문 `as` 다음에 나오는 심벌은 타입인 반면 const나 let 다음에 쓰이는 것은 값이다.

> 타입스크립트의 타입 문법이냐 자바스크립트 문법이냐에 따라 심벌의 차이가 나는 것 같다.
> 

클래스와 enum은 상황에 따라 타입과 값 두 가지 모두 사용가능한 예약어이다.

```tsx
class Cylinder {
	radius = 1;
	height = 1;
}

function calculateVolume(shape: unknown) {
	if (shape instanceof Cylinder) {
		shape // 정상, Cylinder 타입
		shape.radius // 정상, number 타입
	}
}
```

클래스가 타입으로 사용될 때는 속성과 메서드가 사용되는 반면, 값으로 쓰일때는 생성자가 사용된다.

> instanceof가 값에 대한 연산으로 Cylinder 클래스에는 radius와 height가 존재하기 때문에 정상 동작하는 것으로 보인다.
> 

## typeof

타입스크립트와 자바스크립트에는 typeof 연산자가 있다.

```tsx
interface Student {
	name: string,
	age: number
}

const s: Student = {
	name: 'pkch',
	age: 29
}

type T1 = typeof s // Student 타입

const v1 = typeof s // 'object'
```

타입의 관점에서 typeof는 값을 읽어서 타입스크립트 타입을 반환한다. 위 예시에서 Student 타입인 s에 typeof 연산을 통해 타입을 반환하면 Student 타입으로 반환한다. 이처럼 타입 공간의 typeof는 큰 타입의 일부분으로 사용할 수 있고, type 구문으로 이름을 붙이는 용도로도 사용이 가능하다.

반면 값 공간에서는 typeof 연산은 대상 심벌의 자바스크립트 런타임 타입을 가리키는 문자열을 반환한다. 때문에 위 예시에서 v1은 `"object"`가 할당된다. 자바스크립트에는 `string`, `number`, `boolean`, `undefined`, `object`, `function` 6가지의 런타임 타입만 존재하므로 해당 값들만 typeof로 할당이 될 수 있다.

클래스는 타입과 값으로 모두 사용되는 예약어이다. 때문에 클래스에 대한 typeof는 상황에 따라 다르게 동작한다.

```tsx
const v = typeof Cylinder // 'function'
type T = typeof Cylinder // typeof Cylinder
```

클래스가 자바스크립트에서는 함수로 구현되므로 `v`에는 `'function'`이 할당된다. 타입 구문에서는 클래스 Cylinder는 인스턴스의 타입이 아니기 때문에 `typeof Cylinder`가 할당된다.

> Cylinder는 new 키워드로 인스턴스를 생성하는 생성자 함수이다.
> 

이를 타입으로 전환하려면 `InstanceType` 제너릭을 사용하여 전환할 수 있다.

```tsx
type T = InstanceType<typeof Cylinder> // Cylinder 타입
```

# 속성 접근자

속성 접근자인 `[]`는 타입으로 사용될때도 동일하게 동작한다. 즉, 값에서 `obj['field']`와 `obj.field`는 동일하지만 타입의 속성을 얻을 때에는 반드시 전자 `obj['field']` 방법을 사용해야한다.

```tsx
const c: Cylinder = {
	radius: 1,
	height: 1
}
const v: Cylinder['height'] = c['height'];
```

타입 선언 뒤에 쓰인 `Cylinder['height']`는 타입이다.  `Cylinder.height`는 동작하지 않는다.

## 그 외

위 언급 된 경우 말고도 값으로 사용되는 this와 타입스크립트의 this가 있고 값과 타입에서 `for in..loop`가 있다.

> 타입에서 in 루프는 union type에 대해서만 동작한다.

참고: [https://learntypescript.dev/08/l2-mapped-type](https://learntypescript.dev/08/l2-mapped-type)
> 

## 참고

이펙티브 타입스크립트 (댄 밴더캄 저, 장원호 역) item 8: [http://www.kyobobook.co.kr/product/detailViewKor.laf?mallGb=KOR&ejkGb=KOR&barcode=9788966263134](http://www.kyobobook.co.kr/product/detailViewKor.laf?mallGb=KOR&ejkGb=KOR&barcode=9788966263134)
# Day27

## 2020.08.27 Thursday - 우아한 Typescript \(1\)

발표자료: [https://slides.com/woongjae/woowahan-ts](https://slides.com/woongjae/woowahan-ts)

## 타입시스템 올바르게 사용하는 법

### 작성자와 사용자

타입시스템은 컴파일러에서 사용하는 타입을 명시적으로 지정하는 시스템과 컴파일러가 자동으로 추론하는 시스템이 있다.

타입은 해당 변수가 할 수 있는 일을 결정한다.

```jsx
function f1(a) {
    // ...
}
```

이때 a의 타입은 a가 할 수 있는 일을 정의한다.

#### 타입스크립트의 추론

```jsx
function f1(a) {
    return a * 2
}
```

위와 같이 a를 명시적으로 지정하지 않는 경우 타입스크립트는 any 타입으로 추론하게 된다. 이 경우는 자바스크립트와 차이가 없다.

> noImplicitAny 옵션을 켜면 타입을 지정하지 않은 경우 타입스크립트 컴파일러가 any로 추론하게 되면 에러를 내뿜는다.

```jsx
function f1(a: number) {
    if (a > 0) {
        return a * 2
    }
}
```

위 경우 a가 음수가 들어오면 리턴타입은 number와 undefined가 될 수 있다. 이때 기본적으로 타입스크립트는 둘다 반환할 수 있도록 만든다.

```jsx
f1(1) * 2 / 4
f1(-1) * 2 // NaN
```

이 경우 undefined가 반환하여 예상치 못한 결과를 받을 수 있다.

> strictNullChecks 옵션을 켜면 모든 타입에 자동으로 포함된 null과 undefined를 제거한다.

**명시적으로 리턴타입을 지정하는 것을 추천!**

> noImplicitReturns 옵션을 키면 함수 내에서 모든 코드가 리턴을 발생시키지 않으면 에러가 발생한다.

* object literal type

  ```jsx
    function f(a: {name: string, age: number}) {
        // ...
    }
  ```

  위와 같이 a의 타입을 지정한 부분이 object literal type이다. 만약 a가 name, age가 아닌 객체가 들어온다면 타입스크립트 컴파일러는 에러를 뿜는다.

### 타입을 정의하는 방법 `interface` vs `type alias`

#### 타입 시스템 동작 방식

* structural type system `구조가 같으면 같은 타입!`

  ```text
    interface IPerson {
        name: string
        age: number
        speak(): string
    }

    type PersonType = {
        name: string
        age: number
        speak(): string
    }
  ```

  위 interface와 type의 구조는 같다.

  ```text
    let personInterface: IPerson = {} as any
    let personType: PersonType = {} as any

    personInterface = personType
    personType = personInterface // not error!
  ```

  따라서 personInterface와 personType은 서로 캐스팅이 가능하다!

* nominal type system `구조가 같아도 이름이 다르면 다른 타입!`

  ```text
    type PersonID = string & { readonly brand: unique symbol }

    function PersonID(id: string): PersonID {
        return id as PersonId
    }

    function getPersonById(id: PersonID) {}

    getPersonById(PersonId('id-pkch'))
    getPersonById('id-pkch')
  ```

#### 타입스크립트의 타입

* function

  ```text
    type EatType = (food: string) => void;

    interface IEat {
        (food: string): void;
    }
  ```

* array

  ```text
    type PersonList = string[];

    interface IPersonList {
        [index: number]: string;
    }
  ```

* union type

  ```text
    interface Bird {
        fly(): void
        layEgg(): void
    }

    interface Fish {
        swim(): void
        layEgg(): void
    }

    type PetType = Bird | Fish // union type

    interface IPet extends PetType {}
     // error! An interface can only extend an object type or intersection of object types with statically known members.ts(2312)

    class Pet implements PetType {}
    // error! A class can only implement an object type or intersection of object types with statically known members.ts(2422)
  ```

  union type은 상속/구현에 활용할 수 없다.

  intersection type 참고: [https://levelup.gitconnected.com/typescript-advanced-types-union-and-intersection-types-9283046d7859](https://levelup.gitconnected.com/typescript-advanced-types-union-and-intersection-types-9283046d7859)

* declaring merging

  ```text
    interface MergingInterface {
        a: string
    }

    interface MergingInterface {
        b: string
    }

    let mergeInterface = MergeInterface

    // mergeInterface.a, mergeInterface.b
  ```

  타입스크립트에서 인터페이스의 이름이 같은 경우 컴파일러가 자동으로 Merge한다.

  ```text
    type MergingType = {
        a: string
    }

    type MergingType = {
        b: string
    }

    // Duplicate identifier 'MergingType'.ts(2300)
  ```

  단, type alias의 경우는 허용되지 않는다.

  ```text
    class MergeClass {
        a: string
    }

    class MergeClass {
        b: string
    }

    Duplicate identifier 'A'.ts(2300)
  ```

  참고로 class도 허용되지 않는다.

### 서브 타입과 슈퍼 타입

> strictFunctionTypes 옵션을 키면 함수의 매개변수 타입만 같거나 슈퍼타입인 경우가 아니라면 에러를 발생

#### any 대신 unknown

any는 자바스크립트와 다를바 없게 만든다. 따라서 any 타입의 변수에 대해 예측할 수 없는 런타임 에러를 야기할 수 있다.

이를 방지하기 위해 unknown을 추천!

```text
function f(a: unknown): string | number {
    a.toString()

    return "hello world!"
}
```

위 타입스크립트 코드를 컴파일하면 다음 오류를 내뿜는다.

```text
error TS2571: Object is of type 'unknown'.

2 a.toString()
```

any의 경우는 위와 같은 에러를 뿜지 않는다. 때문에 추후 런타임에서 예측하지 못한 에러를 야기할 수 있다.

### Type Guard

* typeof `보통 primitive 타입에 사용한다.`
* instanceof `객체의 타입을 판단할 때 사용`

  보통 Error 객체구분에 주로 사용된다.

* in operation `object의 프로퍼티 유무 판단`

  ```text
    interface Person {
        name: string
        age: number
    }

    interface Ceo {
        name: string
        age: number
        company: string
    }

    function isCeo(a: Person | Ceo): boolean {
        if ('company' in a) {
            return true
        }
        return false
    }
  ```

* literal type `object의 프로퍼티는 같지만 타입이 다른 경우`

  ```text
    interface IMachine {
      type: string;
    }

    class Car implements IMachine {
      type: 'CAR';
      wheel: number;
    }

    class Boat implements IMachine {
      type: 'BOAT';
      motor: number;
    }

    function getWhellOrMotor(machine: Car | Boat): number {
      if (machine.type === 'CAR') {
        return machine.wheel;
      } else {
        return machine.motor;
      }
    }
  ```

* is

  컴파일에서 타입검사를 정의할 수 있도록 도와주는 타입 가드

  ```text
    interface IMachine {
        type: string;
      }

    class Car implements IMachine {
      type: 'CAR';
      wheel: number;
    }

    class Boat implements IMachine {
      type: 'BOAT';
      motor: number;
    }

    function getWhellOrMotor(machine: any): number {
      if (isCar(machine)) {
        return machine.wheel;
      } else if (isBoat(machine)) {
        return machine.motor;
      } else {
        return -1;
      }
    }

    function isCar(arg: any): arg is Car {
        return arg.type === 'CAR';
    }

    function isCar(arg: any): arg is Car {
        return arg.type === 'CAR';
    }

    function isBoat(arg: any): arg is Boat {
        return arg.type === 'BOAT';
    }
  ```

  이를 통해 컴파일러는 arg가 `isCar`에서는 Car의 인스턴스라는 것과 `isBoat`에서는 Boat의 인스턴스라는 것을 알 수 있다.

  참고: [https://hyunseob.github.io/2017/12/12/typescript-type-inteference-and-type-assertion/](https://hyunseob.github.io/2017/12/12/typescript-type-inteference-and-type-assertion/)

### Class 안전하게 만들기

> strictPropertyInitiialization 옵션으로 Class의 property가 생성자나 선언부에서 값이 지정되지 않으면 컴파일 에러를 야기한다.

참고로 typescript v4부터는 클래스 프로퍼티의 타입은 생성자에서 추론된다. 단, 생성자를 벗어나 다른 메서드에서 프로퍼티 값을 할당하면 추론이 불가능하다.

## 실전 타입스크립트 작성하기

### Conditional Type 활용하기

> 제네릭은 타입 제약사항!

```text
interface StringContainer {
    value: string
    format(): string
    split(): string[]
}

interface NumberContainer {
    value: string
    nearestPrime: number
    round(): number
}

type Item1<T> = {
    id: T,
    container: any
}

const item1: Item1<string> = {
    id: 'aaaa',
    container: null
}
```

위 경우 item1에서 T의 값이 string인 경우 StringContainer, 아닌 경우 NumberContainer로 주고 싶을 수 있다.

이런 경우 다음과 같이 사용한다.

```text
type Item<T> = {
    id: T extends string | number ? T : never;
    container: T extends string
        ? StringContainer
        : T extends NumberContainer
        ? NumberContainer
        : never;
}
```

이런식으로 삼항연산자를 사용하여 조건에 맞게 지정가능하다.

#### ArrayFilter

```text
type ArrayFilter<T> = T extends any[] ? T : never;
```

위 표현은 T가 배열이라면 그대로 타입을 가져오고 아닌 경우 사용할 수 없도록 만든다.

```text
type StringsOrNumbers = ArrayFilter<string | number | string[] | number[]>

// string[] | number[]
```

위 `StringsOrNumbers`는 `string[] | number[]`가 된다.

#### Flatten

```text
type Flatten<T> = T extends any[]
    ? T[number]
    : T extends object
    ? T[keyof T]
    : T;

const numbers = [1, 2, 3]
type NumbersArrayFlattened = Flatten<typeof numbers>; // number
```

> keyof는 해당 객체의 property key를 의미한다.
>
> T\[number\]는 \[1, 2, 3\]인 경우 배열의 타입이 number라는 것이므로 number가 된다.

#### infer

```text
type UnpackPromise<T> = T extends Promise<infer K>[] ? K : any;
const promises = [Promise.resolve('Mark'), Promise.resolve(38)]

type Expected = UnpackPromise<typeof promises>; // string | number
```

infer는 추론한다는 의미. 즉, 도출되는 값의 타입을 추론한다는 의미

참고로 infer는 삼항연산자의 형태로 써야한다. 추론이 된 경우, 추론이 안된 경우로 나눠서 타입을 지정해야한다.

```text
type A<T> = T extends Promise<infer K> ? K : any

const expectedString = Promise.resolve('hello world!')

type Expected = A<typeof expectedString> // string
```

#### 내장 conditional types

* Exclude

  ```text
    type Exclude<T, U> = T extends U ? never : T

    type Excluded = Exclude<string | number, string> // number
  ```

* Extract

  ```text
    type Extract<T, U> = T extends U ? T : never

    type Extracted = Extract<number, number | string> // number
  ```

* Pick

  ```text
    Pick<T, Exclude<keyof T, K>
  ```

* Omit

  Pick을 활용하여 Omit을 구현하였다.

  ```text
    type Omit<T, K extends keyof any> = Pick<T, Exclude<keyof T, K>>
  ```

* NonNullable

  ```text
    type NonNullable<T> = T extends null | undefined ? never : T
  ```

* ReturnType

  ```text
    type ReturnType<T extends (...args: any) => any> = T extends (
        ...args: any
    ) => infer R
    ? R
    : any
  ```

  함수를 주면 리턴타입의 타입을 추론하여 타입으로 가져오는 type

* Parameter

  ```text
    type Parameters<T extends (...args: any) => any> = T extends (
        ...args: infer P
    ) => any
    ? P
    : never
  ```

  ReturnType과 마찬가지로 함수를 주되 파라미터의 타입을 추론

* ConstructorParameters

  ```text
    type ConstructorParameters<T extends new (...args: any) => any> =
    T extends new (...args: infer P) => any ? P : never
  ```

  Parameter와 크게 다르지 않다. 다만 함수가 아닌 생성자를 사용한다.

* 참고: Function인 프로퍼티 찾기

  ```text
    type FunctionPropertyNames<T> = {
      [K in keyof T]: T[K] extends Function ? K : never;
    }[keyof T];
    type FunctionProperties<T> = Pick<T, FunctionPropertyNames<T>>;

    type NonFunctionPropertyNames<T> = {
      [K in keyof T]: T[K] extends Function ? never : K;
    }[keyof T];
    type NonFunctionProperties<T> = Pick<T, NonFunctionPropertyNames<T>>;

    interface Person {
      id: number;
      name: string;
      hello(message: string): void;
    }

    type T1 = FunctionPropertyNames<Person>;
    type T2 = NonFunctionPropertyNames<Person>;
    type T3 = FunctionProperties<Person>;
    type T4 = NonFunctionProperties<Person>;
  ```


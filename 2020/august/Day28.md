# 2020.08.28 Friday - 우아한 typescript (2)

## overloading

로직 구현은 함수 하나에서 총괄하되 오버로딩은 시그니처 선언만 담당한다.

```tsx
function suffle(value: string): string;
function suffle<T>(value: T[]): T[];
function suffle(value: string | any[]): string | any[] {
		if (typeof value === 'string') {
				// ...
		}

		// ...
}
```

단, 제약사항은 오버로딩한 시그니처 함수를 구현 함수가 커버할 수 있어야한다.

### class method overloading

```tsx
class ExportLibraryModal {
  public openComponentsToLibrary(
    libraryId: string,
    componentIds: string[],
  ): void;
  public openComponentsToLibrary(componentIds: string[]): void;
  public openComponentsToLibrary(
    libraryIdOrComponentIds: string | string[],
    componentIds?: string[],
  ): void {
    if (typeof libraryIdOrComponentIds === 'string') {
      if (componentIds !== undefined) { // 이건 좀 별루지만,
        // 첫번째 시그니처
        libraryIdOrComponentIds;
        componentIds;
      }
    }

    if (componentIds === undefined) { // 이건 좀 별루지만,
      // 두번째 시그니처
      libraryIdOrComponentIds;
    }
  }
}
```

위와 같이 작성할 경우 `componentIds`가 `?`로 undefined가 들어올 수 있게 된다. 따라서 위와 같이 undefined를 체크해야한다.

```tsx
const modal = new ExportLibraryModal();

modal.openComponentsToLibrary(
  'library-id',
  ['component-id-1', 'component-id-1'],
);

modal.openComponentsToLibrary(['component-id-1', 'component-id-1']);
```

그리고 위 시그니처를 이용하여 함수를 호출할 때 앞서 오버로딩을 활용하여 선언한 시그너처가 적용된다.
즉, `public openComponentsToLibrary(libraryId: string, componentIds: string[])`와 `public openComponentsToLibrary(componentIds: string[]): void`를 사용할 수 있도록 컴파일러가 인식하는 것이다.

## readonly와 as const 적극 활용!

as const는 readonly로 순번까지 타입을 체크하도록 도와준다.

`as const`는 상수로써 해당 값을 리터럴 타입으로 변환하도록 만드는 반면 `readonly`는 타입을 유지하여 해당 값을 읽기 전용으로 만들어주는 기능을 한다.

### Mapped Types

```tsx
// Make all properties in T optional
type Partial<T> = {
    [P in keyof T]?: T[P];
};

// Make all properties in T required
type Required<T> = {
    [P in keyof T]-?: T[P];
};

// Make all properties in T readonly
type Readonly<T> = {
    readonly [P in keyof T]: T[P];
};

// From T, pick a set of properties whose keys are in the union K
type Pick<T, K extends keyof T> = {
    [P in K]: T[P];
};

// Construct a type with a set of properties K of type T
type Record<K extends keyof any, T> = {
    [P in K]: T;
};
```

### Readonly<T>

```tsx
interface Book {
  title: string;
  author: string;
}

interface IRootState {
  book: {
    books: Book[];
    loading: boolean;
    error: Error | null;
  };
}

type IReadonlyRootState = Readonly<IRootState>;
let state1: IReadonlyRootState = {} as IReadonlyRootState;
const book1 = state1.book.books[0];
book1.title = 'new';
```

### DeepReadonly<T>

```tsx
type DeepReadonly<T> = T extends (infer E)[]
  ? ReadonlyArray<DeepReadonlyObject<E>>
  : T extends object
  ? DeepReadonlyObject<T>
  : T;

type DeepReadonlyObject<T> = { readonly [K in keyof T]: DeepReadonly<T[K]> };

type IDeepReadonlyRootState = DeepReadonly<IRootState>;
let state2: IDeepReadonlyRootState = {} as IDeepReadonlyRootState;
const book2 = state2.book.books[0];
book2.title = 'new'; // error! Cannot assign to 'title' because it is a read-only property.
```

## Optional보다 Union Type을 애용할 것.

optional은 예기치 못한 null, undefined를 야기할 수 있다. 반면 Union Type은 명시적으로 제한시켜 처리해야한다. `type guard`, `in operator`

## never

마지막 else에 never를 활용할 것. 만약 if/switch 분기문 중에 return이 누락된 경우 undefined 같은 예상치 못한 값을 추론의 결과로 사용한다. 때문에 마지막에 never를 리턴하도록 하면 undefined를 추론 타입에서 제외할 수 있다.
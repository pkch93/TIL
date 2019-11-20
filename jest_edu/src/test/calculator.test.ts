import * as Calculator from '../main/calculator'

test("sum 1 + 1 = 2", () => {
    expect(Calculator.sum(1, 1)).toBe(2)
})

describe("this is plus and minus", () => {
    let x = 10
    let y = 20

    test("10 + 20 = 30", () => {
        expect(Calculator.sum(x, y)).toEqual(30)
    })

    test("20 - 10 = 10", () => {
        expect(Calculator.substract(y, x)).toEqual(10)
    })
})
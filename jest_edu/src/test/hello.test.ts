import hello from '../main/hello'

test("hello world!", () => {
    expect(hello).toBe("hello world!")
})

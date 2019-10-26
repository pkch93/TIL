package patterns.proxy;

public class QueueLogProxy<T> implements Queue<T> {

    private final Queue<T> real;

    public QueueLogProxy(Queue<T> real) {
        this.real = real;
    }

    @Override
    public void push(T value) {
        System.out.println("push: " + value);

        real.push(value);

        System.out.println("push complete");
    }

    @Override
    public T pop() {
        T popValue = real.pop();

        System.out.println("pop: " + popValue);
        System.out.println("pop complete");

        return popValue;
    }
}

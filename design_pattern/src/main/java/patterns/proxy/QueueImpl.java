package patterns.proxy;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class QueueImpl<T> implements Queue<T> {

    private final List<T> queue;

    public QueueImpl() {
        this.queue = new LinkedList<>();
    }

    public QueueImpl(T... values) {
        this.queue = new LinkedList<T>(Arrays.asList(values));
    }


    @Override
    public void push(T value) {
        queue.add(value);
    }

    @Override
    public T pop() {
        return queue.remove(queue.size() - 1);
    }
}

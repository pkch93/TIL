package patterns.proxy;

public class Main {

    public static void main(String[] args) {
        QueueImpl<Integer> queue = new QueueImpl<>(1, 2, 3);
        Queue<Integer> queueLogProxy = new QueueLogProxy<>(queue);

        queueLogProxy.pop();
        queue.pop();
        queueLogProxy.pop();
    }
}

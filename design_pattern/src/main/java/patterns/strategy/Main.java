package patterns.strategy;

public class Main {
    public static void main(String[] args) {
        Calculator cal = new Calculator(item -> (int) (item.getPrice() * 0.9));
    }
}

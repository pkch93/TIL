package patterns.template_method;

import java.util.Scanner;

public class CalculatorCallbackTemplate {

    private static Scanner scanner = new Scanner(System.in);
    private final Operation<Double, Double> operation;

    public CalculatorCallbackTemplate(Operation<Double, Double> operation) {
        this.operation = operation;
    }

    public double calculate() {
        System.out.println("---- 계산기 ----");
        System.out.print("첫 번째 값: ");
        double x = scanner.nextDouble();
        System.out.print("두 번째 값: ");
        double y = scanner.nextDouble();

        return operation.operate(x, y);
    }
}

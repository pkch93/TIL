package patterns.template_method;

import java.util.Scanner;

public abstract class CalculatorTemplate {

    private static Scanner scanner = new Scanner(System.in);

    public double calculate() {
        System.out.println("---- 계산기 ----");
        System.out.print("첫 번째 값: ");
        double x = scanner.nextDouble();
        System.out.print("두 번째 값: ");
        double y = scanner.nextDouble();

        return operate(x, y);
    }

    protected abstract double operate(double x, double y);
}

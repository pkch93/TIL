package patterns.template_method.legacy;

import java.util.Scanner;

public class Calculator {

    private static Scanner scanner = new Scanner(System.in);

    public double plus() {
        System.out.println("---- 계산기 ----");
        System.out.print("첫 번째 값: ");
        double x = scanner.nextDouble();
        System.out.print("두 번째 값: ");
        double y = scanner.nextDouble();

        return x + y;
    }

    public double minus() {
        System.out.println("---- 계산기 ----");
        System.out.print("첫 번째 값: ");
        double x = scanner.nextDouble();
        System.out.print("두 번째 값: ");
        double y = scanner.nextDouble();

        return x - y;
    }

    public double multiple() {
        System.out.println("---- 계산기 ----");
        System.out.print("첫 번째 값: ");
        double x = scanner.nextDouble();
        System.out.print("두 번째 값: ");
        double y = scanner.nextDouble();

        return x * y;
    }

    public double divide() {
        System.out.println("---- 계산기 ----");
        System.out.print("첫 번째 값: ");
        double x = scanner.nextDouble();
        System.out.print("두 번째 값: ");
        double y = scanner.nextDouble();

        return x / y;
    }
}

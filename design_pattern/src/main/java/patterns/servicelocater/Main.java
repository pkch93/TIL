package patterns.servicelocater;

public class Main {

    public static void main(String[] args) {
        Calculator plusCalculator = Integer::sum;
        Printer starPrinter = result -> System.out.println("* " + result + " *");

        ServiceLocator.init(plusCalculator, starPrinter);
        Operator operator = new Operator();

        operator.calculate(1, 2);
    }
}

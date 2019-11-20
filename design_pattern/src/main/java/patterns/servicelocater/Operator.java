package patterns.servicelocater;

public class Operator {

    private Calculator calculator;
    private Printer printer;

    public Operator() {
        this.calculator = ServiceLocator.getInstance().getCalculator();
        this.printer = ServiceLocator.getInstance().getPrinter();
    }

    public void calculate(int x, int y) {
        int result = calculator.calculate(x, y);

        printer.print(result);
    }
}

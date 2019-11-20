package patterns.servicelocater;

public class ServiceLocator {
    private Calculator calculator;
    private Printer printer;

    private ServiceLocator(Calculator calculator, Printer printer) {
        this.calculator = calculator;
        this.printer = printer;
    }

    private static ServiceLocator instance;

    public static void init(Calculator calculator, Printer printer) {
        instance = new ServiceLocator(calculator, printer);
    }

    public static ServiceLocator getInstance() {
        return instance;
    }

    public Calculator getCalculator() {
        return this.calculator;
    }

    public Printer getPrinter() {
        return this.printer;
    }
}

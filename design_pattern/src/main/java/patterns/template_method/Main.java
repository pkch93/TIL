package patterns.template_method;

public class Main {

    public static void main(String[] args) {
        CalculatorTemplate plusCalculator = new CalculatorTemplate() {
            @Override
            protected double operate(double x, double y) {
                return x + y;
            }
        };

        System.out.println(plusCalculator.calculate());

        CalculatorCallbackTemplate plusCallbackCalculator = new CalculatorCallbackTemplate(Double::sum);
        System.out.println(plusCallbackCalculator.calculate());
    }
}

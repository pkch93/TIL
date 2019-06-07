package patterns.strategy;

import patterns.strategy.domain.Item;

import java.util.List;

public class Calculator {
    private DiscountStrategy discountStrategy;

    public Calculator(final DiscountStrategy discountStrategy) {
        this.discountStrategy = discountStrategy;
    }

    public int calculate(List<Item> items) {
        int sum = 0;

        for (Item item : items) {
            sum += discountStrategy.calculate(item);
        }

        return sum;
    }
}

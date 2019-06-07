package patterns.strategy;

import patterns.strategy.domain.Item;

import java.util.List;

public class NonPatternCalculator {

    private static final double FIRST_GUEST_DISCOUNT_RATE = 0.9;
    private static final double NON_FRESH_DISCOUNT_RATE = 0.8;

    public static int calculate(boolean isFirst, List<Item> items) {
        int sum = 0;

        for (Item item : items) {
            if (isFirst) {
                sum += (int) (item.getPrice() * FIRST_GUEST_DISCOUNT_RATE);
            } else if (!item.isFresh()) {
                sum += (int) (item.getPrice() * NON_FRESH_DISCOUNT_RATE);
            } else {
                sum += item.getPrice();
            }
        }

        return sum;
    }
}

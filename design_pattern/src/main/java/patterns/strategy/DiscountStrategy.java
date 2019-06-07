package patterns.strategy;

import patterns.strategy.domain.Item;

public interface DiscountStrategy {
    int calculate(Item item);
}

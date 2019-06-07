package patterns.strategy.domain;

public class Item {

    private final int price;
    private final String name;
    private final boolean fresh;

    public Item(final int price, final String name, final boolean fresh) {
        this.price = price;
        this.name = name;
        this.fresh = fresh;
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public boolean isFresh() {
        return fresh;
    }
}

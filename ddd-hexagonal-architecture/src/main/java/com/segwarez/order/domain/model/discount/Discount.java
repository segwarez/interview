package com.segwarez.order.domain.model.discount;

import com.segwarez.order.domain.model.discount.vo.DiscountCode;
import com.segwarez.order.domain.model.discount.vo.DiscountPercentage;
import com.segwarez.order.domain.model.pricing.vo.Price;

import java.util.Objects;

public class Discount {
    private static final Discount NONE = new Discount(
            DiscountCode.of("00000000"),
            DiscountPercentage.of(0)
    );

    private final DiscountCode code;
    private final DiscountPercentage percentage;

    private Discount(DiscountCode code, DiscountPercentage percentage) {
        this.code = Objects.requireNonNull(code, "Discount code cannot be null");
        this.percentage = Objects.requireNonNull(percentage, "Discount percentage cannot be null");
    }

    public static Discount of(DiscountCode code, DiscountPercentage percentage) {
        return new Discount(code, percentage);
    }

    public static Discount none() {
        return NONE;
    }

    public boolean isNone() {
        return percentage.value() == 0;
    }

    public Price calculateFor(Price price) {
        Objects.requireNonNull(price, "Price cannot be null");
        return price.percentage(percentage.value());
    }

    public DiscountCode code() {
        return code;
    }

    public DiscountPercentage percentage() {
        return percentage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Discount other)) return false;
        return code.equals(other.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return "Discount{" +
                "code=" + code +
                ", percentage=" + percentage +
                '}';
    }
}
package com.github.geekarist.henripotier;

import java.util.List;

public class Offer {
    final Type type;
    final int value;
    final int sliceValue;

    public Offer(Type type, int value) {
        this.value = value;
        this.type = type;
        this.sliceValue = 0;
    }

    public Offer(String type, int value) {
        this(Type.valueOf(type), value);
    }

    public Offer(Type type, int sliceValue, int value) {
        this.type = type;
        this.sliceValue = sliceValue;
        this.value = value;
    }

    public int apply(List<Book> books) {
        if (type == Type.minus) {
            return value;
        } else if (type == Type.percentage) {
            int sum = 0;
            for (Book b : books) {
                sum += b.price;
            }
            return sum * value / 100;
        }
        return 0;
    }

    public enum Type {minus, percentage, slice}
}

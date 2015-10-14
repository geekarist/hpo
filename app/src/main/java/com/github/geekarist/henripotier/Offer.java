package com.github.geekarist.henripotier;

import java.util.List;

public class Offer {
    final Type type;
    final double value;
    final int sliceValue;

    public Offer(Type type, double value) {
        this.value = value;
        this.type = type;
        this.sliceValue = 0;
    }

    public Offer(String type, double value) {
        this(Type.valueOf(type), value);
    }

    public Offer(Type type, int sliceValue, double value) {
        this.type = type;
        this.sliceValue = sliceValue;
        this.value = value;
    }

    public double apply(List<Book> books) {
        if (type == Type.minus) {
            return value;
        } else if (type == Type.percentage) {
            double sum = totalPrice(books);
            return sum * value / 100;
        } else if (type == Type.slice) {
            double sum = totalPrice(books);
            return ((int) (sum / sliceValue)) * value;
        }
        return 0;
    }

    private double totalPrice(List<Book> books) {
        double sum = 0;
        for (Book b : books) {
            sum += b.price;
        }
        return sum;
    }

    public enum Type {minus, percentage, slice}
}

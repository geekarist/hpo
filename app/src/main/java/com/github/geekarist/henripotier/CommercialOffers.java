package com.github.geekarist.henripotier;

import java.util.ArrayList;
import java.util.List;

public class CommercialOffers {
    public List<Offer> offers = new ArrayList<>();

    public static class Offer {
        String type;
        int value;

        public int apply(List<Book> books) {
            return value;
        }
    }
}

package com.polypote.modele;

import lombok.Builder;

import java.util.Date;


public class Supplier extends Agent {

    @Builder
    public Supplier(double priceLimit, double startPrice, Date maxDate, int submissionCounter) {
        super(priceLimit, startPrice, maxDate, submissionCounter);
    }

    @Override
    protected boolean checkOfferLimit(double currentOffer) {
        return currentOffer >= priceLimit;
    }

    @Override
    public int submissionFrequency() {
        return 6;
    }

    @Override
    public double growth(double offer) {
        return Math.round(offer * 1.1 * 100d) / 100d;
    }

    @Override
    public String toString() {
        return "Supplier";
    }
}

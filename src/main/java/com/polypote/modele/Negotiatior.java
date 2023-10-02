package com.polypote.modele;

import lombok.Builder;

import java.util.Date;
import java.util.Map;


public class Negotiatior extends Agent {

    private Map<Integer, Double> companyPreferences;

    @Builder
    public Negotiatior(double priceLimit, double startPrice, Date maxDate, int submissionCounter) {
        super(priceLimit, startPrice, maxDate, submissionCounter);
    }


    @Override
    protected boolean checkOfferLimit(double currentOffer) {
        return currentOffer <= priceLimit;
    }

    @Override
    public int submissionFrequency() {
        return 6;
    }

    @Override
    public double growth(double offer) {
        return Math.round(offer * 0.9 * 100d) / 100d;
    }

    @Override
    public String toString() {
        return "Negotiatior";
    }
}

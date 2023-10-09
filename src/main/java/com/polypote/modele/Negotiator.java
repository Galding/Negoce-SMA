package com.polypote.modele;

import com.polypote.Negotiation;
import lombok.Builder;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;


public class Negotiator extends Agent {

    private Map<Integer, Double> companyPreferences;

    @Builder
    public Negotiator(double priceLimit, double startPrice, Date maxDate, int submissionCounter, Negotiation negotiation, String name) {
        super(priceLimit, negotiation, name, startPrice, maxDate, submissionCounter);
        negotiation.getOffers().put(name, new ArrayList<>());
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

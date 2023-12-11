package com.polypote.modele;

import com.polypote.Negotiation;
import com.polypote.strategy.HistoryStrategy;
import lombok.Builder;

import java.util.ArrayList;
import java.util.Date;


public class Supplier extends Agent {

    @Builder
    public Supplier(double priceLimit, double startPrice, Date maxDate, int submissionCounter, Negotiation negotiation, String name) {
        super(priceLimit, negotiation, name, startPrice, maxDate, submissionCounter);
        negotiation.getOffers().put(this, new ArrayList<>());
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
        return new HistoryStrategy(true, currentNegotiation, priceLimit, getStartPrice(), getMaxDate(), offer).createOffer();
//        return Math.round(offer * 1.1 * 100d) / 100d;
    }

    @Override
    public String toString() {
        return this.agentName;
    }
}

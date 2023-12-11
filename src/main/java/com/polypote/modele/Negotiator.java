package com.polypote.modele;

import com.polypote.Negotiation;
import com.polypote.strategy.SplitPearStrategy;
import lombok.Builder;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;


public class Negotiator extends Agent {

    private Map<Integer, Double> companyPreferences;

    @Builder
    public Negotiator(double priceLimit, double startPrice, Date maxDate, int submissionCounter, Negotiation negotiation, String name) {
        super(priceLimit, negotiation, name, startPrice, maxDate, submissionCounter);
        negotiation.getOffers().put(this, new ArrayList<>());
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
        double percentDifference = Math.abs(offer - getMyLastOffer()) / getMyLastOffer() * 100;
        if (percentDifference < 20) {
            return offer * 0.9;
        } else if (percentDifference < 50) {
            return new SplitPearStrategy(false, currentNegotiation, priceLimit, getStartPrice(), getMaxDate(), offer).createOffer();
        } else if (percentDifference < 100) {
            return offer * 0.3;
        } else {
            return offer * 0.1;
        }
//        return Math.round(offer * 0.9 * 100d) / 100d;
    }

    @Override
    public String toString() {
        return this.agentName;
    }
}

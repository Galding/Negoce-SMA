package com.polypote.modele;

import com.polypote.Negotiation;
import com.polypote.strategy.HistoryStrategy;
import com.polypote.strategy.SplitPearStrategy;
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
        double percentDifference = Math.abs(offer-getMyLastOffer())/getMyLastOffer()*100;
        double newOffer = offer;
        if (percentDifference < 20){
            return offer*1.1;
        }
        else if (percentDifference < 50){
            return new SplitPearStrategy(false, currentNegotiation, priceLimit, getStartPrice(), getMaxDate(), offer).createOffer();
        }
        else if (percentDifference < 100){
            return offer*1.7;
        }
        else{
            return offer*1.9;
        }
    }

    @Override
    public String toString() {
        return this.agentName;
    }
}

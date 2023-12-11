package com.polypote.strategy;

import com.polypote.Negotiation;
import lombok.AllArgsConstructor;

import java.util.Date;

@AllArgsConstructor
public abstract class Strategy {
    protected boolean isSupplier;
    protected Negotiation negotiation;
    protected double priceLimit;
    protected double startPrice;
    protected Date maxDate;
    protected double lastOffer;

    public abstract double createOffer();
}

package com.polypote.strategy;

import com.polypote.Negotiation;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

import static com.polypote.Utils.addAPercentageToThePrice;

public class DateStrategy extends Strategy {
    public DateStrategy(boolean isSupplier, Negotiation negotiation, double priceLimit, double startPrice, Date maxDate, double lastOffer) {
        super(isSupplier, negotiation, priceLimit, startPrice, maxDate, lastOffer);
    }

    @Override
    public double createOffer() {
        Date now = Date.from(Instant.from(LocalDate.now()));
        double res;
        if (now.before(maxDate)) {
            //phase 1
            res = isSupplier ? addAPercentageToThePrice(lastOffer, 1.25) : addAPercentageToThePrice(lastOffer, 0.75);
        } else {
            //phase 2
            res = isSupplier ? addAPercentageToThePrice(lastOffer, 1.1) : addAPercentageToThePrice(lastOffer, 0.9);
        }
        return res;
    }

}

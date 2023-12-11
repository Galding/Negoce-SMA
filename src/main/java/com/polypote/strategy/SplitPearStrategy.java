package com.polypote.strategy;

import com.polypote.Negotiation;
import com.polypote.modele.Negotiator;
import com.polypote.modele.Supplier;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SplitPearStrategy extends Strategy {

    public SplitPearStrategy(boolean isSupplier, Negotiation negotiation, double priceLimit, double startPrice, Date maxDate, double lastOffer) {
        super(isSupplier, negotiation, priceLimit, startPrice, maxDate, lastOffer);
    }

    public double createOffer(){
        Map<Boolean, List<List<Double>>> splitedOffers = negotiation.getOffers()
                .entrySet()
                .stream()
                .collect(
                        Collectors.partitioningBy(entry -> entry.getKey().getClass().equals(Supplier.class),
                                Collectors.mapping(Map.Entry::getValue, Collectors.toList())));
        List<Double> suppliersOffers = splitedOffers.get(true).stream().flatMap(Collection::stream).collect(Collectors.toList());
        List<Double> negotiatorsOffers = splitedOffers.get(false).stream().flatMap(Collection::stream).collect(Collectors.toList());
        if (isSupplier)
            return (lastOffer - negotiatorsOffers.get(negotiatorsOffers.size()-1))/2;
        return (suppliersOffers.get(suppliersOffers.size()-1) - lastOffer)/2;
    }
}

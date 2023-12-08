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


public class HistoryStrategy extends Strategy {
    public HistoryStrategy(boolean isSupplier, Negotiation negotiation, double priceLimit, double startPrice, Date maxDate, double lastOffer, int agentQuantity) {
        super(isSupplier, negotiation, priceLimit, startPrice, maxDate, lastOffer, agentQuantity);
    }

    @Override
    public double createOffer() {
        var agentType = isSupplier ? Supplier.class : Negotiator.class;
        Map<Boolean, List<List<Double>>> splitedOffers = negotiation.getOffers()
                .entrySet()
                .stream()
                .collect(
                        Collectors.partitioningBy(entry -> entry.getKey().getClass().equals(agentType),
                                Collectors.mapping(Map.Entry::getValue, Collectors.toList())));
        Stream<Double> suppliersOffers = splitedOffers.get(true).stream().flatMap(Collection::stream);
        Stream<Double> negotiatorsOffers = splitedOffers.get(false).stream().flatMap(Collection::stream);
        double supplierMinVal = suppliersOffers.min(Double::compareTo).get();
        double negotiationMinVal = negotiatorsOffers.min(Double::compareTo).get();
        return isSupplier ? (supplierMinVal * 0.8 + negotiationMinVal * 0.2) : (supplierMinVal + negotiationMinVal) / 2;
    }
}

package com.polypote;

import com.polypote.modele.Agent;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Value
@EqualsAndHashCode
public class Negotiation {
    Timestamp startDate = new Timestamp(System.currentTimeMillis());
    Map<Agent, List<Double>> offers = new HashMap<>();

    public void addOfferToNegotiationHistory(double nextOffer, Agent agent) {
        offers.entrySet().stream().filter(entry -> entry.getKey().equals(agent)).forEach(entry -> entry.getValue().add(nextOffer));
    }

    @Override
    public String toString() {
        return "Negotiation{" +
                "startDate=" + startDate +
                ", offers=" + offers +
                '}';
    }
}

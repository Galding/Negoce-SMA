package com.polypote;

import com.polypote.modele.Agent;
import lombok.Value;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Value
public class Negotiation {
    Timestamp startDate = new Timestamp(System.currentTimeMillis());
    Map<String, List<Double>> offers = new HashMap<>();

    public void addOfferToNegociationHistory(double nextOffer, Agent agent) {
        getOffers().get(agent.getAgentName()).add(nextOffer);
    }

    @Override
    public String toString() {
        return "Negotiation{" +
                "startDate=" + startDate +
                ", offers=" + offers +
                '}';
    }
}

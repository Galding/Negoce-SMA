package com.polypote.modele;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class AgentNegociateur extends Agent {
    private double budget;
    private double startPrice;
    private Map<Integer, Double> companyPreferences;


    @Override
    public int submissionFrequency() {
        return 6;
    }

    @Override
    public double growth() {
        return 0.0;
    }
}

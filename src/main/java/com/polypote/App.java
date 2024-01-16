package com.polypote;

import com.polypote.modele.Message;
import com.polypote.modele.Negotiator;
import com.polypote.modele.Supplier;

import static com.polypote.modele.MessageType.NEGOTIATING;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws InterruptedException {
        var negotiation = new Negotiation();
        var negotiator = Negotiator.builder()
                .startPrice(50)
                .priceLimit(54)
                .negotiation(negotiation)
                .name("Negotiator")
                .build();
        var negotiator2 = Negotiator.builder()
                .startPrice(50)
                .priceLimit(52)
                .negotiation(negotiation)
                .name("Negotiator2")
                .build();
        var supplier = Supplier.builder()
                .startPrice(60)
                .priceLimit(45)
                .negotiation(negotiation)
                .name("Supplier")
                .build();
        negotiation.addOfferToNegotiationHistory(supplier.getStartPrice(), supplier);
       /* negotiation.addOfferToNegotiationHistory(negotiator.getStartPrice(), negotiator);
        negotiation.addOfferToNegotiationHistory(negotiator2.getStartPrice(), negotiator2);*/
        Message.builder()
                .sender(supplier)
                .receiver(negotiator)
                .messageType(NEGOTIATING)
                .offer(supplier.getStartPrice())
                .build()
                .send();
        negotiator.start();
        Thread.sleep(1000);
        supplier.start();
        Thread.sleep(1000);
        Message.builder()
                .sender(supplier)
                .receiver(negotiator2)
                .messageType(NEGOTIATING)
                .offer(supplier.getStartPrice())
                .build()
                .send();
        negotiator2.start();

        negotiator.join();
        negotiator2.join();
        supplier.join();
        System.out.println("Negotiation history " + negotiation);
    }
}

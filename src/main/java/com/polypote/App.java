package com.polypote;

import com.polypote.modele.Message;
import com.polypote.modele.Negotiatior;
import com.polypote.modele.Supplier;

import static com.polypote.modele.MessageType.NEGOTIATING;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws InterruptedException {
        var negotiator = Negotiatior.builder()
                .startPrice(200)
                .priceLimit(580)
                .build();
        var supplier = Supplier.builder()
                .startPrice(600)
                .priceLimit(300)
                .build();
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
    }
}

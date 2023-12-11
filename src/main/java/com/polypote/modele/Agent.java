package com.polypote.modele;

import com.polypote.Negotiation;
import com.polypote.strategy.SplitPearStrategy;
import com.polypote.strategy.Strategy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import static com.polypote.modele.MessageType.*;

@EqualsAndHashCode(callSuper = false, exclude = "currentNegotiation")
@Data
@AllArgsConstructor
public abstract class Agent extends Thread {
    private final BlockingDeque<Message> messages = new LinkedBlockingDeque<>();
    private final ReentrantLock lock = new ReentrantLock();
    private final AtomicBoolean isFirstOffer = new AtomicBoolean(true);
    private final double penalty = 1.1;
    protected double priceLimit;
    @ToString.Exclude
    protected Negotiation currentNegotiation;
    protected String agentName;
    private double startPrice;
    private Date maxDate;
    private int submissionCounter;

    public void addMessage(Message message) {
        this.messages.add(message);
    }

    public boolean processMessage() {
        while (messages.peek() == null) ;
        Message currentMessage = messages.poll();
        switch (currentMessage.getMessageType()) {
            case NEGOTIATING:
                return processOffer(currentMessage);
            case ACCEPTED:
                System.out.println("Offer accepted with price " + currentMessage.getOffer());
                break;
            case REJECTED:
                System.out.println("Offer rejected");
                break;
        }
        return false;
    }

    @Override
    public void run() {
        while (processMessage()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean processOffer(Message currentMessage) {
        double currentOffer = currentMessage.getOffer();
        if (isFirstOffer.get() && this instanceof Negotiator) {
            System.out.printf("[FIRST OFFER] %s : Got offer from %s with offer %s sent to %s with offer %s \n", currentMessage.getReceiver(), currentMessage.getSender(), currentOffer, currentMessage.getSender(), startPrice);
            isFirstOffer.set(false);
            Message.builder()
                    .offer(startPrice)
                    .messageType(NEGOTIATING)
                    .sender(this)
                    .receiver(currentMessage.getSender())
                    .build()
                    .send();
            currentNegotiation.addOfferToNegotiationHistory(startPrice, this);
            return true;
        }
        double nextOffer = growth(currentOffer);
        if (submissionCounter <= submissionFrequency() && checkOfferLimit(nextOffer)) {
            System.out.printf("%s : Got offer from %s with offer %s sent to %s with offer %s \n", currentMessage.getReceiver(), currentMessage.getSender(), currentOffer, currentMessage.getSender(), nextOffer);
            Message.builder()
                    .offer(nextOffer)
                    .receiver(currentMessage.getSender())
                    .sender(this)
                    .messageType(NEGOTIATING)
                    .build()
                    .send();
            currentNegotiation.addOfferToNegotiationHistory(nextOffer, this);
            submissionCounter++;
            return true;
        } else if (checkOfferLimit(currentOffer)) {
            acceptOffer(currentMessage, currentOffer);
            this.currentNegotiation.getOffers().keySet().stream().filter(agent -> !agentName.equals(agent.getAgentName())).forEach(agent -> {
                Message.builder().receiver(agent).sender(this).messageType(REJECTED).build().send();
            });

        } else {
            rejectOffer(currentMessage);
        }
        return false;
    }

    private void acceptOffer(Message currentMessage, double currentOffer) {
        Message.builder()
                .offer(currentOffer)
                .receiver(currentMessage.getSender())
                .sender(this)
                .messageType(ACCEPTED)
                .build()
                .send();
    }

    private void rejectOffer(Message currentMessage) {
        if (this instanceof Negotiator && Date.from(Instant.from(LocalDate.now())).after(maxDate)) {
            System.out.printf("%s doit payer %s à %s", this, currentMessage.getOffer() * penalty, currentMessage.getSender());
        }
        Message.builder()
                .receiver(currentMessage.getSender())
                .sender(this)
                .messageType(REJECTED)
                .build()
                .send();
    }

    protected double getMyLastOffer(){
        return 0.0;
    }

    protected abstract boolean checkOfferLimit(double currentOffer);

    // max submission
    public abstract int submissionFrequency();

    //next offer
    public abstract double growth(double offer);
}

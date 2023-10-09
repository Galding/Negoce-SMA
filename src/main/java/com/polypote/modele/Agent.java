package com.polypote.modele;

import com.polypote.Negotiation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.ReentrantLock;

import static com.polypote.modele.MessageType.*;

@EqualsAndHashCode(callSuper = false, exclude = "currentNegotiation")
@Data
@AllArgsConstructor
public abstract class Agent extends Thread {
    private final BlockingDeque<Message> messages = new LinkedBlockingDeque<>();
    private final ReentrantLock lock = new ReentrantLock();
    protected double priceLimit;
    @ToString.Exclude
    protected Negotiation currentNegotiation;
    protected String agentName;
    private double startPrice;
    private Date maxDate;
    private int submissionCounter = 0;

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
        Message.builder()
                .receiver(currentMessage.getSender())
                .sender(this)
                .messageType(REJECTED)
                .build()
                .send();
    }

    protected abstract boolean checkOfferLimit(double currentOffer);

    // max submission
    public abstract int submissionFrequency();

    //next offer
    public abstract double growth(double offer);
}

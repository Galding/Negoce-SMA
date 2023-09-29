package com.polypote.modele;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

@EqualsAndHashCode(callSuper = false)
@Data
public abstract class Agent extends Thread {
    private final Queue<Message> messages = new LinkedList<>();
    private ReentrantLock lock = new ReentrantLock();
    private Date maxDate;

    public void addMessage(Message message) {
        this.messages.add(message);
    }

    // max submission
    public abstract int submissionFrequency();

    //next offer
    public abstract double growth();
}

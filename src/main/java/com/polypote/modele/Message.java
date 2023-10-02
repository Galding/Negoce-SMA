package com.polypote.modele;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class Message {
    private Agent sender;

    private Agent receiver;

    private MessageType messageType;

    private double offer;

    public void send() {
        this.receiver.addMessage(this);
    }
}

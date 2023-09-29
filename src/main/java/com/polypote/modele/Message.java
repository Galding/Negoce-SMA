package com.polypote.modele;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Message {
    private Agent sender;

    private Agent receiver;

    public void send() {
        this.receiver.addMessage(this);
    }
}

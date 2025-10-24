package com.roblez.inventorysystem.alert;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.mail")
public class EmailProperties {
    private  String recipient;

    public  String getRecipient() {
        return recipient;
    }

    public  void setRecipient(String recipient) {
        this.recipient = recipient;
    }
}

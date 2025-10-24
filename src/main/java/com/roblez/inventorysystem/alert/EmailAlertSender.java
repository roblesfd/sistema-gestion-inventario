package com.roblez.inventorysystem.alert;

import org.springframework.context.annotation.Primary;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component("emailAlertSender")
@Primary
public class EmailAlertSender implements AlertSender{
	private final JavaMailSender mailSender;
	

    public EmailAlertSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
	
	
	@Override
	public boolean sendAlert(String recipient, String subject, String message) {
		try {
			SimpleMailMessage mail = new SimpleMailMessage();
			mail.setTo(recipient);
			mail.setSubject(subject);
			mail.setText(message);
			mailSender.send(mail);
			
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}

	}
}

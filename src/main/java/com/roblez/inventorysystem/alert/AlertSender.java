package com.roblez.inventorysystem.alert;

/*
 * Interfaz generica para envio de alertas
 */
public interface AlertSender {
	boolean sendAlert(String recipient, String subject, String message) throws Exception;
}

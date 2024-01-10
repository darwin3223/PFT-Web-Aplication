package com.utils;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.exceptions.MailException;

public class MailUtil {

	public static void mandarMail(String mailDestinatario, String mensaje, String asunto) throws MailException{
		Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com"); // Reemplaza con tu servidor SMTP
        properties.put("mail.smtp.port", "587"); // Puerto SMTP
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true"); // Habilitar TLS si es necesario

        // Credenciales de autenticación
        String usuario = "geddagroup@gmail.com";
        String contraseña = "aaevvhslwdnliwqs";

        // Crea una instancia de Authenticator para autenticarse con el servidor SMTP
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(usuario, contraseña);
            }
        };

        // Crea la sesión de correo electrónico
        Session session = Session.getInstance(properties, authenticator);
        
        try {
            // Crea un mensaje de correo
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(usuario));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailDestinatario));
            message.setSubject(asunto);
            message.setText(mensaje);

            // Envía el mensaje
            Transport.send(message);;
        } catch (MessagingException e) {
        	throw new MailException(e.getMessage());
        	
        }
	}
}

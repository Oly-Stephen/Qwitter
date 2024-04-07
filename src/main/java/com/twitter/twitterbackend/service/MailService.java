package com.twitter.twitterbackend.service;

import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.gmail.Gmail;
import com.sun.mail.iap.ByteArray;
import com.twitter.twitterbackend.exception.EmailFailedToSendException;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.util.Properties;

@Service
public class MailService {
    private final Gmail gmail;

    public MailService(Gmail gmail) {
        this.gmail = gmail;
    }

    public void sendEmail(String toAddress, String subject, String content) throws Exception {
        Properties props = new Properties();

        Session session  = Session.getInstance(props, null);

        MimeMessage email = new MimeMessage(session);

        try {
            email.setFrom(new InternetAddress("stephenoluwayanmi@gmail.com"));
            //email.addRecipients(Message.RecipientType.TO, new InternetAddress(toAddress));
            // TODO Resolve this
            email.setSubject(subject);
            email.setText(content);

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            email.writeTo(buffer);

            byte[] rawMessageBytes = buffer.toByteArray();

            String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);

            com.google.api.services.gmail.model.Message message = new com.google.api.services.gmail.model.Message();
            message.setRaw(encodedEmail);

            message = gmail.users().messages().send("me", message).execute();
        } catch (Exception e) {
            throw new EmailFailedToSendException();
        }
    }

}

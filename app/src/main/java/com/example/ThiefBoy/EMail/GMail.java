package com.example.ThiefBoy.EMail;


import android.util.Log;

import com.example.myapplication.BuildConfig;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class GMail {

    final String emailPort = BuildConfig.SMTP_PORT;// gmail's smtp port
    final String smtpAuth = "true";
    final String starttls = "true";
    final String emailHost = BuildConfig.SMTP_SERVER;

    final String fromEmail = BuildConfig.FROM_EMAIL;
    final String fromPassword = BuildConfig.FROM_EMAIL_PASSWORD;
    final String emailSubject = BuildConfig.EMAIL_SUBJECT;

    String toEmail;
    String emailBody;

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getEmailBody() {
        return emailBody;
    }

    public void setEmailBody(String emailBody) {
        this.emailBody = emailBody;
    }

    Properties emailProperties;
    Session mailSession;
    MimeMessage emailMessage;

//    public GMail() {
//        //SendMailTask
//    }

    public GMail() {


        emailProperties = System.getProperties();
        emailProperties.put("mail.smtp.port", emailPort);
        emailProperties.put("mail.smtp.auth", smtpAuth);
        emailProperties.put("mail.smtp.starttls.enable", starttls);
        Log.i("GMail", "Mail server properties set.");
    }

    public MimeMessage createEmailMessage() throws AddressException,
            MessagingException, UnsupportedEncodingException {


        mailSession = Session.getInstance(emailProperties);
        emailMessage = new MimeMessage(mailSession);
        emailMessage.setFrom(new InternetAddress(fromEmail, fromEmail));


        emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));


        emailMessage.setSubject(emailSubject);
        emailMessage.setContent(emailBody, "text/html");// for a html email
        //emailMessage.setText(emailBody);// for a text email
        Log.i("GMail", "Email Message created.");

        return emailMessage;
    }

    public void sendEmail() throws AddressException, MessagingException {

        Thread thread = new Thread(() -> {
            try {
                Transport transport = mailSession.getTransport("smtp");
                transport.connect(emailHost, fromEmail, fromPassword);
                Log.i("GMail", "allrecipients: " + emailMessage.getAllRecipients());
                transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
                transport.close();
                //Log.i("GMail", "Email sent successfully.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        thread.start();
    }

}
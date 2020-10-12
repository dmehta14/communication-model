package com.ect.communication.model.demo.service;

public interface MessagingService {

     String sendSMS(String toNumber, String textMessage);

     String sendWebApp(String toNumber, String messageText , String communicationType );

     String sendEmail(String toEmail, String subject, String emailBody);

}

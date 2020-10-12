package com.ect.communication.model.demo.service.impl

import com.ect.communication.model.demo.data.AppMessage
import com.ect.communication.model.demo.data.Content
import com.ect.communication.model.demo.data.From
import com.ect.communication.model.demo.data.CommunicationDetails
import com.ect.communication.model.demo.data.To
import com.ect.communication.model.demo.data.WebAppModel
import com.ect.communication.model.demo.service.MessagingService
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.mashape.unirest.http.HttpResponse
import com.mashape.unirest.http.Unirest
import com.nexmo.client.NexmoClient
import com.nexmo.client.NexmoClientException
import com.nexmo.client.sms.SmsSubmissionResponse
import com.nexmo.client.sms.SmsSubmissionResponseMessage
import com.nexmo.client.sms.messages.TextMessage
import com.nexmo.client.voice.Call
import com.nexmo.client.voice.CallEvent
import com.nexmo.client.voice.VoiceName
import com.nexmo.client.voice.ncco.Ncco
import com.nexmo.client.voice.ncco.TalkAction
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

import javax.mail.Message;
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

import static com.ect.communication.model.demo.constants.Constants.MESSAGGE_TYPE_FB
import static com.ect.communication.model.demo.constants.Constants.MESSAGGE_TYPE_WHATSAPP


@Service
class MessagingServiceImpl implements MessagingService {
    private final NexmoClient client

    @Value('${nexmo.client.api.key}')
    String apiKey

    @Value('${nexmo.client.api.secret}')
    String apiSecret

    @Value('${nexmo.client.api.whatsapp}')
    String whatsappNumber

    @Value('${nexmo.client.api.facebook.from}')
    String fromFacebookID

    @Value('${nexmo.client.api.facebook.to}')
    String toFacebookID

    @Value('${smtp.host.server}')
    String smtpHostServer

    @Value('${dummy.email.address}')
    String emailAddress

    @Value('${dummy.email.name}')
    String emailName



    MessagingServiceImpl() {
        client = new NexmoClient.Builder()
                .apiKey("43d5ad6c")
                .apiSecret("7mbawNm2f1jMkWtk")
                .build()
    }

    @Override
    String sendSMS(String toNumber, String textMessage) {

        TextMessage message = new TextMessage("Vonage APIs", toNumber, textMessage)
        try {

            SmsSubmissionResponse response = client.getSmsClient().submitMessage(message)

            for (SmsSubmissionResponseMessage responseMessage : response.getMessages()) {
                System.out.println(responseMessage)
                if (!responseMessage?.errorText?.isEmpty()) {
                    return responseMessage?.errorText
                }

            }
            return "SMS Sent Successfully!!"

        }
        catch (IOException | NexmoClientException ex) {
            return ex.getMessage()
        }


    }

    ResponseEntity voiceCall(CommunicationDetails msg) {
        NexmoClient client = NexmoClient.builder()
                .applicationId("APPLICATION_ID")
                .privateKeyPath("PATH_TO_PRIVATE_KEY")
                .build()

        Ncco ncco = new Ncco(
                TalkAction
                        .builder("This is a text-toNumber-speech test message.")
                        .voiceName(VoiceName.RAVEENA)
                        .build()
        )

        String TO_NUMBER = "919368928170"
        String FROM_NUMBER = "919368928170"

        CallEvent result = client
                .getVoiceClient()
                .createCall(new Call(TO_NUMBER, FROM_NUMBER, ncco));

        System.out.println(result.getConversationUuid())


    }

    @Override
    String sendWebApp(String toNumber, String messageText, String communicationType ) {

        HttpResponse<String> httpResponse = Unirest.post("https://messages-sandbox.nexmo.com/v0.1/messages")
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Basic NDNkNWFkNmM6N21iYXdObTJmMWpNa1d0aw==")
                .header("cache-control", "no-cache")
                .body(convertObjectToString(toNumber, messageText,communicationType))
                .asString()
        System.out.println(httpResponse.statusText +"&&" + httpResponse.body)
        httpResponse.body+httpResponse.statusText

    }

    /**
     * * Utility method to send simple HTML email * @param session * @param
     * toEmail * @param subject * @param body
     */

    String sendEmail(String toEmail, String subject, String body) {

        try {
            Properties props = System.getProperties()
            props.put("mail.smtp.host", smtpHostServer)
            Session session = Session.getInstance(props, null)
            MimeMessage msg = new MimeMessage(session)
            msg.addHeader("Content-type", "text/HTML;//charset=UTF-8")
            msg.addHeader("format", "flowed")
            msg.addHeader("Content-Transfer-Encoding", "8bit")
            msg.setFrom(new InternetAddress(emailAddress, emailName))
            msg.setReplyTo(InternetAddress.parse(emailAddress, false))
            msg.setSubject(subject, "UTF-8")
            msg.setText(body, "UTF-8")
            msg.setSentDate(new Date())
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false))
            Transport.send(msg)
            return "Email Sent Successfully!!"
        } catch (Exception e) {
            return "Email Not Sent "+e.getMessage()
        }

    }


    private String convertObjectToString(String toNumber, String messageText , String communicationType )
    {
        ObjectMapper mapper = new ObjectMapper()
        WebAppModel webAppModel = new WebAppModel()

        From from= formatFromObj(communicationType)
        To to = formatToObj(communicationType,toNumber)
        AppMessage message = new AppMessage()
        Content content = new Content()
        content.setText(messageText)
        content.setType("text")
        message.setContent(content)
        webAppModel.setFrom(from)
        webAppModel.setTo(to)
        webAppModel.setMessage(message)

        try {
            return mapper.writeValueAsString(webAppModel)
        } catch (JsonProcessingException e) {
            e.printStackTrace()
            return "Error in parsing"
        }


    }

    private From formatFromObj(String communicationType)
    {
        From from = null
        if(MESSAGGE_TYPE_WHATSAPP.equalsIgnoreCase(communicationType))
        {
            from = new From()
            from.setType(communicationType)
            from.setNumber(whatsappNumber)
        }
        else if(MESSAGGE_TYPE_FB.equalsIgnoreCase(communicationType))
        {
            from = new From()
            from.setType(communicationType)
            from.setId(fromFacebookID)
        }

        return from
    }

    private To formatToObj(String communicationType, String toNumber)
    {
        To to = null
        if(MESSAGGE_TYPE_WHATSAPP.equalsIgnoreCase(communicationType))
        {
            to = new To()
            to.setType(communicationType)
            to.setNumber(toNumber)
        }
        else if(MESSAGGE_TYPE_FB.equalsIgnoreCase(communicationType))
        {
            to = new To()
            to.setType(communicationType)
            to.setId(toFacebookID)
        }
        to

    }

}

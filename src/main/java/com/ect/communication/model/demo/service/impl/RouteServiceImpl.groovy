package com.ect.communication.model.demo.service.impl

import com.ect.communication.model.demo.data.CommunicationTemplate
import com.ect.communication.model.demo.repository.CommunicationTemplateRepository
import com.ect.communication.model.demo.data.CommunicationDetails
import com.ect.communication.model.demo.service.MessagingService
import com.ect.communication.model.demo.service.RouteService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

import static com.ect.communication.model.demo.constants.Constants.MESSAGGE_TYPE_EMAIL
import static com.ect.communication.model.demo.constants.Constants.MESSAGGE_TYPE_FB
import static com.ect.communication.model.demo.constants.Constants.MESSAGGE_TYPE_SMS
import static com.ect.communication.model.demo.constants.Constants.MESSAGGE_TYPE_WHATSAPP

@Service
class RouteServiceImpl implements RouteService {

    @Autowired
    MessagingService messagingService

    private CommunicationTemplateRepository communicationTemplateRepository

    RouteServiceImpl(CommunicationTemplateRepository communicationTemplateRepository) {
        this.communicationTemplateRepository = communicationTemplateRepository
    }

    @Override
    ResponseEntity<List<String>> routeMessages(CommunicationDetails communicationDetails) {
        if (!communicationDetails || !communicationDetails?.communicationType
                || !communicationDetails?.caseStatus  || !communicationDetails.custName || !communicationDetails.authorizationNumber) {
            return ResponseEntity.ok().body(["Required Fields are empty"])
        }

        callMessagingService(communicationDetails)
    }


    ResponseEntity<List<String>> callMessagingService(CommunicationDetails communicationDetails) {

        List<String> communicationStatus = ['Initialized']
        String getTemplate= null

            communicationDetails?.communicationType?.forEach() { r ->
                getTemplate = getMessageTemplate(communicationDetails, r)

                if (!getTemplate?.isEmpty()) {

                    if (r.equalsIgnoreCase(MESSAGGE_TYPE_EMAIL)) {

                        if (!communicationDetails?.email?.isEmpty()) {

                            communicationStatus.add(messagingService.sendEmail(communicationDetails.email,
                                    communicationDetails.authorizationNumber + "-" + communicationDetails.caseStatus,
                                    getTemplate))
                        } else {
                            communicationStatus.add("Empty Email Address")
                        }

                    } else if (r.equalsIgnoreCase(MESSAGGE_TYPE_FB)) {

                        communicationStatus.add(messagingService.sendWebApp(communicationDetails.toNumber, getTemplate, r))

                    } else if (r.equalsIgnoreCase(MESSAGGE_TYPE_WHATSAPP)) {
                        messagingService.sendWebApp(communicationDetails.toNumber, getTemplate, r)

                    } else if (r.equalsIgnoreCase(MESSAGGE_TYPE_SMS)) {
                        communicationStatus.add(messagingService.sendSMS(communicationDetails.toNumber, getTemplate))
                    }

                }
                else{
                    communicationStatus.add("No Template Configured for Communication Type " + r)

                }
            }

        ResponseEntity.ok().body(communicationStatus)

    }


    CommunicationTemplate callDB(String communicationType, String caseStatus) {
      this.communicationTemplateRepository.findByCommunicationTypeAndCaseStatus(communicationType,
              caseStatus)

}

    private String getMessageTemplate(CommunicationDetails communicationDetails, String communicationType)
    {
        CommunicationTemplate c = callDB(communicationType,communicationDetails.getCaseStatus())
        if(c) {
            c.getTemplateMessage().replace("#CustName", communicationDetails?.getCustName())
                    .replace("#AuthorizationNumber", communicationDetails?.getAuthorizationNumber())
                    .replace("#CaseStatus", communicationDetails?.getCaseStatus())
        }
        else{
            return null
        }
    }
    }
package com.ect.communication.model.demo.repository

import com.ect.communication.model.demo.data.CommunicationTemplate
import org.springframework.data.jpa.repository.JpaRepository

interface CommunicationTemplateRepository extends JpaRepository<CommunicationTemplate, Integer>{

    CommunicationTemplate findByCommunicationTypeAndCaseStatus(String communicationType, String caseStatus)

}
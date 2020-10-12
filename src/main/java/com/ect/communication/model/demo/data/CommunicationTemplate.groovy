package com.ect.communication.model.demo.data

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class CommunicationTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int templateId

    String templateMessage

    String communicationType

    String caseStatus
}

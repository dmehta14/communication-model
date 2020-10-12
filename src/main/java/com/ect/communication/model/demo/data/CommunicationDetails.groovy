package com.ect.communication.model.demo.data

import com.fasterxml.jackson.annotation.JsonProperty

class CommunicationDetails {

    @JsonProperty('toNumber')
    String toNumber

    @JsonProperty('communicationType')
    List<String> communicationType

    @JsonProperty('caseStatus')
    String caseStatus

    @JsonProperty('custName')
    String custName

    @JsonProperty('authorizationNumber')
    String authorizationNumber

    @JsonProperty('ETA')
    String ETA

    @JsonProperty('email')
    String email

}

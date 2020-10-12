package com.ect.communication.model.demo.service

import com.ect.communication.model.demo.data.CommunicationDetails
import org.springframework.http.ResponseEntity

interface RouteService {

   ResponseEntity<List<?>> routeMessages(CommunicationDetails communicationDetails)
}


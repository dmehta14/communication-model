package com.ect.communication.model.demo.controller;

import com.ect.communication.model.demo.data.CommunicationDetails;
import com.ect.communication.model.demo.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/message")
@CrossOrigin
@RestController
public class MessagingController {

    @Autowired
    RouteService routeService;

    @PostMapping(value = "/route-service", produces = "application/json;v=1.0")
    ResponseEntity routeService(@RequestBody CommunicationDetails communicationDetails) {
        System.out.print("Inside sendCommunication");
        return routeService.routeMessages(communicationDetails);

    }

}

package com.example.amazonsqsdemo;

import org.springframework.web.bind.annotation.*;

@RestController
public class SQSResource {

    private final SQSService sqsService;


    public SQSResource(SQSService sqsService) {
        this.sqsService = sqsService;
    }

    @PostMapping("/sendMessage")
    public String sendMessage(@RequestHeader(name = "AuthToken") final String authToken,
                              @RequestBody final String message){
        return sqsService.sendMessage(authToken,message);
    }

    @GetMapping("/test")
    public String get(){
        return "hello";
    }
}
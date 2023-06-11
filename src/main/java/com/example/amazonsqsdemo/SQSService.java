package com.example.amazonsqsdemo;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class SQSService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SQSService.class);

    private final AmazonSQS sqs;

    private final AmazonS3 s3;
    @Value("${cloud.aws.sqs.standard.queue.url}")
    private String queueUrl;

    @Value("${cloud.aws.s3.bucketName}")
    private String awsBucketName;


    public SQSService(AmazonSQS sqs, AmazonS3 s3) {
        this.sqs = sqs;
        this.s3 = s3;
    }

    public String sendMessage(final String authToken, final String message){
        try {
            final Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
            messageAttributes.put("AuthToken",new MessageAttributeValue()
                    .withDataType("String")
                    .withStringValue(authToken));
            final SendMessageRequest sendMessageRequest = new SendMessageRequest();
            sendMessageRequest.withMessageBody(message);
            sendMessageRequest.withQueueUrl(queueUrl);
            sendMessageRequest.withMessageAttributes(messageAttributes);

            SendMessageResult sendMessageResult = sqs.sendMessage(sendMessageRequest);
            LOGGER.info("Message Sent with ID:{}",sendMessageResult.getMessageId());

            SQSMessage sqsMessage = SQSMessage.builder()
                    .messageId(sendMessageResult.getMessageId())
                    .messageAttributes(messageAttributes)
                    .messageContent(message)
                    .build();

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonContent = null;

            try {
                jsonContent = objectMapper.writeValueAsString(sqsMessage);
            } catch (IOException e){
                LOGGER.error("Error Mapping content of message {}",e.getLocalizedMessage());
            }
            byte[] jsonBytes = jsonContent.getBytes();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(jsonBytes);

            //Saving object with Message ID as file name
            s3.putObject(awsBucketName,sendMessageResult.getMessageId()+".json",byteArrayInputStream,null);

            return sendMessageResult.getMessageId();

        } catch (AmazonSQSException e){
            LOGGER.error("SQS Error Occurred with Code: {} message: {}",e.getErrorCode(),e.getErrorMessage());
            return e.getErrorMessage();
        }
    }
}

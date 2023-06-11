package com.example.amazonsqsdemo;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class SQSMessageProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SQSMessageProcessor.class);

    private final AmazonSQS sqs;
    @Value("${cloud.aws.sqs.standard.queue.url}")
    private String queueUrl;

    public SQSMessageProcessor(AmazonSQS sqs) {
        this.sqs = sqs;
    }

    public void processMessage(Message message) {

        try {
            LOGGER.info("Message Processing Started");
            LOGGER.info("Message Received : {}",message);
            //Handle Message Processing Logic Here

            //After Processing the message successfully we can delete it.
            deleteMessage(message.getReceiptHandle());

        } catch (AmazonSQSException e) {
            LOGGER.error(e.getErrorMessage());
        }

    }

    public void deleteMessage(String receiptHandle){
        sqs.deleteMessage(queueUrl,receiptHandle);
        LOGGER.info("Message Deletion Completed");
    }

}

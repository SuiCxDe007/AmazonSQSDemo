package com.example.amazonsqsdemo;

import com.amazonaws.services.sqs.model.MessageAttributeValue;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class SQSMessage {

    private String messageId;
    private Map<String, MessageAttributeValue> messageAttributes;
    private String messageContent;

    @Override
    public String toString(){
        return "Message{"+
                "MessageId='" + messageId +'\'' +
                "MessageAttributes'" + messageAttributes + '\'' +
                ", MessageContent=" +messageContent +
                '}';
    }

}

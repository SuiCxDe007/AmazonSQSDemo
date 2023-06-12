# AmazonSQS Demo

This is a simple Spring boot Project created to demonstrate the AWS SQS Messaging capabilities. Application acts as a message producer & a consumer. 
For this demo I have used `AWS SQS Standard Queue.` 

This application will send the messages to a SQS Standard Queue, Upload the message content as `.json` to a predefined `AWS S3 Bucket` as a means to retain message content for future use.
Application also contains a `GET` API to get the saved messages in the S3.

Application has enabled retry capability in case of message processing failues using `Spring @Retryable` feature. 

Read my [Medium Post](https://medium.com/@SuiCxDe/messaging-using-aws-sqs-with-spring-boot-afdc95d68469) for detailed explaination.

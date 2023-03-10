package com.pa1.textdetectionapp.textdetectionapp.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class SQSService {
    private SqsClient sqsClient;

    private static String queueName = "Car.fifo";

    public SQSService() {
        this.sqsClient = SqsClient.builder()
                .region(Region.US_EAST_1)
                .build();
    }

    public SqsClient getSqsClient() {
        return sqsClient;
    }

    public String getQueueUrl(SqsClient sqsClient) {
        String queueName = "Car.fifo";
        String queueUrl;

        GetQueueUrlRequest getQueueUrlRequest = GetQueueUrlRequest.builder()
                .queueName(queueName)
                .build();

        try {
            queueUrl = sqsClient.getQueueUrl(getQueueUrlRequest).queueUrl();
        } catch (QueueDoesNotExistException e) {
            CreateQueueRequest request = CreateQueueRequest.builder()
                    .attributesWithStrings(Map.of("FifoQueue", "true", "ContentBasedDeduplication", "true"))
                    .queueName(queueName)
                    .build();
            sqsClient.createQueue(request);

            GetQueueUrlRequest getURLQue = GetQueueUrlRequest.builder()
                    .queueName(queueName)
                    .build();
            queueUrl = sqsClient.getQueueUrl(getURLQue).queueUrl();
        }

        return queueUrl;
    }

    public Message receiveMessage(SqsClient sqsClient, String queueUrl) throws InterruptedException {
        Message message = null;
        try {
            ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .maxNumberOfMessages(1)
                    .build();

            try {
                message = sqsClient.receiveMessage(receiveMessageRequest).messages().get(0);
            } catch (IndexOutOfBoundsException e) {
                log.info("Queue is empty, waiting for the message.");
            }
            return message;
        } catch (Exception e) {
            log.info(String.valueOf(e));
        }
        return message;
    }
}


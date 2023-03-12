package com.pa1.carrecognitionapp.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

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

    public boolean pushMessage(SqsClient sqsClient, String imgKey, String queueUrl) {
        try {
            SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageGroupId("CarText")
                    .messageBody(imgKey)
                    .build();
            String messageId = sqsClient.sendMessage(sendMsgRequest).sequenceNumber();
            log.info("Sequence number of the message: {}" , messageId);
            return true;
        } catch (Exception e) {
            log.info(String.valueOf(e));
            return false;
        }
    }
}

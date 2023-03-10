package com.pa1.carrecognitionapp.service;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.*;
import software.amazon.awssdk.services.s3.model.S3Object;


import java.io.FileNotFoundException;
import java.util.List;

@Slf4j
public class RekognitionService {
    private RekognitionClient rekognitionClient;

    public RekognitionService() {
        this.rekognitionClient = RekognitionClient.builder()
                .region(Region.US_EAST_1)
                .build();;
    }

    public RekognitionClient getRekognitionClient() {
        return rekognitionClient;
    }

    public boolean recognize(RekognitionClient rekognitionClient, S3Object s3Object, String bucketName) {
        try {
            Image img = Image.builder().s3Object(software.amazon.awssdk.services.rekognition.model.S3Object
                            .builder().bucket(bucketName).name(s3Object.key()).build())
                    .build();
            DetectLabelsRequest request = DetectLabelsRequest.builder().image(img).minConfidence((float) 90)
                    .build();
            DetectLabelsResponse result = rekognitionClient.detectLabels(request);
            List<Label> labels = result.labels();

            for (Label label : labels) {
                if (label.name().equals("Car")) {
                    return true;
                }
            }
        } catch (RekognitionException e) {
            log.info(e.getMessage());
        }
        return false;
    }
}

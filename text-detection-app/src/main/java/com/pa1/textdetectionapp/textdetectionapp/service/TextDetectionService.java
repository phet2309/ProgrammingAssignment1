package com.pa1.textdetectionapp.textdetectionapp.service;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.DetectTextRequest;
import software.amazon.awssdk.services.rekognition.model.DetectTextResponse;
import software.amazon.awssdk.services.rekognition.model.Image;
import software.amazon.awssdk.services.rekognition.model.TextDetection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class TextDetectionService {

    private RekognitionClient rekognitionClient;

    public TextDetectionService() {
        this.rekognitionClient = RekognitionClient.builder().region(Region.US_EAST_1).build();
    }

    public RekognitionClient getRekognitionClient() {
        return rekognitionClient;
    }

    public void detectTextFromImage(RekognitionClient rekognitionClient, Image image, String imgKey, Map<String, String> mp) {
        DetectTextRequest textRequest = DetectTextRequest.builder()
                .image(image)
                .build();

        DetectTextResponse textResponse = rekognitionClient.detectText(textRequest);
        List<TextDetection> textCollection = textResponse.textDetections();
        log.info("Detected text from image {}: {}", imgKey, textCollection.size());

        StringBuilder s = new StringBuilder();
        for (TextDetection text: textCollection) {
            s = s.append(text.detectedText()).append(" ");
        }
        mp.put(imgKey, s.toString());
    }
}

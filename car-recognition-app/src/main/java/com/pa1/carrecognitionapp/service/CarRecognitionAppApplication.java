package com.pa1.carrecognitionapp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.util.List;

@SpringBootApplication
@Slf4j
public class CarRecognitionAppApplication {

	private static String bucketName = "njit-cs-643";

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(CarRecognitionAppApplication.class, args);


		S3Service s3Service = new S3Service();
		S3Client s3Client = s3Service.getS3Client();
		List<S3Object> images = s3Service.s3DataFetch(s3Client);

		SQSService sqsService = new SQSService();
		SqsClient sqsClient = sqsService.getSqsClient();
		String queueUrl = sqsService.getQueueUrl(sqsClient);

		RekognitionService rekognitionService = new RekognitionService();
		RekognitionClient rekognitionClient = rekognitionService.getRekognitionClient();

		log.info("queueUrl: {}", queueUrl);
		for(S3Object img : images) {
			if (rekognitionService.recognize(rekognitionClient, img, bucketName)) {
				log.info("The image has car label with required confidence: {}", img.key());
				if(sqsService.pushMessage(sqsClient, img.key(), queueUrl))
					log.info("Message pushed successfully: {}", img.key());
				else
					log.info("Error while pushing the message: {}", img.key());
			} else {
				log.info("The image does not have car label: {}", img.key());
			}
//			Thread.sleep(5000);
		}

		sqsService.pushMessage(sqsClient, "-1", queueUrl);
		log.info("End of queue: -1");
	}
}


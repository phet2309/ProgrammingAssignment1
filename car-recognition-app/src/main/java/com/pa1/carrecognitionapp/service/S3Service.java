package com.pa1.carrecognitionapp.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.List;



public class S3Service {

    private static String bucketName = "njit-cs-643";


    private S3Client s3Client;

    public S3Service() {
        this.s3Client = S3Client.builder()
                .region(Region.US_EAST_1)
                .build();
    }

    public S3Client getS3Client() {
        return s3Client;
    }

    public List<S3Object> s3DataFetch(S3Client s3Client){
        try {
            ListObjectsRequest listObjects = ListObjectsRequest
                    .builder()
                    .bucket(bucketName)
                    .build();

            return s3Client.listObjects(listObjects).contents();

        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
        }

        return null;
    }

}

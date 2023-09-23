package com.example.gatosspringboot.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    @Value("${aws.access_key_id}")
    private String awsAccessKey;
    @Value("${aws.secret_access_key}")
    private String awsSecretKey;
    @Value("${aws.s3.bucket}")
    private String awsBucket;
    @Value("${aws.s3.region}")
    private String awsRegion;

    @Bean
    public AmazonS3 getS3Client(){
        BasicAWSCredentials credentials=new BasicAWSCredentials(awsAccessKey,awsSecretKey);
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(awsRegion)
                .build();
    }
}

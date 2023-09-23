package com.example.gatosspringboot.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    @Value("${aws.access_key_id}")
    private final String awsAccessKey;
    @Value("${aws.secret_access_key}")
    private final String awsSecretKey;
    @Value("${aws.s3.bucket}")
    private final String awsBucket;
    @Value("${aws.s3.region}")
    private final String awsRegion;

    public S3Config(String awsAccessKey, String awsSecretKey, String awsBucket, String awsRegion) {
        this.awsAccessKey = awsAccessKey;
        this.awsSecretKey = awsSecretKey;
        this.awsBucket = awsBucket;
        this.awsRegion = awsRegion;
    }

    public AmazonS3 getS3Client(){
        BasicAWSCredentials credentials=new BasicAWSCredentials(awsAccessKey,awsSecretKey);
        return AmazonS3ClientBuilder.standard().withRegion(Regions.fromName(awsRegion)
                ).withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
    }
}

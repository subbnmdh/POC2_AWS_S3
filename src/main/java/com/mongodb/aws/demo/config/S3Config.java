package com.mongodb.aws.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class S3Config {

	@Value("${secreteKey}")
	private String secretKey;
	
	@Value("${accessKey}")
	private String accessKey;
	
	@Bean
	public AmazonS3 getS3Client()
	{
		//System.out.println("\n\tSecretKey :: "+secretKey+"\n\taccessKey :: "+accessKey);
		AWSCredentials credentials=new BasicAWSCredentials(accessKey, secretKey);
		
		AmazonS3ClientBuilder amazonS3ClientBuilder=AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(Regions.AP_SOUTH_1);
		return amazonS3ClientBuilder.build();
	}
	
}

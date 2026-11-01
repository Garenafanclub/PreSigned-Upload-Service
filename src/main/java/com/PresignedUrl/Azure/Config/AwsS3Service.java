package com.PresignedUrl.Azure.Config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Slf4j
@Service
public class AwsS3Service {

    private final S3Presigner s3Presigner;

    private String bucketName;

    public AwsS3Service(S3Presigner s3Presigner) {
        this.s3Presigner = s3Presigner;
    }

}

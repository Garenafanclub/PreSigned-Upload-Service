package com.PresignedUrl.aws.dto.s3Event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record S3EventRecord(

        String eventVersion,
        String eventSource,
        String awsRegion,
        String eventName,
        S3Entity s3
) {}

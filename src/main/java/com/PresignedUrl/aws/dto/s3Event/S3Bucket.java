package com.PresignedUrl.aws.dto.s3Event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record S3Bucket(
    String name
) {}

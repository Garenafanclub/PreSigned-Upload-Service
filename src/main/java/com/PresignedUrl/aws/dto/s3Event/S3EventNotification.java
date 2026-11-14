package com.PresignedUrl.aws.dto.s3Event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record S3EventNotification(
     @JsonProperty("Records")
     List<S3EventRecord> records,

     @JsonProperty("Event")
     String event
){}

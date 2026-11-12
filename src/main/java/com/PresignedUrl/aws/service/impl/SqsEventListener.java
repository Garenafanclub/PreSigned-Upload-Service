package com.PresignedUrl.aws.service.impl;

import com.PresignedUrl.aws.dto.s3Event.S3EventNotification;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
public class SqsEventListener {

    @SqsListener("s3-uploads-events-queue")
    public void consumeMessage(S3EventNotification eventNotification)
    {
        log.info("Received message from SQS: {}", eventNotification);
        // Process the message as needed
        // parse a JSON message and extract the S3 key
        // You can use a JSON library like Jackson or Gson to parse the message
        if(eventNotification == null || eventNotification.records() == null || eventNotification.records().isEmpty()) {
            log.warn("Received empty or null S3EventNotification: {}", eventNotification);
            return;
        }



    }
}

/*
 {
  "Records": [
    {
      "eventVersion": "2.1",
      "eventSource": "aws:s3",
      "awsRegion": "ap-south-1",
      "eventTime": "2026-11-11T19:13:00.000Z",
      "eventName": "ObjectCreated:Put",
      "userIdentity": { "principalId": "AWS:SOME_ID" },
      "s3": {
        "s3SchemaVersion": "1.0",
        "configurationId": "testConfigRule",
        "bucket": {
          "name": "zkteco-project-bucket",
          "arn": "arn:aws:s3:::zkteco-project-bucket"
        },
        "object": {
          "key": "my-uploaded-file.pdf",
          "size": 1024,
          "eTag": "d41d8cd98f00b204e9800998ecf8427e"
        }
      }
    }
  ]
}
 */

package com.PresignedUrl.aws.service.impl;

import com.PresignedUrl.aws.dto.s3Event.S3EventNotification;
import com.PresignedUrl.aws.dto.s3Event.S3EventRecord;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SqsEventListener {

    @SqsListener("s3-uploads-events-queue")
    public void consumeMessage(S3EventNotification eventNotification)
    {
        log.info("Received message from SQS: {}", eventNotification);
        // Process the message as needed
        // parse a JSON message and extract the S3 key
        // You can use a JSON library like Jackson or Gson to parse the message ( or use the S3EventNotification class to deserialize it directly)
        try {
            if (eventNotification == null || eventNotification.records() == null || eventNotification.records().isEmpty()) {
                log.warn("Received empty or null S3EventNotification: {}", eventNotification);
                return; // Don't throw exception, just log and return and let the SQS DELETE this useless message...
            }
            // Idempotent processing honi chahiye...
            // if record/event is wrong , then don't process it further and directly send into dlq...
        }catch (DataAccessException e){
            log.error("Database connection failure while processing SQS message: {}", e.getMessage());
            throw e; // Rethrow to let SQS know to retry the message later
        }catch (Exception e) {
            log.error("Unexpected error while processing SQS message: {}", e.getMessage());
            throw new RuntimeException("Unexpected SQS failure", e); // Rethrow to let SQS know to retry the message later.
        }

        for(S3EventRecord record : eventNotification.records()) {
            if(record == null ||
                    record.eventName() == null ||
                    record.s3() == null ||
                    record.s3().object() == null ||
                    record.s3().object().key() == null ||
                    record.s3().bucket() == null ||
                    record.s3().bucket().name() == null) {
                log.error("Invalid S3EventRecord received: {}", record);
                throw new IllegalArgumentException("Invalid S3EventRecord received: ");
            }
        }

    }
}

/*
// NOT A REAL SQS MESSAGE, but a sample S3 event notification JSON for reference
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
          "etag": "d41d8cd98f00b204e9800998ecf8427e"
        }
      }
    }
  ]
}
 */

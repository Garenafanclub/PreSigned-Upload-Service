package com.PresignedUrl.aws.service.impl;

import com.PresignedUrl.aws.dto.s3Event.S3EventNotification;
import com.PresignedUrl.aws.dto.s3Event.S3EventRecord;
import com.PresignedUrl.aws.service.FileUploadService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class SqsEventListener {

    private final FileUploadService fileUploadService;

    public SqsEventListener(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @SqsListener("s3-uploads-events-queue")
    public void consumeMessage(S3EventNotification eventNotification) {

        log.info("Received message from SQS: {}", eventNotification);

        // parse a JSON message and extract the S3 key
        // You can use a JSON library like Jackson to parse the message ( or use the S3EventNotification class to deserialize it directly)

        if (eventNotification == null) {
            log.warn("Received null S3 event notification. Ignoring.");
            return;
        }

        // Handle S3 configuration test event.
        if ("s3:TestEvent".equals(eventNotification.event())) {
            log.info("Received S3 test event. Ignoring.");
            return;
        }

        if (eventNotification.records() == null ||
                eventNotification.records().isEmpty()) {
            log.warn("Received S3 event without records. Ignoring.");
            return;
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
                continue; // Don't throw exception, just log and return and let the SQS DELETE this useless message...
            }

            // YOU CAN CHECK FOR VALID BUCKET ALSO IF YOU WANT I am skipping...

            if (!record.eventName().startsWith("ObjectCreated:")) {
                log.debug("Ignoring event type: {}", record.eventName());
                continue;
            }

            String s3ObjectKey = decodeS3ObjectKey(record.s3().object().key());
            try {
                fileUploadService.markUploadAsComplete(s3ObjectKey);

            } catch (ResponseStatusException e) {

                if (e.getStatusCode().value() == 404) {
                    log.warn("File metadata not found for S3 key: {}", s3ObjectKey);
                    continue;
                }
                throw e;
            }
        }

    }

    public String decodeS3ObjectKey(String s3ObjectKey){
        // DECODE THE S3 OBJECT KEY IF - IT CONTAINS SPECIAL CHARACTERS LIKES SPACES, PLUS SIGNS, ETC...
        return URLDecoder.decode(s3ObjectKey, StandardCharsets.UTF_8);
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
 // REAL ONE...
{
  "records": [
    {
      "eventVersion": "2.6",
      "eventSource": "aws:s3",
      "awsRegion": "ap-southeast-2",
      "eventName": "ObjectCreated:Put",
      "s3": {
        "bucket": {
          "name": "my-presigned-upload"
        },
        "object": {
          "key": "uploads/users/1/76283a_backup.pdf"
        }
      }
    }
  ]
}
 */

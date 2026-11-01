package com.PresignedUrl.aws.service.impl;

import com.PresignedUrl.aws.dto.request.PresignedUrlRequest;
import com.PresignedUrl.aws.dto.response.PresignedUrlResponse;
import com.PresignedUrl.aws.mapper.FileMapper;
import com.PresignedUrl.aws.model.FileMetaData;
import com.PresignedUrl.aws.model.User;
import com.PresignedUrl.aws.repository.FileMetadataRepo;
import com.PresignedUrl.aws.repository.UserRepo;
import com.PresignedUrl.aws.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {

    private final UserRepo userRepo;
    private final FileMapper fileMapper;
    private final FileMetadataRepo fileMetadataRepo;

    private final AwsS3Service awsS3Service;

    public FileUploadServiceImpl(UserRepo userRepo, FileMapper fileMapper, FileMetadataRepo fileMetadataRepo, AwsS3Service awsS3Service) {
        this.userRepo = userRepo;
        this.fileMapper = fileMapper;
        this.fileMetadataRepo = fileMetadataRepo;
        this.awsS3Service = awsS3Service;
    }

    @Override
    public PresignedUrlResponse generatePresignedUrl(PresignedUrlRequest presignedUrlRequest) {

        log.info("Initiating presigned URL generation for user ID: {}, file: {}", presignedUrlRequest.userId(), presignedUrlRequest.fileName());

        // Validate user existence
        User user = userRepo.findById(presignedUrlRequest.userId())
                .orElseThrow(()-> {
                    log.error("Upload failed: User ID {} not found", presignedUrlRequest.userId());
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
                });

        // Generate unique S3 key for the file
        String generates3Key = "uploads/users/" + user.getUserId() + "/" + UUID.randomUUID() + "_" + presignedUrlRequest.fileName();

        // Map and save file metadata to the database (omitted for brevity)
        FileMetaData fileMetaData = fileMapper.toEntity(presignedUrlRequest);
        fileMetaData.setUser(user);
        fileMetaData.setS3Key(generates3Key);

        FileMetaData savedEntity = fileMetadataRepo.save(fileMetaData);
        log.info("Database record created with ID: {}", fileMetaData.getFileId());

        // Request the URL from our S3 service
        String awsURL = awsS3Service.generatePresignedPutURL(
                                  generates3Key,
                                  presignedUrlRequest.contentType());

        return new PresignedUrlResponse(generates3Key, awsURL, savedEntity.getFileId());
    }
}

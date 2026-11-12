package com.PresignedUrl.aws.service.impl;

import com.PresignedUrl.aws.dto.request.PresignedUrlRequest;
import com.PresignedUrl.aws.dto.response.PresignedUrlResponse;
import com.PresignedUrl.aws.mapper.FileMapper;
import com.PresignedUrl.aws.model.FileMetadata;
import com.PresignedUrl.aws.model.UploadStatus;
import com.PresignedUrl.aws.model.User;
import com.PresignedUrl.aws.repository.FileMetadataRepo;
import com.PresignedUrl.aws.repository.UserRepo;
import com.PresignedUrl.aws.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
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
        FileMetadata fileMetaData = fileMapper.toEntity(presignedUrlRequest);
        fileMetaData.setUser(user);
        fileMetaData.setS3Key(generates3Key);

        FileMetadata savedEntity = fileMetadataRepo.save(fileMetaData);
        log.info("Database record created with ID: {}", savedEntity.getFileId());

        // Request the URL from our S3 service
        String awsURL = awsS3Service.generatePresignedPutURL(
                                  generates3Key,
                                  presignedUrlRequest.contentType());

        return new PresignedUrlResponse(generates3Key, awsURL, savedEntity.getFileId());
    }

    @Override
    @Transactional(readOnly = true)
    public String generateUrlForDownload(UUID fileId, Long userId) {
        log.info("Generating presigned download URL for file ID: {}", fileId);

        FileMetadata fileMetadata = fileMetadataRepo.findById(fileId)
                .orElseThrow(() -> {
                    log.error("File with ID {} not found", fileId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found");
                });

        if(fileMetadata.getUploadStatus() != null && fileMetadata.getUploadStatus().equals(UploadStatus.COMPLETED)) {
            if(!fileMetadata.getUser().getUserId().equals(userId)){
                log.warn("Security violation: User {} attempted to access file {} owned by user {}",
                        userId, fileId, fileMetadata.getUser().getUserId());
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to access this file");
            }

            return awsS3Service.generatePresignedGetURL(fileMetadata.getS3Key());
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File upload is not completed yet");
    }
}

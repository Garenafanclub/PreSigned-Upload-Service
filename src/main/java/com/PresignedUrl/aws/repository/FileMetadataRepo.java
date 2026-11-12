package com.PresignedUrl.aws.repository;

import com.PresignedUrl.aws.model.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FileMetadataRepo extends JpaRepository<FileMetadata, UUID> {

    Optional<FileMetadata> findByS3Key(String s3Key);
}

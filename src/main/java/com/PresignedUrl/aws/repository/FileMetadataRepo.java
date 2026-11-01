package com.PresignedUrl.aws.repository;

import com.PresignedUrl.aws.model.FileMetaData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FileMetadataRepo extends JpaRepository<FileMetaData, UUID> {
}

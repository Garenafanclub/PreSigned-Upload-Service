package com.PresignedUrl.Azure.Repository;

import com.PresignedUrl.Azure.Model.FileMetaData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FileMetadataRepo extends JpaRepository<FileMetaData, UUID> {
}

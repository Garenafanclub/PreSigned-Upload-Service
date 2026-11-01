package com.PresignedUrl.aws.mapper;

import com.PresignedUrl.aws.dto.request.PresignedUrlRequest;
import com.PresignedUrl.aws.dto.response.PresignedUrlResponse;
import com.PresignedUrl.aws.model.FileMetadata;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FileMapper {

    // FILE DTO -- ENTITY... // we use source name explicitly if we have more than one param... (no need here)
    @Mapping(source = "presignedUrlRequest.fileName", target = "originalFilename")
    @Mapping(source = "presignedUrlRequest.fileSizeBytes", target = "fileSize")
    // IGNORE THIS FIELDS... will do in service layer manually...
    @Mapping(target = "fileId", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "s3Key", ignore = true)
    @Mapping(target = "uploadStatus", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    FileMetadata toEntity(PresignedUrlRequest presignedUrlRequest);

    // presignedUrl is ignored because it's not in the DB, it comes from AWS
    @Mapping(target = "presignedUrl", ignore = true)
    PresignedUrlResponse toResponse(FileMetadata fileMetaData);

}

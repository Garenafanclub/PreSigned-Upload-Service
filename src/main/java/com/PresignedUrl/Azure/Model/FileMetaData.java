package com.PresignedUrl.Azure.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "file_metadata")
public class FileMetaData {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID fileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String originalFilename;

    @Column(nullable = false)
    private String contentType;

    @Column(name = "s3_key", nullable = false, unique = true, length = 500)
    private String s3Key;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UploadStatus uploadStatus = UploadStatus.PENDING;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

}

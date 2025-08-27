package com.lykos.application.service;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final MinioClient minioClient;

    @Value("${minio.bucket.name}")
    private String defaultBucketName;

    public String uploadFile(byte[] fileData, String fileName, String folder) {
        try {
            // Verificar se o bucket existe
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(defaultBucketName).build());
            
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(defaultBucketName).build());
            }

            // Gerar nome único para o arquivo
            String uniqueFileName = folder + "/" + UUID.randomUUID() + "_" + fileName;

            // Fazer upload
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(defaultBucketName)
                    .object(uniqueFileName)
                    .stream(new ByteArrayInputStream(fileData), fileData.length, -1)
                    .contentType(getContentType(fileName))
                    .build());

            return uniqueFileName;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao fazer upload do arquivo: " + e.getMessage());
        }
    }

    public String uploadFile(MultipartFile file, String folder) {
        try {
            return uploadFile(file.getBytes(), file.getOriginalFilename(), folder);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar arquivo: " + e.getMessage());
        }
    }

    private String getContentType(String fileName) {
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".gif")) {
            return "image/gif";
        } else if (fileName.endsWith(".mp4")) {
            return "video/mp4";
        } else if (fileName.endsWith(".pdf")) {
            return "application/pdf";
        }
        return "application/octet-stream";
    }
}
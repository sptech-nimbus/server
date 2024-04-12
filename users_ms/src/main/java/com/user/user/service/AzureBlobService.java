package com.user.user.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;

@Service
public class AzureBlobService {
    @Value("${azure.storage.connection-string}")
    String connectionString;

    @Value("${azure.storage.container-name}")
    String containerName;

    @Async
    public void uploadImage(MultipartFile file) throws IOException {
        try {
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(this.connectionString)
                    .buildClient();

            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

            BlobClient blobClient = containerClient.getBlobClient(file.getOriginalFilename());

            blobClient.upload(file.getInputStream(), file.getSize(), true);
        } catch (IOException e) {
            throw e;
        }
    }
}
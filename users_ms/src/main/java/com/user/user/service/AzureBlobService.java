package com.user.user.service;

import java.io.*;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import com.user.user.domain.athlete.Athlete;
import com.user.user.domain.coach.Coach;
import com.user.user.domain.team.Team;
import com.user.user.exception.ResourceNotFoundException;
import com.user.user.repository.AthleteRepository;
import com.user.user.repository.CoachRepository;
import com.user.user.repository.TeamRepository;
import com.user.user.util.CodeGenerator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AzureBlobService {
    @Value("${azure.storage.connection-string}")
    String connectionString;

    @Value("${azure.storage.container-name}")
    String containerName;

    private final CoachRepository coachRepository;
    private final AthleteRepository athleteRepository;
    private final TeamRepository teamRepository;

    @Async
    public void uploadImage(UUID entityId, MultipartFile file) throws IOException, ResourceNotFoundException {
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();

        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

        BlobHttpHeaders headers = new BlobHttpHeaders().setContentType("image/png");

        String blobName = CodeGenerator.codeGen(12, true) + file.getOriginalFilename().replaceAll("\\s", "");

        BlobClient blobClient = containerClient.getBlobClient(blobName);

        blobClient.upload(file.getInputStream(), file.getSize(), true);

        blobClient.setHttpHeaders(headers);

        if (coachRepository.existsByUserId(entityId)) {
            Coach coachFound = coachRepository.findCoachByUserId(entityId).get();
            coachFound.setPicture(createBlobSas(blobClient));

            coachRepository.save(coachFound);
        } else if (athleteRepository.existsByUserId(entityId)) {
            Athlete athleteFound = athleteRepository.findAthleteByUserId(entityId).get();
            athleteFound.setPicture(createBlobSas(blobClient));

            athleteRepository.save(athleteFound);
        } else if (teamRepository.existsById(entityId)) {
            Team teamFound = teamRepository.findById(entityId).get();
            teamFound.setPicture(createBlobSas(blobClient));

            teamRepository.save(teamFound);
        } else {
            throw new ResourceNotFoundException("Entidade", entityId);
        }
    }

    public String uploadCSVFile(String blobName, String filePath) {
        try {
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(connectionString)
                    .buildClient();

            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
            BlobHttpHeaders headers = new BlobHttpHeaders().setContentType("text/CSV");
            BlobClient blobClient = containerClient.getBlobClient(blobName);
            
            InputStream dataStream = new FileInputStream(filePath);
            blobClient.upload(dataStream, true);
            
            blobClient.setHttpHeaders(headers);

            return createBlobSas(blobClient);
        } catch (Exception e) {
            System.out.println(e);
            return "";
        }
    }

    public String createBlobSas(BlobClient blobClient) {
        OffsetDateTime expirationTime = OffsetDateTime.now().plusYears(10);

        BlobSasPermission permission = new BlobSasPermission().setReadPermission(true);

        BlobServiceSasSignatureValues sasSignatureValues = new BlobServiceSasSignatureValues(expirationTime, permission)
                .setStartTime(OffsetDateTime.now().minusMinutes(5));

        String sasToken = blobClient.generateSas(sasSignatureValues);

        return String.format("https://%s.blob.core.windows.net/%s/%s?%s",
                blobClient.getAccountName(),
                containerName,
                blobClient.getBlobName(),
                sasToken);
    }
}
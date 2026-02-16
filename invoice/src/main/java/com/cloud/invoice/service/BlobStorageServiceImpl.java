package com.cloud.invoice.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Service
public class BlobStorageServiceImpl implements BlobStorageService {
    @Value("${azure.storage.connection-string}")
    private String connectionString;

    @Value("${azure.storage.blob-container}")
    private String containerName;

    private BlobContainerClient containerClient;

    @PostConstruct
    public void init() {
        containerClient = new BlobContainerClientBuilder()
                .connectionString(connectionString)
                .containerName(containerName)
                .buildClient();
        containerClient.createIfNotExists();
    }

    @Override
    public String uploadPdf(byte[] pdfBytes, String fileName) {
        BlobClient blobClient = containerClient.getBlobClient(fileName);
        blobClient.upload(new ByteArrayInputStream(pdfBytes), pdfBytes.length, true);
        return blobClient.getBlobUrl();
    }
}

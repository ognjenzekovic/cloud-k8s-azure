package com.cloud.invoice.service;

public interface BlobStorageService {
    String uploadPdf(byte[] pdfBytes, String fileName);
}

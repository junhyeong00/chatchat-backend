package com.junhyeong.chatchat.applications.file;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.junhyeong.chatchat.utils.S3Component;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class S3FileService implements FileService {
    private final AmazonS3Client amazonS3Client;
    private final S3Component component;

    public S3FileService(AmazonS3Client amazonS3Client, S3Component component) {
        this.amazonS3Client = amazonS3Client;
        this.component = component;
    }

    @Override
    public void upload(InputStream inputStream, ObjectMetadata objectMetadata, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(component.getBucket(), fileName, inputStream, objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    @Override
    public String getFileUrl(String fileName) {
        return amazonS3Client.getUrl(component.getBucket(), fileName).toString();
    }
}

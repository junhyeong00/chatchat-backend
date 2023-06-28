package com.junhyeong.chatchat.applications.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.junhyeong.chatchat.utils.S3Component;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class S3FileService implements FileService {
    private final AmazonS3 amazonS3;
    private final S3Component component;

    public S3FileService(AmazonS3 amazonS3, S3Component component) {
        this.amazonS3 = amazonS3;
        this.component = component;
    }

    @Override
    public void upload(InputStream inputStream, ObjectMetadata objectMetadata, String fileName) {
        amazonS3.putObject(new PutObjectRequest(component.getBucket(), fileName, inputStream, objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    @Override
    public String getFileUrl(String fileName) {
        return amazonS3.getUrl(component.getBucket(), fileName).toString();
    }
}

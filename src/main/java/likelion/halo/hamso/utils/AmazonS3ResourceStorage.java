package likelion.halo.hamso.utils;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3ResourceStorage {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3Client amazonS3Client;

    public void store(String fullPath, MultipartFile multipartFile) throws FileUploadException {

//        File file = new File(MultipartUtil.getLocalHomeDirectory(), fullPath);
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(multipartFile.getSize());
            metadata.setContentType(multipartFile.getContentType());
            amazonS3Client.putObject(new PutObjectRequest(bucket, fullPath, multipartFile.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            log.info("Successfully uploaded file to S3. Path: {}", fullPath);

//            multipartFile.transferTo(file);
//            amazonS3Client.putObject(new PutObjectRequest(bucket, fullPath, file)
//                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (Exception e) {
            // 로그에 에러 메세지와 스택 트레이스를 출력
            log.error("Failed to upload file to S3. Path: {}", fullPath, e);
            // 구체적인 예외를 던짐
            throw new FileUploadException("Failed to upload file to S3", e);
        }
    }
}
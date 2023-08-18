package likelion.halo.hamso.controller;

import likelion.halo.hamso.dto.s3.FileDetail;
import likelion.halo.hamso.service.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/s3", produces = APPLICATION_JSON_VALUE)
public class S3Controller {
    private final S3UploadService s3UploadService;

    @PostMapping("/upload")
    public ResponseEntity<FileDetail> post(
            @RequestPart("file") MultipartFile multipartFile) {
        return ResponseEntity.ok(s3UploadService.save(multipartFile));
    }

}

package org.zerock.projectmeongmung.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
public class FileController {
    @GetMapping("/profiles/{filename}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            // 파일이 저장된 실제 경로
            // /Users/yunakang/uploads/
            // C:\\work\\uploads\\
            Path filePath = Paths.get("C:\\work\\uploads\\").resolve(filename);
            Resource file = new UrlResource(filePath.toUri());

            // 파일이 존재하고 읽을 수 있는지 확인
            if (file.exists() || file.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                        .body(file);
            } else {
                throw new RuntimeException("Could not read file: " + filename);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while serving file", e);
        }
    }

    public String saveProfilePhoto(MultipartFile file) throws IOException {
        // 저장할 경로 설정
        // /Users/yunakang/uploads/
        String uploadDir = "C:\\work\\uploads\\";

        // 파일명을 고유하게 하기 위해 UUID를 사용하거나 다른 로직을 사용할 수 있습니다.
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // 전체 경로 생성
        String filePath = uploadDir + fileName;

        // 파일을 저장할 경로로 전송
        file.transferTo(new java.io.File(filePath));

        // 저장된 파일의 경로 반환 (웹에서 접근 가능한 경로로 변환)
        return  fileName;
    }


}

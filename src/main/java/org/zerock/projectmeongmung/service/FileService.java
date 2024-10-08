package org.zerock.projectmeongmung.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {

    // /Users/yunakang/uploads/
    // src/main/resources/static/image/product/
    private final String uploadDir = "src/main/resources/static/image/product/"; // 경로를 환경변수나 설정 파일에서 가져오도록 변경할 수 있다

    public String saveFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + fileName);
        Files.copy(file.getInputStream(), filePath);
        return fileName;
    }
}

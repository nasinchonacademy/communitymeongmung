package org.zerock.projectmeongmung.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VetDTO {

    private Long vetid;
    private String vetname;
    private String animalhospitlename;
    private String registerdate;
    private String withdrawaldate;
    private MultipartFile profilePhoto; // 프로필 사진 업로드 시 사용
    private String profilePhotoPath;    // 저장된 파일 경로를 다루는 필드
    private Boolean visibility;
    private List<String> description = new ArrayList<>();
}


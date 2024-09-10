package org.zerock.projectmeongmung.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.projectmeongmung.entity.Vet;

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
    private String username;                // 수의사 로그인 아이디
    private String password;        // 수의사 로그인 비밀번호 (암호화되기 전의 원본 비밀번호)
    private String email;
    private List<String> description = new ArrayList<>();
    private int recommendationCount;
    private int commentCount;

    public VetDTO(Vet vet) {
    }
}


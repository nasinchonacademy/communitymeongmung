package org.zerock.projectmeongmung.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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
    private MultipartFile profilePhoto;
    private Boolean visibility;
    private List<String> description;
}

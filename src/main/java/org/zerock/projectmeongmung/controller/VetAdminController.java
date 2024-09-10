package org.zerock.projectmeongmung.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.projectmeongmung.dto.VetDTO;
import org.zerock.projectmeongmung.entity.Vet;
import org.zerock.projectmeongmung.entity.VetLog;
import org.zerock.projectmeongmung.service.VetLogService;
import org.zerock.projectmeongmung.service.VetService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")  // 클래스 레벨에서 기본 경로를 설정
@PreAuthorize("hasRole('ADMIN')")  // 관리자 권한이 있는 경우만 접근 가능
public class VetAdminController {
    @Autowired
    private VetService vetService;
    @Autowired
    private VetLogService vetLogService;
    @Autowired
    private FileController fileController;

    // 기본 관리자 페이지로 이동
    @GetMapping("/index")
    public String adminIndex() {
        return "admin/index";  // admin/index.html 뷰로 이동
    }

    // 수의사 등록 페이지로 이동하는 메서드
    @GetMapping("/vetregister")
    public String showRegisterForm(Model model) {
        model.addAttribute("vetDTO", new VetDTO());
        return "admin/VetEnrollment"; // 템플릿 파일 이름 (e.g., vet-register.html)
    }

    // 수의사 등록을 처리하는 메서드
    @PostMapping("/vetregister")
    public String registerVet(VetDTO vetDTO) {
        try {
            String descriptionText = vetDTO.getDescription().get(0); // 텍스트 영역에서의 내용을 가져옴
            List<String> descriptions = Arrays.asList(descriptionText.split("\n"));
            vetDTO.setDescription(descriptions);

            vetService.registerVet(vetDTO);
        } catch (IOException e) {
            // 파일 저장 중 오류가 발생한 경우 처리
            e.printStackTrace();
            return "redirect:/vet/register?error";
        }
        return "redirect:/admin/vetlist";
    }

    // 수의사 목록 출력 메서드
    @GetMapping("/vetlist")
    public String listVets(Model model) {
        List<Vet> vets = vetService.getAllVets();
        model.addAttribute("vets", vets);
        return "admin/adminvetList";
    }

    // 수의사 공개/비공개 토글 처리
    @PostMapping("/vet/toggleVisibility/{vetid}")
    public String toggleVisibility(@PathVariable Long vetid) {
        vetService.toggleVisibility(vetid);
        return "redirect:/admin/vetlist";
    }

    @GetMapping("/vet/edit/{vetid}")
    public String editVet(@PathVariable Long vetid, Model model) {
        VetDTO vetDTO = vetService.getVetById(vetid);

        // description을 하나의 문자열로 합침, 줄바꿈 유지
        if (vetDTO.getDescription() != null && !vetDTO.getDescription().isEmpty()) {
            String descriptionText = String.join("\n", vetDTO.getDescription());  // 줄바꿈 유지
            model.addAttribute("descriptionText", descriptionText);
        } else {
            model.addAttribute("descriptionText", "");  // description이 null이거나 비어있을 경우 빈 문자열
        }

        model.addAttribute("vetDTO", vetDTO);
        return "admin/VetEdit";  // 수의사 수정 페이지
    }

    // 수의사 수정 처리
    @PostMapping("/vet/edit/{vetid}")
    public String updateVet(@PathVariable Long vetid, @ModelAttribute VetDTO vetDTO,
                            @RequestParam("description") String descriptionText) throws IOException {

        // 저장할 때 description을 줄 단위로 분리하고 쉼표를 모두 제거하여 리스트에 저장
        List<String> descriptions = Arrays.stream(descriptionText.split("\n"))
                .map(line -> line.replaceAll(",", ""))  // 쉼표 제거
                .map(String::trim)  // 각 줄 앞뒤 공백 제거
                .filter(line -> !line.isEmpty())  // 빈 줄 제거
                .collect(Collectors.toList());

        vetDTO.setDescription(descriptions);  // 쉼표 제거된 내용을 다시 저장

        // 프로필 사진이 업로드되었는지 확인하고 처리
        if (vetDTO.getProfilePhoto() != null && !vetDTO.getProfilePhoto().isEmpty()) {
            String profilePhotoFileName = fileController.saveProfilePhoto(vetDTO.getProfilePhoto());
            vetDTO.setProfilePhotoPath(profilePhotoFileName);  // 새 프로필 사진 파일명 저장
        }

        vetService.updateVet(vetid, vetDTO);
        return "redirect:/admin/vetlist";  // 수정 후 목록으로 이동
    }

    @GetMapping("/details/{vetid}")
    public String showVetDetails(@PathVariable Long vetid, Model model) {
        VetDTO vetDTO = vetService.getVetById(vetid);
        List<VetLog> vetLogs = vetLogService.getLogsByVetId(vetid);  // 로그 목록 조회
        model.addAttribute("vetDTO", vetDTO);
        model.addAttribute("vetLogs", vetLogs);
        return "admin/Vetdetails";
    }

    @PostMapping("/details/{vetid}/addLog")
    public String addLog(@PathVariable Long vetid, String logMessage) {
        Vet vet = vetService.getVetEntityById(vetid);  // 엔티티 조회
        vetLogService.saveVetLog(vet, logMessage);  // 로그 저장
        return "redirect:/admin/details/" + vetid;  // 상세보기 페이지로 리다이렉트
    }


    // 수의사 삭제 요청
    @PostMapping("/vetdelete")
    public String deleteVet(@RequestParam("vetId") Long vetId, RedirectAttributes redirectAttributes) {
        try {
            vetService.deleteVet(vetId); // 서비스에서 수의사 삭제 처리
            redirectAttributes.addFlashAttribute("successMessage", "수의사 삭제가 완료되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "수의사 삭제 중 오류가 발생했습니다.");
        }
        return "redirect:/admin/vetlist";
    }
}




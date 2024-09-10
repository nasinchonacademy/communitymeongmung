package org.zerock.projectmeongmung.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.projectmeongmung.dto.MeongStoryDTO;
import org.zerock.projectmeongmung.dto.PageRequestDTO;
import org.zerock.projectmeongmung.dto.PageResultDTO;
import org.zerock.projectmeongmung.dto.StoryCommentDto;
import org.zerock.projectmeongmung.entity.MeongStory;
import org.zerock.projectmeongmung.entity.Notice;
import org.zerock.projectmeongmung.entity.User;
import org.zerock.projectmeongmung.repository.NoticeRepository;
import org.zerock.projectmeongmung.repository.StoryCommentRepository;
import org.zerock.projectmeongmung.repository.UserRepository;
import org.zerock.projectmeongmung.service.MeongStoryCommentService;
import org.zerock.projectmeongmung.service.MeongStoryService;
import org.zerock.projectmeongmung.service.UserDetailService;
import org.zerock.projectmeongmung.service.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("/admin")  // 클래스 레벨에서 기본 경로를 설정
@PreAuthorize("hasRole('ADMIN')")  // 관리자 권한이 있는 경우만 접근 가능
@Log4j2
@RequiredArgsConstructor
public class MeongStoryBoardAdminController {

    @Autowired
    private UserDetailService userDetailService;

    @Autowired
    private UserService userService; // 사용자 정보 가져오기

    private final UserRepository userRepository;
    private final MeongStoryService meongStoryService;
    private final NoticeRepository noticeRepository;
    private final MeongStoryCommentService serviceC;
    private final MeongStoryService service;


    @GetMapping("/getMeongStoryBoardList")
    public String getMeongStoryBoardList(@RequestParam(value = "page", defaultValue = "1") int page,
                                         @RequestParam(value = "size", defaultValue = "10") int size,
                                         @RequestParam(value = "type", required = false) String type,
                                         @RequestParam(value = "keyword", required = false) String keyword,
                                         Model model) {
        // PageRequestDTO 객체 생성
        PageRequestDTO pageRequestDTO = new PageRequestDTO();
        pageRequestDTO.setPage(page);
        pageRequestDTO.setSize(size);
        pageRequestDTO.setType(type);
        pageRequestDTO.setKeyword(keyword);

        // MeongStory 리스트를 가져오는 서비스 호출
        PageResultDTO<MeongStoryDTO, MeongStory> result = meongStoryService.getAllItems(pageRequestDTO);

        // 로그로 결과를 확인
        log.info("Admin requested MeongStoryBoardList: {}", result);

        // 모델에 결과 데이터를 추가
        model.addAttribute("result", result);
        model.addAttribute("pageRequestDTO", pageRequestDTO);

        return "admin/getMeongStoryBoardList";
    }

    @GetMapping("/storyread")
    public String storylistread(@RequestParam("seq") long seq,
                                @ModelAttribute("requestDTO") PageRequestDTO requestDTO,
                                Model model) {

        // seq 값에 해당하는 게시글 상세 정보를 가져옴
        MeongStoryDTO dto = service.read(seq);

        // dto를 모델에 추가하여 뷰로 전달
        model.addAttribute("dto", dto);

        // 게시글 상세보기 페이지로 이동
        return "admin/getMeongStoryBoardRead";
    }

    @GetMapping("/getStorywirte")
    public String storywirte(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userDetailService.findUserByUid(username);
        model.addAttribute("user", user);
        return "admin/getMongStoryWirte";
    }

    @PostMapping("/getStorywirte")
    public String storywirtePost(MeongStoryDTO dto, RedirectAttributes redirectAttributes,
                                 @RequestParam("file") MultipartFile file,
                                 Model model) {

        log.info("dto..." + dto);

        // 파일이 비어 있지 않은지 확인
        if (!file.isEmpty()) {
            try {
                // 파일 저장 메서드를 호출하여 파일을 저장하고, 저장된 파일명을 DTO에 설정
                String savedFileName = saveBoardPhoto(file);
                dto.setPicture(savedFileName); // 파일명을 DTO의 picture 필드에 설정

            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("message", "파일 업로드 실패");
                return "error"; // 오류 페이지로 리다이렉트 또는 표시
            }
        }
        // 서비스 계층을 통해 게시글 등록
        service.register(dto);

        return "redirect:/admin/getMeongStoryBoardList";
    }

    public String saveBoardPhoto(MultipartFile file) throws IOException {
        // 저장할 경로 설정
        String uploadDir = "C:\\work\\uploads\\";

        // 파일명을 고유하게 하기 위해 UUID를 사용
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // 전체 경로 생성
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String filePath = uploadDir + fileName;

        // 파일을 지정된 경로에 저장
        file.transferTo(new java.io.File(filePath));

        // 저장된 파일의 경로를 반환
        return fileName;
    }



    @PostMapping("/remove")
    public String remove(long seq, String title, String nickname, String uid,
                         RedirectAttributes redirectAttributes,
                         Model model){
        // 게시글 삭제 처리
        service.remove(seq);

        // 알림 메시지 작성
        String message = "[" + title + "] 게시글이 운영 위반으로 삭제되었습니다.";

        // User 객체 가져오기 (User는 Notice와 연관됨)
        User user = userRepository.findByUid(uid)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Notice 생성 및 저장
        Notice notice = new Notice(message, user);
        noticeRepository.save(notice);  // Notice 테이블에 저장, user_id 참조

        // 리다이렉트 설정
        redirectAttributes.addFlashAttribute("msg", seq);
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/admin/getMeongStoryBoardList";
    }
}

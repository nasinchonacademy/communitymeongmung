package org.zerock.projectmeongmung.controller;

import org.springframework.web.bind.annotation.*;
import org.zerock.projectmeongmung.dto.NoticeDTO;
import org.zerock.projectmeongmung.entity.Notice;
import org.zerock.projectmeongmung.service.NoticeService;
import org.zerock.projectmeongmung.entity.User;
import org.zerock.projectmeongmung.service.UserDetailService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class NoticeController {

    private final NoticeService noticeService;
    private final UserDetailService userDetailService;

    public NoticeController(NoticeService noticeService, UserDetailService userDetailService) {
        this.noticeService = noticeService;
        this.userDetailService = userDetailService;
    }

    @GetMapping("/notices")
    @ResponseBody
    public List<NoticeDTO> getNotices(@RequestParam("uid") String uid) {
        User user = userDetailService.findUserByUid(uid);

        // NoticeDTO에 id와 message 포함해서 반환
        return noticeService.getNoticesByUser(user).stream()
                .map(notice -> new NoticeDTO(notice.getID(), notice.getMessage()))
                .collect(Collectors.toList());
    }
    @PostMapping("/noticeremove")
    @ResponseBody
    public List<NoticeDTO> removeNotice(@RequestParam("id") Long id, @RequestParam("uid") String uid) {
        // 특정 알림 삭제
        noticeService.removeNoticeById(id);

        // 해당 사용자의 남은 알림을 다시 가져옴
        User user = userDetailService.findUserByUid(uid);
        return noticeService.getNoticesByUser(user).stream()
                .map(notice -> new NoticeDTO(notice.getID(), notice.getMessage()))
                .collect(Collectors.toList());
    }

}
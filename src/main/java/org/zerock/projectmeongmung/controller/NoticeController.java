package org.zerock.projectmeongmung.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.projectmeongmung.dto.NoticeDTO;
import org.zerock.projectmeongmung.entity.Notice;
import org.zerock.projectmeongmung.service.NoticeService;
import org.zerock.projectmeongmung.entity.User;
import org.zerock.projectmeongmung.service.UserDetailService;

import java.util.List;

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
    public List<String> getNotices(@RequestParam("uid") String uid) {
        User user = userDetailService.findUserByUid(uid);

        // 메시지 리스트만 가져오기
        return noticeService.getNoticeMessagesByUser(user);
    }
}

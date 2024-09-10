package org.zerock.projectmeongmung.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.projectmeongmung.dto.NoticeDTO;
import org.zerock.projectmeongmung.entity.Notice;
import org.zerock.projectmeongmung.entity.User;
import org.zerock.projectmeongmung.repository.NoticeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;

    // 특정 사용자의 알림 메시지 리스트 가져오기
    public List<String> getNoticeMessagesByUser(User user) {
        return noticeRepository.findMessagesByUser(user);
    }
}

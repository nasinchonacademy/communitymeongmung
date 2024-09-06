package org.zerock.projectmeongmung.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerock.projectmeongmung.dto.AddUserRequest;
import org.zerock.projectmeongmung.dto.SOSBoardCommentDto;
import org.zerock.projectmeongmung.dto.StoryCommentDto;
import org.zerock.projectmeongmung.entity.SOSboardcomment;
import org.zerock.projectmeongmung.entity.StoryComment;
import org.zerock.projectmeongmung.entity.User;
import org.zerock.projectmeongmung.repository.SOSBoardCommentRepository;
import org.zerock.projectmeongmung.repository.SOSboardRepository;
import org.zerock.projectmeongmung.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SOSBoardCommentService {

    private final UserRepository userRepository;
    private final SOSBoardCommentRepository SOSBoardCommentRepository;
    private final SOSboardRepository sosboardrepository;

    public SOSBoardCommentDto Commentread(Long seq){
        //특정 댓글을 가져와서 DTO로 변환
        return SOSBoardCommentRepository.findById(seq)
                .map(this::entityToDto)
                .orElse(null);

    }

    public List<SOSBoardCommentDto> getCommentsByBoardId(Long seq){
        // 특정 게시글에 속한 모든 댓글을 가져와서 DTO로 변환
        List<SOSboardcomment> comment = SOSBoardCommentRepository.findBySosboard_Sosboardseq(seq);
        return comment.stream()
                .map(this ::entityToDto)
                .collect(Collectors.toList());
    }

    public void CommentRegister(StoryCommentDto commentDto) {
        // 댓글 등록 로직
        SOSboardcomment comment = SOSboardcomment.builder()
                .soscommentcontent(commentDto.getCommentcontent())
                .user(userRepository.findById(commentDto.getId())
                        .orElseThrow(() -> new RuntimeException("User not found")))
                .sosboard( sosboardrepository.findById(commentDto.getSeq())
                        .orElseThrow(() -> new RuntimeException("Story not found")))
                .build();

        SOSBoardCommentRepository.save(comment);
    }

    // 엔티티를 DTO로 변환하는 메서드
    private SOSBoardCommentDto entityToDto(SOSboardcomment sosboardcomment) {
        return SOSBoardCommentDto.builder()
                .commentid(sosboardcomment.getCommentid())
                .id(sosboardcomment.getUser().getId())  // User의 ID
                .nickname(sosboardcomment.getUser().getNickname()) // User의 닉네임
                .seq(sosboardcomment.getSosboard().getSosboardseq())  // Story의 seq
                .commentcontent(sosboardcomment.getSoscommentcontent())
                .build();
    }

    public void removeComment(Long commentId) {
       SOSBoardCommentRepository.deleteById(commentId);}

    }

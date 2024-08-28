package org.zerock.projectmeongmung.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerock.projectmeongmung.dto.AddUserRequest;
import org.zerock.projectmeongmung.entity.User;
import org.zerock.projectmeongmung.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class SOSBoardCommentService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long save(AddUserRequest dto) {
        return userRepository.save(User.builder()
                .uid(dto.getUid())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .email(dto.getEmail())
                .name(dto.getName())
                .nickname(dto.getNickname())
                .profilePhoto(String.valueOf(dto.getProfilePhoto()))
                .dogbirthday(dto.getDogbirthday())
                .dogname(dto.getDogname())
                .dogbreed(dto.getDogbreed())
                .dogmeeting(dto.getDogmeeting())
                .termuse(dto.isTermuse())
                .locservice(dto.isLocservice())
                .marketsns(dto.isMarketsns())
                .personalinfo(dto.isPersonalinfo())
                .jellypoint(dto.getJellypoint())
                .build()).getId();

    }

    public boolean checkDuplicateUid(String uid) {
        return userRepository.findByUid(uid).isPresent();
    }

    public boolean checkDuplicateNickname(String nickname) {
        return userRepository.findByNickname(nickname).isPresent();
    }
}

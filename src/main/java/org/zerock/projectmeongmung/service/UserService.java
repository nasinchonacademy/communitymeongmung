package org.zerock.projectmeongmung.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerock.projectmeongmung.dto.AddUserRequest;
import org.zerock.projectmeongmung.entity.User;
import org.zerock.projectmeongmung.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long save(AddUserRequest dto) {
        return userRepository.save(User.builder()
                .uid(dto.getUid())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .email(dto.getEmail())
                .name(dto.getName())
                .nickname(dto.getNickname())
                .profilePhoto(dto.getProfilePhoto())
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

    public void updateUserInfo(User existingUser, User updatedUserData) {
        // 기존 사용자 정보를 새로운 데이터로 업데이트
        User updatedUser = User.builder()
                .uid(existingUser.getUid())  // UID는 변경되지 않도록 기존 값을 유지
                .nickname(updatedUserData.getNickname())
                .dogname(updatedUserData.getDogname())
                .profilePhoto(updatedUserData.getProfilePhoto())
                .dogbirthday(updatedUserData.getDogbirthday())
                .dogbreed(updatedUserData.getDogbreed())
                .build();

        userRepository.save(updatedUser);

    }


}

package org.zerock.projectmeongmung.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.projectmeongmung.dto.AddUserRequest;
import org.zerock.projectmeongmung.entity.User;
import org.zerock.projectmeongmung.service.UserDetailService;
import org.zerock.projectmeongmung.service.UserService;

import java.io.IOException;

@RequiredArgsConstructor
@Controller
public class UserApiController {

    private final UserService userService;
    private final UserDetailService userDetailService;
    private final FileController fileController;

    @PostMapping("/signup1")
    public String handleSignup1(AddUserRequest request, HttpSession session) {
        session.setAttribute("signupData", request);
        return "redirect:/signup2";
    }

    @PostMapping("/signup2")
    public String handleSignup2(@ModelAttribute AddUserRequest request, HttpSession session) {
        AddUserRequest signupData = (AddUserRequest) session.getAttribute("signupData");

        // signup1에서 넘어온 데이터와 signup2에서 받은 데이터를 합쳐 최종적으로 처리
//        signupData.setProfilePhoto(request.getProfilePhoto());
        signupData.setDogbirthday(request.getDogbirthday());
        signupData.setDogname(request.getDogname());
        signupData.setDogmeeting(request.getDogmeeting());
        signupData.setDogbreed(request.getDogbreed());
        signupData.setNickname(request.getNickname());

       ;

        // 프로필 사진을 파일로 저장할 경우에만 처리
        String profilePhotoPath = "";
        MultipartFile profilePhotoFile = request.getProfilePhotoFile();


        if (!profilePhotoFile.isEmpty()) {
            try {
                // 파일 저장 로직을 구현하여 경로를 얻어오는 부분
                profilePhotoPath = fileController.saveProfilePhoto(profilePhotoFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        signupData.setProfilePhoto(profilePhotoPath);

        userService.save(signupData);
        session.removeAttribute("signupData");
        return "redirect:/login"; // 회원가입 성공 페이지로 이동
    }

//    @PostMapping("/user")
//    public String signup(AddUserRequest request) {
//        userService.save(request);
//        return "redirect:/login";
//    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }

    @GetMapping("/api/check-duplicate")
    @ResponseBody
    public boolean checkDuplicate(@RequestParam("uid") String uid) {
        return userService.checkDuplicateUid(uid);
    }

    @GetMapping("/api/check-duplicate/nickname")
    @ResponseBody
    public boolean checkDuplicateNickname(@RequestParam("nickname") String nickname) {
        return userService.checkDuplicateNickname(nickname);
    }


}

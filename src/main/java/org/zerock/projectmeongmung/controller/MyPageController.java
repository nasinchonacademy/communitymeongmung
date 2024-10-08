package org.zerock.projectmeongmung.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.projectmeongmung.dto.MeongStoryDTO;
import org.zerock.projectmeongmung.dto.PageRequestDTO;
import org.zerock.projectmeongmung.dto.PageResultDTO;
import org.zerock.projectmeongmung.entity.Cart;
import org.zerock.projectmeongmung.entity.GamePoints;
import org.zerock.projectmeongmung.entity.MeongStory;
import org.zerock.projectmeongmung.entity.User;
import org.zerock.projectmeongmung.service.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class MyPageController {

    @Autowired
    private MyPageService myPageService;
    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private gameService gameService;
    @Autowired
    private FileController fileController;
    @Autowired
    private MeongStoryService meongStoryService;
    @Autowired
    private CartService cartService;


    @GetMapping("/mypage")
    public String getMyPage(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userDetailService.findUserByUid(username);

        // 좋아요한 스토리 가져오기
        List<MeongStory> likedStories = myPageService.getLikedStories(user);

        // 3개씩 그룹화하여 새로운 리스트 생성
        List<List<MeongStory>> partitionedStories = partitionList(likedStories, 3);


        model.addAttribute("likedStories", likedStories);
        model.addAttribute("partitionedStories", partitionedStories);

        // 젤리 포인트 가져오기
        int jelly = user.getJellypoint();
        model.addAttribute("jelly", jelly);

        // 작성 글 가져오기
        List<MeongStory> writtenStories = myPageService.getWrittenStories(user);
        List<List<MeongStory>> partitionedwrittenStories = partitionList(writtenStories, 5);
//        List<Cart> cartItems = myPageService.getCartItems(user);
        model.addAttribute("partitionedwrittenStories", partitionedwrittenStories);

        // 장바구니 항목 가져오기
        List<Cart> cartItems = cartService.getCartItems(user);

        model.addAttribute("cartItems", cartItems);  // 장바구니 리스트 추가

        return "mypage/mypage";
    }

    private List<List<MeongStory>> partitionList(List<MeongStory> list, int partitionSize) {
        List<List<MeongStory>> partitions = new ArrayList<>();
        for (int i = 0; i < list.size(); i += partitionSize) {
            partitions.add(list.subList(i, Math.min(i + partitionSize, list.size())));
        }
        return partitions;
    }

//    @GetMapping("/api/stories")
//    @ResponseBody
//    public List<MeongStoryDTO> getStories(Authentication authentication) {
//        String username = authentication.getName();
//        User user = userDetailService.loadUserByUsername(username);
//        List<MeongStory> stories = myPageService.getWrittenStories(user);
//
//        // MeongStory를 MeongStoryDTO로 변환
//        return stories.stream()
//                .map(MeongStoryDTO::fromEntity) // or use the lambda expression method
//                .collect(Collectors.toList());
//    }

    // 사용자 정보 수정 페이지를 표시하는 메서드
    @GetMapping("/mypageedit")
    public String showEditPage(Model model,Authentication authentication) {
        String username = authentication.getName();
        User user = userDetailService.findUserByUid(username);
        model.addAttribute("user", user);
        return "mypage/mypage_edit";
    }


    // 사용자 정보를 업데이트하는 메서드
    @PostMapping("/mypageedit")
    public String updateUserInfo(Authentication authentication,
                                 @RequestParam("nickname") String nickname,
                                 @RequestParam("dogname") String dogname,
                                 @RequestParam("profilePhoto") MultipartFile profilePhoto,
                                 @RequestParam(value = "dogbirthday", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dogbirthday,
                                 @RequestParam("dogbreed") String dogbreed)

    {

        // 기존 사용자 정보를 불러오기
        String username = authentication.getName();
        User  existingUser = userDetailService.findUserByUid(username);

        // 프로필 사진을 파일로 저장할 경우에만 처리
        String profilePhotoPath = existingUser.getProfilePhoto();
        if (!profilePhoto.isEmpty()) {
            try {
                // 파일 저장 로직을 구현하여 경로를 얻어오는 부분
                profilePhotoPath = fileController.saveProfilePhoto(profilePhoto);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 업데이트할 새로운 사용자 정보 설정
        userDetailService.updateUser( username, nickname, dogname, profilePhotoPath,dogbreed,dogbirthday);


        // 리다이렉트: 수정 후 다시 마이페이지로 이동
        return "redirect:/mypage?uid=" + username;
    }


    @GetMapping("/mypagejellylist")
    public String showJellyListPage(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userDetailService.findUserByUid(username);

        LocalDate today = LocalDate.now();

        // 게임 목록 정의 (게임 타입과 이름 매핑)
        Map<String, String> games = new LinkedHashMap<>();
        games.put("rsp", "가위바위보");
        games.put("ttt", "틱택톡");
        games.put("roulett", "룰렛");

        // 사용자가 오늘 플레이한 게임 타입 목록 가져오기
        List<String> playedGamesToday = gameService.getPlayedGamesToday(user.getId(), today);

        // 각 게임에 대해 오늘 플레이 여부를 맵에 저장
        Map<String, Boolean> gamePlayedTodayMap = new HashMap<>();
        for (String gameType : games.keySet()) {
            boolean playedToday = playedGamesToday.contains(gameType);
            gamePlayedTodayMap.put(gameType, playedToday);
        }

        model.addAttribute("gamePlayedTodayMap", gamePlayedTodayMap);
        model.addAttribute("games", games);

        // 받은 젤리 내역 추가
        List<GamePoints> gamePoints = gameService.getGamePoints(user);
        model.addAttribute("gamePoints", gamePoints);

        return "mypage/mypagejellylist";
    }

    @GetMapping("/mypapeWritelist")
    public String showWritelistPage(Model model, Authentication authentication,
                                    @RequestParam(value = "page",defaultValue = "1") int page,
                                    @RequestParam(value = "size", defaultValue = "10") int size,
                                    @RequestParam(value = "type", required = false) String type,
                                    @RequestParam(value = "keyword", required = false) String keyword,
                                    PageRequestDTO pageRequestDTO) {

        String username = authentication.getName();
        User user = userDetailService.findUserByUid(username);

        pageRequestDTO.setPage(page);
        pageRequestDTO.setSize(size);
        pageRequestDTO.setType(type);
        pageRequestDTO.setKeyword(keyword);

        PageResultDTO<MeongStoryDTO, MeongStory> result;

        result = myPageService.getWrittenStories(user,pageRequestDTO);

        model.addAttribute("result", result);
        model.addAttribute("pageRequestDTO", pageRequestDTO);

//        List<MeongStory> writtenStories = myPageService.getWrittenStories(user);
//        model.addAttribute("writtenStories", writtenStories);

        return "mypage/mypageWritelist";

    }

    @GetMapping("/mypagelikedlist")
    public String showLikedlistPage(Model model,Authentication authentication,
                                    @RequestParam(value = "page",defaultValue = "1") int page,
                                    @RequestParam(value = "size", defaultValue = "10") int size,
                                    @RequestParam(value = "type", required = false) String type,
                                    @RequestParam(value = "keyword", required = false) String keyword,
                                    PageRequestDTO pageRequestDTO) {

        String username = authentication.getName();
        User user = userDetailService.findUserByUid(username);

        pageRequestDTO.setPage(page);
        pageRequestDTO.setSize(size);
        pageRequestDTO.setType(type);
        pageRequestDTO.setKeyword(keyword);

        PageResultDTO<MeongStoryDTO, MeongStory> result;

        result = myPageService.getLikedStories2(user,pageRequestDTO);

        model.addAttribute("result", result);
        model.addAttribute("pageRequestDTO", pageRequestDTO);

        return "mypage/mypagelikedlist";
    }






}

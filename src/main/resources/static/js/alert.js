function checkLogin() {
    const userConfirmed = confirm('로그인이 필요합니다. 로그인 페이지로 이동하시겠습니까?');
    if (userConfirmed) {
        window.location.href = '/login'; // 여기에 로그인 페이지의 URL을 입력하세요.
    }
    return false;
}
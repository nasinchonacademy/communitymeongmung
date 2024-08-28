
    let isNicknameChecked = false;
    const originalNickname = [[${user.nickname}]];  // 기존 닉네임을 저장

    function checkDuplicateNickname() {
    const nickname = document.getElementById('nickname').value;

    // 입력된 닉네임이 기존 닉네임과 동일한지 확인
    if (nickname === originalNickname) {
    alert('현재 사용 중인 닉네임입니다.');
    isNicknameChecked = true; // 동일하다면 중복 확인이 필요 없으므로 true로 설정
    return;
}

    fetch(`/api/check-duplicate/nickname?nickname=${nickname}`)
    .then(response => response.json())
    .then(data => {
    if (data) {
    alert('이미 사용 중인 닉네임입니다.');
    isNicknameChecked = false; // 중복이면 false
} else {
    alert('사용 가능한 닉네임입니다.');
    isNicknameChecked = true; // 중복이 아니면 true
}
})
    .catch(error => {
    console.error('Error:', error);
});
}

    // 닉네임 필드에 입력이 발생할 때 중복 확인 상태를 초기화
    document.getElementById('nickname').oninput = function() {
    isNicknameChecked = false;
}

    // 닉네임 필드에 입력이 발생할 때 중복 확인 상태를 초기화
    document.getElementById('nickname').oninput = function() {
    isNicknameChecked = false;
}

    function validateForm() {

    const nickname = document.getElementById('nickname').value;


    if (nickname.length < 1 || nickname.length > 10) {
    alert('닉네임은 10글자 이내이어야 합니다.');
    return false;
}

    if (!isNicknameChecked) {
    alert('닉네임 중복 확인을 해주세요.');
    return false;
}

    return true;
}

    var loadFile = function(event) {
    var image = document.getElementById('profileImage');
    var fileName = document.getElementById('fileName');
    var file = event.target.files[0];

    if (file) {
    image.src = URL.createObjectURL(file);
    fileName.textContent = file.name;
} else {
    image.src = "[[${'/image/tool/profilelogo.PNG'}]]";  // 이 부분은 여전히 Thymeleaf 변수로 처리합니다.
    fileName.textContent = '파일이 선택되지 않았습니다';
}
};
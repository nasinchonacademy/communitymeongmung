let isNicknameChecked = false;

function checkDuplicateNickname() {
    const nickname = document.getElementById('nickname').value;

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
    var fileName = document.getElementById('fileName'); // 파일명 표시할 div
    var file = event.target.files[0]; // 선택한 파일 가져오기

    // 이미지 미리보기 업데이트
    image.src = URL.createObjectURL(file);

    // 파일명 업데이트
    fileName.textContent = file ? file.name : '파일이 선택되지 않았습니다';
};
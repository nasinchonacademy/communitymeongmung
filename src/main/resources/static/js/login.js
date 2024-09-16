// 모달 열기 함수
function openModal(modalId) {
    document.getElementById(modalId).style.display = 'block';
}

// 모달 닫기 함수
function closeModal(modalId) {
    document.getElementById(modalId).style.display = 'none';
}

// 현재 모달을 닫고 비밀번호 찾기 모달을 여는 함수
function switchToPasswordModal() {
    closeModal('idResultModal'); // 현재 열려있는 아이디 찾기 결과 모달 닫기
    openModal('passwordModal');   // 비밀번호 찾기 모달 열기
}


// 모달 바깥 영역을 클릭하면 창 닫기
window.onclick = function(event) {
    const idModal = document.getElementById('idModal');
    const passwordModal = document.getElementById('passwordModal');

    if (event.target == idModal) {
        idModal.style.display = 'none';
    } else if (event.target == passwordModal) {
        passwordModal.style.display = 'none';
    }
}

// 아이디 찾기 폼 제출 처리
document.addEventListener("DOMContentLoaded", function() {
    const form = document.getElementById("findIdForm");
    if (form) {
        form.addEventListener("submit", function (event) {
            event.preventDefault(); // 폼의 기본 제출 동작 방지

            const email = document.getElementById("email").value;

            // AJAX 요청
            $.ajax({
                url: '/find-id',
                method: 'POST',
                data: { email: email },
                success: function (response) {
                    console.log("AJAX 요청 성공, uid: " + response);
                    document.getElementById("idResultMessage").textContent = '찾으신 uid는: ' + response+' 입니다.';
                    closeModal('idModal');  // 기존 모달 닫기
                    openModal('idResultModal');  // 결과 모달 열기
                },
                error: function (xhr, status, error) {
                    console.error('해당 이메일로 가입된 uid가 없습니다.');
                }
            });
        });
    } else {
        console.error('findIdForm 요소를 찾을 수 없습니다.');
    }
});

// 비밀번호 찾기 폼 제출 처리
document.addEventListener('DOMContentLoaded', function () {
    const passwordResetForm = document.getElementById('passwordResetForm');

    if (passwordResetForm) {
        passwordResetForm.addEventListener('submit', function (event) {
            event.preventDefault(); // 기본 폼 제출 방지

            // 폼 데이터 가져오기
            const name = document.getElementById('name').value;
            const email = document.getElementById('reset-email').value;

            // 제출 버튼을 비활성화하고 로딩 중 표시
            const submitButton = passwordResetForm.querySelector('.submit-btn');
            submitButton.disabled = true;
            submitButton.textContent = '로딩중...';
            submitButton.insertAdjacentHTML('beforeend', ' <span class="loader"></span>'); // 로딩 아이콘 추가

            // 비밀번호 찾기 요청을 서버에 전송 (AJAX)
            $.ajax({
                url: '/request-reset-password',  // 서버의 비밀번호 재설정 요청 처리 URL
                method: 'POST',
                data: { name: name, email: email },
                success: function (response) {
                    // 성공 시 메시지를 설정하고 결과 모달을 엽니다
                    document.getElementById('resetPasswordResultMessage').textContent = response;
                    closeModal('passwordModal');  // 비밀번호 찾기 모달 닫기
                    openModal('resetPasswordResultModal');  // 결과 모달 열기
                },
                error: function (xhr) {
                    // 실패 시 오류 메시지를 설정하고 결과 모달을 엽니다
                    document.getElementById('resetPasswordResultMessage').textContent = xhr.responseText || '비밀번호 찾기 요청에 실패했습니다.';
                    closeModal('passwordModal');  // 비밀번호 찾기 모달 닫기
                    openModal('resetPasswordResultModal');  // 결과 모달 열기
                },
                complete: function () {
                    // 로딩 상태 해제
                    submitButton.disabled = false;
                    submitButton.textContent = '비밀번호 찾기'; // 원래 텍스트로 복구
                }
            });
        });
    }
});

// URL에서 파라미터 값을 가져오는 함수
function getParameterByName(name) {
    const url = window.location.href;
    name = name.replace(/[\[\]]/g, '\\$&');
    const regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)');
    const results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, ' '));
}

// 메시지 처리
const message = getParameterByName('message');

// message에 따라 alert 표시
if (message === 'success') {
    alert('비밀번호가 성공적으로 변경되었습니다. 다시 로그인 해주세요.');
} else if (message === 'failure') {
    alert('비밀번호 재설정에 실패했습니다. 다시 시도해주세요.');
}
let isUidChecked = false;

        function checkDuplicate() {
            const uid = document.getElementById('uid').value;

            fetch(`/api/check-duplicate?uid=${uid}`)
                .then(response =>{ const contentType = response.headers.get('Content-Type');
                        if (contentType && contentType.includes('application/json')) {
                            return response.json();
                        } else {

                            throw new Error('JSON 형식의 응답을 받지 못했습니다.');
                        }
                    })
                .then(data => {
                    if (data) {
                        alert('이미 사용 중인 아이디입니다.');
                        isUidChecked = false; // 중복이면 false
                    } else {
                        alert('사용 가능한 아이디입니다.');
                        isUidChecked = true; // 중복이 아니면 true
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('해당 아이디는 사용이 불가능합니다.');
                });
        }

        function validateForm() {

            const uid = document.getElementById('uid').value;
            const password = document.getElementById('password').value;
            const name = document.getElementById('name').value;
            const email = document.getElementById('email').value;

            // 아이디 검증: 4자 이상, 20자 이하, 알파벳과 숫자 조합 허용
            const uidPattern = /^[a-zA-Z0-9]{4,20}$/;
            if (!uidPattern.test(uid)) {
                alert('아이디는 4자 이상 20자 이하의 영문자와 숫자 조합이어야 합니다.');
                return false;
            }

            // 비밀번호 검증: 8자 이상, 20자 이하, 최소 하나의 문자, 숫자, 특수 문자 포함
            const passwordPattern = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,20}$/;
            if (!passwordPattern.test(password)) {
                alert('비밀번호는 8자 이상 20자 이하이어야 하며, 최소 하나의 문자, 숫자, 특수 문자를 포함해야 합니다.');
                return false;
            }


            const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailPattern.test(email)) {
                alert('유효한 이메일 주소를 입력하세요.');
                return false;
            }


            if (name.length < 1 || name.length > 10) {
                alert('이름은 10글자 이내이어야 합니다.');
                return false;
            }

            if (!isUidChecked) {
                alert('아이디 중복 확인을 해주세요.');
                return false;
            }

            return true;
        }

        window.onload = function () {
            const modal = document.getElementById('termsModal');
            const nextBtn = document.querySelector('.next-btn');
            const closeBtn = document.querySelector('.close');
            const agreeBtn = document.getElementById('agree-btn');

            nextBtn.addEventListener('click', function(event) {
                 event.preventDefault();
                let validity = validateForm();
                if(validity===true){
                    modal.style.display = "block";
                }
                else {
                    return;
                }

            });

            closeBtn.addEventListener('click', function() {
                modal.style.display = "none";
            });

            agreeBtn.addEventListener('click', function() {
                const terms1 = document.getElementById('terms1Checkbox').checked;
                const terms2 = document.getElementById('terms2Checkbox').checked;

                if (!terms1 || !terms2) {
                    alert('필수 약관에 동의하셔야 합니다.');
                } else {
                    // 필수 약관 동의 여부 값을 폼에 반영
                    document.getElementById('terms1').value = terms1;
                    document.getElementById('terms2').value = terms2;
                    document.getElementById('terms3').value = document.getElementById('terms3Checkbox').checked;
                    document.getElementById('terms4').value = document.getElementById('terms4Checkbox').checked;

                    modal.style.display = "none";
                    document.querySelector('form').submit();
                }
            });

            window.addEventListener('click', function(event) {
                if (event.target == modal) {
                    modal.style.display = "none";
                }
            });
        }
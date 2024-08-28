
$(document).ready(function() {
    // 페이지 로드 시 체크 상태에 따라 함수 실행
    if ($("#btnradio1").is(":checked")) {
        $("#sosPage").load("/mypage");
    } else if ($("#btnradio2").is(":checked")) {
        $("#sosPage").load("mypageedit");
    }
});

    $(document).ready(function() {
        // 라디오 버튼 클릭 시 해당 href로 이동
        $("#btnradio1").on("click", function() {
            window.location.href = "/mypage";
        });

        $("#btnradio2").on("click", function() {
            window.location.href = "/mypageedit?isRadio2Selected=true";
        });
    });


window.addEventListener('DOMContentLoaded', (event) => {
    const urlParams = new URLSearchParams(window.location.search);
    const isRadio2Selected = urlParams.get('isRadio2Selected');

    if (isRadio2Selected === 'true') {
        document.getElementById('btnradio2').checked = true;
    } else {
        document.getElementById('btnradio1').checked = true;
    }
});
$(document).ready(function() {
    // 페이지 로드 시 체크 상태에 따라 함수 실행
    if ($("#btnradio1").is(":checked")) {
        $("#sosPage").load("/soshospitallist/content1");
    } else if ($("#btnradio2").is(":checked")) {
        $("#sosPage").load("/soshospitallist/content2");
    }

    // 라디오 버튼 클릭 시 해당 컨텐츠 로드
    $("#btnradio1").on("click", function() {
        $("#sosPage").load("/soshospitallist/content1");
    });

    $("#btnradio2").on("click", function() {
        $("#sosPage").load("/soshospitallist/content2");
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


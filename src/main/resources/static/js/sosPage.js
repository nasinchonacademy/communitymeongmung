$(document).ready(function() {
    // 페이지 로드 시 URL 파라미터에 따라 라디오 버튼 상태를 설정하고 해당 콘텐츠 로드
    const urlParams = new URLSearchParams(window.location.search);
    const isRadio2Selected = urlParams.get('isRadio2Selected');

    if (isRadio2Selected === 'true') {
        $("#btnradio2").prop("checked", true);
        $("#sosPage").load("/soshospitallist/content2");
    } else {
        $("#btnradio1").prop("checked", true);
        $("#sosPage").load("/soshospitallist/content1");
    }

    // 라디오 버튼 클릭 시 URL을 변경하고 해당 콘텐츠 로드
    $("#btnradio1").on("click", function() {
        window.history.pushState(null, null, "/soshospitallist");
        $("#sosPage").load("/soshospitallist/content1");
    });

    $("#btnradio2").on("click", function() {
        window.history.pushState(null, null, "/soshospitallist?isRadio2Selected=true");
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


$(document).ready(function() {
    // 라디오 버튼 클릭 시 해당 href로 이동
    $("#btnradio1").on("click", function() {
        window.location.href = "/soshospitallist";
    });

    $("#btnradio2").on("click", function() {
        window.location.href = "/soshospitallist?isRadio2Selected=true";
    });
});

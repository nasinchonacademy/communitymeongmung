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

$(document).ready(function () {
    // 검색 폼 서브밋 이벤트를 가로챔
    $('#searchForm').submit(function (e) {
        e.preventDefault();  // 폼의 기본 동작(새 페이지로 이동)을 막음

        const keyword = $('input[name="keyword"]').val();  // 입력한 검색어 가져오기
        const url = '../soshospitallist/content2';  // AJAX 요청 URL

        // AJAX 요청
        $.ajax({
            url: url,
            type: 'GET',
            data: {
                keyword: keyword,
                page: 1  // 검색 시 첫 페이지로 설정
            },
            success: function (response) {
                // 성공 시, #sosPage 영역을 새 콘텐츠로 교체
                $('#sosPage').html(response);
            },
            error: function (error) {
                console.error('검색 중 오류 발생:', error);
            }
        });
    });

    // 페이징 버튼 클릭 이벤트도 AJAX로 처리
    $(document).on('click', '.page-click', function (e) {
        e.preventDefault();  // 링크 클릭 시 페이지 새로고침 방지

        const url = $(this).attr('href');  // 클릭한 링크의 href 가져오기

        // AJAX 요청
        $.ajax({
            url: url,
            type: 'GET',
            success: function (response) {
                $('#sosPage').html(response);  // 성공 시, #sosPage 영역을 새 콘텐츠로 교체
            },
            error: function (error) {
                console.error('페이지 로딩 중 오류 발생:', error);
            }
        });
    });
});
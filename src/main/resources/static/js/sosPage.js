$(document).ready(function() {
    // URL 파라미터에서 current 값을 추출
    function getCurrentFromUrl() {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get('current') || 'radio1';  // 기본값을 'radio1'로 설정
    }

    // 페이지 로드 시 URL의 current 값에 따라 라디오 버튼 상태 설정
    function setRadioButtonState(currentInput) {
        // current 값에 따라 라디오 버튼 상태 설정
        if (currentInput === 'radio1') {
            $('#btnradio1').prop('checked', true);  // radio1 버튼 활성화
            $('#btnradio2').prop('checked', false); // radio2 버튼 비활성화
        } else if (currentInput === 'radio2') {
            $('#btnradio2').prop('checked', true);  // radio2 버튼 활성화
            $('#btnradio1').prop('checked', false); // radio1 버튼 비활성화
        }

        // 라벨의 활성화 상태도 변경 (CSS 스타일 적용)
        $('label.btn').removeClass('active');
        $('label[for="btnradio' + currentInput + '"]').addClass('active');
    }

    // 페이지 로드 시 라디오 버튼 상태 설정
    const initialCurrent = getCurrentFromUrl();
    setRadioButtonState(initialCurrent);

    // 라디오 버튼 변경 시 처리
    $('input[name="btnradio"]').on('change', function() {
        let selectedRadio = $('input[name="btnradio"]:checked').attr('id');
        let currentInput = selectedRadio === 'btnradio1' ? 'radio1' : 'radio2';

        // 페이지 URL 변경
        let url = `/soshospitallist?current=${currentInput}&page=1`;

        // URL을 새로고침 없이 변경
        window.history.pushState(null, '', url);

        // AJAX 요청을 통해 페이지 변경
        loadContent(url, currentInput);  // currentInput 값 유지
    });

    // 검색 폼 제출 시 처리
    $('#searchForm').on('submit', function(e) {
        let keyword = $(this).find('input[name="keyword"]').val();
        let currentInput = getCurrentFromUrl(); // 현재 URL에서 current 값을 추출

        // URL에서 current 값을 올바르게 설정
        let url = `/soshospitallist?current=${currentInput}&keyword=${encodeURIComponent(keyword)}&page=1`;

        // URL을 새로고침 없이 변경
        window.history.pushState(null, '', url);

        // AJAX 요청으로 검색 결과 로드
        loadContent(url, currentInput);  // currentInput 값 유지
    });

    // 페이지네이션 클릭 시 처리
    $(document).on('click', '.pagination a', function(e) {
        e.preventDefault();
        let page = $(this).data('page');
        let keyword = $('input[name="keyword"]').val();
        let currentInput = getCurrentFromUrl(); // URL에서 current 값 추출

        // URL에서 current 값을 올바르게 설정
        let url = `/soshospitallist?current=${currentInput}&keyword=${encodeURIComponent(keyword)}&page=${page}`;

        // URL을 새로고침 없이 변경
        window.history.pushState(null, '', url);

        // AJAX로 페이지 및 검색 결과 로드
        loadContent(url, currentInput);  // currentInput 값 유지
    });

    // 콘텐츠 로드 함수 (AJAX 요청 및 라디오 버튼 상태 유지)
    function loadContent(url, currentInput) {
        $.ajax({
            url: url,
            type: 'GET',
            success: function(response) {
                $('#sosPageContent').html($(response).find('#sosPageContent').html()); // 콘텐츠 영역만 변경

                // AJAX 요청 후 current 값에 따라 라디오 버튼 상태 유지
                setRadioButtonState(currentInput);  // currentInput 값을 기반으로 라디오 버튼 상태를 다시 설정
            },
            error: function(xhr, status, error) {
                console.error('Error loading content:', error);
            }
        });
    }

    // 브라우저의 뒤로 가기/앞으로 가기 처리 (페이지 및 라디오 버튼 상태 유지)
    window.onpopstate = function(event) {
        let current = getCurrentFromUrl();
        let page = new URLSearchParams(window.location.search).get('page') || 1;

        let url = `/soshospitallist?current=${current}&page=${page}`;

        // AJAX로 콘텐츠 로드
        loadContent(url, current);  // current 값 유지
    };
});

// kakao 지도 API를 불러오는 함수입니다.
function loadKakaoMapScript(callback) {
    var script = document.createElement('script');
    script.type = 'text/javascript';
    script.src = '//dapi.kakao.com/v2/maps/sdk.js?appkey=';
    script.onload = callback;
    document.head.appendChild(script);
}

// 지도를 초기화하는 함수입니다.
function initMap() {
    var mapContainer = document.getElementById('map'); // 지도를 표시할 div
    var mapOption = {
        center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
        level: 3 // 지도의 확대 레벨
    };

    var map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다

    // 지도를 클릭했을 때 클릭한 위치에 마커를 추가하도록 지도에 클릭 이벤트를 등록합니다
    kakao.maps.event.addListener(map, 'click', function(mouseEvent) {
        addMarker(mouseEvent.latLng); // 클릭한 위치에 마커를 표시합니다
    });

    // 지도의 마커들을 저장할 배열입니다.
    var markers = [];

    // 초기 마커를 추가합니다.
    addMarker(new kakao.maps.LatLng(33.450701, 126.570667));

    // 마커를 생성하고 지도 위에 표시하는 함수입니다.
    function addMarker(position) {
        var marker = new kakao.maps.Marker({
            position: position
        });

        marker.setMap(map); // 마커를 지도 위에 표시합니다
        markers.push(marker); // 생성된 마커를 배열에 추가합니다
    }

    // 배열에 추가된 마커들을 지도에 표시하거나 삭제하는 함수입니다.
    function setMarkers(map) {
        for (var i = 0; i < markers.length; i++) {
            markers[i].setMap(map);
        }
    }

    // "마커 보이기" 버튼 클릭 시 호출되어 배열에 추가된 마커를 지도에 표시하는 함수입니다.
    window.showMarkers = function() {
        setMarkers(map);
    };

    // "마커 감추기" 버튼 클릭 시 호출되어 배열에 추가된 마커를 지도에서 삭제하는 함수입니다.
    window.hideMarkers = function() {
        setMarkers(null);
    };
}

// DOM이 로드된 후 kakao 지도 API를 불러오고 지도를 초기화합니다.
document.addEventListener('DOMContentLoaded', function() {
    loadKakaoMapScript(initMap);
});
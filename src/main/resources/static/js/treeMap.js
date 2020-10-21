

// 지도 관련 자바스크립트
let container = document.getElementById('map'); //지도를 담을 영역의 DOM 레퍼런스
let options = { //지도를 생성할 때 필요한 기본 옵션
    center: new kakao.maps.LatLng(36.377938, 128.145146), //지도의 중심좌표.
    level: 3 //지도의 레벨(확대, 축소 정도)
};


let map = new kakao.maps.Map(container, options); //지도 생성 및 객체 리턴

let positions = new Array();


var clusterer = new kakao.maps.MarkerClusterer({
    map: map, // 마커들을 클러스터로 관리하고 표시할 지도 객체
    averageCenter: true, // 클러스터에 포함된 마커들의 평균 위치를 클러스터 마커 위치로 설정
    minLevel: 7 // 클러스터 할 최소 지도 레벨
});

for(var i=0; i<treePosArray.length; i++)
    positions.push({
        title: treePosArray[i][0],
        latlng: new kakao.maps.LatLng(treePosArray[i][1], treePosArray[i][2])
    }); //마커표시 위치 지정
// 마커를 생성합니다

var imageSrc = "https://www.flaticon.com/svg/static/icons/svg/1497/1497192.svg";
var markers = new Array();
// 마커 이미지의 이미지 크기 입니다
var imageSize = new kakao.maps.Size(24, 35);
// 마커 이미지를 생성합니다
var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);

// 좌표정보들 data에 넣고 각 data요소를 position이라고 놓고 각각요소들에 대해 마커요소 만들어서 클러스터에 더해주기
$.get("/webfist/", function(data) {
    // 데이터에서 좌표 값을 가지고 마커를 표시합니다
    // 마커 클러스터러로 관리할 마커 객체는 생성할 때 지도 객체를 설정하지 않습니다
    var markers = $(data).map(function(i, position) {

        return new kakao.maps.Marker({
            position : new kakao.maps.LatLng(position.latitude, position.longitude),
            image : markerImage
        });
    });

    // 클러스터러에 마커들을 추가합니다

    clusterer.addMarkers(markers);
});

// 지도화면의 중심점을 최근 마지막 저장위치로 세팅
const idx = positions.length-1;
map.setCenter(positions[idx].latlng);


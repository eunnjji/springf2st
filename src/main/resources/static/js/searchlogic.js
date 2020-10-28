
var spbtn = document.getElementById('search-place-btn');
var resultDiv = document.getElementById('resultdiv');
var tag, place;

var chk = false;

// 주소-좌표 변환 객체를 생성합니다
var geocoder = new kakao.maps.services.Geocoder();

spbtn.onclick = function (){
    resultDiv.innerHTML = '';
    // var optname = $('#toggleSearchName').text();
    var optname = document.getElementById('toggleSearchName').innerText;
    switch (optname) {
        case "지역": tag = 1; break;
        case "시": tag = 2; break;
        case "동(면)": tag = 3; break;
    }
    place = document.getElementById("inputPlaceKeyword").value;
    //alert('test '+tag+place);

    var content = '<h3>'+place+' '+optname+' 검색 결과</h3>';
    //#방법2
    tArray.forEach(function (tmp, i){
        var tname = tmp[0];
        geocoder.coord2Address(tmp[2],tmp[1],function(result, status){
           if(status === kakao.maps.services.Status.OK){
               switch (tag) {
                   case 1:
                       if(place === result[0].address.region_1depth_name){
                           resultArr.push(tname);
                           content += '<li class="list-group-item"><a href="/webfist/map/'+tname+'">'+ tname +'</a></li>';
                       }
                       break;
                   case 2:
                       if(place === result[0].address.region_2depth_name){
                           resultArr.push(tname);
                           content += '<li class="list-group-item"><a href="/webfist/map/'+tname+'">'+ tname +'</a></li>';
                       }
                       break;
                   case 3:
                       if(place === result[0].address.region_3depth_name)
                       {
                           resultArr.push(tname);
                           content += '<li class="list-group-item"><a href="/webfist/map/'+tname+'">'+ tname +'</a></li>';
                       }
                       break;
               }
               resultDiv.innerHTML = '<ul class="list-group">'+content+'</ul>';
           }
        });
    });

    resultArr.length = 0;
}

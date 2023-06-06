import * as View from "./view.js";
import * as Request from "./request.js";
// JSON.stringify() => 객체를 string 으로, JSON.parse() => string 을 객체로 만듦


// 검색창 입력 후 엔터키 => 검색
$("#ticket-search-keyword").keyup(e => {
	if (e.keyCode == 13) {
		selectedTicketList();
		e.preventDefault();
	}
});

/********* function ********/
function selectedTicketList(){
    let keyword=$('#ticket-search-keyword').val();
    let cityNo;
    let sortOrder=$('#ticket-sort').val();

    if($('#city-checkbox:checked').length==1){
        cityNo=$('#city-checkbox:checked').val();
    } else{
        cityNo=0;
    }
    console.log(cityNo);

    let url= 'ticket-list-sort';
    let method='POST';
    let contentType="application/json;charset=UTF-8";
    let sendData={
        currentPage:"1",
        keyword:keyword,
        cityNo:cityNo,
        sortOrder:sortOrder
    };
    let async=true;
    Request.ajaxRequest(url,method,contentType,
        JSON.stringify(sendData),
        function(resultJson){
            if(resultJson.code==1){
                View.render('#ticket-search-list-template',resultJson,'#ticket-list')
            } else{
                alert(resultJson.msg);
            }
        },async);
};


$(document).on('change','#ticket-sort,#city-checkbox',selectedTicketList);
$(document).on('click',"#ticket-search-btn",selectedTicketList);

$('input[type="checkbox"][name="city-checkbox"]').click(function(){
	$(".pagination").show();
    if($(this).prop('checked')){
        $('input[type="checkbox"][name="city-checkbox"]').prop('checked',false);
        $(this).prop('checked',true);
        $(".pagination").hide();
    }
});

$('input[type="checkbox"]').on('click', function() {
	
})

$(document).on('show.bs.modal','#myReview',function(){
    let loginUser=$('#myReviewBtn').data('id');
    let tiNo=$('#myReviewBtn').data('tino');
    $('#myReview').find('#userId').val(loginUser);
    $('#myReview').find('#hiddenTiNo').val(tiNo); // input에 hidden으로 들어가있는 id 값 vlaue에 
    
    $("#ticket-review-modify-action").hide();
	$("#ticket-review-action").show();
    //console.log(loginUser); // id....
    //console.log(tiNo);
});

$(document).on('hidden.bs.modal','#myReview',function(){
    $("#tiReviewTitle").val("");
    $("#tiReviewContent").val("");
});

/***** 티켓 리뷰 작성 *****/

$('#ticket-review-action').click(function(e){
    let tiReviewImg = $('#tiReviewImg')[0].files[0]; //multi-part
    let formData = new FormData();
    if(tiReviewImg){
        formData.append('file', tiReviewImg);
    } else {
		formData.append('file',null);
	}
    $.ajax({
        type: 'POST',
        url: 'upload',
        data: formData,    
        processData: false,
        contentType: false
    })
    .done(function (data) {
        let sendData = {
            tiNo:$('#hiddenTiNo').val(),
            tiReviewTitle:document.ticketReview.tiReviewTitle.value,
            tiReviewStar:document.ticketReview.tiReviewStar.value,
            tiReviewContent:document.ticketReview.tiReviewContent.value,
            tiReviewImg: data, // url 뒷부분 안넘어옴
            userId:document.ticketReview.userId.value
        };
        Request.ajaxRequest('ticket-review-action', 
                            'POST', 
                            'application/json;charset=UTF-8', 
                            JSON.stringify(sendData), 
                            function(resultJson) {
           	let tiNo = $('#hiddenTiNo').val();
            if(resultJson.code==1){
              	window.location.href=`ticket-detail?tiNo=${tiNo}`;
               //View.render("#ticketReview-template", resultJson,'#ticketReviewDiv')
            } else {
                alert(resultJson.msg);
            }
        }   
        , true);
        
    })
    
    .fail(function (error) {
        alert(error);
    })
    e.preventDefault();
});


/*티켓 리뷰 삭제 삭제*/
$('.deleteTiReviewBtn').click(function(e) {
	let tiReviewNo = $(e.target).attr('tiReviewNo');
	//console.log(tiReviewNo); // 61
	Request.ajaxRequest('ticketReview/'+ tiReviewNo,
						'DELETE',
						'application/json;charset=UTF-8',
						{},
						function(resultJson){
							/*url, method, contentType, sendData, callBackFunction, async*/
							if(resultJson.code==1){
								let tiNo = $('#hiddenTiNo').val();
								window.location.href=`ticket-detail?tiNo=${tiNo}`;
								//View.render("#ticketReview-template",resultJson,"#ticketReviewDiv")
							} else {
								alert(resultJson.msg);
							}
						},
						true);
	e.preventDefault();
	
});



/***** 티켓 수정 모달 ***

$('#myReview').on('show.bs.modal', function(e) {
  let tiReviewNo = $("#modifyTiReviewBtn").attr("modifyTiReviewNo"); //  리뷰넘버
  console.log(tiReviewNo); // 확인용

});*/
//modifyTiReviewBtn
// $(document).on('show.bs.modal','#myReview',function(e) { 
//$(document).on('show.bs.modal','#myReview',function(e){
$(".modifyTiReviewBtn").click(function(e) {
	$('#myReview').modal('show');
	$("#ticket-review-modify-action").show();
	$("#ticket-review-action").hide();
	
	let tiReviewNo = $(e.target).attr('tiReviewNo');
	let f = 'f' + tiReviewNo; // 유니크 tiReviewNo
	//console.log(f);
	let form = document.getElementsByName(f);
	//console.log(form)
	let title = form[0].children.namedItem('tiReviewTitle').innerText; // 폼에서 가져오기..
	let content = form[0].children.namedItem('tiReviewContent').innerText;
	//console.log(title);
	//console.log(content);
	$('#myReview').find('#tiReviewNo').val(tiReviewNo);
    $('#myReview').find('#tiReviewTitle').val(title); 
    $('#myReview').find('#tiReviewContent').val(content);
    //console.log(loginUser); // id....
    //console.log(tiNo);
});

    
$('#ticket-review-modify-action').click(function(e){
		//console.log(e);
	    let tiReviewImg = $('#tiReviewImg')[0].files[0]; //multi-part
	    let formData = new FormData();
		    if(tiReviewImg){
		        formData.append('file', tiReviewImg);
		    } else {
				formData.append('file',null);
			}
		let tiReviewNo = $('#tiReviewNo').val();
		//console.log(formData);
	    $.ajax({
	        type: 'POST',
	        url: 'upload',
	        data: formData,    
	        processData: false,
	        contentType: false
	    })
	    .done(function (data) {
	        let sendData = {
				tiReviewNo:tiReviewNo,
	            tiReviewTitle:document.ticketReview.tiReviewTitle.value,
	            tiReviewStar:document.ticketReview.tiReviewStar.value,
	            tiReviewContent:document.ticketReview.tiReviewContent.value,
	            tiReviewImg: data, // url 뒷부분 안넘어옴
	            userId:document.ticketReview.userId.value
	        };
	        //console.log(sendData);
	        Request.ajaxRequest('ticketReview/'+ tiReviewNo, 
	                            'PUT', 
	                            'application/json;charset=UTF-8', 
	                            JSON.stringify(sendData), 
	                            function(resultJson) {
	            if(resultJson.code==1){
		            let tiNo = $('#hiddenTiNo').val();
	                window.location.href=`ticket-detail?tiNo=${tiNo}`;
	            } else {
	                alert(resultJson.msg);
	            }
	        }   
	        , true);
	        
	    })
	    
	    .fail(function (error) {
	        alert(error);
	    })
	    e.preventDefault();
});
    
    
    
/* test 
$(document).ready(function() {
	
  $('.box_style_1 expose').after('<script>$("#ticketReviewDiv").hide();</script>');
});*/

$(document).on('click', '#usePointBtn', function(e) {
  let usedPoint = $('#usedPoint').val(); // 사용할 포인트 입력값
  let nowPoint = $('#nowPoint').val(); // 세션에 담긴 현재 로그인유저 포인트
  let totPrice = $('#totPrice').val(); // 총 금액
  
  let nowPoint2 = nowPoint - usedPoint; // 유저포인트 - 사용포인트
  let totPrice2 = totPrice - usedPoint; // 총 금액 - 사용포인트
  let savePoint2 = totPrice2 * 0.01; // 적립 포인트

  // 현재 포인트가 1000 미만인 경우 버튼을 비활성화 합니다.
  if (nowPoint < 1000) {
    $('#usePointBtn').prop('disabled', true);
  }
  
  document.getElementById('nowPoint2').innerHTML = nowPoint2;
  document.getElementById('usingPoint2').innerHTML = usedPoint;
  document.getElementById('totPrice2').innerHTML = totPrice2;
  document.getElementById('savePoint2').innerHTML = savePoint2;
  $('#pPrice').val(totPrice2);
  $('#pPoint').val(savePoint2 - usedPoint);
  e.preventDefault();
});









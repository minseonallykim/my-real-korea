import * as View from "./view.js";
import * as Request from "./request.js";
// JSON.stringify() => 객체를 string 으로, JSON.parse() => string 을 객체로 만듦 

// 검색창 입력 후 엔터키 => 검색
$("#tour-search-keyword").keyup(e => {
	if (e.keyCode == 13) {
		selectedTourList();
		e.preventDefault();
	}
});

/********* function ********/
//조건 넣어서 리스트 출력하기
function selectedTourList() {
	let keyword = $('#tour-search-keyword').val();
	let cityNo;
	let toType;
	let sortOrder = $('#sort-by').val();

	if ($('#city-checkbox:checked').length == 1) {
		cityNo = $('#city-checkbox:checked').val();
	} else {
		cityNo = 0;
	}
	if ($('input[name="toType-checkbox"]:checked').length == 1) {
		toType = $('input[name="toType-checkbox"]:checked').val();
	} else {
		toType = 0;
	}

	let url = 'tour-list-ajax';
	let method = 'POST';
	let contentType = 'application/json;charset=UTF-8';
	let sendData = {
		pageNo: "1",
		keyword: keyword,
		cityNo: cityNo,
		toType: toType,
		sortOrder: sortOrder
	};
	let async = true;

	Request.ajaxRequest(url, method, contentType,
		JSON.stringify(sendData),	//json으로 보낼 때 전부 string화 해 줘야 한다.
		function(resultJson) {
			//code=1 성공 -> render , 아닐때 msg
			if (resultJson.code == 1) {
				View.render('#tour-search-list-template', resultJson, '#tour-list')
			} else {
				alert(resultJson.msg);
			}
		}, async);
};
// 로그인상태면 모달창 뜰 때  내 이름에 아이디 넣어주기
$(document).on('show.bs.modal', '#myReviewModal', function() {
	let userId = $('#myReviewBtn').data('id');
	$('#myReviewModal').find('#userIdM').val(userId);
});
//모달창 닫힐 때 내용 다 지워주기
$(document).on('click', '#myReviewBtn', function() {
	$('#toReviewNoM').val('0');
	$('#toReviewTitleM').val('');
	$('#toReviewStarM').val('');
	$('#toReviewContentM').val('');
});


//1. 검색, 필터, 정렬 한 투어리스트 
$(document).on('change', '#sort-by,#city-checkbox,#toType-checkbox', selectedTourList);
$(document).on('click', "#tour-search-btn", selectedTourList);


//2. 체크박스 하나만 선택하기
$('input[type="checkbox"][name="city-checkbox"]').click(function() {
	if ($(this).prop('checked')) {
		$('input[type="checkbox"][name="city-checkbox"]').prop('checked', false);
		$(this).prop('checked', true);
	}
});
$('input[type="checkbox"][name="toType-checkbox"]').click(function() {
	if ($(this).prop('checked')) {
		$('input[type="checkbox"][name="toType-checkbox"]').prop('checked', false);
		$(this).prop('checked', true);
	}
});

//3. 투어리뷰 남기기
$(document).on('click', '#submitReviewBtn', function(e) {

	/****************** 1. form으로 전달하는 방법 *************/
	let form = $('#tourReviewF');
	let toReviewNo = $('#toReviewNoM').val();		//--> 왜자꾸 undefined?
	let url;
	let method;
	if (form.get(0).checkValidity() === false) {
		//폼 유효성 검사 실패시 
		form.find(':invalid').first().focus();	//폼 유효성 검사 실패한 요소에 커서 두기
		//이벤트 중지
		e.preventDefault();
		e.stopPropagation();
	} else {
		//폼 유효성 검사 성공시
		if (toReviewNo == 0) {
			url = "tour-review-action";
			method = "POST";
		} else {
			url = "tour-review-update";
			method = "PUT";
		}
		let async = true;
		let formData = new FormData();
		let toReviewImg;
		formData.append('toReviewNo', toReviewNo);
		formData.append('toNo', $('#toNoM').val());
		formData.append('userId', $('#userIdM').val());
		formData.append('toReviewTitle', $('#toReviewTitleM').val());
		formData.append('toReviewStar', $('#toReviewStarM').val());
		formData.append('toReviewContent', $('#toReviewContentM').val());

		let uploadImg = $('#toReviewUploadM');
		//이미지 업로드시 & 업로드 안할 시 나눠서 처리함 
		if (uploadImg.prop('files') && uploadImg.prop('files').length > 0) {
			//파일 업로드시 업로드한 이미지 폼데이터에 추가 
			formData.append('file', uploadImg.prop('files')[0]);
		} else {
			//파일 선택 안되면 hidden으로 넣은 toReviewImg 선택해서 폼데이터에 추가
			//toReviewImg=$('toReviewImgM').val();
			formData.append('toReviewImg', toReviewImg);
		}
		$('#myReviewModal').modal('hide');	//모달 창 닫기

		$.ajax({
			method: method,
			url: url,
			data: formData,
			processData: false,
			contentType: false,
			success: function(resultJson) {
				if (resultJson.code == 1) {
					//window.location.href=`tour-detail?toNo=${formData.get('toNo')}`;
					View.render("#tourReview-template", resultJson, '#tourReviewDiv');
				} else {
					alert(resultJson.msg);
				}
			},
			error: function(err) {
				alert(err.status);
			},
			async: async
		});
		//$('#toReviewNoM').val("0");
		e.preventDefault();	////폼 제출 방지
	}
});


//4. 투어리뷰 삭제
$(document).on('click', "button[name='deleteToReview']", function(e) {
	let urlSearchParams = new URLSearchParams(window.location.search);
	let toNo = urlSearchParams.get('toNo');
	let toReviewNo = e.target.value;
	let url = `tour-review-delete/${toNo}/${toReviewNo}`;
	let method = 'DELETE';
	let contentType = 'application/json;charset=UTF-8';
	let sendData = {};
	let async = true;
	Request.ajaxRequest(url, method, contentType,
		JSON.stringify(sendData),	//json으로 보낼 때 전부 string화 해 줘야 한다.
		function(resultJson) {
			//code=1 성공 -> render , 아닐때 msg
			if (resultJson.code == 1) {
				//window.location.href=`tour-detail?toNo=${toNo}`;
				View.render("#tourReview-template", resultJson, '#tourReviewDiv');
			} else {
				alert(resultJson.msg);
			}
		}, async);
	e.preventDefault();
});

//5. 투어리뷰 수정
//4-1. 수정 모달 띄우기
$(document).on('click', "button[name='updateToReview']", function(e) {
	e.preventDefault();
	$('#myReviewModal').modal('show');
	let selectedForm = e.target.closest('form');
	let toReviewTitle = selectedForm.querySelector('#toReviewTitle').textContent;
	let toReviewStar = selectedForm.querySelector('#toReviewStar').value;
	console.log(toReviewStar);
	let toReviewContent = selectedForm.querySelector('#toReviewContent').textContent;

	$('#toReviewTitleM').val(toReviewTitle);
	$('#toReviewStarM').val(toReviewStar);
	$('#toReviewContentM').text(toReviewContent);
	$('#toReviewNoM').val(e.target.value);

	e.preventDefault();
});

//6. 투어 포인트 사용하기

$(document).on('click', '#usePointBtn', function(e) {
	let usedPoint = $('#usedPoint').val();
	let nowPoint = $('#nowPoint').val();
	let totPrice = $('#totPrice').val();
	let nowPoint2 = nowPoint - usedPoint;
	let totPrice2 = totPrice - usedPoint;
	let savePoint2 = (totPrice2 * 0.01);
	document.getElementById('nowPoint2').innerHTML = nowPoint2;
	document.getElementById('usingPoint2').innerHTML = usedPoint;
	document.getElementById('totPrice2').innerHTML = totPrice2;
	document.getElementById('savePoint2').innerHTML = savePoint2;
	$('#pPrice').val(totPrice2);
	$('#pPoint').val(savePoint2 - usedPoint);
	e.preventDefault();
});


import * as View from "./view.js";
import * as Request from "./request.js";

/*
 notice / wishlist 관련 js
 #notice-search-btn
 #sort-by
 #notice-write-action
 #notice-modify-action-btn
 #notice-delete-action-btn
 #type-tour
 #type-ticket
 #wishlist-delete-action-btn
 #wishlist-all-delete-action-btn
 #add-to-wishlist-btn
 */

// on => 동적으로 이벤트 추가 가능 -> 상위 노드에 처리해줘야함 (document)

/******** 공지사항 검색결과 리스트 notice-search-list **********/
// 검색창 입력 후 엔터키 => 검색
$("#notice-search-keyword").keyup(e => {
	if (e.keyCode == 13) {
		getSearchList();
		e.preventDefault();
	}
});
// 검색창 입력 후 직접 검색버튼 클릭 => 검색
$(document).on('click', '#notice-search-btn', function(e) {
	getSearchList();
	e.preventDefault();
});

function getSearchList() {
	// ajax로 리스트 부분만 검색된 리스트로 변경
	let keyword = $('#notice-search-keyword').val();
	let url = `notice-search/${keyword}`;
	let method = 'GET';
	let contentType = 'application/json;charset=UTF-8';
	let sendData = {};
	let async = true;

	// 키워드 결과, 현재 페이지 숫자 표시
	let curPage = $('#cur-page');
	let totRecordCount = $('#tot-record-count');

	// JSON.stringify() => 객체를 string 으로, JSON.parse() => string 을 객체로 만듬
	Request.ajaxRequest(url, method, contentType,
		sendData,
		function(resultJson) {
			//페이지 정보 변경
			curPage.text(resultJson.data.pageMaker.curPage);
			totRecordCount.text(resultJson.data.pageMaker.totCount);
			//code 1 일때 render, 아닐 때 msg 띄움
			if (resultJson.code == 1) {
				View.render("#notice-search-list-template", resultJson, '#notice-list');
			} else {
				alert(resultJson.msg);
			};
		}, async);
}

/******** 공지사항 정렬 : 최신순, 오래된순, 조회수 높은 순 **********/
$(document).on('change', '#sort-by', function(e) {
	// ajax로 리스트 부분만 검색된 리스트로 변경
	let selectedValue = $(this).val();
	console.log(selectedValue);
	if (selectedValue === "notice-date-desc") {
		// 최신순
		let url = "notice-date-desc";
		let method = 'GET';
		let contentType = 'application/json;charset=UTF-8';
		let sendData = {};
		let async = true;

		Request.ajaxRequest(url, method, contentType,
			sendData,
			function(resultJson) {
				//code 1 일때 render, 아닐 때 msg 띄움
				if (resultJson.code == 1) {
					//console.log(">>>>>>>>>>>>>>>>>>>>>>>>>");
					View.render("#notice-search-list-template", resultJson, '#notice-list');
				} else {
					alert(resultJson.msg);
				};
			}, async);
		e.preventDefault();
	}

	if (selectedValue === "notice-date-asc") {
		// 오래된순
		let url = 'notice-date-asc';
		let method = 'GET';
		let contentType = 'application/json;charset=UTF-8';
		let sendData = {};
		let async = true;

		Request.ajaxRequest(url, method, contentType,
			sendData,
			function(resultJson) {
				//code 1 일때 render, 아닐 때 msg 띄움
				if (resultJson.code == 1) {
					View.render("#notice-search-list-template", resultJson, '#notice-list');
				} else {
					alert(resultJson.msg);
				};
			}, async);
		e.preventDefault();
	}

	if (selectedValue === "notice-readcount") {
		// 조회수 높은순
		let url = 'notice-readcount';
		let method = 'GET';
		let contentType = 'application/json;charset=UTF-8';
		let sendData = {};
		let async = true;

		Request.ajaxRequest(url, method, contentType,
			sendData,
			function(resultJson) {
				//code 1 일때 render, 아닐 때 msg 띄움
				if (resultJson.code == 1) {
					View.render("#notice-search-list-template", resultJson, '#notice-list');
				} else {
					alert(resultJson.msg);
				};
			}, async);
		e.preventDefault();
	}

});

/******** 공지사항 글쓰기 액션 [파일업로드]**********/
$(document).on('click', '#notice-write-action', function(e) {
	let form = $('#noticewriteF');
	console.log(form);
	if (form.get(0).checkValidity() === false) {
		// 폼의 유효성 검사에서 실패한 요소에 커서 두기
		form.find(':invalid').first().focus();
		// 폼 유효성 검사 실패 시 이벤트 중지
		e.preventDefault();
		e.stopPropagation();
	} else {
		// ajax로 리스트 부분만 검색된 리스트로 변경
		let url = 'notice';
		let method = 'POST';
		let formData = new FormData();
		let nImg = null;

		formData.append('nTitle', $('#nTitle').val());
		formData.append('nContent', $('#nContent').val());
		formData.append('userId', $('#userId').val());

		// 업로드 파일 선택이 되었을 때만 FormData에 추가
		let inputFile = $("input[type='file']");
		// 파일이 선택되면 nimg 가 선택된 파일로 지정
		if (inputFile.prop('files') && inputFile.prop('files').length > 0) {
			formData.append('file', inputFile.prop('files')[0]);
			let inputFileName = inputFile.prop('files')[0].name;
			console.log(inputFileName);
		} else {
			//파일 선택 안되면 hidden으로 넣어놓은 nImg 이미지로 선택
			nImg = $('#nImg').val();
			formData.append('nImg', nImg);
		}
		let async = true;
		console.log(formData);

		$.ajax({
			method: method,
			url: url,
			data: formData,
			processData: false,
			contentType: false,
			success: function(resultJson) {
				//code 1 일때 render, 아닐 때 msg 띄움
				if (resultJson.code == 1) {
					View.render("#notice-detail-template", resultJson, '#notice-write-detail');
				} else {
					alert(resultJson.msg);
				};
			},
			err: function(err) {
				alert(err.status);
			},
			async: async
		});
		e.preventDefault();
	}

});

/******** 공지사항 글수정 액션 [파일업로드]**********/
$(document).on('click', '#notice-modify-action-btn', function(e) {
	let form = $('#noticewriteF');
	console.log(form);
	if (form.get(0).checkValidity() === false) {
		// 폼의 유효성 검사에서 실패한 요소에 커서 두기
		form.find(':invalid').first().focus();

		// 폼 유효성 검사 실패 시 이벤트 중지
		e.preventDefault();
		e.stopPropagation();
	} else {
		// ajax로 리스트 부분만 검색된 리스트로 변경
		let nNo = $('#modify-nNo').val();
		console.log(nNo);
		let url = `notice/${nNo}`;
		let method = 'PUT';
		let formData = new FormData();
		let nImg = null;

		formData.append('nTitle', $('#nTitle').val());
		formData.append('nContent', $('#nContent').val());
		formData.append('userId', $('#userId').val());
		// 업로드 파일 선택이 되었을 때만 FormData에 추가
		let inputFile = $("input[type='file']");
		// 파일이 선택되면 nimg 가 선택된 파일로 지정
		if (inputFile.prop('files') && inputFile.prop('files').length > 0) {
			formData.append('file', inputFile.prop('files')[0]);
			let inputFileName = inputFile.prop('files')[0].name;
			console.log(inputFileName);
		} else {
			//파일 선택 안되면 hidden으로 넣어놓은 nImg 이미지로 선택
			nImg = $('#nImg').val();
			formData.append('nImg', nImg);
		}
		let async = true;
		console.log(formData);

		$.ajax({
			method: method,
			url: url,
			data: formData,
			processData: false,
			contentType: false,
			success: function(resultJson) {
				//code 1 일때 render, 아닐 때 msg 띄움
				if (resultJson.code == 1) {
					let updatednNo = resultJson.data[0].nno;
					// update 된 거 출력 위해 notice_detail 로 다시 요청
					Request.ajaxRequest(
						`notice/${updatednNo}`, 'GET', 'application/json;charset=UTF-8', {},
						function(resultJson) {
							View.render("#notice-modified-template", resultJson, '#notice-modify-form');
						});
				} else {
					alert(resultJson.msg);
				};
			},
			err: function(err) {
				alert(err.status);
			},
			async: async
		});
		e.preventDefault();
	}

});

/******** 공지사항 글 삭제 액션**********/

$(document).on('click', '#notice-delete-action-btn', function(event) {
	event.preventDefault();
	let nNo = $('#nNo-hidden-value').val();
	let nno = $(this).data('nno');
	console.log(nno);
	$.ajax({
		type: 'DELETE',
		url: 'notice/' + nNo,
		success: function(result) {
			// 삭제 후 notice-list 로 이동
			window.location.href = 'notice-list';
		},
		error: function(xhr, status, error) {
			console.log(error);
		}
	});
});


/******** 위시리스트 - 투어 클릭 wishlist-list **********/

$(document).on('click', '#type-tour', function(e) {
	// ajax로 리스트 부분만 검색된 리스트로 변경

	let userId = $('#wishlist-id').val();
	console.log(userId);
	let method = 'GET';
	let contentType = 'application/json;charset=UTF-8';
	let sendData = {};
	let async = true;

	Request.ajaxRequest(`wishlist-with-tour?userId=${userId}`,
		method, contentType,
		sendData,
		function(resultJson) {
			//code 1 일때 render, 아닐 때 msg 띄움
			if (resultJson.code == 1) {
				View.render('#wishlist-tour-template', resultJson, '#wishlist-list');
			} else {
				alert(resultJson.msg);
			};
		}, async);
	e.preventDefault();

});

/******** 위시리스트 - 티켓 클릭 wishlist-list **********/

$(document).on('click', '#type-ticket', function(e) {
	// ajax로 리스트 부분만 검색된 리스트로 변경
	let userId = $('#wishlist-id').val();
	let method = 'GET';
	let contentType = 'application/json;charset=UTF-8';
	let sendData = {};
	let async = true;

	Request.ajaxRequest(`wishlist-with-ticket?userId=${userId}`,
		method, contentType,
		sendData,
		function(resultJson) {
			//code 1 일때 render, 아닐 때 msg 띄움
			if (resultJson.code == 1) {
				View.render("#wishlist-ticket-template", resultJson, '#wishlist-list');
			} else {
				alert(resultJson.msg);
			};
		}, async);
	e.preventDefault();

});

/******** 위시리스트 삭제 액션**********/

$(document).on('click', '#wishlist-delete-action-btn', function(event) {
	event.preventDefault();
	let userId = $('#wishlist-userId').val();
	//let wishNo = $('#wishlist-wishNo').val();
	let wishNo = $(this).attr('data-wishNo');
	console.log(userId);
	console.log(wishNo);
	$.ajax({
		type: 'DELETE',
		url: `wishlist/${wishNo}/${userId}`,
		success: function(result) {
			// 삭제 후 wishlist 로 이동
			window.location.href = 'wishlist';
		},
		error: function(xhr, status, error) {
			console.log(error);
		}
	});
});

/******** 위시리스트 전체삭제 액션**********/

$(document).on('click', '#wishlist-all-delete-action-btn', function(event) {
	event.preventDefault();
	let userId = $('#wishlist-all-delete-userId').val();
	console.log(userId);
	$.ajax({
		type: 'DELETE',
		url: `wishlist/${userId}`,
		success: function(result) {
			// 삭제 후 wishlist 로 이동
			window.location.href = 'wishlist';
		},
		error: function(xhr, status, error) {
			console.log(error);
		}
	});
});

/******** 투어상품 버튼 클릭 시 위시리스트로 추가 액션 **********/

$(document).on('click', '#tour-add-to-wishlist-btn', function(e) {
	// ajax로 리스트 부분만 검색된 리스트로 변경
	let url = 'wishlist-tour';
	let method = 'POST';
	let contentType = 'application/json;charset=UTF-8';
	let toNo = $(this).attr('data-toNo');
	let toName = $(this).attr('data-toName');
	let toTime = $(this).attr('data-toTime');
	let toPerson = $(this).attr('data-toPerson');
	let toMeet = $(this).attr('data-toMeet');
	let toPrice = $(this).attr('data-toPrice');
	let toInfo = $(this).attr('data-toInfo');
	let toNotice = $(this).attr('data-toNotice');
	let toCount = $(this).attr('data-toCount');
	let cityNo = $(this).attr('data-cityNo');
	let cityName = $(this).attr('data-cityName');
	let latitude = $(this).attr('data-latitude');
	let longitude = $(this).attr('data-longitude');

	console.log(toNo);
	console.log(toName);
	console.log(toTime);
	console.log(toPerson);
	console.log(toMeet);
	console.log(toPrice);
	console.log(toInfo);
	console.log(toNotice);
	console.log(toCount);
	console.log(cityNo);
	console.log(cityName);
	console.log(latitude);
	console.log(longitude);

	let sendData = {
		tour: {
			toNo: toNo,
			toName: toName,
			toTime: toTime,
			toPerson: toPerson,
			toMeet: toMeet,
			toPrice: toPrice,
			toInfo: toInfo,
			toNotice: toNotice,
			toCount: toCount,
			city: {
				cityNo: cityNo,
				cityName: cityName,
				latitude: latitude,
				longitude: longitude
			}
		}
	};
	let async = true;

	Request.ajaxRequest(url,
		method, contentType,
		JSON.stringify(sendData),
		function(resultJson) {
			console.log("tour add>>>>>>>>>>>>>>>>>>>>");
			//성공시 toast로 성공메세지 띄우기
			if (resultJson.code == 1) {
				$('#wishlist-add-toast').toast('show');
				$('#wishlist-add-toast').toast({ delay: 5000 });
			} else if (resultJson.code == 3) {
				$('#wishlist-added-toast').toast('show');
				$('#wishlist-added-toast').toast({ delay: 5000 });
			} else {
				alert(resultJson.msg);
			};
		}, async);
	e.preventDefault();
});

/******** 티켓상품 버튼 클릭 시 위시리스트로 추가 액션 **********/

$(document).on('click', '#ticket-add-to-wishlist-btn', function(e) {
	// ajax로 리스트 부분만 검색된 리스트로 변경
	let url = 'wishlist-ticket';
	let method = 'POST';
	let contentType = 'application/json;charset=UTF-8';
	let tiNo = $(this).attr('data-tiNo');
	let tiTitle = $(this).attr('data-tiTitle');
	let tiDate = $(this).attr('data-tiDate');
	let tiPrice = $(this).attr('data-tiPrice');
	let tiInfo = $(this).attr('data-tiInfo');
	let tiNotice = $(this).attr('data-tiNotice');
	let tiCount = $(this).attr('data-tiCount');
	let cityNo = $(this).attr('data-cityNo');
	let cityName = $(this).attr('data-cityName');
	let latitude = $(this).attr('data-latitude');
	let longitude = $(this).attr('data-longitude');

	console.log(tiNo);
	console.log(tiTitle);
	console.log(tiDate);
	console.log(tiPrice);
	console.log(tiInfo);
	console.log(tiNotice);
	console.log(tiCount);
	console.log(cityNo);
	console.log(cityName);
	console.log(latitude);
	console.log(longitude);

	let sendData = {
		ticket: {
			tiNo: tiNo,
			tiTitle: tiTitle,
			tiDate: tiDate,
			tiPrice: tiPrice,
			tiInfo: tiInfo,
			tiNotice: tiNotice,
			tiCount: tiCount,
			city: {
				cityNo: cityNo,
				cityName: cityName,
				latitude: latitude,
				longitude: longitude
			}
		}
	};
	let async = true;
	console.log('[ticket add!!!!!]' + JSON.stringify(sendData));
	Request.ajaxRequest(url,
		method, contentType,
		JSON.stringify(sendData),
		function(resultJson) {
			console.log("ticket add>>>>>>>>>>>>>>>>>>>>");
			//성공시 toast로 성공메세지 띄우기
			if (resultJson.code == 1) {
				$('#wishlist-add-toast').toast('show');
				$('#wishlist-add-toast').toast({ delay: 5000 });
			} else if (resultJson.code == 3) {
				$('#wishlist-added-toast').toast('show');
				$('#wishlist-added-toast').toast({ delay: 5000 });
			} else {
				alert(resultJson.msg);
			};
		}, async);
	e.preventDefault();
});





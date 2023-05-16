# My Real Korea

국내 투어/티켓 예약 & 동행 찾기 웹 애플리케이션  
  
![myrealkorea_main](https://github.com/minseonallykim/my-real-korea/assets/117511891/679096cd-dca4-4f43-9b07-a616253ced61)

* Demo : [http://52.64.100.184:8080/my-real-korea](http://52.64.100.184:8080/my-real-korea)  

## 📌 개발 목표
국내 여행에 초점을 맞춘 여행 전문 사이트  
여행 상품 판매 뿐 아니라 여행 관련 정보를 얻고, 동행 찾기가 가능한 여행 전문 웹사이트 구현  
## 📌  사용 기술
* 언어 : Java, HTML/CSS, JavaScript
* 서버 : Apache Tomcat 9.0.73
* 프레임워크 : Spring Boot, MyBatis 2.3.0, Bootstrap4
* DB : Oracle
* IDE : Spring Tool Suite 4(STS) - Gradle
* API, 라이브러리 : Kakao Login / Daum주소 / Google Maps / PortOne, Thymeleaf, Handlebars, JQuery
* ETC : AWS EC2
## 📌  주요 기능
### 회원가입 & 로그인
* 보안을 위해 회원가입 후 첫 로그인은 가입 시 입력한 이메일로 온 인증코드 입력 후 가능  

![myrealkorea_login_email](https://github.com/minseonallykim/my-real-korea/assets/117511891/3c1f1e4e-c339-42ae-bf52-7257cb48848c)

* 마이페이지에서 회원 정보 수정, 내가 쓴 게시글/리뷰, 투어/티켓 예약 내역 보기 가능  

![myrealkorea_mypage](https://github.com/minseonallykim/my-real-korea/assets/117511891/4c147dfa-de1e-4432-86c5-ea1c0a7d437e)  

### 투어/티켓 상품
* 투어/티켓 상품 정렬, 지역별 필터링, 키워드 검색 가능  

![myrealkorea_tourticket](https://github.com/minseonallykim/my-real-korea/assets/117511891/18590e7c-0807-449a-bfd2-8dfc360cc51c)  

* 투어/티켓 상품 예약

![myrealkorea_tour](https://github.com/minseonallykim/my-real-korea/assets/117511891/4b86d5a3-d096-4589-87c0-1bfaa6e8faea)

### 위시리스트  
* 로그인 후 위시리스트에 투어/티켓 상품 추가

![myrealkorea_wishlist_add](https://github.com/minseonallykim/my-real-korea/assets/117511891/7f69be77-372a-4a38-b1d3-74133a25cea4)  

* 위시리스트 투어/티켓만 필터링 가능
* 상세보기 클릭 시 해당 상품 상세보기로 이동
* 해당 상품 삭제, 위시리스트 전체 삭제 가능  

![myreallkorea_wishlistpage](https://github.com/minseonallykim/my-real-korea/assets/117511891/0534ba55-29d8-4d93-bd2e-20d12e6dc2b4)  

### 동행 찾기 게시판  
* 국내 여행 동행을 찾는 게시판
* 게시글, 댓글 CRUD 가능
* 정렬, 지역별/모집 상태별 필터링, 키워드 검색 가능  

![myrealkorea_tripboard](https://github.com/minseonallykim/my-real-korea/assets/117511891/7f0385c7-2881-40e2-99b2-c734695a4f9a)  

### 1:1 실시간 채팅
* 동행 찾기 게시판 상세보기에서 게시글 작성자와 1:1 채팅방 생성
* 1:1 실시간 채팅, 채팅방 클릭 시 대화 내용 불러오기 가능  

![myrealkorea_chat](https://github.com/minseonallykim/my-real-korea/assets/117511891/a02fc91d-b104-40d6-810e-5004cae9f0a1)

### 자유게시판
* 자유롭게 게시글과 댓글로 소통이 가능한 자유게시판
* 게시글, 댓글 CRUD / 정렬, 키워드 검색 가능  

![myrealkorea_freeboard](https://github.com/minseonallykim/my-real-korea/assets/117511891/b68ab1d8-d3cb-46a1-a0ee-ecd53f7aaa3e)

### 공지사항
* 이벤트, 할인 정보 등을 알리는 공지사항 게시판  

![myrealkorea_notice](https://github.com/minseonallykim/my-real-korea/assets/117511891/395cd94a-7091-4888-8405-b8919d62856c)  

* 관리자만 공지사항 글쓰기/수정/삭제 가능 
* 공지사항 정렬, 키워드 검색 가능   

![myrealkorea_notice](https://github.com/minseonallykim/my-real-korea/assets/117511891/2bc3ac48-33f8-401f-b06f-f2518fc71130)  

## 📌  ERD  
![myrealkorea erd](https://github.com/minseonallykim/my-real-korea/assets/117511891/58e20965-9303-4bfd-85d8-97babd9ce934)


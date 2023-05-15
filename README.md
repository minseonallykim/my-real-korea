# My Real Korea

국내 투어/티켓 예약 & 동행 찾기 웹 애플리케이션  
  
![myrealkorea_main](https://github.com/minseonallykim/my-real-korea/assets/117511891/679096cd-dca4-4f43-9b07-a616253ced61)

* Demo : [http://52.64.100.184:8080/my-real-korea](http://52.64.100.184:8080/my-real-korea)  
  
## 개발 목표
국내 여행에 초점을 맞춘 여행 전문 사이트  
여행 상품 판매 뿐 아니라 여행 관련 정보를 얻고, 동행 찾기가 가능한 여행 전문 웹사이트 구현  
## 사용 기술
* 언어 : Java, HTML/CSS, JavaScript
* 서버 : Apache Tomcat 9.0.73
* 프레임워크 : Spring Boot, MyBatis 2.3.0, Bootstrap4
* DB : Oracle
* IDE : Spring Tool Suite 4(STS) - Gradle
* API, 라이브러리 : Kakao Login / Daum주소 / Google Maps / PortOne, Thymeleaf, Handlebars, JQuery
## 주요 기능
#### 회원가입 & 로그인
* 보안을 위해 회원가입 후 첫 로그인은 가입 시 입력한 이메일로 온 인증코드 입력 후 가능  

![myrealkorea_login_email](https://github.com/minseonallykim/my-real-korea/assets/117511891/3c1f1e4e-c339-42ae-bf52-7257cb48848c)

#### 투어/티켓 상품 예약
* Port One API 사용하여 투어/티켓 상품 예약(결제) 구현  

![myrealkorea_tour](https://github.com/minseonallykim/my-real-korea/assets/117511891/4b86d5a3-d096-4589-87c0-1bfaa6e8faea)

#### 위시리스트  
* 로그인 후 위시리스트에 투어/티켓 상품 추가

![myrealkorea_wishlist_add](https://github.com/minseonallykim/my-real-korea/assets/117511891/7f69be77-372a-4a38-b1d3-74133a25cea4)  

* 

#### 1:1 실시간 채팅
* 동행 찾기 게시판에서 게시글 작성자와 1:1 채팅 하기
* 1:1 실시간 채팅  

![myrealkorea_chat](https://github.com/minseonallykim/my-real-korea/assets/117511891/a02fc91d-b104-40d6-810e-5004cae9f0a1)

#### 자유게시판
* 자유롭게 게시글과 댓글로 소통이 가능한 자유게시판
#### 공지사항
* 이벤트, 할인 정보 등을 알리는 공지사항 게시판
* 관리자만 공지사항 글쓰기/수정/삭제 가능
## ERD  
![myrealkorea erd](https://github.com/minseonallykim/my-real-korea/assets/117511891/58e20965-9303-4bfd-85d8-97babd9ce934)


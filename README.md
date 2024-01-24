# crmproject
청년crm101 3조 미니프로젝트입니다.

## 개발 환경
- java 8
- jdk 1.8.0
- Database : Oracle 11g xe
- Server : Apache Tomcat v8.5

## 파일 분류
- Main : 홈 화면
- Board: 게시판
   - 게시물 조회 및 상세조회(제목/내용/작성일/작성자)
   - 게시물 등록
   - 게시물 삭제
   - 게시물 수정
   - 조회수
- GUESTBOOK: 방명록
    - 방명록 조회
    - 방명록 등록
    - 방명록 삭제

## 주요 기능
### 홈 (main/index.jsp)
![image](https://github.com/ananaskimchi/crmproject/assets/141298734/ea334978-f9c9-4e80-b14a-a216481f11b2)

1. /main 주소로 접속하면 MainServlet.java를 실행해 홈 화면을 표시합니다.
2. 오른쪽 상단에 로그인 관련 기능이 있어 회원가입/로그인을 누르면 UserServlet.java를 실행합니다.
3. 로그인 되어 있을 때엔 회원정보수정이 표시되고 누르면 UserServlet.java를 실행합니다.
4. 왼쪽의 네비게이션 바를 통해 홈/방명록/게시판 이동이 가능합니다.
</br>

![image](https://github.com/ananaskimchi/crmproject/assets/104189608/b454aafc-0e13-4978-b02c-286f9c7b8852)


_로그인 시 화면_

</br>

---
### 방명록 (guest/list.jsp)
![image](https://github.com/ananaskimchi/crmproject/assets/141298734/6e523505-a5a2-44f2-a62e-218ba7891011)

1. /gb 주소로 접속하면 GuestbookServlet.java를 실행합니다.
2. 이름, 비밀번호, 내용을 입력해 확인을 누르면 db의 guestbook 테이블에 게시글이 저장됩니다.
3. guestbook의 모든 게시글을 불러와 방명록 페이지에 순서대로 표시합니다.
4. 삭제를 누르면 비밀번호를 입력받는 페이지로 이동해 비밀번호가 일치하면 해당 글을 삭제하고 방명록 페이지로 이동합니다.
- 방명록의 모든 기능은 로그인과 무관합니다.
</br>

---

### 게시판 (board/list.jsp)
![image](https://github.com/ananaskimchi/crmproject/assets/141298734/3a1474bc-3230-42ed-9edb-6709c32ff326)

1. /board 주소로 접속하면 BoardServlet.java를 실행합니다.
2. db의 board 테이블의 모든 게시글을 불러와 한 페이지에 글 10개씩, 한 번에 10개의 페이지를 묶어 화면에 표시합니다. 
3. 제목 컬럼의 게시글 제목을 누르면 해당 글의 상세보기(read.jsp) 페이지를 실행합니다.
4. 상단의 검색창을 통해 제목/내용/글쓴이/작성일로 글 검색이 가능합니다.
</br>

![image](https://github.com/ananaskimchi/crmproject/assets/141298734/f99441d8-88e0-429e-b4c2-18d3e3e54c69)
![image](https://github.com/ananaskimchi/crmproject/assets/104189608/34ea484c-6f68-48b7-a17f-6beaacde736c)


_로그인 시 화면_

5. 로그인하면 오른쪽 하단에 글쓰기 버튼이 표시됩니다. 글쓰기 버튼을 누르면 writeform.jsp를 실행합니다.
6. 본인이 작성한 글이라면 작성일 오른쪽에 휴지통 아이콘이 표시됩니다. 아이콘을 클릭하면 db에서 해당글을 삭제하고 게시판을 새로 접속합니다.
</br>

#### 글쓰기 페이지 (writeform.jsp)
![image](https://github.com/ananaskimchi/crmproject/assets/141298734/ba846c01-71c6-47b3-8398-389af1662166)

7. 제목, 내용, 첨부파일(2개까지)을 입력해 등록버튼을 누르면 db에 데이터가 insert되고 게시판으로 이동합니다.
</br>

#### 상세보기 페이지
![image](https://github.com/ananaskimchi/crmproject/assets/141298734/2f05898d-3ed9-4c28-b90c-afc6025aeb15)

8. 상세보기 페이지에는 db의 board 테이블에서 불러온 해당 글의 제목,내용,첨부파일이름이 표시됩니다.
9. 첨부파일 이름을 클릭하면 해당 이름으로 파일이 바로 저장됩니다.
</br>

![image](https://github.com/ananaskimchi/crmproject/assets/141298734/448ac162-a2fb-4346-973f-2603a4ea1348)
![image](https://github.com/ananaskimchi/crmproject/assets/104189608/53cc4ec4-b854-42b3-985f-687722c10b9d)


_로그인 시 화면_

10. 로그인 시 본인이 작성한 글이라면, 글수정 버튼이 표시됩니다. 글수정 버튼을 누르면 modifyform.jsp를 실행합니다.
</br>

#### 글수정 페이지
![image](https://github.com/ananaskimchi/crmproject/assets/141298734/f68f9ec7-5b77-4624-b4af-c3ed8c74ba5f)

11. 해당 글의 제목, 내용을 지우고 새로 입력해 수정버튼을 누르면 db에 데이터가 update되고 게시판으로 이동합니다.
12. 취소 버튼을 누르면 해당 글의 상세보기 페이지로 이동합니다.
</br>


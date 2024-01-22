<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="/mysite/assets/css/board.css" rel="stylesheet" type="text/css">
<title>Mysite</title>
</head>
<body>
	<div id="container">
		
		<c:import url="/WEB-INF/views/includes/header.jsp"></c:import>
		<c:import url="/WEB-INF/views/includes/navigation.jsp"></c:import>
		
		<div id="content">
			<div id="board">
				<form id="search_form" action="/mysite/board" method="post">
					<select name="keyField" size="1" >
    				<option value="b.title"> 제 목</option>
    				<option value="b.content||UPPER(b.file_name)||UPPER(b.file_name2)"> 내 용</option>
    				<option value="u.name"> 글쓴이</option>
    				<option value="to_char(b.reg_date, 'YY-MM-DD HH24:MI')"> 작성일</option>
   				</select>
					<input type="hidden" name="a" value ="list">
					<input type="text" id="kwd" name="keyWord">
					<input type="submit" value="찾기">
				</form>
				<table class="tbl-ex">
					<tr>
						<th>번호</th>
						<th>제목</th>
						<th>글쓴이</th>
						<th>조회수</th>
						<th>작성일</th>
						<th>&nbsp;</th>
					</tr>				
					<c:forEach items="${boardlist }" var="vo">
						<tr>
							<td>${vo.no }</td>
							<td><a href="/mysite/board?a=read&no=${vo.no }"> ${vo.title } </a></td>
							<td>${vo.userName }</td>
							<td>${vo.hit }</td>
							<td>${vo.regDate }</td>
							<td>
								<c:if test="${authUser.no == vo.userNo }">
									<a href="/mysite/board?a=delete&no=${vo.no }" class="del">삭제</a>
								</c:if>
							</td>
						</tr>
					</c:forEach>
				</table>
				<div class="pager">
				     <c:if test="${currentPage > 1}">
        				<a href="board?a=list&page=${currentPage - 1}">◀</a>
   					 </c:if>
   					  <c:choose>
				        <c:when test="${totalPage <= 10}">
				            <c:forEach var="pageNum" begin="1" end="${totalPage}">
				                <c:choose>
				                    <c:when test="${pageNum == currentPage}">
				                        <a class="selected">${pageNum}</a>
				                    </c:when>
				                    <c:otherwise>
				                        <a href="board?a=list&page=${pageNum}">${pageNum}</a>
				                    </c:otherwise>
				                </c:choose>
				            </c:forEach>
				        </c:when>
				        <c:otherwise>
				            <c:forEach var="pageNum" begin="${currentPage - 5}" end="${currentPage + 5}">
				                <c:choose>
				                    <c:when test="${pageNum < 1 or pageNum > totalPage}">
				                        <!-- 페이지 범위를 벗어나면 표시하지 않음 -->
				                    </c:when>
				                    <c:otherwise>
				                        <c:choose>
				                            <c:when test="${pageNum == currentPage}">
				                                <a class="selected">${pageNum}</a>
				                            </c:when>
				                            <c:otherwise>
				                                <a href="board?a=list&page=${pageNum}">${pageNum}</a>
				                            </c:otherwise>
				                        </c:choose>
				                    </c:otherwise>
				                </c:choose>
				            </c:forEach>
				        </c:otherwise>
				    </c:choose>
				
				    <c:if test="${currentPage < totalPage}">
				        <a href="board?a=list&page=${currentPage + 1}">▶</a>
				    </c:if>
   					 			
				</div>				
				<c:if test="${authUser != null }">
					<div class="bottom">
						<a href="/mysite/board?a=writeform" id="new-book">글쓰기</a>
					</div>
				</c:if>				
			</div>
		</div>
		
		<c:import url="/WEB-INF/views/includes/footer.jsp"></c:import>
		
	</div><!-- /container -->
</body>
</html>		
		

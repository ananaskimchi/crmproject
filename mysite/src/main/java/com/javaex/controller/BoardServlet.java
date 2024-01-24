package com.javaex.controller;

import java.io.*;
import java.util.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javaex.dao.BoardDao;
import com.javaex.dao.BoardDaoImpl;
import com.javaex.util.WebUtil;
import com.javaex.vo.BoardVo;
import com.javaex.vo.UserVo;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.*;

@WebServlet("/board")
public class BoardServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static final String SAVEFOLDER = "C:/Users/User/git/crmproject/mysite/src/main/webapp/WEB-INF/views/storage";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    request.setCharacterEncoding("UTF-8");

	    String actionName = request.getParameter("a");
	    System.out.println("board:" + actionName);
    	
    	// 검색 관련 변수
    	String keyField = "";
    	String keyWord = "";
	    
	    if ("list".equals(actionName)) {
	    	if (request.getParameter("keyWord") != null) {
	    		keyWord = request.getParameter("keyWord");
	    		keyField = request.getParameter("keyField");
	    		
	    	}
	      BoardDao dao = new BoardDaoImpl();
	      System.out.println("keyword:" + keyWord);
    		System.out.println("keyfield:" + keyField);
	    	List<BoardVo> boardlist = dao.getList(keyField, keyWord);
	    	System.out.println(boardlist);
	    	
	    	// 페이징 관련 변수
		    // 페이지 파라미터 값 가져오기
	    	String pageParameter = request.getParameter("page");
	    	// 페이지 파라미터 값이 비어있으면 기본값 1로 설정
	    	int currentPage = (pageParameter != null && !pageParameter.isEmpty()) ? Integer.parseInt(pageParameter) : 1;
	    	int itemsPerPage = 10;
	    	int startRow = (currentPage - 1) * itemsPerPage + 1;
	    	int endRow = startRow + itemsPerPage - 1;
	    	//페이징 변수 끝
	    	
	    	int totalItem = boardlist.size();
	      int totalPage = (int) Math.ceil((double) totalItem / itemsPerPage);
	      System.out.println("a:"+totalItem);
	      System.out.println("a:"+totalPage);
	      System.out.println("a:"+currentPage);
	      System.out.println("a:"+totalPage);
	      System.out.println(startRow);
	      System.out.println(endRow);
	      
        if(startRow <= totalItem) {
        	boardlist=boardlist.subList(startRow-1, Math.min(endRow, totalItem));
        } else {
          boardlist = new ArrayList<>();
        }
        
        request.setAttribute("boardlist", boardlist);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("keyWord", keyWord);
        request.setAttribute("keyField", keyField);

        RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/views/board/list.jsp");
        rd.forward(request, response);
		} else if ("read".equals(actionName)) {
			// 게시물 가져오기
			int no = Integer.parseInt(request.getParameter("no"));
			BoardDao dao = new BoardDaoImpl();
			BoardVo boardVo = dao.getBoard(no);

			System.out.println(boardVo.toString());
			
			// 조회수 늘리기
			int 조회수 = boardVo.getHit();
			조회수 +=1;
			boardVo.setHit(조회수);
			dao.hitUpdate(boardVo);
					
			
			// 게시물 화면에 보내기
			request.setAttribute("boardVo", boardVo);
			WebUtil.forward(request, response, "/WEB-INF/views/board/read.jsp");
		} else if ("modifyform".equals(actionName)) {
			// 게시물 가져오기
			int no = Integer.parseInt(request.getParameter("no"));
			BoardDao dao = new BoardDaoImpl();
			BoardVo boardVo = dao.getBoard(no);

			// 게시물 화면에 보내기
			request.setAttribute("boardVo", boardVo);
			WebUtil.forward(request, response, "/WEB-INF/views/board/modifyform.jsp");
		} else if ("modify".equals(actionName)) {
			// 게시물 가져오기
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			int no = Integer.parseInt(request.getParameter("no"));
			
			BoardVo vo = new BoardVo(no, title, content);
			BoardDao dao = new BoardDaoImpl();
			
			dao.update(vo);
			
			WebUtil.redirect(request, response, "/mysite/board?a=list");
		} else if ("writeform".equals(actionName)) {
			// 로그인 여부체크
			UserVo authUser = getAuthUser(request);
			if (authUser != null) { // 로그인했으면 작성페이지로
				WebUtil.forward(request, response, "/WEB-INF/views/board/writeform.jsp");
			} else { // 로그인 안했으면 리스트로
				WebUtil.redirect(request, response, "/mysite/board?a=list");
			}

		} else if ("write".equals(actionName)) {
			// 게시물 작성
			UserVo authUser = getAuthUser(request);
			
			// 업로드되는 파일을 저장하는 저장소와 관련된 클래스
			File attachesDir = new File(SAVEFOLDER); // 저장 경로를 변수에 담기
			DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
			fileItemFactory.setRepository(attachesDir); // 저장 경로 설정
			fileItemFactory.setSizeThreshold(-1);
			ServletFileUpload fileUpload = new ServletFileUpload(fileItemFactory); //업로드 요청을 처리하는 ServletFileUpload 객체 생성
			List<String> temp = new ArrayList<String>(); 
			
      try {
				List<FileItem> items = fileUpload.parseRequest(request);
				for(FileItem item : items) {
					if(item.isFormField()) {
						System.out.printf("파라미터 명 : %s, 파라미터 값 :  %s \n", item.getFieldName(), item.getString("UTF-8"));
						temp.add(item.getString("UTF-8"));
					} else {
						System.out.printf("파라미터 명 : %s, 파일 명 : %s, 파일 크기 :  %s \n", item.getFieldName(), item.getName(), item.getSize());
						if(item.getSize()>0){
							String separator = File.separator;
							int index = item.getName().lastIndexOf(separator);
							String fileName = item.getName().substring(index + 1);
							temp.add(fileName);
							UUID uuid = UUID.randomUUID();
							String rFileName = uuid + fileName;
							temp.add(rFileName);
							File uploadFile = new File(attachesDir + separator + rFileName);
							item.write(uploadFile);
							item.delete();
						}
					}
				}//for
				
				System.out.println("파일 업로드 완료");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			int userNo = authUser.getNo();
			System.out.println(temp);
			String title = temp.get(0);
			String content = temp.get(1);
			String fileName = (temp.size()>2) ? temp.get(2) : null;
			String origFName = (temp.size()>2) ? temp.get(3) : null;
			String fileName2 = (temp.size()>4) ? temp.get(4) : null;
			String origFName2 = (temp.size()>4) ? temp.get(5) : null;
			System.out.println("userNo : ["+userNo+"]");
			System.out.println("title : ["+title+"]");
			System.out.println("content : ["+content+"]");
			System.out.println("filename : ["+fileName+"]");
			System.out.println("filename2 : ["+fileName2+"]");

			BoardVo vo = new BoardVo(title, content, userNo, fileName, origFName, fileName2, origFName2);
			BoardDao dao = new BoardDaoImpl();
			dao.insert(vo);

			WebUtil.redirect(request, response, "/mysite/board?a=list");

		} else if ("download".equals(actionName)) {
			// 파일 경로, 이름 불러오기
			String fileDir = SAVEFOLDER;
			String fileName = (String)request.getParameter("fileName");
			System.out.println("filename = "+ fileName);
			
			// response에서 OutputStream 객체 가져오기
			OutputStream out = response.getOutputStream();
      String downFile = fileDir + File.separator + fileName;
      File f = new File(downFile);
      
      response.setHeader("Cache-Control","no-cache");
      response.addHeader("Cache-disposition", "attachment; fileName="+fileName);
      
      FileInputStream in = new FileInputStream(f);
      
      // 파일에서 버퍼로 데이터를 읽어와 출력(다운로드)
      byte[] buffer = new byte[(int) f.length()];
      while(true) {
      	int count = in.read(buffer);
          if(count==-1) {
          	break;
          }
          out.write(buffer,0,count);
      }
      in.close();
      out.close();
      			
		} else if ("delete".equals(actionName)) {
			int no = Integer.parseInt(request.getParameter("no"));

			BoardDao dao = new BoardDaoImpl();
			dao.delete(no);

			WebUtil.redirect(request, response, "/mysite/board?a=list");

		} 
//		//게시글 검색
//		else if("search".equals(actionName))
//		{
//			System.out.println("검색시작");
//			String searchThings = request.getParameter("searchThings");
//			
//			BoardVo searchVo = new BoardVo(searchThings);
//
//			BoardDao dao = new BoardDaoImpl();
//			List<BoardVo> searchResults = dao.search(searchVo);
//			int totalItem = searchResults.size();
//      int totalPage = (int) Math.ceil((double) totalItem / itemsPerPage);
//      
//      if(startRow <= totalItem) {
//      	searchResults=searchResults.subList(startRow-1, Math.min(endRow, totalItem));
//      } else {
//      	searchResults = new ArrayList<>();
//      }
//		
//			System.out.println(searchResults.toString());
//			
//			request.setAttribute("boardlist", searchResults);
//			request.setAttribute("currentPage", 1);
//      request.setAttribute("totalPage", totalPage);
//			
//			RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/views/board/list.jsp");
//			rd.forward(request, response);
//		}		
		
		else {
			WebUtil.redirect(request, response, "/mysite/board?a=list");
		}
		
	
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	// 로그인 되어 있는 정보를 가져온다.
	protected UserVo getAuthUser(HttpServletRequest request) {
		HttpSession session = request.getSession();
		UserVo authUser = (UserVo) session.getAttribute("authUser");

		return authUser;
	}
	
	

}

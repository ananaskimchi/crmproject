package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javaex.vo.BoardVo;

public class BoardDaoImpl implements BoardDao {
  private Connection getConnection() throws SQLException {
    Connection conn = null;
    try {
      Class.forName("oracle.jdbc.driver.OracleDriver");
      String dburl = "jdbc:oracle:thin:@localhost:1521:xe";
      conn = DriverManager.getConnection(dburl, "webdb", "1234");
    } catch (ClassNotFoundException e) {
      System.err.println("JDBC 드라이버 로드 실패!");
    }
    return conn;
  }
  
	public List<BoardVo> getList(String keyField, String keyWord) {

		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BoardVo> list = new ArrayList<BoardVo>();

		try {
			conn = getConnection();
			
			if (keyWord.equals("null") || keyWord.equals("")) {
				// 3. SQL문 준비 / 바인딩 / 실행
				String query = "select b.no, b.title, b.hit, to_char(b.reg_date, 'YY-MM-DD HH24:MI') \"reg_date\", b.user_no, b.file_name, b.file_name2, u.name "
						     + " from board b, users u "
						     + " where b.user_no = u.no "
						     + " order by no desc";
				
				pstmt = conn.prepareStatement(query);
			} else {
				// 3. SQL문 준비 / 바인딩 / 실행
				String query = "SELECT b.no, b.title, b.hit, to_char(b.reg_date, 'YY-MM-DD HH24:MI') as reg_date, u.no as user_no, u.name as name, b.file_name, b.file_name2 "
            + "FROM board b "
            + "LEFT JOIN users u ON b.user_no = u.no "
            + "WHERE " + keyField + " LIKE UPPER(?) "
            + "order by b.no desc";
				
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, "%" + keyWord + "%");
			}
			rs = pstmt.executeQuery();
			// 4.결과처리
			while (rs.next()) {
				int no = rs.getInt("no");
				String title = rs.getString("title");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("reg_date");
				int userNo = rs.getInt("user_no");
				String fileName = rs.getString("file_name");
				String fileName2 = rs.getString("file_name2");
				String userName = rs.getString("name");
				
				BoardVo searching = new BoardVo(no, title, hit, regDate, userNo, fileName, fileName2, userName);
				list.add(searching);
			}
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

		}
		
		return list;

	}

	
	public BoardVo getBoard(int no) {

		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BoardVo boardVo = null;
		
		try {
		  conn = getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "select b.no, b.title, b.content, b.hit, to_char(b.reg_date, 'YY-MM-DD HH24:MI') \"reg_date\", b.user_no, b.file_name, b.orig_file_name, b.file_name2, b.orig_file_name2, u.name "
					     + "from board b, users u "
					     + "where b.user_no = u.no "
					     + "and b.no = ?";
			
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, no);
			
			rs = pstmt.executeQuery();
			// 4.결과처리
			while (rs.next()) {
				String title = rs.getString("title");
				String content = rs.getString("content");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("reg_date");
				int userNo = rs.getInt("user_no");
				String fileName = rs.getString("file_name");
				String origFName = rs.getString("orig_file_name");
				String fileName2 = rs.getString("file_name2");
				String origFName2 = rs.getString("orig_file_name2");
				String userName = rs.getString("name");
				
				boardVo = new BoardVo(no, title, content, hit, regDate, userNo, fileName, origFName, fileName2, origFName2, userName);
			}
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

		}
		System.out.println(boardVo);
		return boardVo;

	}
	
	public int insert(BoardVo vo) {
		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;

		try {
		  conn = getConnection();
		  
		  System.out.println("vo.userNo : ["+vo.getUserNo()+"]");

      System.out.println("vo.title : ["+vo.getTitle()+"]");
      System.out.println("vo.content : ["+vo.getContent()+"]");
      System.out.println("vo.fileName : ["+vo.getFileName()+"]");
      System.out.println("vo.origFName : ["+vo.getOrigFName()+"]");
      System.out.println("vo.fileName2 : ["+vo.getFileName2()+"]");
      System.out.println("vo.origFName2 : ["+vo.getOrigFName2()+"]");

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "insert into board (no, title, content, hit, reg_date, user_no, file_name, orig_file_name, file_name2, orig_file_name2) values (seq_board_no.nextval, ?, ?, 0, sysdate, ?, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContent());
			pstmt.setInt(3, vo.getUserNo());

			pstmt.setString(4, vo.getFileName());
			pstmt.setString(5, vo.getOrigFName());
			pstmt.setString(6, vo.getFileName2());
			pstmt.setString(7, vo.getOrigFName2());

			
      
			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println(count + "건 등록");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

		}

		return count;
	}
	
	
	public int delete(int no) {
		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;

		try {
		  conn = getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "delete from board where no = ?";
			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, no);

			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println(count + "건 삭제");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

		}

		return count;
	}
	
	
	public int update(BoardVo vo) {
		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;

		try {
		  conn = getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "update board set title = ?, content = ? where no = ? ";
			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContent());
			pstmt.setInt(3, vo.getNo());

			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println(count + "건 수정");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

		}

		return count;
	}
	public int hitUpdate(BoardVo vo) {
		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;

		try {
		  conn = getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "update board set hit = ? where no = ? ";
			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, vo.getHit()+1);
			pstmt.setInt(2, vo.getNo());

			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println(count + "건 수정");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

		}

		return count;
	}
	
//	public List<BoardVo> search(BoardVo vo) {
//	    Connection conn = null;
//	    PreparedStatement pstmt = null;
//	    ResultSet rs = null;
//	    List<BoardVo> searching = new ArrayList<>();
//
//	    try {
//	        conn = getConnection();
//
//	        String query = "SELECT b.no, b.title, b.hit, to_char(b.reg_date, 'YY-MM-DD HH24:MI') as reg_date, b.file_name, b.file_name2, u.no as user_no, u.name as user_name "
//	                + "FROM board b "
//	                + "LEFT JOIN users u ON b.user_no = u.no "
//	                + "WHERE TO_CHAR(b.REG_DATE, 'YYMMDD') LIKE ? "
//	                + "OR b.title LIKE ? "
//	                + "OR b.content LIKE ? "
//	                + "OR b.file_name LIKE ? "
//	                + "OR b.file_name2 LIKE ? "
//	                + "OR u.name LIKE ?"
//	                + "order by no desc";
//
//	        pstmt = conn.prepareStatement(query);
//
//	        pstmt.setString(1, "%" + vo.getRegDate() + "%");
//	        pstmt.setString(2, "%" + vo.getTitle() + "%");
//	        pstmt.setString(3, "%" + vo.getContent() + "%");
//	        pstmt.setString(4, "%" + vo.getFileName() + "%");
//	        pstmt.setString(5, "%" + vo.getFileName2() + "%");
//	        pstmt.setString(6, "%" + vo.getUserName() + "%");
//
//	        rs = pstmt.executeQuery();
//
//	        while (rs.next()) {
//	            BoardVo resultVo = new BoardVo();
//	            resultVo.setNo(rs.getInt("no"));
//	            resultVo.setTitle(rs.getString("title"));
//	            resultVo.setHit(rs.getInt("hit"));
//	            resultVo.setRegDate(rs.getString("reg_date"));
//	            resultVo.setFileName(rs.getString("file_name"));
//	            resultVo.setFileName2(rs.getString("file_name2"));
//	            resultVo.setUserNo(rs.getInt("user_no"));
//	            resultVo.setUserName(rs.getString("user_name"));
//
//	            searching.add(resultVo);
//	        }
//
//	        System.out.println(searching.size() + "건 검색");
//
//	    } catch (SQLException e) {
//	        System.out.println("error:" + e);
//	    } finally {
//	        try {
//	            if (rs != null) {
//	                rs.close();
//	            }
//	            if (pstmt != null) {
//	                pstmt.close();
//	            }
//	            if (conn != null) {
//	                conn.close();
//	            }
//	        } catch (SQLException e) {
//	            System.out.println("error:" + e);
//	        }
//	    }
//
//	    return searching;
//	}

}

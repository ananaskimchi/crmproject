package com.javaex.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.javaex.vo.BoardVo;
import com.javaex.vo.UserVo;

public class BoardDaoTest {
	private static Connection getConnection() throws SQLException {
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

	public static void main(String[] args) {
		System.out.println("BoardDao 테스트 실행");
		System.out.print("\n");

		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement userPstmt = null;
		ResultSet rs = null;
		int number = 0;
		int userNumber = 0;

		BoardVo vo = new BoardVo();
		BoardDaoImpl dao = new BoardDaoImpl();
		UserDao userDao = new UserDaoImpl();

		try {
			conn = getConnection();

			System.out.println("게시글 입력을 테스트합니다.");
			System.out.print("\n");

			String user = " SELECT NO, NAME, EMAIL, PASSWORD, GENDER" 
					+ " FROM USERS";

			userPstmt = conn.prepareStatement(user, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = userPstmt.executeQuery();
			rs.last();
			userNumber = rs.getInt("no");

			vo.setUserNo(userNumber);
			vo.setTitle("Test");
			vo.setContent("Test");
			int ins = dao.insert(vo);
			
			if (ins == 1) {
				System.out.println("게시물 입력을 성공했습니다.");
				System.out.print("\n");
				String query = "select b.no, b.title, b.hit, to_char(b.reg_date, 'YY-MM-DD HH24:MI') \"reg_date\", b.user_no, u.name "
						+ " from board b, users u "
						+ " where b.user_no = u.no "
						+ " order by b.no";

				pstmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
				rs = pstmt.executeQuery();
				rs.last();
				number = rs.getInt("no");

				System.out.println("게시물 상세 조회를 테스트합니다.");
				dao.getBoard(number);
				System.out.print("\n");

				System.out.println("게시물 업데이트를 테스트합니다.");
				vo.setTitle("Update");
				vo.setContent("Update");
				vo.setNo(number);
				System.out.print("\n");
				int up = dao.update(vo);
				
				if (up == 1) {
					System.out.println("게시물 업데이트를 성공했습니다.");
					System.out.print("\n");
				} else {
					System.out.println("게시물 업데이트를 실패했습니다.");
				}

				System.out.println("게시물 조회수 수정을 테스트합니다.");
				System.out.print("\n");
				int hit = dao.hitUpdate(vo);
				
				if (hit == 1) {
					System.out.println("게시물 조회수 수정을 성공했습니다.");
				} else {
					System.out.println("게시물 조회수 수정을 실패했습니다.");
				}

				System.out.println("테스트용 게시물을 삭제합니다.");
				System.out.print("\n");
				int del = dao.delete(number);
				if (del == 1) {
					System.out.println("게시물을 삭제하였습니다.");
				} else {
					System.out.println("게시물을 삭제하는 것을 실패했습니다.");
				}

				System.out.print("\n");
				System.out.println("BoardDao 테스트 종료");
			} else {
				System.out.println("게시글 입력에 실패했습니다.");
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
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
	}
}

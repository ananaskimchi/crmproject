package com.javaex.dao;

import java.sql.*;
import java.util.List;

import com.javaex.vo.GuestbookVo;

public class GuestbookDaoTest {
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
		System.out.println("GuestbookDao 테스트 실행");
		System.out.print("\n");

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		GuestbookDaoImpl dao = new GuestbookDaoImpl();
		GuestbookVo vo = new GuestbookVo();
		int number = 0;

		vo.setName("테스트");
		vo.setPassword("1234");
		vo.setContent("테스트입니다");

		int insertCount = dao.insert(vo);

		// 데이터 입력 테스트
		if (insertCount == 1) {
			System.out.println("데이터 입력 이상 없음");
			System.out.print("\n");
			try {
				conn = getConnection();
				// 3. SQL문 준비 / 바인딩 / 실행
				String query = " SELECT no, name, password, content, regdate" 
						+ " FROM GUESTBOOK"
						+ " ORDER BY NO";

				pstmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
				rs = pstmt.executeQuery();
				rs.last();
				number = rs.getInt("no");
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

			vo.setNo(number);
			vo.setPassword("1234");

			// 테스트용 데이터 바로 삭제 및 삭제 테스트
			if (dao.delete(vo) == 1) {
				System.out.println("데이터 삭제 이상 없음");
				System.out.print("\n");
			} else {
				System.out.println("데이터 삭제 문제 발생");
			}
		} else {
			System.out.println("데이터 입력 문제 발생");
		}

		// 전체 목록 보여주기
		List<GuestbookVo> allList = dao.getList();
		System.out.println("모든 데이터 보여주기");
		System.out.println(allList);

		System.out.print("\n");
		System.out.println("GuestbookDao 테스트 종료");
	}
}
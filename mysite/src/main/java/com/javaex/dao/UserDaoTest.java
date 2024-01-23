package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.javaex.vo.UserVo;

public class UserDaoTest {
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
		System.out.println("UserDao 테스트 실행");
		System.out.print("\n");

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int number = 0;

		UserDao dao = new UserDaoImpl();
		UserVo vo = new UserVo(0, "Test", "Test", "Test", "Test");

		// Insert 기능
		System.out.println("User Insert Test");

		if (dao.insert(vo) == 1) {
			System.out.println("User Insert 성공");
			System.out.print("\n");

			// id와 이메일이 일치하는 정보 보여주기
			System.out.println("id와 이메일의 정보가 맞는 회원 보여주기");
			System.out.println(dao.getUser("Test", "Test"));
			System.out.print("\n");

			// 이메일 체크하기
			System.out.println("이메일 체크");
			System.out.println(dao.idCheck("Test"));
			System.out.print("\n");

			try {
				conn = getConnection();
				// 3. SQL문 준비 / 바인딩 / 실행
				String query = " SELECT NO, NAME, EMAIL, PASSWORD, GENDER" 
						+ " FROM USERS";
				pstmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
				rs = pstmt.executeQuery();

				// 커서를 마지막 객체 위치로 이동
				rs.last();
				number = rs.getInt("NO");

				// no가 맞는 회원의 정보 보여주기
				System.out.println("No가 맞는 회원의 정보 보여주기");
				System.out.println(dao.getUser(number));
				System.out.print("\n");

				vo.setNo(number);
				vo.setName("Update");
				vo.setGender("Update");
				// User 업데이트 기능 Test
				System.out.println("User Update Test");

				if (dao.update(vo) == 1) {
					System.out.println("User 업데이트 성공");
					System.out.print("\n");

					String delete = "DELETE FROM USERS WHERE NO= "+number+"";
					// 테스트용 User 데이터 삭제
					pstmt.executeUpdate(delete);
					System.out.println("테스트용 User 데이터 삭제");
				} else {
					System.out.println("User 업데이트 실패");
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
		} else {
			System.out.println("User Insert 실패");
		}

		System.out.print("\n");
		System.out.println("UserDao 테스트 종료");
	}
}
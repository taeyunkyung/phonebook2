package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javaex.vo.PersonVo;

public class PhoneDao {

	// 필드
	// 0. import java.sql.*;
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String id = "phonedb";
	private String pw = "phonedb";

	// 메소드 일반
	private void getConnection() {
		try {
			// 1. JDBC 드라이버 (Oracle) 로딩
			Class.forName(driver);

			// 2. Connection 얻어오기
			conn = DriverManager.getConnection(url, id, pw);

		} catch (ClassNotFoundException e) {
			System.out.println("error: 드라이버 로딩실패 - " + e);
		} catch (SQLException e) {
			System.out.println("error." + e);
		}
	}

	private void close() {
		try {
			// 자원정리
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("error." + e);
		}
	}

	// 등록
	public void personInsert(PersonVo personVo) {
		this.getConnection();

		try {
			String query = "";
			query += "insert into person ";
			query += " values(seq_person_id.nextval, ?, ?, ?)";

			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, personVo.getName());
			pstmt.setString(2, personVo.getHp());
			pstmt.setString(3, personVo.getCompany());

			int count = pstmt.executeUpdate();
			System.out.println("[" + count + "건 등록되었습니다.]");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		this.close();
	}

	// 삭제
	public void personDelete(int index) {
		this.getConnection();

		try {
			String query = "";
			query += "delete from person ";
			query += " where person_id = ?";

			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, index);

			int count = pstmt.executeUpdate();
			System.out.println("[" + count + "건 삭제되었습니다.]");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		this.close();
	}

	// 수정
	public void personUpdate(PersonVo personVo) {
		this.getConnection();

		try {
			String query = "";
			query += "update person ";
			query += " set   name = ?, ";
			query += "       hp = ?, ";
			query += "       company = ? ";
			query += " where person_id = ? ";

			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, personVo.getName());
			pstmt.setString(2, personVo.getHp());
			pstmt.setString(3, personVo.getCompany());
			pstmt.setInt(4, personVo.getPersonId());

			int count = pstmt.executeUpdate();
			System.out.println("[" + count + "건 수정되었습니다.]");

		} catch (SQLException e) {
			System.out.println("error." + e);
		}

		this.close();
	}

	// 리스트 가져오기
	public List<PersonVo> getPersonList() {
		return getPersonList("");
	}

	// 검색
	public List<PersonVo> getPersonList(String keyword) {
		List<PersonVo> pList = new ArrayList<PersonVo>();
		this.getConnection();

		try {
			String query = "";
			query += "select  person_id, ";
			query += "        name, ";
			query += "        hp, ";
			query += "        company ";
			query += " from person ";

			if (keyword != "" || keyword == null) {
				query += " where (name||hp||company) like '%'||?||'%' ";
				query += " order by person_id asc";

				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, keyword);
			} else {
				query += " order by person_id asc";
				pstmt = conn.prepareStatement(query);
			}

			rs = pstmt.executeQuery();

			while (rs.next() == true) {
				int personId = rs.getInt("person_id");
				String name = rs.getString("name");
				String hp = rs.getString("hp");
				String company = rs.getString("company");

				PersonVo vo = new PersonVo(personId, name, hp, company);
				pList.add(vo);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		this.close();
		return pList;
	}

	// 한 사람의 데이터만 가져오기
	public PersonVo getPerson(int personId) {
		PersonVo vo = new PersonVo();
		this.getConnection();

		try {
			String query = "";
			query += "select  name, ";
			query += "        hp, ";
			query += "        company ";
			query += " from person ";
			query += " where person_id = ?";

			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, personId);

			rs = pstmt.executeQuery();

			while (rs.next() == true) {
				String name = rs.getString("name");
				String hp = rs.getString("hp");
				String company = rs.getString("company");

				vo.setName(name);
				vo.setHp(hp);
				vo.setCompany(company);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		this.close();
		return vo;
	}
}
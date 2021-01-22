package com.dozendog.excelreader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnector {
	


	public int insert(String insertSqlStatement,int expectedRows) throws SQLException {

		Connection con = DriverManager.getConnection(Constants.SQL_URL,Constants.SQL_USER, Constants.SQL_PASS);
		System.out.println("# create connection insert statement");
		System.out.println("# expectedRows:"+expectedRows);
		
		int successRow = 0;
		Statement stmt = null;
		try {
			stmt = con.createStatement(); 
			con.setAutoCommit(false);
			stmt.addBatch(insertSqlStatement);
	
			System.out.println("# executeBatch");
			int[] results = stmt.executeBatch();
			
			if(results!=null && results.length>0) {
				successRow = 0;
				for(int result:results) {
					successRow = successRow+result;
				}
			} 
			
			if(successRow==expectedRows) {
				System.out.println("# trying commit");
				con.commit();
				System.out.println("# commit success");
			}else {
				System.out.println("# trying rollback");
				con.rollback();
				System.out.println("# rollback success");
			}
		}catch(SQLException e) {
			e.printStackTrace();
			if(con!=null) {
				con.rollback();
			}
			if(stmt!=null) {
				stmt.close();
			}
			if(con!=null) {
				con.close();
			}
			throw e;
		} 
		
		if(stmt!=null) {
			stmt.close();
		}
		if(con!=null) {
			con.close();
		}
		
		System.out.println("# close connection");

		System.out.println("====>>INSERT_SUCCESS_ROW:"+successRow);		
		return successRow;
	}
	
	public int count(String countSqlstatement) throws SQLException {
		if(!countSqlstatement.toLowerCase().contentEquals("count(")){
			System.out.println("WRONG SQL:"+countSqlstatement);
			return -1;
		}

		Connection con = DriverManager.getConnection(Constants.SQL_URL,Constants.SQL_USER, Constants.SQL_PASS);
		System.out.println("# create connection select count(*) statement");
		System.out.println("SQL:"+countSqlstatement);
		
		Statement stmt = con.createStatement();
		int count = 0;
		
		System.out.println("# executeQuery");
		ResultSet rs = stmt.executeQuery(countSqlstatement);
		if(rs!=null){
			if (rs.next()) {
				count = rs.getInt(1);
			}
		}
		stmt.close();
		con.close();
		System.out.println("# close connection");
		
		System.out.println("====>>COUNT_ROW:"+count);		
		return count;
	}
	

	
	

}

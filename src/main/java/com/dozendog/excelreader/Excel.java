package com.dozendog.excelreader;

public class Excel {
	
	private int numberOfRows = 0;
	private int numberOfDataRows = 0;
	private String insertSqlStatement;
	private String file;
	
	public Excel(String file) {
		this.file = file;
	}
	
	public void processing() {
		this.numberOfRows = 0;
		this.numberOfDataRows = 0;
		this.setInsertSqlStatement("");
	}
	
	

	public int getNumberOfRows() {
		return numberOfRows;
	}

	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

	public int getNumberOfDataRows() {
		return numberOfDataRows;
	}

	public void setNumberOfDataRows(int numberOfDataRows) {
		this.numberOfDataRows = numberOfDataRows;
	}

	public String getInsertSqlStatement() {
		return insertSqlStatement;
	}

	public void setInsertSqlStatement(String insertSqlStatement) {
		this.insertSqlStatement = insertSqlStatement;
	}

}

package com.dozendog.excelreader;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.http.client.ClientProtocolException;

public class ExcelReaderMain {

	public static DatabaseConnector db = new DatabaseConnector();

	public static void main(String[] args) {

		System.out.println("INFO: starting.....");

		if (args == null || args.length <= 0) {
			System.out.println("ERROR: NO PARAMETER !!");
			System.exit(1);
			return;
		}

		String file = null;
		int stage = 0;
		for (int i = 0; i < args.length; i++) {
			
			System.out.println("INFO: args["+i+"]="+args[i]);

			file = (i == 0) ? args[0] : null;
			stage = (i == 1) ? Integer.parseInt(args[1]) : 0;
		}


		// TODO Auto-generated method stub
		String countSqlstatementBefore = System.getenv("COUNT_SQL_STATEMENT");
		String insertSqlStatement = null;
		String countSqlstatementAfter = System.getenv("COUNT_SQL_STATEMENT");
		String countSqlstatementVerify = System.getenv("COUNT_SQL_STATEMENT");
		String countSqlstatementVerifyCrossCheck = System.getenv("CROSSCHECK_COUNT_SQL_STATEMENT");
		
		Constants.ENDPOINT_URL = System.getenv("ENDPOINT_URL");
		Constants.SQL_USER = System.getenv("SQL_USER");
		Constants.SQL_PASS = System.getenv("SQL_PASS");
		Constants.SQL_URL = System.getenv("SQL_URL");
				
		
		System.out.println("INFO: ===========================");
		System.out.println("INFO: fileName=" + file);
		System.out.println("INFO: start in stage=" + stage);
		System.out.println("INFO: ENDPOINT_URL=" + Constants.ENDPOINT_URL);
		System.out.println("INFO: SQL_USER=" + Constants.SQL_USER);
		System.out.println("INFO: SQL_PASS=" + Constants.SQL_PASS);
		System.out.println("INFO: SQL_URL=" + Constants.SQL_URL);
		System.out.println("INFO: COUNT_SQL_STATEMENT=" + System.getenv("COUNT_SQL_STATEMENT"));
		System.out.println("INFO: CROSSCHECK_COUNT_SQL_STATEMENT=" + System.getenv("CROSSCHECK_COUNT_SQL_STATEMENT"));
		System.out.println("INFO: ===========================");
		
		Excel excel = new Excel(file);
		int numberOfDataRows = 0;
		String responseMessage = null;

		if (stage == 0) {
			int countBefore = 99999;
			try {
				countBefore = db.count(countSqlstatementBefore);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
			
			if(countBefore>0) {
				System.out.println("ERROR: stage=" + stage+" failed!!");
				System.exit(1);
			}

			System.out.println("INFO: stage=" + stage+" DONE!!");
			stage++;	
		}

		if (stage == 1) {
			//load excel file
			excel.processing();
			insertSqlStatement = excel.getInsertSqlStatement();
			numberOfDataRows = excel.getNumberOfDataRows();
			int totalRows = excel.getNumberOfRows();
			
			if(insertSqlStatement==null||insertSqlStatement.equals("")) {
				System.out.println("ERROR: stage=" + stage+" failed (1)!!");
				System.exit(1);
			}
			if(numberOfDataRows==0) {
				System.out.println("ERROR: stage=" + stage+" failed (2)!!");
				System.exit(1);
			}
			if(totalRows<=numberOfDataRows) {
				System.out.println("ERROR: stage=" + stage+" failed (3)!!");
				System.exit(1);
			}

			System.out.println("INFO: stage=" + stage+" DONE!!");
			stage++;
		}

		if (stage == 2) {
			int insertSuccess = -1;
			try {
				insertSuccess = db.insert(insertSqlStatement,numberOfDataRows);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
			
			if(insertSuccess<=0) {
				System.out.println("ERROR: stage=" + stage+" failed (1)!!");
				System.exit(1);
			}
			if(insertSuccess!=numberOfDataRows) {
				System.out.println("ERROR: stage=" + stage+" failed (2)!!");
				System.exit(1);
			}
			
			System.out.println("INFO: stage=" + stage+" DONE!!");
			stage++;
		}

		if (stage == 3) {
			int countAfter = -1;
			try {
				countAfter = db.count(countSqlstatementAfter);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
			
			if(countAfter!=numberOfDataRows) {
				System.out.println("ERROR: stage=" + stage+" failed!!");
				System.exit(1);
			}
			
			System.out.println("INFO: stage=" + stage+" DONE!!");
			stage++;
		}

		if (stage == 4) {
			//http client
			
			RestClient client = new RestClient();
			int status = 500;
			try {
				status = client.callRestAPI();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
			responseMessage = client.getResponseMessage();
			
			if(status!=200) {
				System.out.println("ERROR: stage=" + stage+" failed (1)!!");
				System.exit(1);
			}
			if(responseMessage==null||responseMessage.length()<=30) {
				System.out.println("ERROR: stage=" + stage+" failed (2)!!");
				System.exit(1);
			}
			
			System.out.println("INFO: stage=" + stage+" DONE!!");
			stage++;
		}
		
		if (stage == 5) {
			int countVerify = 99999;
			try {
				countVerify = db.count(countSqlstatementVerify);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
			
			if(countVerify>0) {
				System.out.println("ERROR: stage=" + stage+" failed!!");
				System.exit(1);
			}
			
			System.out.println("INFO: stage=" + stage+" DONE!!");
			stage++;
		}
		
		if (stage == 6) {
			int countVerify = 99999;
			try {
				countVerify = db.count(countSqlstatementVerifyCrossCheck);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
			
			if(countVerify>0) {
				System.out.println("ERROR: stage=" + stage+" failed!!");
				System.exit(1);
			}
			
			System.out.println("INFO: stage=" + stage+" DONE!!");
			stage++;
		}

		System.exit(0);

	}
	
	public static String extractDatetime(String message) {
		return "";
	}
	
	public static String buildSQL(String sql,String date,String datetime) {
		return sql.replace("{date}", date).replace("{datetime}", datetime);
	}

}

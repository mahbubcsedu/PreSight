package com.mpss.wheelnav.mysql;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mysql.jdbc.PreparedStatement;


public class UpdateIsPostedToTurkFieldHelper {
	
	private static final String IS_POSTED_TO_TURK_COLUMN_NAME = "isPostedToTurk";
	private static final String REQUEST_ID_COLUMN_NAME = "accessibility_data_id";

	private String readFile(String filename) throws IOException {
		File file = new File(filename);
	    FileInputStream fis = new FileInputStream(file);
	    byte[] data = new byte[(int)file.length()];
	    fis.read(data);
	    fis.close();
	    String s = new String(data, "UTF-8");
	    return s;
	}
	
	public void updatePostedToTurk(String mysqlServer, String mysqlUser, String mysqlUserPassword, String mysqlDatabaseName, String mysqlDbTableName, String isPostedToTurkValue, String successfile) throws IOException {

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;

		String url = "jdbc:mysql://" + mysqlServer + ":3306/" + mysqlDatabaseName;
		String user = mysqlUser;
		String password = mysqlUserPassword;
		String ids = readFile(successfile);
		
		try {
			con = DriverManager.getConnection(url, user, password);
			st = con.createStatement();
			String query = "update " + mysqlDatabaseName + "." + mysqlDbTableName 
						+ " set " + IS_POSTED_TO_TURK_COLUMN_NAME + "="  + isPostedToTurkValue
						+ " where " +  REQUEST_ID_COLUMN_NAME  + " in " +  ids;
			//rs = st.executeQuery(query);
			System.out.println("Executing query :\n" + query);
			
			PreparedStatement updateEXP = (PreparedStatement) con.prepareStatement(query);
			int updateEXP_done = updateEXP.executeUpdate();
			

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(GenerateTurkInputFileHelper.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);

		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(GenerateTurkInputFileHelper.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}
	}

	
}

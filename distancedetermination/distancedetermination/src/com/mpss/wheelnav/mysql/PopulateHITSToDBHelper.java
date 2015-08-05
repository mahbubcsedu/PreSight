package com.mpss.wheelnav.mysql;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mysql.jdbc.PreparedStatement;


public class PopulateHITSToDBHelper {

	private class HIT {
		private String hitId;
		private String hitTypeId;

		public String getHitId() {
			return hitId;
		}
		public void setHitId(String hitId) {
			this.hitId = hitId;
		}
		public String getHitTypeId() {
			return hitTypeId;
		}
		public void setHitTypeId(String hitTypeId) {
			this.hitTypeId = hitTypeId;
		}
	}

	private List<HIT> hits;

	public PopulateHITSToDBHelper() {
		this.hits = new ArrayList<HIT>();
	}	

	private void readQuestionniareInputSuccessFile(String questionnaireInputSuccessFile) {

		int hitcount=0;
		try{
			FileInputStream fstream = new FileInputStream(questionnaireInputSuccessFile);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			if((strLine = br.readLine()) != null)
			{
				while ((strLine = br.readLine()) != null)   {
					String[] temp = strLine.split("\t");
					HIT hit = new HIT();
					hit.setHitId(temp[0]);
					hit.setHitTypeId(temp[1]);
					this.hits.add(hit);
					hitcount++;
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace(); 
		}
		System.out.println("Number of HITS read: " + hitcount);
	}

	private int insertRecordInDatabase(String mysqlServer, String mysqlUser, String mysqlUserPassword, String mysqlDatabaseName, String mysqlDbTableName, HIT record) {

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;

		String url = "jdbc:mysql://" + mysqlServer + ":3306/" + mysqlDatabaseName;
		String user = mysqlUser;
		String password = mysqlUserPassword;

		try {
			con = DriverManager.getConnection(url, user, password);
			st = con.createStatement();

			String query = " insert into " + mysqlDatabaseName + "." + mysqlDbTableName + " "
					+ " (hitid,"
					+ "hittypeid,"
					+ "resultsFetched)"
					+ "values"
					+ "(?, ?, ?)";

			PreparedStatement preparedStmt = (PreparedStatement) con.prepareStatement(query);
			preparedStmt.setString (1, record.getHitId());
			preparedStmt.setString (2, record.getHitTypeId());
			preparedStmt.setInt(3, 0);

			preparedStmt.execute();
			
			return 1;

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(GenerateTurkInputFileHelper.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);
			System.out.println("Could not insert record : " + record.getHitId() + "\t" + record.getHitTypeId());		
			return 0;
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

	public void writeRecordsToDB(String mysqlServer, String mysqlUser, String mysqlUserPassword, String mysqlDatabaseName, String mysqlDbTableName) {
		int cnt=0;
		for(int i=0;i<this.hits.size();i++) {
			cnt = cnt + insertRecordInDatabase(mysqlServer, mysqlUser, mysqlUserPassword, mysqlDatabaseName, mysqlDbTableName, this.hits.get(i));
		}
		System.out.println("Number of HITS inserted into databse:" + cnt);
	}

	public void populateHITSToDB(String mysqlServer, String mysqlUser, String mysqlUserPassword, String mysqlDatabaseName, String mysqlDbTableName, String questionnaireInputSuccessFile) {
		System.out.println("Reading Questionnaire Input Success File....");
		readQuestionniareInputSuccessFile(questionnaireInputSuccessFile);
		System.out.println("Writing HITS to databse....");
		writeRecordsToDB(mysqlServer, mysqlUser, mysqlUserPassword, mysqlDatabaseName, mysqlDbTableName);
	}
}

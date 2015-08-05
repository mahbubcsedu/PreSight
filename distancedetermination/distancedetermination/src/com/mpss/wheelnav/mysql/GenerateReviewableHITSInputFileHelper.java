package com.mpss.wheelnav.mysql;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class GenerateReviewableHITSInputFileHelper {

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
	
	public GenerateReviewableHITSInputFileHelper() {
		this.hits = new ArrayList<HIT>();
	}
	
	public void generateReviableHITSFile(String mysqlServer, String mysqlUser, String mysqlUserPassword, String mysqlDatabaseName, String mysqlDbTableName, String file) {
		System.out.println("Fetching HITS form the database...");
		fetchDatabaseRecords(mysqlServer, mysqlUser, mysqlUserPassword, mysqlDatabaseName, mysqlDbTableName);
		System.out.println("Writing HITS to file....");
		writeHITSToFile(file);
	}
	
	private void fetchDatabaseRecords(String mysqlServer, String mysqlUser, String mysqlUserPassword, String mysqlDatabaseName, String mysqlDbTableName) {

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;

		String url = "jdbc:mysql://" + mysqlServer + ":3306/" + mysqlDatabaseName;
		String user = mysqlUser;
		String password = mysqlUserPassword;
		int hitcount=0;

		try {
			con = DriverManager.getConnection(url, user, password);
			st = con.createStatement();
			String query = "select * from " + mysqlDatabaseName +"." + mysqlDbTableName;
			rs = st.executeQuery(query);

			while (rs.next()) {            	
				HIT hit = new HIT();
				hit.setHitId(rs.getString("hitid"));
				hit.setHitTypeId(rs.getString("hittypeid"));
				this.hits.add(hit);
				hitcount++;
			}

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
		
		System.out.println("Number of HITS fetched from the database: " + hitcount);
	}
	
	private void writeHITSToFile(String file) {

		StringBuilder sb = new StringBuilder();
		String header = "hitid" + "\t" + "hittypeid\n";
		sb.append(header);
		for(int i=0;i<this.hits.size();i++) {
			sb.append(this.hits.get(i).getHitId() + "\t" + this.hits.get(i).getHitTypeId() + "\n");
		}
		writeToFile(file, sb.toString());
	}
	
	private void writeToFile(String file, String text) {
		BufferedWriter writer = null;
		try {
			File output = new File(file);
			writer = new BufferedWriter(new FileWriter(output));
			writer.write(text);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (Exception e) {
			}
		}
	}
}


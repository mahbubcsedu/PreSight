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


public class SaveHITResultsToDBHelper {

	private List<TurkResponseRecord> records;

	public SaveHITResultsToDBHelper() {
		this.records = new ArrayList<TurkResponseRecord>();
	}
	
	public void processTurkOutput(String mysqlServer, String mysqlUser, String mysqlUserPassword, String mysqlDatabaseName, String mysqlDbTableName, String outputFile) {
		System.out.println("Reading Turk Output File....");
		readTurkOutputFile(outputFile);
		System.out.println("Inserting result records into the databse....");
		insertRecords(mysqlServer, mysqlUser, mysqlUserPassword, mysqlDatabaseName, mysqlDbTableName);
	}
	
	private void insertRecords(String mysqlServer, String mysqlUser, String mysqlUserPassword, String mysqlDatabaseName, String mysqlDbTableName) {
		int recordsCount=0;
		for(int i=0;i<this.records.size();i++) {
			recordsCount = recordsCount + insertRecordInDatabase(mysqlServer, mysqlUser, mysqlUserPassword, mysqlDatabaseName, mysqlDbTableName, this.records.get(i));
		}
		System.out.println("Number of result records inserted into database: " + recordsCount);
	}

	private int insertRecordInDatabase(String mysqlServer, String mysqlUser, String mysqlUserPassword, String mysqlDatabaseName, String mysqlDbTableName, TurkResponseRecord record) {
		
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
					+ "title,"
					+ "description,"
					+ "keywords,"
					+ "reward,"
					+ "creationtime,"
					+ "assignments,"
					+ "numavailable,"
					+ "numpending,"
					+ "numcomplete,"
					+ "hitstatus,"
					+ "reviewstatus,"
					+ "annotation," 
					+ "assignmentduration,"
					+ "autoapprovaldelay,"
					+ "hitlifetime,"
					+ "viewhit,"
					+ "assignmentid,"
					+ "workerid,"
					+ "assignmentstatus,"
					+ "autoapprovaltime,"
					+ "assignmentaccepttime,"
					+ "assignmentsubmittime,"
					+ "assignmentapprovaltime,"
					+ "assignmentrejecttime,"
					+ "deadline,"
					+ "feedback,"
					+ "reject,"
					+ "answer_point1_x,"
					+ "answer_point1_y,"
					+ "answer_point2_x,"
					+ "answer_point2_y,"
					+ "answer_point3_x,"
					+ "answer_point3_y,"
					+ "answer_point4_x,"
					+ "answer_point4_y,"
					+ "answer_txtWidth,"
					+ "answer_txtLength,"
					+ "answer_requestID,"
					+ "answer_imageWidth,"
					+ "answer_imageHeight,"
					+ "answer_canvasWidth,"
					+ "answer_canvasHeight,"
					+ "answer_unit)"
					+ "values"
					+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
					+ "?, ?, ?, ?, ?)";

				  PreparedStatement preparedStmt = (PreparedStatement) con.prepareStatement(query);
			      preparedStmt.setString (1,record.getHitid());
			      preparedStmt.setString (2, record.getHittypeid());
			      preparedStmt.setString (3, record.getTitle());
			      preparedStmt.setString (4, record.getDescription());
			      preparedStmt.setString (5, record.getKeywords());
			      preparedStmt.setString (6, record.getReward());
			      preparedStmt.setString (7, record.getCreationtime());
			      preparedStmt.setString (8, record.getAssignments());
			      preparedStmt.setString (9, record.getNumavailable());
			      preparedStmt.setString (10, record.getNumpending());
			      preparedStmt.setString (11, record.getNumcomplete());
			      preparedStmt.setString (12, record.getHitstatus());
			      preparedStmt.setString (13, record.getReviewstatus());
			      preparedStmt.setString (14, record.getAnnotation());
			      preparedStmt.setString (15, record.getAssignmentduration());
			      preparedStmt.setString (16, record.getAutoapprovaldelay());
			      preparedStmt.setString (17, record.getHitlifetime());
			      preparedStmt.setString (18, record.getViewhit());
			      preparedStmt.setString (19, record.getAssignmentid());
			      preparedStmt.setString (20, record.getWorkerid());
			      preparedStmt.setString (21, record.getAssignmentstatus());
			      preparedStmt.setString (22, record.getAutoapprovaltime());
			      preparedStmt.setString (23, record.getAssignmentaccepttime());
			      preparedStmt.setString (24, record.getAssignmentsubmittime());
			      preparedStmt.setString (25, record.getAssignmentapprovaltime());
			      preparedStmt.setString (26, record.getAssignmentrejecttime());
			      preparedStmt.setString (27, record.getDeadline());
			      preparedStmt.setString (28, record.getFeedback());
			      preparedStmt.setString (29, record.getReject());
			      preparedStmt.setString (30, record.getAnswer_point1_x());
			      preparedStmt.setString (31, record.getAnswer_point1_y());
			      preparedStmt.setString (32, record.getAnswer_point2_x());
			      preparedStmt.setString (33, record.getAnswer_point2_y());
			      preparedStmt.setString (34, record.getAnswer_point3_x());
			      preparedStmt.setString (35, record.getAnswer_point3_y());
			      preparedStmt.setString (36, record.getAnswer_point4_x());
			      preparedStmt.setString (37, record.getAnswer_point4_y());
			      preparedStmt.setString (38, record.getAnswer_txtWidth());
			      preparedStmt.setString (39, record.getAnswer_txtLength());
			      preparedStmt.setString (40, record.getAnswer_requestID());
			      preparedStmt.setString (41, record.getAnswer_imageWidth());
			      preparedStmt.setString (42, record.getAnswer_imageHeight());
			      preparedStmt.setString (43, record.getAnswer_canvasWidth());
			      preparedStmt.setString (44, record.getAnswer_canvasHeight());
			      preparedStmt.setString(45,  record.getAnswer_unit());
			      
			      preparedStmt.execute();

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(GenerateTurkInputFileHelper.class.getName());
			//lgr.log(Level.SEVERE, ex.getMessage(), ex);
			System.out.println("Could not insert record : " + record.getHitid() + "\t" + record.getHittypeid() + "\t" +record.getAssignmentid());
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
		return 1;
	}
	
	private void readTurkOutputFile(String outputFile) {
		try{
			FileInputStream fstream = new FileInputStream(outputFile);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			int resultsCount=0;
			String strLine;
			if((strLine = br.readLine()) != null)
			{
				while ((strLine = br.readLine()) != null)   {
					String[] temp = strLine.split("\t");
					TurkResponseRecord record = new TurkResponseRecord();
					try{
					record.setHitid(temp[0].substring(1, temp[0].length()-1));
					record.setHittypeid(temp[1].substring(1, temp[1].length()-1));
					record.setTitle(temp[2].substring(1, temp[2].length()-1));
					record.setDescription(temp[3].substring(1, temp[3].length()-1));
					record.setKeywords(temp[4].substring(1, temp[4].length()-1));
					record.setReward(temp[5].substring(1, temp[5].length()-1));
					record.setCreationtime(temp[6].substring(1, temp[6].length()-1));
					record.setAssignments(temp[7].substring(1, temp[7].length()-1));
					record.setNumavailable(temp[8].substring(1, temp[8].length()-1));
					record.setNumpending(temp[9].substring(1, temp[9].length()-1));
					record.setNumcomplete(temp[10].substring(1, temp[10].length()-1));
					record.setHitstatus(temp[11].substring(1, temp[11].length()-1));
					record.setReviewstatus(temp[12].substring(1, temp[12].length()-1));
					record.setAnnotation(temp[13].substring(1, temp[13].length()-1));
					record.setAssignmentduration(temp[14].substring(1, temp[14].length()-1));
					record.setAutoapprovaldelay(temp[15].substring(1, temp[15].length()-1));
					record.setHitlifetime(temp[16].substring(1, temp[16].length()-1));
					record.setViewhit(temp[17].substring(1, temp[17].length()-1));
					record.setAssignmentid(temp[18].substring(1, temp[18].length()-1));
					record.setWorkerid(temp[19].substring(1, temp[19].length()-1));
					record.setAssignmentstatus(temp[20].substring(1, temp[20].length()-1));
					record.setAutoapprovaltime(temp[21].substring(1, temp[21].length()-1));
					record.setAssignmentaccepttime(temp[22].substring(1, temp[22].length()-1));
					record.setAssignmentsubmittime(temp[23].substring(1, temp[23].length()-1));
					record.setAssignmentapprovaltime(temp[24].substring(1, temp[24].length()-1));
					record.setAssignmentrejecttime(temp[25].substring(1, temp[25].length()-1));
					record.setDeadline(temp[26].substring(1, temp[26].length()-1));
					record.setFeedback(temp[27].substring(1, temp[27].length()-1));
					record.setReject(temp[28].substring(1, temp[28].length()-1));
					record.setAnswer_point1_x(temp[29].substring(1, temp[29].length()-1));
					record.setAnswer_point1_y(temp[30].substring(1, temp[30].length()-1));
					record.setAnswer_point2_x(temp[31].substring(1, temp[31].length()-1));
					record.setAnswer_point2_y(temp[32].substring(1, temp[32].length()-1));
					record.setAnswer_point3_x(temp[33].substring(1, temp[33].length()-1));
					record.setAnswer_point3_y(temp[34].substring(1, temp[34].length()-1));
					record.setAnswer_point4_x(temp[35].substring(1, temp[35].length()-1));
					record.setAnswer_point4_y(temp[36].substring(1, temp[36].length()-1));
					record.setAnswer_unit(temp[37].substring(1, temp[37].length()-1));
					record.setAnswer_txtWidth(temp[38].substring(1, temp[38].length()-1));
					record.setAnswer_txtLength(temp[39].substring(1, temp[39].length()-1));
					record.setAnswer_requestID(temp[40].substring(1, temp[40].length()-1));
					record.setAnswer_imageWidth(temp[41].substring(1, temp[41].length()-1));
					record.setAnswer_imageHeight(temp[42].substring(1, temp[42].length()-1));
					record.setAnswer_canvasWidth(temp[43].substring(1, temp[43].length()-1));
					record.setAnswer_canvasHeight(temp[44].substring(1, temp[44].length()-1));
					records.add(record);
					resultsCount++;
					}
					catch(Exception ex) {
						if(record.getAssignmentid()!=null && record.getAssignmentid().trim().length()!=0) {
							System.out.println("Error while reading assignment: " + record.getAssignmentid());
							//ex.printStackTrace(); 
						}
					}
				}
			}
			System.out.println("\nNumber of result records read from the success file:" + resultsCount);
			in.close();
		}catch (Exception e){
			e.printStackTrace();
		}

	}

}

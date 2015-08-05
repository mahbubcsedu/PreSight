package com.mpss.wheelnav.mysql;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class GenerateTurkInputFileHelper {

	private static final String IS_POSTED_TO_TURK_COLUMN_NAME = "isPostedToTurk";
	private static final String REQUEST_ID_COLUMN_NAME = "accessibility_data_id";
	private static final String IMAGE_NAME_COLUMN_NAME = "image_name";
	private static final String IMAGE_WIDTH_COLUMN_NAME = "image_width";
	private static final String IMAGE_HEIGHT_COLUMN_NAME = "image_height";
	private static final double CANVAS_WIDTH_HORIZONTAL_IMAGE = (626.5 * 2);
	private static final double CANVAS_WIDTH_VERTICAL_IMAGE = 626.5;
	
	private List<TurkInputRecord> horizontalImages;
	private List<TurkInputRecord> verticalImages;

	public GenerateTurkInputFileHelper() {
		this.horizontalImages = new ArrayList<TurkInputRecord>();
		this.verticalImages = new ArrayList<TurkInputRecord>();
	}

	private void fetchDatabaseRecords(String mysqlServer, String mysqlUser, String mysqlUserPassword, String mysqlDatabaseName, String mysqlDbTableName) {

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;

		String url = "jdbc:mysql://" + mysqlServer + ":3306/" + mysqlDatabaseName;
		String user = mysqlUser;
		String password = mysqlUserPassword;
		
		int recordsCount = 0;

		try {
			con = DriverManager.getConnection(url, user, password);
			st = con.createStatement();
			String query = "select * from " + mysqlDatabaseName +"." + mysqlDbTableName + " ad inner join "
					+ mysqlDatabaseName + ".user_info ui on ad.device_uuid = ui.user_info_id";
			rs = st.executeQuery(query);

			while (rs.next()) {            	
				if(rs.getInt(REQUEST_ID_COLUMN_NAME)>0 && rs.getInt(IS_POSTED_TO_TURK_COLUMN_NAME)==0) {
					TurkInputRecord record = new TurkInputRecord();
					record.setImageWidth(rs.getInt(IMAGE_WIDTH_COLUMN_NAME));
					record.setImageHeight(rs.getInt(IMAGE_HEIGHT_COLUMN_NAME));
					record.setImageName(rs.getString(IMAGE_NAME_COLUMN_NAME));
					record.setRequestId(rs.getInt(REQUEST_ID_COLUMN_NAME));
					
					record.setDeviceType(rs.getString("device_type"));
					record.setPitchString(Float.toString(rs.getFloat("pitch")));
					record.setRollString(Float.toString(rs.getFloat("roll")));
					record.setBbleft(Integer.toString(rs.getInt("bouding_box_left")));
					record.setBbright(Integer.toString(rs.getInt("bounding_box_right")));
					record.setBbtop(Integer.toString(rs.getInt("bouding_box_top")));
					record.setBbbottom(Integer.toString(rs.getInt("bounding_box_bottom")));
					
					if(record.getImageWidth()>record.getImageHeight()) {
						if(record.getImageWidth()> CANVAS_WIDTH_HORIZONTAL_IMAGE) {
							record.setCanvasWidth(CANVAS_WIDTH_HORIZONTAL_IMAGE);
							record.setCanvasHeight((record.getImageHeight() * CANVAS_WIDTH_HORIZONTAL_IMAGE)/record.getImageWidth());
						}
						else {
							record.setCanvasHeight(record.getImageHeight());
							record.setCanvasWidth(record.getImageWidth());
						}
						this.horizontalImages.add(record);
					}
					else if(record.getImageHeight()>record.getImageWidth()) {
						if(record.getImageWidth() > CANVAS_WIDTH_VERTICAL_IMAGE) {
							record.setCanvasWidth(CANVAS_WIDTH_VERTICAL_IMAGE);
							record.setCanvasHeight((record.getImageHeight() * CANVAS_WIDTH_VERTICAL_IMAGE)/record.getImageWidth());
						}
						else {
							record.setCanvasWidth(record.getImageWidth());
							record.setCanvasHeight(record.getImageHeight());
						}	
						this.verticalImages.add(record);
						recordsCount++;
					}
				}
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
		
		System.out.println("Number of records fetched from database: " + recordsCount);
	}

	private void writeOutput(String outputFileVertical, String outputFileHorizontal, String successFile) {
		String header = "requestID" + "\t" 
					  + "imageName" + "\t" 
					  + "imageWidth" + "\t" 
					  + "imageHeight" + "\t" 
					  + "canvasWidth" + "\t" 
					  + "canvasHeight" + "\t"
					  + "deviceType" + "\t"
			    	  + "pitch" + "\t"
					  + "roll" + "\t"
					  + "bbleft" + "\t"
					  + "bbright" + "\t"
					  + "bbtop" + "\t"
					  + "bbbottom" + "\n";
		
		StringBuilder sb1 = new StringBuilder();
		sb1.append("(");
		
		StringBuilder sb = new StringBuilder();
		sb.append(header);
		for(int i=0;i<this.verticalImages.size();i++) {
			sb.append(Integer.toString(verticalImages.get(i).getRequestId()) + "\t" 
					+ verticalImages.get(i).getImageName() + "\t" 
					+ Double.toString(verticalImages.get(i).getImageWidth()) + "\t" 
					+ Double.toString(verticalImages.get(i).getImageHeight()) + "\t" 
					+ Double.toString(verticalImages.get(i).getCanvasWidth()) + "\t"
					+ Double.toString(verticalImages.get(i).getCanvasHeight()) + "\t"
					+ verticalImages.get(i).getDeviceType() + "\t"
					+ verticalImages.get(i).getPitchString() + "\t"
					+ verticalImages.get(i).getRollString() + "\t"
					+ verticalImages.get(i).getBbleft() + "\t"
					+ verticalImages.get(i).getBbright() + "\t"
					+ verticalImages.get(i).getBbtop() + "\t"
					+ verticalImages.get(i).getBbbottom() + "\n");
			sb1.append(verticalImages.get(i).getRequestId() + ", ");
		}
		writeToFile(outputFileVertical, sb.toString());

		sb.delete(0, sb.length());
		sb.append(header);
		for(int i=0;i<this.horizontalImages.size();i++) {
			sb.append(horizontalImages.get(i).getRequestId() + "\t" 
					+ horizontalImages.get(i).getImageName() + "\t" 
					+ horizontalImages.get(i).getImageWidth() + "\t" 
					+ horizontalImages.get(i).getImageHeight() + "\t" 
					+ horizontalImages.get(i).getCanvasWidth() + "\t"
					+ horizontalImages.get(i).getCanvasHeight() + "\t"
					+ horizontalImages.get(i).getDeviceType() + "\t"
					+ horizontalImages.get(i).getPitchString() + "\t"
					+ horizontalImages.get(i).getRollString() + "\t"
					+ horizontalImages.get(i).getBbleft() + "\t"
					+ horizontalImages.get(i).getBbright() + "\t"
					+ horizontalImages.get(i).getBbtop() + "\t"
					+ horizontalImages.get(i).getBbbottom() + "\n");

			sb1.append(horizontalImages.get(i).getRequestId() + ", ");
		}
		writeToFile(outputFileHorizontal, sb.toString());
		
		sb1.append("-1)");
		writeToFile(successFile, sb1.toString());

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
	
	public void generateTurkInput(String mysqlServer, String mysqlUser, String mysqlUserPassword, String mysqlDatabaseName, String mysqlDbTableName, String outputFileVertical, String outputFileHorizontal, String successfile) {
		System.out.println("Fetching Database records....");
		fetchDatabaseRecords(mysqlServer, mysqlUser, mysqlUserPassword, mysqlDatabaseName, mysqlDbTableName);
		System.out.println("Writing to file....");
		writeOutput(outputFileVertical, outputFileHorizontal, successfile);
	}
}

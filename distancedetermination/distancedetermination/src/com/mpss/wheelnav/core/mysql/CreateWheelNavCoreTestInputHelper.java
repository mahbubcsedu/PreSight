/*
 * Distance determintaion
 * Author : Amol Deshpande, Mahbubur rahman
 * {date}
 * @author B. P. Hennessey
 * @version 1.0
 */
package com.mpss.wheelnav.core.mysql;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


// TODO: Auto-generated Javadoc
/**
 * The Class CreateWheelNavCoreTestInputHelper.
 */
public class CreateWheelNavCoreTestInputHelper {

	/** The Constant INTRINSIC_PARAMETERS_FILE_NAME. */
	private static final String INTRINSIC_PARAMETERS_FILE_NAME = "intrinsics.xml";
	
	/** The Constant DISTORTION_PARAMETERS_FILE_NAME. */
	private static final String DISTORTION_PARAMETERS_FILE_NAME = "distortion.xml";
	
	/** The Constant WHEELNAV_CORE_OUTPUT_DIRECTORY_NAME. */
	private static final String WHEELNAV_CORE_OUTPUT_DIRECTORY_NAME = "wheelnavcore-output";
	
	/** The Constant MAKE_DIRECTORIES_SCRIPT_NAME. */
	private static final String MAKE_DIRECTORIES_SCRIPT_NAME = "makeWorkingDirectory.sh";
	
	/** The Constant WHEELNAV_CORE_EXECUTABLE_NAME. */
	private static final String WHEELNAV_CORE_EXECUTABLE_NAME = "wheelnavcore-test";
	
	/** The Constant EXECUTE_WHEELNAV_CORE_SCRIPT_NAME. */
	private static final String EXECUTE_WHEELNAV_CORE_SCRIPT_NAME = "executewheelnavcore-test.sh";
	
	/** The Constant OUTPUT_LOCATIONS_FILE_NAME. */
	private static final String OUTPUT_LOCATIONS_FILE_NAME = "outputs.map";

	/**
	 * The Class CameraParameterMapping.
	 */
	private class CameraParameterMapping {
		
		/** The device type. */
		private String deviceType;
		
		/** The camera parameters directory. */
		private String cameraParametersDirectory;

		/**
		 * Gets the device type.
		 *
		 * @return the device type
		 */
		public String getDeviceType() {
			return deviceType;
		}
		
		/**
		 * Sets the device type.
		 *
		 * @param deviceType the new device type
		 */
		public void setDeviceType(String deviceType) {
			this.deviceType = deviceType;
		}
		
		/**
		 * Gets the camera parameters directory.
		 *
		 * @return the camera parameters directory
		 */
		public String getCameraParametersDirectory() {
			return cameraParametersDirectory;
		}
		
		/**
		 * Sets the camera parameters directory.
		 *
		 * @param cameraParametersDirectory the new camera parameters directory
		 */
		public void setCameraParametersDirectory(String cameraParametersDirectory) {
			this.cameraParametersDirectory = cameraParametersDirectory;
		}
	}

	/** The mappings. */
	private HashMap<String, CameraParameterMapping> mappings;
	
	/** The records. */
	private List<WheelNavCoreInputRecord> records;

	/**
	 * Instantiates a new creates the wheel nav core test input helper.
	 */
	public CreateWheelNavCoreTestInputHelper() {
		mappings = new HashMap<String, CameraParameterMapping>();
		records = new ArrayList<WheelNavCoreInputRecord>();
	}

	/**
	 * Read camera parameter mappings.
	 *
	 * @param cameraParametersFile the camera parameters file
	 */
	private void readCameraParameterMappings(String cameraParametersFile) {
		int mappingCount=0;
		try{
			FileInputStream fstream = new FileInputStream(cameraParametersFile);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null)   {
				String[] temp = strLine.split("\t");
				CameraParameterMapping mapping = new CameraParameterMapping();
				mapping.setDeviceType(temp[0]);
				mapping.setCameraParametersDirectory(temp[1]);
				mappings.put(temp[0].trim().toUpperCase(), mapping);
				mappingCount++;
			}

		} catch(Exception ex) {
			ex.printStackTrace(); 
		}
		System.out.println("Number of camera parameters mappings read: " + mappingCount);
	}

	/**
	 * Retrieve database records.
	 *
	 * @param mysqlServer the mysql server
	 * @param mysqlUser the mysql user
	 * @param mysqlUserPassword the mysql user password
	 * @param mysqlDatabaseName the mysql database name
	 * @param inputImagesDirectory the input images directory
	 * @param workingDirectory the working directory
	 */
	private void retrieveDatabaseRecords(String mysqlServer, String mysqlUser, String mysqlUserPassword, String mysqlDatabaseName, String inputImagesDirectory, String workingDirectory) {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;

		String url = "jdbc:mysql://" + mysqlServer + ":3306/" + mysqlDatabaseName;
		String user = mysqlUser;
		String password = mysqlUserPassword;
		int recordsCount=0;

		List<Integer> recordsWithNoDeviceType = new ArrayList<Integer>();
		List<Integer> recordWithNoCameraParameters = new ArrayList<Integer>();
		List<Integer> recordsWithNoMeasurements = new ArrayList<Integer>();

		try {
			con = DriverManager.getConnection(url, user, password);
			st = con.createStatement();
			String query = "select * from "+ mysqlDatabaseName + ".accesspath_data ad inner join " + mysqlDatabaseName + ".user_info ui on ad.device_uuid = ui.user_info_id";
			rs = st.executeQuery(query);

			while (rs.next()) {            	
				boolean isValid = true;
				WheelNavCoreInputRecord record = new WheelNavCoreInputRecord();
				record.setRequestId(Integer.toString(rs.getInt("accessibility_data_id")));
				record.setHitId("NA");
				record.setAssignmentId("NA");
				record.setWorkerId("NA");
				record.setInputImageFilePath(inputImagesDirectory + "/" + rs.getString("image_name"));
				record.setOutputDirectory(workingDirectory + "/" + WHEELNAV_CORE_OUTPUT_DIRECTORY_NAME + "/coreOutput_" + record.getRequestId());
				record.setOutputImageName("photo" + Integer.toString(rs.getInt("accessibility_data_id")));

				String deviceType = rs.getString("device_type");
				if(deviceType!=null && deviceType.trim().length()>0) {
					if(mappings.containsKey(deviceType.trim().toUpperCase())) {
						String dir = mappings.get(deviceType.trim().toUpperCase()).getCameraParametersDirectory();
						record.setIntrinsicParameterFile(dir + "/" + INTRINSIC_PARAMETERS_FILE_NAME);
						record.setDistortionParameterFile(dir + "/" + DISTORTION_PARAMETERS_FILE_NAME);
					}
					else {
						recordWithNoCameraParameters.add(Integer.parseInt(record.getRequestId()));	
						isValid=false;
					}
				}
				else {
					recordsWithNoDeviceType.add(Integer.parseInt(record.getRequestId()));
					isValid=false;
				}

				int image_height = rs.getInt("image_height");
				int image_width = rs.getInt("image_width");
				double pitch = Math.abs(rs.getFloat("pitch"));
				double roll = Math.abs(rs.getFloat("roll"));

				if(image_height>image_width) {
					record.setTheta(90-roll);
				}
				else if(image_width>image_height) {
					record.setTheta(90-pitch);
				}

				String txtComment = rs.getString("text_comment");
				if(txtComment.contains("#")) {
					String[] temp = txtComment.split("#");
					if(temp!=null && temp.length==5) {
						record.setMeasuredHeight(Double.parseDouble(temp[0]) * 25.4);
						record.setMeasuredLengthOfTile(Double.parseDouble(temp[1]) * 25.4);
						record.setMeasuredWidthOfTile(Double.parseDouble(temp[2]) * 25.4);
						record.setMeasuredDistanceToTile(Double.parseDouble(temp[3]) * 25.4);
						record.setMeasuredDistanceToIssue(Double.parseDouble(temp[4]) * 25.4);
					}
					else {
						recordsWithNoMeasurements.add(Integer.parseInt(record.getRequestId()));
						isValid = false;
					}
				}
				else {
					recordsWithNoMeasurements.add(Integer.parseInt(record.getRequestId()));
					isValid = false;
				}

				record.setSidewalkTileLength(0);
				record.setSidewalkTileWidth(0);

				int bBoxLeft, bBoxTop, bBoxRight, bBoxBottom;
				bBoxLeft = rs.getInt("bouding_box_left");
				bBoxTop = rs.getInt("bouding_box_top");
				bBoxRight = rs.getInt("bounding_box_right");
				bBoxBottom = rs.getInt("bounding_box_bottom");

				record.setBbPoint1_x(bBoxLeft);
				record.setBbPoint1_y(bBoxTop);

				record.setBbPoint2_x(bBoxRight);
				record.setBbPoint2_y(bBoxTop);

				record.setBbPoint3_x(bBoxRight);
				record.setBbPoint3_y(bBoxBottom);

				record.setBbPoint4_x(bBoxLeft);
				record.setBbPoint4_y(bBoxBottom);

				if(isValid) {
					this.records.add(record);
				}
				recordsCount++;
			}

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(CreateWheelNavCoreTestInputHelper.class.getName());
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
				Logger lgr = Logger.getLogger(CreateWheelNavCoreTestInputHelper.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}

		System.out.println("");
		System.out.println("\t		Records fetched from the database: " + recordsCount);
		System.out.println("\t                    Valid Records count: " + records.size());
		System.out.println("\t      Records with no device type count: " + recordsWithNoDeviceType.size());
		System.out.println("\tRecords with no camera parameters count: " + recordWithNoCameraParameters.size());
		System.out.println("\t     Records with no measurements count: " + recordsWithNoMeasurements.size());
		System.out.println("");

		StringBuilder sbNoDeviceType = new StringBuilder();
		for(int s : recordsWithNoDeviceType) {
			sbNoDeviceType.append("\t" + Integer.toString(s));
		}
		StringBuilder sbNoCamParams = new StringBuilder();
		for(int s : recordWithNoCameraParameters) {
			sbNoCamParams.append("\t" + Integer.toString(s));
		}
		StringBuilder sbNoMeasurements = new StringBuilder();
		for(int s : recordsWithNoMeasurements) {
			sbNoMeasurements.append("\t" + Integer.toString(s));
		}

		System.out.println("\nRecords with no device type: \n" + sbNoDeviceType.toString());
		System.out.println("\nRecords with no camera parameters type: \n" + sbNoCamParams.toString());
		System.out.println("\nRecords with no measurements: \n" + sbNoMeasurements.toString());
	}

	/**
	 * Write to file.
	 *
	 * @param file the file
	 * @param text the text
	 */
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

	/**
	 * Write make directories script.
	 *
	 * @param workingDirectory the working directory
	 * @param outputDirectory the output directory
	 */
	private void writeMakeDirectoriesScript(String workingDirectory, String outputDirectory) {
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<records.size();i++) {
			sb.append("\nmkdir -p " + workingDirectory + "/" + WHEELNAV_CORE_OUTPUT_DIRECTORY_NAME + "/coreOutput_" + records.get(i).getRequestId());
		}
		writeToFile(outputDirectory + "/" + MAKE_DIRECTORIES_SCRIPT_NAME, sb.toString());
	}

	/**
	 * Write wheel nav core execution script.
	 *
	 * @param outputDirectory the output directory
	 */
	private void writeWheelNavCoreExecutionScript(String outputDirectory) {
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<records.size();i++) {
			sb.append("\n\n./" + WHEELNAV_CORE_EXECUTABLE_NAME + " ");
			sb.append(records.get(i).getRecordString("TEST"));
		}
		writeToFile(outputDirectory + "/" + EXECUTE_WHEELNAV_CORE_SCRIPT_NAME, sb.toString());
	}

	/**
	 * Write output locations file.
	 *
	 * @param workingDirectory the working directory
	 * @param outputDirectory the output directory
	 */
	private void writeOutputLocationsFile(String workingDirectory, String outputDirectory) {
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<records.size();i++) {
			sb.append("\n" + workingDirectory + "/" + WHEELNAV_CORE_OUTPUT_DIRECTORY_NAME + "/coreOutput_" + records.get(i).getRequestId() + "/output_" + records.get(i).getRequestId() + ".xls");
		}
		writeToFile(outputDirectory + "/" + OUTPUT_LOCATIONS_FILE_NAME, sb.toString());
	}

	/**
	 * Creates the wheel nav core test inputgenerate turk input.
	 *
	 * @param mysqlServer the mysql server
	 * @param mysqlUser the mysql user
	 * @param mysqlUserPassword the mysql user password
	 * @param mysqlDatabaseName the mysql database name
	 * @param cameraParametersMappingFile the camera parameters mapping file
	 * @param inputImagesDirectory the input images directory
	 * @param workingDirectory the working directory
	 * @param outputDirectory the output directory
	 */
	public void createWheelNavCoreTestInputgenerateTurkInput(String mysqlServer, String mysqlUser, String mysqlUserPassword, String mysqlDatabaseName, String cameraParametersMappingFile, String inputImagesDirectory, String workingDirectory, String outputDirectory) {
		System.out.println("Reading camera parameters mapping...");
		readCameraParameterMappings(cameraParametersMappingFile);
		System.out.println("Retrieving database records...");
		retrieveDatabaseRecords(mysqlServer, mysqlUser, mysqlUserPassword, mysqlDatabaseName, inputImagesDirectory, workingDirectory);
		System.out.println("Writing scripts...");
		writeMakeDirectoriesScript(workingDirectory, outputDirectory);
		writeWheelNavCoreExecutionScript(outputDirectory);
		System.out.println("Writing output locaitons file...");
		writeOutputLocationsFile(workingDirectory, outputDirectory);
		System.out.println("DONE");
	}
}

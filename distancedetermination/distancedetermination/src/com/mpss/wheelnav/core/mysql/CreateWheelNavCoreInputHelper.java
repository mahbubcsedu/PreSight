/*
 * Distance determintaion
 * Author : Amol Deshpande, Mahbubur rahman
 * {date}
 * @author B. P. Hennessey
 * @version 1.0
 */
package com.mpss.wheelnav.core.mysql;

import java.awt.Point;
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
 * The Class CreateWheelNavCoreInputHelper.
 */
public class CreateWheelNavCoreInputHelper {

	/** The Constant INTRINSIC_PARAMETERS_FILE_NAME. */
	private static final String INTRINSIC_PARAMETERS_FILE_NAME = "intrinsics.xml";
	
	/** The Constant DISTORTION_PARAMETERS_FILE_NAME. */
	private static final String DISTORTION_PARAMETERS_FILE_NAME = "distortion.xml";
	
	/** The Constant WHEELNAV_CORE_OUTPUT_DIRECTORY_NAME. */
	private static final String WHEELNAV_CORE_OUTPUT_DIRECTORY_NAME = "wheelnavcore-output";
	
	/** The Constant MAKE_DIRECTORIES_SCRIPT_NAME. */
	private static final String MAKE_DIRECTORIES_SCRIPT_NAME = "makeWorkingDirectory.sh";
	
	/** The Constant WHEELNAV_CORE_EXECUTABLE_NAME. */
	private static final String WHEELNAV_CORE_EXECUTABLE_NAME = "wheelnavcore";
	
	/** The Constant EXECUTE_WHEELNAV_CORE_SCRIPT_NAME. */
	private static final String EXECUTE_WHEELNAV_CORE_SCRIPT_NAME = "executewheelnavcore.sh";
	
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
		public void setCameraParametersDirectory(
				String cameraParametersDirectory) {
			this.cameraParametersDirectory = cameraParametersDirectory;
		}
	}

	/** The mappings. */
	private HashMap<String, CameraParameterMapping> mappings;
	
	/** The records. */
	private List<WheelNavCoreInputRecord> records;

	/**
	 * Instantiates a mapping hashmap where string as key and mappings as value. Key could be like S4, MOTOG or NEXUS.
	 */
	public CreateWheelNavCoreInputHelper() {
		mappings = new HashMap<String, CameraParameterMapping>();
		records = new ArrayList<WheelNavCoreInputRecord>();
	}

	/**
	 * Read camera parameter mappings from parameter file, here in the example
	 * its mappings file only. The device type are listed here only NEXUS, S4 and MOTOG
	 * and its parameters are listed in the directory.The parameter types are 
	 * normal parameters, intrinsic and distortion all are xml file.
	 *
	 * @param cameraParametersFile the camera parameters file 
	 * which is send as input to the functions to look for parameters
	 */
	private void readCameraParameterMappings(String cameraParametersFile) {
		int mappingCount = 0;
		try {
			FileInputStream fstream = new FileInputStream(cameraParametersFile);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				String[] temp = strLine.split("\t");
				CameraParameterMapping mapping = new CameraParameterMapping();
				mapping.setDeviceType(temp[0]);
				mapping.setCameraParametersDirectory(temp[1]);
				mappings.put(temp[0].trim().toUpperCase(), mapping);
				mappingCount++;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println("Number of camera parameters mappings read: "
				+ mappingCount);
	}

	/**
	 * Rearrange sidewalk tile points.
	 *
	 * @param stPoints the st points
	 * @return the point[]
	 */
	private Point[] rearrangeSidewalkTilePoints(Point[] stPoints) {
		int firstPointIndex = -1;
		int lastPointIndex = -1;
		int firstSmallest = -1;
		int secondSmallest = -1;
		for (int i = 0; i < 4; i++) {
			if (firstSmallest == -1) {
				firstSmallest = i;
			} else if (secondSmallest == -1) {
				if (stPoints[i].x < stPoints[firstSmallest].x) {
					secondSmallest = firstSmallest;
					firstSmallest = i;
				} else {
					secondSmallest = i;
				}
			} else {
				if (stPoints[i].x < stPoints[secondSmallest].x) {
					secondSmallest = i;
					if (stPoints[i].x < stPoints[firstSmallest].x) {
						secondSmallest = firstSmallest;
						firstSmallest = i;
					}
				}
			}
		}

		if (stPoints[firstSmallest].y > stPoints[secondSmallest].y) {
			firstPointIndex = secondSmallest;
			lastPointIndex = firstSmallest;
		} else {
			firstPointIndex = firstSmallest;
			lastPointIndex = secondSmallest;
		}

		Point[] newStPoints = new Point[4];
		newStPoints[0] = stPoints[firstPointIndex];
		newStPoints[3] = stPoints[lastPointIndex];

		if (firstPointIndex == 0 && lastPointIndex == 1) {
			newStPoints[1] = stPoints[3];
			newStPoints[2] = stPoints[2];
		} else if (firstPointIndex == 1 && lastPointIndex == 0) {
			newStPoints[1] = stPoints[2];
			newStPoints[2] = stPoints[3];
		} else if (firstPointIndex == 1 && lastPointIndex == 2) {
			newStPoints[1] = stPoints[0];
			newStPoints[2] = stPoints[3];
		} else if (firstPointIndex == 2 && lastPointIndex == 1) {
			newStPoints[1] = stPoints[3];
			newStPoints[2] = stPoints[0];
		} else if (firstPointIndex == 2 && lastPointIndex == 3) {
			newStPoints[1] = stPoints[1];
			newStPoints[2] = stPoints[0];
		} else if (firstPointIndex == 3 && lastPointIndex == 2) {
			newStPoints[1] = stPoints[0];
			newStPoints[2] = stPoints[1];
		} else if (firstPointIndex == 0 && lastPointIndex == 3) {
			newStPoints[1] = stPoints[1];
			newStPoints[2] = stPoints[2];
		} else if (firstPointIndex == 3 && lastPointIndex == 0) {
			newStPoints[1] = stPoints[2];
			newStPoints[2] = stPoints[1];
		}

		/*
		 * if(firstPointIndex>lastPointIndex) { int k=firstPointIndex; k++;
		 * if(k==4) {k=0;} newStPoints[1]=stPoints[k]; k++; if(k==4) {k=0;}
		 * newStPoints[2]=stPoints[k]; } else { int k=lastPointIndex; k++;
		 * if(k==4) {k=0;} newStPoints[2]=stPoints[k]; k++; if(k==4) {k=0;}
		 * newStPoints[1]=stPoints[k]; }
		 */

		return newStPoints;
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
	private void retrieveDatabaseRecords(String mysqlServer, String mysqlUser,
			String mysqlUserPassword, String mysqlDatabaseName,
			String inputImagesDirectory, String workingDirectory) {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;

		String url = "jdbc:mysql://" + mysqlServer + ":3306/"
				+ mysqlDatabaseName;
		String user = mysqlUser;
		String password = mysqlUserPassword;
		int recordsCount = 0;
		String driver = "com.mysql.jdbc.Driver";
		
		List<String> recordsWithNoDeviceType = new ArrayList<String>();
		List<String> recordWithNoCameraParameters = new ArrayList<String>();
		List<String> recordsWithNoMeasurements = new ArrayList<String>();

		try {
			try {
				Class.forName(driver).newInstance();
			} catch (InstantiationException | IllegalAccessException
					| ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			con = DriverManager.getConnection(url, user, password);
			st = con.createStatement();

			String query = "select ad.*, re.*, ui.* from "
					+ mysqlDatabaseName
					+ ".AMTSidewalkTileAnnotationResults re "
					+ "inner join "
					+ "(select *, cast(accessibility_data_id as CHAR(500)) as requestID from "
					+ mysqlDatabaseName
					+ ".accesspath_data) as ad "
					+ "inner join "
					+ mysqlDatabaseName
					+ ".user_info ui "
					+ "on re.answer_requestID = ad.requestID and ad.device_uuid = ui.user_info_id";

			/*
			 * String query = "select ad.*, re.*, ui.* from " + " (select  *, "
			 * + " (CASE WHEN answer_unit = 'FOOT' THEN " +
			 * "	(CAST(answer_txtLength AS UNSIGNED) * 304.8) " +
			 * "	WHEN answer_unit = 'INCH' THEN " +
			 * "	(CAST(answer_txtLength AS UNSIGNED) * 25.4) " +
			 * "	WHEN answer_unit = 'CENTIMETER' THEN " +
			 * "	(CAST(answer_txtLength AS UNSIGNED) * 10) " +
			 * " END) AS LENGTH , " + " (CASE WHEN answer_unit = 'FOOT' THEN " +
			 * "	(CAST(answer_txtWidth AS UNSIGNED) * 304.8) " +
			 * "	WHEN answer_unit = 'INCH' THEN " +
			 * "	(CAST(answer_txtWidth AS UNSIGNED) * 25.4) " +
			 * "	WHEN answer_unit = 'CENTIMETER' THEN " +
			 * "	(CAST(answer_txtWidth AS UNSIGNED) * 10) " + " END) AS WIDTH "
			 * + " from " + mysqlDatabaseName +
			 * ".AMTSidewalkTileAnnotationResults " +
			 * " HAVING (LENGTH > 889 AND LENGTH < 1905) AND (WIDTH > 889 AND WIDTH < 1905) "
			 * + " ) " + " as re " + " inner join " +
			 * " (select *, cast(accessibility_data_id as CHAR(500)) as requestID from "
			 * + mysqlDatabaseName + ".accesspath_data) as ad " + " inner join "
			 * + mysqlDatabaseName + ".user_info ui " +
			 * " on re.answer_requestID = ad.requestID and ad.device_uuid = ui.user_info_id "
			 * ;
			 */
			rs = st.executeQuery(query);

			while (rs.next()) {
				boolean isValid = true;
				WheelNavCoreInputRecord record = new WheelNavCoreInputRecord();
				record.setRequestId(Integer.toString(rs
						.getInt("accessibility_data_id")));
				record.setHitId(rs.getString("hitid"));
				record.setAssignmentId(rs.getString("assignmentid"));
				record.setWorkerId(rs.getString("workerid"));
				record.setInputImageFilePath(inputImagesDirectory + "/"
						+ rs.getString("image_name"));
				record.setOutputDirectory(workingDirectory + "/"
						+ WHEELNAV_CORE_OUTPUT_DIRECTORY_NAME + "/coreOutput_"
						+ record.getAssignmentId());
				record.setOutputImageName("photo"
						+ Integer.toString(rs.getInt("accessibility_data_id")));

				String deviceType = rs.getString("device_type");
				if (deviceType != null && deviceType.trim().length() > 0) {
					if (mappings.containsKey(deviceType.trim().toUpperCase())) {
						String dir = mappings.get(
								deviceType.trim().toUpperCase())
								.getCameraParametersDirectory();
						record.setIntrinsicParameterFile(dir + "/"
								+ INTRINSIC_PARAMETERS_FILE_NAME);
						record.setDistortionParameterFile(dir + "/"
								+ DISTORTION_PARAMETERS_FILE_NAME);
					} else {
						recordWithNoCameraParameters.add(record
								.getAssignmentId());
						isValid = false;
					}
				} else {
					recordsWithNoDeviceType.add(record.getAssignmentId());
					isValid = false;
				}

				int image_height = rs.getInt("image_height");
				int image_width = rs.getInt("image_width");
				double pitch = Math.abs(rs.getFloat("pitch"));
				double roll = Math.abs(rs.getFloat("roll"));

				if (image_height > image_width) {
					record.setTheta(90 - roll);
				} else if (image_width > image_height) {
					record.setTheta(90 - pitch);
				}

				String txtComment = rs.getString("text_comment");
				if (txtComment.contains("#")) {
					String[] temp = txtComment.split("#");
					if (temp != null && temp.length == 5) {
						record.setMeasuredHeight(Double.parseDouble(temp[0]) * 25.4);
						record.setMeasuredLengthOfTile(Double
								.parseDouble(temp[1]) * 25.4);
						record.setMeasuredWidthOfTile(Double
								.parseDouble(temp[2]) * 25.4);
						record.setMeasuredDistanceToTile(Double
								.parseDouble(temp[3]) * 25.4);
						record.setMeasuredDistanceToIssue(Double
								.parseDouble(temp[4]) * 25.4);
					} else {
						recordsWithNoMeasurements.add(record.getRequestId());
						isValid = false;
					}
				} else {
					recordsWithNoMeasurements.add(record.getRequestId());
					isValid = false;
				}

				String answer_unit = rs.getString("answer_unit");
				double st_length = Double.parseDouble(rs
						.getString("answer_txtLength"));
				double st_width = Double.parseDouble(rs
						.getString("answer_txtWidth"));

				if (answer_unit.trim().toUpperCase().equals("FOOT")) {
					record.setSidewalkTileLength(st_length * 309.4);
					record.setSidewalkTileWidth(st_width * 309.4);
				} else if (answer_unit.trim().toUpperCase().equals("INCH")) {
					record.setSidewalkTileLength(st_length * 25.4);
					record.setSidewalkTileWidth(st_width * 25.4);
				} else if (answer_unit.trim().toUpperCase()
						.equals("CENTIMETER")) {
					record.setSidewalkTileLength(st_length * 10);
					record.setSidewalkTileWidth(st_width * 10);
				}

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

				float canvasHeight = Float.parseFloat(rs
						.getString("answer_canvasHeight"));
				float canvasWidth = Float.parseFloat(rs
						.getString("answer_canvasWidth"));
				float imageHeight = Float.parseFloat(rs
						.getString("answer_imageHeight"));
				float imageWidth = Float.parseFloat(rs
						.getString("answer_imageWidth"));

				float answer_point1_x = Float.parseFloat(rs
						.getString("answer_point1_x"));
				float answer_point1_y = Float.parseFloat(rs
						.getString("answer_point1_y"));
				float answer_point2_x = Float.parseFloat(rs
						.getString("answer_point2_x"));
				float answer_point2_y = Float.parseFloat(rs
						.getString("answer_point2_y"));
				float answer_point3_x = Float.parseFloat(rs
						.getString("answer_point3_x"));
				float answer_point3_y = Float.parseFloat(rs
						.getString("answer_point3_y"));
				float answer_point4_x = Float.parseFloat(rs
						.getString("answer_point4_x"));
				float answer_point4_y = Float.parseFloat(rs
						.getString("answer_point4_y"));

				Point[] stPoints = new Point[4];

				stPoints[0] = new Point();
				stPoints[0].x = (int) (answer_point1_x * imageWidth / canvasWidth);
				stPoints[0].y = (int) (answer_point1_y * imageHeight / canvasHeight);
				stPoints[1] = new Point();
				stPoints[1].x = (int) (answer_point2_x * imageWidth / canvasWidth);
				stPoints[1].y = (int) (answer_point2_y * imageHeight / canvasHeight);
				stPoints[2] = new Point();
				stPoints[2].x = (int) (answer_point3_x * imageWidth / canvasWidth);
				stPoints[2].y = (int) (answer_point3_y * imageHeight / canvasHeight);
				stPoints[3] = new Point();
				stPoints[3].x = (int) (answer_point4_x * imageWidth / canvasWidth);
				stPoints[3].y = (int) (answer_point4_y * imageHeight / canvasHeight);

				Point[] newSTPoints = rearrangeSidewalkTilePoints(stPoints);

				record.setStPoint1_x(newSTPoints[0].x);
				record.setStPoint1_y(newSTPoints[0].y);

				record.setStPoint2_x(newSTPoints[1].x);
				record.setStPoint2_y(newSTPoints[1].y);

				record.setStPoint3_x(newSTPoints[2].x);
				record.setStPoint3_y(newSTPoints[2].y);

				record.setStPoint4_x(newSTPoints[3].x);
				record.setStPoint4_y(newSTPoints[3].y);

				if (isValid) {
					this.records.add(record);
				}
				recordsCount++;
			}

		} catch (SQLException ex) {
			Logger lgr = Logger
					.getLogger(CreateWheelNavCoreTestInputHelper.class
							.getName());
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
				Logger lgr = Logger
						.getLogger(CreateWheelNavCoreTestInputHelper.class
								.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}

		System.out.println("");
		System.out.println("\t		Records fetched from the database: "
				+ recordsCount);
		System.out.println("\t                    Valid Records count: "
				+ records.size());
		System.out.println("\t      Records with no device type count: "
				+ recordsWithNoDeviceType.size());
		System.out.println("\tRecords with no camera parameters count: "
				+ recordWithNoCameraParameters.size());
		System.out.println("\t     Records with no measurements count: "
				+ recordsWithNoMeasurements.size());
		System.out.println("");

		StringBuilder sbNoDeviceType = new StringBuilder();
		for (String s : recordsWithNoDeviceType) {
			sbNoDeviceType.append("\t" + s);
		}
		StringBuilder sbNoCamParams = new StringBuilder();
		for (String s : recordWithNoCameraParameters) {
			sbNoCamParams.append("\t" + s);
		}
		StringBuilder sbNoMeasurements = new StringBuilder();
		for (String s : recordsWithNoMeasurements) {
			sbNoMeasurements.append("\t" + s);
		}

		System.out.println("\nRecords with no device type: \n"
				+ sbNoDeviceType.toString());
		System.out.println("\nRecords with no camera parameters type: \n"
				+ sbNoCamParams.toString());
		System.out.println("\nRecords with no measurements: \n"
				+ sbNoMeasurements.toString());
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
	private void writeMakeDirectoriesScript(String workingDirectory,
			String outputDirectory) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < records.size(); i++) {
			sb.append("\nmkdir -p " + workingDirectory + "/"
					+ WHEELNAV_CORE_OUTPUT_DIRECTORY_NAME + "/coreOutput_"
					+ records.get(i).getAssignmentId());
		}
		writeToFile(outputDirectory + "/" + MAKE_DIRECTORIES_SCRIPT_NAME,
				sb.toString());
	}

	/**
	 * Write wheel nav core execution script.
	 *
	 * @param outputDirectory the output directory
	 */
	private void writeWheelNavCoreExecutionScript(String outputDirectory) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < records.size(); i++) {
			sb.append("\n\n./" + WHEELNAV_CORE_EXECUTABLE_NAME + " ");
			sb.append(records.get(i).getRecordString("CORE"));
		}
		writeToFile(outputDirectory + "/" + EXECUTE_WHEELNAV_CORE_SCRIPT_NAME,
				sb.toString());
	}

	/**
	 * Write output locations file.
	 *
	 * @param workingDirectory the working directory
	 * @param outputDirectory the output directory
	 */
	private void writeOutputLocationsFile(String workingDirectory,
			String outputDirectory) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < records.size(); i++) {
			sb.append("\n" + workingDirectory + "/"
					+ WHEELNAV_CORE_OUTPUT_DIRECTORY_NAME + "/coreOutput_"
					+ records.get(i).getAssignmentId() + "/output_"
					+ records.get(i).getRequestId() + ".xls");
		}
		writeToFile(outputDirectory + "/" + OUTPUT_LOCATIONS_FILE_NAME,
				sb.toString());
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
	public void createWheelNavCoreTestInputgenerateTurkInput(
			String mysqlServer, String mysqlUser, String mysqlUserPassword,
			String mysqlDatabaseName, String cameraParametersMappingFile,
			String inputImagesDirectory, String workingDirectory,
			String outputDirectory) {
		System.out.println("Reading camera parameters mapping...");
		readCameraParameterMappings(cameraParametersMappingFile);
		System.out.println("Retrieving database records...");
		retrieveDatabaseRecords(mysqlServer, mysqlUser, mysqlUserPassword,
				mysqlDatabaseName, inputImagesDirectory, workingDirectory);
		System.out.println("Writing scripts...");
		writeMakeDirectoriesScript(workingDirectory, outputDirectory);
		writeWheelNavCoreExecutionScript(outputDirectory);
		System.out.println("Writing output locaitons file...");
		writeOutputLocationsFile(workingDirectory, outputDirectory);
		System.out.println("DONE");
	}
}

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
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


// TODO: Auto-generated Javadoc
/**
 * The Class GenerateScriptForTurkersPHPHelper.
 */
public class GenerateScriptForTurkersPHPHelper {
	
	/** The Constant INTRINSIC_PARAMETERS_FILE_NAME. */
	private static final String INTRINSIC_PARAMETERS_FILE_NAME = "intrinsics.xml";
	
	/** The Constant DISTORTION_PARAMETERS_FILE_NAME. */
	private static final String DISTORTION_PARAMETERS_FILE_NAME = "distortion.xml";
	
	/** The Constant WHEELNAV_CORE_EXECUTABLE_NAME. */
	private static final String WHEELNAV_CORE_EXECUTABLE_NAME = "wheelnavcore";
	
	/** The Constant EXECUTE_WHEELNAV_CORE_SCRIPT_NAME. */
	private static final String EXECUTE_WHEELNAV_CORE_SCRIPT_NAME = "executewheelnavcore.sh";

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
	 * Instantiates a new generate script for turkers php helper.
	 */
	public GenerateScriptForTurkersPHPHelper() {
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
				if(strLine.trim().length()>0) {
					String[] temp = strLine.split("\t");
					if(temp!=null && temp.length==2) {
						CameraParameterMapping mapping = new CameraParameterMapping();
						mapping.setDeviceType(temp[0]);
						mapping.setCameraParametersDirectory(temp[1]);
						mappings.put(temp[0].trim().toUpperCase(), mapping);
						mappingCount++;
					}
				}
			}

		} catch(Exception ex) {
		}
	}

	/**
	 * Rearrange sidewalk tile points.
	 *
	 * @param stPoints the st points
	 * @return the point[]
	 */
	private Point[] rearrangeSidewalkTilePoints(Point[] stPoints) {
		int firstPointIndex=-1;
		int lastPointIndex=-1;
		int firstSmallest=-1;
		int secondSmallest=-1;
		for(int i=0;i<4;i++) {
			if(firstSmallest==-1) {
				firstSmallest=i;
			}
			else if(secondSmallest==-1) {
				if(stPoints[i].x<stPoints[firstSmallest].x) {
					secondSmallest=firstSmallest;
					firstSmallest=i;
				}
				else {
					secondSmallest=i;
				}
			}
			else {
				if(stPoints[i].x<stPoints[secondSmallest].x) {
					secondSmallest=i;
					if(stPoints[i].x<stPoints[firstSmallest].x) {
						secondSmallest=firstSmallest;
						firstSmallest=i;
					}
				}
			}
		}

		if(stPoints[firstSmallest].y>stPoints[secondSmallest].y) {
			firstPointIndex=secondSmallest;
			lastPointIndex=firstSmallest;
		}
		else {
			firstPointIndex=firstSmallest;
			lastPointIndex=secondSmallest;
		}

		Point[] newStPoints = new Point[4];
		newStPoints[0]=stPoints[firstPointIndex];
		newStPoints[3]=stPoints[lastPointIndex];

		if(firstPointIndex==0 && lastPointIndex==1) {
			newStPoints[1]=stPoints[3];
			newStPoints[2]=stPoints[2];
		}
		else if(firstPointIndex==1 && lastPointIndex==0) {
			newStPoints[1]=stPoints[2];
			newStPoints[2]=stPoints[3];
		}
		else if(firstPointIndex==1 && lastPointIndex==2) {
			newStPoints[1]=stPoints[0];
			newStPoints[2]=stPoints[3];
		}
		else if(firstPointIndex==2 && lastPointIndex==1) {
			newStPoints[1]=stPoints[3];
			newStPoints[2]=stPoints[0];
		}
		else if(firstPointIndex==2 && lastPointIndex==3) {
			newStPoints[1]=stPoints[1];
			newStPoints[2]=stPoints[0];
		}
		else if(firstPointIndex==3 && lastPointIndex==2) {
			newStPoints[1]=stPoints[0];
			newStPoints[2]=stPoints[1];
		}
		else if(firstPointIndex==0 && lastPointIndex==3) {
			newStPoints[1]=stPoints[1];
			newStPoints[2]=stPoints[2];
		}
		else if(firstPointIndex==3 && lastPointIndex==0) {
			newStPoints[1]=stPoints[2];
			newStPoints[2]=stPoints[1];
		}


		
		/*
		if(firstPointIndex>lastPointIndex) {
			int k=firstPointIndex;
			k++;
			if(k==4) {k=0;}
			newStPoints[1]=stPoints[k];
			k++;
			if(k==4) {k=0;}
			newStPoints[2]=stPoints[k];
		}
		else {
			int k=lastPointIndex;
			k++;
			if(k==4) {k=0;}
			newStPoints[2]=stPoints[k];
			k++;
			if(k==4) {k=0;}
			newStPoints[1]=stPoints[k];
		}
		*/		
		return newStPoints;
	}

	/**
	 * Retrieve database records.
	 *
	 * @param inputImagesDirectory the input images directory
	 * @param workingDirectory the working directory
	 * @param requestID the request id
	 * @param sidewalkTileLength the sidewalk tile length
	 * @param sidewalkTileWidth the sidewalk tile width
	 * @param cvHeight the cv height
	 * @param cvWidth the cv width
	 * @param iHeight the i height
	 * @param iWidth the i width
	 * @param stp1x the stp1x
	 * @param stp1y the stp1y
	 * @param stp2x the stp2x
	 * @param stp2y the stp2y
	 * @param stp3x the stp3x
	 * @param stp3y the stp3y
	 * @param stp4x the stp4x
	 * @param stp4y the stp4y
	 * @param deviceType the device type
	 * @param pitchString the pitch string
	 * @param rollString the roll string
	 * @param bbleft the bbleft
	 * @param bbright the bbright
	 * @param bbtop the bbtop
	 * @param bbbottom the bbbottom
	 * @param answer_unit the answer_unit
	 */
	private void retrieveDatabaseRecords(String inputImagesDirectory, String workingDirectory, 
			String requestID, String sidewalkTileLength, String sidewalkTileWidth, String cvHeight, String cvWidth, String iHeight, String iWidth, String stp1x, String stp1y, String stp2x, String stp2y, String stp3x, String stp3y, String stp4x, String stp4y, String deviceType, String pitchString, String rollString, String bbleft, String bbright, String bbtop, String bbbottom, String answer_unit) {

		int recordsCount=0;

		List<String> recordsWithNoDeviceType = new ArrayList<String>();
		List<String> recordWithNoCameraParameters = new ArrayList<String>();
		List<String> recordsWithNoMeasurements = new ArrayList<String>();

		try {

			boolean isValid = true;
			WheelNavCoreInputRecord record = new WheelNavCoreInputRecord();
			record.setRequestId(requestID);
			record.setHitId("NA");
			record.setAssignmentId("NA");
			record.setWorkerId("NA");
			record.setInputImageFilePath(inputImagesDirectory + "/" + "photo" + requestID + ".jpg");
			record.setOutputDirectory(workingDirectory);
			record.setOutputImageName("photo" + requestID);

			if(deviceType!=null && deviceType.trim().length()>0) {
				if(mappings.containsKey(deviceType.trim().toUpperCase())) {
					String dir = mappings.get(deviceType.trim().toUpperCase()).getCameraParametersDirectory();
					record.setIntrinsicParameterFile(dir + "/" + INTRINSIC_PARAMETERS_FILE_NAME);
					record.setDistortionParameterFile(dir + "/" + DISTORTION_PARAMETERS_FILE_NAME);
				}
				else {
					recordWithNoCameraParameters.add(record.getRequestId());	
					isValid=false;
				}
			}
			else {
				recordsWithNoDeviceType.add(record.getRequestId());
				isValid=false;
			}

			int image_height = (int)Float.parseFloat(iHeight);
			int image_width = (int)Float.parseFloat(iWidth);
			double pitch = Math.abs(Float.parseFloat(pitchString));
			double roll = Math.abs(Float.parseFloat(rollString));

			if(image_height>image_width) {
				record.setTheta(90-roll);
			}
			else if(image_width>image_height) {
				record.setTheta(90-pitch);
			}

			record.setMeasuredHeight(0);
			record.setMeasuredLengthOfTile(0);
			record.setMeasuredWidthOfTile(0);
			record.setMeasuredDistanceToTile(0);
			record.setMeasuredDistanceToIssue(0);

			double st_length = Double.parseDouble(sidewalkTileLength);
			double st_width = Double.parseDouble(sidewalkTileLength);

			if (answer_unit.trim().toUpperCase().equals("FOOT")) {
				record.setSidewalkTileLength(st_length*309.4);
				record.setSidewalkTileWidth(st_width*309.4);
			}
			else if (answer_unit.trim().toUpperCase().equals("INCH")) {
				record.setSidewalkTileLength(st_length*25.4);
				record.setSidewalkTileWidth(st_width*25.4);
			}
			else if (answer_unit.trim().toUpperCase().equals("CENTIMETER")) {
				record.setSidewalkTileLength(st_length*10);
				record.setSidewalkTileWidth(st_width*10);
			}

			/*record.setSidewalkTileLength(Float.parseFloat(sidewalkTileLength));
			record.setSidewalkTileLength(Float.parseFloat(sidewalkTileWidth));
			*/

			int bBoxLeft, bBoxTop, bBoxRight, bBoxBottom;
			bBoxLeft = (int)Float.parseFloat(bbleft);
			bBoxTop = (int)Float.parseFloat(bbtop);
			bBoxRight = (int)Float.parseFloat(bbright);
			bBoxBottom = (int)Float.parseFloat(bbbottom);

			record.setBbPoint1_x(bBoxLeft);
			record.setBbPoint1_y(bBoxTop);

			record.setBbPoint2_x(bBoxRight);
			record.setBbPoint2_y(bBoxTop);

			record.setBbPoint3_x(bBoxRight);
			record.setBbPoint3_y(bBoxBottom);

			record.setBbPoint4_x(bBoxLeft);
			record.setBbPoint4_y(bBoxBottom);

			float canvasHeight = Float.parseFloat(cvHeight);
			float canvasWidth = Float.parseFloat(cvWidth);
			float imageHeight = Float.parseFloat(iHeight);
			float imageWidth = Float.parseFloat(iWidth);

			float answer_point1_x = Float.parseFloat(stp1x);
			float answer_point1_y = Float.parseFloat(stp1y);
			float answer_point2_x = Float.parseFloat(stp2x);
			float answer_point2_y = Float.parseFloat(stp2y);
			float answer_point3_x = Float.parseFloat(stp3x);
			float answer_point3_y = Float.parseFloat(stp3y);
			float answer_point4_x = Float.parseFloat(stp4x);
			float answer_point4_y = Float.parseFloat(stp4y);

			Point[] stPoints = new Point[4];

			stPoints[0]=new Point();
			stPoints[0].x = (int)(answer_point1_x * imageWidth / canvasWidth);
			stPoints[0].y = (int)(answer_point1_y * imageHeight / canvasHeight);
			stPoints[1]=new Point();
			stPoints[1].x = (int)(answer_point2_x * imageWidth / canvasWidth);
			stPoints[1].y = (int)(answer_point2_y * imageHeight / canvasHeight);
			stPoints[2]=new Point();
			stPoints[2].x = (int)(answer_point3_x * imageWidth / canvasWidth);
			stPoints[2].y = (int)(answer_point3_y * imageHeight / canvasHeight);
			stPoints[3]=new Point();
			stPoints[3].x = (int)(answer_point4_x * imageWidth / canvasWidth);
			stPoints[3].y = (int)(answer_point4_y * imageHeight / canvasHeight);

			Point[] newSTPoints = rearrangeSidewalkTilePoints(stPoints);

			record.setStPoint1_x(newSTPoints[0].x);
			record.setStPoint1_y(newSTPoints[0].y);

			record.setStPoint2_x(newSTPoints[1].x);
			record.setStPoint2_y(newSTPoints[1].y);

			record.setStPoint3_x(newSTPoints[2].x);
			record.setStPoint3_y(newSTPoints[2].y);

			record.setStPoint4_x(newSTPoints[3].x);
			record.setStPoint4_y(newSTPoints[3].y);

			if(isValid) {
				this.records.add(record);
			}
			recordsCount++;
			//}

		} catch (Exception ex) {
			Logger lgr = Logger.getLogger(GenerateScriptForTurkersPHPHelper.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);

		} finally {
		}

		StringBuilder sbNoDeviceType = new StringBuilder();
		for(String s : recordsWithNoDeviceType) {
			sbNoDeviceType.append("\t" + s);
		}
		StringBuilder sbNoCamParams = new StringBuilder();
		for(String s : recordWithNoCameraParameters) {
			sbNoCamParams.append("\t" + s);
		}
		StringBuilder sbNoMeasurements = new StringBuilder();
		for(String s : recordsWithNoMeasurements) {
			sbNoMeasurements.append("\t" + s);
		}
	}

	/**
	 * Write wheel nav core execution script.
	 */
	private void writeWheelNavCoreExecutionScript() {
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<records.size();i++) {
			sb.append("\n\n./" + "wheelnavcore/" + WHEELNAV_CORE_EXECUTABLE_NAME + " ");
			sb.append(records.get(i).getRecordString("CORE") + " 2>&1");
		}
		System.out.println(sb.toString());
	}


	/**
	 * Creates the wheel nav core test inputgenerate turk input.
	 *
	 * @param cameraParametersMappingFile the camera parameters mapping file
	 * @param inputImagesDirectory the input images directory
	 * @param workingDirectory the working directory
	 * @param requestID the request id
	 * @param sidewalkTileLength the sidewalk tile length
	 * @param sidewalkTileWidth the sidewalk tile width
	 * @param cvHeight the cv height
	 * @param cvWidth the cv width
	 * @param iHeight the i height
	 * @param iWidth the i width
	 * @param stp1x the stp1x
	 * @param stp1y the stp1y
	 * @param stp2x the stp2x
	 * @param stp2y the stp2y
	 * @param stp3x the stp3x
	 * @param stp3y the stp3y
	 * @param stp4x the stp4x
	 * @param stp4y the stp4y
	 * @param deviceType the device type
	 * @param pitchString the pitch string
	 * @param rollString the roll string
	 * @param bbleft the bbleft
	 * @param bbright the bbright
	 * @param bbtop the bbtop
	 * @param bbbottom the bbbottom
	 * @param answer_unit the answer_unit
	 */
	public void createWheelNavCoreTestInputgenerateTurkInput(String cameraParametersMappingFile, String inputImagesDirectory, String workingDirectory, String requestID, String sidewalkTileLength, String sidewalkTileWidth, String cvHeight, String cvWidth, String iHeight, String iWidth, String stp1x, String stp1y, String stp2x, String stp2y, String stp3x, String stp3y, String stp4x, String stp4y, String deviceType, String pitchString, String rollString, String bbleft, String bbright, String bbtop, String bbbottom, String answer_unit) {
		readCameraParameterMappings(cameraParametersMappingFile);
		retrieveDatabaseRecords(inputImagesDirectory, workingDirectory, requestID, sidewalkTileLength, sidewalkTileWidth, cvHeight, cvWidth, iHeight, iWidth, stp1x, stp1y, stp2x, stp2y, stp3x, stp3y, stp4x, stp4y,  deviceType, pitchString, rollString, bbleft, bbright, bbtop, bbbottom, answer_unit);
		writeWheelNavCoreExecutionScript();
	}
}

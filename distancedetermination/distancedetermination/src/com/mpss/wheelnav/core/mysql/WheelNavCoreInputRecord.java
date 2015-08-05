/*
 * Distance determintaion
 * Author : Amol Deshpande, Mahbubur rahman
 * {date}
 * @author B. P. Hennessey
 * @version 1.0
 */
package com.mpss.wheelnav.core.mysql;

// TODO: Auto-generated Javadoc
/**
 * The Class WheelNavCoreInputRecord.
 */
public class WheelNavCoreInputRecord {
	
	/** The request id. */
	private String requestId;
	
	/** The hit id. */
	private String hitId;
	
	/** The assignment id. */
	private String assignmentId;
	
	/** The worker id. */
	private String workerId;
	
	/** The input image file path. */
	private String inputImageFilePath;
	
	/** The output directory. */
	private String outputDirectory;
	
	/** The output image name. */
	private String outputImageName;
	
	/** The intrinsic parameter file. */
	private String intrinsicParameterFile;
	
	/** The distortion parameter file. */
	private String distortionParameterFile;
	
	/** The theta. */
	private double theta;
	
	/** The measured height. */
	private double measuredHeight;
	
	/** The measured length of tile. */
	private double measuredLengthOfTile;
	
	/** The measured width of tile. */
	private double measuredWidthOfTile;
	
	/** The measured distance to tile. */
	private double measuredDistanceToTile;
	
	/** The measured distance to issue. */
	private double measuredDistanceToIssue;
	
	/** The sidewalk tile length. */
	private double sidewalkTileLength;
	
	/** The sidewalk tile width. */
	private double sidewalkTileWidth;
	
	/** The bb point1_x. */
	private double bbPoint1_x;
	
	/** The bb point1_y. */
	private double bbPoint1_y;
	
	/** The bb point2_x. */
	private double bbPoint2_x;
	
	/** The bb point2_y. */
	private double bbPoint2_y;
	
	/** The bb point3_x. */
	private double bbPoint3_x;
	
	/** The bb point3_y. */
	private double bbPoint3_y;
	
	/** The bb point4_x. */
	private double bbPoint4_x;
	
	/** The bb point4_y. */
	private double bbPoint4_y;
	
	/** The st point1_x. */
	private double stPoint1_x;
	
	/** The st point1_y. */
	private double stPoint1_y;
	
	/** The st point2_x. */
	private double stPoint2_x;
	
	/** The st point2_y. */
	private double stPoint2_y;
	
	/** The st point3_x. */
	private double stPoint3_x;
	
	/** The st point3_y. */
	private double stPoint3_y;
	
	/** The st point4_x. */
	private double stPoint4_x;
	
	/** The st point4_y. */
	private double stPoint4_y;

	/**
	 * Gets the request id.
	 *
	 * @return the request id
	 */
	public String getRequestId() {
		return requestId;
	}
	
	/**
	 * Sets the request id.
	 *
	 * @param requestId the new request id
	 */
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	
	/**
	 * Gets the hit id.
	 *
	 * @return the hit id
	 */
	public String getHitId() {
		return hitId;
	}
	
	/**
	 * Sets the hit id.
	 *
	 * @param hitId the new hit id
	 */
	public void setHitId(String hitId) {
		this.hitId = hitId;
	}
	
	/**
	 * Gets the assignment id.
	 *
	 * @return the assignment id
	 */
	public String getAssignmentId() {
		return assignmentId;
	}
	
	/**
	 * Sets the assignment id.
	 *
	 * @param assignmentId the new assignment id
	 */
	public void setAssignmentId(String assignmentId) {
		this.assignmentId = assignmentId;
	}
	
	/**
	 * Gets the worker id.
	 *
	 * @return the worker id
	 */
	public String getWorkerId() {
		return workerId;
	}
	
	/**
	 * Sets the worker id.
	 *
	 * @param workerId the new worker id
	 */
	public void setWorkerId(String workerId) {
		this.workerId = workerId;
	}
	
	/**
	 * Gets the input image file path.
	 *
	 * @return the input image file path
	 */
	public String getInputImageFilePath() {
		return inputImageFilePath;
	}
	
	/**
	 * Sets the input image file path.
	 *
	 * @param inputImageFilePath the new input image file path
	 */
	public void setInputImageFilePath(String inputImageFilePath) {
		this.inputImageFilePath = inputImageFilePath;
	}
	
	/**
	 * Gets the output directory.
	 *
	 * @return the output directory
	 */
	public String getOutputDirectory() {
		return outputDirectory;
	}
	
	/**
	 * Sets the output directory.
	 *
	 * @param outputDirectory the new output directory
	 */
	public void setOutputDirectory(String outputDirectory) {
		this.outputDirectory = outputDirectory;
	}
	
	/**
	 * Gets the output image name.
	 *
	 * @return the output image name
	 */
	public String getOutputImageName() {
		return outputImageName;
	}
	
	/**
	 * Sets the output image name.
	 *
	 * @param outputImageName the new output image name
	 */
	public void setOutputImageName(String outputImageName) {
		this.outputImageName = outputImageName;
	}
	
	/**
	 * Gets the intrinsic parameter file.
	 *
	 * @return the intrinsic parameter file
	 */
	public String getIntrinsicParameterFile() {
		return intrinsicParameterFile;
	}
	
	/**
	 * Sets the intrinsic parameter file.
	 *
	 * @param intrinsicParameterFile the new intrinsic parameter file
	 */
	public void setIntrinsicParameterFile(String intrinsicParameterFile) {
		this.intrinsicParameterFile = intrinsicParameterFile;
	}
	
	/**
	 * Gets the distortion parameter file.
	 *
	 * @return the distortion parameter file
	 */
	public String getDistortionParameterFile() {
		return distortionParameterFile;
	}
	
	/**
	 * Sets the distortion parameter file.
	 *
	 * @param distortionParameterFile the new distortion parameter file
	 */
	public void setDistortionParameterFile(String distortionParameterFile) {
		this.distortionParameterFile = distortionParameterFile;
	}
	
	/**
	 * Gets the theta.
	 *
	 * @return the theta
	 */
	public double getTheta() {
		return theta;
	}
	
	/**
	 * Sets the theta.
	 *
	 * @param theta the new theta
	 */
	public void setTheta(double theta) {
		this.theta = theta;
	}
	
	/**
	 * Gets the measured height.
	 *
	 * @return the measured height
	 */
	public double getMeasuredHeight() {
		return measuredHeight;
	}
	
	/**
	 * Sets the measured height.
	 *
	 * @param measuredHeight the new measured height
	 */
	public void setMeasuredHeight(double measuredHeight) {
		this.measuredHeight = measuredHeight;
	}
	
	/**
	 * Gets the measured length of tile.
	 *
	 * @return the measured length of tile
	 */
	public double getMeasuredLengthOfTile() {
		return measuredLengthOfTile;
	}
	
	/**
	 * Sets the measured length of tile.
	 *
	 * @param measuredLengthOfTile the new measured length of tile
	 */
	public void setMeasuredLengthOfTile(double measuredLengthOfTile) {
		this.measuredLengthOfTile = measuredLengthOfTile;
	}
	
	/**
	 * Gets the measured width of tile.
	 *
	 * @return the measured width of tile
	 */
	public double getMeasuredWidthOfTile() {
		return measuredWidthOfTile;
	}
	
	/**
	 * Sets the measured width of tile.
	 *
	 * @param measuredWidthOfTile the new measured width of tile
	 */
	public void setMeasuredWidthOfTile(double measuredWidthOfTile) {
		this.measuredWidthOfTile = measuredWidthOfTile;
	}
	
	/**
	 * Gets the measured distance to tile.
	 *
	 * @return the measured distance to tile
	 */
	public double getMeasuredDistanceToTile() {
		return measuredDistanceToTile;
	}
	
	/**
	 * Sets the measured distance to tile.
	 *
	 * @param measuredDistanceToTile the new measured distance to tile
	 */
	public void setMeasuredDistanceToTile(double measuredDistanceToTile) {
		this.measuredDistanceToTile = measuredDistanceToTile;
	}
	
	/**
	 * Gets the measured distance to issue.
	 *
	 * @return the measured distance to issue
	 */
	public double getMeasuredDistanceToIssue() {
		return measuredDistanceToIssue;
	}
	
	/**
	 * Sets the measured distance to issue.
	 *
	 * @param measuredDistanceToIssue the new measured distance to issue
	 */
	public void setMeasuredDistanceToIssue(double measuredDistanceToIssue) {
		this.measuredDistanceToIssue = measuredDistanceToIssue;
	}
	
	/**
	 * Gets the sidewalk tile length.
	 *
	 * @return the sidewalk tile length
	 */
	public double getSidewalkTileLength() {
		return sidewalkTileLength;
	}
	
	/**
	 * Sets the sidewalk tile length.
	 *
	 * @param sidewalkTileLength the new sidewalk tile length
	 */
	public void setSidewalkTileLength(double sidewalkTileLength) {
		this.sidewalkTileLength = sidewalkTileLength;
	}
	
	/**
	 * Gets the sidewalk tile width.
	 *
	 * @return the sidewalk tile width
	 */
	public double getSidewalkTileWidth() {
		return sidewalkTileWidth;
	}
	
	/**
	 * Sets the sidewalk tile width.
	 *
	 * @param sidewalkTileWidth the new sidewalk tile width
	 */
	public void setSidewalkTileWidth(double sidewalkTileWidth) {
		this.sidewalkTileWidth = sidewalkTileWidth;
	}
	
	/**
	 * Gets the bb point1_x.
	 *
	 * @return the bb point1_x
	 */
	public double getBbPoint1_x() {
		return bbPoint1_x;
	}
	
	/**
	 * Sets the bb point1_x.
	 *
	 * @param bbPoint1_x the new bb point1_x
	 */
	public void setBbPoint1_x(double bbPoint1_x) {
		this.bbPoint1_x = bbPoint1_x;
	}
	
	/**
	 * Gets the bb point1_y.
	 *
	 * @return the bb point1_y
	 */
	public double getBbPoint1_y() {
		return bbPoint1_y;
	}
	
	/**
	 * Sets the bb point1_y.
	 *
	 * @param bbPoint1_y the new bb point1_y
	 */
	public void setBbPoint1_y(double bbPoint1_y) {
		this.bbPoint1_y = bbPoint1_y;
	}
	
	/**
	 * Gets the bb point2_x.
	 *
	 * @return the bb point2_x
	 */
	public double getBbPoint2_x() {
		return bbPoint2_x;
	}
	
	/**
	 * Sets the bb point2_x.
	 *
	 * @param bbPoint2_x the new bb point2_x
	 */
	public void setBbPoint2_x(double bbPoint2_x) {
		this.bbPoint2_x = bbPoint2_x;
	}
	
	/**
	 * Gets the bb point2_y.
	 *
	 * @return the bb point2_y
	 */
	public double getBbPoint2_y() {
		return bbPoint2_y;
	}
	
	/**
	 * Sets the bb point2_y.
	 *
	 * @param bbPoint2_y the new bb point2_y
	 */
	public void setBbPoint2_y(double bbPoint2_y) {
		this.bbPoint2_y = bbPoint2_y;
	}
	
	/**
	 * Gets the bb point3_x.
	 *
	 * @return the bb point3_x
	 */
	public double getBbPoint3_x() {
		return bbPoint3_x;
	}
	
	/**
	 * Sets the bb point3_x.
	 *
	 * @param bbPoint3_x the new bb point3_x
	 */
	public void setBbPoint3_x(double bbPoint3_x) {
		this.bbPoint3_x = bbPoint3_x;
	}
	
	/**
	 * Gets the bb point3_y.
	 *
	 * @return the bb point3_y
	 */
	public double getBbPoint3_y() {
		return bbPoint3_y;
	}
	
	/**
	 * Sets the bb point3_y.
	 *
	 * @param bbPoint3_y the new bb point3_y
	 */
	public void setBbPoint3_y(double bbPoint3_y) {
		this.bbPoint3_y = bbPoint3_y;
	}
	
	/**
	 * Gets the bb point4_x.
	 *
	 * @return the bb point4_x
	 */
	public double getBbPoint4_x() {
		return bbPoint4_x;
	}
	
	/**
	 * Sets the bb point4_x.
	 *
	 * @param bbPoint4_x the new bb point4_x
	 */
	public void setBbPoint4_x(double bbPoint4_x) {
		this.bbPoint4_x = bbPoint4_x;
	}
	
	/**
	 * Gets the bb point4_y.
	 *
	 * @return the bb point4_y
	 */
	public double getBbPoint4_y() {
		return bbPoint4_y;
	}
	
	/**
	 * Sets the bb point4_y.
	 *
	 * @param bbPoint4_y the new bb point4_y
	 */
	public void setBbPoint4_y(double bbPoint4_y) {
		this.bbPoint4_y = bbPoint4_y;
	}
	
	/**
	 * Gets the st point1_x.
	 *
	 * @return the st point1_x
	 */
	public double getStPoint1_x() {
		return stPoint1_x;
	}
	
	/**
	 * Sets the st point1_x.
	 *
	 * @param stPoint1_x the new st point1_x
	 */
	public void setStPoint1_x(double stPoint1_x) {
		this.stPoint1_x = stPoint1_x;
	}
	
	/**
	 * Gets the st point1_y.
	 *
	 * @return the st point1_y
	 */
	public double getStPoint1_y() {
		return stPoint1_y;
	}
	
	/**
	 * Sets the st point1_y.
	 *
	 * @param stPoint1_y the new st point1_y
	 */
	public void setStPoint1_y(double stPoint1_y) {
		this.stPoint1_y = stPoint1_y;
	}
	
	/**
	 * Gets the st point2_x.
	 *
	 * @return the st point2_x
	 */
	public double getStPoint2_x() {
		return stPoint2_x;
	}
	
	/**
	 * Sets the st point2_x.
	 *
	 * @param stPoint2_x the new st point2_x
	 */
	public void setStPoint2_x(double stPoint2_x) {
		this.stPoint2_x = stPoint2_x;
	}
	
	/**
	 * Gets the st point2_y.
	 *
	 * @return the st point2_y
	 */
	public double getStPoint2_y() {
		return stPoint2_y;
	}
	
	/**
	 * Sets the st point2_y.
	 *
	 * @param stPoint2_y the new st point2_y
	 */
	public void setStPoint2_y(double stPoint2_y) {
		this.stPoint2_y = stPoint2_y;
	}
	
	/**
	 * Gets the st point3_x.
	 *
	 * @return the st point3_x
	 */
	public double getStPoint3_x() {
		return stPoint3_x;
	}
	
	/**
	 * Sets the st point3_x.
	 *
	 * @param stPoint3_x the new st point3_x
	 */
	public void setStPoint3_x(double stPoint3_x) {
		this.stPoint3_x = stPoint3_x;
	}
	
	/**
	 * Gets the st point3_y.
	 *
	 * @return the st point3_y
	 */
	public double getStPoint3_y() {
		return stPoint3_y;
	}
	
	/**
	 * Sets the st point3_y.
	 *
	 * @param stPoint3_y the new st point3_y
	 */
	public void setStPoint3_y(double stPoint3_y) {
		this.stPoint3_y = stPoint3_y;
	}
	
	/**
	 * Gets the st point4_x.
	 *
	 * @return the st point4_x
	 */
	public double getStPoint4_x() {
		return stPoint4_x;
	}
	
	/**
	 * Sets the st point4_x.
	 *
	 * @param stPoint4_x the new st point4_x
	 */
	public void setStPoint4_x(double stPoint4_x) {
		this.stPoint4_x = stPoint4_x;
	}
	
	/**
	 * Gets the st point4_y.
	 *
	 * @return the st point4_y
	 */
	public double getStPoint4_y() {
		return stPoint4_y;
	}
	
	/**
	 * Sets the st point4_y.
	 *
	 * @param stPoint4_y the new st point4_y
	 */
	public void setStPoint4_y(double stPoint4_y) {
		this.stPoint4_y = stPoint4_y;
	}

	/**
	 * Gets the record string.
	 *
	 * @param mode the mode
	 * @return the record string
	 */
	public String getRecordString(String mode) {
		StringBuilder sb = new StringBuilder();
		sb.append("\"" + requestId + "\"");
		sb.append(" \"" + hitId + "\"");
		sb.append(" \"" + assignmentId + "\"");
		sb.append(" \"" + workerId + "\"");
		sb.append(" \"" + inputImageFilePath + "\"");
		sb.append(" \"" + outputDirectory + "\"");
		sb.append(" \"" + outputImageName + "\"");
		sb.append(" \"" + intrinsicParameterFile + "\"");
		sb.append(" \"" + distortionParameterFile + "\"");
		sb.append(" \"" + theta + "\"");
		sb.append(" \"" + measuredHeight + "\"");
		sb.append(" \"" + measuredLengthOfTile + "\"");
		sb.append(" \"" + measuredWidthOfTile + "\"");
		sb.append(" \"" + measuredDistanceToTile + "\"");
		sb.append(" \"" + measuredDistanceToIssue + "\"");
		sb.append(" \"" + sidewalkTileLength + "\"");
		sb.append(" \"" + sidewalkTileWidth + "\"");
		sb.append(" \"" + bbPoint1_x + "\"");
		sb.append(" \"" + bbPoint1_y + "\"");
		sb.append(" \"" + bbPoint2_x + "\"");
		sb.append(" \"" + bbPoint2_y + "\"");
		sb.append(" \"" + bbPoint3_x + "\"");
		sb.append(" \"" + bbPoint3_y + "\"");
		sb.append(" \"" + bbPoint4_x + "\"");
		sb.append(" \"" + bbPoint4_y + "\"");
		if(mode=="CORE") {
			sb.append(" \"" + stPoint1_x + "\"");
			sb.append(" \"" + stPoint1_y + "\"");
			sb.append(" \"" + stPoint2_x + "\"");
			sb.append(" \"" + stPoint2_y + "\"");
			sb.append(" \"" + stPoint3_x + "\"");
			sb.append(" \"" + stPoint3_y + "\"");
			sb.append(" \"" + stPoint4_x + "\"");
			sb.append(" \"" + stPoint4_y + "\"");
		}
		return sb.toString();
	}
}

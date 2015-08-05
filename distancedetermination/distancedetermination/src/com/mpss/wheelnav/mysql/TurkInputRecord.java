package com.mpss.wheelnav.mysql;

public class TurkInputRecord {

	private int requestId;
	private double imageWidth;
	private double imageHeight;
	private String imageName;
	private double canvasWidth;
	private double canvasHeight;
	
	private String deviceType;
	private String pitchString;
	private String rollString;
	private String bbleft;
	private String bbright; 
	private String bbtop; 
	private String bbbottom;
	
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getPitchString() {
		return pitchString;
	}
	public void setPitchString(String pitchString) {
		this.pitchString = pitchString;
	}
	public String getRollString() {
		return rollString;
	}
	public void setRollString(String rollString) {
		this.rollString = rollString;
	}
	public String getBbleft() {
		return bbleft;
	}
	public void setBbleft(String bbleft) {
		this.bbleft = bbleft;
	}
	public String getBbright() {
		return bbright;
	}
	public void setBbright(String bbright) {
		this.bbright = bbright;
	}
	public String getBbtop() {
		return bbtop;
	}
	public void setBbtop(String bbtop) {
		this.bbtop = bbtop;
	}
	public String getBbbottom() {
		return bbbottom;
	}
	public void setBbbottom(String bbbottom) {
		this.bbbottom = bbbottom;
	}
	public int getRequestId() {
		return requestId;
	}
	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}
	public double getImageWidth() {
		return imageWidth;
	}
	public void setImageWidth(double imageWidth) {
		this.imageWidth = imageWidth;
	}
	public double getImageHeight() {
		return imageHeight;
	}
	public void setImageHeight(double imageHeight) {
		this.imageHeight = imageHeight;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public double getCanvasWidth() {
		return canvasWidth;
	}
	public void setCanvasWidth(double canvasWidth) {
		this.canvasWidth = canvasWidth;
	}
	public double getCanvasHeight() {
		return canvasHeight;
	}
	public void setCanvasHeight(double canvasHeight) {
		this.canvasHeight = canvasHeight;
	}
}

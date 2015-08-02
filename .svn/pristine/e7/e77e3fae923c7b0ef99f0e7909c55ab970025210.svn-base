package com.mpss.wheelnav.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.os.Parcel;
import android.os.Parcelable;

public class AccessibilityIssueRequest implements Parcelable {

	public String getNearFocusDistance() {
		return nearFocusDistance;
	}

	public void setNearFocusDistance(String nearFocusDistance) {
		this.nearFocusDistance = nearFocusDistance;
	}

	public String getOptimalFocusDistance() {
		return optimalFocusDistance;
	}

	public void setOptimalFocusDistance(String optimalFocusDistance) {
		this.optimalFocusDistance = optimalFocusDistance;
	}

	public String getFarFocusDistance() {
		return farFocusDistance;
	}

	public void setFarFocusDistance(String farFocusDistance) {
		this.farFocusDistance = farFocusDistance;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		
		if(imageData!=null) {
			dest.writeInt(imageDataSize);
			dest.writeByteArray(imageData);
		}
		else {
			dest.writeInt(0);
		}
		
		dest.writeInt(isImageAnnotated);
		
		dest.writeString(imageFilePath);
		dest.writeString(annotatedImageFilePath);
		
		dest.writeInt(boundingBoxLeft);
		dest.writeInt(boundingBoxTop);
		dest.writeInt(boundingBoxRight);
		dest.writeInt(boundingBoxBottom);

		dest.writeString(voiceCommentFilePath);
		dest.writeString(textComment);

		dest.writeString(locationLatitude);
		dest.writeString(locationLongitude);
		dest.writeString(compassAzimuthValue);

		dest.writeString(nearFocusDistance);
		dest.writeString(optimalFocusDistance);
		dest.writeString(farFocusDistance);
		
		dest.writeString(aperature_EXIF);
		dest.writeString(datetime_EXIF);
		dest.writeString(exposureTime_EXIF);
		dest.writeString(flash_EXIF);
		dest.writeString(focalLength_EXIF);

		dest.writeString(gpsLatitude_EXIF);
		dest.writeString(gpsLatitudeRef_EXIF);
		dest.writeString(gpsLongitude_EXIF);
		dest.writeString(gpsLongitudeRef_EXIF);
		dest.writeString(gpsTimestamp_EXIF);

		dest.writeString(iso_EXIF);
		dest.writeString(make_EXIF);
		dest.writeString(model_EXIF);
		dest.writeString(orientation_EXIF);
		dest.writeString(whiteBalance_EXIF);    

		dest.writeString(issueType);

		dest.writeFloat(distanceToSubject); 
		dest.writeString(distanceUnit); 
		dest.writeString(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ", Locale.US).format(dateCaptured));
		
		dest.writeString(chronometerReading);
	}

	private void readFromParcel(Parcel in) {   

		imageDataSize = in.readInt();
		if(imageDataSize>0) {
			imageData = new byte[imageDataSize];
			in.readByteArray(imageData);
		}
		
		isImageAnnotated = in.readInt();
		
		imageFilePath = in.readString();
		annotatedImageFilePath = in.readString();
		
		
		boundingBoxLeft = in.readInt();
		boundingBoxTop = in.readInt();
		boundingBoxRight = in.readInt();
		boundingBoxBottom= in.readInt();

		voiceCommentFilePath=in.readString();
		textComment=in.readString();

		locationLatitude=in.readString();
		locationLongitude=in.readString();
		compassAzimuthValue=in.readString();
		
		nearFocusDistance = in.readString();
		optimalFocusDistance = in.readString();
		farFocusDistance = in.readString();

		aperature_EXIF=in.readString();
		datetime_EXIF=in.readString();
		exposureTime_EXIF=in.readString();
		flash_EXIF=in.readString();
		focalLength_EXIF=in.readString();

		gpsLatitude_EXIF=in.readString();
		gpsLatitudeRef_EXIF=in.readString();
		gpsLongitude_EXIF=in.readString();
		gpsLongitudeRef_EXIF=in.readString();
		gpsTimestamp_EXIF=in.readString();

		iso_EXIF=in.readString();
		make_EXIF=in.readString();
		model_EXIF=in.readString();
		orientation_EXIF=in.readString();
		whiteBalance_EXIF=in.readString();    

		issueType=in.readString();

		distanceToSubject=in.readFloat(); 
		distanceUnit=in.readString(); 
		try {
			dateCaptured = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ", Locale.US).parse(in.readString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		chronometerReading= in.readString();
	}	

	public AccessibilityIssueRequest(Parcel in) { 
		readFromParcel(in); 
	}   

	public AccessibilityIssueRequest() { 
	}   

	public static final Parcelable.Creator<AccessibilityIssueRequest> CREATOR = new Parcelable.Creator<AccessibilityIssueRequest>() { 

		public AccessibilityIssueRequest createFromParcel(Parcel in) { 
			return new AccessibilityIssueRequest(in); 
		}   

		public AccessibilityIssueRequest[] newArray(int size) { 
			return new AccessibilityIssueRequest[size]; 
		} 
	};  

	private int imageDataSize;
	private byte[] imageData;
	
	private int isImageAnnotated;

	private String imageFilePath; 
	private String annotatedImageFilePath; 
	
	private int boundingBoxLeft;
	private int boundingBoxTop;
	private int boundingBoxRight;
	private int boundingBoxBottom;

	private String voiceCommentFilePath;
	private String textComment;

	private String locationLatitude;
	private String locationLongitude;
	private String compassAzimuthValue;
	
	private String nearFocusDistance;
	private String optimalFocusDistance;
	private String farFocusDistance;

	private String aperature_EXIF;
	private String datetime_EXIF;
	private String exposureTime_EXIF;
	private String flash_EXIF;
	private String focalLength_EXIF;

	private String gpsLatitude_EXIF;
	private String gpsLatitudeRef_EXIF;
	private String gpsLongitude_EXIF;
	private String gpsLongitudeRef_EXIF;
	private String gpsTimestamp_EXIF;

	private String iso_EXIF;
	private String make_EXIF;
	private String model_EXIF;
	private String orientation_EXIF;
	private String whiteBalance_EXIF;    

	private String issueType;

	private float distanceToSubject; 
	private String distanceUnit; 
	private Date dateCaptured;
	
	public Date getDateCaptured() {
		return dateCaptured;
	}

	public void setDateCaptured(Date dateCaptured) {
		this.dateCaptured = dateCaptured;
	}

	private String chronometerReading; 
	
		
	public String getChronometerReading() {
		return chronometerReading;
	}

	public void setChronometerReading(String chronometerReading) {
		this.chronometerReading = chronometerReading;
	}
	
	public String getImageFilePath() {
		return imageFilePath;
	}

	public void setImageFilePath(String imageFilePath) {
		this.imageFilePath = imageFilePath;
	}

	public String getAnnotatedImageFilePath() {
		return annotatedImageFilePath;
	}

	public void setAnnotatedImageFilePath(String annotatedImageFilePath) {
		this.annotatedImageFilePath = annotatedImageFilePath;
	}

	public int getIsImageAnnotated() {
		return isImageAnnotated;
	}

	public void setIsImageAnnotated(int isImageAnnotated) {
		this.isImageAnnotated = isImageAnnotated;
	}

	public int getImageDataSize() {
		return imageDataSize;
	}

	public void setImageDataSize(int imageDataSize) {
		this.imageDataSize = imageDataSize;
	}
	
	public String getLocationLatitude() {
		return locationLatitude;
	}

	public void setLocationLatitude(String locationLatitude) {
		this.locationLatitude = locationLatitude;
	}

	public String getLocationLongitude() {
		return locationLongitude;
	}

	public void setLocationLongitude(String locationLongitude) {
		this.locationLongitude = locationLongitude;
	}



	public byte[] getImageData() {
		return imageData;
	}

	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}

	public int getBoundingBoxLeft() {
		return boundingBoxLeft;
	}

	public void setBoundingBoxLeft(int boundingBoxLeft) {
		this.boundingBoxLeft = boundingBoxLeft;
	}

	public int getBoundingBoxTop() {
		return boundingBoxTop;
	}

	public void setBoundingBoxTop(int boundingBoxTop) {
		this.boundingBoxTop = boundingBoxTop;
	}

	public int getBoundingBoxRight() {
		return boundingBoxRight;
	}

	public void setBoundingBoxRight(int boundingBoxRight) {
		this.boundingBoxRight = boundingBoxRight;
	}

	public int getBoundingBoxBottom() {
		return boundingBoxBottom;
	}

	public void setBoundingBoxBottom(int boundingBoxBottom) {
		this.boundingBoxBottom = boundingBoxBottom;
	}

	public float getDistanceToSubject() {
		return distanceToSubject;
	}

	public void setDistanceToSubject(float distanceToSubject) {
		this.distanceToSubject = distanceToSubject;
	}

	public String getDistanceUnit() {
		return distanceUnit;
	}

	public void setDistanceUnit(String distanceUnit) {
		this.distanceUnit = distanceUnit;
	}


	public String getIssueType() {
		return issueType;
	}

	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}

	public String getAperature_EXIF() {
		return aperature_EXIF;
	}

	public void setAperature_EXIF(String aperature_EXIF) {
		this.aperature_EXIF = aperature_EXIF;
	}

	public String getDatetime_EXIF() {
		return datetime_EXIF;
	}

	public void setDatetime_EXIF(String datetime_EXIF) {
		this.datetime_EXIF = datetime_EXIF;
	}

	public String getExposureTime_EXIF() {
		return exposureTime_EXIF;
	}

	public void setExposureTime_EXIF(String exposureTime_EXIF) {
		this.exposureTime_EXIF = exposureTime_EXIF;
	}

	public String getFlash_EXIF() {
		return flash_EXIF;
	}

	public void setFlash_EXIF(String flash_EXIF) {
		this.flash_EXIF = flash_EXIF;
	}

	public String getFocalLength_EXIF() {
		return focalLength_EXIF;
	}

	public void setFocalLength_EXIF(String focalLength_EXIF) {
		this.focalLength_EXIF = focalLength_EXIF;
	}

	public String getGpsLatitude_EXIF() {
		return gpsLatitude_EXIF;
	}

	public void setGpsLatitude_EXIF(String gpsLatitude_EXIF) {
		this.gpsLatitude_EXIF = gpsLatitude_EXIF;
	}

	public String getGpsLatitudeRef_EXIF() {
		return gpsLatitudeRef_EXIF;
	}

	public void setGpsLatitudeRef_EXIF(String gpsLatitudeRef_EXIF) {
		this.gpsLatitudeRef_EXIF = gpsLatitudeRef_EXIF;
	}

	public String getGpsLongitude_EXIF() {
		return gpsLongitude_EXIF;
	}

	public void setGpsLongitude_EXIF(String gpsLongitude_EXIF) {
		this.gpsLongitude_EXIF = gpsLongitude_EXIF;
	}

	public String getGpsLongitudeRef_EXIF() {
		return gpsLongitudeRef_EXIF;
	}

	public void setGpsLongitudeRef_EXIF(String gpsLongitudeRef_EXIF) {
		this.gpsLongitudeRef_EXIF = gpsLongitudeRef_EXIF;
	}

	public String getGpsTimestamp_EXIF() {
		return gpsTimestamp_EXIF;
	}

	public void setGpsTimestamp_EXIF(String gpsTimestamp_EXIF) {
		this.gpsTimestamp_EXIF = gpsTimestamp_EXIF;
	}

	public String getIso_EXIF() {
		return iso_EXIF;
	}

	public void setIso_EXIF(String iso_EXIF) {
		this.iso_EXIF = iso_EXIF;
	}

	public String getMake_EXIF() {
		return make_EXIF;
	}

	public void setMake_EXIF(String make_EXIF) {
		this.make_EXIF = make_EXIF;
	}

	public String getModel_EXIF() {
		return model_EXIF;
	}

	public void setModel_EXIF(String model_EXIF) {
		this.model_EXIF = model_EXIF;
	}

	public String getOrientation_EXIF() {
		return orientation_EXIF;
	}

	public void setOrientation_EXIF(String orientation_EXIF) {
		this.orientation_EXIF = orientation_EXIF;
	}

	public String getWhiteBalance_EXIF() {
		return whiteBalance_EXIF;
	}

	public void setWhiteBalance_EXIF(String whiteBalance_EXIF) {
		this.whiteBalance_EXIF = whiteBalance_EXIF;
	}

	public String getVoiceCommentFilePath() {
		return voiceCommentFilePath;
	}

	public void setVoiceCommentFilePath(String voiceCommentFilePath) {
		this.voiceCommentFilePath = voiceCommentFilePath;
	}

	public String getTextComment() {
		return textComment;
	}

	public void setTextComment(String textComment) {
		this.textComment = textComment;
	}

	public String getCompassAzimuthValue() {
		return compassAzimuthValue;
	}

	public void setCompassAzimuthValue(String compassAzimuthValue) {
		this.compassAzimuthValue = compassAzimuthValue;
	}
}

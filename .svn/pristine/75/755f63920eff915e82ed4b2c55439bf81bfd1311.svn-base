package com.mpss.wheelnav.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {


	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "issueRequestsManager";

	// Contacts table name
	private static final String TABLE_ISSUE_REQUESTS = "issueRequests";

	// Contacts Table Columns names
	public static final String KEY_ID = "id";
	public static final String KEY_ANNOTATED_IMAGE_FILE_PATH = "annotatedImageFilePath";
	public static final String KEY_BOUNDING_BOX_LEFT = "boundingBoxLeft";
	public static final String KEY_BOUNDING_BOX_TOP = "boundingBoxTop";
	public static final String KEY_BOUNDING_BOX_RIGHT = "boundingBoxRight";
	public static final String KEY_BOUNDING_BOX_BOTTOM = "boundingBoxBottom";
	public static final String KEY_VOICE_COMMENT_FILE_PATH = "voiceCommentFilePath";
	public static final String KEY_TEXT_COMMENT = "textComment";
	public static final String KEY_LOCATION_LATITUDE  = "locationLatitude";
	public static final String KEY_LOCATION_LONGITUDE = "locationLongitude";
	public static final String KEY_COMPASS_AZIMUTH_VALUE = "compassAzimuthValue";
	public static final String KEY_ISSUE_TYPE = "issueType";
	public static final String KEY_DISTANCE_TO_SUBJECT = "distanceToSubject";
	public static final String KEY_DISTANCE_UNIT = "distanceUnit";
	public static final String KEY_APERATURE_EXIF = "aperature_EXIF";
	public static final String KEY_DATETIME_EXIF = "datetime_EXIF";
	public static final String KEY_EXPOSURETIME_EXIF = "exposureTime_EXIF";
	public static final String KEY_FLASH_EXIF = "flash_EXIF";
	public static final String KEY_FOCAL_LENGTH_EXIF = "focalLength_EXIF";
	public static final String KEY_GPS_LATITUDE_EXIF = "gpsLatitude_EXIF";
	public static final String KEY_GPS_LATITUDE_REF_EXIF = "gpsLatitudeRef_EXIF";
	public static final String KEY_GPS_LONGITUDE_EXIF = "gpsLongitude_EXIF";
	public static final String KEY_GPS_LONGITUDE_REF_EXIF = "gpsLongitudeRef_EXIF";
	public static final String KEY_GPS_TIMESTAMP_EXIF = "gpsTimestamp_EXIF";
	public static final String KEY_ISO_EXIF = "iso_EXIF";
	public static final String KEY_MAKE_EXIF = "make_EXIF";
	public static final String KEY_MODEL_EXIF = "model_EXIF";
	public static final String KEY_ORIENTATION_EXIF = "orientation_EXIF";
	public static final String KEY_WHITE_BALANCE_EXIF = "whiteBalance_EXIF";
	public static final String KEY_UPLOADED_TO_SERVER = "uploadedToServer";
	public static final String KEY_DATE_CAPTURED = "dateCaptured";
	public static final String KEY_NEAR_FOCUS_DISTANCE = "nearFocusDistance";
	public static final String KEY_OPTIMAL_FOCUS_DISTANCE = "optimalFocusDistance";
	public static final String KEY_FAR_FOCUS_DISTANCE = "farFocusDistance";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/*
	public DatabaseHandler(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}*/

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_ISSUE_REQUESTS_TABLE = "CREATE TABLE " + TABLE_ISSUE_REQUESTS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," 
				+ KEY_ANNOTATED_IMAGE_FILE_PATH + " TEXT," 
				+ KEY_BOUNDING_BOX_LEFT + " TEXT," 
				+ KEY_BOUNDING_BOX_TOP + " TEXT," 
				+ KEY_BOUNDING_BOX_RIGHT + " TEXT," 
				+ KEY_BOUNDING_BOX_BOTTOM + " TEXT," 
				+ KEY_VOICE_COMMENT_FILE_PATH + " TEXT," 
				+ KEY_TEXT_COMMENT + " TEXT," 
				+ KEY_LOCATION_LATITUDE  + " TEXT," 
				+ KEY_LOCATION_LONGITUDE+ " TEXT," 
				+ KEY_COMPASS_AZIMUTH_VALUE + " TEXT," 
				+ KEY_ISSUE_TYPE + " TEXT," 
				+ KEY_DISTANCE_TO_SUBJECT + " TEXT," 
				+ KEY_DISTANCE_UNIT + " TEXT," 
				+ KEY_APERATURE_EXIF + " TEXT," 
				+ KEY_DATETIME_EXIF + " TEXT," 
				+ KEY_EXPOSURETIME_EXIF + " TEXT," 
				+ KEY_FLASH_EXIF + " TEXT," 
				+ KEY_FOCAL_LENGTH_EXIF + " TEXT,"  
				+ KEY_GPS_LATITUDE_EXIF + " TEXT," 
				+ KEY_GPS_LATITUDE_REF_EXIF + " TEXT," 
				+ KEY_GPS_LONGITUDE_EXIF + " TEXT," 
				+ KEY_GPS_LONGITUDE_REF_EXIF + " TEXT," 
				+ KEY_GPS_TIMESTAMP_EXIF + " TEXT," 
				+ KEY_ISO_EXIF + " TEXT," 
				+ KEY_MAKE_EXIF + " TEXT," 
				+ KEY_MODEL_EXIF + " TEXT," 
				+ KEY_ORIENTATION_EXIF + " TEXT," 
				+ KEY_WHITE_BALANCE_EXIF + " TEXT,"
				+ KEY_UPLOADED_TO_SERVER + " TEXT,"
				+ KEY_DATE_CAPTURED + " TEXT,"
				+ KEY_NEAR_FOCUS_DISTANCE + " TEXT,"
				+ KEY_OPTIMAL_FOCUS_DISTANCE + " TEXT,"
				+ KEY_FAR_FOCUS_DISTANCE + " TEXT"
				+ ")";
		db.execSQL(CREATE_ISSUE_REQUESTS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ISSUE_REQUESTS);
		onCreate(db);
	}


	// Adding new issue request
	public long addRequest(IssueRequest request) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ANNOTATED_IMAGE_FILE_PATH, request.get_annotatedImageFilePath());
		values.put(KEY_BOUNDING_BOX_LEFT, Integer.toString(request.get_boundingBoxBottom()));
		values.put(KEY_BOUNDING_BOX_TOP, Integer.toString(request.get_boundingBoxTop()));
		values.put(KEY_BOUNDING_BOX_RIGHT, Integer.toString(request.get_boundingBoxRight()));
		values.put(KEY_BOUNDING_BOX_BOTTOM, Integer.toString(request.get_boundingBoxBottom()));
		values.put(KEY_VOICE_COMMENT_FILE_PATH, request.get_voiceCommentFilePath());
		values.put(KEY_TEXT_COMMENT, request.get_textComment());
		values.put(KEY_LOCATION_LATITUDE, request.get_locationLatitude());
		values.put(KEY_LOCATION_LONGITUDE, request.get_locationLongitude());
		values.put(KEY_COMPASS_AZIMUTH_VALUE, request.get_compassAzimuthValue());
		values.put(KEY_ISSUE_TYPE, request.get_issueType());
		values.put(KEY_DISTANCE_TO_SUBJECT, Float.toString(request.get_distanceToSubject()));
		values.put(KEY_DISTANCE_UNIT, request.get_distanceUnit());
		values.put(KEY_APERATURE_EXIF, request.get_aperature_EXIF());
		values.put(KEY_DATETIME_EXIF, request.get_datetime_EXIF());
		values.put(KEY_EXPOSURETIME_EXIF, request.get_exposureTime_EXIF());
		values.put(KEY_FLASH_EXIF, request.get_flash_EXIF());
		values.put(KEY_FOCAL_LENGTH_EXIF, request.get_focalLength_EXIF());
		values.put(KEY_GPS_LATITUDE_EXIF, request.get_gpsLatitude_EXIF());
		values.put(KEY_GPS_LATITUDE_REF_EXIF, request.get_gpsLatitudeRef_EXIF());
		values.put(KEY_GPS_LONGITUDE_EXIF, request.get_gpsLongitude_EXIF());
		values.put(KEY_GPS_LONGITUDE_REF_EXIF, request.get_gpsLongitudeRef_EXIF());
		values.put(KEY_GPS_TIMESTAMP_EXIF, request.get_gpsTimestamp_EXIF());
		values.put(KEY_ISO_EXIF, request.get_iso_EXIF());
		values.put(KEY_MAKE_EXIF, request.get_make_EXIF());
		values.put(KEY_MODEL_EXIF, request.get_model_EXIF());
		values.put(KEY_ORIENTATION_EXIF, request.get_orientation_EXIF());
		values.put(KEY_WHITE_BALANCE_EXIF, request.get_whiteBalance_EXIF());
		values.put(KEY_UPLOADED_TO_SERVER, Boolean.toString(request.is_UploadedToServer()));
		values.put(KEY_DATE_CAPTURED, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ",Locale.US).format(request.get_DateCaptured()));
		values.put(KEY_NEAR_FOCUS_DISTANCE, request.get_nearFocusDistance());
		values.put(KEY_OPTIMAL_FOCUS_DISTANCE, request.get_optimalFocusDistance());
		values.put(KEY_FAR_FOCUS_DISTANCE, request.get_farFocusDistance());

		// Inserting Row
		long id = db.insert(TABLE_ISSUE_REQUESTS, null, values);
		db.close(); // Closing database connection
		return id;
	}


	// Getting single request
	public IssueRequest getRequest(int id) {

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_ISSUE_REQUESTS, new String[] { KEY_ID,
				KEY_ANNOTATED_IMAGE_FILE_PATH,  
				KEY_BOUNDING_BOX_LEFT,  
				KEY_BOUNDING_BOX_TOP,  
				KEY_BOUNDING_BOX_RIGHT,  
				KEY_BOUNDING_BOX_BOTTOM,  
				KEY_VOICE_COMMENT_FILE_PATH,  
				KEY_TEXT_COMMENT,  
				KEY_LOCATION_LATITUDE,   
				KEY_LOCATION_LONGITUDE, 
				KEY_COMPASS_AZIMUTH_VALUE,  
				KEY_ISSUE_TYPE,  
				KEY_DISTANCE_TO_SUBJECT,  
				KEY_DISTANCE_UNIT,  
				KEY_APERATURE_EXIF,  
				KEY_DATETIME_EXIF,  
				KEY_EXPOSURETIME_EXIF,  
				KEY_FLASH_EXIF,  
				KEY_FOCAL_LENGTH_EXIF,   
				KEY_GPS_LATITUDE_EXIF,  
				KEY_GPS_LATITUDE_REF_EXIF,  
				KEY_GPS_LONGITUDE_EXIF,  
				KEY_GPS_LONGITUDE_REF_EXIF,  
				KEY_GPS_TIMESTAMP_EXIF,  
				KEY_ISO_EXIF,  
				KEY_MAKE_EXIF,  
				KEY_MODEL_EXIF,  
				KEY_ORIENTATION_EXIF,  
				KEY_WHITE_BALANCE_EXIF,
				KEY_UPLOADED_TO_SERVER,
				KEY_DATE_CAPTURED,
				KEY_NEAR_FOCUS_DISTANCE,
				KEY_OPTIMAL_FOCUS_DISTANCE,
				KEY_FAR_FOCUS_DISTANCE }, 
				KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		IssueRequest request = new IssueRequest();
		request.set_id(Integer.parseInt(cursor.getString(0)));

		request.set_annotatedImageFilePath(cursor.getString(1)); 

		request.set_boundingBoxLeft(Integer.parseInt(cursor.getString(2)));
		request.set_boundingBoxTop(Integer.parseInt(cursor.getString(3)));
		request.set_boundingBoxRight(Integer.parseInt(cursor.getString(4)));
		request.set_boundingBoxBottom(Integer.parseInt(cursor.getString(5)));

		request.set_voiceCommentFilePath(cursor.getString(6));
		request.set_textComment(cursor.getString(7));

		request.set_locationLatitude(cursor.getString(8));
		request.set_locationLongitude(cursor.getString(9));
		request.set_compassAzimuthValue(cursor.getString(10));

		request.set_issueType(cursor.getString(11));

		request.set_distanceToSubject(Float.parseFloat(cursor.getString(12))); 
		request.set_distanceUnit(cursor.getString(13)); 

		request.set_aperature_EXIF(cursor.getString(14));
		request.set_datetime_EXIF(cursor.getString(15));
		request.set_exposureTime_EXIF(cursor.getString(16));
		request.set_flash_EXIF(cursor.getString(17));
		request.set_focalLength_EXIF(cursor.getString(18));

		request.set_gpsLatitude_EXIF(cursor.getString(19));
		request.set_gpsLatitudeRef_EXIF(cursor.getString(20));
		request.set_gpsLongitude_EXIF(cursor.getString(21));
		request.set_gpsLongitudeRef_EXIF(cursor.getString(22));
		request.set_gpsTimestamp_EXIF(cursor.getString(23));

		request.set_iso_EXIF(cursor.getString(24));
		request.set_make_EXIF(cursor.getString(25));
		request.set_model_EXIF(cursor.getString(26));
		request.set_orientation_EXIF(cursor.getString(27));
		request.set_whiteBalance_EXIF(cursor.getString(28));    

		request.set_UploadedToServer(Boolean.parseBoolean(cursor.getString(29)));
		try {
			request.set_DateCaptured(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ", Locale.US).parse(cursor.getString(30)));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		request.set_nearFocusDistance(cursor.getString(31));
		request.set_optimalFocusDistance(cursor.getString(32));
		request.set_farFocusDistance(cursor.getString(33));

		return request;
	}


	// Getting All requests
	public Cursor getAllRequestsCursor() {

		String selectQuery = "SELECT  " + KEY_ANNOTATED_IMAGE_FILE_PATH + ", " + KEY_ISSUE_TYPE  + " FROM " + TABLE_ISSUE_REQUESTS;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		return cursor;
	}

	// Getting All requests
	public List<IssueRequest> getAllRequests(String status) {
		List<IssueRequest> requestList = new ArrayList<IssueRequest>();
		// Select All Query
		String selectQuery="";
		if(status==null) {
			selectQuery = "SELECT  * FROM " + TABLE_ISSUE_REQUESTS;
		}
		else {
			selectQuery = "SELECT  * FROM " + TABLE_ISSUE_REQUESTS + " WHERE " + KEY_UPLOADED_TO_SERVER + " = 'false'" ;
		}

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				IssueRequest request = new IssueRequest();
				request.set_id(Integer.parseInt(cursor.getString(0)));

				request.set_annotatedImageFilePath(cursor.getString(1)); 

				request.set_boundingBoxLeft(Integer.parseInt(cursor.getString(2)));
				request.set_boundingBoxTop(Integer.parseInt(cursor.getString(3)));
				request.set_boundingBoxRight(Integer.parseInt(cursor.getString(4)));
				request.set_boundingBoxBottom(Integer.parseInt(cursor.getString(5)));

				request.set_voiceCommentFilePath(cursor.getString(6));
				request.set_textComment(cursor.getString(7));

				request.set_locationLatitude(cursor.getString(8));
				request.set_locationLongitude(cursor.getString(9));
				request.set_compassAzimuthValue(cursor.getString(10));

				request.set_issueType(cursor.getString(11));

				request.set_distanceToSubject(Float.parseFloat(cursor.getString(12))); 
				request.set_distanceUnit(cursor.getString(13)); 

				request.set_aperature_EXIF(cursor.getString(14));
				request.set_datetime_EXIF(cursor.getString(15));
				request.set_exposureTime_EXIF(cursor.getString(16));
				request.set_flash_EXIF(cursor.getString(17));
				request.set_focalLength_EXIF(cursor.getString(18));

				request.set_gpsLatitude_EXIF(cursor.getString(19));
				request.set_gpsLatitudeRef_EXIF(cursor.getString(20));
				request.set_gpsLongitude_EXIF(cursor.getString(21));
				request.set_gpsLongitudeRef_EXIF(cursor.getString(22));
				request.set_gpsTimestamp_EXIF(cursor.getString(23));

				request.set_iso_EXIF(cursor.getString(24));
				request.set_make_EXIF(cursor.getString(25));
				request.set_model_EXIF(cursor.getString(26));
				request.set_orientation_EXIF(cursor.getString(27));
				request.set_whiteBalance_EXIF(cursor.getString(28));  

				request.set_UploadedToServer(Boolean.parseBoolean(cursor.getString(29)));
				try {
					request.set_DateCaptured(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ", Locale.US).parse(cursor.getString(30)));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				request.set_nearFocusDistance(cursor.getString(31));
				request.set_optimalFocusDistance(cursor.getString(32));
				request.set_farFocusDistance(cursor.getString(33));

				// Adding request to list
				requestList.add(request);
			} while (cursor.moveToNext());
		}

		// return requests list
		return requestList;
	}

	// Getting requests Count
	public int getRequestsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_ISSUE_REQUESTS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();
		// return count
		return cursor.getCount();
	}

	// Updating single request
	public int updateRequest(IssueRequest request) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ANNOTATED_IMAGE_FILE_PATH, request.get_annotatedImageFilePath());
		values.put(KEY_BOUNDING_BOX_LEFT, Integer.toString(request.get_boundingBoxBottom()));
		values.put(KEY_BOUNDING_BOX_TOP, Integer.toString(request.get_boundingBoxTop()));
		values.put(KEY_BOUNDING_BOX_RIGHT, Integer.toString(request.get_boundingBoxRight()));
		values.put(KEY_BOUNDING_BOX_BOTTOM, Integer.toString(request.get_boundingBoxBottom()));
		values.put(KEY_VOICE_COMMENT_FILE_PATH, request.get_voiceCommentFilePath());
		values.put(KEY_TEXT_COMMENT, request.get_textComment());
		values.put(KEY_LOCATION_LATITUDE, request.get_locationLatitude());
		values.put(KEY_LOCATION_LONGITUDE, request.get_locationLongitude());
		values.put(KEY_COMPASS_AZIMUTH_VALUE, request.get_compassAzimuthValue());
		values.put(KEY_ISSUE_TYPE, request.get_issueType());
		values.put(KEY_DISTANCE_TO_SUBJECT, Float.toString(request.get_distanceToSubject()));
		values.put(KEY_DISTANCE_UNIT, request.get_distanceUnit());
		values.put(KEY_APERATURE_EXIF, request.get_aperature_EXIF());
		values.put(KEY_DATETIME_EXIF, request.get_datetime_EXIF());
		values.put(KEY_EXPOSURETIME_EXIF, request.get_exposureTime_EXIF());
		values.put(KEY_FLASH_EXIF, request.get_flash_EXIF());
		values.put(KEY_FOCAL_LENGTH_EXIF, request.get_focalLength_EXIF());
		values.put(KEY_GPS_LATITUDE_EXIF, request.get_gpsLatitude_EXIF());
		values.put(KEY_GPS_LATITUDE_REF_EXIF, request.get_gpsLatitudeRef_EXIF());
		values.put(KEY_GPS_LONGITUDE_EXIF, request.get_gpsLongitude_EXIF());
		values.put(KEY_GPS_LONGITUDE_REF_EXIF, request.get_gpsLongitudeRef_EXIF());
		values.put(KEY_GPS_TIMESTAMP_EXIF, request.get_gpsTimestamp_EXIF());
		values.put(KEY_ISO_EXIF, request.get_iso_EXIF());
		values.put(KEY_MAKE_EXIF, request.get_make_EXIF());
		values.put(KEY_MODEL_EXIF, request.get_model_EXIF());
		values.put(KEY_ORIENTATION_EXIF, request.get_orientation_EXIF());
		values.put(KEY_WHITE_BALANCE_EXIF, request.get_whiteBalance_EXIF());

		values.put(KEY_UPLOADED_TO_SERVER, Boolean.toString(request.is_UploadedToServer()));
		values.put(KEY_DATE_CAPTURED, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ", Locale.US).format(request.get_DateCaptured()));

		values.put(KEY_NEAR_FOCUS_DISTANCE, request.get_nearFocusDistance());
		values.put(KEY_OPTIMAL_FOCUS_DISTANCE, request.get_optimalFocusDistance());
		values.put(KEY_FAR_FOCUS_DISTANCE, request.get_farFocusDistance());


		// updating row
		return db.update(TABLE_ISSUE_REQUESTS, values, KEY_ID + " = ?",
				new String[] { String.valueOf(request.get_id()) });
	}


	// Deleting single request
	public void deleteRequest(IssueRequest request) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_ISSUE_REQUESTS, KEY_ID + " = ?",
				new String[] { String.valueOf(request.get_id()) });
		db.close();
	}

}
package com.mpss.wheelnav;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import com.mpss.wheelnav.R;
import com.mpss.wheelnav.model.AccessibilityIssueRequest;
import com.mpss.wheelnav.utility.GPSLocationService;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi") public class CustomCameraActivity extends Activity {

	public static final String ACCESSIBILITY_ISSUE_REQUEST = "ACCESSIBILITY_ISSUE_REQUEST";
	private static final int ORIENTATION_PORTRAIT_NORMAL =  1;
	private static final int ORIENTATION_PORTRAIT_INVERTED =  2;
	private static final int ORIENTATION_LANDSCAPE_NORMAL =  3;
	private static final int ORIENTATION_LANDSCAPE_INVERTED =  4;
	public static final int BOUNDING_BOX_ACTIVITY_REQUEST_CODE = 0;
	private static final String IMAGE_FOLDER = "CustomCameraImages";

	private GPSLocationService gpsService;
	private OrientationEventListener mOrientationEventListener;
	private int mOrientation =  -1;
	private Camera mCamera;
	private CameraPreview mPreview;
	public Compass compass;
	public TextView compass_text;
	static boolean trackFocusDistanceEnabled = false;;
	private float fd0;
	private float fd1;
	private float fd2;


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		getActionBar().setTitle("Step 1 of 4");
		getActionBar().setSubtitle("Capture photo");

		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		int actionBarTitleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
		if (actionBarTitleId > 0) {
			TextView title = (TextView) findViewById(actionBarTitleId);
			if (title != null) {
				title.setTextColor(Color.WHITE);
			}
		}

		int actionBarSubTitleId = Resources.getSystem().getIdentifier("action_bar_subtitle", "id", "android");
		if (actionBarSubTitleId > 0) {
			TextView title = (TextView) findViewById(actionBarSubTitleId);
			if (title != null) {
				title.setTextColor(Color.WHITE);
			}
		}

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_custom_camera);

		compass = new Compass(this);
		compass_text = (TextView) findViewById(R.id.compass_angle);
		compass.compass_text = compass_text;

		Button captureButton = (Button) findViewById(R.id.button_capture);
		captureButton.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Camera.Parameters parameters = mCamera.getParameters();
						float[] distances = new float[3];
						System.out.println("Focus Mode:  " + parameters.getFocusMode());
						parameters.getFocusDistances(distances);
						mCamera.takePicture(null, null, mPicture);
					}
				}
				);

		captureButton.setOnKeyListener( new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				Toast.makeText(CustomCameraActivity.this, "on key", Toast.LENGTH_SHORT).show();
				return false;
			}
		});

	}

	@Override
	protected void onPause() {
		super.onPause();
		releaseCamera();
		FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
		preview.removeView(mPreview);
		compass.stop();
		gpsService.stopGPSUse();
		mOrientationEventListener.disable();
		trackFocusDistanceEnabled = false;
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		releaseCamera();
		compass.stop();
		gpsService.stopGPSUse();
	}

	@Override
	protected void onResume() {
		super.onResume(); 

		mCamera = getCameraInstance();
		mPreview = new CameraPreview(this, mCamera);
		FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);

		Camera.Parameters parameters = mCamera.getParameters(); 
		Point screenSize = new Point();
		getWindowManager().getDefaultDisplay().getSize(screenSize);
		Camera.Size size = getBestPreviewSize(screenSize.x, screenSize.y, parameters);
		parameters.setPreviewSize(size.width, size.height);

		//parameters.setAntibanding(Camera.Parameters.ANTIBANDING_AUTO);
		//parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
		//parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);

		parameters.setJpegQuality(100);
		parameters.setJpegThumbnailQuality(100);
		parameters.setPictureFormat(ImageFormat.JPEG);


		List<int[]> availableFpsRanges = parameters.getSupportedPreviewFpsRange();
		if(availableFpsRanges!=null && availableFpsRanges.size()>0) {
			int[] range = availableFpsRanges.get(availableFpsRanges.size()-1);
			parameters.setPreviewFpsRange(range[0], range[1]);
		}

		if(parameters.isVideoStabilizationSupported()) {
			parameters.setVideoStabilization(true);
		}

		parameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);


		mCamera.setParameters(parameters);


		preview.addView(mPreview);
		compass.start();

		mOrientation = -1;
		if (mOrientationEventListener == null) {            
			mOrientationEventListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_FASTEST) {

				@Override
				public void onOrientationChanged(int orientation) {

					int lastOrientation = mOrientation;

					if (orientation >= 315 || orientation < 45) {
						mOrientation = ORIENTATION_PORTRAIT_NORMAL;
					}
					else if (orientation < 315 && orientation >= 225) {
						mOrientation = ORIENTATION_LANDSCAPE_NORMAL;
					}
					else if (orientation < 225 && orientation >= 135) {
						mOrientation = ORIENTATION_PORTRAIT_INVERTED;
					}
					else { // orientation <135 && orientation > 45
						mOrientation = ORIENTATION_LANDSCAPE_INVERTED;
					}   

					if (lastOrientation != mOrientation) {
						changeRotation(mOrientation, lastOrientation, orientation);
					}
				}
			};
		}
		if (mOrientationEventListener.canDetectOrientation()) {
			mOrientationEventListener.enable();
		}

		gpsService=new GPSLocationService(CustomCameraActivity.this);
		Location currentLocation=gpsService.getLocation();
		if(currentLocation==null){
			//TODO: check what else should be done here
			Toast.makeText(this, "No location provider exists. You can not add a reqeust right now", Toast.LENGTH_LONG).show();
			finish();
		}

		trackFocusDistanceEnabled = true;
		new FocusDistanceTask().execute((Void[])null);	
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		trackFocusDistanceEnabled =false;


		Intent intent = new Intent(CustomCameraActivity.this, MainActivity.class);
		startActivity(intent);
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		onPause();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	public static Camera getCameraInstance(){
		Camera c = null;
		try {
			c = Camera.open(0); // attempt to get a Camera instance
		}
		catch (Exception e){
			// Camera is not available (in use or does not exist)
		}
		return c; 
	}

	public class FocusDistanceTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... voids) {

			while(mCamera!=null && trackFocusDistanceEnabled == true) {

				try {
					Camera.Parameters parameters = mCamera.getParameters();

					float[] distances = new float[3];
					parameters.getFocusDistances(distances);
					fd0=distances[0];
					fd1=distances[1];
					fd2=distances[2];
				}
				catch(Exception ex) {
					System.out.println(ex.getMessage());
					trackFocusDistanceEnabled=false;
				}
			}
			return true; 
		}

		@Override
		protected void onPostExecute(Boolean result) {
			/*if (trackFocusDistanceEnabled) {
				focusDistanceTrackDone(result);	
			}*/
		}
	}


	private void focusDistanceTrackDone(Boolean result) {

		TextView tv0 = (TextView)findViewById(R.id.fd0);
		TextView tv1 = (TextView)findViewById(R.id.fd1);
		TextView tv2 = (TextView)findViewById(R.id.fd2);

		tv0.setVisibility(1);
		tv1.setVisibility(1);		
		tv2.setVisibility(1);		

		tv0.setText(Float.toString(fd0));
		tv1.setText(Float.toString(fd1));
		tv2.setText(Float.toString(fd2));

		if(result && trackFocusDistanceEnabled) {
			new FocusDistanceTask().execute((Void[])null);	
		}	
	}

	private Camera.Size getBestPreviewSize(int width, int height, Camera.Parameters parameters) {

		Camera.Size result=null;

		for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
			if (size.width <= width && size.height <= height) {
				if (result == null) {
					result=size;
				}
				else {
					int resultArea=result.width * result.height;
					int newArea=size.width * size.height;

					if (newArea > resultArea) {
						result=size;
					}
				}
			}
		}

		return(result);
	}

	private Camera.Size getBestPictureSize(int width, int height, Camera.Parameters parameters) {

		Camera.Size result=null;

		for (Camera.Size size : parameters.getSupportedPictureSizes()) {
			if (size.width <= width && size.height <= height) {
				if (result == null) {
					result=size;
				}
				else {
					int resultArea=result.width * result.height;
					int newArea=size.width * size.height;

					if (newArea > resultArea) {
						result=size;
					}
				}
			}
		}

		return(result);
	}



	private PictureCallback mPicture = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {

			//TODO: remove this toast and hide the text-view showing azimuth
			//Toast.makeText(CustomCameraActivity.this, "Azimuth: " + compass_text.getText(), Toast.LENGTH_LONG).show();

			Location currentLocation = gpsService.getLocation();
			if(currentLocation==null) {
				//check what all other things must be done here
				finish();
			}

			if(compass_text.getText().toString().trim()== "" || !isNumeric(compass_text.getText().toString().trim())) {
				// what should happen if azimuth is null/NoN
			}


			if(Float.isInfinite(fd0))
				fd0=-1.0f;
			if(Float.isInfinite(fd1))
				fd1=-1.0f;
			if(Float.isInfinite(fd2))
				fd2=-1.0f;

			Intent intent = new Intent(CustomCameraActivity.this, BoundingBoxActivity.class);
			AccessibilityIssueRequest newRequest = new AccessibilityIssueRequest();
			newRequest.setImageFilePath(writeByteArrayToFile(data));
			newRequest.setIsImageAnnotated(0);
			newRequest.setCompassAzimuthValue(compass_text.getText().toString());
			newRequest.setLocationLatitude(Double.toString(currentLocation.getLatitude()));
			newRequest.setLocationLongitude(Double.toString(currentLocation.getLongitude()));
			newRequest.setDateCaptured(new Date());
			newRequest.setNearFocusDistance(Float.toString(fd0));
			newRequest.setOptimalFocusDistance(Float.toString(fd1));
			newRequest.setFarFocusDistance(Float.toString(fd2));
			intent.putExtra(ACCESSIBILITY_ISSUE_REQUEST, newRequest);
			startActivity(intent);
		}
	};

	private void changeRotation(int mOrientation, int lastOrientation, int orientation) {

		Camera.Parameters parameters = mCamera.getParameters();
		//if (orientation == ORIENTATION_UNKNOWN) return;
		android.hardware.Camera.CameraInfo info =
				new android.hardware.Camera.CameraInfo();
		android.hardware.Camera.getCameraInfo(0, info);
		orientation = (orientation + 45) / 90 * 90;
		int pictureRotattion = 0;
		if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
			pictureRotattion = (info.orientation - orientation + 360) % 360;
		} else {  // back-facing camera
			pictureRotattion = (info.orientation + orientation) % 360;
		}
		parameters.setRotation(pictureRotattion);

		int rotation = getWindowManager().getDefaultDisplay()
				.getRotation();
		int degrees = 0;
		switch (rotation) {
		case Surface.ROTATION_0: degrees = 0; break;
		case Surface.ROTATION_90: degrees = 90; break;
		case Surface.ROTATION_180: degrees = 180; break;
		case Surface.ROTATION_270: degrees = 270; break;
		}

		int result;
		if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360;  // compensate the mirror
		} else {  // back-facing
			result = (info.orientation - degrees + 360) % 360;
		}
		mCamera.setDisplayOrientation(result);

		if(rotation==0 || rotation==180) {
			Size s = getBestPictureSize(580,640, parameters);
			if(s!=null) {
				parameters.setPictureSize(s.width, s.height);
			}
		}
		else if(rotation==90 || rotation==270) {
			Size s = getBestPictureSize(640, 580, parameters);
			if(s!=null) {
				parameters.setPictureSize(s.width, s.height);
			}
		}
		mCamera.setParameters(parameters);
	}

	private void releaseCamera(){
		if (mCamera != null){
			mCamera.release();    
			mPreview.getHolder().removeCallback(mPreview);// release the camera for other applications
			mCamera = null;
		}
	}

	public static boolean isNumeric(String str)  
	{  
		try  
		{  
			double d = Double.parseDouble(str);  
		}  
		catch(NumberFormatException nfe)  
		{  
			return false;  
		}  
		return true;  
	}


	private String writeByteArrayToFile(byte[] array) {

		String rawFileName=null; 
		OutputStream fOut = null;

		try {

			rawFileName = getImageFileName("org_");
			File pictureFile=new File(rawFileName);
			pictureFile.createNewFile();
			fOut = new FileOutputStream(pictureFile);
			fOut.write(array);
			fOut.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//TODO: do this at the end after submitting the request
		/*sendBroadcast(new Intent(
				Intent.ACTION_MEDIA_MOUNTED,
				Uri.parse("file://" + Environment.getExternalStorageDirectory())));
		 */
		return rawFileName;
	}

	private String getImageFileName(String prefix){

		File file = new File(Environment.getExternalStorageDirectory(),IMAGE_FOLDER);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file.getAbsolutePath() + "/" + prefix + System.currentTimeMillis() + ".jpg";
	}	
}

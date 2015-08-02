package com.mpss.wheelnav;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.mpss.wheelnav.utility.AppLog;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

	private SurfaceHolder mHolder;
	private Camera mCamera;

	public CameraPreview(Context context, Camera camera) {
		super(context);
		mCamera = camera;
		mHolder = getHolder();
		mHolder.addCallback(this);
		try {
			// deprecated setting, but required on Android versions prior to 3.0
			mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		} 
		catch (Exception e) {
			AppLog.logString(e.getMessage());
		}
	}

	public void surfaceCreated(SurfaceHolder holder) {

		try {
			mCamera.setPreviewDisplay(holder);
			mCamera.startPreview();
		} catch (IOException e) {
			AppLog.logString("Error setting camera preview: " + e.getMessage());
		}
	}


	public void surfaceDestroyed(SurfaceHolder holder) {
		// empty. Take care of releasing the Camera preview in your activity.
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		// If your preview can change or rotate, take care of those events here.
		// Make sure to stop the preview before resizing or reformatting it.

		if (mHolder.getSurface() == null){
			// preview surface does not exist
			return;
		}

		// stop preview before making changes
		try {
			mCamera.stopPreview();
		} catch (Exception e){
			// ignore: tried to stop a non-existent preview
			AppLog.logString(e.getMessage());
		}

		// set preview size and make any resize, rotate or
		// reformatting changes here

		// start preview with new settings
		try {
			mCamera.setPreviewDisplay(mHolder);
			mCamera.startPreview();
			System.out.println("AutoFocus");
			Log.d("TAG", "AutoFocus log");
		} catch (Exception e){
			AppLog.logString("Error starting camera preview: " + e.getMessage());
		}
	}
}

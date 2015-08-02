package com.mpss.wheelnav;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.mpss.wheelnav.R;
import com.mpss.wheelnav.model.AccessibilityIssueRequest;
import com.mpss.wheelnav.utility.AudioRecorder;
import com.mpss.wheelnav.utility.BitmapCompressor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioFormat;
import android.media.ExifInterface;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

public class SubmitRequestActivity extends Activity {

	Context context = this;
	ProgressDialog progressDialogue;
	private static final String ACCESSIBILITY_ISSUE_REQUEST = "ACCESSIBILITY_ISSUE_REQUEST";
	private static final String SAVED_ACCESSIBILITY_ISSUE_REQUEST = "SAVED_ACCESSIBILITY_ISSUE_REQUEST";
	private static final String IMAGE_FOLDER = "CustomCameraImages";

	private AccessibilityIssueRequest request;

	ImageView imageView;  
	EditText txtComments;
	String strAudioPath="";
	public static final int BUFFER_SIZE = 1024 * 8;
	//private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".3gp";
	//private static final String AUDIO_RECORDER_FILE_EXT_MP4 = ".m4a";
	private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";

	private AudioRecorder audioRecorder=null;
	Chronometer mChronometer;

	//private int currentFormat = 0;
	//private String file_exts[] = { AUDIO_RECORDER_FILE_EXT_MP4, AUDIO_RECORDER_FILE_EXT_3GP }; 

	private static final int RECORDER_SAMPLERATE = 44100;
	private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_STEREO;
	private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
	public enum audioState {INITIALIZING, READY, RECORDING, ERROR, STOPPED};


	@Override
	public void onCreate(Bundle savedInstanceState) {

		getActionBar().setTitle("Step 4 of 4");
		getActionBar().setSubtitle("Review and Submit");

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
		setContentView(R.layout.activity_submit_request);

		if(this.getIntent().hasExtra(ACCESSIBILITY_ISSUE_REQUEST)) {
			this.request = this.getIntent().getParcelableExtra(ACCESSIBILITY_ISSUE_REQUEST);
		}
		else {
			// TODO: invalid case
		}

		progressDialogue = new ProgressDialog(context);
		imageView = (ImageView) findViewById(R.id.imageView1);
		txtComments=(EditText)findViewById(R.id.txtComments);

		setButtonHandlers();
		enableButtons(false);

		Button cancelButton = (Button) findViewById(R.id.button_cancel);
		cancelButton.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Builder builder = new AlertDialog.Builder(SubmitRequestActivity.this);
						builder.setMessage("Do you want to cancel this request?")
						.setCancelable(false)
						.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								voidTheRequest();
								finish();
								Intent intent = new Intent(SubmitRequestActivity.this, MainActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent);
							}
						})
						.setNegativeButton("No", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
						builder.create().show();
					}
				});

	}

	private void voidTheRequest() {

		if(request.getImageFilePath()!=null && request.getImageFilePath().trim()!="") {
			File image = new File(request.getImageFilePath());
			if(image!=null)
				image.delete();
		}
		if(request.getVoiceCommentFilePath()!=null && request.getVoiceCommentFilePath().trim()!="") {
			File voice = new File(request.getVoiceCommentFilePath());
			if(voice!=null)
				voice.delete();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadViewWithRequest();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		if(audioRecorder!=null){
			if(audioRecorder.getState().name()==audioState.RECORDING.name())
			{
				stopRecording();
			}
		}
		super.onBackPressed();
		saveViewStateToRequest();
		Intent intent = new Intent(SubmitRequestActivity.this, QuestionnaireActivity.class);
		intent.putExtra(ACCESSIBILITY_ISSUE_REQUEST, request);
		startActivity(intent);
	}

	@Override
	protected void onRestoreInstanceState (Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if(savedInstanceState!=null && savedInstanceState.containsKey("SAVED_ACCESSIBILITY_ISSUE_REQUEST")) {
			request = savedInstanceState.getParcelable(SAVED_ACCESSIBILITY_ISSUE_REQUEST);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		saveViewStateToRequest();
		savedInstanceState.putParcelable(SAVED_ACCESSIBILITY_ISSUE_REQUEST, request);
	}


	@Override  
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
		super.onActivityResult(requestCode, resultCode, data);  
	}

	private void loadViewWithRequest() {

		if(request.getImageFilePath()!=null) {
			//Bitmap bitmap = BitmapFactory.decodeFile(request.getAnnotatedImageFilePath());
			Bitmap bitmap = getAnnotatedBitmap();
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setImageBitmap(bitmap);
		}

		if(request.getTextComment()!=null && request.getTextComment().trim()!="") {
			txtComments.setText(request.getTextComment());
		}

		if(request.getChronometerReading()!=null && request.getChronometerReading().trim()!="") {
			mChronometer.setText(request.getChronometerReading());
		}
	}

	private void saveViewStateToRequest() {

		if(strAudioPath!=null && strAudioPath.trim()!="") {
			request.setVoiceCommentFilePath(strAudioPath);
			request.setChronometerReading(mChronometer.getText().toString());
		}
		request.setTextComment(txtComments.getText().toString());
	}

	private void submitRequest() {

		if(audioRecorder!=null){
			if(audioRecorder.getState().name()==audioState.RECORDING.name())
			{
				Toast.makeText(SubmitRequestActivity.this, "Please stop recording before submit", Toast.LENGTH_SHORT).show();
				return;
			}
		}

		if(request.getTextComment()!=null && request.getTextComment().trim().equals("") && request.getIssueType().trim().equals("Type5")) {
			Toast.makeText(SubmitRequestActivity.this, "Mention the type of Accessibility Issue", Toast.LENGTH_SHORT).show();
			return;
		}

		saveViewStateToRequest();
		saveExifInfoToRequest();
		saveAnnotatedBitmapToRequest();
		//deleteimage
		progressDialogue.setMessage("Sending Requst...");
		progressDialogue.setCancelable(false);
		progressDialogue.show();
		new UploadNewRequestTask().execute((Void[])null);
	}


	private void finishUploading(Boolean successful) {
		if(successful) {
			Toast.makeText(this, "SUCCESS", Toast.LENGTH_LONG).show();
		}
		else {
			Toast.makeText(this, "FAILURE", Toast.LENGTH_LONG).show();
		}
		finish();
		Intent intent = new Intent(SubmitRequestActivity.this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);

	}

	private class UploadNewRequestTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... voids) {

			HTTPRequestManager requestManager=new HTTPRequestManager(getString(R.string.newrequest_url), SubmitRequestActivity.this);
			return requestManager.uploadNewRequest(request);
			//return Boolean.TRUE;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			progressDialogue.dismiss();
			finishUploading(result);
		}
	}

	private void setButtonHandlers() {
		((ImageButton)findViewById(R.id.btnStart)).setOnClickListener(btnClick);
		((ImageButton)findViewById(R.id.btnStop)).setOnClickListener(btnClick);
		((Button) findViewById(R.id.send)).setOnClickListener(btnClick);
		mChronometer = (Chronometer) findViewById(R.id.chronometer);
	}

	private void enableButtons(boolean isRecording) {
		enableButton(R.id.btnStart,!isRecording);
		enableButton(R.id.btnStop,isRecording);
	}

	private void enableButton(int id,boolean isEnable){
		((ImageButton)findViewById(id)).setEnabled(isEnable);
	}

	private String getAudioFileName(){
		File file = new File(Environment.getExternalStorageDirectory().getPath(),AUDIO_RECORDER_FOLDER);
		if(!file.exists()){
			file.mkdirs();
		}
		return file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".wav";
	}

	private void startRecording(){

		((ImageButton)findViewById(R.id.btnStart)).setImageResource(R.drawable.btnrecord_dis_s);
		((ImageButton)findViewById(R.id.btnStop)).setImageResource(R.drawable.btnstop_en_s);

		audioRecorder=new AudioRecorder(false,MediaRecorder.AudioSource.MIC, RECORDER_SAMPLERATE,  RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING);

		if(strAudioPath!=null && strAudioPath.trim()!="") {
			File oldFile = new File(strAudioPath);
			oldFile.delete();
		}
		strAudioPath = getAudioFileName();
		audioRecorder.setOutputFile(strAudioPath);

		try {
			mChronometer.setBase(SystemClock.elapsedRealtime());
			mChronometer.start();
			audioRecorder.prepare();
			audioRecorder.start();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

	private void stopRecording(){
		((ImageButton)findViewById(R.id.btnStart)).setImageResource(R.drawable.btnrecord_en_s);
		((ImageButton)findViewById(R.id.btnStop)).setImageResource(R.drawable.btnstop_dis_s);
		if(null != audioRecorder){
			audioRecorder.stop();
			audioRecorder.reset();
			audioRecorder.release();
			mChronometer.stop();
			Toast.makeText(SubmitRequestActivity.this, mChronometer.getText() , Toast.LENGTH_LONG).show();
		}
	}

	private View.OnClickListener btnClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			switch(v.getId()){
			case R.id.btnStart:
				enableButtons(true);
				startRecording();                                                        
				break;

			case R.id.btnStop:
				enableButtons(false);
				stopRecording();                                        
				break;

			case R.id.send:
				submitRequest();
				break;
			}
		}
	};


	private void saveExifInfoToRequest() {

		ExifHelper exifHelper = new ExifHelper();
		try {
			exifHelper.createInFile(request.getImageFilePath());
			exifHelper.writeExifDataToRequest(request);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Bitmap getAnnotatedBitmap() {

		Bitmap bitmap;
		//bitmap = BitmapFactory.decodeFile(request.getImageFilePath());
		bitmap = BitmapCompressor.decodeSampledBitmapFromFile(request.getImageFilePath(), 480, 640);
		ExifInterface exif=null;
		try {
			exif = new ExifInterface(request.getImageFilePath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("6")){
			bitmap= BitmapCompressor.rotate(bitmap, 90);
		}else if(exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("8")){
			bitmap= BitmapCompressor.rotate(bitmap, 270);
		}else if(exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("3")){
			bitmap= BitmapCompressor.rotate(bitmap, 180);
		}
		android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
		if(bitmapConfig == null) {
			bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
		}
		bitmap = bitmap.copy(bitmapConfig, true);
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint(Paint.LINEAR_TEXT_FLAG);
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(5.0F);
		paint.setStyle(Paint.Style.STROKE);
		Rect boundingBox = new Rect(request.getBoundingBoxLeft(), request.getBoundingBoxTop(), request.getBoundingBoxRight(), request.getBoundingBoxBottom());
		canvas.drawRect(boundingBox, paint);
		return bitmap;
	}

	private void saveAnnotatedBitmapToRequest() {

		Bitmap bitmap = getAnnotatedBitmap();
		String filename = getImageFileName("bit_");
		File imageFile = new File(filename);

		try {
			imageFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(imageFile);
			final BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER_SIZE);
			bitmap.compress(CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {

		}
		//Toast.makeText(SubmitRequestActivity.this, "IMAGE SAVED", Toast.LENGTH_SHORT).show();
		request.setAnnotatedImageFilePath(filename);
	}

	private String getImageFileName(String prefix){

		File file = new File(Environment.getExternalStorageDirectory(),IMAGE_FOLDER);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file.getAbsolutePath() + "/" + prefix + System.currentTimeMillis() + ".jpg";
	}


	/*
	private void displayFormatDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String formats[] = {"MPEG 4", "3GPP"};

		builder.setTitle(getString(R.string.choose_format_title))
		.setSingleChoiceItems(formats, currentFormat, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				currentFormat = which;
				setFormatButtonCaption();                                        
				dialog.dismiss();
			}
		})
		.show();
	}

	private MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
		@Override
		public void onError(MediaRecorder mr, int what, int extra) {
			AppLog.logString("Error: " + what + ", " + extra);
		}
	};

	private MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
		@Override
		public void onInfo(MediaRecorder mr, int what, int extra) {
			AppLog.logString("Warning: " + what + ", " + extra);
		}
	};
	private void resetRecording(){
		((ImageButton)findViewById(R.id.btnStart)).setImageResource(R.drawable.btnrecord_en_s);
		((ImageButton)findViewById(R.id.btnStop)).setImageResource(R.drawable.btnstop_dis_s);
		//((ImageButton)findViewById(R.id.btnReset)).setImageResource(R.drawable.btn_reset_disable);
		mChronometer.setBase(SystemClock.elapsedRealtime());
		if(null!=arec){
			//recorder.reset();
			arec.reset();
			arec=null;
			//recorder=null;

		}
	}
	 */
}


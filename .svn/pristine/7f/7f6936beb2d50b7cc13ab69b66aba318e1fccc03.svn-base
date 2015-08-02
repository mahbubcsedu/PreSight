package com.mpss.wheelnav;

import java.io.File;
import java.io.IOException;

import com.mpss.wheelnav.R;
import com.mpss.wheelnav.model.AccessibilityIssueRequest;
import com.mpss.wheelnav.utility.BitmapCompressor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

public class BoundingBoxActivity extends Activity {

	private static final String ACCESSIBILITY_ISSUE_REQUEST = "ACCESSIBILITY_ISSUE_REQUEST";
	private static final String SAVED_ACCESSIBILITY_ISSUE_REQUEST = "SAVED_ACCESSIBILITY_ISSUE_REQUEST";
	public static final int BUFFER_SIZE = 1024 * 8;

	private CustomImageView imageView;
	private AccessibilityIssueRequest request;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		getActionBar().setTitle("Step 2 of 4");
		getActionBar().setSubtitle("Annotate Image : Highlight Accessibility Issue");

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
		setContentView(R.layout.activity_bounding_box);

		Intent intent = this.getIntent();
		if(intent.hasExtra(ACCESSIBILITY_ISSUE_REQUEST)) {
			request = intent.getParcelableExtra(ACCESSIBILITY_ISSUE_REQUEST);
		}
		else {
			// TODO: invalid case
		}

		Button nextButton = (Button) findViewById(R.id.button_next);
		nextButton.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						saveViewStateToRequest();
						Intent intent = new Intent(BoundingBoxActivity.this, QuestionnaireActivity.class);
						intent.putExtra(ACCESSIBILITY_ISSUE_REQUEST, request);
						startActivity(intent);
					}
				});

		Button cancelButton = (Button) findViewById(R.id.button_cancel);
		cancelButton.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Builder builder = new AlertDialog.Builder(BoundingBoxActivity.this);
						builder.setMessage("Do you want to cancel this request?")
						.setCancelable(false)
						.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								voidTheRequest();
								finish();
								Intent intent = new Intent(BoundingBoxActivity.this, MainActivity.class);
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
	
	private void loadImageViewFromRequest() {

		Bitmap bitmap=null;

		if(request.getImageFilePath()!=null) {
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
		}
		else {
			//TODO: invalid case 
		}

		this.imageView = new CustomImageView(getApplicationContext());
		imageView.setImageBitmap(bitmap);
		imageView.setScaleType(ScaleType.FIT_CENTER);

		if (!(request.getBoundingBoxLeft()== 0 
				&& request.getBoundingBoxRight()==0 
				&& request.getBoundingBoxTop()==0 
				&& request.getBoundingBoxBottom()==0)) {

			Rect bitmapBoundingBox = new Rect();
			bitmapBoundingBox.left = request.getBoundingBoxLeft();
			bitmapBoundingBox.right = request.getBoundingBoxRight();
			bitmapBoundingBox.top = request.getBoundingBoxTop();
			bitmapBoundingBox.bottom = request.getBoundingBoxBottom();
			this.imageView.setBitmapBoundingBox(bitmapBoundingBox);
		}

		FrameLayout preview = (FrameLayout) findViewById(R.id.image_preview);
		preview.removeAllViews();
		preview.addView(imageView);

		imageView.invalidate();
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
	protected void onResume() {
		super.onResume();
		loadImageViewFromRequest();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if(request.getImageFilePath()!=null && request.getImageFilePath().trim()!="") {
			File oldFile = new File(request.getImageFilePath());
			if (oldFile!=null)
				oldFile.delete();
		} 
		if(request.getVoiceCommentFilePath()!=null && request.getVoiceCommentFilePath().trim()!="") {
			File voice = new File(request.getVoiceCommentFilePath());
			if(voice!=null)
				voice.delete();
		}
		finish();
		Intent intent = new Intent(BoundingBoxActivity.this, CustomCameraActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	@Override
	protected void onRestoreInstanceState (Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if(savedInstanceState!=null) {
			request.setBoundingBoxLeft(savedInstanceState.getInt("left"));
			request.setBoundingBoxTop(savedInstanceState.getInt("top"));
			request.setBoundingBoxRight(savedInstanceState.getInt("right"));
			request.setBoundingBoxBottom(savedInstanceState.getInt("bottom"));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		Rect bitmapBoundingBox = this.imageView.getBitmapBoundingBox();
		savedInstanceState.putInt("left", bitmapBoundingBox.left);
		savedInstanceState.putInt("top", bitmapBoundingBox.top);
		savedInstanceState.putInt("right", bitmapBoundingBox.right);
		savedInstanceState.putInt("bottom", bitmapBoundingBox.bottom);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void saveViewStateToRequest() {

		Rect bitmapBoundingBox = this.imageView.getBitmapBoundingBox();
		request.setBoundingBoxLeft(bitmapBoundingBox.left);
		request.setBoundingBoxTop(bitmapBoundingBox.top);
		request.setBoundingBoxRight(bitmapBoundingBox.right);
		request.setBoundingBoxBottom(bitmapBoundingBox.bottom);
	}
}

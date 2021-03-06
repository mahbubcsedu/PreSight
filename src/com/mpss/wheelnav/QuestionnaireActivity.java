package com.mpss.wheelnav;

import java.io.File;
import java.util.Locale;

import com.mpss.wheelnav.R;
import com.mpss.wheelnav.model.AccessibilityIssueRequest;
import com.mpss.wheelnav.model.IssueType;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class QuestionnaireActivity extends Activity {

	private static final String ACCESSIBILITY_ISSUE_REQUEST = "ACCESSIBILITY_ISSUE_REQUEST";
	private static final String SAVED_ACCESSIBILITY_ISSUE_REQUEST = "SAVED_ACCESSIBILITY_ISSUE_REQUEST";

	private AccessibilityIssueRequest request;

	private RadioGroup issueTypeRadioGroup; 
	private NumberPicker meterNumberPicker;
	private NumberPicker centimeterNumberPicker;
	private Button btnNext;
	private ToggleButton btnUnitToggle;
	private TextView textViewUnit;
	private TextView textViewSubunit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		getActionBar().setTitle("Step 3 of 4");
		getActionBar().setSubtitle("Answer 2 Questions");

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
		setContentView(R.layout.activity_questionaaire);

		if(this.getIntent().hasExtra(ACCESSIBILITY_ISSUE_REQUEST)) {
			this.request = this.getIntent().getParcelableExtra(ACCESSIBILITY_ISSUE_REQUEST);
		}
		else {
			// TODO: invalid case
		}

		issueTypeRadioGroup = (RadioGroup)findViewById(R.id.radio_issueType); 

		meterNumberPicker = (NumberPicker)findViewById(R.id.numberPicker_meter);		
		meterNumberPicker.setMinValue(0);
		meterNumberPicker.setMaxValue(10);

		centimeterNumberPicker = (NumberPicker)findViewById(R.id.numberPicker_centimeter);		
		centimeterNumberPicker.setMinValue(0);
		//centimeterNumberPicker.setMaxValue(100);

		textViewUnit = (TextView)findViewById(R.id.textView_unit);
		textViewSubunit = (TextView)findViewById(R.id.textView_subunit);

		btnUnitToggle = (ToggleButton)findViewById(R.id.toggleButton_unit);
		btnUnitToggle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				meterNumberPicker.setValue(0);
				centimeterNumberPicker.setValue(0);

				if(btnUnitToggle.isChecked()) {
					textViewUnit.setText("ft");
					textViewSubunit.setText("in");
					centimeterNumberPicker.setMaxValue(11);
				}
				else {
					textViewUnit.setText("m");
					textViewSubunit.setText("cm");
					centimeterNumberPicker.setMaxValue(99);
				}
			}
		});

		btnNext = (Button) findViewById(R.id.btn_next);
		btnNext.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if(checkValidToMoveNext()) {
							saveQuestionnaireStateToRequest();
							Intent intent = new Intent(QuestionnaireActivity.this, SubmitRequestActivity.class);
							intent.putExtra(ACCESSIBILITY_ISSUE_REQUEST, request);
							startActivity(intent);
						}
						else {
							//TODO: show message
							Toast.makeText(QuestionnaireActivity.this, "ANSWER ALL QUESTIONS", Toast.LENGTH_SHORT).show();
						}
					}
				});

		Button cancelButton = (Button) findViewById(R.id.button_cancel);
		cancelButton.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Builder builder = new AlertDialog.Builder(QuestionnaireActivity.this);
						builder.setMessage("Do you want to cancel this request?")
						.setCancelable(false)
						.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								voidTheRequest();
								finish();
								Intent intent = new Intent(QuestionnaireActivity.this, MainActivity.class);
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
		loadQuestionnaireFromRequest();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		saveQuestionnaireStateToRequest();
		Intent intent = new Intent(QuestionnaireActivity.this, BoundingBoxActivity.class);
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
		saveQuestionnaireStateToRequest();
		savedInstanceState.putParcelable(SAVED_ACCESSIBILITY_ISSUE_REQUEST, request);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void saveQuestionnaireStateToRequest() {

		switch(issueTypeRadioGroup.getCheckedRadioButtonId()) {
		case R.id.radio0:
			request.setIssueType("Type1");
			break;
		case R.id.radio1:
			request.setIssueType("Type2");
			break;
		case R.id.radio2:
			request.setIssueType("Type3");
			break;
		case R.id.radio3:
			request.setIssueType("Type4");
			break;
		case R.id.radio4:
			request.setIssueType("Type5");
			break;
		}

		request.setDistanceToSubject((float)getDistanceToSubject());
		
		if(btnUnitToggle.isChecked()) {
			request.setDistanceUnit("FOOT");
		}
		else {
			request.setDistanceUnit("METER");
		}
	} 

	private void loadQuestionnaireFromRequest() {

		if (request.getIssueType()!=null && request.getIssueType().trim()!="") {

			IssueType type = IssueType.valueOf(request.getIssueType().trim());
			switch(type) {
			case Type1:
				issueTypeRadioGroup.check(R.id.radio0);
				break;
			case Type2:
				issueTypeRadioGroup.check(R.id.radio1);
				break;
			case Type3:
				issueTypeRadioGroup.check(R.id.radio2);
				break;
			case Type4:
				issueTypeRadioGroup.check(R.id.radio3);
				break;
			case Type5:
				issueTypeRadioGroup.check(R.id.radio4);
				break;
			}
		}
		else {
			issueTypeRadioGroup.clearCheck();
		}

		if(request.getDistanceUnit()!=null && request.getDistanceUnit().trim()!="") {
			if(request.getDistanceUnit().toUpperCase(Locale.getDefault()).equals("METER")) {
				btnUnitToggle.setChecked(false);	
				textViewUnit.setText("m");
				textViewSubunit.setText("cm");
				centimeterNumberPicker.setMaxValue(99);
			}
			else if(request.getDistanceUnit().toUpperCase(Locale.getDefault()).equals("FOOT")) {
				btnUnitToggle.setChecked(true);
				textViewUnit.setText("ft");
				textViewSubunit.setText("in");
				centimeterNumberPicker.setMaxValue(11);
			}
		}
		else {
			btnUnitToggle.setChecked(true);
			textViewUnit.setText("ft");
			textViewSubunit.setText("in");
			centimeterNumberPicker.setMinValue(0);
			centimeterNumberPicker.setMaxValue(11);
		}
		
		if (request.getDistanceToSubject()> 0) {
			String doubleAsText = Float.toString(request.getDistanceToSubject());
			int meterDistance = Integer.parseInt(doubleAsText.split("\\.")[0].toString());
			int centimeterDistance = Integer.parseInt(doubleAsText.split("\\.")[1].toString());
			meterNumberPicker.setValue(meterDistance);
			centimeterNumberPicker.setValue(centimeterDistance);
		}
		else {
			meterNumberPicker.setValue(0);
			centimeterNumberPicker.setValue(0);
		}

	}

	private boolean checkValidToMoveNext() {
		if(issueTypeRadioGroup.getCheckedRadioButtonId()==-1 || getDistanceToSubject()<=0) {
			return false;
		}
		return true;
	}

	private double getDistanceToSubject() {

		return Double.parseDouble(Integer.toString(meterNumberPicker.getValue()) + "." + 
				Integer.toString(centimeterNumberPicker.getValue()));
	}

}

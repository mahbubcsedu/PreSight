package com.mpss.wheelnav;

import java.util.ArrayList;
import java.util.List;

import com.mpss.wheelnav.R;
import com.mpss.wheelnav.model.DatabaseHandler;
import com.mpss.wheelnav.model.IssueRequest;

import android.app.ListActivity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

public class RequestsListViewActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		getActionBar().setTitle("Previous Requests");
		//getActionBar().setSubtitle("Review and Submit");

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
		setContentView(R.layout.activity_requests_list_view);
		String[] values = getValues();
		CustomArrayAdapter adapter = new CustomArrayAdapter(this, values);
		setListAdapter(adapter);
	}

	private String[] getValues() {
		List<String> valuesList = new ArrayList<String>();

		DatabaseHandler handler = new DatabaseHandler(this);
		List<IssueRequest> requests = handler.getAllRequests(null);
		for (IssueRequest request:requests) {
			valuesList.add(Integer.toString(request.get_id()));
		}

		String[] valuesArray = new String[valuesList.size()];
		return valuesList.toArray(valuesArray);
	}


}

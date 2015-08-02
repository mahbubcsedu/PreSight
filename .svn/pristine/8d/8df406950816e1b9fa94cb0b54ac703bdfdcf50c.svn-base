package com.mpss.wheelnav;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.client.methods.HttpPost;
import com.mpss.wheelnav.R;
import com.mpss.wheelnav.model.DatabaseHandler;
import com.mpss.wheelnav.model.IssueRequest;
import com.mpss.wheelnav.model.UploadToServerResponse;
import com.mpss.wheelnav.versionupdate.AutoUpdateActivity;
import com.mpss.wheelnav.versionupdate.VersionCheck;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	final VersionCheck mVC = new VersionCheck();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getActionBar().hide();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Thread downloadThread = new Thread(backgroundDownload, "VersionCheck");
        downloadThread.start();
		

		Button btnNewRequest = (Button)findViewById(R.id.btnNewRequest);
		Button btnViewAll = (Button) findViewById(R.id.btnViewAll);
		TextView textViewHtmlLink = (TextView) findViewById(R.id.textView_htmlLink);
		
		textViewHtmlLink.setText(Html.fromHtml("<p>Learn about <i><b>Accessibility Issue Types</b></i> <a href=\"http://mpss.csce.uark.edu/mpss/wheelnav/detail-problem/\">here.</a></p>"));
		textViewHtmlLink. setMovementMethod(LinkMovementMethod.getInstance());

		btnNewRequest.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, CustomCameraActivity.class);
				startActivity(intent);
			}
		});

		btnViewAll.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, RequestsListViewActivity.class);
				startActivity(intent);
			}
		});
		
		
		
		
		
	}
	   //private  Handler handler = new  Handler ();
	 // Runnable already retrieved data in a background thread.
	    // Equivalent to doInBackground () of AsyncTask
	    private  Runnable backgroundDownload = new  Runnable () {
	        @ Override
	        public  void  run () {
	            mVC.getData(MainActivity.this);
	            // When finished downloading the updated interface
	            //handler.post (finishBackgroundDownload);
	           // handler.postDelayed(backgroundDownload, 1);
	            if(mVC.isNewVersionAvailable())
	    		{
	    			createNotification();
	    		}
	        }
	    };
	 @SuppressLint("NewApi")
	public void createNotification() {
		    // Prepare intent which is triggered if the
		    // notification is selected
		    Intent intent = new Intent(this, AutoUpdateActivity.class);
		    PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

		    // Build notification
		    // Actions are just fake
		    Notification noti = new Notification.Builder(this)
		        .setContentTitle("New version available")
		        .setContentText("Wheelnav new version is available, tap to update").setSmallIcon(R.drawable.ic_launcher)
		        .setContentIntent(pIntent).build();
		    
		    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		    // hide the notification after its selected
		    noti.flags |= Notification.FLAG_AUTO_CANCEL;

		    notificationManager.notify(0, noti);

		  }
	 
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	protected void onResume() {
		super.onResume(); 

		//TODO: check for internet connection 

		DatabaseHandler dbHandler = new DatabaseHandler(MainActivity.this);
		List<IssueRequest> requests = dbHandler.getAllRequests("false");
		List<String> requestIds = new ArrayList<String>();
		if(requests!=null) {
			for(IssueRequest request : requests) {
				requestIds.add(Integer.toString(request.get_id()));
			}
		}
		String[] requestIdArray = new String[requests.size()];
		new RequestStatusPoller().execute(requestIds.toArray(requestIdArray));	
		
	}

	public class RequestStatusPoller extends AsyncTask<String, String, Boolean>{
		@Override
		protected Boolean doInBackground(String... requestIds) {

			try {
				DatabaseHandler dbHandler = new DatabaseHandler(MainActivity.this);
				int count = requestIds.length;
				for (int i = 0; i < count; i++) {
					IssueRequest request = dbHandler.getRequest(Integer.parseInt(requestIds[i]));
					HTTPRequestManager manager = new HTTPRequestManager(getString(R.string.newrequest_url), MainActivity.this);
					HttpPost httpPostRequest = manager.createHttpRequest(request);

					if(httpPostRequest==null) {
						//return false;
						//TODO: return 
					}	

					UploadToServerResponse responseObj = manager.postRequest(httpPostRequest);

					if(responseObj==null) {
						//return false;
						//TODO: return 
					}

					manager.processResponse(Integer.parseInt(requestIds[i]), responseObj);
				}
			}
			catch (Exception ex) {

			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
		}

	}


}

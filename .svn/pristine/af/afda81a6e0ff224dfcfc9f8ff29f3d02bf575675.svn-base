package com.mpss.wheelnav.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.os.Environment;
import android.util.Log;

public class AppLog {
        private static final String APP_TAG = "WheelNav";
        
        public static int logString(String message){
        	    AppLog.appendLog(APP_TAG+message);
                return Log.i(APP_TAG,message);
        }
        
        public static void appendLog(String text)
        {       
        	File myFile = new File(Environment.getExternalStorageDirectory(), "log.txt");
           if (!myFile.exists())
           {
              try
              {
            	  myFile.createNewFile();
              } 
              catch (IOException e)
              {
                 // TODO Auto-generated catch block
                 e.printStackTrace();
              }
           }
           try
           {
              //BufferedWriter for performance, true to set append to file flag
              BufferedWriter buf = new BufferedWriter(new FileWriter(myFile, true)); 
              buf.append(text);
              buf.newLine();
              buf.close();
           }
           catch (IOException e)
           {
              e.printStackTrace();
           }
        }
}
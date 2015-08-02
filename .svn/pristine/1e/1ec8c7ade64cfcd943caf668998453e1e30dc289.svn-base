package com.mpss.wheelnav.utility;

import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

public class GPSLocationService extends Service implements LocationListener{

	
	private final Context context;
	Location location;
	LocationManager locationManager;
	boolean isGPSEnabled=false,isNetworkEnabled=false,isPassiveEnabled=false;
	boolean stateGetLocation=false;
	double latitude,longitude;
	String stateName;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 5; // 10 meters
 
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
	
	
	
	public GPSLocationService(Context context) {
		super();
		this.context = context;
		getLocation();
	}
	
	public Location getLocation() {
        try {
            locationManager = (LocationManager) context
                    .getSystemService(LOCATION_SERVICE);
 
            isPassiveEnabled = locationManager
            		.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);
            
            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
 
            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
 
            if (!isGPSEnabled && !isNetworkEnabled && !isPassiveEnabled) {
                // no network provider is enabled
            } else {
                this.stateGetLocation = true;
                
             // First get location from Network Provider
                if (isPassiveEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.PASSIVE_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Passive", "Passive location");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
 
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        return location;
    }

	
	public String getAddress(Location loc){
		
			
    	double latitude; 
    	double longitude; 
    	String fulladdress=null;
    	
    	
		try {
			latitude= loc.getLatitude();		
			longitude= loc.getLongitude();


	         Geocoder gc = new Geocoder(this.context, Locale.getDefault());

			List<Address> addresses;
			addresses = gc.getFromLocation(latitude, longitude, 1);
		
		    StringBuilder sb = new StringBuilder();
		    if (addresses.size() > 0) 
		    {
			Address address = addresses.get(0);

			for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
			
				sb.append(address.getAddressLine(i)).append("\n");
				sb.append(address.getLocality()).append("\n");
				sb.append(address.getPostalCode()).append("\n");
				sb.append(address.getCountryName());
				sb.append(address.getAdminArea().toLowerCase());
				stateName=address.getAdminArea().toLowerCase();
				fulladdress=sb.toString();
				//Toast.makeText(this, address.getLocality(), Toast.LENGTH_SHORT);
		}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return fulladdress;
    }
	
	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public boolean stateGetLocation() {
        return this.stateGetLocation;
    }
	 /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     * */
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
      
        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");
  
        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
  
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });
  
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });
  
        // Showing Alert Message
        alertDialog.show();
    }
	
    
    public void stopGPSUse()
    {
    	if(locationManager!=null)
    	{
    		locationManager.removeUpdates(GPSLocationService.this);
    	}
    }
    
	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public boolean isStateGetLocation() {
		return stateGetLocation;
	}

	public void setStateGetLocation(boolean stateGetLocation) {
		this.stateGetLocation = stateGetLocation;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}

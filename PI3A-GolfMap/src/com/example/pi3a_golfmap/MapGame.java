package com.example.pi3a_golfmap;

//import JAVA;
import java.util.HashMap;

//import JSON;
import org.json.JSONException;
import org.json.JSONObject;

//import ANDROID;
import android.app.Activity;
import android.app.Dialog;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

//import PROJECT;
import com.example.pi3a_golfmap.R;
import com.example.pi3a_golfmap.LatLngInterpolator.Linear;
import com.example.pi3a_golfmap.MarkerAnimation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * This class implement the Google Map API v2 and manage the game events 
 * @author Dan Castro-Lopez
 * @author Thomas Prak
 * 
 * Google Map API V2 for android
 *@see https://developers.google.com/maps/documentation/android/start?hl=fr#getting_the_google_maps_android_api_v2
 */

public class MapGame extends Activity implements GooglePlayServicesClient.ConnectionCallbacks
	,GooglePlayServicesClient.OnConnectionFailedListener
	,OnMarkerDragListener
	,OnMarkerClickListener{
	
	private GoogleMap mMap = null;// the map
	private CameraPosition cameraPosition = null;
	
	private LatLng posBall = null;
	private LatLng posClub = null;
	private LatLng drag = null;
	//private static LatLng imerir = new LatLng(42.674792,2.848261);

	//marker
	private HashMap <String, Marker> mMarkers = new HashMap<String, Marker>();
	//private Marker ballMarker = null;
	//private Marker clubMarker = null;
	private MarkerOptions markerO = null;
	
	//Marker
	//private static int ressource_imerir = R.drawable.imerir;
	private static int ressource_ball= R.drawable.golfball;
	private static int ressource_club=R.drawable.club;
	
	private Polyline polyline = null;
	
	private float zoom = 6.0f;
	
	private String tokenId="";
	
	TextView tvLocInfo;
	
	private static String urltoken="http://172.31.1.64:34568/client/init";
	private static String urlshot="http://172.31.1.64:34568/client/shot";
	
	public MapGame() {}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_game);
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
		
		// Showing status
		if(status!=ConnectionResult.SUCCESS){ 
			// Google Play Services are not available
			int requestCode = 10;
		    Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
		    dialog.show();
		
		}else { 
			setUpMapIfNeeded();
			mMap.setOnMyLocationChangeListener(onMyLocationChange(this));						
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map_game, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Method who verify the map availability.
	 * If the map does not exist, it create the map and setup the different control gesture 
	 * and set the localization user to enable.
	 */
	private void setUpMapIfNeeded() {
	    // Do a null check to confirm that we have not already instantiated the map.
	    if (this.mMap == null) {
	        this.mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
	        
	        // Check if we were successful in obtaining the map.
	        if (this.mMap != null) {// The Map is verified. It is now safe to manipulate the map.
	            
	        	//this.addMarkerPoint(new LatLng(42.674792,2.848261),ressource_imerir,"IMERIR",false);
	        	
	        	//Location ,drag and click Listener ENABLE
	        	mMap.setMyLocationEnabled(true);
	        	mMap.setOnMarkerDragListener(this);
	        	mMap.setOnMarkerClickListener(this);
	        	
	        	mMap.getUiSettings().setAllGesturesEnabled(false);
				mMap.getUiSettings().setZoomControlsEnabled(false);
				
				mMap.getUiSettings().setZoomGesturesEnabled(true);
				mMap.getUiSettings().setScrollGesturesEnabled(true);
	        }
	    }
	}
	
	public OnMyLocationChangeListener onMyLocationChange(final MapGame mapGame) {
		OnMyLocationChangeListener listener = new GoogleMap.OnMyLocationChangeListener() {

				@Override
				public void onMyLocationChange(Location arg0){	
					//get position
					posBall = new LatLng(arg0.getLatitude(), arg0.getLongitude());
					mapGame.addMarkerPoint(posBall,ressource_ball,"BALL",false);

					cameraPosition = new CameraPosition.Builder().target(posBall).zoom(zoom).build();
					mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));	
					
					mMap.setOnMyLocationChangeListener(null);
					mMap.setMyLocationEnabled(false);
					
					Log.d("Location Listener","Try to get the token");
				}
	        };  
		return listener;
    }
	
	/**
	 * This method allow to add marker easily on the map by just passing the options we use in this application
	 * @param location Put the marker at this position
	 * @param ressource Customize the marker appearance with the image resource chosen
	 * @param title
	 * @param b
	 */
	public Marker addMarkerPoint(LatLng location, int ressource, String id, boolean b) { 
		// create marker
		Marker marker = null;
		markerO = new MarkerOptions().position(location).draggable(b);
		// Changing marker icon
		if (ressource != 0)
			markerO.icon(BitmapDescriptorFactory.fromResource(ressource));
		
		//Save the marker id on the HashMap
		if(ressource == R.drawable.golfball)
			markerO.anchor(0.5f, 0.5f);
		
		// adding marker
		marker = mMap.addMarker(markerO);
		
		//save
		mMarkers.put(id, marker);
		return marker;
	}
	
	void fetchTokenId() 
	{
		Log.d("Step0","Enter in step0 method");
		JSONObject jsonData = new JSONObject();
		double latitude = posBall.latitude;
		double longitude= posBall.longitude;
		
		try {
			jsonData.put("BallLat", latitude);
			jsonData.put("BallLng", longitude);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		new DownloadAsyncTask(new RequestInterface() 
		{
			public JSONObject processJSON(JSONObject rawJson) 
			{
				Log.d ("Step0-0","before");
				try {
					tokenId =rawJson.getString("token");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Log.d ("Step0-1",tokenId);
				return rawJson;
			}
		}).execute(urltoken, jsonData.toString());
		
	}
	
	void fetchNewBallPosition() 
	{
		
		JSONObject jsonData = new JSONObject();
		double ballLat = posBall.latitude;
		double ballLng = posBall.longitude;
		double clubLat = posClub.latitude;
		double clubLng = posClub.longitude;
		
		
		try {
			jsonData.put("Token", tokenId);
			jsonData.put("BallLat", ballLat);
			jsonData.put("BallLng", ballLng);
			jsonData.put("ClubLat", clubLat);
			jsonData.put("ClubLng", clubLng);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d("Step1",jsonData.toString());
		new DownloadAsyncTask(new RequestInterface() 
		{
			public JSONObject processJSON(JSONObject rawJson) 
			{ 
				double lat;
				double lon;
				Log.d("step1", "after async task");
				try {
					if (rawJson!=null){
						lat= rawJson.getJSONObject("coordinates").getDouble("lat");
						lon =rawJson.getJSONObject("coordinates").getDouble("lon");					
						posBall = new LatLng(lat,lon);
						
						MarkerAnimation.animateMarkerToGB(mMarkers.get("BALL"), posBall, new Linear());
						cameraPosition = new CameraPosition.Builder().target(posBall).zoom(zoom).build();
						mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
						
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return rawJson;
			}

		}).execute(urlshot, jsonData.toString());
		
	}
	
	@Override
    public void onMarkerDragStart(Marker arg0) {}
	
	@Override
    public void onMarkerDrag(Marker arg0) {
		if(polyline != null)
			polyline.remove();
    
		drag = arg0.getPosition();
        double dragLat = drag.latitude;
        double dragLong = drag.longitude;
		
        //Directive line
		double newlat = ((posBall.latitude - dragLat)/3) + posBall.latitude;
	    double newlon =  ((posBall.longitude - dragLong)/3) + posBall.longitude;
	    
	   	// Instantiates a new Polyline object and adds points to define a rectangle
	    PolylineOptions rectOptions = new PolylineOptions()
	            .add(posBall)
	            .add(new LatLng(newlat, newlon))
	            .width(8);
	    
	    // Get back the mutable Polyline
	    polyline = mMap.addPolyline(rectOptions);

    }
 
    /**
     * Implemented method from the Google Map API V2
     * It is called when a marker has finished being dragged.
     * Here we use this method principally to catch the club marker position.
     * 
     * @see https://developer.android.com/reference/com/google/android/gms/maps/GoogleMap.OnMarkerDragListener.html
     */
	 @Override
    public void onMarkerDragEnd(Marker arg0) {
    	
    	polyline.remove();
    	
    	if (mMarkers.get("CLUB").getId().equals(arg0.getId())){
    		
	        drag = arg0.getPosition();
	        double dragLat = drag.latitude;
	        double dragLong = drag.longitude;
	        
	        posClub =  new LatLng(dragLat, dragLong);
	        
	        fetchNewBallPosition();
	        
	        double newlat = ((posBall.latitude - dragLat)*2.0) + posBall.latitude;
	        double newlon =  ((posBall.longitude - dragLong)*2.0) + posBall.longitude;
	        
	        MarkerAnimation.animateMarkerToGB(mMarkers.get("CLUB"), posBall, new Linear());	
	        
	        if(newlat > 85 || newlat < -85){
				newlat = newlat*Math.PI/180;
				newlat = Math.sin(newlat);
				newlat = newlat*85.0;
				if(newlat > 85 || newlat < -270)
					newlon -= 180; 
				if(newlat > 270 || newlat < -85)
					newlon += 360; 
			}
	        else{}	
			
	        MarkerAnimation.animateMarkerToGB(mMarkers.get("BALL"), new LatLng(newlat, newlon), new Linear());
	        
			cameraPosition = new CameraPosition.Builder().target(new LatLng(newlat, newlon)).zoom(zoom).build();
			mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
			
			mMap.getUiSettings().setScrollGesturesEnabled(true);
			mMap.setMyLocationEnabled(true);
			
			Log.d("DAN","remove marker");
			
			//remove the marker
			mMarkers.get("CLUB").remove();
			
			Log.d("DAN","after remove marker");
			
			//remove the marker in hashmap
			mMarkers.remove("CLUB");
			
			Log.d("DAN","remove hashmap marker");
			
	 	}
    }
   
	 @Override
	public boolean onMarkerClick(Marker arg0) {
		 
		mMap.getUiSettings().setAllGesturesEnabled(false);
		fetchTokenId();
		if(mMarkers.get("BALL") != null){
			if(mMarkers.get("CLUB") == null)
				this.addMarkerPoint(posBall,ressource_club,"CLUB",true);	
			
			//set marker option (position and title)
			cameraPosition = new CameraPosition.Builder().target(posBall).zoom(zoom).build();
			mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		}
		else{}

		return false;
	}
	 
    public class DownloadAsyncTask extends AsyncTask<String, Void,JSONObject > {
    	
    	RequestInterface strategy;
    	JSONObject response = new JSONObject();
		
    	DownloadAsyncTask(RequestInterface strategy){
    		super();
    		 Log.d("Async e","Test");
    		this.strategy=strategy;
    		
    	}
    	
		protected JSONObject doInBackground(String... urls) {
			
			Log.d("Async stringResponse","Test");
			//return serverPostRequestToken(urls[0],urls[1]);
			ServerRequest postRequest = new ServerRequest();
			return postRequest.Post(urls[0], urls[1]);			
		}		
		
		protected void onPostExecute(JSONObject result) 
		{
			Log.d("onPostExecute",result.toString());
			strategy.processJSON(result);
			Log.d("onPostExecute",response.toString());
        }
    }
	
    public void zoom1(View sender){
		zoom = 7.0f;
		cameraPosition = new CameraPosition.Builder().target(posBall).zoom(zoom).build();
		mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}
	
	public void zoom2(View sender){
		zoom = 6.0f;	
		cameraPosition = new CameraPosition.Builder().target(posBall).zoom(zoom).build();
		mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}
	
	public void zoom3(View sender){
		zoom = 5.0f;
		cameraPosition = new CameraPosition.Builder().target(posBall).zoom(zoom).build();
		mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}
    
	@Override
	public void onConnected(Bundle arg0) {}
	
	public void onConnectionFailed(ConnectionResult arg0) {}
	
	@Override
	public void onDisconnected() {}
}




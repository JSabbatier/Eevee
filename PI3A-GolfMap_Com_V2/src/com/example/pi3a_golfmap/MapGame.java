package com.example.pi3a_golfmap;

//import android.R;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore.Files;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import android.widget.Toast;
import android.content.res.Resources;
import android.graphics.Path;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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
	,OnMarkerDragListener{
	
	private MapView mapView=null;
	private GoogleMap mMap = null;// the map
	private CameraPosition cameraPosition = null;
	
	private LatLng posBall = null;
	private LatLng posClub = null;
	//private static LatLng imerir = new LatLng(42.674792,2.848261);

	//marker
	private HashMap <String, String> mMarkers = new HashMap<String, String>();
	private Marker ballMarker;
	private Marker clubMarker;
	
	//Marker
	//private static int ressource_imerir = R.drawable.imerir;
	private static int ressource_ball= R.drawable.golfball;
	private static int ressource_club=R.drawable.club;
	
	private String tokenId="";
	
	private JSONObject jsonLocation;	
	private LatLng newballocation;
	TextView tvLocInfo;
	
	private static String urltoken="http://172.31.1.64:34568/client/init";
	private static String urlshot="http://172.31.1.64:34568/client/shot";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_game);
		
		//Initialization
		jsonLocation = new JSONObject();
		
		
		Log.d("onCreate","Set ressources");
		
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
		// Showing status
		if(status!=ConnectionResult.SUCCESS){ 
			// Google Play Services are not available
			int requestCode = 10;
		    Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
		    dialog.show();
		
		}else {
				
			setUpMapIfNeeded();		
			Log.d("MapGameOnCreate","Map created");	
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
	 * Method who verify the map availablity.
	 * If the map does not exist, it create the map and setup the different control gestion 
	 * and add the current location of the user.
	 */
	private void setUpMapIfNeeded() {
	    // Do a null check to confirm that we have not already instantiated the map.
	    if (this.mMap == null) {	
	    	this.mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();	
	 
	        Log.d("DAN","Map acquiered");
	        // Check if we were successful in obtaining the map.
	        if (this.mMap != null) {
	            // The Map is verified. It is now safe to manipulate the map.
	        	mMap.setMyLocationEnabled(true);
	        	mMap.setOnMarkerDragListener(this);
	        	//this.addMarkerPoint(imerir,ressource_imerir,"IMERIR",false);
	        	Log.d("DAN","Location enable plus imerir marker");
	        	
	        	mMap.getUiSettings().setAllGesturesEnabled(false);
				mMap.getUiSettings().setZoomControlsEnabled(false);
				mMap.getUiSettings().setZoomGesturesEnabled(true);
				mMap.getUiSettings().setScrollGesturesEnabled(true);
				Log.d("DAN","Map option set");
	        }
	    }
	}
	
	public OnMyLocationChangeListener onMyLocationChange(final MapGame mapGame) {
		OnMyLocationChangeListener listener = new GoogleMap.OnMyLocationChangeListener() {
			
			@Override
			public void onMyLocationChange(Location arg0){
				
				//get location
				posBall = new LatLng(arg0.getLatitude(), arg0.getLongitude());
				Log.d("Play","Position acquired");
				
				
				//Adding markers				
				ballMarker = mapGame.addMarkerPoint(posBall,ressource_ball,"Shoot me :)",false,"ball");
				clubMarker = mapGame.addMarkerPoint(posBall,ressource_club,"I'm a club",true,"club");
				Log.d("Play","Ball marker placed");
				
				
				//set marker option (position and title)
				cameraPosition = new CameraPosition.Builder().target(posBall).zoom(7).build();
				mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
				Log.d("Play","Camera on marker");
				
				mMap.setOnMyLocationChangeListener(null);
				mMap.setMyLocationEnabled(false);
				Log.d("Play","Listener and location off");
			     
		        Log.d("Play","Try to get the token");
		        fetchTokenId();
			}
        };
   
		return listener;
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
				try {
					tokenId =rawJson.getString("token");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Log.d ("Step0",tokenId);
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
						ballMarker.setPosition(posBall);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return rawJson;
			}

		}).execute(urlshot, jsonData.toString());
		
	}
	
	/**
	 * This method allow to add marker easily on the map by just passing the options we use in this application
	 * @param location Put the marker at this position
	 * @param ressource Customize the marker appearance with the image resource chosen
	 * @param title
	 * @param b
	 */
	public Marker addMarkerPoint(LatLng location,int ressource, String title,boolean b, String id) { 
		MarkerOptions markerOptions;
		Marker marker;
		
		// create marker options
		markerOptions = new MarkerOptions().position(location).title(title).draggable(b);
		
		// Changing marker icon
		if (ressource != 0)
			markerOptions.icon(BitmapDescriptorFactory.fromResource(ressource));
		
		// adding marker on the map
		marker = mMap.addMarker(markerOptions);
		
		//Save the marker id on the HashMap
		mMarkers.put(marker.getId(), id);
		Log.d("marker get", marker.getId());
		Log.d("marker add",mMarkers.get(marker.getId()));
		return marker;
	}
	
	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}

	public void playGame(View sender){
		
		if (this.mMap != null){
			Log.d("DAN","Listener Location start");
			mMap.setOnMyLocationChangeListener(onMyLocationChange(this));
			
			mMap.getUiSettings().setAllGesturesEnabled(false);
			
			//mMap.getUiSettings().setZoomGesturesEnabled(true);
			//mMap.getUiSettings().setScrollGesturesEnabled(true);
		}
		else{
			Log.d("DAN","map failed");
		}
		
	}
	 
	@Override
	public void onMarkerDragStart(Marker arg0) {
	        Log.d("marker drag id ", arg0.getId());
	    }
	
	@Override
    public void onMarkerDrag(Marker arg0) {
        // TODO Auto-generated method stub
         
    }
 
    /**
     * Implemented method from the Google Map API V2
     * It is called when a marker has finished being dragged.
     * Here we use this method principally to catch the club marker position.
     * 
     * @see https://developer.android.com/reference/com/google/android/gms/maps/GoogleMap.OnMarkerDragListener.html
     */
    public void onMarkerDragEnd(Marker marker) {
        // TODO Auto-generated method stub
    	Log.d("marker get", marker.getId());
    	
        if (mMarkers.get(marker.getId())== "club")
        {
        	Log.d("club","new position calculate");
	    	LatLng dragPosition = marker.getPosition();
	        double dragLat = dragPosition.latitude;
	        double dragLong = dragPosition.longitude;
	        Log.i("info","START : "+ posBall.latitude + "," + posBall.longitude);
	        Log.i("info","END   : "+ dragLat + "," + dragLong);
	        
	        //map game vecteur variable
	        posClub = new LatLng(dragLat, dragLong);
	        
	      
	        Log.d("Step1","Enter Step1");
	        fetchNewBallPosition();
        }
  
    }
   
    public JSONObject serverPostRequestToken(String url, String postdata) {
    	HttpClient httpClient;
    	HttpPost httpPost;
    	InputStream inputStream = null;
    	JSONObject stringResponse= new JSONObject();
    	
			try {
				httpClient = new DefaultHttpClient();
	    		httpPost = new HttpPost(url);
	    		StringEntity se;
				se = new StringEntity(postdata);
				
		  		httpPost.setEntity(se);
	    		httpPost.addHeader("Content-Type", "application/json");
	    		
	    		HttpResponse httpResponse = httpClient.execute(httpPost);
	            inputStream = httpResponse.getEntity().getContent();
	    	
	            if(inputStream != null){
	            	Log.d("RequestServer","inputStream non null");
	            	stringResponse = convertInputStreamToJSONObject(inputStream);
	            	
	            }
	    				
	            Log.d("RequestServer","envoi de la reponse");
	            Log.d("RequestServerResponse",stringResponse.toString());
	            return stringResponse;
			} catch (Exception e) {
				return stringResponse=null;
			}
    }
    	
    	
	private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
 
        inputStream.close();
        return result;
    }
	
private static JSONObject convertInputStreamToJSONObject(InputStream inputStream) throws IOException, JSONException{
		 BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
		    String line = "";
		    String result = "";
		    while((line = bufferedReader.readLine()) != null)
		        result += line;

		    inputStream.close();
		    return new JSONObject(result); 
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
			JSONObject response =strategy.processJSON(result);
			Log.d("onPostExecute",response.toString());
        }
    }
}




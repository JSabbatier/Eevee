package com.example.pi3a_golfmap;

// google map import
import java.util.HashMap;

import android.app.Activity;
import android.app.Dialog;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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


public class MapGame extends Activity implements GooglePlayServicesClient.ConnectionCallbacks,GooglePlayServicesClient.OnConnectionFailedListener, OnMarkerDragListener, OnMarkerClickListener {
	
	private GoogleMap mMap = null;// the map
	private CameraPosition cameraPosition = null;
	
	private LatLng posBall = null;
	private LatLng drag = null;
	
	private HashMap <String, String> mMarkers = new HashMap<String, String>();
	private Marker ballMarker = null;
	private Marker clubMarker = null;
	private MarkerOptions markerO = null;
	
	private final int ressource_imerir = R.drawable.unnamed;
	private final int ressource_ball = R.drawable.golfball;
	private final int ressource_club = R.drawable.club;
	
	private Polyline polyline = null;
	
	private float zoom;

	public MapGame() {
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_game);		
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
		
		//Initialization
		
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
		// Inflate the menu, this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map_game, menu);
		return true;
	}
	
	/**
	 * Method who verify the map availability.
	 * If the map does not exist, it create the map and setup the different control gestion 
	 * and add the current location of the user.
	 */
	private void setUpMapIfNeeded() {
	    // Do a null check to confirm that we have not already instantiated the map.
	    if (this.mMap == null) {
	        this.mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
	        
	        // Check if we were successful in obtaining the map.
	        if (this.mMap != null) {// The Map is verified. It is now safe to manipulate the map.
	            
	        	this.addMarkerPoint(new LatLng(42.674792,2.848261),ressource_imerir,"IMERIR",false);
	        	
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
				ballMarker = mapGame.addMarkerPoint(posBall,ressource_ball,"BALL",false);
				//set marker option (position and title)
				cameraPosition = new CameraPosition.Builder().target(posBall).zoom(zoom).build();
				mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));		
				mMap.setOnMyLocationChangeListener(null);
				mMap.setMyLocationEnabled(false);
				step0_fetchTokenId();
			}
        };
		return listener;
    }

	void step0_fetchTokenId() {}
	void step1_fetchTokenId() {}
	void step2_fetchTokenId() {}
	
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
		mMarkers.put(marker.getId(), id);
		return marker;
	}
	
	@Override
    public void onMarkerDragStart(Marker arg0) {
		
    }
	
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
 
    @Override
    public void onMarkerDragEnd(Marker arg0) {
    	
    	polyline.remove();
    	
        drag = arg0.getPosition();
        double dragLat = drag.latitude;
        double dragLong = drag.longitude;
             
        double newlat = ((posBall.latitude - dragLat)*2.0) + posBall.latitude;
        double newlon =  ((posBall.longitude - dragLong)*2.0) + posBall.longitude;
        
        MarkerAnimation.animateMarkerToGB(clubMarker, posBall, new Linear());	
        
        if(newlat > 85 || newlat < -85){
			newlat = newlat*Math.PI/180;
			newlat = Math.sin(newlat);
			newlat = newlat*85.0;
			if(newlat > 85 || newlat < -270)
				newlon -= 180; 
			if(newlat > 270 || newlat < -85)
				newlon += 360; 
			posBall = new LatLng(newlat, newlon);
			MarkerAnimation.animateMarkerToGB(ballMarker, posBall, new LatLngInterpolator.Spherical());
		}
        else{
        	posBall = new LatLng(newlat, newlon);
    		MarkerAnimation.animateMarkerToGB(ballMarker, posBall, new Linear());
        }	
		

		cameraPosition = new CameraPosition.Builder().target(posBall).zoom(zoom).build();
		mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		
		mMap.getUiSettings().setZoomGesturesEnabled(true);
		mMap.getUiSettings().setScrollGesturesEnabled(true);
		mMap.setMyLocationEnabled(true);
		clubMarker = null;
		step1_fetchTokenId();
    }

	@Override
	public boolean onMarkerClick(Marker arg0) {
		
		mMap.getUiSettings().setAllGesturesEnabled(false);
		if(ballMarker != null){
		if(clubMarker == null)
			clubMarker = this.addMarkerPoint(posBall,ressource_club,"CLUB",true);				
		//set marker option (position and title)
		cameraPosition.fromLatLngZoom(posBall, zoom);
		mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		}
		else{}

		return false;
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
	
	@Override
	public void onConnected(Bundle arg0) {}

	@Override
	public void onDisconnected() {}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {}
	
	public void zoom1(View sender){
		zoom = 5.0f;
	}
	
	public void zoom2(View sender){
		zoom = 6.0f;	
	}
	
	public void zoom3(View sender){
		zoom = 7.0f;
	}
}



package com.example.pi3a_golfmap;

//import android.R;
import android.app.Activity;
import android.app.Dialog;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.content.res.Resources;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapGame extends Activity implements GooglePlayServicesClient.ConnectionCallbacks,GooglePlayServicesClient.OnConnectionFailedListener, OnMarkerDragListener {
	
	private GoogleMap mMap = null;// the map
	private CameraPosition cameraPosition = null;
	private LatLng pos = null;
	private LatLng posClub = null;
	private static LatLng imerir = null;
	private Marker marker = null;
	private MarkerOptions markerO = null;
	private static int ressource_imerir;
	private static int ressource_ball;
	TextView tvLocInfo;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_game);
		
		Log.d("DAN","Start map");
		
		
		imerir = new LatLng(42.674792,2.848261);
		ressource_imerir = R.drawable.unnamed;
		ressource_ball = R.drawable.golfball;
		
		Log.d("DAN","Set ressources");
		
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
		// Showing status
		if(status!=ConnectionResult.SUCCESS){ 
			// Google Play Services are not available
			int requestCode = 10;
		    Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
		    dialog.show();
		
		}else { 
			setUpMapIfNeeded();		
			Log.d("DAN","Map set");
						
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
	
	private void setUpMapIfNeeded() {
	    // Do a null check to confirm that we have not already instantiated the map.
	    if (this.mMap == null) {
	        this.mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
	        Log.d("DAN","Map aquiered");
	        // Check if we were successful in obtaining the map.
	        if (this.mMap != null) {
	            // The Map is verified. It is now safe to manipulate the map.
	        	mMap.setMyLocationEnabled(true);
	        	mMap.setOnMarkerDragListener(this);
	        	this.addMarkerPoint(imerir,ressource_imerir,"IMERIR",false);
	        	Log.d("DAN","Location enable plus imerir marker");
	        	
	        	mMap.getUiSettings().setAllGesturesEnabled(false);
				mMap.getUiSettings().setZoomControlsEnabled(false);
				mMap.getUiSettings().setZoomGesturesEnabled(true);
				mMap.getUiSettings().setScrollGesturesEnabled(true);
				Log.d("DAN","Map option set");
	        }
	    }
	}
	
	public MapGame() {
		// TODO Auto-generated constructor stub
	}
	
	public OnMyLocationChangeListener onMyLocationChange(final MapGame mapGame) {
		OnMyLocationChangeListener listener = new GoogleMap.OnMyLocationChangeListener() {
			
			@Override
			public void onMyLocationChange(Location arg0){
				
				Log.d("DAN","Position aquiered");
				
				//get position
				pos = new LatLng(arg0.getLatitude(), arg0.getLongitude());
				mapGame.addMarkerPoint(pos,ressource_ball,"Shoot me :D",false);
				mapGame.addMarkerPoint(pos,R.drawable.club,"I'm a club",true);
				
				Log.d("DAN","Ball marker placed");
				
				//set marker option (position and title)
				cameraPosition = new CameraPosition.Builder().target(pos).zoom(7).build();
				mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
				
				Log.d("DAN","Camera on marker");
				
				mMap.setOnMyLocationChangeListener(null);
				mMap.setMyLocationEnabled(false);
				
				Log.d("DAN","Listener and location off");
		
			}
        };
        
		return listener;
    }
	
	public void addMarkerPoint(LatLng location,int ressource, String position_name,boolean b) { 
		// create marker
		markerO = new MarkerOptions().position(location).title(position_name).draggable(b);
		// Changing marker icon
		if (ressource != 0)
			markerO.icon(BitmapDescriptorFactory.fromResource(ressource));
		// adding marker
		marker = mMap.addMarker(markerO);
	}
	
	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
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
    public void onMarkerDrag(Marker arg0) {
        // TODO Auto-generated method stub
         
    }
 
    @Override
    public void onMarkerDragEnd(Marker arg0) {
        // TODO Auto-generated method stub
        LatLng dragPosition = arg0.getPosition();
        double dragLat = dragPosition.latitude;
        double dragLong = dragPosition.longitude;
        
        Log.i("info","START : "+ pos.latitude + "," + pos.longitude);
        Log.i("info","END   : "+ dragLat + "," + dragLong);
        
        posClub = new LatLng(dragLat, dragLong);
        //Toast.makeText(getApplicationContext(), "Marker Dragged..!", Toast.LENGTH_LONG).show();
    }
 
    @Override
    public void onMarkerDragStart(Marker arg0) {
        // TODO Auto-generated method stub
    }
}




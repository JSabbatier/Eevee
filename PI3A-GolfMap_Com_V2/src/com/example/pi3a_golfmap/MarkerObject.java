package com.example.pi3a_golfmap;

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
import android.widget.Toast;
import android.content.res.Resources;
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

public class MarkerObject {
	
	private MarkerOptions markerOptions ;
	private Marker marker;
	private GoogleMap mMap;
	
	public MarkerObject(GoogleMap mMap){
		markerOptions = new MarkerOptions();
		this.mMap= mMap;
	}

	public MarkerOptions getMarkerOptions() {
		return markerOptions;
	}

	public void setMarkerOptions(MarkerOptions markerOptions) {
		this.markerOptions = markerOptions;
	}
	
	public void setMarkerOptions(LatLng location,int ressource, String title,boolean b) {
		this.markerOptions = new MarkerOptions().position(location).title(title).draggable(b);
		if (ressource != 0)
			markerOptions.icon(BitmapDescriptorFactory.fromResource(ressource));
	}

	public Marker getMarker() {
		return marker;
	}

	public void setMarker(Marker marker) {
		this.marker = marker;
	}
	
	public void addMarkerToMap() { 
		// adding marker
		this.marker = this.mMap.addMarker(this.markerOptions);
	}


}

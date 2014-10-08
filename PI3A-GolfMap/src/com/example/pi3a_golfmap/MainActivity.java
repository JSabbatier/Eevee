package com.example.pi3a_golfmap;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager.OnActivityResultListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

/**
 * A class who extends Activty
 * It is use to manage the main page of the application
 * @author Thomas PRAK
 * @version 2.0
 *
 *For the Activity class and its implemented methods see:
 *@see http://developer.android.com/reference/android/app/Activity.html
 */


public class MainActivity extends Activity {
	//Attributes for the server request
	private EditText username;
	
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		username= (EditText) findViewById(R.id.enter_username);
		username.setTextColor(Color.WHITE);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
	 * This method allow go to the next Activity
	 * Once the player wrote it's username, the application go to the MapGame Activity
	 * by pressing the Login button
	 * @param view The actual used view
	 * 
	 */
	public void sendLogin(View view)
	{
		Log.d("sendLoginFct","Attempt sending login");
		Intent intent = new Intent(this, MapGame.class);
		Log.d("sendLoginFct","Attempt sending login");

		startActivity(intent);
	}
	
	public boolean ifConnected()
	{
		//Verification of the connectivity availability
		ConnectivityManager connMgr = (ConnectivityManager) 
        getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) 
		{
			return true;
		} 
		else 
		{
			return false;
		}
		
	}
	
	
	
	/**
	 * Subclass of MainActivity
	 * This class allows to perform background operations 
	 * and publish results on the UI thread without having to manipulate threads and/or handlers.
	 * @author Thomas Prak
	 * @see http://developer.android.com/reference/android/os/AsyncTask.html
	 * @deprecated This class was used to test the connection between the application and the server
	 * @deprecated It is not used anymore but can still be useful
	 */
	/*public class SendPositionAsyncTask extends AsyncTask<String, Void, String> {

		
		@Override
		protected String doInBackground(String...urls) {
			
			
			ServerRequest tryGet= new ServerRequest();
			Log.d("SendPos","Hello");
			return tryGet.GET(tryGet.getUrlServer());
		}
		
		protected void onPostExecute(String result) 
		{
            Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            Log.d("Resultat",result);
        }
        

	}*/

}

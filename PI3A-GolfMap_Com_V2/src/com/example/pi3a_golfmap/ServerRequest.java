package com.example.pi3a_golfmap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * This class is used to send post et get request to the server.
 * @author Dan Castro-Lopez
 * @author Thomas Prak
 * 
 * Google Map API V2 for android
 *@see https://developers.google.com/maps/documentation/android/start?hl=fr#getting_the_google_maps_android_api_v2
 */





public class ServerRequest {

	protected static HttpClient httpClient;
	protected static HttpGet httpGet;
	protected static HttpPost httpPost;
	protected static HttpEntity httpEntity;
	protected static HttpResponse httpResponse;
	protected String urlServer;
	protected JSONObject parameters;
	protected JSONObject responseJSON;
	

	public ServerRequest(){
		
	}
	
	public  JSONObject GET(String url) 
	{
		Log.d("Meth Get",url);
	        InputStream inputStream = null;
   
	            // create HttpClient
	            httpClient = new DefaultHttpClient();
	 
	            // make GET request to the given URL
	            httpGet = new HttpGet(url);

	            try {
					httpResponse = httpClient.execute(httpGet);
		            // receive response as inputStream
		            inputStream = httpResponse.getEntity().getContent();
		 
		            // convert inputstream to JSONObject
		            if(inputStream != null)
		            	responseJSON = convertInputStreamToJSONObject(inputStream);
		            
				} catch (Exception e) {
					e.printStackTrace();
				}

	        return responseJSON;
	   }

	public JSONObject Post(String url, String postdata)  {
    	
    	InputStream inputStream = null;
    	JSONObject response= new JSONObject();
    	StringEntity se;
    	
    	//instantiates httpclient to make request
		httpClient = new DefaultHttpClient();
		
		//url with the post data
		httpPost = new HttpPost(url);

		
		try {
			//passes the post data to a string builder/entity
			se = new StringEntity(postdata);
			
			//sets the post request as the resulting string
			httpPost.setEntity(se);
			
			//sets a request header so the page receving the request
		    //will know what to do with it
		    httpPost.addHeader("Content-Type", "application/json");
		    	
		    // Get the server response
		    HttpResponse httpResponse = httpClient.execute(httpPost);
		    inputStream = httpResponse.getEntity().getContent();
		    	
		    // convert inputstream to JSONObject
		     if(inputStream != null){
		            Log.d("RequestServer","inputStream non null");
		            responseJSON = convertInputStreamToJSONObject(inputStream);
		           
		     }	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
					
	            Log.d("RequestServer","envoi de la reponse");
	            Log.d("RequestServerResponse",response.toString());
	            try {
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return responseJSON;
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
	
	public String getUrlServer() {
		return urlServer;
	}

	public void setUrlServer(String urlServer) {
		this.urlServer = urlServer;
	}
	
}

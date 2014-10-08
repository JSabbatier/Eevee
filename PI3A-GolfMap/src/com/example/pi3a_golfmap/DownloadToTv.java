package com.example.pi3a_golfmap;

import android.util.Log;

public class DownloadToTv implements RequestInterface {
	
	@Override
	public void process(String rawJson) {
		String tv;
		tv = rawJson;
		Log.d("ResultPost",tv);
	}

}

package com.mimostudios.utilities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class Share {
	
	private final Context context;
	
	public Share(Context context) {
		this.context = context;
	}
	
	public void shareScore(String title, String message) {
		Intent share = new Intent(Intent.ACTION_SEND);
		share.setType("text/plain");
		share.putExtra(Intent.EXTRA_TEXT, message);
		try {
			Intent finalIntent = Intent.createChooser(share, title); 
			context.startActivity(finalIntent); 
		} catch (Exception e) {
			System.out.println("error: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void openOtherApp() {
		String appPackageName = "com.mimostudios.drop";
		try {
		    this.context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
		} catch (android.content.ActivityNotFoundException anfe) {
			 this.context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
		}
	}	

}

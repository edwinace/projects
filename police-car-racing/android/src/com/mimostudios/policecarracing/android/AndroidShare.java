package com.mimostudios.policecarracing.android;

import com.mimostudios.policecarracing.ShareInterface;
import com.mimostudios.utilities.Share;

public class AndroidShare implements ShareInterface {
	
	private final Share share;
	
	public AndroidShare(Share share) {
		this.share = share;
	}
	
	@Override
	public void shareScore(String title, String message) {
		this.share.shareScore(title, message);
	}
	
	@Override
	public void openOtherApp() {
		this.share.openOtherApp();
	}	

}

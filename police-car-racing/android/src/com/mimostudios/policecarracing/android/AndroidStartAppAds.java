package com.mimostudios.policecarracing.android;

import com.mimostudios.policecarracing.StartAppAds;
import com.startapp.android.publish.StartAppAd;

public class AndroidStartAppAds implements StartAppAds {
	
	private StartAppAd startAppAd;
	
	public AndroidStartAppAds(StartAppAd startAppAd) {
		this.startAppAd = startAppAd; 
	}

	@Override
	public void showInterstitial() {
		startAppAd.showAd(); 
		//startAppAd.loadAd();
	}
}

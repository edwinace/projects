package com.mimostudios.policecarracing.android;

import android.os.Bundle;
import android.view.View;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;
import com.mimostudios.policecarracing.MyGame;
import com.mimostudios.utilities.Share;

public class AndroidLauncher extends AndroidApplication {
	
    private StartAppAd startAppAd = new StartAppAd(this);
    private Share share = new Share(this);
    private boolean firstTime = true;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        StartAppSDK.init(this, "109125082", "206775825", false);
        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useAccelerometer = false;
        cfg.useCompass = false; 
        
        initialize(new MyGame(new AndroidStartAppAds(startAppAd), new AndroidShare(share)), cfg);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        startAppAd.onResume();
    }
    
    @Override
    public void onBackPressed() {
    	if (firstTime) {
    		startAppAd.onBackPressed();
    		firstTime=false;
    	} else {
    		super.onBackPressed();
    	}
    }
    
}
package com.mimostudios.policecarracing.desktop;

import com.badlogic.gdx.Gdx;
import com.mimostudios.policecarracing.ShareInterface;

public class DesktopShare implements ShareInterface {
	
   public void shareScore(String title, String message) {
	      Gdx.app.log("DesktopShare", "title: " + title + "  message: " + message);
	   }


   public void openOtherApp() {
	      Gdx.app.log("DesktopShare","openOtherApp");
   }
}

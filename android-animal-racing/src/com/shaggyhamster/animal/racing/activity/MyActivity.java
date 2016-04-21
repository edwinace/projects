package com.shaggyhamster.animal.racing.activity;

import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.shaggyhamster.animal.racing.manager.ResourcesManager;
import com.shaggyhamster.animal.racing.manager.SceneManager;
import com.shaggyhamster.animal.racing.util.ConstantsUtil;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.view.RenderSurfaceView;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.debug.Debug;

import java.io.IOException;

public class MyActivity extends BaseGameActivity {

    private Camera camera;
    private AdView adView;

    @Override
    public EngineOptions onCreateEngineOptions() {
        camera = new BoundCamera(0, 0, ConstantsUtil.SCREEN_WIDTH, ConstantsUtil.SCREEN_HEIGHT);
        EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), camera);
        engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
        engineOptions.getAudioOptions().setNeedsMusic(true);
        engineOptions.getAudioOptions().setNeedsSound(true);
        engineOptions.getRenderOptions().setDithering(true);
        return engineOptions;
    }

    @Override
    public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws IOException {
        ResourcesManager.prepareManager(getEngine(), this, camera, getVertexBufferObjectManager());
        pOnCreateResourcesCallback.onCreateResourcesFinished();
    }

    @Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws IOException {
        SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);

    }

    @Override
    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException {
        getEngine().registerUpdateHandler(new TimerHandler(ConstantsUtil.SPLASH_SCREEN_TIME, new ITimerCallback() {
            @Override
            public void onTimePassed(TimerHandler pTimerHandler) {
                getEngine().unregisterUpdateHandler(pTimerHandler);
                SceneManager.getInstance().createMainMenuScene();
            }
        }));

        pOnPopulateSceneCallback.onPopulateSceneFinished();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
        }
        return false;
    }

    @Override
    protected void onSetContentView() {

        FrameLayout relativeLayout = new FrameLayout(this);

        this.mRenderSurfaceView = new RenderSurfaceView(this);
        mRenderSurfaceView.setRenderer(mEngine, this);

        try {
            adView = new AdView(this, AdSize.BANNER, ConstantsUtil.MY_AD_UNIT_ID);
            adView.setTag("adView");
            adView.refreshDrawableState();
            adView.setVisibility(AdView.INVISIBLE);
            adView.setVerticalGravity(Gravity.BOTTOM);


            // Initiate a generic request to load it with an ad
            AdRequest adRequest = new AdRequest();

            adRequest.addTestDevice(AdRequest.TEST_EMULATOR);
            adRequest.addTestDevice("2D91A564A65AF57C28A98B6EC9456D29");
            adView.loadAd(adRequest);

            AdView.LayoutParams adViewParams = new AdView.LayoutParams(
                    AdView.LayoutParams.MATCH_PARENT,
                    AdView.LayoutParams.MATCH_PARENT);
            //the next line is the key to putting it on the bottom
            adViewParams.addRule(AdView.ALIGN_PARENT_BOTTOM);


            relativeLayout.addView(this.mRenderSurfaceView);
            relativeLayout.addView(adView, adViewParams);
        } catch (Exception e) {
            Debug.e("ADS ARE NOT WORKING");
            //ads aren't working. oh well
        }
        this.setContentView(relativeLayout);

    }

    public AdView getAdView() {
        return adView;
    }
}

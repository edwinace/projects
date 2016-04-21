package com.shaggyhamster.animal.racing.manager;

import com.google.ads.AdView;
import com.shaggyhamster.animal.racing.activity.MyActivity;
import com.shaggyhamster.animal.racing.model.scene.*;
import com.shaggyhamster.animal.racing.util.ConstantsUtil;
import com.shaggyhamster.animal.racing.util.LevelDifficulty;
import com.shaggyhamster.animal.racing.util.MathParameter;
import com.shaggyhamster.animal.racing.util.SceneType;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.ui.IGameInterface;

/**
 * User: Breku
 * Date: 21.09.13
 */
public class SceneManager {

    private static final SceneManager INSTANCE = new SceneManager();

    private SceneType currentSceneType = SceneType.SPLASH;
    private BaseScene gameScene, menuScene, loadingScene, splashScene, currentScene,
            aboutScene, optionsScene, endGameScene, recordScene, gameTypeScene;

    public static SceneManager getInstance() {
        return INSTANCE;
    }

    public void setScene(BaseScene scene) {

        if (scene instanceof GameScene || scene instanceof GameTypeScene || scene instanceof HighScoreScene
                || scene instanceof AboutScene) {
            ResourcesManager.getInstance().getActivity().runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {
                            ((MyActivity) ResourcesManager.getInstance().getActivity()).getAdView().setVisibility(AdView.INVISIBLE);
                        }
                    }
            );
        } else {
            ResourcesManager.getInstance().getActivity().runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {
                            ((MyActivity) ResourcesManager.getInstance().getActivity()).getAdView().setVisibility(AdView.VISIBLE);
                        }
                    }
            );

        }

        ResourcesManager.getInstance().getEngine().setScene(scene);
        currentScene = scene;
        currentSceneType = scene.getSceneType();
    }

    public void createSplashScene(IGameInterface.OnCreateSceneCallback onCreateSceneCallback) {
        ResourcesManager.getInstance().loadSplashScreen();
        splashScene = new SplashScene();
        currentScene = splashScene;
        onCreateSceneCallback.onCreateSceneFinished(splashScene);
    }


    public void createMainMenuScene() {
        ResourcesManager.getInstance().loadMainMenuResources();
        ResourcesManager.getInstance().loadGameTypeResources();
        menuScene = new MainMenuScene();
        loadingScene = new LoadingScene();
        setScene(menuScene);
        disposeSplashScene();
    }


    public void loadMenuSceneFrom(SceneType sceneType) {
        setScene(loadingScene);
        switch (sceneType) {
            case ABOUT:
                aboutScene.disposeScene();
                ResourcesManager.getInstance().unloadAboutTextures();
                break;
            case GAME:
                gameScene.disposeScene();
                ResourcesManager.getInstance().unloadGameTextures();
                break;
            case OPTIONS:
                ResourcesManager.getInstance().unloadOptionsTextures();
                optionsScene.disposeScene();
                break;
            case ENDGAME:
                ResourcesManager.getInstance().unloadEndGameTextures();
                endGameScene.disposeScene();
                break;
            case RECORDS:
                ResourcesManager.getInstance().unloadRecordsTextures();
                recordScene.disposeScene();
                break;
            case GAMETYPE:
                ResourcesManager.getInstance().unloadGameTypeTextures();
                gameTypeScene.disposeScene();
                break;
        }
        ResourcesManager.getInstance().getEngine().registerUpdateHandler(new TimerHandler(ConstantsUtil.LOADING_SCENE_TIME, new ITimerCallback() {
            @Override
            public void onTimePassed(TimerHandler pTimerHandler) {
                ResourcesManager.getInstance().getEngine().unregisterUpdateHandler(pTimerHandler);
                ResourcesManager.getInstance().loadMenuTextures();
                ResourcesManager.getInstance().loadGameTypeResources();
                setScene(menuScene);
            }
        }));
    }

    public void loadGameTypeScene() {
        gameTypeScene = new GameTypeScene();
        setScene(gameTypeScene);
        ResourcesManager.getInstance().unloadMenuTextures();
    }

    public void loadGameScene() {
        setScene(loadingScene);
        ResourcesManager.getInstance().unloadGameTypeTextures();
        ResourcesManager.getInstance().getEngine().registerUpdateHandler(new TimerHandler(ConstantsUtil.LOADING_SCENE_TIME, new ITimerCallback() {
            @Override
            public void onTimePassed(TimerHandler pTimerHandler) {
                ResourcesManager.getInstance().getEngine().unregisterUpdateHandler(pTimerHandler);
                ResourcesManager.getInstance().loadGameResources();
                gameScene = new GameScene();
                setScene(gameScene);
            }
        }));
    }

    public void loadAboutScene() {
        setScene(loadingScene);
        ResourcesManager.getInstance().unloadMenuTextures();
        ResourcesManager.getInstance().getEngine().registerUpdateHandler(new TimerHandler(ConstantsUtil.LOADING_SCENE_TIME / 2, new ITimerCallback() {
            @Override
            public void onTimePassed(TimerHandler pTimerHandler) {
                ResourcesManager.getInstance().getEngine().unregisterUpdateHandler(pTimerHandler);
                ResourcesManager.getInstance().loadAboutResources();
                aboutScene = new AboutScene();
                setScene(aboutScene);
            }
        }));
    }

    public void loadHighScoreSceneFrom(SceneType sceneType, final Integer score, final LevelDifficulty levelDifficulty, final MathParameter mathParameter) {
        switch (sceneType) {
            case MENU:
                setScene(loadingScene);
                ResourcesManager.getInstance().unloadMenuTextures();
                ResourcesManager.getInstance().getEngine().registerUpdateHandler(new TimerHandler(ConstantsUtil.LOADING_SCENE_TIME / 2, new ITimerCallback() {
                    @Override
                    public void onTimePassed(TimerHandler pTimerHandler) {
                        ResourcesManager.getInstance().getEngine().unregisterUpdateHandler(pTimerHandler);
                        ResourcesManager.getInstance().loadRecordResources();
                        recordScene = new HighScoreScene();
                        setScene(recordScene);
                    }
                }));
                break;
            case GAME:
                setScene(loadingScene);
                gameScene.disposeScene();
                ResourcesManager.getInstance().unloadGameTextures();
                ResourcesManager.getInstance().getEngine().registerUpdateHandler(new TimerHandler(ConstantsUtil.LOADING_SCENE_TIME / 4, new ITimerCallback() {
                    @Override
                    public void onTimePassed(TimerHandler pTimerHandler) {
                        ResourcesManager.getInstance().getEngine().unregisterUpdateHandler(pTimerHandler);
                        ResourcesManager.getInstance().loadRecordResources();
                        recordScene = new HighScoreScene(score, levelDifficulty, mathParameter);
                        setScene(recordScene);
                    }
                }));
                break;
            case ENDGAME:
                ResourcesManager.getInstance().loadRecordResources();
                recordScene = new HighScoreScene();
                setScene(recordScene);
                break;
            default:
                throw new UnsupportedOperationException();
        }

    }

    public void loadEndGameScene(Integer score) {
        endGameScene = new EndGameScene(score);
        setScene(endGameScene);
        gameScene.disposeScene();
        ResourcesManager.getInstance().unloadGameTextures();
    }

    public void loadOptionsScene() {
        setScene(loadingScene);
        ResourcesManager.getInstance().unloadMenuTextures();
        ResourcesManager.getInstance().getEngine().registerUpdateHandler(new TimerHandler(ConstantsUtil.LOADING_SCENE_TIME / 2, new ITimerCallback() {
            @Override
            public void onTimePassed(TimerHandler pTimerHandler) {
                ResourcesManager.getInstance().getEngine().unregisterUpdateHandler(pTimerHandler);
                ResourcesManager.getInstance().loadOptionsResources();
                optionsScene = new OptionsScene();
                setScene(optionsScene);
            }
        }));
    }


    private void disposeSplashScene() {
        ResourcesManager.getInstance().unloadSplashScreen();
        splashScene.disposeScene();
        splashScene = null;
    }


    public BaseScene getCurrentScene() {
        return currentScene;
    }

}

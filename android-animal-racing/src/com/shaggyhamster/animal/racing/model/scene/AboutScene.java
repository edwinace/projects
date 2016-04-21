package com.shaggyhamster.animal.racing.model.scene;

import com.shaggyhamster.animal.racing.manager.SceneManager;
import com.shaggyhamster.animal.racing.util.ConstantsUtil;
import com.shaggyhamster.animal.racing.util.SceneType;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

/**
 * User: Breku
 * Date: 21.09.13
 */
public class AboutScene extends BaseScene implements IOnSceneTouchListener {

    @Override
    public void createScene(Object... objects) {
        createBackground();
        setOnSceneTouchListener(this);
    }

    private void createBackground() {
        attachChild(new Sprite(ConstantsUtil.SCREEN_WIDTH / 2, ConstantsUtil.SCREEN_HEIGHT / 2, resourcesManager.getAboutBackgroundTextureRegion(), vertexBufferObjectManager));
    }

    @Override
    public void onBackKeyPressed() {
        SceneManager.getInstance().loadMenuSceneFrom(SceneType.ABOUT);
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.ABOUT;
    }

    @Override
    public void disposeScene() {
    }

    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        if (pSceneTouchEvent.isActionUp()) {
            SceneManager.getInstance().loadMenuSceneFrom(SceneType.ABOUT);
        }
        return false;
    }
}

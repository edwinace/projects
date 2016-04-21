package com.shaggyhamster.animal.racing.model.scene;

import com.shaggyhamster.animal.racing.manager.SceneManager;
import com.shaggyhamster.animal.racing.service.HighScoreService;
import com.shaggyhamster.animal.racing.util.ConstantsUtil;
import com.shaggyhamster.animal.racing.util.SceneType;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;

/**
 * User: Breku
 * Date: 08.10.13
 */
public class GameTypeScene extends BaseScene implements MenuScene.IOnMenuItemClickListener {

    private MenuScene menuScene;
    private HighScoreService highScoreService;

    @Override
    public void createScene(Object... objects) {
        init();
        createBackground();
        createButtons();
    }

    private void createButtons() {
        menuScene = new MenuScene(camera);
        menuScene.setPosition(400, 240);
        menuScene.setBackgroundEnabled(false);
        menuScene.buildAnimations();

        menuScene.addMenuItem(new ScaleMenuItemDecorator(new SpriteMenuItem(1, resourcesManager.getPlayButtonTextureRegion(), vertexBufferObjectManager), 1.5f, 1.0f));

        menuScene.setOnMenuItemClickListener(this);

        setChildScene(menuScene);
    }

    private void init() {
        highScoreService = new HighScoreService();
    }

    private void createBackground() {
        attachChild(new Sprite(ConstantsUtil.SCREEN_WIDTH / 2, ConstantsUtil.SCREEN_HEIGHT / 2, resourcesManager.getBackgroundGameTypeTextureRegion(), vertexBufferObjectManager));
    }

    @Override
    public void onBackKeyPressed() {
        SceneManager.getInstance().loadMenuSceneFrom(SceneType.GAMETYPE);
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.GAMETYPE;
    }

    @Override
    public void disposeScene() {
    }

    @Override
    public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY) {
        SceneManager.getInstance().loadGameScene();
        return true;
    }
}

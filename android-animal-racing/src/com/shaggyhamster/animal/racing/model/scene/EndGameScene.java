package com.shaggyhamster.animal.racing.model.scene;

import com.shaggyhamster.animal.racing.manager.ResourcesManager;
import com.shaggyhamster.animal.racing.manager.SceneManager;
import com.shaggyhamster.animal.racing.util.ConstantsUtil;
import com.shaggyhamster.animal.racing.util.SceneType;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;

/**
 * User: Breku
 * Date: 04.10.13
 */
public class EndGameScene extends BaseScene implements IOnSceneTouchListener {

    /**
     * @param objects objects[0] - Integer score
     */
    public EndGameScene(Object... objects) {
        super(objects);
    }

    @Override
    public void createScene(Object... objects) {
        createBackground((Integer) objects[0]);
        setOnSceneTouchListener(this);
    }

    private void createBackground(Integer score) {
        attachChild(new Sprite(ConstantsUtil.SCREEN_WIDTH / 2, ConstantsUtil.SCREEN_HEIGHT / 2,
                ResourcesManager.getInstance().getEndGameBackgroundTextureRegion(), vertexBufferObjectManager));
        attachChild(new Text(400, 200, ResourcesManager.getInstance().getBlackFont(),
                score.toString(), vertexBufferObjectManager));
    }

    @Override
    public void onBackKeyPressed() {
        SceneManager.getInstance().loadMenuSceneFrom(SceneType.ENDGAME);
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.ENDGAME;
    }

    @Override
    public void disposeScene() {
    }

    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        if (pSceneTouchEvent.isActionUp()) {
            SceneManager.getInstance().loadMenuSceneFrom(SceneType.ENDGAME);
        }
        return false;
    }
}

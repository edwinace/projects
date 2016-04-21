package com.shaggyhamster.animal.racing.model.scene;

import com.badlogic.gdx.math.Vector2;
import com.shaggyhamster.animal.racing.loader.MainLevelLoader;
import com.shaggyhamster.animal.racing.loader.ShapeAndTextLoader;
import com.shaggyhamster.animal.racing.manager.SceneManager;
import com.shaggyhamster.animal.racing.model.shape.Animal;
import com.shaggyhamster.animal.racing.service.HighScoreService;
import com.shaggyhamster.animal.racing.util.AnimalType;
import com.shaggyhamster.animal.racing.util.ConstantsUtil;
import com.shaggyhamster.animal.racing.util.SceneType;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;
import org.andengine.util.level.EntityLoader;
import org.andengine.util.level.simple.SimpleLevelEntityLoaderData;
import org.andengine.util.level.simple.SimpleLevelLoader;

/**
 * User: Breku
 * Date: 21.09.13
 */
public class GameScene extends BaseScene implements IOnSceneTouchListener {

    private HUD gameHUD;

    private PhysicsWorld physicsWorld;

    private HighScoreService highScoreService;

    private SimpleLevelLoader levelLoader;
    private EntityLoader mainLevelLoader;
    private EntityLoader shapeAndTextLoader;

    // our flag
    private boolean isScreenTouched;

    private Animal player;

    public GameScene(Object... objects) {
        super(objects);
    }


    @Override
    public void createScene(Object... objects) {
        createHUD();
        init(objects);
        initPhysics();
        createBackground();
        createAnimal();


        loadLevel(1);
    }

    private void createAnimal() {
        player = new Animal(400, 240, AnimalType.HORSE, camera, physicsWorld);

        attachChild(player);
    }

    private void initPhysics() {
        physicsWorld = new PhysicsWorld(new Vector2(0, 0), false);
        registerUpdateHandler(physicsWorld);


    }


    @Override
    public void onBackKeyPressed() {
        SceneManager.getInstance().loadMenuSceneFrom(SceneType.GAME);
    }

    private void init(Object... objects) {
        clearUpdateHandlers();
        clearTouchAreas();

        highScoreService = new HighScoreService();

        isScreenTouched = false;

        levelLoader = new SimpleLevelLoader(vertexBufferObjectManager);
        mainLevelLoader = new MainLevelLoader<SimpleLevelEntityLoaderData>(this, camera, ConstantsUtil.TAG_LEVEL);
        shapeAndTextLoader = new ShapeAndTextLoader<SimpleLevelEntityLoaderData>(this, ConstantsUtil.TAG_ENTITY);

        setOnSceneTouchListener(this);

    }

    private void loadLevel(int levelID) {
        levelLoader.registerEntityLoader(mainLevelLoader);
        levelLoader.registerEntityLoader(shapeAndTextLoader);
        levelLoader.loadLevelFromAsset(activity.getAssets(), "lvl/" + levelID + ".lvl");
    }


    private void createHUD() {
        gameHUD = new HUD();
        camera.setHUD(gameHUD);
    }

    private void createBackground() {
        setBackground(new Background(Color.WHITE));
//        attachChild(new Sprite(ConstantsUtil.SCREEN_WIDTH / 2, ConstantsUtil.SCREEN_HEIGHT / 2,
//                ResourcesManager.getInstance().getBackgroundGameTextureRegion(), vertexBufferObjectManager));
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);

        updatePlayerPosition();


    }

    private void updatePlayerPosition() {
        if (isScreenTouched && !player.isRunning()) {
            player.run();
        }
        if (!isScreenTouched && player.isRunning()) {
            player.stopRunning();
        }
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.GAME;
    }

    @Override
    public void disposeScene() {
        gameHUD.clearChildScene();
        camera.setHUD(null);
        camera.setCenter(ConstantsUtil.SCREEN_WIDTH / 2, ConstantsUtil.SCREEN_HEIGHT / 2);
        camera.setChaseEntity(null);
    }

    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {

        if (pSceneTouchEvent.isActionDown()) {
            isScreenTouched = true;
        }
        if (pSceneTouchEvent.isActionUp()) {
            isScreenTouched = false;
        }
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}

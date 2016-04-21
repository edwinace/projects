package com.shaggyhamster.animal.racing.model.shape;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.shaggyhamster.animal.racing.manager.ResourcesManager;
import com.shaggyhamster.animal.racing.util.AnimalType;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.util.debug.Debug;

/**
 * User: Breku
 * Date: 06.12.13
 */
public class Animal extends AnimatedSprite {

    private AnimalType animalType;
    private Body body;
    private FixtureDef fixtureDef;

    private boolean running;

    public Animal(final float pX, final float pY, AnimalType animalType, Camera camera, PhysicsWorld physicsWorld) {
        super(pX, pY, ResourcesManager.getInstance().getAnimalTiledTextureRegionFor(animalType), ResourcesManager.getInstance().getVertexBufferObjectManager());
        this.animalType = animalType;
        this.fixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0);
        this.running = false;
        animate(animalType.getSpeed(), true);
        camera.setChaseEntity(this);
        createPhysics(camera, physicsWorld);

    }

    private void createPhysics(final Camera camera, PhysicsWorld physicsWorld) {
        body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyDef.BodyType.DynamicBody, fixtureDef);
        body.setUserData("player");
        body.setFixedRotation(true);

        physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false));

    }

    public void run() {
        running = true;
        body.setLinearVelocity(20, 0);
        Debug.e("AAA");
    }

    public void stopRunning() {
        running = false;
        body.setLinearVelocity(0, 0);
    }

    public FixtureDef getFixtureDef() {
        return fixtureDef;
    }

    public Body getBody() {
        return body;
    }

    public AnimalType getAnimalType() {
        return animalType;
    }

    public boolean isRunning() {
        return running;
    }
}

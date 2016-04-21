package com.shaggyhamster.animal.racing.loader;

import com.shaggyhamster.animal.racing.model.scene.BaseScene;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.util.SAXUtils;
import org.andengine.util.level.EntityLoader;
import org.andengine.util.level.IEntityLoaderData;
import org.andengine.util.level.constants.LevelConstants;
import org.xml.sax.Attributes;

import java.io.IOException;

/**
 * User: Breku
 * Date: 07.08.13
 */
public class MainLevelLoader<T extends IEntityLoaderData> extends EntityLoader<T> {

    private BaseScene scene;
    private Camera camera;

    public MainLevelLoader(BaseScene scene, Camera camera, String... pEntityNames) {
        super(pEntityNames);
        this.scene = scene;
        this.camera = camera;
    }

    @Override
    public IEntity onLoadEntity(String pEntityName, IEntity pParent, Attributes pAttributes, T pEntityLoaderData) throws IOException {
        final int width = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_WIDTH);
        final int height = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_HEIGHT);

        ((BoundCamera) camera).setBounds(0, 0, width, height);
        ((BoundCamera) camera).setBoundsEnabled(true);
        return scene;
    }
}

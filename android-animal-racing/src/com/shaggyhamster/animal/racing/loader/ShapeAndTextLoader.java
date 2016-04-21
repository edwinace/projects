package com.shaggyhamster.animal.racing.loader;


import com.shaggyhamster.animal.racing.manager.ResourcesManager;
import com.shaggyhamster.animal.racing.model.scene.BaseScene;
import com.shaggyhamster.animal.racing.util.ConstantsUtil;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.util.SAXUtils;
import org.andengine.util.adt.color.Color;
import org.andengine.util.level.EntityLoader;
import org.andengine.util.level.IEntityLoaderData;
import org.xml.sax.Attributes;

import java.io.IOException;

/**
 * User: Breku
 * Date: 07.08.13
 */
public class ShapeAndTextLoader<T extends IEntityLoaderData> extends EntityLoader<T> {

    private BaseScene scene;

    public ShapeAndTextLoader(BaseScene scene, String... pEntityNames) {
        super(pEntityNames);
        this.scene = scene;
    }

    @Override
    public IEntity onLoadEntity(String pEntityName, IEntity pParent, Attributes pAttributes, T pEntityLoaderData) throws IOException {
        final float x = SAXUtils.getFloatAttributeOrThrow(pAttributes, ConstantsUtil.TAG_ENTITY_ATTRIBUTE_X);
        final float y = SAXUtils.getFloatAttributeOrThrow(pAttributes, ConstantsUtil.TAG_ENTITY_ATTRIBUTE_Y);

        String type = SAXUtils.getAttributeOrThrow(pAttributes, ConstantsUtil.TAG_ENTITY_ATTRIBUTE_TYPE);

        final Entity levelObject;

        if (type.equals(ConstantsUtil.TAG_ENTITY_ATTRIBUTE_TYPE_SHAPE)) {
            levelObject = new Rectangle(x, y, 100, 100, ResourcesManager.getInstance().getVertexBufferObjectManager());
            levelObject.setColor(Color.BLACK);
            levelObject.setVisible(true);
        } else {
            throw new IllegalArgumentException();
        }
        return levelObject;
    }
}

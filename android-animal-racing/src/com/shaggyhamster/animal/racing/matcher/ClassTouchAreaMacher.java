package com.shaggyhamster.animal.racing.matcher;

import org.andengine.entity.scene.ITouchArea;

/**
 * User: Breku
 * Date: 29.09.13
 */
public class ClassTouchAreaMacher implements ITouchArea.ITouchAreaMatcher {

    private Class type;

    public ClassTouchAreaMacher(Class clazz) {
        this.type = clazz;
    }

    @Override
    public boolean matches(ITouchArea pItem) {
        if (type.isAssignableFrom(pItem.getClass())) {
            return true;
        }
        return false;
    }
}
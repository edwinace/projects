package com.shaggyhamster.animal.racing.matcher;

import org.andengine.entity.IEntity;
import org.andengine.entity.IEntityMatcher;

/**
 * User: Breku
 * Date: 26.09.13
 */
public class ClassIEntityMatcher implements IEntityMatcher {

    private Class type;

    public ClassIEntityMatcher(Class clazz) {
        this.type = clazz;
    }

    @Override
    public boolean matches(IEntity pEntity) {
        if (type.isAssignableFrom(pEntity.getClass())) {
            return true;
        }
        return false;
    }
}

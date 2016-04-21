package com.shaggyhamster.animal.racing.service;

import android.app.Activity;
import com.shaggyhamster.animal.racing.manager.ResourcesManager;
import org.andengine.engine.Engine;

/**
 * User: Breku
 * Date: 06.10.13
 */
public abstract class BaseService {

    protected Engine engine;
    protected Activity activity;
    protected ResourcesManager resourcesManager;

    public BaseService() {
        this.resourcesManager = ResourcesManager.getInstance();
        this.engine = resourcesManager.getEngine();
        this.activity = resourcesManager.getActivity();
    }
}

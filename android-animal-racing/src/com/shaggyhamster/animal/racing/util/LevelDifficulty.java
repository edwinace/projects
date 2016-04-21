package com.shaggyhamster.animal.racing.util;

/**
 * User: Breku
 * Date: 18.10.13
 */
public enum LevelDifficulty {
    EASY(20, false, -14.0f),
    MEDIUM(100, false, -16.0f),
    HARD(100, true, -18.0f);

    private Integer randomSeedSize;
    private Boolean minusAllowed;
    private float lifeBarSpeed;

    private LevelDifficulty(Integer randomSeedSize, Boolean minusAllowed, float lifeBarSpeed) {
        this.randomSeedSize = randomSeedSize;
        this.minusAllowed = minusAllowed;
        this.lifeBarSpeed = lifeBarSpeed;
    }

    public Integer getRandomSeedSize() {
        return randomSeedSize;
    }

    public Boolean isMinusAllowed() {
        return minusAllowed;
    }

    public float getLifeBarSpeed() {
        return lifeBarSpeed;
    }
}

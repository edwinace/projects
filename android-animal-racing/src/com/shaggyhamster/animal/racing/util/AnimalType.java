package com.shaggyhamster.animal.racing.util;

/**
 * User: Breku
 * Date: 06.12.13
 */
public enum AnimalType {
    HORSE(60),
    CAT(50);

    private Integer speed;

    private AnimalType(Integer speed) {
        this.speed = speed;
    }

    public Integer getSpeed() {
        return speed;
    }
}

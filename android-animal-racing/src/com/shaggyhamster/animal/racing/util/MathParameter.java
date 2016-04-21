package com.shaggyhamster.animal.racing.util;

/**
 * User: Breku
 * Date: 21.09.13
 */
public enum MathParameter {
    ADD(false, true, false, false, 1),
    SUB(false, false, true, false, 2),
    MUL(true, false, false, false, 3),
    DIV(false, false, false, true, 4),
    ALL(true, true, true, true, 5);


    private boolean multiplication;
    private boolean addition;
    private boolean subtraction;
    private boolean division;
    private int ID;

    private MathParameter(boolean multiplication, boolean addition, boolean subtraction, boolean division, int ID) {
        this.multiplication = multiplication;
        this.addition = addition;
        this.subtraction = subtraction;
        this.division = division;
        this.ID = ID;
    }

    public boolean isMultiplication() {
        return multiplication;
    }

    public boolean isAddition() {
        return addition;
    }

    public boolean isSubtraction() {
        return subtraction;
    }

    public boolean isDivision() {
        return division;
    }

    public int getID() {
        return ID;
    }
}

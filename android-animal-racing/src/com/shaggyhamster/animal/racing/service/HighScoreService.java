package com.shaggyhamster.animal.racing.service;

import com.shaggyhamster.animal.racing.util.LevelDifficulty;
import com.shaggyhamster.animal.racing.util.MathParameter;

/**
 * User: Breku
 * Date: 03.12.13
 */
public class HighScoreService extends BaseService {

    DatabaseHelper databaseHelper = new DatabaseHelper(activity);

    public Integer getHighScoresFor(LevelDifficulty levelDifficulty, MathParameter mathParameter) {
        return databaseHelper.getHighScoresFor(levelDifficulty, mathParameter);
    }

    public boolean isHighScore(LevelDifficulty levelDifficulty, MathParameter mathParameter, Integer currentScore) {
        Integer databaseScore = databaseHelper.getHighScoresFor(levelDifficulty, mathParameter);
        if (currentScore > databaseScore) {
            return true;
        }
        return false;
    }

    public void updateRecordFor(LevelDifficulty levelDifficulty, MathParameter mathParameter, Integer score) {
        databaseHelper.updateRecordFor(levelDifficulty, mathParameter, score);
    }

    public void unlockLevelUpFor(LevelDifficulty levelDifficulty, MathParameter mathParameter) {
        switch (levelDifficulty) {
            case EASY:
                switch (mathParameter) {
                    case ADD:
                        databaseHelper.unlockLevel(LevelDifficulty.EASY, MathParameter.SUB);
                        break;
                    case SUB:
                        databaseHelper.unlockLevel(LevelDifficulty.EASY, MathParameter.MUL);
                        break;
                    case MUL:
                        databaseHelper.unlockLevel(LevelDifficulty.EASY, MathParameter.DIV);
                        break;
                    case DIV:
                        databaseHelper.unlockLevel(LevelDifficulty.EASY, MathParameter.ALL);
                        break;
                    case ALL:
                        databaseHelper.unlockLevel(LevelDifficulty.MEDIUM, MathParameter.ADD);
                        break;
                }
                break;
            case MEDIUM:
                switch (mathParameter) {
                    case ADD:
                        databaseHelper.unlockLevel(LevelDifficulty.MEDIUM, MathParameter.SUB);
                        break;
                    case SUB:
                        databaseHelper.unlockLevel(LevelDifficulty.MEDIUM, MathParameter.MUL);
                        break;
                    case MUL:
                        databaseHelper.unlockLevel(LevelDifficulty.MEDIUM, MathParameter.DIV);
                        break;
                    case DIV:
                        databaseHelper.unlockLevel(LevelDifficulty.MEDIUM, MathParameter.ALL);
                        break;
                    case ALL:
                        databaseHelper.unlockLevel(LevelDifficulty.HARD, MathParameter.ADD);
                        break;
                }
                break;
            case HARD:
                switch (mathParameter) {
                    case ADD:
                        databaseHelper.unlockLevel(LevelDifficulty.HARD, MathParameter.SUB);
                        break;
                    case SUB:
                        databaseHelper.unlockLevel(LevelDifficulty.HARD, MathParameter.MUL);
                        break;
                    case MUL:
                        databaseHelper.unlockLevel(LevelDifficulty.HARD, MathParameter.DIV);
                        break;
                    case DIV:
                        databaseHelper.unlockLevel(LevelDifficulty.HARD, MathParameter.ALL);
                        break;
                }
                break;
        }

    }


    public boolean isLevelUnlocked(LevelDifficulty levelDifficulty, MathParameter mathParameter) {
        return databaseHelper.isLevelUnlocked(levelDifficulty, mathParameter);
    }
}

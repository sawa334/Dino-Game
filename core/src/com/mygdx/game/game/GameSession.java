package com.mygdx.game.game;

import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.managers.MemoryManager;

import java.util.ArrayList;


public class GameSession {

    long nextTrashSpawnTime;

    public GameState state;
    long nextEnemySpawnTime;
    long sessionStartTime;
    long pauseStartTime;
    MyGdxGame myGdxGame;
    GameSession gameSession;

    private int score;
    int destructedTrashNumber;

    public GameSession() {
    }

    public void startGame() {
        state = GameState.PLAYING;
        score = 0;
        destructedTrashNumber = 0;
        sessionStartTime = TimeUtils.millis();
        nextEnemySpawnTime = sessionStartTime + (long) (GameSettings.STARTING_ENEMY_APPEARANCE_COOL_DOWN
                * getEnemyTrashCoolDown());
    }




    private float getEnemyTrashCoolDown() {
        return (float) Math.exp(-0.001 * (TimeUtils.millis() - sessionStartTime + 1) / 1000);
    }

    public void pauseGame() {
        state = GameState.PAUSED;
        pauseStartTime = TimeUtils.millis();
    }

    public void resumeGame() {
        state = GameState.PLAYING;
        sessionStartTime += TimeUtils.millis() - pauseStartTime;
    }

    public boolean shouldSpawnTrash () {
        if (nextTrashSpawnTime <= TimeUtils.millis()) {
            nextTrashSpawnTime = TimeUtils.millis() + (long) (GameSettings.STARTING_TRASH_APPEARANCE_COOL_DOWN
                    * getTrashPeriodCoolDown() + (long) (Math.random() * 2000));
            return true;
        }
        return false;
    }

    private float getTrashPeriodCoolDown () {
        return (float) Math.exp(-0.001 * (TimeUtils.millis() - sessionStartTime) / 1000);
    }
    public void destructionRegistration() {
        destructedTrashNumber += 1;
    }
    public void updateScore() {
        score = (int) (TimeUtils.millis() - sessionStartTime) / 100 + destructedTrashNumber * 100;
    }
    public int getScore() {
        return score;
    }

    public void endGame() {
        updateScore();
        state = GameState.ENDED;
        ArrayList<Integer> recordsTable = MemoryManager.loadRecordsTable();
        if (recordsTable == null) {
            recordsTable = new ArrayList<>();
        }
        int foundIdx = 0;
        for (; foundIdx < recordsTable.size(); foundIdx++) {
            if (recordsTable.get(foundIdx) < getScore()) break;
        }
        recordsTable.add(foundIdx, getScore());
        MemoryManager.saveTableOfRecords(recordsTable);
    }

}
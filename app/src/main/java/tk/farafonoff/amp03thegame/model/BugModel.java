package tk.farafonoff.amp03thegame.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import tk.farafonoff.amp03thegame.BugType;

/**
 * Created by beaver on 28.01.2016.
 */
public class BugModel {

    private final int ADD_PERIOD_INITIAL = 5000;
    int MAX_BUGS = 100;
    float speedMultiplier = 20;
    long addTimeAccumulator = 0;
    Random random = new Random();
    private List<BugState> bugs = new ArrayList<>();
    private int screenWidth;
    private int screenHeight;
    private int score = 0;
    private int addPeriod;

    public BugModel(int width, int height, int count) {
        this.setScreenWidth(width);
        this.setScreenHeight(height);
        speedMultiplier = Math.min(width, height) / 3.0f;
        for (int i = 0; i < count; ++i) {
            add();
        }
        setAddPeriod(getADD_PERIOD_INITIAL());
    }

    public void add() {
        BugState state = new BugState();
        state.setX(random.nextInt(getScreenWidth()));
        state.setY(random.nextInt(getScreenHeight()));
        state.setVx(random.nextFloat() * speedMultiplier - speedMultiplier / 2);
        state.setVy(random.nextFloat() * speedMultiplier - speedMultiplier / 2);
        state.setType(random.nextInt(BugType.BUGS.length));
        getBugs().add(state);
    }

    public void update(long elapsedTime) {
        float fraction = ((float) elapsedTime) / 1000.0f;
        for (BugState state : getBugs()) {
            if (!state.isDead()) {
                advance(state, fraction);
                clamp(state, getScreenWidth(), getScreenHeight());
            }
        }
        addTimeAccumulator += elapsedTime;
        if (addTimeAccumulator > getAddPeriod()) {
            addTimeAccumulator = 0;
            add();
        }
    }

    private void advance(BugState state, float fraction) {
        state.setX(state.getX() + fraction * state.getVx());
        state.setY(state.getY() + fraction * state.getVy());
    }

    private void clamp(BugState state, int width, int height) {
        if (state.getX() < 0) {
            state.setX(0);
            state.setVx(-state.getVx());
        }
        if (state.getY() < 0) {
            state.setY(0);
            state.setVy(-state.getVy());
        }
        if (state.getX() >= getScreenWidth()) {
            state.setX(getScreenWidth());
            state.setVx(-state.getVx());
        }
        if (state.getY() >= getScreenHeight()) {
            state.setY(getScreenHeight());
            state.setVy(-state.getVy());
        }

    }

    public List<BugState> getBugs() {
        return bugs;
    }

    public void setBugs(List<BugState> bugs) {
        this.bugs = bugs;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getADD_PERIOD_INITIAL() {
        return ADD_PERIOD_INITIAL;
    }

    public int getAddPeriod() {
        return addPeriod;
    }

    public void setAddPeriod(int addPeriod) {
        this.addPeriod = addPeriod;
    }
}

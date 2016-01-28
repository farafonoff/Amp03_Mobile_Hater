package tk.farafonoff.amp03thegame.model;

/**
 * Created by beaver on 28.01.2016.
 */
public class BugState {
    private float x;
    private float y;
    private float vx;
    private float vy;
    private boolean dead;
    private long deathTime;
    private int type;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getVx() {
        return vx;
    }

    public void setVx(float vx) {
        this.vx = vx;
    }

    public float getVy() {
        return vy;
    }

    public void setVy(float vy) {
        this.vy = vy;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public long getDeathTime() {
        return deathTime;
    }

    public void setDeathTime(long deathTime) {
        this.deathTime = deathTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

package tk.farafonoff.amp03thegame;

/**
 * Created by beaver on 28.01.2016.
 */
public class BugType {
    public static BugType[] BUGS = new BugType[]{
            new BugType(0, R.mipmap.ic_launcher, R.mipmap.ic_dead),
            new BugType(0, R.mipmap.ic_win, R.mipmap.ic_win_dead),
            new BugType(0, R.mipmap.ic_apple, R.mipmap.ic_apple_dead)

    };

    private int res_alive;
    private int res_dead;
    private int name_res;

    public BugType(int name, int alive, int dead) {
        this.setName_res(name);
        this.setRes_alive(alive);
        this.setRes_dead(dead);
    }


    public int getRes_alive() {
        return res_alive;
    }

    public void setRes_alive(int res_alive) {
        this.res_alive = res_alive;
    }

    public int getRes_dead() {
        return res_dead;
    }

    public void setRes_dead(int res_dead) {
        this.res_dead = res_dead;
    }

    public int getName_res() {
        return name_res;
    }

    public void setName_res(int name_res) {
        this.name_res = name_res;
    }
}

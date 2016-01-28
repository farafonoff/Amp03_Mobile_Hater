package tk.farafonoff.amp03thegame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.LruCache;
import android.view.SurfaceHolder;

import java.util.ListIterator;

import tk.farafonoff.amp03thegame.model.BugModel;
import tk.farafonoff.amp03thegame.model.BugState;

/**
 * Created by beaver on 28.01.2016.
 */
public class BugDrawer implements Runnable {
    final int FPS_CALC_RATE = 2;
    final int DEAD_STAY_TIME = 700;
    BugModel model;
    SurfaceHolder surface;
    long lastTick;
    long fpsCounterStartTick;
    int fps;
    int fpsCounter;
    Resources resources;
    LruCache<Integer, Bitmap> cache = new LruCache<Integer, Bitmap>(10);
    KillRequest killRequest = null;
    private volatile boolean shouldRun = true;

    public BugDrawer(BugModel model, SurfaceHolder target, Resources resources) {
        this.model = model;
        this.surface = target;
        this.resources = resources;
    }

    public void stop() {
        shouldRun = false;
    }

    public int getFps() {
        return fps;
    }

    @Override
    public void run() {
        lastTick = System.currentTimeMillis();
        fpsCounterStartTick = lastTick;
        while (shouldRun) {
            long tick = System.currentTimeMillis();
            long diff = tick - lastTick;
            ++fpsCounter;
            if (tick - fpsCounterStartTick > 1000 / FPS_CALC_RATE) {
                fps = fpsCounter;
                fpsCounter = 0;
                fpsCounterStartTick = tick;
            }
            model.update(diff);
            lastTick = tick;
            Canvas canvas = surface.lockCanvas();
            if (canvas == null) {
                continue;
            }
            canvas.drawColor(Color.BLACK);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
            KillRequest localCopy = null;
            if (killRequest != null) {
                synchronized (this) {
                    localCopy = killRequest;
                    killRequest = null;
                }
            }
            BugState killedBug = null;
            for (ListIterator<BugState> iterator = model.getBugs().listIterator(); iterator.hasNext(); ) {
                BugState bug = iterator.next();
                Bitmap bmp = getBitmap(resources, bug.getType(), bug.isDead());
                Matrix m = new Matrix();
                double rotation = Math.atan2(bug.getVy(), bug.getVx());
                double degrees = Math.toDegrees(rotation) + 90.0f;
                m.postTranslate(-bmp.getWidth() / 2, -bmp.getHeight() / 2);
                m.postRotate((float) degrees);
                m.postTranslate(bug.getX(), bug.getY());
                if (!bug.isDead() && localCopy != null) {
                    Matrix m_inv = new Matrix();
                    m.invert(m_inv);
                    float[] killPos = new float[]{localCopy.x, localCopy.y};
                    m_inv.mapPoints(killPos);
                    if (Math.abs(killPos[0]) < bmp.getWidth() && Math.abs(killPos[1]) < bmp.getHeight()) {
                        killedBug = bug;
                    }
                }
                if (bug.isDead()) {
                    if (tick - bug.getDeathTime() > DEAD_STAY_TIME) {
                        iterator.remove();
                    }
                }
                canvas.drawBitmap(bmp, m, paint);
            }
            if (killedBug != null) {
                killedBug.setDead(true);
                killedBug.setDeathTime(tick);
            }
            String sampleText = "FPS: 999";
            String fpsText = String.format("FPS: %d", fps * FPS_CALC_RATE);
            Rect bounds = new Rect();
            paint.setColor(Color.WHITE);
            paint.getTextBounds(sampleText, 0, sampleText.length(), bounds);
            canvas.drawText(fpsText, model.getScreenWidth() - bounds.width(), model.getScreenHeight() - bounds.height(), paint);
            surface.unlockCanvasAndPost(canvas);
        }
    }

    private Bitmap getBitmap(Resources rez, int type, boolean dead) {
        BugType bug = BugType.BUGS[type];
        int res_id;
        if (dead) {
            res_id = bug.getRes_dead();
        } else {
            res_id = bug.getRes_alive();
        }
        Bitmap bmp = cache.get(res_id);
        if (bmp == null) {
            bmp = BitmapFactory.decodeResource(rez, res_id);
            cache.put(res_id, bmp);
        }
        return bmp;
    }

    public void kill(float x, float y) {
        synchronized (this) {
            killRequest = new KillRequest();
            killRequest.x = x;
            killRequest.y = y;
        }
    }

    static class KillRequest {
        float x;
        float y;
    }
}

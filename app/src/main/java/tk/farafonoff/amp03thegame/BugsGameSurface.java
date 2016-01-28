package tk.farafonoff.amp03thegame;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import tk.farafonoff.amp03thegame.model.BugModel;

/**
 * Created by beaver on 28.01.2016.
 */
public class BugsGameSurface extends SurfaceView implements SurfaceHolder.Callback {

    ObjectAnimator gameSpeedup;
    BugDrawer drawer;
    BugModel model;
    Thread drawerThread;
    SparseArray<EventPosAndTime> initialPositions = new SparseArray<>();

    public BugsGameSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        gameSpeedup = (ObjectAnimator) AnimatorInflater.loadAnimator(getContext(), R.animator.generate_speed);
    }

    void createSurface(int bugs) {
        model = new BugModel(getWidth(), getHeight(), bugs);
        drawer = new BugDrawer(model, getHolder(), getContext().getResources());
        drawerThread = new Thread(drawer);
        drawerThread.start();
        gameSpeedup.setTarget(model);
        gameSpeedup.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        createSurface(5);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        drawer.stop();
        createSurface(model.getBugs().size());
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        drawer.stop();
        gameSpeedup.cancel();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN || event.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN) {
            int pointer = event.getActionIndex();
            EventPosAndTime ep = new EventPosAndTime();
            ep.x = event.getX(pointer);
            ep.y = event.getY(pointer);
            ep.time = event.getEventTime();
            initialPositions.put(event.getPointerId(pointer), ep);
        }
        if (event.getActionMasked() == MotionEvent.ACTION_UP || event.getActionMasked() == MotionEvent.ACTION_POINTER_UP) {
            int pointer = event.getActionIndex();
            EventPosAndTime ep = initialPositions.get(event.getPointerId(pointer));
            if (ep != null) {
                long time = event.getEventTime();
                if (time - ep.time < 1000) {//short touch
                    drawer.kill(ep.x, ep.y);
                }
            }
        }
        return super.onTouchEvent(event);
    }

    static class EventPosAndTime {
        float x;
        float y;
        long time;
    }
}

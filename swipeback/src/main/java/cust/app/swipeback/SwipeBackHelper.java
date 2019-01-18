package cust.app.swipeback;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

public class SwipeBackHelper implements SwipeBack {

    private SwipeBackLayout swipeBackLayout;
    private Activity activity;

    public SwipeBackHelper(Activity activity) {
        this.activity = activity;
        swipeBackLayout = new SwipeBackLayout(activity);
    }

    public void setContentView(int layoutResID) {
        activity.setContentView(swipeBackLayout);
        View view = LayoutInflater.from(activity).inflate(layoutResID, null);
        swipeBackLayout.addView(view);
    }

    @Override
    public void enableSwipeBack(boolean enableSwipe) {
        swipeBackLayout.enableSwipeBack(enableSwipe);
    }

    /**
     * @param trackingEdge
     */
    @Override
    public void setTrackingEdge(boolean trackingEdge) {
        swipeBackLayout.setSwipeFromEdge(trackingEdge);
    }

    /**
     * {@link SwipeBackLayout#FROM_LEFT}
     * {@link SwipeBackLayout#FROM_TOP}
     * {@link SwipeBackLayout#FROM_RIGHT}
     * {@link SwipeBackLayout#FROM_BOTTOM}
     *
     * @param direction
     */
    @Override
    public void setTrackingDirection(int direction) {
        swipeBackLayout.setDirectionMode(direction);
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return swipeBackLayout;
    }

    @Override
    public void setOnSwipeBackListener(SwipeBackLayout.OnSwipeBackListener listener) {
        swipeBackLayout.setOnSwipeBackListener(listener);
    }

}

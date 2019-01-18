package cust.app.swipeback;

public interface SwipeBack {
    void enableSwipeBack(boolean enableSwipe);

    void setTrackingEdge(boolean edgeTracking);

    void setTrackingDirection(int dragMode);

    SwipeBackLayout getSwipeBackLayout();

    void setOnSwipeBackListener(SwipeBackLayout.OnSwipeBackListener listener);
}
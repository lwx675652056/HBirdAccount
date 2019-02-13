package com.hbird.widget.swipe_recyclerView;

public class SwipeMenuBridge {

    private final Controller mController;
    private final int mDirection;
    private final int mPosition;

    public SwipeMenuBridge(Controller controller, int direction, int position) {
        this.mController = controller;
        this.mDirection = direction;
        this.mPosition = position;
    }

    @SwipeRecyclerView.DirectionMode
    public int getDirection() {
        return mDirection;
    }

    public int getPosition() {
        return mPosition;
    }

    public void closeMenu() {
        mController.smoothCloseMenu();
    }
}
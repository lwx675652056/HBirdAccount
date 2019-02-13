package com.hbird.widget.swipe_recyclerView;

public interface OnItemMenuClickListener {

    /**
     * Invoke when the menu item is clicked.
     *
     * @param menuBridge menu.
     */
    void onItemClick(SwipeMenuBridge menuBridge, int position);
}
package sing.refreshlayout.listener;

import android.support.annotation.NonNull;

import sing.refreshlayout.api.RefreshLayout;

/**
 * 加载更多监听器
 */

public interface OnLoadMoreListener {
    void onLoadMore(@NonNull RefreshLayout refreshLayout);
}

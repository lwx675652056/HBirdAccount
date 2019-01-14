package sing.refreshlayout.listener;

import android.support.annotation.NonNull;

import sing.refreshlayout.api.RefreshLayout;

/**
 * 刷新监听器
 */
public interface OnRefreshListener {
    void onRefresh(@NonNull RefreshLayout refreshLayout);
}

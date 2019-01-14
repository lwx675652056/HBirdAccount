package sing.refreshlayout.impl;

import android.annotation.SuppressLint;
import android.view.View;

import sing.refreshlayout.api.RefreshFooter;
import sing.refreshlayout.internal.InternalAbstract;

/**
 * 刷新底部包装
 */
@SuppressLint("ViewConstructor")
public class RefreshFooterWrapper extends InternalAbstract implements RefreshFooter/*, InvocationHandler */{

    public RefreshFooterWrapper(View wrapper) {
        super(wrapper);
    }

    @Override
    public boolean setNoMoreData(boolean noMoreData) {
        return mWrapperView instanceof RefreshFooter && ((RefreshFooter) mWrapperView).setNoMoreData(noMoreData);
    }
}

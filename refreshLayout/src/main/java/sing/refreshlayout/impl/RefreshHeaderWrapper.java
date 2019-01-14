package sing.refreshlayout.impl;

import android.annotation.SuppressLint;
import android.view.View;

import sing.refreshlayout.api.RefreshHeader;
import sing.refreshlayout.internal.InternalAbstract;


/**
 * 刷新头部包装
 */
@SuppressLint("ViewConstructor")
public class RefreshHeaderWrapper extends InternalAbstract implements RefreshHeader/*, InvocationHandler*/ {

    public RefreshHeaderWrapper(View wrapper) {
        super(wrapper);
    }
}

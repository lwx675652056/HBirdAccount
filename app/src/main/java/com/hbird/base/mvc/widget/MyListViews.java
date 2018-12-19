/**
 * @(#)MyListView.java Sep 23, 2015
 *
 * Copyright (c) 2010 by vcread.com. All rights reserved.
 * 
 */
package com.hbird.base.mvc.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 解决 ListView 和 滑动 UI 冲突
 * Create By Liul On 2018/06/30
 */
public class MyListViews extends ListView {
	public MyListViews(Context context) {
		super(context);
	}

	public MyListViews(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyListViews(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}

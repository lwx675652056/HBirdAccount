package com.hbird.base.mvc.widget.waterwave_progress;

import android.content.Context;
/**
 * Created by Liul on 2018/8/07.
 */
public class WidgetUtil {
	public static int Dp2Px(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	public static int Px2Dp(Context context, float px) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}
}

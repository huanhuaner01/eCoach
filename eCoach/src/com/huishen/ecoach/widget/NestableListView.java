package com.huishen.ecoach.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 重写了 {@link #onMeasure(int, int)}方法，用于解决嵌套ListView时显示不全的问题。
 * @author Muyangmin
 * @create 2015-2-26
 */
public class NestableListView extends ListView {

	public NestableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}

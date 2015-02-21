package com.huishen.ecoach.util;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

/**
 * 放置UI相关的快捷方法。
 * 
 * @author Muyangmin
 * @create 2015-2-14
 */
public final class Uis {

	/**
	 * 发送Toast消息。
	 * 
	 * @param context
	 *            上下文信息。
	 * @param strid
	 *            要进行提示的文本 Id。
	 */
	public static final void toastShort(Context context, int strid) {
		toastShort(context, context.getResources().getString(strid));
	}

	/**
	 * 发送Toast消息。
	 * 
	 * @param context
	 *            上下文信息。
	 * @param msg
	 *            要进行提示的文本内容。
	 */
	public static final void toastShort(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 设置一个组件为Visible，若干个组件为Invisible。
	 */
	public static final void setVisibleInvisible(View toBeVisible,
			View... toBeInvisible) {
		if (toBeVisible != null) {
			toBeVisible.setVisibility(View.VISIBLE);
		}
		if (toBeInvisible != null) {
			for (View v : toBeInvisible) {
				v.setVisibility(View.INVISIBLE);
			}
		}
	}

	/**
	 * 设置一个组件为Visible，若干个组件为Gone。
	 */
	public static final void setVisibleGone(View toBeVisible, View... toBeGone) {
		if (toBeVisible != null) {
			toBeVisible.setVisibility(View.VISIBLE);
		}
		if (toBeGone != null) {
			for (View v : toBeGone) {
				v.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * 设置若干个组件为相同的Visibility状态。
	 * 
	 * @param visibility
	 *            取值应为 {@link View#VISIBLE}, {@link View#INVISIBLE},
	 *            {@link View#GONE}中的一个。
	 * @param views
	 *            要设置的组件
	 */
	public static final void setVisible(int visibility, View... views) {
		if (views != null) {
			for (View v : views) {
				v.setVisibility(visibility);
			}
		}
	}
}

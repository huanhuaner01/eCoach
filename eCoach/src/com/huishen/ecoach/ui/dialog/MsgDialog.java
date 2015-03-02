package com.huishen.ecoach.ui.dialog;

import com.huishen.ecoach.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * <p>
 * 实现对话框的封装。该类提供了两个构造器，调用者可以使用默认对话框或自定义样式的对话框。
 * </p>
 * <h1>Tips</h1>
 * <p>
 * 如果使用默认的对话框，则可以使用快捷方法 {@link #showDialog(Context, int)}和
 * {@link #showDialog(Context, int, int)} ，以避免因忘记调用 {@link #show()}
 * 方法造成的bug。但当需要自定义一些处理（例如指定和默认样式不同的图片）时，则需要通过重写该类的方式实现。
 * </p>
 * 
 * @author Muyangmin
 * @create 2015-3-2
 */
public class MsgDialog {

	public static final int TYPE_WARNING = 0;
	public static final int TYPE_MESSAGE = 1;
	public static final int TYPE_ERROR = 2;

	private AlertDialog.Builder builder;
	private final Context context;
	private final int msgType; // 要显示的类型
	private final int msgid; // 要显示的文字
	private final View mSpecifiedView;// 指定的View
	/**
	 * 显示默认样式的对话框，消息类型将被设置为{@link #TYPE_WARNING}。
	 * 
	 * @param context
	 *            上下文信息。
	 * @param msgid
	 *            要显示的文字。
	 */
	public static final void showDialog(Context context, int msgid){
		showDialog(context, TYPE_WARNING);
	}
	/**
	 * 显示默认样式的对话框，消息类型必须为 {@link #TYPE_ERROR}, {@link #TYPE_MESSAGE},
	 * {@link #TYPE_WARNING}中的一个。
	 * 
	 * @param context
	 *            上下文信息。
	 * @param msgid
	 *            要显示的文字。
	 * @param type
	 *            消息类型
	 */
	public static final void showDialog(Context context, int msgid, int type){
		new MsgDialog(context, msgid, type).show();
	}

	/**
	 * 构造默认样式的对话框，消息类型必须为 {@link #TYPE_ERROR}, {@link #TYPE_MESSAGE},
	 * {@link #TYPE_WARNING}中的一个。
	 * 
	 * @param context
	 *            上下文信息。
	 * @param msgid
	 *            要显示的文字。
	 * @param type
	 *            消息类型
	 */
	public MsgDialog(Context context, int msgid, int type) {
		if (type < TYPE_WARNING || type > TYPE_ERROR) {
			throw new IllegalArgumentException("wrong dialog type.");
		}
		this.context = context;
		builder = new Builder(context);
		this.msgid = msgid;
		this.msgType = type;
		mSpecifiedView = null;
	}

	/**
	 * 显示自定义样式的对话框。
	 * 
	 * @param context
	 *            上下文信息。
	 * @param view
	 *            要显示的ContextView
	 */
	public MsgDialog(Context context, View view) {
		this.context = context;
		builder = new Builder(context);
		this.msgid = -1;
		this.msgType = -1;
		this.mSpecifiedView = view;

	}

	public final void show() {
		if (mSpecifiedView != null) {
			builder.setCancelable(isCancelable()).setView(mSpecifiedView)
					.create().show();
		}
		View view = LayoutInflater.from(context).inflate(
				R.layout.layout_dialog_msgonly, null);
		ImageView img = (ImageView) view.findViewById(R.id.dialog_msg_img);
		TextView tv = (TextView) view.findViewById(R.id.dialog_msg_tv);
		Button btn = (Button) view.findViewById(R.id.dialog_msg_btn);
		tv.setText(msgid);
		img.setBackgroundResource(getImageResourceId(msgType));
		final AlertDialog dialog = builder.setCancelable(isCancelable())
				.setView(view).create();
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	/**
	 * 指定要显示的消息图片，默认按类型返回不同的图片资源。注意，该方法仅对默认样式有效。
	 */
	protected int getImagResourceId(){
		return getImageResourceId(msgType);
	}
	
	private final int getImageResourceId(int type) {
		switch (type) {
		case TYPE_WARNING:
			return R.drawable.icon_dialog_msgtype_warning;
		default:
			return R.drawable.icon_dialog_msgtype_warning;
		}
	}

	/**
	 * 指定对话框是否能够取消，默认返回false。
	 */
	protected boolean isCancelable() {
		return false;
	}
}

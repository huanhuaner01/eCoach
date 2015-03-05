package com.huishen.ecoach.ui.dialog;

import com.huishen.ecoach.R;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 展示一个对话框Fragment，显示图标、文字和仅一个按钮。
 * <p>
 * 实际使用时，请务必调用{@link #newMessageInstance(int)}, {@link #newWarningInstance(int)},
 *  {@link #newErrorInstance(int)}中的一个，而不是调用默认构造器。并且。 Activity必须实现
 * {@link OnButtonClickedListener}，否则将会抛出异常。
 * </p>
 * 
 * @author Muyangmin
 * @create 2015-3-5
 */
public final class MessageDialogFragment extends DialogFragment {

	private static final int TYPE_WARNING = 0;
	private static final int TYPE_MESSAGE = 1;
	private static final int TYPE_ERROR = 2;

	private static final String EXTRA_MSGTYPE = "msgtype";
	private static final String EXTRA_MSGID = "msgid";

	private OnButtonClickedListener clickedListener;// 按钮监听

	/**
	 * 普通消息对话框。
	 */
	public static final MessageDialogFragment newMessageInstance(int msgid) {
		return newInstance(TYPE_MESSAGE, msgid);
	}

	/**
	 * 警告消息对话框。
	 */
	public static final MessageDialogFragment newWarningInstance(int msgid) {
		return newInstance(TYPE_WARNING, msgid);
	}

	/**
	 * 错误消息对话框。
	 */
	public static final MessageDialogFragment newErrorInstance(int msgid) {
		return newInstance(TYPE_ERROR, msgid);
	}

	private static final MessageDialogFragment newInstance(int msgType,
			int msgid) {
		MessageDialogFragment frag = new MessageDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(EXTRA_MSGTYPE, msgType);
		bundle.putInt(EXTRA_MSGID, msgid);
		frag.setArguments(bundle);
		return frag;
	}

	private final int getImageResourceId(int type) {
		switch (type) {
		case TYPE_WARNING:
			return R.drawable.icon_dialog_msgtype_warning;
		default:
			return R.drawable.icon_dialog_msgtype_warning;
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			clickedListener = (OnButtonClickedListener) activity;
		} catch (ClassCastException e) {
			throw new RuntimeException(
					"Activity must implement OnButtonClickedListener!");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 去掉标题栏
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCancelable(false);
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.layout_dialog_msgonly, null);
		ImageView img = (ImageView) view.findViewById(R.id.dialog_msg_img);
		TextView tv = (TextView) view.findViewById(R.id.dialog_msg_tv);
		Button btn = (Button) view.findViewById(R.id.dialog_msg_btn);
		Bundle args = getArguments();
		if (args != null) {
			int msgid = args.getInt(EXTRA_MSGID);
			int msgType = args.getInt(EXTRA_MSGTYPE);
			tv.setText(msgid);
			img.setBackgroundResource(getImageResourceId(msgType));
			btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					clickedListener.onConfirmButtonClicked(MessageDialogFragment.this);
				}
			});
		}
		return view;
	}

	public interface OnButtonClickedListener {
		void onConfirmButtonClicked(DialogFragment fragment);
	}
}

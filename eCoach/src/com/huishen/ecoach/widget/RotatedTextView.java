package com.huishen.ecoach.widget;

import com.huishen.ecoach.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * <p>
 * 实现倾斜效果的TextView。由于在实现上是先直接进行画布的旋转再进行图像的绘制，
 * 因此使用时必须当心文字的padding和gravity属性的设置。
 * </p>
 * <h1>XML属性</h1> degrees： 代表旋转的方向角度。 <br/>
 * pivotx: X轴旋转中心<br/>
 * pivoty: Y轴旋转中心
 * 
 * @author Muyangmin
 * @create 2015-2-28
 */
public final class RotatedTextView extends TextView {

	private static final int DEFAULT_DEGREE = 315; // 逆时针45度
	private static final float DEFAULT_PIVOTX = 0.5F;
	private static final float DEFAULT_PIVOTY = 0.5F;

	private Context mContext;

	public RotatedTextView(Context context) {
		super(context);
		this.mContext = context;
	}

	public RotatedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		TypedArray attr = mContext
				.obtainStyledAttributes(R.styleable.rotatedtextview);
		final int degrees = attr.getInt(R.styleable.rotatedtextview_degrees,
				DEFAULT_DEGREE);
		final float pivotx = attr.getFloat(R.styleable.rotatedtextview_pivotx,
				DEFAULT_PIVOTX);
		final float pivoty = attr.getFloat(R.styleable.rotatedtextview_pivotx,
				DEFAULT_PIVOTY);
		attr.recycle();
		canvas.rotate(degrees, getMeasuredWidth() * pivotx, getMeasuredHeight()
				* pivoty);
		super.onDraw(canvas);
	}
}

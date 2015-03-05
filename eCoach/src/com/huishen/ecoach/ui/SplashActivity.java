package com.huishen.ecoach.ui;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.huishen.ecoach.Const;
import com.huishen.ecoach.R;
import com.huishen.ecoach.net.NetUtil;
import com.huishen.ecoach.net.ResponseListener;
import com.huishen.ecoach.net.SRL;
import com.huishen.ecoach.ui.login.LoginActivity;
import com.huishen.ecoach.util.Packages;
import com.huishen.ecoach.util.Prefs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SplashActivity extends Activity {
	
	private static final String LOG_TAG = "SplashActivity";
	
	private ViewPager viewPager;		//新手引导的pager
	private ImageView imageView;		//常规启动的Splash
	private ArrayList<View> dotList;	//新手引导的Pager下的小点
	private volatile boolean firstuse; 
	
	private SplashHandler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		handler = new SplashHandler(new WeakReference<SplashActivity>(this));
		viewPager = (ViewPager)findViewById(R.id.splash_viewpager);
		imageView = (ImageView)findViewById(R.id.splash_image);
		
		firstuse = checkFirstStart();
		Log.d(LOG_TAG, "check first use: " + firstuse);
		if (!firstuse){
			viewPager.setVisibility(View.INVISIBLE);
			findViewById(R.id.splash_ll_dots).setVisibility(View.INVISIBLE);
			handler.sendEmptyMessageDelayed(SplashHandler.MSG_UI_FINISHED,
					getResources().getInteger(R.integer.splash_min_displaytime));
		}
		else{
			initViewPager();
		}
//		checkSoftwareUpdate();
	}
	
	private void checkSoftwareUpdate(){
		NetUtil.requestStringData(SRL.Method.METHOD_CHECK_UPDATE, new ResponseListener() {
			
			@Override
			protected void onSuccess(String arg0) {
				try {
					JSONObject json = new JSONObject(arg0);
					int servercode = json.getInt(SRL.ReturnField.FIELD_UPDATE_SERVER_VERSIONCODE);
					int localcode = Packages.getVersioCode(SplashActivity.this);
					if (servercode <= localcode){
						Log.d(LOG_TAG, "No avaliable update found.");
						return;
					}
					final boolean forceUpdate = json.optBoolean(SRL.ReturnField.FIELD_UPDATE_FORCE_UPDATE);
					new AlertDialog.Builder(SplashActivity.this).setTitle(getString(R.string.str_checkupdate_update_avaliable))
					.setMessage(getString(R.string.str_checkupdate_update_description, json.optString(SRL.ReturnField.FIELD_UPDATE_SERVER_VERSIONNAME), json.optString(SRL.ReturnField.FIELD_UPDATE_SERVER_VERSIONDESC)))
					.setPositiveButton(R.string.str_checkupdate_update_now, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							performUpdate();
						}
					}).setPositiveButton(R.string.str_checkupdate_update_later, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (forceUpdate){
								finish();
							}
							else{
								//TODO finish logic
							}
						}
					}).create();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			protected void onReturnBadResult(int errorCode, String arg0) {
				
			}
		});
	}
	
	private void performUpdate(){
		
	}
	
	private static final class SplashHandler extends Handler{
		
		WeakReference<SplashActivity> wk;
		
		//使用该标记保证用户的强制更新
		private boolean noUpdateAvaliable = false;
		private boolean uiPerformFinished = false;
		
		SplashHandler(WeakReference<SplashActivity> wk) {
			this.wk = wk;
		}
		private static final int MSG_UI_FINISHED = 1;
		private static final int MSG_NO_UPDATE = 2;
		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			SplashActivity activity = wk.get();
			if (activity==null){
				Log.w(LOG_TAG, "reference is null.");
				return ;
			}
			switch (msg.what) {
			case MSG_UI_FINISHED:
//				uiPerformFinished = true;
//				if (noUpdateAvaliable){
					activity.startNextActivity();
//				}
				break;
			case MSG_NO_UPDATE:
				noUpdateAvaliable = true;
				if (uiPerformFinished){
					activity.startNextActivity();
				}
				break;
			default:
				break;
			}
		}
	}
	
	/**
	 * 启动下个Activity并关闭当前Activity。
	 */
	private final void startNextActivity(){
		startActivity(LoginActivity.getIntent(SplashActivity.this));
		finish();
	}
	
	private final void initViewPager(){
		ArrayList<View> images = new ArrayList<View>();
		ViewGroup.LayoutParams params = new LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		final int pagenum = getResources().getInteger(R.integer.spalsh_image_count);
		//使用TypedArray方式加载配置文件里的Splash图片
		TypedArray picid = getResources().obtainTypedArray(R.array.splash_image_ids);
		for (int i=0; i<pagenum; i++){
			ImageView img = new ImageView(this);
			img.setLayoutParams(params);
			img.setBackgroundResource(picid.getResourceId(i, -1));
			images.add(img);
		}
		picid.recycle();
		dotList = new ArrayList<View>();
		LinearLayout llDots = (LinearLayout)findViewById(R.id.splash_ll_dots);
		//safety check 
//		int dotnum = llDots.getChildCount();
//		if (pagenum != dotnum){
//			Log.e(LOG_TAG, "UI bug detected: dots!=pages.");
//			throw new RuntimeException("UI bug detected: dots!=pages.");
//		}
		for (int i=0; i<3; i++){
			ImageView img= new ImageView(SplashActivity.this, null, R.style.style_splash_viewpager_dot);
			llDots.addView(img);
//			dotList.add(llDots.getChildAt(i));
			dotList.add(img);
		}
		
		SplashAdapter adapter = new SplashAdapter(images);
		imageView.setVisibility(View.INVISIBLE);
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			private boolean hasJumped = false;	//解决多次跳转进入MainActivity的bug。
			
			@Override
			public void onPageSelected(int arg0) {
				setCurrentDot(arg0);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				if (arg0==pagenum-1 && !hasJumped){
					Log.d(LOG_TAG, "on page last");
					hasJumped = true;
					//using handler to force update.
					handler.sendEmptyMessage(SplashHandler.MSG_UI_FINISHED);
				}
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
	}
	
	private boolean checkFirstStart(){
		//首次使用时应为true
		boolean value = Prefs.getBoolean(this, Const.KEY_FIRSTUSE, true);
		Prefs.setBoolean(this, Const.KEY_FIRSTUSE, false);	
		return value;
	}
	
	
	//设置点的样式。
	private void setCurrentDot(int position){
		for (int i=0; i<dotList.size(); i++){
			if (i!=position){
				dotList.get(i).setSelected(false);
			}
			else {
				dotList.get(i).setSelected(true);
			}
		}
	}
	
	private class SplashAdapter extends PagerAdapter{
		
		private ArrayList<View> imglist;
		
		/**
		 * 构造一个适配器，该构造器默认两个list的长度相等，但不做检查。
		 * @param imglist 图片列表
		 * @param dotlist 小点列表
		 */
		public SplashAdapter(ArrayList<View> imglist) {
			if ( imglist==null ){
				throw new NullPointerException("param is null");
			}
			this.imglist = imglist;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			((ViewPager)container).addView(imglist.get(position%imglist.size()), 0);
			return imglist.get(position);
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager)container).removeView(imglist.get(position%imglist.size()));
		}

		@Override
		public int getCount() {
			return imglist.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0==arg1;
		}
		
	}
}

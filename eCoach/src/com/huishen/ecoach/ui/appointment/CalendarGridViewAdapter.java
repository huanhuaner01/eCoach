package com.huishen.ecoach.ui.appointment;

import java.util.List;
import java.util.Map;

import com.huishen.ecoach.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * 日期显示格子的GridView适配器
 * @author zhanghuan
 *
 */
@SuppressLint("NewApi") public class CalendarGridViewAdapter extends SimpleAdapter {
    private List<? extends Map<String,Object>> data ;
    private int currentChecked = -1 ; //当前选中的日期的索引 ，默认没有选中任何天数
    private int currentpreStatus = 0 ;//当前选中的日期的原始状态
    private Context context ;
    public CalendarGridViewAdapter(Context context,
			List<? extends Map<String, Object>> data, int resource, String[] from,
			int[] to ) {
		super(context, data, resource, from, to);
		this.data = data ;
		this.context = context ;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view =  super.getView(position, convertView, parent);
		TextView tv = (TextView)view.findViewById(R.id.day) ;
		TextView bottomtv = (TextView)view.findViewById(R.id.day_bottomtv) ;
		setTvStatus(bottomtv ,tv ,Integer.parseInt(this.data.get(position).get("status").toString()),position );
		view.setTag(this.data.get(position));
		return view ;
	}
	
	/**
	 * 设置选中项
	 * @param position 选中的索引
	 * 
	 */
	public void selectOption(int position){
		this.data.get(currentChecked).put("status",this.currentpreStatus) ;
		this.currentpreStatus = Integer.parseInt(this.data.get(position).get("status").toString()) ;
		
		this.data.get(position).put("status", 1);
		this.notifyDataSetChanged() ;
		
	}
	
	/**
	 * 设置日期格子的显示样式
	 * @param bottomtv
	 * @param tv
	 * @param status
	 * @param position
	 */
	private void setTvStatus(TextView bottomtv,TextView tv , int status ,int position){
		
		switch(status){
		case 0:  //可选
			tv.setEnabled(true)  ;
			tv.setSelected(false);
			break ;
		case 1:  //选中
			tv.setEnabled(true)  ;
			tv.setSelected(true);
			this.currentChecked = position ;
			break ;
			
		case 2:  //今日可选
			tv.setEnabled(true)  ;
			tv.setBackgroundResource(R.drawable.bkg_calendar_today) ;
			if(this.currentChecked == -1){
				tv.setSelected(true);
				this.currentChecked = position ;
				this.currentpreStatus = 2 ;
			}else{
				tv.setSelected(false);
			}
			bottomtv.setText("今日") ;
			bottomtv.setTextColor(this.context.getResources().getColor(R.color.color_calendar_title_background)) ;
			break ;
		case -1: //不可选
			tv.setEnabled(false) ;
			tv.setSelected(false);
			tv.setBackground(null);
			tv.setTextColor(this.context.getResources().getColor(R.color.color_calendar_gray_line)) ;
			break ;
		case -2: //今天不可选
			tv.setEnabled(false)  ;
			tv.setSelected(false);
			tv.setTextColor(this.context.getResources().getColor(R.color.color_calendar_gray_line)) ;
			tv.setBackgroundResource(R.drawable.bkg_calendar_circle_today_notoption) ;
			bottomtv.setText("今日") ;
			bottomtv.setTextColor(this.context.getResources().getColor(R.color.color_calendar_title_background)) ;
			break ;
			
		}
	}
    
}

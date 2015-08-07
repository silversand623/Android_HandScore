package com.example.webview;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class SegmentSeekBarView extends LinearLayout{
	private TextView TextValue;
	private TextView segLeftText;
	private TextView segRightText;
	private int groupId;
	private int childId;
	private SeekBar sb;
	private onSegmentSeekBarViewClickListener listener;
	public SeekBar getSb() {
		return sb;
	}

	public void setSb(SeekBar sb) {
		this.sb = sb;
	}

	public TextView getTextValue() {
		return TextValue;
	}

	public void setTextValue(TextView textValue) {
		TextValue = textValue;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getChildId() {
		return childId;
	}

	public void setChildId(int childId) {
		this.childId = childId;
	}

	public SegmentSeekBarView(Context context, AttributeSet attrs) {
		super(context,attrs);
		init(); 
		// TODO 自动生成的构造函数存根
	}

	public SegmentSeekBarView(Context context) {
		super(context);
		// TODO 自动生成的构造函数存根
		init();
	}

	private void init() {
		TextValue= new TextView(getContext());
		segLeftText = new TextView(getContext());
		segRightText = new TextView(getContext());
		sb=new SeekBar(getContext());
		TextValue.setTextColor(Color.BLUE);
		TextValue.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
		segLeftText.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
		segRightText.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
		sb.setLayoutParams(new LayoutParams(180, LayoutParams.WRAP_CONTENT, 1));		
		sb.setProgressDrawable(this.getResources().getDrawable(R.drawable.progress_holo_light));
		sb.setThumb(this.getResources().getDrawable(R.drawable.detail_icon_schedule_ball));	
		sb.setPadding(15,10, 15, 0);
		segLeftText.setText("+");		
		segRightText.setText("-");	
		//sb.setMax(5);
		
		      XmlPullParser xrp = getResources().getXml(R.drawable.seg_text_color_selector);    
		       try {    
		           ColorStateList csl = ColorStateList.createFromXml(getResources(), xrp);    
		           segLeftText.setTextColor(csl);
		           segRightText.setTextColor(csl);  
		        } catch (Exception e) {    
		        }   
		       segLeftText.setGravity(Gravity.CENTER);
		       segRightText.setGravity(Gravity.CENTER); 
		       segLeftText.setPadding(3, 6, 3, 6);
		      segRightText.setPadding(3, 6, 3, 6);		      
		       setSegmentTextSize(23); 
		       segLeftText.setBackgroundResource(R.drawable.seg_left);
		       segRightText.setBackgroundResource(R.drawable.seg_right);
		       segLeftText.setSelected(true);  
		       this.removeAllViews(); 
		       this.addView(TextValue); 
		       this.addView(sb); 
		       this.addView(segLeftText);
		       this.addView(segRightText);  
		       this.invalidate();
		       
		       segLeftText.setOnClickListener(new OnClickListener() {
		    	             @Override  
		    	             public void onClick(View v) {
		    	               segLeftText.setSelected(true);
		    	               segRightText.setSelected(false);
		    	               if(sb.getProgress()<sb.getMax())
		    	               {
		    	            	   sb.incrementProgressBy(1);
		    	               }		    	             
		    	               TextValue.setText(sb.getProgress()+"");
		    	                 if (listener != null) {  
		    	                     listener.onSegmentSeekBarViewClick(groupId,childId,segLeftText, sb.getProgress());  
		    	                	 //listener.onSegmentSeekBarViewClick(TextValue, 0);
		    	                 }  
		    	              }  
		    	 }); 
		       
		       segRightText.setOnClickListener(new OnClickListener() {
	  	             @Override  
	  	             public void onClick(View v) {
	  	               segLeftText.setSelected(false);
	  	               segRightText.setSelected(true);  
		  	               if(sb.getProgress()>0)
		  	               {
		  	            	   sb.setProgress(sb.getProgress()-1);
		  	               }
		  	             TextValue.setText(sb.getProgress()+"");
	  	                 if (listener != null) {  
	  	                     listener.onSegmentSeekBarViewClick(groupId,childId,segRightText, sb.getProgress());
	  	                	//listener.onSegmentSeekBarViewClick(TextValue, 0);
	  	                 }  
	  	              }  
			       });
		       
		       sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
				
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					// TODO 自动生成的方法存根
					
				}
				
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					// TODO 自动生成的方法存根
					
				}
				
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					// TODO 自动生成的方法存根
					TextValue.setText(progress+"");		
					
				}
			});

	}

	public void setSegmentTextSize(int dp) {  
		 TextValue.setTextSize(TypedValue.COMPLEX_UNIT_DIP, dp);
		 segLeftText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, dp);
		 segRightText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, dp); 
	}
		       
	 public void setOnSegmentViewClickListener(onSegmentSeekBarViewClickListener listener) {  
		  this.listener = listener;  
	}
	public void setSegmentText(CharSequence text,int position) {  
		if (position == 0) {  
			segLeftText.setText(text);  
		  }  
		  if (position == 1) {  
			  segRightText.setText(text);
		   }
		}  

	public static interface onSegmentSeekBarViewClickListener{  
		       /** 
		        *  
		        * <p>2014年7月18日</p> 
		        * @param v 
		       * @param position 0-左边 1-右边 
		        * @author RANDY.ZHANG 
		         */  
		       public void onSegmentSeekBarViewClick(int groupPosition, int childPosition,View v,int position);  
	}  

}

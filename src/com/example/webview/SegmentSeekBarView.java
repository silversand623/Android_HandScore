package com.example.webview;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.content.SharedPreferences;
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
	//��������
	private String progressStep;
	private String maxScore;
	private String MSI_RealScore;
	//��־��ʼ�������Ϊ����Ƴ�ʼ��ʱ����������ֵ
	//private String modelValue;
	
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

	public String getProgressStep() {
		return progressStep;
	}

	public void setProgressStep(String progressStep) {
		this.progressStep = progressStep;
	}

	public String getMaxScore() {
		return maxScore;
	}

	public void setMaxScore(String maxScore) {
		this.maxScore = maxScore;
	}

	public String getMSI_RealScore() {
		return MSI_RealScore;
	}

	public void setMSI_RealScore(String mSI_RealScore) {
		MSI_RealScore = mSI_RealScore;
	}
	

	public SegmentSeekBarView(Context context, AttributeSet attrs) {
		super(context,attrs);
		init(); 
		// TODO �Զ����ɵĹ��캯�����
	}

	public SegmentSeekBarView(Context context) {
		super(context);
		// TODO �Զ����ɵĹ��캯�����
		init();
	}

	private void init() {	
		 //������ݣ�������ѱ��������		
				
		TextValue= new TextView(getContext());
		segLeftText = new TextView(getContext());
		segRightText = new TextView(getContext());
		sb=new SeekBar(getContext());
		TextValue.setTextColor(getResources().getColor(R.color.blue));
		TextValue.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
		segLeftText.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
		segRightText.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
		sb.setLayoutParams(new LayoutParams(170, LayoutParams.WRAP_CONTENT, 1));		
		sb.setProgressDrawable(this.getResources().getDrawable(R.drawable.progress_holo_light));
		sb.setThumb(this.getResources().getDrawable(R.drawable.detail_icon_schedule_ball));	
		sb.setPadding(20,0, 40, 0);
		//���ÿ�Ⱥ͸߶�
		segLeftText.setWidth(35);
		segLeftText.setHeight(35);
		segRightText.setWidth(35);
		segRightText.setHeight(35);
		
		segLeftText.setText("+");		
		segRightText.setText("-");	

		
		      XmlPullParser xrp = getResources().getXml(R.drawable.seg_text_color_selectorseek);    
		       try { 
		           ColorStateList csl = ColorStateList.createFromXml(getResources(), xrp);    
		           segLeftText.setTextColor(csl);
		           segRightText.setTextColor(csl);  
		        } catch (Exception e) {    
		        }   
		       segLeftText.setGravity(Gravity.CENTER);
		       segRightText.setGravity(Gravity.CENTER);
		       setSegmentTextSize(25); 
		       segLeftText.setBackgroundResource(R.drawable.seg_leftseek);
		       segRightText.setBackgroundResource(R.drawable.seg_rightseek);
		       //segLeftText.setSelected(true);  
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
		    	               setRealValue();
		    	              
		    	                 if (listener != null) {  
		    	                     listener.onSegmentSeekBarViewClick(groupId,childId,segLeftText, TextValue.getText().toString());  
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
		  	             setRealValue();
	  	                 if (listener != null) {  
	  	                     listener.onSegmentSeekBarViewClick(groupId,childId,segRightText,TextValue.getText().toString());
	  	                	//listener.onSegmentSeekBarViewClick(TextValue, 0);
	  	                 }  
	  	              }  
			       });
		       
		       sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
				
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					// TODO �Զ����ɵķ������
					setRealValue();
					if (listener != null) {  
 	                     listener.onSegmentSeekBarViewClick(groupId,childId,sb,  TextValue.getText().toString()); 	                	
 	                 }  
				}
				
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					// TODO �Զ����ɵķ������
					
				}
				
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					// TODO �Զ����ɵķ������				
					//setRealValue(progress);
					//if (listener != null) {  
 	                     //listener.onSegmentSeekBarViewClick(groupId,childId,sb,  TextValue.getText().toString());
 	                	//listener.onSegmentSeekBarViewClick(TextValue, 0);
 	                 //}
				}
			});

	}
//���ݲ���������ʽ����
	private void setRealValue()
	{		
		 Float realScore=sb.getProgress()*Float.parseFloat(progressStep);		
			 if(realScore<Float.parseFloat(maxScore))
	         {
	      	   TextValue.setText(String.format("%.2f",realScore));
	         }
	         else
	         {
	      	   TextValue.setText(maxScore);
	         }			 
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
		        * <p>2014��7��18��</p> 
		        * @param v 
		       * @param position 0-��� 1-�ұ� 
		        * @author RANDY.ZHANG 
		         */  
		       public void onSegmentSeekBarViewClick(int groupPosition, int childPosition,View v,String TextValueStr);  
	}  

}

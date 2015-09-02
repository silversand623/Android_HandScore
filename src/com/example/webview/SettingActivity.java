package com.example.webview;

import java.util.HashMap;

import com.example.webview.tools.CustomDialog;
import com.example.webview.tools.DialogListener;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SettingActivity extends Activity {
	SegmentSet segmentSet;
	EditText eTextInterval;
	
	 private TextView TVBack;
	    private TextView TVsubmit; 
	    int modelValueStr;
	    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		segmentSet=(SegmentSet)findViewById(R.id.SegmentSetBar);
		eTextInterval=(EditText)findViewById(R.id.eTextInterval);
		
		 TVBack=(TextView)findViewById(R.id.TVBack);
		 TVsubmit=(TextView)findViewById(R.id.TVsubmit);
		 
		 SharedPreferences userInfo = getSharedPreferences("user_info",0);		
		 //������ݣ�������ѱ��������
		 if (userInfo.contains("progressStep")) {
			 String aa=userInfo.getString("progressStep",null);
			 eTextInterval.setText(userInfo.getString("progressStep",null));
			}
		 if (userInfo.contains("modelValue")) {
			 String tempValue=userInfo.getString("modelValue",null);
			 modelValueStr=Integer.parseInt(tempValue);
			 if(tempValue.equals("0"))
			 {
				 segmentSet.getSegLeftText().setSelected(true);
				 segmentSet.getSegRightText().setSelected(false);
			 }
			 else
			 {
				 segmentSet.getSegLeftText().setSelected(false);
				 segmentSet.getSegRightText().setSelected(true);
			 }
			 
			}
		//������ʾ����
		segmentSet.setSegmentText("�����",0);
		segmentSet.setSegmentText("�۷���",1);
		segmentSet.setOnSegmentViewClickListener(new SegmentSet.onSegmentViewClickListener() {
			
			@Override
			public void onSegmentViewClick(View v, int position) {
				// TODO �Զ����ɵķ������
				//�ڴ˿��Ի���û����������
				modelValueStr=position;
				
			}
		} );
		eTextInterval.addTextChangedListener(textWatcher);		
	
		//���ذ�ť
    	TVBack.setOnClickListener(new View.OnClickListener(){
			   @Override  
	             public void onClick(View v) {
				   //����ʱ�رձ�����				  
				    Intent intent = new Intent();   
	   				intent.setClass(SettingActivity.this, ScoreActivity.class);
	   				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);   			
	   				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	   				startActivity(intent);
	              }  
		});
		//������������
		TVsubmit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������	
				// �˴�Ϊʧȥ����ʱ�Ĵ�������
				String AllNumber=eTextInterval.getText().toString();
	        	if(AllNumber.length()>0)
	        	{
	        		if(Float.parseFloat(AllNumber)==0)
	        		{
	        			DialogAlert("���ּ������Ϊ0��");
	        			eTextInterval.setText("1");
	        			return;
	        		}
	        		else
	        		{
	        			eTextInterval.setText(String.valueOf(Float.parseFloat(AllNumber)));
	        		}	        		
	        		
	        	}
	        	else
	        	{
	        		//���Ϊ����Ĭ��1
	        		DialogAlert("���ּ������Ϊ�ա�");
		     			eTextInterval.setText("1");
		     			return;
	        	}
	        	//�ж����û���޸ľͲ���ʾ���Ƿ�ȷ���޸�
	        	String flag="false";
	        	 SharedPreferences userInfo = getSharedPreferences("user_info",0);		
	    		 //������ݣ�������ѱ��������
	    		 if (userInfo.contains("progressStep")) {
	    			 String aa=userInfo.getString("progressStep",null);
	    			 String bb=eTextInterval.getText().toString();
	    			 if(!aa.equals(bb))
	    			 {
	    				 flag="true";
	    			 }	    			
	    			}else
	    			{
	    				flag="true";
	    			}
	    		 if (userInfo.contains("modelValue")) {
	    			 String tempValue=userInfo.getString("modelValue",null);
	    			 if(!tempValue.equals(String.valueOf(modelValueStr)))
	    			 {
	    				 flag="true";
	    			 }
	    			}
	    		 else
	    		 {
	    			 flag="true";
	    		 }
	        	//��ʾ�Ƿ񱣴�����,�ж�������޸ĵ������򱣴棬����ֱ��������¼����
	    		 if(flag.equals("true"))
	    		 {
	        	 AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this); 
				 builder.setMessage("�Ƿ�ȷ���޸Ĳ�����");
				 builder.setTitle("��ʾ");
					builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {							
						public void onClick(DialogInterface dialog, int which) 
						{
							SharedPreferences userInfo = getSharedPreferences("user_info",0);
							userInfo.edit()
							.putString("progressStep",eTextInterval.getText().toString())
							.putString("modelValue",String.valueOf(modelValueStr))
							.commit();
							
							//��ת����ֽ���
			     			Intent intent = new Intent();   
				   			intent.setClass(SettingActivity.this, ScoreActivity.class);
				   			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);   			
				   			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				   			startActivity(intent);
						 }
						});
					builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {					  
					 public void onClick(DialogInterface dialog, int which) {
						 dialog.dismiss();
					 }
					});
					builder.create().show(); 
	    		 }
	    		 else
	    		 {
	    			//��ת����ֽ���
		     			Intent intent = new Intent();   
			   			intent.setClass(SettingActivity.this, ScoreActivity.class);
			   			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);   			
			   			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			   			startActivity(intent);
	    		 }
/*		 
				 CustomDialog customDialog = new CustomDialog(SettingActivity.this,
	     					R.style.MyDialog, new DialogListener() {
	     						@Override
	     						public void refreshActivity(Object object) {
	     	
	     						}
	     					}, "�������óɹ���", false);
	     			customDialog.show();*/
	     			
			}
		});

	}
	/*@Override
	public boolean dispatchKeyEvent(KeyEvent event)
	{
		System.out.println(event.getKeyCode());
		return true;
	}*/
	private TextWatcher textWatcher = new TextWatcher() {  
		 
		@Override  
		public void onTextChanged(CharSequence s, int start, int before,int count) {		   
			
		}
		@Override  
		public void beforeTextChanged(CharSequence s, int start, int count,int after)
		{	
			
		} 
		
		@Override  
		public void afterTextChanged(Editable s) {  
		   
			String AllNumber=s.toString();
        	if(AllNumber.length()>0)
        	{
        		if(AllNumber.indexOf(".")!=-1)
        		{
        			String dotNumber=AllNumber.substring(AllNumber.indexOf("."));
		        	if(dotNumber.length()>2)
		        	{
		        		DialogAlert("ֻ�ܾ�ȷ��һλС����");		        		     			
		     			//��ȡС�����һλ��ֵ���ı���
			        	eTextInterval.setText(AllNumber.substring(0, AllNumber.indexOf(".")+2));
		        	}		        	    	
        		}
        		
        	}
			
		} 
	};
	public void DialogAlert(String Message)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this); 
		builder.setTitle("��ʾ").setMessage(Message) 
	      .setPositiveButton("ȷ��", 
	                    new DialogInterface.OnClickListener(){ 
	                               public void onClick(DialogInterface dialoginterface, int i){ 
	                                    //��ť�¼� 
	                                 } 
	                         }) 
	        .show();	
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.setting, menu);
		return true;
	}

}

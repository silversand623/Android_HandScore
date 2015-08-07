package com.example.webview.tools;

import com.example.webview.R;
import com.example.webview.R.id;
import com.example.webview.R.layout;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

public class CustomDialog extends Dialog{
	private String bb;
	Context context;
	LayoutParams p ;
	boolean dd;
	DialogListener dialogListener;
	private Button btnokButton;
	private Button btncacelButton;
	private TextView textView;
	public CustomDialog(Context context,int theme,DialogListener dialogListener,String aa,boolean cc) {
		super(context,theme);
		this.context = context;
		this.dialogListener = dialogListener;
		bb=aa;
		dd=cc;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
		requestWindowFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.dialog_layout);
//		p = getWindow().getAttributes();  //��ȡ�Ի���ǰ�Ĳ���ֵ   
//		p.height = 240;//(int) (d.getHeight() * 0.4);   //�߶�����Ϊ��Ļ��0.4 
//		p.width = 320;//(int) (d.getWidth() * 0.6);    //�������Ϊ��Ļ��0.6		   
//		getWindow().setAttributes(p);     //������Ч
		textView=(TextView)findViewById(R.id.tablet_text);
		textView.setText(bb);
		Button btnOk = (Button) findViewById(R.id.tablet_ok);
		btnOk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					dialogListener.refreshActivity("");
					CustomDialog.this.dismiss();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	
		Button btnCancel = (Button)findViewById(R.id.tablet_cancel);
		if(dd==true){
		btnCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				cancel();
			}
		});
		}
		else{
			btnCancel.setVisibility(View.GONE);
		}
	}
}

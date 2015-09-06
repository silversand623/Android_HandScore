package com.example.webview.tools;

import com.example.webview.R;
import com.koushikdutta.ion.Ion;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;

public class CustomDialogImage extends Dialog{
	private String bb;
	Context context;
	LayoutParams p ;
	boolean dd;
	DialogListener dialogListener;	
	private ImageView imageView;
	public CustomDialogImage(Context context,int theme,DialogListener dialogListener,String aa,boolean cc) {
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
		setContentView(R.layout.dialogimage_layout);
		imageView=(ImageView)findViewById(R.id.dialog_title_image);
		//����ͼƬ����
    	Ion.with(imageView)
        // use a placeholder google_image if it needs to load from the network
        .placeholder(R.drawable.username)
        .error(R.drawable.username)
        // load the url
        .load(bb);
		Button btnOk = (Button) findViewById(R.id.tablet_ok);
		btnOk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					//dialogListener.refreshActivity("");
					CustomDialogImage.this.dismiss();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	
		/*Button btnCancel = (Button)findViewById(R.id.tablet_cancel);
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
		}*/
	}
}

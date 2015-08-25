package com.example.webview;


import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import com.example.webview.tools.CustomDialog;
import com.example.webview.tools.DialogListener;
import com.example.webview.tools.WritePadDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HandwritingActivity extends Activity {
    /** Called when the activity is first created. */
	
	private Bitmap mSignBitmap;
	private String signPath;
	private ImageView ivSign;
	private TextView tvSign;
	private Button btn_submit;
	private String sign_dir;
	private String newName ="image.jpg";
	private ProgressDialog pd;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setTitle("��ӭʹ������ǩ��");
        ivSign =(ImageView)findViewById(R.id.iv_sign);
        tvSign = (TextView)findViewById(R.id.tv_sign);      
        btn_submit=(Button)findViewById(R.id.btn_sumbit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(tvSign.getVisibility()==View.VISIBLE){
					Toast.makeText(getApplicationContext(), "������ǩ�������ύ", 0).show();
				}else{
				uploadFile("");
				}
			}
		});
        ivSign.setOnClickListener(signListener);
        tvSign.setOnClickListener(signListener);
    }
    
	
	private OnClickListener signListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			WritePadDialog writeTabletDialog = new WritePadDialog(
					HandwritingActivity.this, new DialogListener() {
						@Override
						public void refreshActivity(Object object) {							
							
							mSignBitmap = (Bitmap) object;
//							BitmapFactory.Options options = new BitmapFactory.Options();
//							options.inSampleSize = 15;
//							options.inTempStorage = new byte[5 * 1024];
//							Bitmap zoombm = BitmapFactory.decodeFile(signPath, options);	
							//ivSign.setImageBitmap(zoombm);
							//Bitmap.createScaledBitmap(bitmap, width, height, false);
							Bitmap bitmap=Bitmap.createScaledBitmap(mSignBitmap, 200, 200, false);
							signPath = createFile();
							//Bitmap bitmap=Bitmap.createBitmap(mSignBitmap, 0, 0, 200, 200);
							//ivSign.setImageBitmap(mSignBitmap);
							ivSign.setImageBitmap(bitmap);
						
						
							ivSign.setVisibility(View.VISIBLE);
							ivSign.setClickable(false);
							tvSign.setClickable(false);
							tvSign.setVisibility(View.GONE);
						}
					});
			writeTabletDialog.show();
		}
	};
	
	/**
	 * ������дǩ���ļ�
	 * 
	 * @return
	 */
	private String createFile() {
		ByteArrayOutputStream baos = null;
		String _path = null;
		try {
			String sign_dir = Environment.getExternalStorageDirectory().getPath()+"/qianmingImages/";			
			_path = sign_dir + System.currentTimeMillis() + ".jpg";
			baos = new ByteArrayOutputStream();
			mSignBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			byte[] photoBytes = baos.toByteArray();
			if (photoBytes != null) {
				new FileOutputStream(new File(_path)).write(photoBytes);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null)
					baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return _path;
	} 
	private void uploadFile(String actionUrl)
    {
      String end ="\r\n";
      String twoHyphens ="--";
      String boundary ="*****";
    
      try
      {
        URL url =new URL(actionUrl);
        HttpURLConnection con=(HttpURLConnection)url.openConnection();
        pd = new ProgressDialog(HandwritingActivity.this);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);// ���÷��ΪԲ�ν�����
		pd.setTitle("�Ե�Ƭ��");// ���ñ���
		pd.setMessage("���ڼ��أ����Ժ�");
		pd.setIndeterminate(false);// ���ý������Ƿ�Ϊ����ȷ
		pd.setCancelable(true);// ���ý������Ƿ���԰��˻ؼ�ȡ��
		pd.show();
        /* ����Input��Output����ʹ��Cache */
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false);
        /* ���ô��͵�method=POST */
        con.setRequestMethod("POST");
        /* setRequestProperty */
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");
        con.setRequestProperty("Content-Type",
                           "multipart/form-data;boundary="+boundary);
        /* ����DataOutputStream */
        DataOutputStream ds =
          new DataOutputStream(con.getOutputStream());
        ds.writeBytes(twoHyphens + boundary + end);
        ds.writeBytes("Content-Disposition: form-data; "+
                      "name=\"file1\";filename=\""+
                      newName +"\""+ end);
        ds.writeBytes(end);  
        /* ȡ���ļ���FileInputStream */
        FileInputStream fStream =new FileInputStream(signPath);
        /* ����ÿ��д��1024bytes */
        int bufferSize =1024;
        byte[] buffer =new byte[bufferSize];
        int length =-1;
        /* ���ļ���ȡ������������ */
        while((length = fStream.read(buffer)) !=-1)
        {
          /* ������д��DataOutputStream�� */
          ds.write(buffer, 0, length);
        }
        ds.writeBytes(end);
        ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
        /* close streams */
        fStream.close();
        ds.flush();
        /* ȡ��Response���� */
        InputStream is = con.getInputStream();
        int ch;
        StringBuffer b =new StringBuffer();
        while( ( ch = is.read() ) !=-1 )
        {
          b.append( (char)ch );
        }
        pd.dismiss();
        /* ��Response��ʾ��Dialog */
        showDialog("�ϴ��ɹ�"+b.toString().trim());
        /* �ر�DataOutputStream */
        ds.close();
      }
      catch(Exception e)
      {
      if(pd==null){
    	     CustomDialog customDialog=new CustomDialog(HandwritingActivity.this,R.style.MyDialog,new DialogListener() {
 				@Override
 				public void refreshActivity(Object object) {							
 				
 				
 				}
 			}, "�ϴ�ʧ��",false);
 customDialog.show();
     //  showDialog("�ϴ�ʧ��");
      }
      }
    }
    /* ��ʾDialog��method */
    private void showDialog(String mess)
    {
      new AlertDialog.Builder(HandwritingActivity.this).setTitle("Message")
       .setMessage(mess)
       .setNegativeButton("ȷ��",new DialogInterface.OnClickListener()
       {
         public void onClick(DialogInterface dialog, int which)
         {          
        	 Intent intent=new Intent();
        	 intent.setClass(HandwritingActivity.this, HandheldscaleActivity.class);
        	 startActivity(intent);
         }
       })
       .show();
    }
    public void createSDCardDir(){
		 File sdcardDir =Environment.getExternalStorageDirectory();
        //�õ�һ��·����������sdcard���ļ���·��������
          String pathdir=sdcardDir.getPath()+"/qianmingImages/";
          File path1 = new File(pathdir);
         if (!path1.exists()) {
          //�������ڣ�����Ŀ¼��������Ӧ��������ʱ�򴴽�
          path1.mkdirs();
//          setTitle("paht ok,path:"+pathdir);
        }
	    }
}
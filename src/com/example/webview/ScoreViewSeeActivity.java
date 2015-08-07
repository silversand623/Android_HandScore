package com.example.webview;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import com.example.webview.tools.CustomDialog;
import com.example.webview.tools.DialogListener;
import com.example.webview.tools.WritePadDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.handscore.model.LoginInfoType;
import com.handscore.model.MarkSheet;
import com.handscore.model.StudentInfo;
import com.handscore.model.MarkSheet.Items;
import com.handscore.model.MarkSheet.MarkData;
import com.handscore.model.MarkSheet.children_item;
import com.handscore.model.StudentInfo.Student;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ScoreViewSeeActivity extends Activity {
	 private Bitmap mSignBitmap;   
	 private String signPath;   
    private ImageView ivSign;   
    private Button btnfanhui;
    private Button btnsubmit;
     //�����б���Ϣ   	
 	private ExpandableListView expandableListView;	
 	private ProgressHUD mProgressHUD;
 	private MyExpandableListViewAdapter adapter;
 	MarkSheet Infos;
 	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_score_view_see);
		ivSign =(ImageView)findViewById(R.id.iv_sign);   
		 btnfanhui=(Button)findViewById(R.id.btnfanhui);
		 btnsubmit=(Button)findViewById(R.id.btnsubmit);
		 
		 GlobalSetting myApp = (GlobalSetting)getApplication();
		 
		    LoginInfoType info = myApp.gLoginItem;
		    try
		    {
			    if (info != null) {
			    	TextView tvExamName = (TextView)findViewById(R.id.ExamName);
			    	tvExamName.setText(URLDecoder.decode(info.E_Name, "UTF-8"));
			    	TextView tvStationName = (TextView)findViewById(R.id.KaoZhan);
			    	tvStationName.setText(URLDecoder.decode(info.ES_Name, "UTF-8"));
			    	TextView RoomName = (TextView)findViewById(R.id.FangJian);
			    	RoomName.setText(URLDecoder.decode(info.Room_Name, "UTF-8"));
			    	TextView ScoreItems = (TextView)findViewById(R.id.PingFenBiaoCount);
			    	ScoreItems.setText(URLDecoder.decode(info.mark_sheet_count, "UTF-8"));
			    }
			    
			    for (HashMap<String, Object> map : myApp.gStudnetArray)
	            {
	            	String sUid = (String)map.get("U_ID");
	            	if (sUid == myApp.gStudentId)
	            	{
	            		TextView labelExamNo = (TextView)findViewById(R.id.TextXueHao);
	            		labelExamNo.setText(URLDecoder.decode((String)map.get("itemXuehao"), "UTF-8"));
	            		TextView labelName = (TextView)findViewById(R.id.TextKaoHao);
	            		labelName.setText(URLDecoder.decode((String)map.get("itemKaohao"), "UTF-8"));
	            		TextView labelStudentNo = (TextView)findViewById(R.id.TextXingMing);
	            		labelStudentNo.setText(URLDecoder.decode((String)map.get("itemName"), "UTF-8"));
	            		TextView labelClassName = (TextView)findViewById(R.id.TextBanji);
	            		labelClassName.setText(URLDecoder.decode((String)map.get("itemBanJi"), "UTF-8"));
	            		TextView TextZongFen=(TextView)findViewById(R.id.TextZongFen);
	            		TextZongFen.setText(String.valueOf(getSum(0)));
	            		TextView TextDeFen=(TextView)findViewById(R.id.TextDeFen);
	            		TextDeFen.setText(String.valueOf(getSum(1)));
	            		break;
	            	}
	            }
			    
		    }catch (Exception e1)
		    {
		    	
		    }
		    
		    boolean bValue = false;
		    if (myApp.getScoresList() != null)
		    {
			    for (HashMap<String, Object> map : myApp.getScoresList())
	            {
	            	String sUid = (String)map.get("id");
	            	if (sUid == myApp.gStudentId)
	            	{
	            		Infos = (MarkSheet)map.get("marksheet");
	            		bValue = true;
	            		break;
	            	}
	            }
		    }
	        if (!bValue) {
	            mProgressHUD = ProgressHUD.show(this,"���ڲ�ѯ�ɼ�", true,true,null);   
	            getScoreInfo();
	        }
	        
	
		expandableListView =(ExpandableListView)findViewById(R.id.LVListView);	
		//���۵���;		
		expandableListView.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// TODO �Զ����ɵķ������
				return true;
			}
		});		
		adapter=new MyExpandableListViewAdapter(this);
		expandableListView.setAdapter(adapter);	
		for(int i = 0; i < adapter.getGroupCount(); i++){
    		expandableListView.expandGroup(i);
    	}
		//���ذ�ť
		btnfanhui.setOnClickListener(new View.OnClickListener(){
			   @Override  
	             public void onClick(View v) {
				   //����ʱ�رձ�����
				   ScoreViewSeeActivity.this.finish();
	              }  
		});
	
	}
	
	private void getScoreInfo(){
		SharedPreferences userInfo = getSharedPreferences("user_info",0);
		if (!userInfo.contains("ipconfig")) {
			return;
		}
		GlobalSetting myApp = (GlobalSetting)getApplication();
		LoginInfoType loginItem = myApp.gLoginItem;
		String BaseUrl = userInfo.getString("ipconfig", null);
		String url="http://";
	    url=url+BaseUrl+"/AppDataInterface/HandScore.aspx/SearchStudentInfo";
	    Ion.with(this)
        .load(url)
        .setBodyParameter("E_ID", loginItem.E_ID)
        .setBodyParameter("ES_ID", loginItem.ES_ID)
        .setBodyParameter("Room_ID", loginItem.Room_ID) 
        .setBodyParameter("U_ID", loginItem.U_ID) 
        .setBodyParameter("Student_U_ID",myApp.getId())
        .asJsonObject()
        .setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                mProgressHUD.dismiss();
            	if (e != null) {
                    return;
                }
                //////
                try {
                	Gson gson =new Gson();
                	java.lang.reflect.Type type = new TypeToken<MarkSheet>() {}.getType();
                	Infos=gson.fromJson(result,type);
                	adapter.notifyDataSetChanged();
                	for(int i = 0; i < adapter.getGroupCount(); i++){
                		expandableListView.expandGroup(i);
                	} 
	            }
                catch (Exception eJson) {
                	////
                }
                /////
                
            }
        });
	}
	
	
	//0 return Total score,else return actural score
		private float getSum(int nCase) {
		    float nSum = 0.0f;
		    float nTotalSum = 0.0f;
		    if(Infos!=null)
			{
				if(Infos.mark_sheet_list.size()>0)
				{
					for (int i = 0; i < Infos.mark_sheet_list.get(0).item_list.size(); i++)
					{

						for (int j=0; j < Infos.mark_sheet_list.get(0).item_list.get(i).children_item_list.size(); j++)
						{
							children_item  ci=Infos.mark_sheet_list.get(0).item_list.get(i).children_item_list.get(j);
							nSum +=Float.parseFloat(ci.MSI_Score);
							nTotalSum +=Float.parseFloat(ci.MSI_RealScore);
						}
					}
				}
			}
		    if (nCase ==0)
		    {
		    	return nSum;
		    } else
		    {
		    	return nTotalSum;
		    }
		}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.score_view, menu);
		return true;
	}
	
		// �ù�ListView����һ������Ϥ��ֻ���������� BaseExpandableListAdapter
			class MyExpandableListViewAdapter extends BaseExpandableListAdapter {	
				private Context context;
				public MyExpandableListViewAdapter(Context context) {    this.context = context;   } 
				@Override
				public int getGroupCount() {
					int num=0;
					if(Infos!=null)
					{
						if(Infos.mark_sheet_list.size()>0)
						{				
							num=Infos.mark_sheet_list.get(0).item_list.size();
						}
					}
					return num;
				}

				@Override
				public int getChildrenCount(int groupPosition) {
					int num=0;
					if(Infos!=null)
					{
						if(Infos.mark_sheet_list.size()>0)
						{
								num=Infos.mark_sheet_list.get(0).item_list.get(groupPosition).children_item_list.size();
						}
					}
					return num;
				}

				@Override
				public Object getGroup(int groupPosition) {
					Object obj=null;
					if(Infos!=null)
					{
					if(Infos.mark_sheet_list.size()>0)
					{				
						obj=Infos.mark_sheet_list.get(0).item_list.get(groupPosition);
					}
					}
					return obj;
				}

				@Override
				public Object getChild(int groupPosition, int childPosition) {			
					Object obj=null;
					if(Infos!=null)
					{
					if(Infos.mark_sheet_list.size()>0)
					{	
						//������Ϊ��ʱ��������ӵ�����
						if(Infos.mark_sheet_list.get(0).item_list.get(groupPosition).children_item_list==null)
						{
							
							obj=Infos.mark_sheet_list.get(0).item_list.get(groupPosition);
						}
						else
						{
							obj=Infos.mark_sheet_list.get(0).item_list.get(groupPosition).children_item_list.get(childPosition);
						}
					}
					}			
					return obj;
				}

				@Override
				public long getGroupId(int groupPosition) {
					return groupPosition;
				}

				@Override
				public long getChildId(int groupPosition, int childPosition) {
					return childPosition;
				}

				@Override
				public boolean hasStableIds() {
					return true;
				}

				@Override
				public View getGroupView(int groupPosition, boolean isExpanded,
						View convertView, ViewGroup parent) {
					GroupHolder groupHolder = null;
					if (convertView == null) {
						convertView = (View) getLayoutInflater().from(context).inflate(
								R.layout.activity_scoreparent, null);
						groupHolder = new GroupHolder();
						groupHolder.txt = (TextView) convertView.findViewById(R.id.itemtitle);						
						convertView.setTag(groupHolder);
					} else {
						groupHolder = (GroupHolder) convertView.getTag();
					}			
					try {
						if(Infos!=null)
						{
						if(Infos.mark_sheet_list.size()>0)
						{
							groupHolder.txt.setText(URLDecoder.decode(Infos.mark_sheet_list.get(0).item_list.get(groupPosition).MSI_Item,"UTF-8"));
						}
						}
					} catch (UnsupportedEncodingException e) {
						// TODO �Զ����ɵ� catch ��
						e.printStackTrace();
					}
					return convertView;
				}

				@Override
				public View getChildView(int groupPosition, int childPosition,
						boolean isLastChild, View convertView, ViewGroup parent) {
					ItemHolder itemHolder = null;
					if (convertView == null) {
						convertView = (View) getLayoutInflater().from(context).inflate(
								R.layout.activity_scorelistview, null);
						itemHolder = new ItemHolder();
						itemHolder.itemNeiRong = (TextView) convertView.findViewById(R.id.itemNeiRong);						
						itemHolder.itemFenZhi = (TextView) convertView.findViewById(R.id.itemFenZhi);						
						convertView.setTag(itemHolder);
					} else {
						itemHolder = (ItemHolder) convertView.getTag();
					}
					try {
						if(Infos!=null)
						{
						if(Infos.mark_sheet_list.size()>0)
						{
						if(groupPosition<Infos.mark_sheet_list.get(0).item_list.size())
						{															
								children_item  ci=Infos.mark_sheet_list.get(0).item_list.get(groupPosition).children_item_list.get(childPosition);
								itemHolder.itemNeiRong.setText(URLDecoder.decode(ci.MSI_Item,"UTF-8"));
								itemHolder.itemFenZhi.setText(ci.MSI_RealScore);												
							
						}
						}
						}
					} catch (NumberFormatException e) {
						// TODO �Զ����ɵ� catch ��
						e.printStackTrace();
					} catch (UnsupportedEncodingException e) {
						// TODO �Զ����ɵ� catch ��
						e.printStackTrace();
					}
					return convertView;
				}

				@Override
				public boolean isChildSelectable(int groupPosition, int childPosition) {
					return true;
				}
			}

			class GroupHolder {
				public TextView txt;		
			}

			class ItemHolder {
				TextView itemNeiRong;
				TextView itemFenZhi;				
			}
}

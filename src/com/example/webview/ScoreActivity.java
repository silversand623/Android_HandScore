package com.example.webview;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.webview.SegmentSeekBarView.onSegmentSeekBarViewClickListener;
import com.example.webview.tools.CustomDialog;
import com.example.webview.tools.DialogListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.handscore.model.LoginInfoType;
import com.handscore.model.MarkSheet;
import com.handscore.model.StudentInfo;
import com.handscore.model.MarkSheet.Items;
import com.handscore.model.MarkSheet.children_item;
import com.handscore.model.StudentInfo.Student;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ScoreActivity extends Activity {	
	private ExpandableListView expandableListView;	
	private LoginInfoType loginItem;
	private ProgressHUD mProgressHUD;
	private MyExpandableListViewAdapter adapter;
	MarkSheet Infos;
	 private int scorestep=100;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_score);
		try {
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
	            		TextView labelExamNo = (TextView)findViewById(R.id.kaohao);
	            		labelExamNo.setText(URLDecoder.decode((String)map.get("itemKaohao"), "UTF-8"));
	            		break;
	            	}
	            }
			    
		    }catch (Exception e1)
		    {
		    	
		    }
			mProgressHUD = ProgressHUD.show(this,"正在加载评分表", true,true,null);
			getMarkSheetInfo();
            
			expandableListView =(ExpandableListView)findViewById(R.id.LVList); 
			//不折叠树;		
			expandableListView.setOnGroupClickListener(new OnGroupClickListener() {
				@Override
				public boolean onGroupClick(ExpandableListView parent, View v,
						int groupPosition, long id) {
					// TODO 自动生成的方法存根
					return true;
				}
			});
			adapter=new MyExpandableListViewAdapter(this);
			expandableListView.setAdapter(adapter); 
			
			//第一种方式    
	        Button btnPreview = (Button)findViewById(R.id.btnyulan);//获取按钮资源    
	        btnPreview.setOnClickListener(new Button.OnClickListener(){//创建监听    
	            public void onClick(View v) {    
	            	boolean bValue = false;
	                int nIndex = -1;
	                int nScoreCount=0;
	                int nSection = -1;
	                if(Infos!=null)
					{
						if(Infos.mark_sheet_list.size()>0)
						{
							for (int i = 0; i < Infos.mark_sheet_list.get(0).item_list.size(); i++)
							{
								if (!bValue) 
								{
		                            nIndex = -1;
		                            nSection++;
		                        }
								
								for (int j=0; j < Infos.mark_sheet_list.get(0).item_list.get(i).children_item_list.size(); j++)
								{
									children_item  ci=Infos.mark_sheet_list.get(0).item_list.get(i).children_item_list.get(j);
									if (!bValue) {
		                                nIndex++;
		                            }
									if (ci.MSI_RealScore == "-1"){
		                                bValue = true;
		                                nScoreCount++;
		                            }
								}
								
								
							}
						}
					}
	                
	                if (bValue) {
	                    expandableListView.setSelectedChild(nSection, nIndex, true);
	                    String strMsg = String.format("还有%d项未打分，请完成打分再预览", nScoreCount);
	                    CustomDialog customDialog = new CustomDialog(ScoreActivity.this,
	        					R.style.MyDialog, new DialogListener() {
	        						@Override
	        						public void refreshActivity(Object object) {
	        	
	        						}
	        					}, strMsg, false);
	        			customDialog.show();
	                    
	                    return;
	                }
	                
	                GlobalSetting myApp = (GlobalSetting)getApplication();  
                    myApp.setMarkSheet(Infos);
	                Intent intent =new Intent(ScoreActivity.this,ScoreViewActivity.class); 
	        		ScoreActivity.this.startActivity(intent);
	            }    
	  
	        }); 
	        
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
	}
	
	private void getMarkSheetInfo(){
		GlobalSetting myApp = (GlobalSetting)getApplication();  
		loginItem=myApp.gLoginItem;
		SharedPreferences userInfo = getSharedPreferences("user_info",0);
		if (!userInfo.contains("ipconfig")) {
			return;
		}
		String BaseUrl = userInfo.getString("ipconfig", null);
		String url="http://";
	    url=url+BaseUrl+"/AppDataInterface/HandScore.aspx/SearchMarkSheet";
	    Ion.with(this)
        .load(url)
        .setBodyParameter("EU_ID", loginItem.EU_ID) 
        .asJsonObject()
        .setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                mProgressHUD.dismiss();
            	if (e != null) {
                    return;
                }
                //////
                try 
                {                	
                	Gson gson =new Gson();
                	java.lang.reflect.Type type = new TypeToken<MarkSheet>() {}.getType();
                	Infos=gson.fromJson(result,type);
                	adapter.notifyDataSetChanged();
                	for(int i = 0; i < adapter.getGroupCount(); i++){
                		expandableListView.expandGroup(i);
                	} 
                
	            }
                catch (Exception eJson) {
                	System.out.println(eJson);
                }
                /////
                
            }
        });
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.score, menu);
		return true;
	}
	// 用过ListView的人一定很熟悉，只不过这里是 BaseExpandableListAdapter
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
					if(Infos.mark_sheet_list.get(0).item_list.get(groupPosition).children_item_list==null)
					{
						List<children_item> childList=new ArrayList<children_item>();
						Items item=Infos.mark_sheet_list.get(0).item_list.get(groupPosition);
						children_item ci=new children_item();
						ci.MSI_ID=item.MSI_ID;
						ci.MSI_Item=item.MSI_Item;
						ci.MSI_Score=item.MSI_Score;	
						ci.MSI_RealScore="-1";
						try {
							childList.add(ci);
							Infos.mark_sheet_list.get(0).item_list.get(groupPosition).children_item_list=childList;
						} catch (Exception e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}
						
						num=1;
					}
					else
					{
						num=Infos.mark_sheet_list.get(0).item_list.get(groupPosition).children_item_list.size();
					}	
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
					//在子项为空时将父项添加到子项
					if(Infos.mark_sheet_list.get(0).item_list.get(groupPosition).children_item_list==null)
					{
						
						obj=Infos.mark_sheet_list.get(0).item_list.get(groupPosition);
					}
					else
					{
						obj=Infos.mark_sheet_list.get(0).item_list.get(groupPosition).children_item_list.get(childPosition);
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
					// TODO 自动生成的 catch 块
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
							R.layout.activity_scorelist, null);
					itemHolder = new ItemHolder();
					itemHolder.itemNeiRong = (TextView) convertView.findViewById(R.id.itemNeiRong);
					itemHolder.itemFenZhi = (TextView) convertView.findViewById(R.id.itemFenZhi);
					itemHolder.segSeekBar= (SegmentSeekBarView) convertView.findViewById(R.id.SegSeekBar);				
					
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
								itemHolder.itemFenZhi.setText(ci.MSI_Score);
								SeekBar sb= itemHolder.segSeekBar.getSb();													
								sb.setMax((int)Float.parseFloat(ci.MSI_Score));
								itemHolder.segSeekBar.setGroupId(groupPosition);
								itemHolder.segSeekBar.setChildId(childPosition);							
								TextView tv= itemHolder.segSeekBar.getTextValue();
								
								if (ci.MSI_RealScore == "-1")
								{									
									sb.setProgress(0);
									tv.setText("");
								} else
								{
									sb.setProgress((int)Float.parseFloat(ci.MSI_RealScore));
									tv.setText(ci.MSI_RealScore);									
								}											
							
							itemHolder.segSeekBar.setOnSegmentViewClickListener(new onSegmentSeekBarViewClickListener() {
				  	             @Override  
				  	             public void onSegmentSeekBarViewClick(int groupId,int childId,View v,int position) {
				  	                 children_item  ciInfo=Infos.mark_sheet_list.get(0).item_list.get(groupId).children_item_list.get(childId);	
				  	                 ciInfo.MSI_RealScore =Integer.toString(position);
				  	                
				  	              }  
						     });
						}
					}
					}
				} catch (NumberFormatException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO 自动生成的 catch 块
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
			SegmentSeekBarView segSeekBar;
		}
}

package com.example.webview;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.king.swipelibrary.SwipeAdapter;
import cn.king.swipelibrary.SwipeLayout;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class StudentAdapter extends SwipeAdapter {

	private ArrayList<HashMap<String, Object>> list;
	private Context context;
	private String url;
	SwipeLayout swipe;

	public StudentAdapter(Context context, ArrayList<HashMap<String, Object>> data) {
		//super(context, data);
		// TODO Auto-generated constructor stub
		this.list = data;
		this.context = context;
	}
	@Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }
	public void setList(ArrayList<HashMap<String, Object>> data)
	{
		if(swipe!=null)
		{
			swipe.close();
		}
		this.list = data;
	}
	
	public void setUrl(String url)
	{
		this.url = url;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}	

	class ViewHolder {
		TextView TvZhaoPian;
		TextView TvPingFen;
		TextView TvQueKao;
		ImageView img;
		//TextView itemName;
		TextView itemTime;
		TextView itemKaohao;
		TextView itemXuehao;
		TextView itemBanJi;
		TextView itemZhuangtai;
		TextView itemFenshu;
	}
	
	

    @Override
    public View generateView(int position, ViewGroup parent) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_mainlist, null);
        SwipeLayout swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
			@Override
			public void onClose(SwipeLayout layout) {

			}

			@Override
			public void onUpdate(SwipeLayout layout, int leftOffset,
					int topOffset) {
				
			}

			@Override
			public void onOpen(SwipeLayout layout) {
				if (swipe != null && swipe!=layout)
				{
					swipe.close();
					swipe = layout;
				}else
				{
					swipe=layout;
				}
			}

			@Override
			public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
				
			}
		});
        
        return v;
    }

	public void getSystemTime(final HashMap<String, Object> map)
	{
		SharedPreferences userInfo = context.getSharedPreferences("user_info",0);
		if (!userInfo.contains("ipconfig")) {
			return;
		}
		String BaseUrl = userInfo.getString("ipconfig", null);
		String url="http://";
	    url=url+BaseUrl+"/AppDataInterface/HandScore.aspx/SearchCurrentSystemDatetime";
	    Ion.with(context)
        .load(url)
        .asString()
        .setCallback(new FutureCallback<String>() {
            @Override
            public void onCompleted(Exception e, String result) {
                if (e != null) {
                    return;
                }
                //////
                try {
                	
                	//2015-08-24 15:38:53
                	Date dateSystem=new Date();  
                    Date dateStart=new Date();
                    Date dateEnd=new Date();
                    SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
                    String sStartTime = map.get("itemTime").toString();
                    String sEndTime = map.get("itemEndTime").toString();
                    try {                  
                    	dateSystem=format.parse(result);  
                    	dateStart=format.parse(String.format("%s %s:00", result.substring(0, 10),sStartTime));
                    	dateEnd = format.parse(String.format("%s %s:00", result.substring(0, 10),sEndTime));
                    	long lInterval1 = dateSystem.getTime() - dateStart.getTime();
                    	long lInterval2 = dateEnd.getTime()-dateSystem.getTime();
                    	if (lInterval1 >=0.0 && lInterval2 >=0.0) 
                        {
                        	Intent intent =new Intent(context,ScoreActivity.class); 
                        	context.startActivity(intent);
                        } else if (lInterval1 < 0.0)
                        {
                        	AlertDialog.Builder builder = new AlertDialog.Builder(context); 
           				 builder.setMessage("当前学生还没有开始考试，请确认是否继续评分？");
           				 builder.setTitle("提示");
           					builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {							
           						public void onClick(DialogInterface dialog, int which) 
           						{
           							Intent intent =new Intent(context,ScoreActivity.class); 
           							context.startActivity(intent);
           						 }
           						});
           					builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {					  
           					 public void onClick(DialogInterface dialog, int which) {
           						 dialog.dismiss();
           					 }
           					});
           					builder.create().show();
                        } else if (lInterval2 < 0.0)
                        {
                        	AlertDialog.Builder builder = new AlertDialog.Builder(context); 
              				 builder.setMessage("当前学生考试时间已过,请确认是否继续评分？");
              				 builder.setTitle("提示");
              					builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {							
              						public void onClick(DialogInterface dialog, int which) 
              						{
              							Intent intent =new Intent(context,ScoreActivity.class); 
              							context.startActivity(intent);
              						 }
              						});
              					builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {					  
              					 public void onClick(DialogInterface dialog, int which) {
              						 dialog.dismiss();
              					 }
              					});
              					builder.create().show();
                        }
                    } catch (ParseException e1) {  
                        // TODO Auto-generated catch block  
                        e1.printStackTrace();  
                    }
	            }
                catch (Exception eJson) {
                	////
                }
                /////
                
            }
        });
	}    
    
    @Override
    public void fillValues(int position, View convertView) {
    	ViewHolder holder = null;
    	
		
			holder = new ViewHolder();
			holder.TvZhaoPian=(TextView) convertView.findViewById(R.id.TvZhaoPian);
			holder.TvPingFen=(TextView) convertView.findViewById(R.id.TvPingFen);
			holder.TvQueKao=(TextView) convertView.findViewById(R.id.TvQueKao);
			holder.img = (ImageView) convertView.findViewById(R.id.itemImage);
			holder.itemTime = (TextView) convertView.findViewById(R.id.itemTime);
			holder.itemKaohao = (TextView) convertView.findViewById(R.id.itemKaohao);
			holder.itemXuehao = (TextView) convertView.findViewById(R.id.itemXuehao);
			holder.itemBanJi = (TextView) convertView.findViewById(R.id.itemBanJi);
			holder.itemZhuangtai = (TextView) convertView.findViewById(R.id.itemZhuangtai);
			holder.itemFenshu = (TextView) convertView.findViewById(R.id.itemFenshu);
			

		if (position < list.size())
		{
			final HashMap<String, Object> map = list.get(position);
			Object uid = map.get("U_ID");
			
			final String imgUrl = url+uid.toString();
			if (imgUrl != null && !imgUrl.equals("")) {
				
				// Use Ion's builder set the google_image on an ImageView from a URL

                // start with the ImageView
                Ion.with(holder.img)
                // use a placeholder google_image if it needs to load from the network
                .placeholder(R.drawable.username)
                .error(R.drawable.username)
                // load the url
                .load(imgUrl);
			}
			
			//holder.itemName.setText((String)map.get("itemName"));
			holder.itemTime.setText((String)map.get("itemTime"));
			holder.itemKaohao.setText((String)map.get("itemKaohao"));
			holder.itemXuehao.setText((String)map.get("itemXuehao"));
			holder.itemBanJi.setText((String)map.get("itemBanJi"));
			

			holder.itemZhuangtai.setText((String)map.get("itemZhuangtai"));
			final String sStatus = map.get("itemZhuangtai").toString();
			if(map.get("itemZhuangtai").equals("已考"))
			{				
				holder.itemZhuangtai.setTextColor(Color.parseColor("#55c439"));
			
				holder.TvQueKao.setVisibility(View.GONE);
				holder.TvPingFen.setText("查看");
			}
			else if(map.get("itemZhuangtai").equals("缺考"))
			{				
				holder.itemZhuangtai.setTextColor(Color.parseColor("#f5321e"));
				
				holder.TvQueKao.setVisibility(View.GONE);
				holder.TvPingFen.setText("评分");
			}
			else
			{
				holder.itemZhuangtai.setTextColor(Color.BLACK);
				holder.TvQueKao.setVisibility(View.VISIBLE);
				holder.TvPingFen.setText("评分");				
			}
			holder.itemFenshu.setText((String)map.get("itemFenshu"));
			
			holder.TvPingFen.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View view) {
	            	if (swipe != null)
    				{
    					swipe.close();
    				}
	            	if(sStatus.equals("已考"))
	            	{
	            		String userid = map.get("U_ID").toString();
	            		MainActivity activity = (MainActivity)context;
	            	    GlobalSetting myApp = (GlobalSetting)activity.getApplication();  
	                    myApp.gStudentId = userid;
	            		Intent intent =new Intent(context,ScoreViewSeeActivity.class); 
	        			context.startActivity(intent);
	            	}
	            	else
	            	{
	            		getSystemTime(map);
	            	}
	            }
	        });
	        
			holder.TvQueKao.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View view) {
	                Toast.makeText(context, "que kao111", Toast.LENGTH_SHORT).show();
	            }
	        });
		}
		
    }
}

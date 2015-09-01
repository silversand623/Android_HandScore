package com.example.webview;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.king.swipelibrary.SwipeAdapter;
import cn.king.swipelibrary.SwipeLayout;

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
        Toast.makeText(context, "positon is "+position, Toast.LENGTH_SHORT).show();
        final int pos = position;
        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
			@Override
			public void onClose(SwipeLayout layout) {

			}

			@Override
			public void onUpdate(SwipeLayout layout, int leftOffset,
					int topOffset) {
				//Toast.makeText(context, "click onUpdate", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onOpen(SwipeLayout layout) {
				Toast.makeText(context, "click open", Toast.LENGTH_SHORT).show();
				//layout.close();
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
				//Toast.makeText(context, "click onHandRelease", Toast.LENGTH_SHORT).show();
			}
		});
       
        v.findViewById(R.id.TvPingFen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "click delete", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {
    	ViewHolder holder = null;
    	
		//if (convertView == null) {
			holder = new ViewHolder();
			//convertView = LayoutInflater.from(context).inflate(
					//R.layout.activity_mainlist, null);
			holder.img = (ImageView) convertView.findViewById(R.id.itemImage);
			//holder.itemName = (TextView) convertView.findViewById(R.id.itemName);
			holder.itemTime = (TextView) convertView.findViewById(R.id.itemTime);
			holder.itemKaohao = (TextView) convertView.findViewById(R.id.itemKaohao);
			holder.itemXuehao = (TextView) convertView.findViewById(R.id.itemXuehao);
			holder.itemBanJi = (TextView) convertView.findViewById(R.id.itemBanJi);
			holder.itemZhuangtai = (TextView) convertView.findViewById(R.id.itemZhuangtai);
			holder.itemFenshu = (TextView) convertView.findViewById(R.id.itemFenshu);
			//convertView.setTag(holder);
		//} else {
			//holder = (ViewHolder) convertView.getTag();
		//}

		if (position < list.size())
		{
			HashMap<String, Object> map = list.get(position);
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
			if(map.get("itemZhuangtai").equals("ÒÑ¿¼"))
			{
				holder.itemZhuangtai.setTextColor(Color.parseColor("#55c439"));
			}
			else if(map.get("itemZhuangtai").equals("È±¿¼"))
			{
				holder.itemZhuangtai.setTextColor(Color.parseColor("#f5321e"));
			}
			else
			{
				holder.itemZhuangtai.setTextColor(Color.BLACK);
			}
			holder.itemFenshu.setText((String)map.get("itemFenshu"));
		}
		
    }
}

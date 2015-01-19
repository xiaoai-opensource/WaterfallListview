package cn.bitlove.waterfalllistview;

import java.util.ArrayList;

import cn.bitlove.waterfalllistview.widget.WaterfallListview;
import cn.bitlove.waterfalllistview.widget.WaterfallListview.IOnRefresh;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	int mTimes = 0;
	Context mContext = this;
	WaterfallListview listView;
	ArrayList<String> mData;
	BaseAdapter mAdapter;
	LayoutInflater mInflater;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mInflater = LayoutInflater.from(mContext);
		listView = (WaterfallListview) findViewById(R.id.waterfallListview1);
		mData = makeData();
		mAdapter = new BaseAdapter() {
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				LinearLayout layout = (LinearLayout) mInflater.inflate(R.layout.item_list, null);
				TextView tv =  (TextView) layout.findViewById(R.id.tv1);
			
				tv.setText(mData.get(position));
				
				return layout;
			}
			
			@Override
			public long getItemId(int position) {
				return position;
			}
			
			@Override
			public Object getItem(int position) {
				return mData.get(position);
			}
			
			@Override
			public int getCount() {
				return mData.size();
			}
		};
		listView.setAdapter(mAdapter);

		listView.setRefreshListener(new IOnRefresh() {
			
			@Override
			public void doRefresh() {
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						mTimes++;
						for(int i=0;i<10;i++){
							mData.add("新到请求数据 ,第" + mTimes +"次" + i);
						}
						mAdapter.notifyDataSetChanged();
						listView.completeRefresh();
						Toast.makeText(mContext, "刷新完成",Toast.LENGTH_SHORT).show();
					}
				}, 500);
				
			}
			
			@Override
			public void beforeRefresh() {
				Toast.makeText(mContext, "开始刷新数据",Toast.LENGTH_SHORT).show();
			}
		});
	
	}

	/**
	 * 提供初始化数据
	 * */
	public ArrayList<String> makeData(){
		ArrayList<String> arr = new ArrayList<String>();
		for(int i=0;i<30;i++){
			arr.add("测试条目 : " + i);
		}
		
		return arr;
	}
}

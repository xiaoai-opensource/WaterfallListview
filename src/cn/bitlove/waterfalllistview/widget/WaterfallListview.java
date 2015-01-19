package cn.bitlove.waterfalllistview.widget;

import cn.bitlove.waterfalllistview.R;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

/**
 * 瀑布流Listview
 * */
public class WaterfallListview extends ListView implements OnScrollListener {

	final private String tag="WaterfallListView";
	private Context mContext;
	private LayoutInflater mInflater;		
	final int REFRESH_IDEL=0;		//空闲中
	final int REFRESH_PREPARE=1;	//准备刷新
	final int REFRESH_ING=2;		//刷新中
	final int REFRESH_End=3;		//刷新完成
	private int mRefreshState = REFRESH_IDEL;		//刷新状态
	private int mScrollState;		//滚动状态

	private FrameLayout mFooterLayout;	//ListView 的footer
	private View mListFoot;		//底部刷新区域

	private IOnRefresh mRefreshListener = null;	//刷新监听器
	public WaterfallListview(Context context) {
		super(context);
	}
	public  WaterfallListview(Context context, AttributeSet attrs) {
		super(context,attrs);
		init();
	}
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		mScrollState = scrollState;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if(totalItemCount==0 || mScrollState==SCROLL_STATE_IDLE){
			return;
		}
		//最后一个item
		int lastItemCount = firstVisibleItem+visibleItemCount;
		if(lastItemCount==totalItemCount && mRefreshState==REFRESH_IDEL){
			prepareRefresh();
			doRefresh();
		}
	}

	private void init(){
		mContext = getContext();
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		initFooter();
		setOnScrollListener(this);
	}
	/**
	 * 设置刷新器
	 * */
	public void setRefreshListener(IOnRefresh refresher){
		mRefreshListener = refresher;
	}

	/**
	 * 初始化底部刷新内容
	 * */
	private void initFooter(){
		mListFoot = mInflater.inflate(R.layout.foot_waterfall_listview, null);
		mFooterLayout = new FrameLayout(mContext);
		addFooterView(mFooterLayout);
	}
	/**
	 * 准备刷新
	 * */
	private void prepareRefresh(){
		//Log.i(tag,"prepareRefresh");
		mRefreshState = REFRESH_PREPARE;
		ViewGroup vg = (ViewGroup) mListFoot.getParent();
		if(vg!=null){
			vg.removeView(mListFoot);
		}

		mFooterLayout.addView(mListFoot);

		if(mRefreshListener!=null){
			mRefreshListener.beforeRefresh();
		}
	}
	/**
	 * 执行刷新
	 * */
	private void doRefresh(){
		//Log.i(tag,"doRefresh");
		mRefreshState=REFRESH_ING;

		if(mRefreshListener!=null){
			mRefreshListener.doRefresh();
		}

	}
	/**
	 * 完成刷新
	 * */
	public void completeRefresh(){
		//Log.i(tag,"completeRefresh");
		mRefreshState = REFRESH_IDEL;
		mFooterLayout.removeAllViews();
	}
	
	public interface IOnRefresh{
		public void beforeRefresh();
		/**
		 * 执行完doRefresh操作后，必须调用completeRefresh方法来清理刷新状态
		 * */
		public void doRefresh();
	}
}

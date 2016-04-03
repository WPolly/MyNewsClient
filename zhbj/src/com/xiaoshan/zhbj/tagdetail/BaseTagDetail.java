package com.xiaoshan.zhbj.tagdetail;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.viewpagerindicator.CirclePageIndicator;
import com.xiaoshan.zhbj.NewsDetailsActivity;
import com.xiaoshan.zhbj.R;
import com.xiaoshan.zhbj.domai.NewsTabDetailData;
import com.xiaoshan.zhbj.domai.NewsTabDetailData.ListNews;
import com.xiaoshan.zhbj.global.GlobalConstants;
import com.xiaoshan.zhbj.ui.RefreshListView;
import com.xiaoshan.zhbj.ui.RefreshListView.OnLoadMoreListener;
import com.xiaoshan.zhbj.ui.RefreshListView.OnRefreshListener;
import com.xiaoshan.zhbj.ui.TopNewsViewPager;

public class BaseTagDetail {

	public Activity mActivity;
	public TopNewsViewPager vp;
	public RefreshListView lv;
	public List<String> mUrls;
	public TextView tvTopNewsTitle;
	private PagerAdapter mTopNewsAdapter;
	public View mView;
	private NewsTabDetailData newsTabDetailData;
	private CirclePageIndicator indicator;
	private ListNewsAdapter mListNewsAdapter;
	private View headerView;
	private String mUrl;
	private ArrayList<ListNews> mListNews;
	private String mMore;
	private String partMore;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int curentItem = vp.getCurrentItem();
			curentItem++;
			if (curentItem < newsTabDetailData.data.topnews.size()) {
				vp.setCurrentItem(curentItem);
			} else {
				vp.setCurrentItem(0);
			}
			
			mHandler.sendEmptyMessageDelayed(0, 3000);
		}
	};

	public BaseTagDetail(Activity mActivity) {
		this.mActivity = mActivity;
		initView();
	}

	public void initView() {
		mView = View.inflate(mActivity,
				R.layout.viewpager_base_news_center_item, null);
		headerView = View.inflate(mActivity, R.layout.header_list_news, null);
		vp = (TopNewsViewPager) headerView.findViewById(R.id.pager);
		lv = (RefreshListView) mView.findViewById(R.id.lv_news_details);
		lv.addHeaderView(headerView);
		lv.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void refresh() {
				getDataFromServer(true);
			}
		});

		lv.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void loadMore() {
				getMoreDataFromServer();
			}
		});
		tvTopNewsTitle = (TextView) headerView
				.findViewById(R.id.tv_top_news_title);
		indicator = (CirclePageIndicator) headerView
				.findViewById(R.id.indicator);
	}

	public void getDataFromServer(final boolean viaRefresh) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, mUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				// Log.i(TAG, "开始从服务器获取数据.");
				String result = responseInfo.result;
				parseData(result);
				// Log.i(TAG, "解析完成");
				if (viaRefresh) {
					lv.completedRefresh(true);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
				error.printStackTrace();
				if (viaRefresh) {
					lv.completedRefresh(false);
				}
			}
		});
	}

	protected void parseData(String result) {
		Gson gson = new Gson();
		newsTabDetailData = gson.fromJson(result, NewsTabDetailData.class);
		mListNews = newsTabDetailData.data.news;
		partMore = newsTabDetailData.data.more;
		String topNewsTitle = newsTabDetailData.data.topnews.get(0).title;
		tvTopNewsTitle.setText(topNewsTitle);
		mTopNewsAdapter = new TabNewsDetailAdapter();
		mListNewsAdapter = new ListNewsAdapter();
		vp.setAdapter(mTopNewsAdapter);
		lv.setAdapter(mListNewsAdapter);
		mHandler.removeMessages(0);
		mHandler.sendEmptyMessageDelayed(0, 3000);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String url = mListNews.get(position).url;
				Intent intent = new Intent(mActivity, NewsDetailsActivity.class);
				intent.putExtra("url", url);
				mActivity.startActivity(intent);
			}
		});
		indicator.setViewPager(vp);
		indicator.setSnap(true);
		indicator.setOnPageChangeListener(new OnPageChangeListener() {// 相当重要

					@Override
					public void onPageSelected(int position) {
						String topNewsTitle = newsTabDetailData.data.topnews
								.get(position).title;
						tvTopNewsTitle.setText(topNewsTitle);
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {

					}

					@Override
					public void onPageScrollStateChanged(int arg0) {

					}
				});
	}

	private void getMoreDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		if (TextUtils.isEmpty(partMore)) {
			lv.completedLoadMore(RefreshListView.LOADNOTHING);
		} else {
			mMore = GlobalConstants.ONLINE_URL + partMore;
			httpUtils.send(HttpMethod.GET, mMore,
					new RequestCallBack<String>() {

						@Override
						public void onSuccess(ResponseInfo<String> responseInfo) {
							// Log.i(TAG, "开始从服务器获取数据.");
							String result = responseInfo.result;
							Gson gson = new Gson();
							NewsTabDetailData newsTabDetailData = new NewsTabDetailData();
							newsTabDetailData = gson.fromJson(result,
									NewsTabDetailData.class);
							ArrayList<ListNews> moreNews = new ArrayList<ListNews>();
							moreNews = newsTabDetailData.data.news;
							mListNews.addAll(moreNews);
							mListNewsAdapter.notifyDataSetChanged();
							partMore = newsTabDetailData.data.more;

							lv.completedLoadMore(RefreshListView.LOADSUCCEED);
							// Log.i(TAG, "解析完成");

						}

						@Override
						public void onFailure(HttpException error, String msg) {
							Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT)
									.show();
							error.printStackTrace();
							lv.completedLoadMore(RefreshListView.LOADFAILED);
						}
					});
		}

	}

	class TabNewsDetailAdapter extends PagerAdapter {

		@Override
		public int getCount() {

			return newsTabDetailData.data.topnews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView iv = new ImageView(mActivity);
			BitmapUtils utils = new BitmapUtils(mActivity);
			utils.configDefaultLoadingImage(R.drawable.topnews_item_default);
			String imageUrl = newsTabDetailData.data.topnews.get(position).topimage;

			utils.display(iv, imageUrl);
			container.addView(iv);

			return iv;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

	class ListNewsAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mListNews.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			ViewHolder holder;

			if (convertView != null) {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(mActivity, R.layout.list_news_item, null);
				holder = new ViewHolder();
				holder.ivListNewsImg = (ImageView) view
						.findViewById(R.id.iv_list_news);
				holder.tvListNewsTitle = (TextView) view
						.findViewById(R.id.tv_list_news_title);
				holder.tvListNewsDate = (TextView) view
						.findViewById(R.id.tv_list_news_date);
				view.setTag(holder);
			}

			BitmapUtils utils = new BitmapUtils(mActivity);
			utils.display(holder.ivListNewsImg,
					mListNews.get(position).listimage);
			holder.tvListNewsTitle.setText(mListNews.get(position).title);
			holder.tvListNewsDate.setText(mListNews.get(position).pubdate);

			return view;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

	}

	class ViewHolder {
		ImageView ivListNewsImg;
		TextView tvListNewsTitle;
		TextView tvListNewsDate;
	}

	public void setCurrentUrl(String url) {
		mUrl = url;
	}
}

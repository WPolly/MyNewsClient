package com.xiaoshan.zhbj;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.viewpagerindicator.TabPageIndicator;
import com.xiaoshan.zhbj.domai.NewsCenterData;
import com.xiaoshan.zhbj.global.GlobalConstants;
import com.xiaoshan.zhbj.tagdetail.BaseTagDetail;

public class NewsCenterActivity extends Activity {

	protected static final String TAG = "NewsCenterActivity";
	private ImageButton ibBack;
	private NewsCenterData newsCenterData;
	private int viewpagerCount;
	public PagerAdapter adapter;
	private ArrayList<String> mUrls;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_center);
		ibBack = (ImageButton) findViewById(R.id.ib_back);
		ibBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		initData();

		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);

		TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(pager);
	}

	private void initData() {
		// Intent intent = getIntent();
		// newsCenterData = (NewsCenterData)
		// intent.getSerializableExtra("data");
		newsCenterData = GlobalConstants.newsCenterData;
		viewpagerCount = newsCenterData.data.get(0).children.size();
		mUrls = new ArrayList<String>();
		int len = newsCenterData.data.get(0).children.size();
		for (int i = 0; i < len; i++) {
			String url = GlobalConstants.ONLINE_URL
					+ newsCenterData.data.get(0).children.get(i).url;
			System.out.println(url);
			mUrls.add(url);
		}
		adapter = new NewsCenterAdapter();
	}

	class NewsCenterAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return viewpagerCount;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return newsCenterData.data.get(0).children.get(position).title;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			BaseTagDetail detail = new BaseTagDetail(NewsCenterActivity.this);
			View view = detail.mView;
			String url = mUrls.get(position);
			detail.setCurrentUrl(url);
			detail.getDataFromServer(false);
			container.addView(view);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}
}

package com.xiaoshan.zhbj;

import java.util.ArrayList;
import java.util.List;

import com.xiaoshan.zhbj.utils.PrefUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;

public class GuideActivity extends Activity {

	private ViewPager vpGuide;
	private List<ImageView> ivs;
	private PagerAdapter adapter;
	private Button btEnterHome;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		vpGuide = (ViewPager) findViewById(R.id.vp_guide);
		btEnterHome = (Button) findViewById(R.id.bt_enter_home);
		initView();
		adapter = new MyPagerAdapter();
		vpGuide.setAdapter(adapter);
		vpGuide.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				if (position == ivs.size() - 1) {
					btEnterHome.setVisibility(View.VISIBLE);
					ScaleAnimation sAnimation = new ScaleAnimation(0.2f, 2.0f,
							0.2f, 2.0f, Animation.RELATIVE_TO_SELF, 0.5f,
							Animation.RELATIVE_TO_SELF, 0.5f);
					sAnimation.setRepeatCount(5);
					sAnimation.setRepeatMode(Animation.REVERSE);
					sAnimation.setDuration(80);
					btEnterHome.startAnimation(sAnimation);
				} else {
					btEnterHome.setVisibility(View.INVISIBLE);
				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {

			}

			@Override
			public void onPageScrollStateChanged(int state) {
				
			}
		});
		
		btEnterHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				PrefUtil.setBoolean(GuideActivity.this, "isguided", true);
				Intent intent = new Intent(GuideActivity.this,
						MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	private void initView() {
		int[] imgIds = { R.drawable.guide_1, R.drawable.guide_2,
				R.drawable.guide_3 };
		ivs = new ArrayList<ImageView>();

		for (int i : imgIds) {
			ImageView iv = new ImageView(this);
			iv.setBackgroundResource(i);
			ivs.add(iv);
		}
	}

	class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return ivs.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(ivs.get(position));
			return ivs.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}
}

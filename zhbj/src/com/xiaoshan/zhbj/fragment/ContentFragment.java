package com.xiaoshan.zhbj.fragment;

import java.util.ArrayList;
import java.util.List;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.xiaoshan.zhbj.MainActivity;
import com.xiaoshan.zhbj.R;
import com.xiaoshan.zhbj.pageoperator.BasePagerOperator;
import com.xiaoshan.zhbj.pageoperator.GovPagerOperator;
import com.xiaoshan.zhbj.pageoperator.HomePagerOperator;
import com.xiaoshan.zhbj.pageoperator.NewsCenterPagerOperator;
import com.xiaoshan.zhbj.pageoperator.SettingPagerOperator;
import com.xiaoshan.zhbj.pageoperator.SmartServicePagerOperator;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class ContentFragment extends BaseFragment {

	private List<BasePagerOperator> operators;
	private View view;
	private ViewPager vpContent;
	private RadioGroup rgBottomTag;
	protected boolean flag = true;

	@Override
	public View initView() {
		view = View.inflate(mActivity, R.layout.fragment_content, null);
		vpContent = (ViewPager) view.findViewById(R.id.vp_content);
		rgBottomTag = (RadioGroup) view.findViewById(R.id.rg_bottom_tags);
		return view;
	}

	@Override
	public void initData() {
		final int[] rbIds = { R.id.rb_home, R.id.rb_news, R.id.rb_service,
				R.id.rb_gov, R.id.rb_setting };
		operators = new ArrayList<BasePagerOperator>();
		operators.add(new HomePagerOperator(mActivity));
		operators.add(new NewsCenterPagerOperator(mActivity));
		operators.add(new SmartServicePagerOperator(mActivity));
		operators.add(new GovPagerOperator(mActivity));
		operators.add(new SettingPagerOperator(mActivity));
		vpContent.setAdapter(new MyPagerAdapter());

		vpContent.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				System.out.println("" + position);
				flag = false;
				rgBottomTag.check(rbIds[position]);
				operators.get(position).initData();
				MainActivity activity = (MainActivity) mActivity;
				SlidingMenu menu = activity.getSlidingMenu();
				if (position == 0) {
					menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
					operators.get(position).ibMainMenu
							.setVisibility(View.VISIBLE);
				} else {
					menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
					operators.get(position).ibMainMenu
							.setVisibility(View.INVISIBLE);
				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				//System.out.println("" + position + "\n" + positionOffset + "\n"
						//+ positionOffsetPixels);
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});

		rgBottomTag.check(rbIds[0]);
		operators.get(0).initData();
		rgBottomTag.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (flag) {
					switch (checkedId) {
					case R.id.rb_home:
						vpContent.setCurrentItem(0, false);
						break;

					case R.id.rb_news:
						vpContent.setCurrentItem(1, false);
						operators.get(1).initData();
						break;

					case R.id.rb_service:
						vpContent.setCurrentItem(2, false);
						operators.get(2).initData();
						break;

					case R.id.rb_gov:
						vpContent.setCurrentItem(3, false);
						operators.get(3).initData();
						break;

					case R.id.rb_setting:
						vpContent.setCurrentItem(4, false);
						operators.get(4).initData();
						break;

					default:
						break;
					}
				}
				flag = true;
			}
		});
	}

	class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return operators.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = operators.get(position).mView;
			container.addView(view);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}

}

package com.xiaoshan.zhbj.pageoperator;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

public class NewsCenterPagerOperator extends BasePagerOperator {

	public NewsCenterPagerOperator(Activity mActivity) {
		super(mActivity);
		tvTitle.setText("新闻");
	}
	
	@Override
	public void initData() {
		TextView tv = new TextView(mActivity);
		tv.setText("新闻联播");
		tv.setTextColor(Color.RED);
		tv.setTextSize(50);
		tv.setGravity(Gravity.CENTER);
		flContainer.addView(tv);
	}

}

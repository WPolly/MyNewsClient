package com.xiaoshan.zhbj.pageoperator;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

public class SettingPagerOperator extends BasePagerOperator {

	public SettingPagerOperator(Activity mActivity) {
		super(mActivity);
		tvTitle.setText("����");
	}
	
	@Override
	public void initData() {
		TextView tv = new TextView(mActivity);
		tv.setText("˽�˶���");
		tv.setTextColor(Color.RED);
		tv.setTextSize(50);
		tv.setGravity(Gravity.CENTER);
		flContainer.addView(tv);
	}

}

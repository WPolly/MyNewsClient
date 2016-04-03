package com.xiaoshan.zhbj.pageoperator;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

public class SmartServicePagerOperator extends BasePagerOperator {

	public SmartServicePagerOperator(Activity mActivity) {
		super(mActivity);
		tvTitle.setText("����");
	}
	
	@Override
	public void initData() {
		TextView tv = new TextView(mActivity);
		tv.setText("�ǻ��������");
		tv.setTextColor(Color.RED);
		tv.setTextSize(50);
		tv.setGravity(Gravity.CENTER);
		flContainer.addView(tv);
	}

}

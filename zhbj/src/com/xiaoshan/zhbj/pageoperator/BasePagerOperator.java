package com.xiaoshan.zhbj.pageoperator;

import com.xiaoshan.zhbj.R;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

public class BasePagerOperator {
	public Activity mActivity;
	public View mView;
	public TextView tvTitle;
	public ImageButton ibMainMenu;
	public FrameLayout flContainer;

	public BasePagerOperator(Activity mActivity) {
		this.mActivity = mActivity;
		initView();
	}

	private void initView() {
		mView = View.inflate(mActivity, R.layout.base_pager, null);
		tvTitle = (TextView) mView.findViewById(R.id.tv_title);
		ibMainMenu = (ImageButton) mView.findViewById(R.id.ib_main_menu);
		flContainer = (FrameLayout) mView.findViewById(R.id.fl_container);
	}
	
	public void initData() {
		
	}
	
	
}

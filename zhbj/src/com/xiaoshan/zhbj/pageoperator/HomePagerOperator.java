package com.xiaoshan.zhbj.pageoperator;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xiaoshan.zhbj.MainActivity;
import com.xiaoshan.zhbj.domai.NewsCenterData;
import com.xiaoshan.zhbj.fragment.LeftMenuFragment;
import com.xiaoshan.zhbj.global.GlobalConstants;

public class HomePagerOperator extends BasePagerOperator {

	public HomePagerOperator(Activity mActivity) {
		super(mActivity);
		tvTitle.setText("智慧北京");
		ibMainMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MainActivity activity = (MainActivity) HomePagerOperator.this.mActivity;
				activity.getSlidingMenu().toggle();
			}
		});
	}

	@Override
	public void initData() {
		// System.out.println("首页初始化数据..");
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, GlobalConstants.ONLINE_URL
				+ "/categories.json", new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = (String) responseInfo.result;
				parseData(result);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
				error.printStackTrace();
			}
		});
	}

	protected void parseData(String result) {
		Gson gson = new Gson();
		NewsCenterData centerData = gson.fromJson(result, NewsCenterData.class);
		GlobalConstants.newsCenterData = centerData;
		MainActivity mainActivity = (MainActivity) mActivity;
		LeftMenuFragment leftMenuFragment = mainActivity.getLeftMenuFragment();
		leftMenuFragment.setLeftMenuData(centerData);
	}

}

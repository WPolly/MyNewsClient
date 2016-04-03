package com.xiaoshan.zhbj;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PicGroupDetailsActivity extends Activity {
	private WebView wvPicGroupDetails;
	private ProgressBar pbLoadingWeb;
	private TextView tvPicTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pic_group_details);
		initView();
		initData();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initView() {
		wvPicGroupDetails = (WebView) findViewById(R.id.wv_pic_group_details);
		pbLoadingWeb = (ProgressBar) findViewById(R.id.pb_loading_web);
		tvPicTitle = (TextView) findViewById(R.id.tv_pic_title);
		WebSettings settings = wvPicGroupDetails.getSettings();
		settings.setJavaScriptEnabled(true);
	}

	private void initData() {
		String url = getIntent().getStringExtra("url");
		String picTitle = getIntent().getStringExtra("title");
		tvPicTitle.setText(picTitle);
		if (TextUtils.isEmpty(url)) {
			url = "http://www.baidu.com/";
		}

		wvPicGroupDetails.loadUrl(url);
		wvPicGroupDetails.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				pbLoadingWeb.setVisibility(View.VISIBLE);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				pbLoadingWeb.setVisibility(View.INVISIBLE);
			}

		});
		
		wvPicGroupDetails.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onReceivedTitle(WebView view, String title) {
				
			}
		});
	}
}

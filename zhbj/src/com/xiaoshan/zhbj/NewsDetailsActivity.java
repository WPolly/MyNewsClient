package com.xiaoshan.zhbj;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

public class NewsDetailsActivity extends Activity {

	private ImageButton ibBack, ibShare, ibSetTextSize;
	private WebView wvNewsDetails;
	private ProgressBar pbLoadingWeb;
	private WebSettings settings;
	private SharedPreferences sp;
	private int[] mTextSize;
	protected int mCurrentSize;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_details);
		initView();
		sp = getSharedPreferences("config", MODE_PRIVATE);
		mCurrentSize = sp.getInt("currentSize", 2);
		mTextSize = new int[] { 200, 150, 100, 70, 40 };
		String url = getIntent().getStringExtra("url");
		settings = wvNewsDetails.getSettings();
		settings.setTextZoom(mTextSize[mCurrentSize]);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setJavaScriptEnabled(true);
		String jUrl = getIntent().getStringExtra("Jurl");
		if (TextUtils.isEmpty(jUrl)) {
			url = "http://www.hao123.com/";
		} else {
			url = jUrl;
		}
		wvNewsDetails.loadUrl(url);
		wvNewsDetails.setWebViewClient(new WebViewClient() {

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

		wvNewsDetails.setWebChromeClient(new WebChromeClient() {

		});
		
		
	}

	private void initView() {
		ibBack = (ImageButton) findViewById(R.id.ib_back);
		ibSetTextSize = (ImageButton) findViewById(R.id.ib_text_size);
		ibShare = (ImageButton) findViewById(R.id.ib_share);
		wvNewsDetails = (WebView) findViewById(R.id.wv_news_details);
		pbLoadingWeb = (ProgressBar) findViewById(R.id.pb_loading_web);
		ibBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (wvNewsDetails.canGoBack()) {
					wvNewsDetails.goBack();
				} else {
					finish();
				}
			}
		});

		ibSetTextSize.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showChooseSizeDialog();
			}
		});

		ibShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showShare();
			}
		});
	}

	protected void showChooseSizeDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("设置字体大小");
		String[] items = { "超大号", "大号", "正常", "小号", "超小号" };
		builder.setSingleChoiceItems(items, mCurrentSize,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						mCurrentSize = which;
					}

				});

		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				settings.setTextZoom(mTextSize[mCurrentSize]);
				Editor editor = sp.edit();
				editor.putInt("currentSize", mCurrentSize);
				editor.commit();
			}
		});

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				mCurrentSize = sp.getInt("currentSize", 2);
			}

		});
		builder.show();
	}

	private void showShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		
		oks.setTheme(OnekeyShareTheme.SKYBLUE);
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(getString(R.string.share));
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl("http://sharesdk.cn");
		// text是分享文本，所有平台都需要这个字段
		oks.setText("真没意思");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		oks.setImagePath(Environment.getExternalStorageDirectory().getPath()
				+ "/share.jpg");// 确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://sharesdk.cn");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl(wvNewsDetails.getUrl());

		// 启动分享GUI
		oks.show(this);
	}

	@Override
	public void onBackPressed() {
		if (wvNewsDetails.canGoBack()) {
			wvNewsDetails.goBack();
		} else {
			finish();
		}
	}
}

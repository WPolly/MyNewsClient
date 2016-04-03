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
		builder.setTitle("���������С");
		String[] items = { "�����", "���", "����", "С��", "��С��" };
		builder.setSingleChoiceItems(items, mCurrentSize,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						mCurrentSize = which;
					}

				});

		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				settings.setTextZoom(mTextSize[mCurrentSize]);
				Editor editor = sp.edit();
				editor.putInt("currentSize", mCurrentSize);
				editor.commit();
			}
		});

		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

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
		// �ر�sso��Ȩ
		oks.disableSSOWhenAuthorize();

		// ����ʱNotification��ͼ������� 2.5.9�Ժ�İ汾�����ô˷���
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title���⣬ӡ��ʼǡ����䡢��Ϣ��΢�š���������QQ�ռ�ʹ��
		oks.setTitle(getString(R.string.share));
		// titleUrl�Ǳ�����������ӣ�������������QQ�ռ�ʹ��
		oks.setTitleUrl("http://sharesdk.cn");
		// text�Ƿ����ı�������ƽ̨����Ҫ����ֶ�
		oks.setText("��û��˼");
		// imagePath��ͼƬ�ı���·����Linked-In�����ƽ̨��֧�ִ˲���
		oks.setImagePath(Environment.getExternalStorageDirectory().getPath()
				+ "/share.jpg");// ȷ��SDcard������ڴ���ͼƬ
		// url����΢�ţ��������Ѻ�����Ȧ����ʹ��
		oks.setUrl("http://sharesdk.cn");
		// comment���Ҷ�������������ۣ�������������QQ�ռ�ʹ��
		oks.setComment("���ǲ��������ı�");
		// site�Ƿ�������ݵ���վ���ƣ�����QQ�ռ�ʹ��
		oks.setSite(getString(R.string.app_name));
		// siteUrl�Ƿ�������ݵ���վ��ַ������QQ�ռ�ʹ��
		oks.setSiteUrl(wvNewsDetails.getUrl());

		// ��������GUI
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

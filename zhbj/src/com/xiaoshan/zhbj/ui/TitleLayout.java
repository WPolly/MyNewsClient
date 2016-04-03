package com.xiaoshan.zhbj.ui;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.xiaoshan.zhbj.R;

public class TitleLayout extends RelativeLayout {

	public TitleLayout(Context context) {
		super(context);
	}

	public TitleLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		inflate(context, R.layout.title_bar, this);
		ImageButton ibBack = (ImageButton) findViewById(R.id.ib_back);
		ibBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((Activity) getContext()).finish();
			}
		});
	}
	
	public TitleLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
}

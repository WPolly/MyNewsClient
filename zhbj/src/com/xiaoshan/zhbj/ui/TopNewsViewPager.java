package com.xiaoshan.zhbj.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class TopNewsViewPager extends ViewPager {
	int startX;
	int startY;

	public TopNewsViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TopNewsViewPager(Context context) {
		super(context);
	}

	/**
	 * �¼��ַ�, ���󸸿ؼ������ڿؼ��Ƿ������¼� 1. �һ�, �����ǵ�һ��ҳ��, ��Ҫ���ؼ����� 2. ��, ���������һ��ҳ��, ��Ҫ���ؼ�����
	 * 3. ���»���, ��Ҫ���ؼ�����
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			getParent().requestDisallowInterceptTouchEvent(false);// ��Ҫ����,
																	// ������Ϊ�˱�֤ACTION_MOVE����
			startX = (int) ev.getRawX();
			startY = (int) ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:

			int endX = (int) ev.getRawX();
			int endY = (int) ev.getRawY();

			if (Math.abs(endX - startX) > Math.abs(endY - startY)) {// ���һ���
				getParent().requestDisallowInterceptTouchEvent(true);
			} else {// ���»���
				getParent().requestDisallowInterceptTouchEvent(false);
			}

			break;

		default:
			break;
		}

		return super.dispatchTouchEvent(ev);
	}
}

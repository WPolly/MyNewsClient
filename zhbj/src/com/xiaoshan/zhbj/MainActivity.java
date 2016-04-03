package com.xiaoshan.zhbj;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.xiaoshan.zhbj.fragment.ContentFragment;
import com.xiaoshan.zhbj.fragment.LeftMenuFragment;

public class MainActivity extends SlidingFragmentActivity {
	private static final String CONTENTFRAGMENT = "contentfragment";
	private static final String LEFTFRAGMENT = "leftfragment";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setBehindContentView(R.layout.left_menu);
		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT);
		sm.setBehindOffsetRes(R.dimen.sliding_offset);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow_sliding_menu);
		initFragment();
	}

	private void initFragment() {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.fl_main, new ContentFragment(),
				CONTENTFRAGMENT);
		fragmentTransaction.replace(R.id.fl_left_menu, new LeftMenuFragment(),
				LEFTFRAGMENT);
		fragmentTransaction.commit();
	}
	
	public LeftMenuFragment getLeftMenuFragment() {
		FragmentManager fragmentManager = getFragmentManager();
		LeftMenuFragment fragment = (LeftMenuFragment) fragmentManager.findFragmentByTag(LEFTFRAGMENT);
		return fragment;
	}

}

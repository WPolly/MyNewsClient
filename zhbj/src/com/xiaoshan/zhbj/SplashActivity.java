package com.xiaoshan.zhbj;

import com.xiaoshan.zhbj.utils.PrefUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

public class SplashActivity extends Activity {

	private RelativeLayout rlSplash;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		rlSplash = (RelativeLayout) findViewById(R.id.rl_splash_root);
		executeAnimation();
	}

	private void executeAnimation() {
		RotateAnimation rotationAnim = new RotateAnimation(0, 720,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);

		rotationAnim.setDuration(1000);
		rotationAnim.setFillAfter(true);

		ScaleAnimation scaleAnimation = new ScaleAnimation(0.1f, 1.0f, 0.3f,
				1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);

		scaleAnimation.setDuration(1000);

		AlphaAnimation alphaAnimation = new AlphaAnimation(0.2f, 1.0f);
		alphaAnimation.setDuration(3000);

		AnimationSet set = new AnimationSet(false);
		set.addAnimation(alphaAnimation);
		set.addAnimation(scaleAnimation);
		set.addAnimation(rotationAnim);
		set.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				boolean isGuided = PrefUtil.getBoolean(SplashActivity.this,
						"isguided");
				if (!isGuided) {
					Intent intent = new Intent(SplashActivity.this,
							GuideActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(SplashActivity.this,
							MainActivity.class);
					startActivity(intent);
				}

				finish();
			}
		});

		rlSplash.startAnimation(set);
	}
}

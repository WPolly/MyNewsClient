package com.xiaoshan.zhbj.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoshan.zhbj.R;

public class RefreshListView extends ListView implements
		android.widget.AdapterView.OnItemClickListener {

	private static final int STATE_PULL_REFRESH = 0;
	private static final int STATE_RELEASE_REFRESH = 1;
	private static final int STATE_REFRESHING = 2;

	public static final int LOADSUCCEED = 3;
	public static final int LOADNOTHING = 4;
	public static final int LOADFAILED = 5;

	private View headerView, footerView;
	private ImageView ivRefreshArrow;
	private TextView tvRefreshState, tvRefreshTime;
	private ProgressBar pbRefreshing;
	private RotateAnimation upAnimation, downAnimation;

	private int measuredHeaderHeight, measuredFooterHeight;
	private int mRefreshState = STATE_PULL_REFRESH;
	private int startY;
	private OnRefreshListener mRefreshListener;
	private OnLoadMoreListener mLoadMoreListenr;
	private android.widget.AdapterView.OnItemClickListener mItemClickListener;
	private boolean mMoveFlag = true;

	public RefreshListView(Context context) {
		super(context);
		initHeaderView();
		initFooterView();
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeaderView();
		initFooterView();
	}

	public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initHeaderView();
		initFooterView();
	}

	private void initHeaderView() {
		headerView = inflate(getContext(), R.layout.refresh_listview_header,
				null);
		ivRefreshArrow = (ImageView) headerView
				.findViewById(R.id.iv_refresh_arrow);
		pbRefreshing = (ProgressBar) headerView.findViewById(R.id.pb_refresh);
		tvRefreshState = (TextView) headerView
				.findViewById(R.id.tv_refresh_state);
		tvRefreshTime = (TextView) headerView
				.findViewById(R.id.tv_refresh_time);
		initAnimation();

		headerView.measure(0, 0);
		measuredHeaderHeight = headerView.getMeasuredHeight();
		headerView.setPadding(0, -measuredHeaderHeight, 0, 0);
		this.addHeaderView(headerView);
	}

	private void initFooterView() {
		footerView = inflate(getContext(), R.layout.refresh_listview_footer,
				null);
		footerView.measure(0, 0);
		measuredFooterHeight = footerView.getMeasuredHeight();
		footerView.setPadding(0, 0, 0, 0);
		this.addFooterView(footerView);
		setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == SCROLL_STATE_FLING
						|| scrollState == SCROLL_STATE_IDLE) {
					if (getLastVisiblePosition() == (getCount() - 1)) {
						mLoadMoreListenr.loadMore();
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startY = (int) ev.getRawY();
			System.out.println(startY);
			break;

		case MotionEvent.ACTION_MOVE:
			int endY = (int) ev.getRawY();
			if (mMoveFlag) {
				if (getFirstVisiblePosition() == 0) {
					startY = endY;
					mMoveFlag = false;
				}
			}

			int dy = endY - startY;
			// System.out.println(endY);

			if (mRefreshState == STATE_REFRESHING) {
				break;
			}
			if (dy >= 0 && getFirstVisiblePosition() == 0) {
				int padding = dy - measuredHeaderHeight;
				headerView.setPadding(0, padding, 0, 0);

				if (padding > 0 && mRefreshState == STATE_PULL_REFRESH) {
					mRefreshState = STATE_RELEASE_REFRESH;
					changeRefreshState();
				} else if (padding <= 0
						&& mRefreshState == STATE_RELEASE_REFRESH) {
					mRefreshState = STATE_PULL_REFRESH;
					changeRefreshState();
				}

				return true;
			}
		case MotionEvent.ACTION_UP:
			mMoveFlag = true;

			if (mRefreshState == STATE_PULL_REFRESH) {
				headerView.setPadding(0, -measuredHeaderHeight, 0, 0);
			} else if (mRefreshState == STATE_RELEASE_REFRESH) {
				headerView.setPadding(0, 0, 0, 0);
				mRefreshState = STATE_REFRESHING;
				changeRefreshState();
				if (mRefreshListener != null) {
					mRefreshListener.refresh();
				}
			}

		default:
			break;
		}

		return super.onTouchEvent(ev);
	}

	private void changeRefreshState() {
		switch (mRefreshState) {
		case STATE_PULL_REFRESH:
			tvRefreshState.setText("下拉刷新");
			ivRefreshArrow.startAnimation(downAnimation);
			ivRefreshArrow.setVisibility(View.VISIBLE);
			pbRefreshing.setVisibility(View.INVISIBLE);
			break;

		case STATE_RELEASE_REFRESH:
			tvRefreshState.setText("松开立即刷新");
			ivRefreshArrow.startAnimation(upAnimation);
			break;

		case STATE_REFRESHING:
			tvRefreshState.setText("正在刷新...");
			ivRefreshArrow.clearAnimation();
			ivRefreshArrow.setVisibility(View.INVISIBLE);
			pbRefreshing.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
	}

	private void initAnimation() {
		upAnimation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		upAnimation.setDuration(50);
		upAnimation.setFillAfter(true);
		downAnimation = new RotateAnimation(-180, -360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		downAnimation.setDuration(50);
		downAnimation.setFillAfter(true);
	}

	public void setOnRefreshListener(OnRefreshListener listener) {
		mRefreshListener = listener;
	}

	public interface OnRefreshListener {
		public void refresh();
	}

	public void setOnLoadMoreListener(OnLoadMoreListener listener) {
		mLoadMoreListenr = listener;
	}

	public interface OnLoadMoreListener {
		public void loadMore();
	}

	public void completedRefresh(boolean flag) {
		headerView.setPadding(0, -measuredHeaderHeight, 0, 0);
		mRefreshState = STATE_PULL_REFRESH;
		changeRefreshState();
		if (flag) {
			tvRefreshTime.setText("最后刷新时间: " + getRefreshedTime());
			Toast.makeText(getContext(), "Y(^_^)Y刷新成功!", Toast.LENGTH_SHORT)
					.show();
		} else {
			Toast.makeText(getContext(), "-_-。sorry！刷新失败,请检查网络后,重新刷新.",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void completedLoadMore(int resultCode) {

		switch (resultCode) {
		case LOADSUCCEED:
			Toast.makeText(getContext(), "Y(^_^)Y加载更多成功!", Toast.LENGTH_SHORT)
					.show();
			break;

		case LOADFAILED:
			Toast.makeText(getContext(), "-_-。sorry！加载失败,请检查网络后,重新加载.",
					Toast.LENGTH_SHORT).show();
			footerView.setPadding(0, -measuredFooterHeight, 0, 0);

		case LOADNOTHING:
			Toast.makeText(getContext(), "没有更多了", Toast.LENGTH_SHORT).show();
			footerView.setPadding(0, -measuredFooterHeight, 0, 0);

		default:
			break;
		}

	}

	private String getRefreshedTime() {
		SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		String currentTime = format.format(new Date());
		return currentTime;
	}

	/**
	 * 以下代码,有点难懂
	 */
	@Override
	public void setOnItemClickListener(
			android.widget.AdapterView.OnItemClickListener listener) {
		super.setOnItemClickListener(this);
		mItemClickListener = listener;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (mItemClickListener != null) {
			mItemClickListener.onItemClick(parent, view, position
					- getHeaderViewsCount(), id);
		}
	}

}

package com.xiaoshan.zhbj.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.xiaoshan.zhbj.InteractActivity;
import com.xiaoshan.zhbj.NewsCenterActivity;
import com.xiaoshan.zhbj.PictureGroupActivity;
import com.xiaoshan.zhbj.R;
import com.xiaoshan.zhbj.SubjectActivity;
import com.xiaoshan.zhbj.domai.NewsCenterData;
import com.xiaoshan.zhbj.domai.NewsCenterData.MenuData;

public class LeftMenuFragment extends BaseFragment {

	private View view;
	private ArrayList<MenuData> menuDatas;
	private ListView lvLeftMenu;
	private String[] mTempMenus = {"新闻", "专题", "组图", "互动"};

	// private NewsCenterData newsCenterData;

	@Override
	public View initView() {
		view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
		return view;
	}

	@Override
	public void initData() {
		lvLeftMenu = (ListView) view.findViewById(R.id.lv_left_menu);
		lvLeftMenu.setAdapter(new LeftMenuAdapter());
		lvLeftMenu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Class<?>[] clazz = { NewsCenterActivity.class,
						SubjectActivity.class, PictureGroupActivity.class,
						InteractActivity.class };
				Intent intent = new Intent(mActivity, clazz[position]);
				// intent.putExtra("data", newsCenterData);
				startActivity(intent);
			}
		});
	}

	public void setLeftMenuData(NewsCenterData centerData) {
		// newsCenterData = centerData;
		menuDatas = centerData.data;
		
	}

	class LeftMenuAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mTempMenus.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View itemView = View.inflate(mActivity,
					R.layout.list_left_menu_item, null);
			TextView tvMenuItem = (TextView) itemView
					.findViewById(R.id.tv_left_menu_item);
			if (menuDatas == null) {
				tvMenuItem.setText(mTempMenus[position]);
			} else {
				tvMenuItem.setText(menuDatas.get(position).title);
			}
			return itemView;
		}

	}

}

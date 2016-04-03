package com.xiaoshan.zhbj;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xiaoshan.zhbj.domai.PicGroupData;
import com.xiaoshan.zhbj.domai.PicGroupData.PicListDetails;
import com.xiaoshan.zhbj.global.GlobalConstants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PictureGroupActivity extends Activity {

	private ImageButton ibChangeStyle;
	private ListView lvPicGroup;
	private GridView gvPicGroup;
	private ArrayList<PicListDetails> mPics;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activiy_picture_group);
		initView();
		initData();
	}

	private void initView() {
		ibChangeStyle = (ImageButton) findViewById(R.id.ib_switch_style);
		lvPicGroup = (ListView) findViewById(R.id.lv_pic_group);
		gvPicGroup = (GridView) findViewById(R.id.gv_pic_group);
		ibChangeStyle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (gvPicGroup.isShown()) {
					gvPicGroup.setVisibility(View.GONE);
					lvPicGroup.setVisibility(View.VISIBLE);
					ibChangeStyle
							.setImageResource(R.drawable.icon_pic_grid_type);
				} else {
					gvPicGroup.setVisibility(View.VISIBLE);
					lvPicGroup.setVisibility(View.GONE);
					ibChangeStyle
							.setImageResource(R.drawable.icon_pic_list_type);
				}
			}
		});

		lvPicGroup.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				openPicDetails(position);
			}
		});
		
		gvPicGroup.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				openPicDetails(position);
			}
		});
	}

	private void initData() {
		getDataFromServer(GlobalConstants.PICGROUP_URL);
	}

	private void getDataFromServer(String picgroupUrl) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, picgroupUrl,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String json = responseInfo.result;
						parseData(json);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Toast.makeText(PictureGroupActivity.this, msg,
								Toast.LENGTH_SHORT).show();
						error.printStackTrace();
					}
				});
	}

	protected void parseData(String json) {
		Gson gson = new Gson();
		PicGroupData picData = gson.fromJson(json, PicGroupData.class);
		mPics = picData.data.news;
		PicGroupAdapter adapter = new PicGroupAdapter();
		lvPicGroup.setAdapter(adapter);
		gvPicGroup.setAdapter(adapter);
	}

	private class PicGroupAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mPics.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			ViewHolder holder;
			if (convertView != null) {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(PictureGroupActivity.this,
						R.layout.list_pic_group, null);
				holder = new ViewHolder();
				holder.ivPic = (ImageView) view.findViewById(R.id.iv_pic_group);
				holder.tvPicTitle = (TextView) view
						.findViewById(R.id.tv_pic_group_title);
				view.setTag(holder);
			}

			BitmapUtils utils = new BitmapUtils(PictureGroupActivity.this);
			utils.display(holder.ivPic, mPics.get(position).largeimage);
			utils.configDefaultLoadingImage(R.drawable.pic_item_list_default);
			holder.tvPicTitle.setText(mPics.get(position).title);
			return view;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
	}

	class ViewHolder {
		ImageView ivPic;
		TextView tvPicTitle;
	}

	private void openPicDetails(int position) {
		Intent intent = new Intent(PictureGroupActivity.this,
				PicGroupDetailsActivity.class);
		intent.putExtra("url", mPics.get(position).url);
		intent.putExtra("title", mPics.get(position).title);
		startActivity(intent);
	}

}

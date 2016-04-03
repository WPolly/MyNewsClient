package com.xiaoshan.zhbj;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.xiaoshan.zhbj.domai.VoiceChatData;
import com.xiaoshan.zhbj.domai.VoiceResultData;
import com.xiaoshan.zhbj.domai.VoiceResultData.WSBean;

public class InteractActivity extends Activity {
	private ListView lvChat;
	private Button btStartVoice;
	private ArrayList<VoiceChatData> mVoiceChats;
	private RecognizerDialogListener mRecognizerDialogListener;
	private ChatAdapter mChatAdapter;

	private String[] mMMAnswers = new String[] { "约吗?", "讨厌!", "不要再要了,你是个坏人!",
			"这是最后一张了!", "漂亮吧?" };

	private int[] mMMImageIDs = new int[] { R.drawable.p1, R.drawable.p2,
			R.drawable.p3, R.drawable.p4 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_interact);
		initView();
		initData();
		mChatAdapter = new ChatAdapter();
		lvChat.setAdapter(mChatAdapter);
	}
	
	private void initView() {
		lvChat = (ListView) findViewById(R.id.lv_chat);
		btStartVoice = (Button) findViewById(R.id.bt_start_voice);
		btStartVoice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startListen();
			}
		});
	}

	private void initData() {
		mVoiceChats = new ArrayList<VoiceChatData>();
		
		mRecognizerDialogListener = new RecognizerDialogListener() {
			private StringBuffer sb = new StringBuffer();

			@Override
			public void onResult(RecognizerResult recognizerResult,
					boolean isLast) {
				String listenResult = parseListenResult(recognizerResult
						.getResultString());
				sb.append(listenResult);
				if (isLast) {
					int imageId = -1;
					String ask = sb.toString();
					sb = new StringBuffer();
					mVoiceChats.add(new VoiceChatData(ask, true, imageId));
					mChatAdapter.notifyDataSetChanged();
					lvChat.setSelection(mVoiceChats.size() - 1);

					String answer = "就不告诉你";
					if (ask.contains("你好")) {
						answer = "大家好,才是真的好!";
					} else if (ask.contains("你是谁")) {
						answer = "我是你的小助手呀!";
						imageId = R.drawable.d_aini;

					} else if (ask.contains("天王盖地虎")) {
						answer = "小鸡炖蘑菇";
						imageId = R.drawable.m;
					} else if (ask.contains("美女")) {
						Random random = new Random();
						int i = random.nextInt(mMMAnswers.length);
						int j = random.nextInt(mMMImageIDs.length);
						answer = mMMAnswers[i];
						imageId = mMMImageIDs[j];
					}

					mVoiceChats
							.add(new VoiceChatData(answer, false, imageId));
					mChatAdapter.notifyDataSetChanged();
					lvChat.setSelection(mVoiceChats.size() - 1);
					readText(answer);
				}
			}

			@Override
			public void onError(SpeechError arg0) {

			}
		};
	}

	protected void readText(String text) {
		SpeechSynthesizer mTts = SpeechSynthesizer
				.createSynthesizer(this, null);

		mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaomei");
		mTts.setParameter(SpeechConstant.SPEED, "50");
		mTts.setParameter(SpeechConstant.VOLUME, "80");
		mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);

		mTts.startSpeaking(text, null);
	}

	protected String parseListenResult(String resultString) {
		Gson gson = new Gson();
		VoiceResultData resultData = gson.fromJson(resultString,
				VoiceResultData.class);
		ArrayList<WSBean> ws = resultData.ws;

		StringBuffer sb = new StringBuffer();
		for (WSBean wsBean : ws) {
			String text = wsBean.cw.get(0).w;
			sb.append(text);
		}

		return sb.toString();
	}


	protected void startListen() {
		RecognizerDialog iatDialog = new RecognizerDialog(this, null);

		// 2.设置听写参数，详见《科大讯飞MSC API手册(Android)》SpeechConstant类
		iatDialog.setParameter(SpeechConstant.DOMAIN, "iat");
		iatDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
		iatDialog.setParameter(SpeechConstant.ACCENT, "mandarin");

		iatDialog.setListener(mRecognizerDialogListener);

		iatDialog.show();
	}

	class ChatAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mVoiceChats.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			ViewHolder holder;
			if (convertView != null) {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(InteractActivity.this,
						R.layout.list_chat_item, null);
				holder = new ViewHolder();
				holder.llAsker = (LinearLayout) view.findViewById(R.id.ll_ask);
				holder.llAnswer = (LinearLayout) view
						.findViewById(R.id.ll_answer);
				holder.tvAsker = (TextView) view.findViewById(R.id.tv_ask_item);
				holder.tvAnswer = (TextView) view
						.findViewById(R.id.tv_answer_item);
				holder.ivAnswer = (ImageView) view
						.findViewById(R.id.iv_answer_pic);
				view.setTag(holder);
			}

			VoiceChatData chatData = mVoiceChats.get(position);
			if (chatData.isAsker) {
				holder.llAnswer.setVisibility(View.GONE);
				holder.llAsker.setVisibility(View.VISIBLE);
				holder.tvAsker.setText(chatData.chatText);
			} else {
				holder.llAnswer.setVisibility(View.VISIBLE);
				holder.llAsker.setVisibility(View.GONE);
				holder.tvAnswer.setText(chatData.chatText);

				if (chatData.imageId == -1) {
					holder.ivAnswer.setVisibility(View.GONE);
				} else {
					holder.ivAnswer.setVisibility(View.VISIBLE);
					holder.ivAnswer.setImageResource(chatData.imageId);
				}
			}
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
		LinearLayout llAsker, llAnswer;
		TextView tvAsker, tvAnswer;
		ImageView ivAnswer;
	}
}

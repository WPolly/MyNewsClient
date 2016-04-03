package com.xiaoshan.zhbj.domai;

import java.util.ArrayList;

public class VoiceResultData {

	public ArrayList<WSBean> ws;

	public class WSBean {
		public ArrayList<CWBean> cw;
	}

	public class CWBean {
		public String w;
	}
}

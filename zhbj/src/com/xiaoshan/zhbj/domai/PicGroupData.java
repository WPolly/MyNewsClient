package com.xiaoshan.zhbj.domai;

import java.util.ArrayList;

public class PicGroupData {
	public PicListPreView data;
	public int retcode;
	
	public class PicListPreView {
		public String title;
		public ArrayList<PicListDetails> news;
	}
	
	public class PicListDetails {
		public String id;
		public String title;
		public String url;
		public String pubdate;
		public String listimage;
		public String largeimage;
	}
}

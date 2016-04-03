package com.xiaoshan.zhbj.domai;

import java.util.ArrayList;

public class NewsTabDetailData {
	public int retcode;
	public ContenDetail data;
	
	public class ContenDetail {
		public String more;
		public ArrayList<ListNews> news;
		public String title;
		public ArrayList<TopNews> topnews;
	}
	
	public class ListNews {
		public String id;
		public String pubdate;
		public String title;
		public String type;
		public String url;
		public String listimage;
	}
	
	public class TopNews {
		public String id;
		public String pubdate;
		public String title;
		public String type;
		public String url;
		public String topimage;
	}
}

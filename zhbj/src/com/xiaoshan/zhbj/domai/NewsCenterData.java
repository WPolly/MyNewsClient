package com.xiaoshan.zhbj.domai;

import java.io.Serializable;
import java.util.ArrayList;

public class NewsCenterData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int retcode;
	public ArrayList<MenuData> data;
	
	public class MenuData implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 2L;
		public String id;
		public String title;
		public int type;
		public ArrayList<TagData> children;
		@Override
		public String toString() {
			return "MenuData [title=" + title + ", children=" + children + "]";
		}
	}
	
	public class TagData implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 3L;
		public String id;
		public String title;
		public int type ;
		public String url;
		@Override
		public String toString() {
			return "TagData [title=" + title + "]";
		}
	}

	@Override
	public String toString() {
		return "NewsCenterData [data=" + data + "]";
	}
	
	
}

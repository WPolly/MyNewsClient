package com.xiaoshan.zhbj.domai;

public class VoiceChatData {
	public String chatText;
	public boolean isAsker;
	public int imageId = -1;

	public VoiceChatData(String chatText, boolean isAsker, int imageId) {
		super();
		this.chatText = chatText;
		this.isAsker = isAsker;
		this.imageId = imageId;
	}
}

package com.xiaoshan.zhbj;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import cn.jpush.android.api.JPushInterface;
import android.app.Application;
import android.os.Environment;
import android.widget.Toast;

public class MyApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);

		SpeechUtility.createUtility(this, SpeechConstant.APPID + "=55ea5c41");

		copyFile("share.jpg");
	}

	private void copyFile(String fileName) {
		File file = new File(Environment.getExternalStorageDirectory()
				.getPath(), fileName);
		if (!file.exists() || file.length() == 0) {
			try {
				InputStream is = getAssets().open(fileName);
				OutputStream os = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					os.write(buffer, 0, len);
				}

				is.close();
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
				Toast.makeText(this, "数据初始化异常", Toast.LENGTH_SHORT).show();
			}
		}
	}
}

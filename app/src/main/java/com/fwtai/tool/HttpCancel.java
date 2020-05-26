package com.fwtai.tool;

import okhttp3.Call;

/**取消*/
public final class HttpCancel {
	public Call call;
	/**移除请求*/
	public void cancel() {
		if (call != null) {
			call.cancel();
			call = null;
		}
	}
}
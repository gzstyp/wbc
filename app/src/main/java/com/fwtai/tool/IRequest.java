package com.fwtai.tool;

import java.io.IOException;

/**
 * <strong style='color:#03f;'>请求回调</strong>
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2017年3月14日 21:16:05
 * @QQ号码 444141300
 * @官网 http://www.yinlz.com
*/
public abstract class IRequest {
	
	/**请求开始*/
	public void start(){};
	
	/**请求成功回调*/
	public abstract void onSuccess(final String data);
	
	/**请求失败回调*/
	public void onFailure(final IOException exception){};
}
package com.fwtai.tool;

import android.os.Handler;
import android.os.Looper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * <strong style='color:#03f;'>组装okhttp异步网络请求、数据提交</strong>
 * @作者 田应平
 * @原理 异步时接口在另一个方法调用
 * @版本 v1.0
 * @创建时间 2017年3月14日 21:16:18
 * @QQ号码 444141300
 * @主页 http://www.yinlz.com
*/
public class ToolHttp {
	
	
	private final static ToolHttp instance = new ToolHttp();
	
	private final ArrayList<WeakReference<HttpCancel>> listCancel = new ArrayList<WeakReference<HttpCancel>>(0);
	
	/**单例模式*/
	private ToolHttp(){}
	
	/**对外提供获取单例模式方法*/
	public final static ToolHttp getInstance(){
		return instance;
	}
	
	private static final Handler mHandler = new Handler(Looper.getMainLooper());
	
	/**统一全局的code的状态码json关键字key关键字响应给客户端*/
	public final static String code = "code";
	/**统一全局的msg提示消息json关键字key响应给客户端*/
	public final static String msg = "msg";
	/**统一全局的map数据json关键字key响应给客户端*/
	public final static String map = "map";
    public final static String data = "data";
	/**统一全局的obj数据json关键字key响应给客户端*/
	public final static String obj = "obj";
	/**统一全局的listData数据集合(含分页的数据)json关键字key响应给客户端*/
	public final static String listData = "listData";
	public final static String total = "total";
	public final static String code199 = "199";//操作成功或数据不为空
	public final static String code200 = "200";//暂无数据
	public final static String code201 = "201";//暂无数据
    public final static String msg201 = "暂无数据";//暂无数据

	public final static boolean succeed(final HashMap<String,String> map){
		if(map == null || map.size() <= 0)return false;
		if(map.containsKey(code))
		if(map.get(code).equals("200"))return true;
		return false;
	}
	
    /** 异步GET方式提交且参数在url里 */
	public HttpCancel requestGet(final String url,final IRequest iRequest){
		final HttpCancel httpCancel = new HttpCancel();
		httpCancel.call = HttpBase.getInstance().callGet(url);
		listCancel.add(new WeakReference<HttpCancel>(httpCancel));
		handlerUI(httpCancel,iRequest);
		return httpCancel;
	}
	
	/** 异步GET方式提交且带请求参数 */
	public HttpCancel requestGet(final String url,final HashMap<String,String> params,final IRequest iRequest){
		final HttpCancel httpCancel = new HttpCancel();
		httpCancel.call = HttpBase.getInstance().callGet(url,params);
		listCancel.add(new WeakReference<HttpCancel>(httpCancel));
		handlerUI(httpCancel,iRequest);
		return httpCancel;
	}

    /** 异步GET方式提交且带请求参数[String|BigDecimal|Integer|Double] */
    public HttpCancel requestGetObj(final String url,final HashMap<String,Object> params,final IRequest iRequest){
        final HttpCancel httpCancel = new HttpCancel();
        httpCancel.call = HttpBase.getInstance().callGetObj(url,params);
        listCancel.add(new WeakReference<HttpCancel>(httpCancel));
        handlerUI(httpCancel,iRequest);
        return httpCancel;
    }
	
	/** 异步POST请求不带参数的或参数在url里 */
	public HttpCancel requestPost(final String url,final IRequest iRequest){
		final HttpCancel httpCancel = new HttpCancel();
		httpCancel.call = HttpBase.getInstance().callPost(url);
		listCancel.add(new WeakReference<HttpCancel>(httpCancel));
		handlerUI(httpCancel,iRequest);
		return httpCancel;
	}
	
	/** 异步POST请求有带参数 */
	public HttpCancel requestPost(final String url,final HashMap<String,String> params,final IRequest iRequest){
		final HttpCancel httpCancel = new HttpCancel();
		httpCancel.call = HttpBase.getInstance().callPost(url,params);
		listCancel.add(new WeakReference<HttpCancel>(httpCancel));
		handlerUI(httpCancel,iRequest);
		return httpCancel;
	}

	/**支持表单的方式提交*/
    public HttpCancel requestPostFormBody(final String url,final HashMap<String,Object> params,final IRequest iRequest){
        final HttpCancel httpCancel = new HttpCancel();
        httpCancel.call = HttpBase.getInstance().callPostFormBody(url,params);
        listCancel.add(new WeakReference<HttpCancel>(httpCancel));
        handlerUI(httpCancel,iRequest);
        return httpCancel;
    }

	/** 异步POST请求-仅仅只上传文件(含单文件|多文件)或参数在url里 */
	public HttpCancel requestPost(final String url,final ArrayList<File> files,final String name,final IRequest iRequest){
		final HttpCancel httpCancel = new HttpCancel();
		httpCancel.call = HttpBase.getInstance().callPost(url,files,name);
		listCancel.add(new WeakReference<HttpCancel>(httpCancel));
		handlerUI(httpCancel,iRequest);
		return httpCancel;
	}
	
	/** 异步POST请求-上传文件(含单文件|多文件时不用结合后台是否可以使用相同的文件名)附加请求参数 */
	public HttpCancel requestPost(final String url,final HashMap<String,String> params,final ArrayList<File> files,final String name,final IRequest iRequest){
		final HttpCancel httpCancel = new HttpCancel();
		httpCancel.call = HttpBase.getInstance().callPost(url,params,files,name);
		listCancel.add(new WeakReference<HttpCancel>(httpCancel));
		handlerUI(httpCancel,iRequest);
		return httpCancel;
	}

    /** 异步POST请求-上传文件(含单文件|多文件时不用结合后台是否可以使用相同的文件名)附加请求参数 */
    public HttpCancel requestPost(final String url,final HashMap<String,String> params,final ArrayList<File> files,final IRequest iRequest){
        final HttpCancel httpCancel = new HttpCancel();
        httpCancel.call = HttpBase.getInstance().callPost(url,params,files);
        listCancel.add(new WeakReference<HttpCancel>(httpCancel));
        handlerUI(httpCancel,iRequest);
        return httpCancel;
    }

    /** 异步POST请求有多实体对象的参数 */
    public final void requestPostBeans(final String url,final HashMap<String,HashMap<String,Object>> params,final IRequest iRequest){
        final HttpCancel httpCancel = new HttpCancel();
        httpCancel.call = HttpBase.getInstance().callPostBeans(url,params);
        listCancel.add(new WeakReference<HttpCancel>(httpCancel));
        handlerUI(httpCancel,iRequest);
    }

    /** 异步POST请求有单实体对象的参数 */
    public final void requestPostBean(final String url,final HashMap<String,Object> params,final IRequest iRequest){
        final HttpCancel httpCancel = new HttpCancel();
        httpCancel.call = HttpBase.getInstance().callPostBean(url,params);
        listCancel.add(new WeakReference<HttpCancel>(httpCancel));
        handlerUI(httpCancel,iRequest);
    }

    /** 异步POST请求有多实体对象的参数 */
    public final void requestPostJSONObject(final String url,final HashMap<String,JSONObject> params,final IRequest iRequest){
        final HttpCancel httpCancel = new HttpCancel();
        httpCancel.call = HttpBase.getInstance().callPostJSONObject(url,params);
        listCancel.add(new WeakReference<HttpCancel>(httpCancel));
        handlerUI(httpCancel,iRequest);
    }

	/** 发送消息处理数据更新UI */
	private final void handlerUI(final HttpCancel httpCancel,final IRequest iRequest){
		if(httpCancel.call != null && !httpCancel.call.isCanceled()){
			mHandler.post(new Runnable(){
				public void run(){
					iRequest.start();
				}
			});
			httpCancel.call.enqueue(new Callback(){
				@Override
				public void onResponse(final Call mCall, final Response response){
					if(mCall.isCanceled())return;
					try {
						final String data = response.body().string();
						mHandler.post(new Runnable(){
							public void run(){
								iRequest.onSuccess(data);
							}
						});
					} catch (final IOException exception){
						mHandler.post(new Runnable(){
							public void run(){
								iRequest.onFailure(exception);
							}
						});
					}
				}
				@Override
				public void onFailure(final Call mCall, final IOException exception){
					if(mCall.isCanceled())return;
					mHandler.post(new Runnable(){
						public void run(){
							iRequest.onFailure(exception);
						}
					});
				}
			});
		}
	}

    /**
     * 解析json对象字符串
     * @param json
     * @日期 2019年10月8日 10:26:54
     * @return
     */
    public final static HashMap<String,String> parseJsonObject(final Object json){
        final HashMap<String,String> jsonMap = new HashMap<String,String>();
        if(json == null || json.toString().length() <= 0) return jsonMap;
        try {
            final HashMap<String,String> map = JSON.parseObject(json.toString(),new TypeReference<HashMap<String,String>>(){});
            return map == null ? jsonMap : map;
        } catch (Exception e){
            return jsonMap;
        }
    }

    /** 解析json数组字符串 */
    public final static ArrayList<HashMap<String,String>> parseJsonArray(final Object jsonArray){
        final ArrayList<HashMap<String,String>> listResult = new ArrayList<HashMap<String,String>>();//初始化,以免出现空指针异常
        if(jsonArray == null || jsonArray.toString().length() <= 0) return listResult;
        try {
            ArrayList<HashMap<String,String>> result = JSON.parseObject(jsonArray.toString(),new TypeReference<ArrayList<HashMap<String,String>>>(){});
            return result == null ? listResult : result;
        } catch (Exception e){
            return listResult;
        }
    }
	
	/**
     * 将json字符串解析为一个 JavaBean 对象
     * @param cls 转换的目标对象
     * @用法 调用时写 StringTools.jsonToObject(json, javaBean.class);
     * @作者 田应平
     * @返回值类型 T
     * @创建时间 2017年3月7日 下午9:12:19
     * @QQ号码 444141300
     * @官网 http://www.yinlz.com
    */
    public final static <T> T parseJsonToBean(final String json,final Class<T> cls){
		T t = null;
		try {
			t = JSON.parseObject(json,cls);
		} catch (Exception e){
			e.printStackTrace();
		}
		return t;
	}
    
    /**
     * 将json数组字符串 解析成为一个 List< JavaBean >
     * @param cls 转换的目标对象
     * @用法 如果是实体类则在调用时写 jsonToListObject(json,javaBean.class);或 jsonToListObject(json,String.class);
     * @作者 田应平
     * @返回值类型 List< T >
     * @创建时间 2017年3月7日 21:15:00
     * @QQ号码 444141300
     * @官网 http://www.yinlz.com
    */
    public final static <T> List<T> parseJsonToListBean(final String jsonArray,final Class<T> cls){
		List<T> list = new ArrayList<T>();//初始化,以免出现空指针异常
		try {
			list = JSON.parseArray(jsonArray,cls);
		} catch (Exception e){
		}
		return list;
	}
	
	/** 返回1说明是json对象;返回2说明是json数组;返回0不是有效的json数据 */
	public final static int jsonType(final String json){
		if(json == null || json.length() <= 0){
			return 0;
		}
		try {
			if(Pattern.matches("^\\{\".*\\}$",json)){
				JSONObject.parseObject(json);
				return 1;
			}else if(Pattern.matches("^\\[\\{\".*\\]$",json)){
				JSONArray.parseArray(json);
				return 2;
			}else {
				return 0;
			}
		} catch (Exception e){
			return 0;
		}
	}
}
package com.jade.customervisit.network;

import java.io.File;
import java.net.URLDecoder;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import com.jade.customervisit.CVApplication;
import com.jade.customervisit.network.IAsyncListener.ResultParser;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;

/**
 * Web服务
 * 
 * @author huangzhongwen
 * 
 */
public class WebService {

    /**
     * 
     */
    public static final String TAG = WebService.class.getName();

    /**
     * 服务器地址
     */
    public static String URL = CVApplication.DEFAULT_URL;

    /**
     * 异步HTTP请求工具，30S超时
     */
    public static final HttpUtils HTTP = new HttpUtils(60000, "android");

    /**
     * 私有构造
     */
    private WebService() {
    }

    /**
     * POST请求
     * 
     * @param url
     *            请求地址
     * @param params
     *            请求参数
     * @param listener
     *            请求监听器
     * @param parser
     *            结果解析器
     * @return
     */
    public static <T> HttpHandler<String> post(String url,
            final RequestParams params, final RequestListener<T> listener,
            final ResultParser<T> parser) {
        String requestUrl = URL + url;
        return request(HttpMethod.POST, requestUrl, params, listener, parser);
    }

    /**
     * POST请求
     * 
     * @param url
     *            请求地址
     * @param listener
     *            请求监听器
     * @param parser
     *            结果解析器
     * @return
     */
    public static <T> HttpHandler<String> post(String url,
            final RequestListener<T> listener, final ResultParser<T> parser) {
        return post(url, null, listener, parser);
    }

    /**
     * GET请求
     * 
     * @param url
     *            请求地址
     * @param params
     *            请求参数
     * @param listener
     *            请求监听器
     * @param parser
     *            结果解析器
     * @return
     */
    public static <T> HttpHandler<String> get(String url,
            final RequestParams params, final RequestListener<T> listener,
            final ResultParser<T> parser) {
        String requestUrl = URL + url;
        return request(HttpMethod.GET, requestUrl, params, listener, parser);
    }

    /**
     * Get请求
     * 
     * @param url
     *            请求地址
     * @param listener
     *            请求监听器
     * @param parser
     *            结果解析器
     * @return
     */
    public static <T> HttpHandler<String> get(String url,
            final RequestListener<T> listener, final ResultParser<T> parser) {
        return get(url, null, listener, parser);
    }

    /**
     * Http请求
     * 
     * @param method
     *            请求方式
     * @param url
     *            请求地址
     * @param params
     *            请求参数
     * @param listener
     *            监听器
     * @param parser
     *            结果解析器
     * @return
     */
    public static <T> HttpHandler<String> request(HttpMethod method,
            final String url, final RequestParams params,
            final RequestListener<T> listener, final ResultParser<T> parser) {
        return HTTP.send(method, url, params, new RequestCallBack<String>() {

            @SuppressWarnings("deprecation")
            @Override
            public void onStart() {
                if (listener != null) {
                    listener.onStart();
                }
                StringBuilder urlStr = new StringBuilder(url);
                try {
                    HttpEntity entity = params.getEntity();
                    if (entity != null) {
                        urlStr.append("&");
                        urlStr.append(EntityUtils.toString(entity));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                LogUtils.i(URLDecoder.decode(urlStr.toString()));
            }

            @SuppressWarnings("deprecation")
            @Override
            public void onFailure(HttpException e, String content) {
                if (listener != null) {
                    listener.onFailure(e, content);
                    listener.onStopped();
                }
                StringBuilder urlStr = new StringBuilder(url);
                try {
                    HttpEntity entity = params.getEntity();
                    if (entity != null) {
                        urlStr.append("&");
                        urlStr.append(EntityUtils.toString(entity));
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                LogUtils.i(URLDecoder.decode(urlStr.toString())
                        + "->onFailure():HttpException=" + e.getMessage()
                        + ",content=" + content);
            }

            @SuppressWarnings("deprecation")
            @Override
            public void onSuccess(final ResponseInfo<String> response) {
                StringBuilder urlStr = new StringBuilder(url);
                try {
                    HttpEntity entity = params.getEntity();
                    if (entity != null) {
                        urlStr.append("&");
                        urlStr.append(EntityUtils.toString(entity));
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                LogUtils.i(URLDecoder.decode(urlStr.toString())
                        + "->onSuccess():statusCode=" + response.statusCode
                        + ",content=" + response.result);
                if (listener != null) {
                    if (parser == null) {
                        T t = listener.parseResult(response.result);
                        listener.onSuccess(response.statusCode, t);
                        listener.onSuccess(t);
                        listener.onStopped();
                    } else {
                        new ParseTask<T>(new RequestListener<T>() {
                            @Override
                            public void onSuccess(T t) {
                                listener.onSuccess(response.statusCode, t);
                                listener.onSuccess(t);
                                listener.onStopped();
                            }
                        }, parser, response.result).execute();
                    }
                }
            }

            @Override
            public void onCancelled() {
                if (listener != null) {
                    listener.onStopped();
                }
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                if (listener != null) {
                    listener.onLoading(total, current, isUploading);
                }
            }

            @SuppressWarnings("hiding")
            class ParseTask<T> extends BaseAsyncTask<T> {
                ResultParser<T> parser;
                String response;

                public ParseTask(RequestListener<T> callback,
                        ResultParser<T> parser, String response) {
                    super(callback);
                    this.parser = parser;
                    this.response = response;
                }

                @Override
                protected T doInBackground(Object... params) {
                    T t = null;
                    try {
                        t = parser.parse(response);
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e, "解析异常！");
                    }
                    return t;
                }

            }
        });
    }

    /**
     * Http请求
     * 
     * @param method
     *            请求方式
     * @param url
     *            请求地址
     * @param params
     *            请求参数
     * @param listener
     *            监听器
     * @param parser
     *            结果解析器
     * @return
     */
    public static <T> HttpHandler<String> upload(HttpMethod method,
            final String url, final RequestParams params,
            final RequestListener<T> listener, final ResultParser<T> parser) {
        return HTTP.send(method, url, params, new RequestCallBack<String>() {

            @Override
            public void onStart() {
                if (listener != null) {
                    listener.onStart();
                }
            }

            @SuppressWarnings("deprecation")
            @Override
            public void onFailure(HttpException e, String content) {
                LogUtils.i(URLDecoder.decode(url)
                        + "->onFailure():HttpException=" + e.getMessage()
                        + ",content=" + content);
                if (listener != null) {
                    listener.onFailure(e, content);
                    listener.onStopped();
                }
            }

            @SuppressWarnings("deprecation")
            @Override
            public void onSuccess(final ResponseInfo<String> response) {
                LogUtils.i(URLDecoder.decode(url) + "->onSuccess():statusCode="
                        + response.statusCode + ",content=" + response.result);
                if (listener != null) {
                    if (parser == null) {
                        T t = listener.parseResult(response.result);
                        listener.onSuccess(response.statusCode, t);
                        listener.onSuccess(t);
                        listener.onStopped();
                    } else {
                        new ParseTask<T>(new RequestListener<T>() {
                            @Override
                            public void onSuccess(T t) {
                                listener.onSuccess(response.statusCode, t);
                                listener.onSuccess(t);
                                listener.onStopped();
                            }
                        }, parser, response.result).execute();
                    }
                }
            }

            @Override
            public void onCancelled() {
                if (listener != null) {
                    listener.onStopped();
                }
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                if (listener != null) {
                    listener.onLoading(total, current, isUploading);
                }
            }

            @SuppressWarnings("hiding")
            class ParseTask<T> extends BaseAsyncTask<T> {
                ResultParser<T> parser;
                String response;

                public ParseTask(RequestListener<T> callback,
                        ResultParser<T> parser, String response) {
                    super(callback);
                    this.parser = parser;
                    this.response = response;
                }

                @Override
                protected T doInBackground(Object... params) {
                    T t = null;
                    try {
                        t = parser.parse(response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return t;
                }

            }
        });
    }

    /**
     * 上传文件
     * 
     * @param url
     * @param params
     * @param listener
     * @param parser
     * @return
     */
    public static <T> HttpHandler<String> upload(final String url,
            final RequestParams params, final RequestListener<T> listener,
            final ResultParser<T> parser) {
        return upload(HttpMethod.POST, url, params, listener, parser);
    }

    /**
     * 下载
     * 
     * @param url
     *            路径
     * @param params
     *            参数
     * @param target
     *            保存路径
     * @param autoRename
     *            自动重命名
     * @param listener
     *            回调
     * @return
     */
    @SuppressWarnings("deprecation")
    public static HttpHandler<File> download(String url,
            final RequestParams params, String target, boolean autoRename,
            final RequestListener<File> listener) {
        StringBuilder urlStr = new StringBuilder(url);
        try {
            HttpEntity entity = params.getEntity();
            if (entity != null) {
                urlStr.append("&");
                urlStr.append(EntityUtils.toString(entity));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        URLDecoder.decode(urlStr.toString());
        LogUtils.i(urlStr.toString());
        return HTTP.download(HttpMethod.POST, url, target, params, true,
                autoRename, new RequestCallBack<File>() {

                    @Override
                    public void onStart() {
                        if (listener != null) {
                            listener.onStart();
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String content) {
                        // 获取响应状态码
                        int exceptionCode = e.getExceptionCode();
                        switch (exceptionCode) {
                        case 403:
                            // 状态码是403（token失效、设备未验证、长时间未使用）
                            if (listener != null) {
                                listener.onFailure(e, content);
                            }
                            break;
                        case 404:
                            // 状态码是404（附件不存在）
                            if (listener != null) {
                                listener.onFailure(e, "附件不存在");
                            }
                            break;

                        default:
                            // 其他状态码
                            if (listener != null) {
                                listener.onFailure(e, content);
                            }
                            break;
                        }
                        LogUtils.e("exceptionCode=" + exceptionCode);
                        LogUtils.e(content, e);
                        if (listener != null) {
                            listener.onStopped();
                        }
                    }

                    @Override
                    public void onSuccess(ResponseInfo<File> response) {
                        LogUtils.i("reasonPhrase=" + response.reasonPhrase);
                        LogUtils.i("statusCode=" + response.statusCode);
                        LogUtils.i("contentLength=" + response.contentLength);
                        LogUtils.i("result="
                                + response.result.getAbsolutePath());
                        if (listener != null) {
                            listener.onSuccess(response.statusCode,
                                    response.result);
                            listener.onSuccess(response.result);
                            listener.onStopped();
                            listener.onStopped(response.result);
                        }
                    }

                    @Override
                    public void onCancelled() {
                        if (listener != null) {
                            listener.onStopped();
                        }
                    }

                    @Override
                    public void onLoading(long total, long current,
                            boolean isUploading) {
                        if (listener != null) {
                            listener.onLoading(total, current, isUploading);
                        }
                    }
                });
    }
}

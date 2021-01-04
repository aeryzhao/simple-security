package top.amfun.simple.util;

import cn.hutool.core.exceptions.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @author zhaoxg
 * @date 2020/11/17 11:23
 */
@Slf4j
@Component
public class OkHttpUtil {
    private final static MediaType JSON = MediaType.parse("application/json; character=utf-8");
    private final static MediaType XML = MediaType.parse("application/xml; character=utf-8");

    @Autowired
    private OkHttpClient okHttpClient;

    /**
     * Get请求
     * @param url 请求地址
     * @return String
     */
    public String doGet(String url) {
        return doGet(url, null, null);
    }

    /**
     * Get请求
     * @param url 请求地址
     * @param params 请求参数Map
     * @return String
     */
    public String doGet(String url, Map<String, String> params) {
        return doGet(url, params, null);
    }

    /**
     * Get请求
     * @param url 请求地址
     * @param headers 请求头字段 {k1,v1,k2,v2...}
     * @return String
     */
    public String doGet(String url, String[] headers) {
        return doGet(url, null, headers);
    }
    /**
     * Get 请求
     * @param url 请求地址
     * @param params 请求参数map
     * @param headers 请求头字段 {k1,v1,k2,v2...}
     * @return String
     */
    public String doGet(String url, Map<String,String> params, String[] headers) {
        StringBuilder stringBuilder = new StringBuilder(url);
        if (params !=null && params.keySet().size() >0) {
            boolean firstFlag = true;
            for (String key : params.keySet()) {
                if (firstFlag) {
                    stringBuilder.append("?").append(key).append("=").append(params.get(key));
                    firstFlag = false;
                } else {
                    stringBuilder.append("&").append(key).append("=").append(params.get(key));
                }
            }
        }
        Request.Builder builder = new Request.Builder();
        if (headers != null && headers.length >0) {
            if (headers.length % 2 == 0) {
                for (int i = 0; i < headers.length; i = i + 2) {
                    builder.addHeader(headers[i], headers[i + 1]);
                }
            } else {
                log.warn("header's length [{}] is error", headers.length);
            }
        }
        Request request = builder.url(stringBuilder.toString()).build();
        return excute(request);
    }

    /**
     * Post
     * @param url 请求地址
     * @param params 请求参数Map
     * @return String
     */
    public String doPost(String url, Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null && params.keySet().size() > 0) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }
        Request request = new Request.Builder().url(url).post(builder.build()).build();
        log.info("do post request and url [{}]", url);
        return excute(request);
    }

    /**
     * Post
     * @param url 请求地址
     * @param json 请求数据，json字符串
     * @return String
     */
    public String doPostJson(String url, String json) {
        return excetePost(url, json, JSON);
    }

    /**
     * Post
     * @param url 请求地址
     * @param xml 请求数据，xml字符串
     * @return String
     */
    public String doPostXML(String url, String xml) {
        return excetePost(url, xml, XML);
    }

    private String excetePost(String url, String data, MediaType contentType) {
        RequestBody requestBody = RequestBody.create(contentType, data);
        Request request = new Request.Builder().url(url).post(requestBody).build();

        return excute(request);
    }

    private String excute(Request request) {
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (IOException e) {
            log.error(ExceptionUtil.getSimpleMessage(e));
        } finally {
            if (request != null) {
                response.close();
            }
        }
        return null;
    }
}

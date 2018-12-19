package com.hbird.http;


import com.hbird.base.BuildConfig;
import com.hbird.http.service.DynamicApiService;
import com.hbird.http.service.GankDataService;
import com.ljy.devring.http.support.interceptor.HttpLoggingInterceptor;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import sing.common.http.fastjsonconverter.FastJsonConverterFactory;

/**
 * @author Sing
 * @date 2018/10/17 10:13
 * @desc 请求
 */
public class ApiClient {
//    public static final String BASE_URL = "http://118.190.200.64:8088/ShangGong/control/restful/";

    //  static final  String BASE_URL = "https://api.fengniaojizhang.com/rest/api/v1/";//生产环境
//   static final String BASE_H5_URL ="https://api.fengniaojizhang.com/h5/index.html?token=";//正式领票H5

    static final String BASE_URL = "https://api.galaxyhouse.cn/rest/api/v1/";//测试环境
//    String BASE_H5_URL ="http://api.galaxyhouse.cn:8203?token=";//测试 领票H5

//    static final String BASE_URL = "https://api.fengniaojizhang.cn/rest/api/v1/";//灰度环境
    String BASE_H5_URL ="http://api.fengniaojizhang.cn:8089?token=";//灰度 领票H5

    public static GankDataService getGankDataService() {
        return initService(GankDataService.class);
    }

    // 动态url获取数据
    public static DynamicApiService getDynamicDataService() {
        return initService(DynamicApiService.class);
    }

    private static <T> T initService(Class<T> clazz) {
        return getRetrofitInstance().create(clazz);
    }

    /**
     * 单例retrofit
     */
    private static Retrofit retrofitInstance;

    private static Retrofit getRetrofitInstance() {
        if (retrofitInstance == null) {
            synchronized (ApiClient.class) {
                if (retrofitInstance == null) {
                    retrofitInstance = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(FastJsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .client(getOkHttpClientInstance())
                            .build();
                }
            }
        }
        return retrofitInstance;
    }

    /**
     * 单例OkHttpClient
     */
    private static OkHttpClient okHttpClientInstance;
    //连接超时
    public static final int CONNEC_TTIME_OUT = 15;
    // 读取数据超时
    public static final int READ_TIME_OUT = 15;
    // 写数据数据超时
    public static final int WRITE_TIME_OUT = 15;

    private static OkHttpClient getOkHttpClientInstance() {
        if (okHttpClientInstance == null) {
            synchronized (ApiClient.class) {
                if (okHttpClientInstance == null) {
                    OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
                    if (BuildConfig.DEBUG) {
                        builder.addInterceptor(new HttpLoggingInterceptor());
                    }
                    builder.addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            // 拦截器
                            Request request = chain.request();
                            Request.Builder requestBuilder = request.newBuilder()
                                    .addHeader("Content-Type", "application/json; charset=UTF-8")
                                    // .addHeader("Accept-Encoding", "gzip, deflate")
                                    .addHeader("Connection", "keep-alive")
                                    .addHeader("Accept", "*/*");

                            if (request.body() instanceof FormBody) {
                                FormBody oldFormBody = (FormBody) request.body();
                                FormBody body = (FormBody) request.body();

                                StringBuffer sb = new StringBuffer();
                                for (int i = 0; i < body.size(); i++) {
                                    sb.append(body.encodedName(i) + "=" + body.encodedValue(i) + ",");
                                }
                                requestBuilder.method(request.method(), oldFormBody);
                            }
                            return chain.proceed(requestBuilder.build());
                        }
                    });

                    builder.connectTimeout(CONNEC_TTIME_OUT, TimeUnit.SECONDS);
                    builder.readTimeout(READ_TIME_OUT, TimeUnit.SECONDS);
                    builder.writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS);
                    builder.sslSocketFactory(getSSLSocketFactory());
                    okHttpClientInstance = builder.build();
                }
            }
        }
        return okHttpClientInstance;
    }

    private static SSLSocketFactory getSSLSocketFactory() {

        //创建一个不验证证书链的证书信任管理器。
        final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[0];
            }
        }};
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
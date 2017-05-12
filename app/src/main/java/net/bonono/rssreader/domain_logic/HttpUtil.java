package net.bonono.rssreader.domain_logic;

import android.support.annotation.Nullable;
import android.support.compat.BuildConfig;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpUtil {
    private static final String USER_AGENT = "rssr " + BuildConfig.VERSION_NAME;

    private static OkHttpClient sClient;

    static {
        sClient = new OkHttpClient.Builder()
                .followRedirects(true)
                .followSslRedirects(true)
                .connectTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
    }

    public static ResponseWrapper get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", USER_AGENT)
                .build();

        Response response = sClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected response code: " + response.code());
        }

        return new ResponseWrapper(response);
    }

    public static class ResponseWrapper {
        private Response mResponse;
        private String mContentType;

        public ResponseWrapper(Response response) {
            mResponse = response;
            mContentType = mResponse.header("Content-Type", "");
        }

        public ResponseBody body() {
            return mResponse.body();
        }

        @Nullable
        public String charset() {
            for (String e : mContentType.split(";\\s?")) {
                if (e.startsWith("charset=")) {
                    return e.split("=")[1].toUpperCase();
                }
            }

            return null;
        }

        public String requestedUrl() {
            return mResponse.request().url().toString();
        }

        public boolean isXml() {
            return mContentType.contains("xml");
        }

        public boolean isHtml() {
            return mContentType.contains("html");
        }
    }
}

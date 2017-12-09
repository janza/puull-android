package com.jjanzic.puull;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

final class ImageUplodader {
    private static final String BASE_URL = "http://puull.pw/";
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void upload(InputStream myFile, AsyncHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();

        params.put("f", myFile);

        client.post(BASE_URL, params, responseHandler);
    }

    public static void upload(File myFile, AsyncHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();

        try {
            params.put("f", myFile);
        } catch (FileNotFoundException e) {

        }

        client.post(BASE_URL, params, responseHandler);
    }
}

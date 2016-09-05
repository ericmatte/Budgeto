package com.endless.budgeto;

import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import java.util.concurrent.CountDownLatch;

/**
 * Created by Eric on 2016-09-04.
 */
public class JavascriptFetcher implements Runnable {

    WebView webView;
    String command;
    String data;
    CountDownLatch callbackLatch;

    public JavascriptFetcher(WebView webView, String command, CountDownLatch callbackLatch, String data) {
        this.webView = webView;
        this.callbackLatch = callbackLatch;
        this.data = data;
    }

    @Override
    public void run() {
        webView.evaluateJavascript("(function() { return " + command + "; })();", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
                Log.d("LogName", s); // Prints: "this"
                callbackLatch.countDown();
            }
        });
    }


}

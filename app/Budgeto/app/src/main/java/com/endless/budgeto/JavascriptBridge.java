package com.endless.budgeto;

import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import java.util.concurrent.CountDownLatch;

/**
 * Created by Eric on 2016-09-04.
 */
public class JavascriptBridge {

    WebView webView;
    private CountDownLatch callbackLatch;

    public JavascriptBridge(WebView webView) {
        this.webView = webView;
        this.webView.getSettings().setJavaScriptEnabled(true);
    }

    public void sendJsData(String command) {
        webView.loadUrl("javascript:(function() { " + command + "})()");
    }

    public String getJsData(String command) throws InterruptedException {
//        webView.loadUrl("javascript:window.JSInterface.fetchData(" + command + ");");

        callbackLatch = new CountDownLatch (1);
        String data = null;
        //new Thread(new JavascriptFetcher(webView, command, callbackLatch, data)).start();

        webView.evaluateJavascript("(function() { return " + command + "; })();", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
                Log.d("LogName", s); // Prints: "this"
                callbackLatch.countDown();
            }
        });

        callbackLatch.await();


//        callbackLatch = new CountDownLatch (1);
//        String data = null;
//        new Thread(new JavascriptFetcher(webView, command, callbackLatch, data)).start();
//        callbackLatch.await();
//        String temp = data;
//        data = null;
        return "";
    }

    public String getDataFromJs(String command, WebView webView) {
        String data = null;

        webView.evaluateJavascript("(function() { return 'this'; })();", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
                Log.d("LogName", s); // Print "this"
                // data = s; // The value that I would like to return
            }
        });

        return data;
    }


}


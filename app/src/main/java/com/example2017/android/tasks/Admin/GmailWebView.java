package com.example2017.android.tasks.Admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.example2017.android.tasks.R;

public class GmailWebView extends AppCompatActivity {

    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmail_web_view);

    webView=(WebView)findViewById(R.id.webView);
        webView.loadUrl("http://gmail.com");
    }
}

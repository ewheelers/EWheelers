package com.ewheelers.ewheelers.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ewheelers.ewheelers.R;

public class WebViewActivity extends AppCompatActivity {
WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webView = findViewById(R.id.web_view);
        String url = getIntent().getStringExtra("urlIs");
        webView.loadUrl(url);
        webView.canGoBack();
        webView.canGoForward();
        webView.getSettings().setJavaScriptEnabled(true);
    }
}

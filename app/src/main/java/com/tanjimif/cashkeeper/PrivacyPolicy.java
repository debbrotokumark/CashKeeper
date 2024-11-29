package com.tanjimif.cashkeeper;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PrivacyPolicy extends AppCompatActivity {
    private ImageView imgback;
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        imgback = findViewById(R.id.imgback);
        imgback.setOnClickListener(view -> finish());

        webView = findViewById(R.id.webview);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://www.termsfeed.com/live/9e858c2a-8373-407a-9be5-0edeeea0916e");
        webView.setWebViewClient(new WebViewClient());
    }
}
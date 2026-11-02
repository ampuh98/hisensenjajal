package com.hisense.customlauncher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(this);
        setContentView(webView);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setMediaPlaybackRequiresUserGesture(false);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return handleCustomUrl(url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return handleCustomUrl(request.getUrl().toString());
            }
        });

        webView.loadUrl("file:///android_asset/index.html");
    }

    private boolean handleCustomUrl(String url) {
        try {
            if (url.startsWith("intent://")) {
                Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                String pkg = intent.getPackage();
                if (pkg != null) {
                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage(pkg);
                    if (launchIntent != null) {
                        startActivity(launchIntent);
                        return true;
                    }
                }
                startActivity(intent);
                return true;
            } else if (url.equals("app://settings")) {
                startActivity(new Intent(Settings.ACTION_SETTINGS));
                return true;
            } else if (url.equals("app://wifi")) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        // Mencegah tombol Back menutup Home Launcher
    }
}

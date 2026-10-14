package com.zoryak.ai14;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.GeolocationPermissions;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;

public class MainActivity extends Activity {
    private static final int FILE_CHOOSER_REQUEST = 5101;
    private static final int WEB_PERMISSION_REQUEST = 5102;
    private WebView webView;
    private TextView status;
    private ValueCallback<Uri[]> filePathCallback;
    private PermissionRequest pendingWebPermission;
    private String pendingOrigin = "";
    private String serverUrl;
    private SharedPreferences prefs;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.rgb(8, 27, 42));
        getWindow().setNavigationBarColor(Color.rgb(8, 27, 42));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        prefs = getSharedPreferences("zoryak_settings", MODE_PRIVATE);
        serverUrl = prefs.getString("server_url", "");
        buildUi();
        if (serverUrl.isEmpty()) showServerDialog(true);
        else loadServer();
    }

    private void buildUi() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.rgb(8, 27, 42));

        LinearLayout bar = new LinearLayout(this);
        bar.setOrientation(LinearLayout.HORIZONTAL);
        bar.setGravity(android.view.Gravity.CENTER_VERTICAL);
        bar.setPadding(dp(14), dp(8), dp(10), dp(8));
        bar.setBackgroundColor(Color.rgb(8, 27, 42));

        TextView title = new TextView(this);
        title.setText("✦  ZORYAK AI 14.0");
        title.setTextColor(Color.WHITE);
        title.setTextSize(17);
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        bar.addView(title, new LinearLayout.LayoutParams(0, dp(42), 1));

        Button settings = new Button(this);
        settings.setText("Sunucu ⚙");
        settings.setTextSize(12);
        settings.setAllCaps(false);
        settings.setTextColor(Color.WHITE);
        settings.setBackground(tint(Color.rgb(22, 66, 88)));
        bar.addView(settings, new LinearLayout.LayoutParams(dp(104), dp(42)));
        settings.setOnClickListener(v -> showServerDialog(false));
        root.addView(bar);

        status = new TextView(this);
        status.setText("Bağlantı hazırlanıyor…");
        status.setTextColor(Color.rgb(160, 220, 238));
        status.setTextSize(12);
        status.setPadding(dp(14), dp(5), dp(14), dp(7));
        status.setBackgroundColor(Color.rgb(11, 39, 56));
        root.addView(status);

        webView = new WebView(this);
        webView.setBackgroundColor(Color.WHITE);
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);
        ws.setDatabaseEnabled(true);
        ws.setAllowFileAccess(true);
        ws.setAllowContentAccess(true);
        ws.setMediaPlaybackRequiresUserGesture(false);
        ws.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        ws.setSupportZoom(false);
        ws.setBuiltInZoomControls(false);
        CookieManager.getInstance().setAcceptCookie(true);
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);

        webView.setWebViewClient(new WebViewClient() {
            @Override public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String scheme = request.getUrl().getScheme();
                if ("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme)) return false;
                return true;
            }
            @Override public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
                status.setText("Zoryak sunucusuna bağlanıyor…");
            }
            @Override public void onPageFinished(WebView view, String url) {
                status.setText("Sunucu: " + serverUrl + "  ·  Menüden adresi değiştirebilirsin");
            }
            @Override public void onReceivedError(WebView view, WebResourceRequest request, android.webkit.WebResourceError error) {
                if (request.isForMainFrame()) status.setText("Bağlantı kurulamadı. Bilgisayardaki sunucuyu ve Wi-Fi ağını kontrol et.");
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override public void onPermissionRequest(PermissionRequest request) {
                runOnUiThread(() -> {
                    pendingWebPermission = request;
                    pendingOrigin = request.getOrigin().toString();
                    boolean needsAudio = false, needsCamera = false;
                    for (String r : request.getResources()) {
                        if (PermissionRequest.RESOURCE_AUDIO_CAPTURE.equals(r)) needsAudio = true;
                        if (PermissionRequest.RESOURCE_VIDEO_CAPTURE.equals(r)) needsCamera = true;
                    }
                    if ((needsAudio && checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) ||
                        (needsCamera && checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
                        java.util.ArrayList<String> perms = new java.util.ArrayList<>();
                        if (needsAudio && checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) perms.add(Manifest.permission.RECORD_AUDIO);
                        if (needsCamera && checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) perms.add(Manifest.permission.CAMERA);
                        requestPermissions(perms.toArray(new String[0]), WEB_PERMISSION_REQUEST);
                    } else request.grant(request.getResources());
                });
            }
            @Override public void onPermissionRequestCanceled(PermissionRequest request) {
                if (pendingWebPermission == request) pendingWebPermission = null;
            }
            @Override public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> callback, FileChooserParams params) {
                if (filePathCallback != null) filePathCallback.onReceiveValue(null);
                filePathCallback = callback;
                Intent intent = params.createIntent();
                try { startActivityForResult(intent, FILE_CHOOSER_REQUEST); }
                catch (Exception e) { filePathCallback = null; Toast.makeText(MainActivity.this, "Dosya seçici açılamadı", Toast.LENGTH_SHORT).show(); return false; }
                return true;
            }
            @Override public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, false, false);
            }
        });
        root.addView(webView, new LinearLayout.LayoutParams(-1, 0, 1));
        setContentView(root);
    }

    private void showServerDialog(boolean firstRun) {
        LinearLayout box = new LinearLayout(this);
        box.setPadding(dp(20), dp(8), dp(20), dp(4));
        box.setOrientation(LinearLayout.VERTICAL);
        TextView help = new TextView(this);
        help.setText("Önce bilgisayarında Zoryak AI 14.0 start.bat ile çalışsın. Siyah pencerede görünen ‘Android / aynı Wi-Fi’ adresini buraya yaz. Örnek: http://192.168.1.35:8013");
        help.setTextSize(14);
        box.addView(help);
        EditText input = new EditText(this);
        input.setSingleLine(true);
        input.setHint("http://192.168.1.35:8013");
        input.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_URI);
        input.setText(serverUrl);
        box.addView(input);
        new AlertDialog.Builder(this)
            .setTitle("Zoryak sunucu adresi")
            .setView(box)
            .setCancelable(!firstRun)
            .setPositiveButton("Bağlan", (d, w) -> {
                String value = input.getText().toString().trim().replaceAll("/+$", "");
                if (!(value.startsWith("http://") || value.startsWith("https://"))) value = "http://" + value;
                if (value.length() < 10 || value.contains(" ")) {
                    Toast.makeText(this, "Geçerli bir adres gir. Örnek: http://192.168.1.35:8013", Toast.LENGTH_LONG).show();
                    showServerDialog(firstRun); return;
                }
                serverUrl = value;
                prefs.edit().putString("server_url", serverUrl).apply();
                loadServer();
            })
            .setNegativeButton(firstRun ? "Çıkış" : "İptal", (d, w) -> { if (firstRun) finish(); })
            .show();
    }

    private void loadServer() {
        if (serverUrl == null || serverUrl.isEmpty()) return;
        status.setText("Bağlanıyor: " + serverUrl);
        webView.loadUrl(serverUrl);
    }

    @Override public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WEB_PERMISSION_REQUEST && pendingWebPermission != null) {
            boolean allGranted = true;
            for (int result : grantResults) if (result != PackageManager.PERMISSION_GRANTED) allGranted = false;
            if (allGranted) pendingWebPermission.grant(pendingWebPermission.getResources());
            else pendingWebPermission.deny();
            pendingWebPermission = null;
        }
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSER_REQUEST && filePathCallback != null) {
            Uri[] results = null;
            if (resultCode == Activity.RESULT_OK && data != null) {
                Uri uri = data.getData();
                if (uri != null) results = new Uri[]{uri};
            }
            filePathCallback.onReceiveValue(results);
            filePathCallback = null;
        }
    }

    @Override public void onBackPressed() {
        if (webView != null && webView.canGoBack()) webView.goBack();
        else new AlertDialog.Builder(this).setMessage("Zoryak AI'dan çıkılsın mı?")
            .setNegativeButton("Kal", (d, w) -> {})
            .setPositiveButton("Çık", (d, w) -> finish()).show();
    }

    private GradientDrawable tint(int color) {
        GradientDrawable d = new GradientDrawable(); d.setColor(color); d.setCornerRadius(dp(12)); return d;
    }
    private int dp(int value) { return (int)(value * getResources().getDisplayMetrics().density + 0.5f); }
}

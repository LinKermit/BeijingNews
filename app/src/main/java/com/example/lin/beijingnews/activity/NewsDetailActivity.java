package com.example.lin.beijingnews.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lin.beijingnews.R;
import com.example.lin.beijingnews.utils.LogUtil;

import static android.view.View.GONE;

public class NewsDetailActivity extends Activity implements View.OnClickListener{
    private TextView tvTitle;
    private ImageButton ibMenu;
    private ImageButton ibBack;
    private ImageButton ibTextsize;
    private ImageButton ibShare;

    private WebView webview;
    private ProgressBar pbLoading;
    private String url;
    private WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        findViews();
        getData();

    }

    private void getData() {
        url = getIntent().getStringExtra("url");
        webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);//支持js

        //设置双击变大变小
        webSettings.setUseWideViewPort(true);
        //增加缩放按钮
        webSettings.setBuiltInZoomControls(true);
        //设置文字大小
//        webSettings.setTextSize(WebSettings.TextSize.NORMAL);
        webSettings.setTextZoom(100);

        //不让webview跳转到其他浏览器
        webview.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pbLoading.setVisibility(View.GONE);
            }
        });
        LogUtil.e("地址地址"+url);
        webview.loadUrl("http://m.weibo.cn/p/102803_ctg1_8999_-_ctg1_8999_home?wm=5399_0012&v_p=42&extparam=%7C1");
    }

    private void findViews() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ibMenu = (ImageButton) findViewById(R.id.ib_menu);
        ibBack = (ImageButton) findViewById(R.id.ib_back);
        ibTextsize = (ImageButton) findViewById(R.id.ib_textsize);
        ibShare = (ImageButton) findViewById(R.id.ib_share);
        webview = (WebView) findViewById(R.id.webview);
        pbLoading = (ProgressBar) findViewById(R.id.pb_loading);

        tvTitle.setVisibility(GONE);
        ibMenu.setVisibility(GONE);
        ibBack.setVisibility(View.VISIBLE);
        ibTextsize.setVisibility(View.VISIBLE);
        ibShare.setVisibility(View.VISIBLE);

        ibBack.setOnClickListener(this);
        ibTextsize.setOnClickListener(this);
        ibShare.setOnClickListener(this);
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2016-08-24 09:43:47 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if (v == ibBack) {
            finish();
        } else if (v == ibTextsize) {
            showChangeTextSizeDialog();
        } else if (v == ibShare) {
//            showShare();
        }
    }

    private int tempSize = 2;//默认第二个单选向
    private int realSize = tempSize; //点击确定时的选项
    private void showChangeTextSizeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置文字大小");
        String[] items = {"超大字体","大字体","正常字体","小字体","超小字体"};
        builder.setSingleChoiceItems(items, realSize, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tempSize = which;
            }
        });
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                realSize = tempSize;
                changeTextSize(realSize);
            }
        });
        builder.show();
    }

    private void changeTextSize(int realSize) {
        switch (realSize) {
            case 0://超大字体
                webSettings.setTextZoom(200);
                break;
            case 1://大字体
                webSettings.setTextZoom(150);
                break;
            case 2://正常字体
                webSettings.setTextZoom(100);
                break;
            case 3://小字体
                webSettings.setTextZoom(75);
                break;
            case 4://超小字体
                webSettings.setTextZoom(50);
                break;
        }
    }
}

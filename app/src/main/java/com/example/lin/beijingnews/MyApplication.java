package com.example.lin.beijingnews;

import android.app.Application;

import com.android.volley.toolbox.Volley;
import com.example.lin.beijingnews.volley.VolleyManager;

import org.xutils.x;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by lin on 2017/2/7.
 */

public class MyApplication extends Application{

    // 在application的onCreate中初始化
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.

        VolleyManager.init(this);

        //极光推送初始化
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
    }
}

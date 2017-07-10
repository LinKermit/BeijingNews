package com.example.lin.beijingnews.menudetailpager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.lin.beijingnews.base.MenuDetailBasePager;
import com.example.lin.beijingnews.utils.LogUtil;

/**
 * Created by lin on 2017/2/8.
 */

public class InteracMenuDetailPager extends MenuDetailBasePager{

    public InteracMenuDetailPager(Context context) {
        super(context);
    }
    private TextView textView ;
    @Override
    public View initView() {
        LogUtil.e("政要页面数据被初始化了..");
        //1.设置标题
        //2.联网请求，得到数据，创建视图
        textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        textView.setText("第一页面内容");
    }
}

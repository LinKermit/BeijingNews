package com.example.lin.beijingnews.base;

import android.content.Context;
import android.view.View;

/**
 * Created by lin on 2017/2/8.基类
 * 左侧菜单界面的
 */

public abstract class MenuDetailBasePager {


    public final Context context;
    public View rootView;

    public MenuDetailBasePager(Context context){
        this.context = context ;
        rootView = initView();
    }
    /**
     * 抽象方法，强制孩子实现该方法，每个页面实现不同的效果
     * @return
     */
    public abstract View initView();

    public void  initData(){

    }
}

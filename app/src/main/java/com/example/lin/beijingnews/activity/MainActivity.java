package com.example.lin.beijingnews.activity;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;

import com.example.lin.beijingnews.R;
import com.example.lin.beijingnews.fragment.ContentFragment;
import com.example.lin.beijingnews.fragment.LeftmenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {
    private static final String MAIN_CONTENT_TAG = "main_content_tag";
    private static final String LEFTMENU_TAG = "leftmenu_tag";
    private int screeWidth;
    private int screeHeight;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        initSlidingMenu();
        //初始化Fragment
        initFragment();
    }

    private void initSlidingMenu() {
        //1.设置主页面
        setContentView(R.layout.activity_main);

        //2.设置左侧菜单
        setBehindContentView(R.layout.activity_leftmenu);


        //3.设置右侧菜单
        SlidingMenu slidingMenu = getSlidingMenu();
//        slidingMenu.setSecondaryMenu(R.layout.activity_rightmenu);//设置右侧菜单

        //4.设置显示的模式：左侧菜单+主页，左侧菜单+主页面+右侧菜单；主页面+右侧菜单
        slidingMenu.setMode(SlidingMenu.LEFT);

        //5.设置滑动模式：滑动边缘，全屏滑动，不可以滑动
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        DisplayMetrics outmetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outmetrics);
        screeWidth = outmetrics.widthPixels;
        screeHeight = outmetrics.heightPixels;
        //6.设置主页占据的宽度
//        slidingMenu.setBehindOffset(DensityUtil.dip2px(MainActivity.this, 200));
        slidingMenu.setBehindOffset((int) (screeWidth*0.625));
    }

    private void initFragment() {
        //1.得到FragmentManger
        FragmentManager fm = getSupportFragmentManager();
        //2.开启事务
        FragmentTransaction ft= fm.beginTransaction();
        //3.替换
        ft.replace(R.id.fl_main_content,new ContentFragment(), MAIN_CONTENT_TAG);//主页
        ft.replace(R.id.fl_leftmenu, new LeftmenuFragment(), LEFTMENU_TAG);//左侧菜单
        //4.提交
        ft.commit();
//      getSupportFragmentManager().beginTransaction().replace(R.id.fl_main_content,new ContentFragment(), MAIN_CONTENT_TAG).replace(R.id.fl_leftmenu,new LeftmenuFragment(), LEFTMENU_TAG).commit();
    }

    public LeftmenuFragment getLeftmenuFragment() {
        FragmentManager fm = getSupportFragmentManager();
        LeftmenuFragment leftmenuFragment = (LeftmenuFragment) fm.findFragmentByTag(LEFTMENU_TAG);
        return leftmenuFragment;

    }

    //得到正文的Fragment
    public ContentFragment getContentFragment() {
        FragmentManager fm = getSupportFragmentManager();
        ContentFragment contentFragment = (ContentFragment) fm.findFragmentByTag(MAIN_CONTENT_TAG);
        return contentFragment;
    }
}

package com.example.lin.beijingnews.menudetailpager;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.lin.beijingnews.R;
import com.example.lin.beijingnews.activity.MainActivity;
import com.example.lin.beijingnews.base.MenuDetailBasePager;
import com.example.lin.beijingnews.bean.NewsCenterPagerBean;
import com.example.lin.beijingnews.menudetailpager.tabdetailpager.TabDetailPager;
import com.example.lin.beijingnews.utils.LogUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lin on 2017/2/8.
 */

public class NewsMenuDetailPager extends MenuDetailBasePager{
    @ViewInject(R.id.tabLayout)
    private TabLayout tabLayout;
    @ViewInject(R.id.viewpager)
    private ViewPager viewPager;
    @ViewInject(R.id.ib_tab_next)
    private ImageButton ib_tab_next;

    private MyNewsMenuDetailPagerAdapter adapter;
    /**
     * 页签数据集合
     */
    private List<NewsCenterPagerBean.DataBean.ChildrenBean> childrenBeen;
    /**
     * 页签页面的集合
     */
    private ArrayList<TabDetailPager> tabDetailPagers;
    public NewsMenuDetailPager(Context context, NewsCenterPagerBean.DataBean dataBean) {
        super(context);
        childrenBeen = dataBean.getChildren();
    }

    @Override
    public View initView() {
       View view = View.inflate(context, R.layout.newsmenu_detail_pager,null);
        x.view().inject(NewsMenuDetailPager.this,view);
        ib_tab_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
            }
        });
        return view;
    }

    @Override
    public void initData() {
        super.initData();

        //准备新闻标签页下的页面数据
        tabDetailPagers = new ArrayList<>();
        for (int i=0;i<childrenBeen.size();i++){
            tabDetailPagers.add(new TabDetailPager(context,childrenBeen.get(i)));
        }
        adapter = new MyNewsMenuDetailPagerAdapter();
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        //设置左边第一个可以拉动左侧菜单
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0){
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_FULLSCREEN);
                }else {
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                }
            }
            @Override
            public void onPageSelected(int position) {

            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    /**
     根据传人的参数设置是否让SlidingMenu可以滑动
     */
    private void isEnableSlidingMenu(int touchmodeFullscreen) {
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.getSlidingMenu().setTouchModeAbove(touchmodeFullscreen);
    }
    class MyNewsMenuDetailPagerAdapter extends PagerAdapter{

        @Override
        public CharSequence getPageTitle(int position) {
            return childrenBeen.get(position).getTitle();
        }

        @Override
        public int getCount() {
            return tabDetailPagers.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager tabDetailPager = tabDetailPagers.get(position);
            View rootView = tabDetailPager.rootView;
            tabDetailPager.initData();//初始化数据
            container.addView(rootView);
            return rootView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }


    }
}

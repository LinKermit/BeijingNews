package com.example.lin.beijingnews.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.example.lin.beijingnews.R;
import com.example.lin.beijingnews.activity.MainActivity;
import com.example.lin.beijingnews.base.BaseFragment;
import com.example.lin.beijingnews.base.BasePager;
import com.example.lin.beijingnews.pager.GovaffairPager;
import com.example.lin.beijingnews.pager.HomePager;
import com.example.lin.beijingnews.pager.NewsCenterPager;
import com.example.lin.beijingnews.pager.SettingPager;
import com.example.lin.beijingnews.pager.SmartServicePager;
import com.example.lin.beijingnews.ui.NoScrollViewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by lin on 2017/2/6.
 */

public class ContentFragment extends BaseFragment{
    @ViewInject(R.id.rg_main)
    private RadioGroup rg_main;

    @ViewInject(R.id.viewpager)
    private NoScrollViewPager viewpager;
    private ArrayList<BasePager> basePagers;
    @Override
    public View initView() {
        View view = View.inflate(context,  R.layout.content_fragment,null);
//        rg_main = (RadioGroup) view.findViewById(R.id.rg_main);
//        viewpager = (ViewPager) view.findViewById(R.id.viewpager);
        x.view().inject(ContentFragment.this,view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        basePagers = new ArrayList<>();
        basePagers.add(new HomePager(context));
        basePagers.add(new NewsCenterPager(context));
        basePagers.add(new SmartServicePager(context));
        basePagers.add(new GovaffairPager(context));
        basePagers.add(new SettingPager(context));


        viewpager.setAdapter(new ContentFragmentAdapter());
        rg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());

        //第一页数据加载
        rg_main.check(R.id.rb_home);
        basePagers.get(0).initData();
        isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);

    }
    //获取新闻中心
    public NewsCenterPager getNewsCenterPager() {
        return (NewsCenterPager) basePagers.get(1);
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        /**
         * 当某个页面被选中回调该方法，只加载该页面的数据
         * @param position
         */
        @Override
        public void onPageSelected(int position) {
            basePagers.get(position).initData();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    //为RadioGroup设置监听
    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                case R.id.rb_home://主页面radiobutton的id
                    viewpager.setCurrentItem(0,false);//加false没有动画viewpager切换
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.rb_newscenter:
                    viewpager.setCurrentItem(1,false);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_FULLSCREEN);
                    break;
                case R.id.rb_govaffair:
                    viewpager.setCurrentItem(2,false);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.rb_smartservice:
                    viewpager.setCurrentItem(3,false);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.rb_setting:
                    viewpager.setCurrentItem(4);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;

            }
        }
    }

     class ContentFragmentAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return basePagers.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager basePager = basePagers.get(position);
            View view = basePager.rootView;
//            basePager.initData();//初始化页面数据
            container.addView(view);
            return view;
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

    /**
     根据传人的参数设置是否让SlidingMenu可以滑动
     */
    private void isEnableSlidingMenu(int touchmodeFullscreen) {
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.getSlidingMenu().setTouchModeAbove(touchmodeFullscreen);
    }
}

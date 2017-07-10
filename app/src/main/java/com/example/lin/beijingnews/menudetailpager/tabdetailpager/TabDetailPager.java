package com.example.lin.beijingnews.menudetailpager.tabdetailpager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lin.beijingnews.R;
import com.example.lin.beijingnews.activity.NewsDetailActivity;
import com.example.lin.beijingnews.adapter.TabDetailPagerAdapter;
import com.example.lin.beijingnews.base.MenuDetailBasePager;
import com.example.lin.beijingnews.bean.NewsCenterPagerBean;
import com.example.lin.beijingnews.bean.TabDetailPagerBean;
import com.example.lin.beijingnews.ui.HorizontalScrollViewPager;
import com.example.lin.beijingnews.utils.CacheUtils;
import com.example.lin.beijingnews.utils.Constants;
import com.example.lin.beijingnews.utils.LogUtil;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;
import java.util.logging.Handler;

/**
 * Created by lin on 2017/2/8.
 */

public class TabDetailPager extends MenuDetailBasePager {

    private HorizontalScrollViewPager viewPager;
    private TextView title;
    private LinearLayout ll_point_group;
    private ListView listView;

    private MyHandler myHandler;
    /**
     * 之前高亮点的位置
     */
    private int prePosition;
    private final NewsCenterPagerBean.DataBean.ChildrenBean childrenData;

    private String url;
    private List<TabDetailPagerBean.DataBean.TopnewsBean> topnews;
    private List<TabDetailPagerBean.DataBean.NewsBean> news;

    private TabDetailPagerAdapter adapter;

    public static final String ARRAY_ID = "array_id";
    public TabDetailPager(Context context, NewsCenterPagerBean.DataBean.ChildrenBean childrenBean) {
        super(context);
        childrenData = childrenBean;
    }

    @Override
    public View initView() {
       View view = View.inflate(context, R.layout.tabdetail_pager,null);
        listView = (ListView) view.findViewById(R.id.listview);

        //加载viewpager，是viewpager可以和listview一起滑动
        View topview = View.inflate(context, R.layout.topnews_viewpager,null);
        viewPager = (HorizontalScrollViewPager) topview.findViewById(R.id.viewpager);
        title = (TextView) topview.findViewById(R.id.title);
        ll_point_group = (LinearLayout) topview.findViewById(R.id.ll_point_group);
        //把顶部轮播图部分视图，以头的方式添加到listview中
        listView.addHeaderView(topview);

        //设置监听
        listView.setOnItemClickListener(new MyOnItemClickListner());
        return view;
    }


    @Override
    public void initData() {
        super.initData();
        url = Constants.BASE_URL+childrenData.getUrl();
        //得到缓存数据
        String saveJson = CacheUtils.getString(context,url);
        if (!TextUtils.isEmpty(saveJson)){
            //解析缓存数据
            processsData(saveJson);
        }
        getDataFromNet();

    }

    /**
     * viewpager页面监听
     */
    class  MyPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //设置标题
            title.setText(topnews.get(position).getTitle());
            ll_point_group.getChildAt(prePosition).setEnabled(false);
            ll_point_group.getChildAt(position).setEnabled(true);
            prePosition = position;
        }

        private  boolean isDragging = false;
        @Override
        public void onPageScrollStateChanged(int state) {
            if(state ==ViewPager.SCROLL_STATE_DRAGGING){//拖拽
                isDragging = true;
                LogUtil.e("拖拽");
                //拖拽要移除消息
                myHandler.removeCallbacksAndMessages(null);
            }else if(state ==ViewPager.SCROLL_STATE_SETTLING&&isDragging){//惯性
                //发消息
                LogUtil.e("惯性");
                isDragging = false;
                myHandler.removeCallbacksAndMessages(null);
                myHandler.postDelayed(new MyRunnable(),4000);

            }else if(state ==ViewPager.SCROLL_STATE_IDLE&&isDragging){//静止状态
                //发消息
                LogUtil.e("静止状态");
                isDragging = false;
                myHandler.removeCallbacksAndMessages(null);
                myHandler.postDelayed(new MyRunnable(),4000);
            }

        }
    }
    private void getDataFromNet() {
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //缓存数据
                CacheUtils.putString(context,url,result);
                processsData(result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 解析数据
     * @param json
     */
    private void processsData(String json) {
        TabDetailPagerBean bean = parseJson(json);
        topnews = bean.getData().getTopnews();
        viewPager.setAdapter(new TabDetailPagerTopAdapter());
        addPoint();

        viewPager.addOnPageChangeListener(new MyPageChangeListener() );
        title.setText(topnews.get(prePosition).getTitle());

        //准备ListView对应的集合数据
        news = bean.getData().getNews();
        //为listview添加适配器
        adapter = new TabDetailPagerAdapter(context,news);
        listView.setAdapter(adapter);

        if (myHandler == null){
            myHandler = new MyHandler();
        }
        //是把消息队列所有的消息和回调移除
        myHandler.removeCallbacksAndMessages(null);
        myHandler.postDelayed(new MyRunnable(),4000);

    }

    class MyHandler extends android.os.Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int item = (viewPager.getCurrentItem()+1)%topnews.size();
            viewPager.setCurrentItem(item);
            myHandler.postDelayed(new MyRunnable(), 4000);
        }
    }

    class MyRunnable implements Runnable{

        @Override
        public void run() {
            myHandler.sendEmptyMessage(0);
        }
    }


    private TabDetailPagerBean parseJson(String json) {
        Gson gson = new Gson();
        TabDetailPagerBean bean = gson.fromJson(json,TabDetailPagerBean.class);
        return bean;
    }
    class TabDetailPagerTopAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return topnews.size();
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(context);
            imageView.setBackgroundResource(R.drawable.home_scroll_default);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);//拉伸图片
            container.addView(imageView);
            //联网请求图片
            String imageurl = Constants.BASE_URL + topnews.get(position).getTopimage();
            x.image().bind(imageView,imageurl);

            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            //按下移除所有消息
                            myHandler.removeCallbacksAndMessages(null);
                            break;
                        case MotionEvent.ACTION_UP:
                            myHandler.removeCallbacksAndMessages(null);
                            myHandler.postDelayed(new MyRunnable(),4000);
                            break;
                    }
                    return true;
                }
            });
            return imageView;
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
    //添加红点
    private void addPoint() {
        ll_point_group.removeAllViews();//移除所有的红点
        for (int i = 0; i < topnews.size(); i++) {

            ImageView imageView = new ImageView(context);
            //设置背景选择器
            imageView.setBackgroundResource(R.drawable.point_selector);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(5),DensityUtil.dip2px(5));

            if(i==0){
                imageView.setEnabled(true);
            }else{
                imageView.setEnabled(false);
                params.leftMargin = DensityUtil.dip2px(8);
            }
            imageView.setLayoutParams(params);

            ll_point_group.addView(imageView);

        }
    }
    /**
     * 设置listview监听，保存点击ID，已点击的变灰色
     */
    class MyOnItemClickListner implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            int realPosition = position - 1;
            TabDetailPagerBean.DataBean.NewsBean newsData = news.get(realPosition);
            Toast.makeText(context,"newsData===ID"+newsData.getId(),Toast.LENGTH_LONG).show();
            //取出保存的id集合
            String idArray = CacheUtils.getString(context,ARRAY_ID);
            //判断是否存在，如果不存在，才保存，刷新适配器
            if (!idArray.contains(newsData.getId()+"")){
                CacheUtils.putString(context,ARRAY_ID,idArray+newsData.getId()+""+",");
                adapter.notifyDataSetChanged();//getcount - getview
            }
            Intent intent = new Intent(context, NewsDetailActivity.class);
            intent.putExtra("url",Constants.BASE_URL+newsData.getUrl());
            context.startActivity(intent);

        }
    }

}

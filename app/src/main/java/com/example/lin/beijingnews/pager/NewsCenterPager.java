package com.example.lin.beijingnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lin.beijingnews.activity.MainActivity;
import com.example.lin.beijingnews.base.BasePager;
import com.example.lin.beijingnews.base.MenuDetailBasePager;
import com.example.lin.beijingnews.bean.NewsCenterPagerBean;
import com.example.lin.beijingnews.fragment.LeftmenuFragment;
import com.example.lin.beijingnews.menudetailpager.InteracMenuDetailPager;
import com.example.lin.beijingnews.menudetailpager.NewsMenuDetailPager;
import com.example.lin.beijingnews.menudetailpager.PhotosMenuDetailPager;
import com.example.lin.beijingnews.menudetailpager.TopicMenuDetailPager;
import com.example.lin.beijingnews.utils.CacheUtils;
import com.example.lin.beijingnews.utils.Constants;
import com.example.lin.beijingnews.utils.LogUtil;
import com.example.lin.beijingnews.volley.VolleyManager;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class NewsCenterPager extends BasePager {
    public NewsCenterPager(Context context) {
        super(context);
    }

    private List<NewsCenterPagerBean.DataBean> data ;

    private ArrayList<MenuDetailBasePager> detailBasePagers;
    @Override
    public void initData() {
        super.initData();
        LogUtil.e("新闻中心数据被初始化了..");
        //1.设置标题
        tv_title.setText("新闻中心");
        //2.联网请求，得到数据，创建视图
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);
        //3.把子视图添加到BasePager的FrameLayout中
        fl_content.addView(textView);
        //4.绑定数据
        textView.setText("新闻中心内容");
        ib_menu.setVisibility(View.VISIBLE);
        /**
         * 获取缓存数据，并解析
         */
        String saveJson = CacheUtils.getString(context,Constants.NEWSCENTER_PAGER_URL);
        if (!TextUtils.isEmpty(saveJson)){
            processData(saveJson);//解析数据
        }
        getDataFromNet();
        getDataFromNetByVolley();


    }

    private void getDataFromNetByVolley() {
//        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.NEWSCENTER_PAGER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        processData(result);//解析数据
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String  parsed = new String(response.data, "UTF-8");
                    return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return super.parseNetworkResponse(response);
            }
        };//解决乱码问题



        VolleyManager.getRequestQueue().add(stringRequest);
    }

    private void getDataFromNet() {
        RequestParams params = new RequestParams(Constants.NEWSCENTER_PAGER_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("联网数据请求成功"+result);
                //将数据存在本地  缓存
                CacheUtils.putString(context,Constants.NEWSCENTER_PAGER_URL,result);
                processData(result);//解析数据
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("联网数据请求成功"+ex.getCause());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("联网数据请求成功"+cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("联网数据请求结束");
            }
        });
    }

    private void processData(String json) {
        NewsCenterPagerBean bean = parseJson(json);
        String result = bean.getData().get(0).getChildren().get(3).getTitle();
        /**
         *创建MainActivity实例
         */
        data = bean.getData();
        MainActivity mainactivity = (MainActivity) context;
        LeftmenuFragment leftmenuFragment = mainactivity.getLeftmenuFragment();//获取LeftmenuFragment实例

        //添加左侧4个页面
        detailBasePagers = new ArrayList<>();
        detailBasePagers.add(new NewsMenuDetailPager(context,data.get(0)));//新闻中心的数据在传递给NewsMenuDetailPager，获取页签页面的数据，即新闻下12个标签的标题
        detailBasePagers.add(new TopicMenuDetailPager(context));
        detailBasePagers.add(new PhotosMenuDetailPager(context,data.get(2)));
        detailBasePagers.add(new InteracMenuDetailPager(context));

        //把数据传递给左侧菜单
        leftmenuFragment.sendData(data);


    }

    /**
     * 使用gson解析
     * @param json
     * @return
     */
    private NewsCenterPagerBean parseJson(String json) {
        Gson gson = new Gson();
        NewsCenterPagerBean newsBean =  gson.fromJson(json,NewsCenterPagerBean.class);
        return newsBean;
    }

    /**
     * 根据位置切换详情页面
     * @param position
     */
    public void switchPager(int position) {
        //1、设置标题
        tv_title.setText(data.get(position).getTitle());
        //2、移除之前的内容View
        fl_content.removeAllViews();
        // 3、添加新内容，及详情界面

        MenuDetailBasePager menuDetailBasePager = detailBasePagers.get(position);
        View view = menuDetailBasePager.rootView;
        menuDetailBasePager.initData();
        fl_content.addView(view);
        if(position ==2){
            //图组详情页面
            ib_swich_list_grid.setVisibility(View.VISIBLE);
            //设置点击事件
            ib_swich_list_grid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //1.得到图组详情页面对象
                    PhotosMenuDetailPager detailPager = (PhotosMenuDetailPager) detailBasePagers.get(2);
                    //2.调用图组对象的切换ListView和GridView的方法
                    detailPager.swichListAndGrid(ib_swich_list_grid);
                }
            });
        }else{
            //其他页面
            ib_swich_list_grid.setVisibility(View.GONE);
        }
    }
}

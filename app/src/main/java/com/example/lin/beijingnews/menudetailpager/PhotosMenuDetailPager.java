package com.example.lin.beijingnews.menudetailpager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.lin.beijingnews.R;
import com.example.lin.beijingnews.activity.ShowImageActivity;
import com.example.lin.beijingnews.base.MenuDetailBasePager;
import com.example.lin.beijingnews.bean.NewsCenterPagerBean;
import com.example.lin.beijingnews.bean.PhotosMenuDetailPagerBean;
import com.example.lin.beijingnews.utils.CacheUtils;
import com.example.lin.beijingnews.utils.Constants;
import com.example.lin.beijingnews.utils.LogUtil;
import com.example.lin.beijingnews.volley.VolleyManager;
import com.google.gson.Gson;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by lin on 2017/2/8.
 */

public class PhotosMenuDetailPager extends MenuDetailBasePager{


    private final NewsCenterPagerBean.DataBean detailPagerData;
    private PhotosMenuDetailPagerAdapter adapter;

    @ViewInject(R.id.listview)
    private ListView listview;

    @ViewInject(R.id.gridview)
    private GridView gridview;

    private String url;
    private List<PhotosMenuDetailPagerBean.DataEntity.NewsEntity> news;

    public PhotosMenuDetailPager(Context context, NewsCenterPagerBean.DataBean detailPagerData) {
        super(context);
        this.detailPagerData = detailPagerData;
    }


    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.photos_menudetail_pager, null);
        x.view().inject(this, view);
        //设置点击某条的item的监听
        listview.setOnItemClickListener(new MyOnItemClickListener());
        gridview.setOnItemClickListener(new MyOnItemClickListener());
        return view;
    }
    class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            PhotosMenuDetailPagerBean.DataEntity.NewsEntity newsEntity = news.get(position);
            String imageUrl = Constants.BASE_URL+newsEntity.getLargeimage();
            Intent intent = new Intent(context, ShowImageActivity.class);
            intent.putExtra("url",imageUrl);
            context.startActivity(intent);
        }
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("图组详情页面数据被初始化了..");
        url = Constants.BASE_URL + detailPagerData.getUrl();
        String saveJson = CacheUtils.getString(context, url);
        if (!TextUtils.isEmpty(saveJson)) {
            processData(saveJson);
        }
//        getDataFromNetByOkHttpUtils();
        getDataFromNet();
    }




    private void getDataFromNet() {
        //String请求
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                LogUtil.e("使用Volley联网请求成功==" + result);
                //缓存数据
                CacheUtils.putString(context, url, result);

                processData(result);
                //设置适配器
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.e("使用Volley联网请求失败==" + volleyError.getMessage());
            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String parsed = new String(response.data, "UTF-8");
                    return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return super.parseNetworkResponse(response);
            }
        };

        //添加到队列
        VolleyManager.getRequestQueue().add(request);
    }


    /**
     * 解析和显示数据
     *
     * @param json
     */
    private void processData(String json) {

        PhotosMenuDetailPagerBean bean = parsedJson(json);
        LogUtil.e("图组解析成功==" + bean.getData().getNews().get(0).getTitle());

        isShowListView = true;
        //设置适配器
        news = bean.getData().getNews();
        adapter = new PhotosMenuDetailPagerAdapter();
        listview.setAdapter(adapter);
    }

    /**
     * true,显示ListView，隐藏GridView
     * false,显示GridView,隐藏ListView
     */
    private boolean isShowListView = true;

    public void swichListAndGrid(ImageButton ib_swich_list_grid) {
        if(isShowListView){
            isShowListView = false;
            //显示GridView,隐藏ListView
            gridview.setVisibility(View.VISIBLE);
            adapter = new PhotosMenuDetailPagerAdapter();
            gridview.setAdapter(adapter);
            listview.setVisibility(View.GONE);
            //按钮显示--ListView
            ib_swich_list_grid.setImageResource(R.drawable.icon_pic_list_type);


        }else{
            isShowListView = true;
            //显示ListView，隐藏GridView
            listview.setVisibility(View.VISIBLE);
            adapter = new PhotosMenuDetailPagerAdapter();
            listview.setAdapter(adapter);
            gridview.setVisibility(View.GONE);
            //按钮显示--GridView
            ib_swich_list_grid.setImageResource(R.drawable.icon_pic_grid_type);
        }
    }

    class PhotosMenuDetailPagerAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return news.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView == null){
                convertView = View.inflate(context,R.layout.item_photos_menudetail_pager,null);
                viewHolder = new ViewHolder();
                viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //根据位置得到对应的数据
            PhotosMenuDetailPagerBean.DataEntity.NewsEntity newsEntity = news.get(position);
            viewHolder.tv_title.setText(newsEntity.getTitle());
            String imageUrl = Constants.BASE_URL+newsEntity.getSmallimage();

            Glide.with(context)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .placeholder(R.drawable.news_pic_default)
                    .into(viewHolder.iv_icon);
            return convertView;
        }
    }

    static class ViewHolder{
        ImageView iv_icon;
        TextView tv_title;
    }




    private PhotosMenuDetailPagerBean parsedJson(String json) {
        return new Gson().fromJson(json, PhotosMenuDetailPagerBean.class);
    }
}

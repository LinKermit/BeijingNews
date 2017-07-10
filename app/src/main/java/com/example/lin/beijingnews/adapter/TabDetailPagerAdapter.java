package com.example.lin.beijingnews.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lin.beijingnews.R;
import com.example.lin.beijingnews.bean.TabDetailPagerBean;
import com.example.lin.beijingnews.menudetailpager.tabdetailpager.TabDetailPager;
import com.example.lin.beijingnews.utils.CacheUtils;
import com.example.lin.beijingnews.utils.Constants;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

/**
 * Created by lin on 2017/2/9.
 */

public class TabDetailPagerAdapter extends BaseAdapter{
    private Context context;
    List<TabDetailPagerBean.DataBean.NewsBean> news;
    ImageOptions imageOptions;//设置加载失败的图片
    public TabDetailPagerAdapter(Context context, List<TabDetailPagerBean.DataBean.NewsBean> news) {
        this.context = context;
        this.news = news;
        imageOptions = new ImageOptions.Builder()
                .setSize(DensityUtil.dip2px(120), DensityUtil.dip2px(90))
                .setRadius(DensityUtil.dip2px(5))
                // 如果ImageView的大小不是定义为wrap_content, 不要crop.
                .setCrop(true) // 很多时候设置了合适的scaleType也不需要它.
                // 加载中或错误图片的ScaleType
                //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(R.drawable.news_pic_default)
                .setFailureDrawableId(R.drawable.news_pic_default)
                .build();
    }

    @Override
    public int getCount() {
        return news.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null ){
            convertView = View.inflate(context, R.layout.item_tabdetail,null);
            viewHolder = new ViewHolder();
            viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //根据位置得到数据
        TabDetailPagerBean.DataBean.NewsBean newsData = news.get(position);
        String imageUrl = Constants.BASE_URL + newsData.getListimage();
        x.image().bind(viewHolder.iv_icon,imageUrl,imageOptions);
        viewHolder.tv_title.setText(newsData.getTitle());
        viewHolder.tv_time.setText(newsData.getPubdate());

        String idArray = CacheUtils.getString(context, TabDetailPager.ARRAY_ID);
        if (idArray.contains(newsData.getId()+"")){
            viewHolder.tv_title.setTextColor(Color.GRAY);//包含则设置为灰色
        }else {
            viewHolder.tv_title.setTextColor(Color.BLACK);
        }

        return convertView;
    }

   static class ViewHolder {
        TextView tv_title;
        TextView tv_time;
        ImageView iv_icon;
        }
}

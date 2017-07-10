package com.example.lin.beijingnews.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lin.beijingnews.R;

/**
 * Created by Administrator on 2017/2/12.
 */

public class RefreshListView extends ListView{

    /**
     * 下拉刷新和轮播图集成到listview
     */


    private View ll_pull_down_refresh;
    private ImageView iv_arrow;
    private ProgressBar pb_status;
    private TextView tv_status;
    private TextView tv_time;

    private LinearLayout headerView;
    public RefreshListView(Context context) {
        this(context,null);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs ,0);
    }

    //前面两个直接通过第三个初始化
    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView(context);
    }

    private void initHeaderView(Context context) {

         headerView = (LinearLayout) View.inflate(context, R.layout.refresh_header,null);
        //下拉刷新控件
        ll_pull_down_refresh = headerView.findViewById(R.id.ll_pull_down_refresh);
        iv_arrow = (ImageView) headerView.findViewById(R.id.iv_arrow);
        pb_status = (ProgressBar) headerView.findViewById(R.id.pb_status);
        tv_status = (TextView) headerView.findViewById(R.id.tv_status);
        tv_time = (TextView) headerView.findViewById(R.id.tv_time);

        //添加ListView的头
        addHeaderView(headerView);

    }


}

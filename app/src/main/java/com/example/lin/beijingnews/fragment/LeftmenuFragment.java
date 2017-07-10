package com.example.lin.beijingnews.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lin.beijingnews.R;
import com.example.lin.beijingnews.activity.MainActivity;
import com.example.lin.beijingnews.base.BaseFragment;
import com.example.lin.beijingnews.bean.NewsCenterPagerBean;
import com.example.lin.beijingnews.pager.NewsCenterPager;
import com.example.lin.beijingnews.utils.LogUtil;

import java.util.List;

/**
 * Created by lin on 2017/2/6.
 */

public class LeftmenuFragment extends BaseFragment{

    List<NewsCenterPagerBean.DataBean> data;
    private ListView listView;
    private LeftmenuFragmentAdapter adapter;
    private int prePosition;
    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.left_fragment,null);
        listView = (ListView) view.findViewById(R.id.listview);

        /**
         * 设置点击事件
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               //1、记录点击的位置，变为红色
                prePosition = position;
                adapter.notifyDataSetChanged();
                //2、将左侧菜单收起
                MainActivity mainActivity = (MainActivity) context;
                mainActivity.getSlidingMenu().toggle();

                //3、切换到对应的详情页面//重
                switchPager(prePosition);

            }
        });
        return view;
    }

    /**
     * 根据位置切换详情界面
     * @param position
     */
    private void switchPager(int position) {
        MainActivity mainActivity = (MainActivity) context;
        ContentFragment contentFragment = mainActivity.getContentFragment();//得到ContentFragment
        NewsCenterPager newsCenterPager =   contentFragment.getNewsCenterPager();//得到新闻中心，它里面包含4个详情页面
        newsCenterPager.switchPager(position);
    }

    @Override
    public void initData() {
        super.initData();
    }

    public void sendData(List<NewsCenterPagerBean.DataBean> data) {
        this.data = data;
        for (int i=0;i<data.size();i++) {
            LogUtil.e("获取到的标题"+data.get(i).getTitle());
        }
        adapter = new LeftmenuFragmentAdapter();
        listView.setAdapter(adapter);
        //设置默认页面是左侧新闻
        switchPager(prePosition);

    }

    class LeftmenuFragmentAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return data.size();
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = (TextView) View.inflate(context,R.layout.item_leftmenu,null);
            textView.setText(data.get(position).getTitle());
            if (prePosition == position){
                textView.setEnabled(true);
            }else {
                textView.setEnabled(false);
            }
            return textView;
        }
        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


    }
}

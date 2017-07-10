package com.example.lin.beijingnews.utils;


public class Constants {

    /**
     * 联网请求的ip和端口
     */
    public static final String BASE_URL = "http://192.168.0.104:8080/web_home";
//    public static final String BASE_URL = "http://192.168.191.1:8080/web_home/static/api/news/categories.json";

//    public static final String BASE_URL = "http://10.0.2.2:8080/web_home";

    /**
     * 新闻中心的网络地址
     */
    public static final String NEWSCENTER_PAGER_URL = BASE_URL+"/static/api/news/categories.json";

    /**
     * 商品热卖
     */
    public static final String WARES_HOT_URL = "http://112.124.22.238:8081/course_api/wares/hot?pageSize=";
}

package com.example.lin.beijingnews;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.example.lin.beijingnews.activity.GuideActivity;
import com.example.lin.beijingnews.activity.MainActivity;
import com.example.lin.beijingnews.utils.CacheUtils;

public class SplashActivity extends AppCompatActivity {

    /**
     * 静态变量Ctrl+alt+c
     */
    public static final String START_MAIN = "start_main";
    private RelativeLayout rl_splash_root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        rl_splash_root = (RelativeLayout) findViewById(R.id.rl_splash_root);
        //渐变动画
        AlphaAnimation aa = new AlphaAnimation(0,1);
        aa.setDuration(500);
        aa.setFillAfter(true);

        ScaleAnimation sa = new ScaleAnimation(0,1,0,1,ScaleAnimation.RELATIVE_TO_SELF,0.5f,ScaleAnimation.RELATIVE_TO_SELF,0.5f);
        sa.setDuration(500);
        sa.setFillAfter(true);

        RotateAnimation ra = new RotateAnimation(0,360,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        ra.setDuration(500);
        ra.setFillAfter(true);

        AnimationSet set = new AnimationSet(false);
        set.addAnimation(aa);
        set.addAnimation(sa);
        set.addAnimation(ra);
        set.setDuration(2000);
        rl_splash_root.startAnimation(set);

        set.setAnimationListener(new MyAnimationListener());

    }

    class MyAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

            boolean isStartMain = CacheUtils.getBoolean(SplashActivity.this,START_MAIN);
            if (isStartMain){
            //如果进入过主界面，直接进
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
            }else {
                Intent intent = new Intent(SplashActivity.this,GuideActivity.class);
                startActivity(intent);
            }
            finish();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}

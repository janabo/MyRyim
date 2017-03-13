package com.janabo.myim;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.janabo.myim.http.HttpClientUtil;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

/**
 * 评价
 * 作者：janabo on 2017/3/13 11:22
 */
@ContentView(R.layout.activity_discuss)
public class DiscussActivity extends Activity implements View.OnClickListener{
    @ViewInject(R.id.satisfaction_flayout)
    FrameLayout satisfaction_flayout;
    @ViewInject(R.id.sameas_fLayout)
    FrameLayout sameas_fLayout;
    @ViewInject(R.id.unsatisfy_flayout)
    FrameLayout unsatisfy_flayout;
    @ViewInject(R.id.satisfaction_img)
    ImageView satisfaction_img;
    @ViewInject(R.id.sameas_img)
    ImageView sameas_img;
    @ViewInject(R.id.unsatisfy_img)
    ImageView unsatisfy_img;
    @ViewInject(R.id.satisfaction)
    TextView satisfaction;
    @ViewInject(R.id.sameas)
    TextView sameas;
    @ViewInject(R.id.unsatisfy)
    TextView unsatisfy;
    @ViewInject(R.id.submit_discuss)
    Button submit_discuss;
    private int selectpar = 1;
    DiscussActivity mContext;
    CoreSharedPreferencesHelper helper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.gf_black));
        }
        helper = new CoreSharedPreferencesHelper(this);
        mContext = this;
        satisfaction_flayout.setOnClickListener(this);
        sameas_fLayout.setOnClickListener(this);
        unsatisfy_flayout.setOnClickListener(this);
        submit_discuss.setOnClickListener(this);
        dealBackgroud();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.satisfaction_flayout:
                selectpar = 1;
                dealBackgroud();
                break;
            case R.id.sameas_fLayout:
                selectpar = 2;
                dealBackgroud();
                break;
            case R.id.unsatisfy_flayout:
                selectpar = 3;
                dealBackgroud();
                break;
            case R.id.submit_discuss:
                submit();
                if(MessageActivity.instance != null){
                    MessageActivity.instance.finish();
                }
                finish();
                break;
        }
    }

    public void submit(){
        Map<String,String> map = new HashMap<>();
        map.put("strID",helper.getValue("callid"));
        map.put("strScore",selectpar+"");
        HttpClientUtil.doPost("http://srv.huaruntong.cn/chat/hprongyun.asmx/GetOpen_Opinion", map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(mContext,"感谢您的评价",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(mContext,"登录失败",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(CancelledException cex) {

            }
            @Override
            public void onFinished() {

            }
        });
    }


    public void dealBackgroud(){
            switch (selectpar){
                case 2:
                    satisfaction_flayout.setBackground(getResources().getDrawable(R.drawable.default_border));
                    satisfaction.setTextColor(getResources().getColor(R.color.gf_black));
                    satisfaction_img.setVisibility(View.GONE);
                    sameas_fLayout.setBackground(getResources().getDrawable(R.drawable.grade_border));
                    sameas.setTextColor(getResources().getColor(R.color.colorPrimary));
                    sameas_img.setVisibility(View.VISIBLE);
                    unsatisfy_flayout.setBackground(getResources().getDrawable(R.drawable.default_border));
                    unsatisfy.setTextColor(getResources().getColor(R.color.gf_black));
                    unsatisfy_img.setVisibility(View.GONE);
                    break;
                case 3:
                    satisfaction_flayout.setBackground(getResources().getDrawable(R.drawable.default_border));
                    satisfaction.setTextColor(getResources().getColor(R.color.gf_black));
                    satisfaction_img.setVisibility(View.GONE);
                    sameas_fLayout.setBackground(getResources().getDrawable(R.drawable.default_border));
                    sameas.setTextColor(getResources().getColor(R.color.gf_black));
                    sameas_img.setVisibility(View.GONE);
                    unsatisfy_flayout.setBackground(getResources().getDrawable(R.drawable.grade_border));
                    unsatisfy.setTextColor(getResources().getColor(R.color.colorPrimary));
                    unsatisfy_img.setVisibility(View.VISIBLE);
                    break;
                default:
                    satisfaction_flayout.setBackground(getResources().getDrawable(R.drawable.grade_border));
                    satisfaction.setTextColor(getResources().getColor(R.color.colorPrimary));
                    satisfaction_img.setVisibility(View.VISIBLE);
                    sameas_fLayout.setBackground(getResources().getDrawable(R.drawable.default_border));
                    sameas.setTextColor(getResources().getColor(R.color.gf_black));
                    sameas_img.setVisibility(View.GONE);
                    unsatisfy_flayout.setBackground(getResources().getDrawable(R.drawable.default_border));
                    unsatisfy.setTextColor(getResources().getColor(R.color.gf_black));
                    unsatisfy_img.setVisibility(View.GONE);
                    break;
            }

    }
}

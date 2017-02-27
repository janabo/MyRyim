package com.janabo.myim;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.janabo.myim.http.HttpClientUtil;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：janabo on 2017/2/27 14:32
 */
@ContentView(R.layout.activity_leave_a_message)
public class LeaveAMessage extends AppCompatActivity {
    @ViewInject(R.id.name)
    EditText name;
    @ViewInject(R.id.email)
    EditText email;
    @ViewInject(R.id.phone)
    EditText phone;
    @ViewInject(R.id.message)
    EditText message;
    @ViewInject(R.id.submit)
    Button submit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Event(value={R.id.submit})
    private void getEvent(View view){
        switch(view.getId()){
            case R.id.submit:
                submitMessage();
                break;
        }
    }

    private void submitMessage(){
        Map<String,String> map = new HashMap<>();
        map.put("Ip","123456789012");
        map.put("guest_name","system");
        map.put("urlref","");
        HttpClientUtil.doPost("http://srv.huaruntong.cn/chat/hprongyun.asmx/Init_Guest_Info", map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
//                Toast.makeText(mContext,"登录失败",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(CancelledException cex) {

            }
            @Override
            public void onFinished() {

            }
        });
    }



}

package com.janabo.myim;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.janabo.myim.http.HttpClientUtil;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：janabo on 2017/2/27 14:32
 */
@ContentView(R.layout.activity_leave_a_message)
public class LeaveAMessage extends AppCompatActivity {
    LeaveAMessage mContext;
    CoreSharedPreferencesHelper helper;

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
        x.view().inject(this);
        helper = new CoreSharedPreferencesHelper(this);
        mContext = this;
    }
    @Event(value={R.id.submit})
    private void getEvent(View view){
        switch(view.getId()){
            case R.id.submit:
                submitMessage();
                break;
        }
    }

    /**
     * 提交留言
     */
    private void submitMessage(){
        if(name.getText().toString().length()<=0){
            Toast.makeText(mContext,"请填写您的姓名",Toast.LENGTH_SHORT).show();
            return;
        }
        if(email.getText().toString().length()<=0){
            Toast.makeText(mContext,"请填写您的点子邮件",Toast.LENGTH_SHORT).show();
            return;
        }
        if(phone.getText().toString().length()<=0){
            Toast.makeText(mContext,"请填写您的联系方式",Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String,String> map = new HashMap<>();
        map.put("strID",helper.getValue("callid"));
        map.put("strName",name.getText().toString());
        map.put("strEamil",email.getText().toString());
        map.put("strTelnum",phone.getText().toString());
        map.put("strMessage",phone.getText().toString());
        HttpClientUtil.doPost("http://srv.huaruntong.cn/chat/hprongyun.asmx/GetLeave_Msg", map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(mContext,"感谢您的留言",Toast.LENGTH_SHORT).show();
                finish();
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
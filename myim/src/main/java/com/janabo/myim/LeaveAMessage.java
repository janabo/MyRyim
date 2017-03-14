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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    @ViewInject(R.id.back)
    Button back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        helper = new CoreSharedPreferencesHelper(this);
        mContext = this;
    }
    @Event(value={R.id.submit,R.id.back})
    private void getEvent(View view){
        switch(view.getId()){
            case R.id.submit:
                submitMessage();
                break;
            case R.id.back:
                finish();
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

        if(!checkEmail(email.getText().toString())){
            Toast.makeText(mContext,"请填写正确的邮箱格式",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!isMobile(phone.getText().toString()) && !isPhone(phone.getText().toString()) ){
            Toast.makeText(mContext,"请填写正确的联系方式",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(mContext,"提交留言失败",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(CancelledException cex) {

            }
            @Override
            public void onFinished() {

            }
        });
    }


    /**
     * 邮箱校验
     * @param email
     * @return
     */
    public static boolean checkEmail(String email){
        boolean flag = false;
        try{
            String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        }catch(Exception e){
            flag = false;
        }
        return flag;
    }

    /**
       * 手机号验证
       * @author ：shijing
       * 2016年12月5日下午4:34:46
       * @param  str
       * @return 验证通过返回true
       */
     public static boolean isMobile(final String str) {
             Pattern p = null;
             Matcher m = null;
             boolean b = false;
             p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
             m = p.matcher(str);
             b = m.matches();
             return b;
     }


 /**
  * 电话号码验证
 * @author ：shijing
   * 2016年12月5日下午4:34:21
   * @param  str
   * @return 验证通过返回true
   */
    public static boolean isPhone(final String str) {
        Pattern p1 = null, p2 = null;
        Matcher m = null;
        boolean b = false;
        p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$");  // 验证带区号的
        p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");         // 验证没有区号的
        if (str.length() > 9) {
                m = p1.matcher(str);
                b = m.matches();
             } else {
                 m = p2.matcher(str);
                b = m.matches();
             }
             return b;
         }

    }

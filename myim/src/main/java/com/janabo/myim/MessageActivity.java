package com.janabo.myim;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.janabo.myim.adapter.MessageListAdapter;
import com.janabo.myim.entity.Face;
import com.janabo.myim.entity.Message;
import com.janabo.myim.entity.Msg;
import com.janabo.myim.http.HttpClientUtil;
import com.janabo.myim.http.Manager;
import com.janabo.myim.util.EmojiUtil;
import com.janabo.myim.util.Global;
import com.janabo.myim.util.Util;
import com.janabo.myim.widget.ChatKeyBoard;
import com.janabo.myim.widget.DropDownListView;
import com.janabo.myim.widget.listener.SendListener;

import org.xutils.common.Callback;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by janabo on 2016-12-11.
 */

public class MessageActivity extends BaseFragmentActivity implements DropDownListView.OnRefreshListenerHeader,EasyPermissions.PermissionCallbacks{
    private static final int CAMERA_P = 2;
    private static final int WRITE_RERD = 1;
    private String noCutFilePath ="";
    Context mContext = this;
    CoreSharedPreferencesHelper helper;
    Button back,leavemessage;
    TextView no;//客服工号
    //表情键盘
    private ChatKeyBoard boxInput = null;
    //对话列表
    private DropDownListView lvMsg = null;
    //对话列表适配器
    private MessageListAdapter adapter = null;
    //消息集合
    private List<Message> list = new ArrayList<>();
    private Handler mHandler = null;
    public static MessageActivity instance;
    private boolean flag = true;

    //修改BUG：如果不在handler中操作，会导致无法正确置底
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            lvMsg.smoothScrollToPosition(list.size());
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        instance = MessageActivity.this;
        helper = new CoreSharedPreferencesHelper(this);
        back = (Button)findViewById(R.id.back);
        leavemessage = (Button) findViewById(R.id.leavemessage);
        boxInput = (ChatKeyBoard) findViewById(R.id.bjmgf_message_chat_keyboard);
        lvMsg = (DropDownListView) findViewById(R.id.bjmgf_message_chat_listview);
        no = (TextView) findViewById(R.id.no);
        boxInput.setMessageIndex(0);

        //如果草稿不为空，则显示草稿
        if (Global.MESSAGES.get(0).getDraft().trim().length() > 0) {
            boxInput.getEditText().setText(EmojiUtil.convert(this, Global.MESSAGES.get(0).getDraft()));
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitApp();
            }
        });

        //留言
        leavemessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,LeaveAMessage.class);
                startActivity(intent);
            }
        });

        boxInput.setListener(new SendListener() {
            @Override
            public void sendMsg(String text) {
                Message msg = new Message("", text, "", "", true, false);
                list.add(msg);
                adapter.notifyDataSetChanged();
                handler.sendEmptyMessage(0);
                text = EmojiUtil.convertToEmoji(text);
                sendmsg(text);
            }
            @Override
            public void sendExpression(Face face) {
                    if(face.getPosition() == 0){
                        startReadWi();
                    }else if(face.getPosition() == 1){
                        startCAMERA();
                    }

            }
        });

        helper = new CoreSharedPreferencesHelper(this);

        DisplayMetrics dm=new DisplayMetrics();//创建矩阵
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        adapter = new MessageListAdapter(this, list);
        lvMsg.setAdapter(adapter);
        lvMsg.setOnRefreshListenerHead(this);
        //将消息置底
        lvMsg.setSelection(list.size()>0? list.size()- 1 :0);
        //设置触摸监听
        lvMsg.setOnTouchListener(getOnTouchListener());
        //设置光标处于最后
        boxInput.getEditText().setSelection(boxInput.getEditText().getText().length());
        list.add(new Message("正在接入客服中...", false, "", false));
        adapter.notifyDataSetChanged();
        handler.sendEmptyMessage(0);
        msgPoll();//定时器TODO开发先注销

    }

    public void msgPoll(){
        handlerMsg.postDelayed(runnable, 1000);//每两秒执行一次runnable
    }

    Handler handlerMsg=new Handler();
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            initMsgs();
            if(flag) {
                handlerMsg.postDelayed(this, 1000);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        flag = false;
    }

    private void initMsgs() {

        Map<String,String> map = new HashMap<>();
        map.put("gustid",helper.getValue("mguestid"));
        HttpClientUtil.doPost("http://srv.huaruntong.cn/chat/hprongyun.asmx/ChatTimer", map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if(isNotEmpty(result)) {
                    Msg msg = Manager.getObj(result, Msg.class);
                    Message m = new Message(msg.getMsg(), false, msg.getUrlimg(), false);
                    if (Util.isNotEmpty(msg.getMsg()) || Util.isNotEmpty(msg.getUrlimg())) {
                        list.add(m);
                    }
                    if("001".equals(msg.getCode())){
                        no.setText(msg.getStro());
                    }
                    if ("002".equals(msg.getCode()) || "004".equals(msg.getCode()) || "005".equals(msg.getCode())) {
                        list.add(new Message("请点击留言按钮留下您的联系方式我们会尽快联系您，感谢您的理解...", false, "", false));
                    }
                    adapter.notifyDataSetChanged();
                    handler.sendEmptyMessage(0);
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
//                Toast.makeText(mContext,"接收失败",Toast.LENGTH_SHORT).show();
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
     * 若软键盘或表情键盘弹起，点击上端空白处应该隐藏输入法键盘
     *
     * @return 会隐藏输入法键盘的触摸事件监听器
     */
    private View.OnTouchListener getOnTouchListener() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                boxInput.hideExpressionLayout();
                Util.hideKeyboard(MessageActivity.this);
                boxInput.hideFunctionLayout();
                return false;
            }
        };
    }

    @Override
    public void onRefresh() {
        final List<Message> oldMsg = new ArrayList<>();
        mHandler = new Handler(getMainLooper()) {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void handleMessage(android.os.Message msg) {
                super.handleMessage(msg);
                adapter.notifyDataSetChanged();
                if (!lvMsg.isMove()) {
                    lvMsg.setSelectionFromTop(oldMsg.size() + 1, (int) getResources().getDimension(R.dimen.gf_40dp));
                }
                lvMsg.onRefreshCompleteHeader();
            }
        };
        mHandler.sendEmptyMessageDelayed(0, 1000);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitApp();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void finish() {
        Util.hideKeyboard(this);
        super.finish();
    }

    public void exitApp() {
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(mContext);
        alertDialog.setMessage("您确定退出聊天吗?")
                .setPositiveButton("取消",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(mContext,DiscussActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_up_in, 0);

            }
        });
        alertDialog.create().show();
    }



    public void sendmsg(String content){
        Map<String,String> map = new HashMap<>();
//        map.put("fromid",helper.getValue("mguestid"));
        map.put("gustid",helper.getValue("mguestid"));
        map.put("msg",content);
        HttpClientUtil.doPost("http://srv.huaruntong.cn/chat/hprongyun.asmx/SendToAgent", map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                Toast.makeText(mContext,result.toString(),Toast.LENGTH_SHORT).show();
                if(isNotEmpty(result)) {
                    Msg msg = Manager.getObj(result, Msg.class);
                    Message m = new Message(msg.getMsg(), false, msg.getUrlimg(), false);
                    if ("002".equals(msg.getCode())){
                        list.add(m);
                        adapter.notifyDataSetChanged();
                        handler.sendEmptyMessage(0);
                    }
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
//                Toast.makeText(mContext,"发送失败",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(CancelledException cex) {

            }
            @Override
            public void onFinished() {

            }
        });

    }

    public void ablum(){
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, 5);
    }

    public void camera(){
        noCutFilePath = Environment.getExternalStorageDirectory() + "/myim/cache/" + UUID.randomUUID().toString() + ".jpg";
        Intent getImageByCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(noCutFilePath)));
        startActivityForResult(getImageByCamera, 4);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 4://拍照 不剪切
                File f=new File(noCutFilePath);
                if(f.exists()) {
                    Message msg2 = new Message("", true, noCutFilePath, true);
                    list.add(msg2);
                    adapter.notifyDataSetChanged();
                    lvMsg.requestFocus();
                    handler.sendEmptyMessage(0);
                    uploadImg(new File(noCutFilePath));
                }
                break;
            case 5://相册选取不 剪切
                if (data != null) {
                    Uri uri = data.getData();
                    ContentResolver cr = this.getContentResolver();
                    Cursor c = cr.query(uri, null, null, null, null);
                    if (c.moveToNext()) {
                        noCutFilePath=c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
                    }
                    c.close();

                    Message msg = new Message("",true,noCutFilePath,true);
                    list.add(msg);
                    adapter.notifyDataSetChanged();
                    lvMsg.requestFocus();
                    handler.sendEmptyMessage(0);

                    uploadImg(new File(noCutFilePath));
                }
                break;
            default:
                break;
        }

    }

    /**
     * 压缩图片
     *
     *
     */
    private Bitmap createThumbnail(String filepath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true，即只读边不读内容
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        BitmapFactory.decodeFile(filepath, options);

        options.inJustDecodeBounds = false;
        int w = options.outWidth;
        int h = options.outHeight;
        // 想要缩放的目标尺寸
        float hh = h/2;// 设置高度为240f时，可以明显看到图片缩小了
        float ww = w;// 设置宽度为120f，可以明显看到图片缩小了
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (options.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (options.outHeight / hh);
        }
        if (be <= 0) be = 1;
        options.inSampleSize = be;//设置缩放比例
        // 开始压缩图片，注意此时已经把options.inJustDecodeBounds 设回false了

        return BitmapFactory.decodeFile(filepath, options);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        try {
            if(requestCode == WRITE_RERD) {
                ablum();
            }
            else if(requestCode == CAMERA_P){
                camera();
            }
        } catch (Exception e) {
            Toast.makeText(mContext, "请正确授权", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Toast.makeText(mContext, "请正确授权", Toast.LENGTH_LONG).show();
    }

    @AfterPermissionGranted(WRITE_RERD)
    private void startReadWi(){
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(mContext, perms)) {
            try {
                ablum();
            } catch (Exception e) {
                Toast.makeText(mContext, "请正确授权", Toast.LENGTH_LONG).show();
            }
        } else {
            EasyPermissions.requestPermissions(this,
                    "请求读写权限",
                    WRITE_RERD, perms);
        }
    }

    @AfterPermissionGranted(CAMERA_P)
    private void startCAMERA(){
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(mContext, perms)) {
            try {
                camera();
            } catch (Exception e) {
                Toast.makeText(mContext, "请正确授权", Toast.LENGTH_LONG).show();
            }
        } else {
            EasyPermissions.requestPermissions(this,
                    "请求读写权限",
                    CAMERA_P, perms);
        }
    }


    public void uploadImg(File file) {
        Map<String, Object> map = new HashMap<>();
        map.put("guestid",helper.getValue("mguestid"));
        HttpClientUtil.upload("http://srv.huaruntong.cn/chat/hprongyun.asmx/UploadFile", map,file, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("result",result);
//                Toast.makeText(mContext,result.toString(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    public static boolean isNotEmpty(String str) {
        boolean bool = true;
        if (str == null || "null".equals(str) || "".equals(str)) {
            bool = false;
        } else {
            bool = true;
        }
        return bool;
    }

}

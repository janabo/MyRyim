package com.janabo.myim.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.janabo.myim.BaseFaceFragment;
import com.janabo.myim.R;
import com.janabo.myim.adapter.FunctionAdapter;
import com.janabo.myim.adapter.MessageChatFunctionAdapter;
import com.janabo.myim.entity.Emoji;
import com.janabo.myim.entity.Emoticion;
import com.janabo.myim.entity.Face;
import com.janabo.myim.entity.Function;
import com.janabo.myim.fragment.FacePageFragment;
import com.janabo.myim.fragment.QqPageFragment;
import com.janabo.myim.util.EmojiUtil;
import com.janabo.myim.util.Global;
import com.janabo.myim.util.Util;
import com.janabo.myim.widget.adapter.FaceCategroyAdapter;
import com.janabo.myim.widget.listener.KeyBoardListener;
import com.janabo.myim.widget.listener.SendListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by janabo on 2016-12-11.
 */

public class ChatKeyBoard extends RelativeLayout implements
        SoftKeyboardStateHelper.SoftKeyboardStateListener, KeyBoardListener {
    public static final int LAYOUT_TYPE_HIDE = 0;//隐藏
    public static final int LAYOUT_TYPE_FACE = 1;//显示表情
    public static final int LAYOUT_TYPE_MORE = 2;//显示更多
    private EditText mEtMsg = null;
    private CheckBox mBtnFace = null;
    private CheckBox mBtnMore = null;
    private ImageView mBtnSend = null;
    private ViewPager mPagerFaceCagetory = null;
    private RelativeLayout mRlFace = null;
    private PagerSlidingTabStrip mFaceTabs = null;
    private ViewPager vpFunction = null;
    private LinearLayout llDot = null;
    private LinearLayout llFunction = null;
    private int layoutType = LAYOUT_TYPE_HIDE;
    private FaceCategroyAdapter adapter;  //点击表情按钮时的适配器
    private List<String> mFaceData = new ArrayList<>();
    private SoftKeyboardStateHelper mKeyboardHelper;
    private Context context = null;
    private SendListener mListener = null;
    private boolean isSend = false;
    private List<View> views = new ArrayList<>();
    private int columns = 4;
    private int rows = 2;
    private String[] functionArray = new String[]{"照片", "拍照"};
    private int msgIndex = 0;
    private List<BaseFaceFragment> listFragment = new ArrayList<>();
    private int currentPosition = 0;
    private int viewPagerIndex = 0;
    private int subViewPagerIndex = 0;
    private boolean isSlide = false;

    public ChatKeyBoard(Context context) {
        super(context);
        init(context);
    }

    public ChatKeyBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ChatKeyBoard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View root = View.inflate(context, R.layout.bjmgf_message_chat_keyboard, null);
        this.addView(root);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initData();
        initWidget();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (getContext() instanceof Activity) {
            mKeyboardHelper = new SoftKeyboardStateHelper(((Activity) getContext())
                    .getWindow().getDecorView());
            mKeyboardHelper.addSoftKeyboardStateListener(this);
        }
    }

    /**
     * 初始化控件
     */
    private void initWidget() {
        mEtMsg = (EditText) findViewById(R.id.bjmgf_message_chat_et);
        mBtnSend = (ImageView) findViewById(R.id.bjmgf_message_chat_sound_btn);
        mBtnFace = (CheckBox) findViewById(R.id.bjmgf_message_chat_btn_face);
        mBtnMore = (CheckBox) findViewById(R.id.bjmgf_message_chat_btn_more);
        mRlFace = (RelativeLayout) findViewById(R.id.bjmgf_message_chat_face_rl);
        mPagerFaceCagetory = (ViewPager) findViewById(R.id.bjmgf_message_chat_face_vp);
        mFaceTabs = (PagerSlidingTabStrip) findViewById(R.id.bjmgf_message_chat_toolbox_tabs);
        vpFunction = (ViewPager) findViewById(R.id.bjmgf_message_chat_function_vp);
        llDot = (LinearLayout) findViewById(R.id.bjmgf_message_chat_function_dots);
        llFunction = (LinearLayout) findViewById(R.id.bjmgf_message_chat_function_ll);

        mEtMsg.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEND && mEtMsg.getText().toString().length()>0) {
                    if (mListener != null) {
                        mListener.sendMsg(mEtMsg.getText().toString());
                        mEtMsg.setText("");
                    }
                }
                return false;
            }
        });

        mBtnSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (mListener != null && mEtMsg.getText().toString().length()>0) {
                        mListener.sendMsg(mEtMsg.getText().toString());
                        mEtMsg.setText("");
                    }
            }
        });


        // 点击表情按钮
        mBtnFace.setOnClickListener(getFunctionBtnListener(LAYOUT_TYPE_FACE));
        // 点击表情按钮旁边的加号
        mBtnMore.setOnClickListener(getFunctionBtnListener(LAYOUT_TYPE_MORE));
        // 点击消息输入框
        mEtMsg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                hideExpressionLayout();
                hideFunctionLayout();
            }
        });
        //edittext增加文本改变监听
        mEtMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //每次文本变化，都放入草稿中
                Global.MESSAGES.get(msgIndex).setDraft(s.toString());
                isSend = true;
                int length = s.toString().length();
                if (length > 0) {
                    String sign = s.toString().substring(start, length);
                    String emojiId = Global.EMOJISCODE.get(sign);
                    if (emojiId != null) {
                        ((Editable) mEtMsg.getText()).delete(start, length);
                      EmojiUtil.insert(mEtMsg, EmojiUtil.getFace(context, "face/emojis/EmojiS_" + emojiId + ".png"));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        initFaceData();
        initFunction();
    }

    /**
     * 初始化表情数据
     */
    public void initFaceData() {
        mFaceData.clear();
        listFragment.clear();
        File faceList = new File(Environment.getExternalStorageDirectory() + File.separator + "chat" + File.separator);
        if (faceList.isDirectory()) {
            File[] faceFolderArray = faceList.listFiles();
            for (File folder : faceFolderArray) {
                if (!folder.isHidden()) {
                    mFaceData.add(folder.getAbsolutePath());
                }
            }
        }
        if (layoutType == LAYOUT_TYPE_MORE) {
            mFaceTabs.setVisibility(GONE);
        } else {
            //加1是表示第一个分类为默认的emoji表情分类，这个分类是固定不可更改的
            if (mFaceData.size() + 1 < 2) {
                mFaceTabs.setVisibility(GONE);
            } else {
                mFaceTabs.setVisibility(VISIBLE);
            }
        }
        //更换为QQ表情
//        BaseFaceFragment f = new EmojiPageFragment();
//        ((EmojiPageFragment) f).setKeyBoardListener(this);
//        listFragment.add(f);
        BaseFaceFragment f = new QqPageFragment();
        ((QqPageFragment) f).setKeyBoardListener(this);
        listFragment.add(f);

        for (int i = 0; i < mFaceData.size(); i++) {
            BaseFaceFragment faceFragment = new FacePageFragment();
            ((FacePageFragment) faceFragment).setKeyBoardListener(this);
            ((FacePageFragment) faceFragment).setPath(mFaceData.get(i));
            listFragment.add(faceFragment);
        }
    }

    /**
     * 初始化功能
     */
    private void initFunction() {
        List<Function> listFunction = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Function f = null;
            try {
                f = new Function(functionArray[i], BitmapFactory.decodeStream(context.getAssets().open("function/function_" + i + ".png")));
            } catch (IOException e) {
                e.printStackTrace();
            }
            listFunction.add(f);
        }
    //    for (int i = 0; i < 1; i++) {
            views.add(viewPagerItem(0, listFunction));
     //       LayoutParams params = new LayoutParams(16, 16);
     //       llDot.addView(dotsItem(i), params);//一页不需要点
     //   }
        MessageChatFunctionAdapter adapter = new MessageChatFunctionAdapter(views);
        vpFunction.setAdapter(adapter);
//        llDot.getChildAt(0).setSelected(true);
        vpFunction.addOnPageChangeListener(new PageChange());
    }

    /**
     * 获取当前点击的按钮，触发对应事件
     *
     * @param which
     * @return
     */
    private OnClickListener getFunctionBtnListener(final int which) {

        return new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                layoutType = which;
                if (which == LAYOUT_TYPE_FACE) {
                    Util.hideKeyboard(context);
                    hideFunctionLayout();
                    showExpressionLayout();
                } else if (which == LAYOUT_TYPE_MORE) {
                    hideExpressionLayout();
                    Util.hideKeyboard(context);
                    showFunctionLayout();
                }
//                mBtnFace.setChecked(layoutType == LAYOUT_TYPE_FACE);
//                mBtnMore.setChecked(layoutType == LAYOUT_TYPE_MORE);
            }
        };
    }

    /**
     * 隐藏表情布局
     */
    public void hideExpressionLayout() {
        Global.canBack = true;
        if (mPagerFaceCagetory != null) {
            viewPagerIndex = mPagerFaceCagetory.getCurrentItem();
        }
        if (listFragment.get(mPagerFaceCagetory.getCurrentItem()).getViewPager() != null) {
            subViewPagerIndex = listFragment.get(mPagerFaceCagetory.getCurrentItem()).getViewPager().getCurrentItem();
        }
        mRlFace.setVisibility(View.GONE);
        if (mBtnFace.isChecked()) {
            mBtnFace.setChecked(false);
        }
        if (mBtnMore.isChecked()) {
            mBtnMore.setChecked(false);
        }
    }

    /**
     * 显示表情布局
     */
    public void showExpressionLayout() {
        Global.canBack = false;
        adapter = new FaceCategroyAdapter(context, ((FragmentActivity) getContext())
                .getSupportFragmentManager(), layoutType, listFragment);
        mPagerFaceCagetory.setAdapter(adapter);
        mPagerFaceCagetory.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position != 0) {
                    mEtMsg.setEnabled(false);
                } else {
                    mEtMsg.setEnabled(true);
                }
            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 1 && currentPosition > 0) {
                    isSlide = true;
                    listFragment.get(currentPosition - 1).getViewPager().setCurrentItem(listFragment.get(currentPosition - 1).getPage(), false);
                } else if (state == 0) {
                    if (!isSlide) {
                        listFragment.get(currentPosition).getViewPager().setCurrentItem(0, false);
                    } else {
                        isSlide = false;
                    }
                } else {
                    if (null != listFragment.get(currentPosition).getViewPager()) {
                        listFragment.get(currentPosition).getViewPager().setCurrentItem(0, false);
                    }
                }
            }
        });
        adapter.notifyDataSetChanged();
        mFaceTabs.setViewPager(mPagerFaceCagetory);
        Util.hideKeyboard(context);
        // 延迟一会，让键盘先隐藏再显示表情键盘，否则会有一瞬间表情键盘和软键盘同时显示
        if (((Activity) context).getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRlFace.setVisibility(View.VISIBLE);
                }
            },50);
        } else {
            mRlFace.setVisibility(View.VISIBLE);
        }

        mEtMsg.setEnabled(true);

        if (mPagerFaceCagetory != null) {
            mPagerFaceCagetory.setCurrentItem(viewPagerIndex);
        }
        if (listFragment.get(viewPagerIndex).getViewPager() != null) {
            listFragment.get(viewPagerIndex).getViewPager().setCurrentItem(subViewPagerIndex);
        }
    }

    /**
     * 显示功能布局
     */
    public void showFunctionLayout() {
        Global.canBack = false;
        llFunction.setVisibility(View.VISIBLE);

        mEtMsg.setEnabled(true);
    }

    /**
     * 隐藏功能布局
     */
    public void hideFunctionLayout() {
        Global.canBack = true;
        llFunction.setVisibility(View.GONE);
        if (mBtnFace.isChecked()) {
            mBtnFace.setChecked(false);
        }
        if (mBtnMore.isChecked()) {
            mBtnMore.setChecked(false);
        }
    }

    @Override
    public void onSoftKeyboardOpened(int keyboardHeightInPx) {
        hideExpressionLayout();
    }

    @Override
    public void onSoftKeyboardClosed() {
    }

    @Override
    public void selectedFace(Face face) {
        if (mListener != null) {
            mListener.sendExpression(face);
        }
    }

    @Override
    public void selectedEmoji(Emoji emoji) {
        EmojiUtil.insert(mEtMsg, EmojiUtil.getFace(context, "face/emojis/EmojiS_" + emoji.name + ".png"));
    }

    @Override
    public void selectedBackSpace(Emoji back) {
        String content = mEtMsg.getText().toString();
        if (content.length() > 0) {
            if (EmojiUtil.isDeletePng(content)) {
                ((Editable) mEtMsg.getText()).delete(content.length() - EmojiUtil.emojiFormat.length(), content.length());
            } else {
                ((Editable) mEtMsg.getText()).delete(content.length() - 1, content.length());
            }
        }
    }

    @Override
    public void selectedEmoticion(Emoticion emoticion) {
        EmojiUtil.insert(mEtMsg, EmojiUtil.getFace(context, "face/emoticon/" + emoticion.name + ".png"));
    }

    @Override
    public void selectedBackSpace(Emoticion emoticion) {
        String content = mEtMsg.getText().toString();
        if (content.length() > 0) {
            if (EmojiUtil.isDeleteQQPng(content)) {
                ((Editable) mEtMsg.getText()).delete(content.length() - EmojiUtil.emoticonFormat.length(), content.length());
            } else {
                ((Editable) mEtMsg.getText()).delete(content.length() - 1, content.length());
            }
        }
    }

    /**
     * 获取viewpager item
     *
     * @param position
     * @param list
     * @return
     */
    private View viewPagerItem(int position, List<Function> list) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.bjmgf_message_chat_function_gridview, null);
        GridView gv = (GridView) view.findViewById(R.id.bjmgf_message_chat_function_gv);
        List<Function> subList = new ArrayList<>();
        subList.addAll(list
                .subList(position * (columns * rows),
                        (columns * rows) * (position + 1) > list
                                .size() ? list.size() : (columns
                                * rows)
                                * (position + 1)));
        FunctionAdapter functionAdapter = new FunctionAdapter(context, subList);
        gv.setAdapter(functionAdapter);
        gv.setNumColumns(columns);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Face face = new Face();
                face.setPosition(position);
                    if (mListener != null) {
                        mListener.sendExpression(face);
                    }
            }
        });
        return gv;
    }

    /**
     * 圆点item
     *
     * @param position
     * @return
     */
    private ImageView dotsItem(int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.bjmgf_message_chat_function_dots, null);
        ImageView iv = (ImageView) layout.findViewById(R.id.bjmgf_message_chat_function_dot);
        iv.setId(position);
        return iv;
    }

    public void setListener(SendListener listener) {
        mListener = listener;
    }

    private class PageChange implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            for (int i = 0; i < llDot.getChildCount(); i++) {
                llDot.getChildAt(i).setSelected(false);
            }
            llDot.getChildAt(arg0).setSelected(true);
        }
    }

    public void setMessageIndex(int index) {
        this.msgIndex = index;
    }

    public EditText getEditText() {
        return mEtMsg;
    }

}

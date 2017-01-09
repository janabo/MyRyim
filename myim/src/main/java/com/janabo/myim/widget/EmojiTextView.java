package com.janabo.myim.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.janabo.myim.util.EmojiUtil;

/**
 * 作者：janabo on 2016/12/12 13:49
 */
public class EmojiTextView extends TextView {

    public EmojiTextView(Context context) {
        super(context);
    }

    public EmojiTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmojiTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        text = EmojiUtil.convert(getContext(), text.toString());
        super.setText(text, type);
    }
}

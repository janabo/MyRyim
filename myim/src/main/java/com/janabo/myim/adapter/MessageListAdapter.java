package com.janabo.myim.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.janabo.myim.ImageDetailActivity;
import com.janabo.myim.R;
import com.janabo.myim.entity.Message;
import com.janabo.myim.util.ImageUtil;
import com.janabo.myim.util.Util;
import com.janabo.myim.widget.EmojiTextView;

import org.xutils.image.ImageOptions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 作者：janabo on 2016/12/12 13:45
 */
public class MessageListAdapter extends BaseAdapter {

    private Context mContext = null;
    private List<Message> mList = null;
    private LayoutInflater inflater;

    public MessageListAdapter(Context context, List<Message> list) {
        mContext = context;
        mList = list;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).isFlag() ? 1 : 0;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Message msg = mList.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            if (msg.isFlag()) {
                convertView = View.inflate(mContext, R.layout.bjmgf_message_chat_list_msg_info_me_item, null);
            } else {
                convertView = View.inflate(mContext, R.layout.bjmgf_message_chat_list_msg_info_you_item, null);
            }
            holder.tvChat = (EmojiTextView)convertView.findViewById(R.id.bjmgf_message_chat_msg_item_tv);
            holder.tvTime = (TextView)convertView.findViewById(R.id.bjmgf_message_chat_time_tv);
            holder.ivFace = (CircleImageView)convertView.findViewById(R.id.bjmgf_message_chat_msg_item_face_iv);
            holder.ivChat = (ImageView)convertView.findViewById(R.id.bjmgf_message_chat_msg_item_expression);
            holder.ivImg = (ImageView)convertView.findViewById(R.id.bjmgf_message_chat_msg_item_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        ImageOptions imageOptions = new ImageOptions.Builder()
                // 是否忽略GIF格式的图片
                .setIgnoreGif(false)
                // 图片缩放模式
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                // 得到ImageOptions对象
                .build();

        if (mList.get(position).getExpression() != null) {
            holder.ivChat.setImageBitmap(mList.get(position).getExpression());
            holder.tvChat.setVisibility(View.GONE);
            holder.ivImg.setVisibility(View.GONE);
            holder.ivChat.setVisibility(View.VISIBLE);
        } else if(Util.isNotEmpty(mList.get(position).getUrlimg())) {
//            if(mList.get(position).isbd()){
//                x.image().bind(holder.ivImg, mList.get(position).getUrlimg(), imageOptions);
//            }else {
                ImageUtil.displayImage(mContext, mList.get(position).getUrlimg(), holder.ivImg);
//            }
            holder.tvChat.setVisibility(View.GONE);
            holder.ivImg.setVisibility(View.VISIBLE);
            holder.ivChat.setVisibility(View.GONE);

            final String imgurl = mList.get(position).getUrlimg();
            final boolean isbd = mList.get(position).isbd();
            holder.ivImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ImageDetailActivity.class);
                    intent.putExtra("imgurl",imgurl);
                    intent.putExtra("isbd",isbd);
                    mContext.startActivity(intent);
                }
            });
        }else {
            holder.tvChat.setText(msg.getInfo());
            holder.ivChat.setVisibility(View.GONE);
            holder.tvChat.setVisibility(View.VISIBLE);
            holder.ivImg.setVisibility(View.GONE);
        }


        return convertView;
    }

    static class ViewHolder {
        TextView tvTime;
        EmojiTextView tvChat;
        ImageView ivChat;
        CircleImageView ivFace;
        ImageView ivImg;
    }


    /**
     * 加载本地图片
     * http://bbs.3gstdy.com
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}

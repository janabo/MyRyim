package com.janabo.myim.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.widget.EditText;

import com.google.gson.Gson;
import com.janabo.myim.R;
import com.janabo.myim.entity.Emoji;
import com.janabo.myim.entity.Emoticion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.janabo.myim.R.raw.emoji;

/**
 * emoji工具类
 * Created by sunhaoyang on 2016/2/24.
 */
public class EmojiUtil {
    //emoji表情格式
    public static String emojiFormat = "#[face/emojis/EmojiS_000.png]#";

    //emoji表情格式
    public static String emoticonFormat = "#[face/emoticon/f000.png]#";

    //emoji表情正则表达式
    public static String emojiRegex = "(\\#\\[face/emojis/EmojiS_)\\d{3}(.png\\]\\#)";

    //QQ表情正则表达式
    public static String emoticonRegex = "(\\#\\[face/emoticon/f)\\d{3}(.png\\]\\#)";

    public static String emoticonRegex2 = "(\\[[\u4E00-\u9FA5]+\\])|(\\[bye\\])|(\\[汗1\\])|(\\[汗2\\])" +
            "|(\\[什么2\\])|(\\[累2\\])|(\\[难过1\\])|(\\[难过2\\])|(\\[吓1\\])";

    /**
     * 把emoji_code json转换
     *
     * @param context
     * @return
     */
    public static Map<String, String> parseEmojiCode(Context context) {
        InputStream isEmojiCode = context.getResources().openRawResource(R.raw.emojicode);
        Map<String, String> maps = new HashMap<>();
        try {
            byte[] bEmojiCode = new byte[isEmojiCode.available()];
            isEmojiCode.read(bEmojiCode);
            String jEmojiCode = new String(bEmojiCode, "UTF-8");
            JSONArray jEmojiCodeArray = new JSONArray(jEmojiCode);
            for (int i = 0; i < jEmojiCodeArray.length(); i++) {
                JSONObject item = jEmojiCodeArray.getJSONObject(i);
                String id = "";
                int val = Integer.parseInt(item.getString("id"));
                if (val >= 100) {
                    id = item.getString("id");
                } else if (val < 100 && val >= 10) {
                    id = "0" + item.getString("id");
                } else {
                    id = "00" + item.getString("id");
                }
                maps.put(item.getString("sign"), id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maps;
    }

    /**
     * 把emoji_code json转换
     *
     * @param context
     * @return
     */
    public static Map<String, String> parseEmojiCode2(Context context) {
        InputStream isEmojiCode = context.getResources().openRawResource(R.raw.emojicode);
        Map<String, String> maps = new HashMap<>();
        try {
            byte[] bEmojiCode = new byte[isEmojiCode.available()];
            isEmojiCode.read(bEmojiCode);
            String jEmojiCode = new String(bEmojiCode, "UTF-8");
            JSONArray jEmojiCodeArray = new JSONArray(jEmojiCode);
            for (int i = 0; i < jEmojiCodeArray.length(); i++) {
                JSONObject item = jEmojiCodeArray.getJSONObject(i);
                String id = "";
                int val = Integer.parseInt(item.getString("id"));
                if (val >= 100) {
                    id = item.getString("id");
                } else if (val < 100 && val >= 10) {
                    id = "0" + item.getString("id");
                } else {
                    id = "00" + item.getString("id");
                }
                maps.put(id, item.getString("sign"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maps;
    }

    /**
     * 把emoji json转换
     *
     * @param context
     * @return
     */
    public static List<Emoji> parseEmoji(Context context) {
        List<Emoji> list = new ArrayList<>();
        InputStream isEmoji = context.getResources().openRawResource(emoji);
        try {
            byte[] bEmoji = new byte[isEmoji.available()];
            isEmoji.read(bEmoji);
            String jEmoji = new String(bEmoji, "UTF-8");
            JSONArray jEmojiArray = new JSONArray(jEmoji);
            for (int i = 0; i < jEmojiArray.length(); i++) {
                JSONObject item = jEmojiArray.getJSONObject(i);
                String name = "";
                int val = Integer.parseInt(item.getString("Name"));
                if (val >= 100) {
                    name = item.getString("Name");
                } else if (val < 100 && val >= 10) {
                    name = "0" + item.getString("Name");
                } else {
                    name = "00" + item.getString("Name");
                }
                Emoji emoji = new Emoji(item.getString("ID"), name, item.getString("Show"), "", "");
                list.add(emoji);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }

    /**
     * 解析QQ表情
     * @param context
     * @return
     */
    public static List<Emoticion> parseEmotioion(Context context){
        List<Emoticion> list = new ArrayList<>();
        InputStream isEmoji = context.getResources().openRawResource(R.raw.emoticion);
        try {
            byte[] bEmoji = new byte[isEmoji.available()];
            isEmoji.read(bEmoji);
            String jEmoji = new String(bEmoji, "UTF-8");
            JSONArray jEmojiArray = new JSONArray(jEmoji);
            for (int i = 0; i < jEmojiArray.length(); i++) {
                JSONObject item = jEmojiArray.getJSONObject(i);
                Emoticion emoticion = new Gson().fromJson(item.toString(),Emoticion.class);
                list.add(emoticion);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }


    /**
     * 把QQ表情 json转换
     *
     * @param context
     * @return
     */
    public static Map<String, String> parseEmotioionCode(Context context) {
        InputStream isEmojiCode = context.getResources().openRawResource(R.raw.emoticion);
        Map<String, String> maps = new HashMap<>();
        try {
            byte[] bEmojiCode = new byte[isEmojiCode.available()];
            isEmojiCode.read(bEmojiCode);
            String jEmojiCode = new String(bEmojiCode, "UTF-8");
            JSONArray jEmojiCodeArray = new JSONArray(jEmojiCode);
            for (int i = 0; i < jEmojiCodeArray.length(); i++) {
                JSONObject item = jEmojiCodeArray.getJSONObject(i);
                Emoticion emoticion = new Gson().fromJson(item.toString(),Emoticion.class);
                maps.put(emoticion.getName(), "["+emoticion.getImgname()+"]");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maps;
    }

    /**
     * 把QQ表情 json转换
     *
     * @param context
     * @return
     */
    public static Map<String, String> parseEmotioionCode2(Context context) {
        InputStream isEmojiCode = context.getResources().openRawResource(R.raw.emoticion);
        Map<String, String> maps = new HashMap<>();
        try {
            byte[] bEmojiCode = new byte[isEmojiCode.available()];
            isEmojiCode.read(bEmojiCode);
            String jEmojiCode = new String(bEmojiCode, "UTF-8");
            JSONArray jEmojiCodeArray = new JSONArray(jEmojiCode);
            for (int i = 0; i < jEmojiCodeArray.length(); i++) {
                JSONObject item = jEmojiCodeArray.getJSONObject(i);
                Emoticion emoticion = new Gson().fromJson(item.toString(),Emoticion.class);
                maps.put("["+emoticion.getImgname()+"]", emoticion.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maps;
    }


    /**
     * 获取表情，显示在textview和edittext中
     *
     * @param context
     * @param png
     * @return
     */
    public static SpannableStringBuilder getFace(Context context, String png) {
        SpannableStringBuilder sb = new SpannableStringBuilder();
        try {
            /**
             * 经过测试，虽然这里tempText被替换为png显示，但是但我单击发送按钮时，获取到輸入框的内容是tempText的值而不是png
             * 所以这里对这个tempText值做特殊处理
             * 格式：#[face/emoji/Emoji_000.png]#，以方便判斷當前圖片是哪一個
             * */
            String tempText = "#[" + png + "]#";
            sb.append(tempText);
            Bitmap bm = BitmapFactory
                    .decodeStream(context.getAssets().open(png));

            sb.setSpan(
                    new ImageSpan(context, bm), sb.length()
                            - tempText.length(), sb.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb;
    }


    /**
     * 获取表情，显示在textview和edittext中
     *
     * @param context
     * @param png
     * @return
     */
    public static SpannableStringBuilder getFace2(Context context, String png) {
        SpannableStringBuilder sb = new SpannableStringBuilder();
        try {
            /**
             * 经过测试，虽然这里tempText被替换为png显示，但是但我单击发送按钮时，获取到輸入框的内容是tempText的值而不是png
             * 所以这里对这个tempText值做特殊处理
             * 格式：#[face/emoji/Emoji_000.png]#，以方便判斷當前圖片是哪一個
             * */
            String tempText = png ;
            sb.append(tempText);
//            sb.setSpan(
//                    new ImageSpan(context, BitmapFactory
//                            .decodeStream(context.getAssets().open(png))), sb.length()
//                            - tempText.length(), sb.length(),
//                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb;
    }

    /**
     * 判断当前内容是否表情
     *
     * @param content
     * @return
     */
    public static boolean isDeletePng(String content) {
        if (content.length() >= EmojiUtil.emojiFormat.length()) {
            String checkStr = content.substring(content.length() - EmojiUtil.emojiFormat.length(),
                    content.length());
            Pattern p = Pattern.compile(EmojiUtil.emojiRegex);
            Matcher m = p.matcher(checkStr);
            return m.matches();
        }
        return false;
    }

    /**
     * 判断当前内容是否表情
     *
     * @param content
     * @return
     */
    public static boolean isDeleteQQPng(String content) {
        if (content.length() >= EmojiUtil.emoticonFormat.length()) {
            String checkStr = content.substring(content.length() - EmojiUtil.emoticonFormat.length(),
                    content.length());
            Pattern p = Pattern.compile(EmojiUtil.emoticonRegex);
            Matcher m = p.matcher(checkStr);
            return m.matches();
        }
        return false;
    }

    /**
     * 转换QQ表情
     *
     * @param context
     * @param content
     * @return
     */
    public static SpannableStringBuilder convert(Context context, String content) {
        SpannableStringBuilder sb = new SpannableStringBuilder(content);
        Pattern p = Pattern.compile(EmojiUtil.emoticonRegex);
        Matcher m = p.matcher(content);
        while (m.find()) {
            String tempText = m.group();
            String png = tempText.substring("#[".length(), tempText.length() - "]#".length());
            try {
                sb.setSpan(new ImageSpan(context, BitmapFactory.decodeStream(context.getAssets().open(png))), m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Pattern p2 = Pattern.compile(EmojiUtil.emoticonRegex2);
        Matcher m2 = p2.matcher(content);
        while (m2.find()) {
            String tempText = m2.group();
            String png = "face/emoticon/"+Global.EMOTICIONCODE2.get(tempText)+".png";
            try {
                sb.setSpan(new ImageSpan(context, BitmapFactory.decodeStream(context.getAssets().open(png))), m2.start(), m2.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb;
    }

    /**
     * 转换QQ表情
     *
     * @return
     */
    public static String convertToEmoji(String content) {
        StringBuffer sb = new StringBuffer(content);
        String con = sb.toString();
        Pattern p = Pattern.compile(EmojiUtil.emoticonRegex);
        Matcher m = p.matcher(content);
        while (m.find()) {
            String tempText = m.group();
            String png = tempText.substring("#[face/emoticon/".length(), tempText.length() - ".png]#".length());
            String s = "#\\[face/emoticon/";
            s = s + png +".png\\]#";
            String sign = Global.EMOTICIONCODE.get(png);
            con = con.replaceAll(s,sign);
        }
        return con;
    }

    /**
     * 转换emoji表情，带草稿前缀
     *
     * @param context
     * @param content
     * @return
     */
    public static SpannableStringBuilder convertDraft(Context context, String content) {
        SpannableStringBuilder sb = convert(context, content);
        ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
        sb.setSpan(redSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }

    /**
     * 插入emoji
     * @param text
     */
    public static void insert(EditText editText, CharSequence text) {
        int iCursorStart = Selection.getSelectionStart((editText.getText()));
        int iCursorEnd = Selection.getSelectionEnd((editText.getText()));
        if (iCursorStart != iCursorEnd) {
            ((Editable) editText.getText()).replace(iCursorStart, iCursorEnd, "");
        }
        int iCursor = Selection.getSelectionEnd((editText.getText()));
        ((Editable) editText.getText()).insert(iCursor, text);
    }

    public static Bitmap getCreateNewBitmap(Bitmap bm){
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) 35) / width;
        float scaleHeight = ((float) 35) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, 35, 35, matrix, true);
        return newbm;
    }
}

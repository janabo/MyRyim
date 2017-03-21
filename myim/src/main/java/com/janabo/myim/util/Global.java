package com.janabo.myim.util;

import com.janabo.myim.entity.Emoji;
import com.janabo.myim.entity.Emoticion;
import com.janabo.myim.entity.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by janabo on 2016-12-11.
 */

public class Global {
    public static List<Emoji> EMOJIS = null;
    public static Map<String, String> EMOJISCODE = null;
    public static Map<String, String> EMOJISCODE2 = null;
    public static List<Emoticion> EMOTICIONS = null;
    public static Map<String, String> EMOTICIONCODE = null;
    public static List<Message> MESSAGES = new ArrayList<>();
    public static boolean canBack = true;
}

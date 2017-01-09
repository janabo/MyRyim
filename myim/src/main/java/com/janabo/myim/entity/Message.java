package com.janabo.myim.entity;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * 消息
 * Created by sunhaoyang on 2016/2/22.
 */
public class Message implements Serializable {

    private String nickName = "";
    private String info = "";
    private String date = "";
    private String head = "";
    private Bitmap face = null;
    private boolean flag = false;
    private Bitmap expression = null;
    private boolean isTop = false;
    private int oldPosition = 0;
    private String draft = "";
    private String urlimg;
    private boolean isbd = true;//是否是本地图片

    public String getDraft() {
        return draft;
    }

    public void setDraft(String draft) {
        this.draft = draft;
    }

    public int getOldPosition() {
        return oldPosition;
    }

    public void setOldPosition(int oldPosition) {
        this.oldPosition = oldPosition;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public Bitmap getFace() {
        return face;
    }

    public void setFace(Bitmap face) {
        this.face = face;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Bitmap getExpression() {
        return expression;
    }

    public void setExpression(Bitmap expression) {
        this.expression = expression;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }

    public Message(){}

    public Message(String info,  boolean flag, String urlimg,boolean isbd) {
        this.info = info;
        this.flag = flag;
        this.urlimg = urlimg;
        this.isbd = isbd;
    }

    public Message(String nickName, String info, String date, String head, boolean flag, boolean isTop) {
        this.nickName = nickName;
        this.info = info;
        this.date = date;
        this.head = head;
        this.flag = flag;
        this.isTop = isTop;
    }

    public Message(String nickName, String info, String date, String head, boolean flag, Bitmap expression, boolean isTop) {
        this.nickName = nickName;
        this.info = info;
        this.date = date;
        this.head = head;
        this.flag = flag;
        this.expression = expression;
        this.isTop = isTop;
    }

    public Message(String nickName, String info, String date, String head, Bitmap face) {
        this.nickName = nickName;
        this.info = info;
        this.date = date;
        this.head = head;
        this.face = face;
    }

    public String getUrlimg() {
        return urlimg;
    }

    public void setUrlimg(String urlimg) {
        this.urlimg = urlimg;
    }

    public boolean isbd() {
        return isbd;
    }

    public void setIsbd(boolean isbd) {
        this.isbd = isbd;
    }
}

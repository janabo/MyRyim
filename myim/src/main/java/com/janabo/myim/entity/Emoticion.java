package com.janabo.myim.entity;

/**
 * QQ表情
 * 作者：janabo on 2017/3/17 16:17
 */
public class Emoticion {

    public String id;
    public String name;
    public String imgname;

    public Emoticion() {
    }

    public Emoticion(String id, String name, String imgname) {
        this.id = id;
        this.name = name;
        this.imgname = imgname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgname() {
        return imgname;
    }

    public void setImgname(String imgname) {
        this.imgname = imgname;
    }
}

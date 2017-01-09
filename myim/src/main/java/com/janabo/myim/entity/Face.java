package com.janabo.myim.entity;

/**
 * 表情
 * Created by sunhaoyang on 2016/2/29.
 */
public class Face {

    public String id;
    public String name;
    public String show;
    public String value;
    public String path;
    public int position;
    public boolean isShow;

    public Face() {
    }

    public Face(String id, String name, String show, String value, String path, boolean isShow) {
        this.id = id;
        this.name = name;
        this.show = show;
        this.value = value;
        this.path = path;
        this.isShow = isShow;
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

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}

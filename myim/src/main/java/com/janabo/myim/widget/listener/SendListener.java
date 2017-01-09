package com.janabo.myim.widget.listener;


import com.janabo.myim.entity.Face;

public interface SendListener {

    void sendMsg(String text);

    void sendExpression(Face face);
}

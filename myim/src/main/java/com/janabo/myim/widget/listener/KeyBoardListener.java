package com.janabo.myim.widget.listener;


import com.janabo.myim.entity.Emoji;
import com.janabo.myim.entity.Face;

public interface KeyBoardListener {

    void selectedFace(Face face);

    void selectedEmoji(Emoji emoji);
    
    void selectedBackSpace(Emoji back);
}

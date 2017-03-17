package com.janabo.myim.widget.listener;


import com.janabo.myim.entity.Emoji;
import com.janabo.myim.entity.Emoticion;
import com.janabo.myim.entity.Face;

public interface KeyBoardListener {

    void selectedFace(Face face);

    void selectedEmoji(Emoji emoji);
    
    void selectedBackSpace(Emoji back);

    void selectedEmoticion(Emoticion emoticion);

    void selectedBackSpace(Emoticion emoticion);
}

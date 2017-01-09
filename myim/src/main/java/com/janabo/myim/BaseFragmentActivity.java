package com.janabo.myim;

import android.support.v4.app.FragmentActivity;
import android.view.View;

/**
 * Created by janabo on 2016-12-11.
 */

public class BaseFragmentActivity extends FragmentActivity {

    public final <E extends View> E getView(int id) {
        try {
            return (E) findViewById(id);
        } catch (ClassCastException ex) {
            throw ex;
        }
    }
}

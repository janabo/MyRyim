package com.janabo.myim;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

/**
 * Created by janabo on 2016-12-11.
 */

public class BaseFaceFragment extends Fragment {

    protected ViewPager mPagerFace = null;
    protected int pages = 0;

    public ViewPager getViewPager() {
        return mPagerFace;
    }

    public int getPage() {
        return pages;
    }
}

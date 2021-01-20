package com.yunbao.main.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.yunbao.common.utils.WordUtil;
import com.yunbao.main.R;
import com.yunbao.main.interfaces.TopTitleListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxf on 2018/9/22.
 * 附近
 */

public class MainNearViewHolder extends AbsMainHomeParentViewHolder {

    private MainNearNearViewHolder mNearViewHolder;

    public MainNearViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_main_near;
    }

    @Override
    protected void loadPageData(int position) {
        if (mViewHolders == null) {
            return;
        }
        AbsMainHomeChildViewHolder vh = mViewHolders[position];
        if (vh == null) {
            if (mViewList != null && position < mViewList.size()) {
                FrameLayout parent = mViewList.get(position);
                if (parent == null) {
                    return;
                }
                if (position == 0) {
                    mNearViewHolder = new MainNearNearViewHolder(mContext, parent);
                    vh = mNearViewHolder;
                }
                if (vh == null) {
                    return;
                }
                mViewHolders[position] = vh;
                vh.addToParent();
                vh.subscribeActivityLifeCycle();
            }
        }
        if (vh != null) {
            vh.loadData();
        }

        setTopTitleListener(new TopTitleListener() {
            @Override
            public void onTopTitle(int index) {
                if (mViewPager != null) {
                    mViewPager.setCurrentItem(index);
                }
            }
        });
    }

    @Override
    protected int getPageCount() {
        return 1;
    }

    @Override
    protected List<String> getTitles() {
        List<String> list = new ArrayList<>();
        list.add(WordUtil.getString(R.string.near));
        return list;
    }

    @Override
    public void loadData() {
        loadPageData(0);
    }
}

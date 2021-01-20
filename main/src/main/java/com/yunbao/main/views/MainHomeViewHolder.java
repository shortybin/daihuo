package com.yunbao.main.views;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.yunbao.common.utils.WordUtil;
import com.yunbao.main.R;
import com.yunbao.main.activity.ImageShowActivity;
import com.yunbao.main.bean.HomeTopTagBean;
import com.yunbao.main.interfaces.TopTitleListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxf on 2018/9/22.
 * MainActivity 首页
 */

public class MainHomeViewHolder extends AbsMainHomeParentViewHolder {

    private MainHomeFollowViewHolder mFollowViewHolder;
    private MainHomeLiveViewHolder mLiveViewHolder;
    private MainHomeVideoViewHolder mVideoViewHolder;
    private List<String> mTitleList;
    private List<HomeTopTagBean> mHomeTopTagBeans;


    public MainHomeViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_main_home;
    }

    @Override
    public void init() {
        mTitleList = new ArrayList<>();
        mTitleList.add(WordUtil.getString(R.string.follow));
        mTitleList.add(WordUtil.getString(R.string.live));
        mTitleList.add(WordUtil.getString(R.string.video));

        mHomeTopTagBeans = new ArrayList<>();

        HomeTopTagBean homeTopTagBean = new HomeTopTagBean();
        homeTopTagBean.setCustom_tag_name(WordUtil.getString(R.string.follow));
        mHomeTopTagBeans.add(homeTopTagBean);

        HomeTopTagBean homeTopTagBean1 = new HomeTopTagBean();
        homeTopTagBean1.setCustom_tag_name(WordUtil.getString(R.string.live));
        mHomeTopTagBeans.add(homeTopTagBean1);

        HomeTopTagBean homeTopTagBean2 = new HomeTopTagBean();
        homeTopTagBean2.setCustom_tag_name(WordUtil.getString(R.string.video));
        mHomeTopTagBeans.add(homeTopTagBean2);
        super.init();
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
                    mFollowViewHolder = new MainHomeFollowViewHolder(mContext, parent);
                    vh = mFollowViewHolder;
                } else if (position == 1) {
                    mLiveViewHolder = new MainHomeLiveViewHolder(mContext, parent, new MainHomeLiveViewHolder.HomeTopTagCallback() {
                        @Override
                        public void tagList(List<HomeTopTagBean> list) {
                            for (int i = 0; i < list.size(); i++) {
                                mTitleList.add(list.get(i).getCustom_tag_name());
                            }
                            mHomeTopTagBeans.addAll(list);
                            setMagicIndicator();
                        }
                    });

                    vh = mLiveViewHolder;
                } else if (position == 2) {
                    mVideoViewHolder = new MainHomeVideoViewHolder(mContext, parent);
                    vh = mVideoViewHolder;
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
                if (index > 2) {
                    Intent intent = new Intent(mContext, ImageShowActivity.class);
                    intent.putExtra("data", mHomeTopTagBeans.get(index));
                    mContext.startActivity(intent);
                } else {
                    if (mViewPager != null) {
                        mViewPager.setCurrentItem(index);
                    }
                }
            }
        });
    }

    @Override
    protected int getPageCount() {
        return 3;
    }

    @Override
    protected List<String> getTitles() {
        return mTitleList;
    }


}

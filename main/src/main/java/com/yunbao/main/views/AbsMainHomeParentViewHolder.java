package com.yunbao.main.views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunbao.common.adapter.ViewPagerAdapter;
import com.yunbao.common.utils.DpUtil;
import com.yunbao.im.utils.ImMessageUtil;
import com.yunbao.main.R;
import com.yunbao.main.interfaces.AppBarStateListener;
import com.yunbao.main.interfaces.MainAppBarExpandListener;
import com.yunbao.main.interfaces.MainAppBarLayoutListener;
import com.yunbao.main.interfaces.TopTitleListener;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxf on 2019/2/20.
 */

public abstract class AbsMainHomeParentViewHolder extends AbsMainViewHolder {

    private AppBarLayout mAppBarLayout;
    protected ViewPager mViewPager;
    private MagicIndicator mIndicator;
    private TextView mRedPoint;//显示未读消息数量的红点
    protected AbsMainHomeChildViewHolder[] mViewHolders;
    private MainAppBarLayoutListener mAppBarLayoutListener;
    private MainAppBarExpandListener mAppBarExpandListener;
    private boolean mPaused;
    protected List<FrameLayout> mViewList;
    private int mAppLayoutOffestY;
    private CommonNavigatorAdapter commonNavigatorAdapter;
    private TopTitleListener mTopTitleListener;

    public AbsMainHomeParentViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    public void init() {
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarStateListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, int state) {
                switch (state) {
                    case AppBarStateListener.EXPANDED:
                        if (mAppBarExpandListener != null) {
                            mAppBarExpandListener.onExpand(true);
                        }
                        break;
                    case AppBarStateListener.MIDDLE:
                    case AppBarStateListener.COLLAPSED:
                        if (mAppBarExpandListener != null) {
                            mAppBarExpandListener.onExpand(false);
                        }
                        break;
                }
            }
        });
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (mAppBarLayoutListener != null) {
                    if (verticalOffset > mAppLayoutOffestY) {
                        mAppBarLayoutListener.onOffsetChanged(false);
                    } else if (verticalOffset < mAppLayoutOffestY) {
                        mAppBarLayoutListener.onOffsetChanged(true);
                    }
                    mAppLayoutOffestY = verticalOffset;
                }
            }
        });
        mViewList = new ArrayList<>();
        int pageCount = getPageCount();
        for (int i = 0; i < pageCount; i++) {
            FrameLayout frameLayout = new FrameLayout(mContext);
            frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mViewList.add(frameLayout);
        }
        mViewHolders = new AbsMainHomeChildViewHolder[pageCount];
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        if (pageCount > 1) {
            mViewPager.setOffscreenPageLimit(pageCount - 1);
        }
        mViewPager.setAdapter(new ViewPagerAdapter(mViewList));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                loadPageData(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mIndicator = (MagicIndicator) findViewById(R.id.indicator);

        final List<String> titles = getTitles();
        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigatorAdapter = new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return titles.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                CommonPagerTitleView commonPagerTitleView = new CommonPagerTitleView(context);
                commonPagerTitleView.setContentView(R.layout.home_page_title_view);
                final ImageView image = commonPagerTitleView.findViewById(R.id.image);
                final TextView title = commonPagerTitleView.findViewById(R.id.title);

                title.setText(titles.get(index));
                if (titles.size() >= 3) {
                    if (titles.get(0).equals("关注")) {
                        if (index == 0) {
                            image.setImageResource(R.mipmap.icon_home_top_follow);
                        } else if (index == 1) {
                            image.setImageResource(R.mipmap.icon_home_top_live);
                        } else if (index == 2) {
                            image.setImageResource(R.mipmap.icon_home_top_video);
                        }
                    }
                }

                commonPagerTitleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {

                    @Override
                    public void onSelected(int index, int totalCount) {
                        title.setTextColor(ContextCompat.getColor(mContext, R.color.textColor));
                        image.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onDeselected(int index, int totalCount) {
                        title.setTextColor(ContextCompat.getColor(mContext, R.color.gray1));
                        image.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {

                    }

                    @Override
                    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {

                    }

                });
                commonPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mTopTitleListener.onTopTitle(index);
                    }
                });

                return commonPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                linePagerIndicator.setXOffset(DpUtil.dp2px(5));
                linePagerIndicator.setRoundRadius(DpUtil.dp2px(2));
                linePagerIndicator.setColors(ContextCompat.getColor(mContext, R.color.global));
                return linePagerIndicator;
            }
        };

        commonNavigator.setAdapter(commonNavigatorAdapter);
        mIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mIndicator, mViewPager);

        mRedPoint = (TextView) findViewById(R.id.red_point);
        String unReadCount = ImMessageUtil.getInstance().getAllUnReadMsgCount();
        setUnReadCount(unReadCount);
    }


    /**
     * 设置AppBarLayout滑动监听
     */
    public void setAppBarLayoutListener(MainAppBarLayoutListener appBarLayoutListener) {
        mAppBarLayoutListener = appBarLayoutListener;
    }

    /**
     * 设置AppBarLayout展开监听
     */
    public void setAppBarExpandListener(MainAppBarExpandListener appBarExpandListener) {
        mAppBarExpandListener = appBarExpandListener;
    }

    public void setTopTitleListener(TopTitleListener topTitleListener) {
        mTopTitleListener = topTitleListener;
    }

    @Override
    public void loadData() {
        if (mViewPager != null) {
            loadPageData(mViewPager.getCurrentItem());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isShowed() && mPaused) {
            loadData();
        }
        mPaused = false;
    }


    @Override
    public void onPause() {
        super.onPause();
        mPaused = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAppBarLayoutListener = null;
        mAppBarExpandListener = null;
        super.onDestroy();
    }

    public void setCurrentPage(int position) {
        if (mViewPager == null) {
            return;
        }
        if (mViewPager.getCurrentItem() == position) {
            loadPageData(position);
        } else {
            mViewPager.setCurrentItem(position, false);
        }
    }

    /**
     * 显示未读消息
     */
    public void setUnReadCount(String unReadCount) {
        if (mRedPoint != null) {
            if ("0".equals(unReadCount)) {
                if (mRedPoint.getVisibility() == View.VISIBLE) {
                    mRedPoint.setVisibility(View.INVISIBLE);
                }
            } else {
                if (mRedPoint.getVisibility() != View.VISIBLE) {
                    mRedPoint.setVisibility(View.VISIBLE);
                }
                mRedPoint.setText(unReadCount);
            }
        }
    }

    protected abstract void loadPageData(int position);

    protected abstract int getPageCount();

    protected abstract List<String> getTitles();

    void setMagicIndicator() {
        commonNavigatorAdapter.notifyDataSetChanged();
    }
}

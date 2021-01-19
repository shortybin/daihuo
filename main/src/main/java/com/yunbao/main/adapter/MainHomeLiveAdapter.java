package com.yunbao.main.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;

import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.yunbao.common.activity.WebViewActivity;
import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.utils.DpUtil;
import com.yunbao.live.bean.GroupBannerBean;
import com.yunbao.live.bean.LiveBean;
import com.yunbao.main.R;
import com.yunbao.main.bean.BannerBean;
import com.yunbao.main.utils.MainIconUtil;

import java.util.List;

/**
 * Created by cxf on 2018/9/26.
 * 首页 直播
 */

public class MainHomeLiveAdapter extends RefreshAdapter<LiveBean> {

    private static final int HEAD = 0;
    private static final int LEFT = 1;
    private static final int RIGHT = 2;
    private static final int BANNERLIST = 3;

    private View.OnClickListener mOnClickListener;
    private View mHeadView;

    public MainHomeLiveAdapter(Context context) {
        super(context);
        mHeadView = mInflater.inflate(R.layout.item_main_home_live_head, null, false);
        mHeadView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DpUtil.dp2px(184)));
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!canClick()) {
                    return;
                }
                Object tag = v.getTag();
                if (tag != null) {
                    int position = (int) tag;
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(mList.get(position), position);
                    }
                }
            }
        };
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int viewType = getItemViewType(position);
                    if (viewType == BANNERLIST || viewType == HEAD) {
                        return 2;
                    } else {
                        return 1;
                    }
                }
            });
        }
    }

    public View getHeadView() {
        return mHeadView;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEAD;
        } else if (mList.get(position - 1).getViewType() == 0) {
            return BANNERLIST;
        } else if (mList.get(position - 1).getViewType() == 1) {
            return LEFT;
        } else if (mList.get(position - 1).getViewType() == 2) {
            return RIGHT;
        }
        return LEFT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == HEAD) {
            ViewParent viewParent = mHeadView.getParent();
            if (viewParent != null) {
                ((ViewGroup) viewParent).removeView(mHeadView);
            }
            HeadVh headVh = new HeadVh(mHeadView);
            headVh.setIsRecyclable(false);
            return headVh;
        } else if (viewType == LEFT) {
            return new Vh(mInflater.inflate(R.layout.item_main_home_live_left, parent, false));
        } else if (viewType == RIGHT) {
            return new Vh(mInflater.inflate(R.layout.item_main_home_live_right, parent, false));
        } else if (viewType == BANNERLIST) {
            return new BannerVh(mInflater.inflate(R.layout.item_main_home_banner_list, parent, false));
        }
        return new Vh(mInflater.inflate(R.layout.item_main_home_live_right, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {
        if (vh instanceof Vh) {
            ((Vh) vh).setData(mList.get(position - 1), position - 1);
        } else if (vh instanceof BannerVh) {
            ((BannerVh) vh).setData(mList.get(position - 1).getGroupBannerBeanList());
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    class HeadVh extends RecyclerView.ViewHolder {
        public HeadVh(View itemView) {
            super(itemView);
        }
    }

    class BannerVh extends RecyclerView.ViewHolder {
        Banner mBanner;

        public BannerVh(View itemView) {
            super(itemView);
            mBanner = itemView.findViewById(R.id.banner);
        }

        void setData(final List<GroupBannerBean> list) {
            if (list.size() > 0) {
                mBanner.setVisibility(View.VISIBLE);
                mBanner.setImageLoader(new ImageLoader() {
                    @Override
                    public void displayImage(Context context, Object path, ImageView imageView) {
                        ImgLoader.display(mContext, ((GroupBannerBean) path).getGroup_image_image(), imageView);
                    }
                });
                mBanner.setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int p) {
                        if (list != null) {
                            if (p >= 0 && p < list.size()) {
                                GroupBannerBean bean = list.get(p);
                                if (bean != null) {
                                    String link = bean.getGroup_image_external_links();
                                    if (!TextUtils.isEmpty(link)) {
                                        WebViewActivity.forward(mContext, link, false);
                                    }
                                }
                            }
                        }
                    }
                });
                mBanner.setImages(list);
                mBanner.start();
            } else {
                mBanner.setVisibility(View.GONE);
            }
        }
    }

    class Vh extends RecyclerView.ViewHolder {

        ImageView mCover;
        ImageView mAvatar;
        TextView mName;
        TextView mTitle;
        TextView mNum;
        ImageView mType;
        ImageView mImgGoodsIcon;

        public Vh(View itemView) {
            super(itemView);
            mCover = (ImageView) itemView.findViewById(R.id.cover);
            mAvatar = (ImageView) itemView.findViewById(R.id.avatar);
            mName = (TextView) itemView.findViewById(R.id.name);
            mTitle = (TextView) itemView.findViewById(R.id.title);
            mNum = (TextView) itemView.findViewById(R.id.num);
            mType = (ImageView) itemView.findViewById(R.id.type);
            mImgGoodsIcon = (ImageView) itemView.findViewById(R.id.img_goods_icon);
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(LiveBean bean, int position) {
            itemView.setTag(position);
            ImgLoader.display(mContext, bean.getThumb(), mCover);
            ImgLoader.display(mContext, bean.getAvatar(), mAvatar);
            mName.setText(bean.getUserNiceName());
            if (TextUtils.isEmpty(bean.getTitle())) {
                if (mTitle.getVisibility() == View.VISIBLE) {
                    mTitle.setVisibility(View.GONE);
                }
            } else {
                if (mTitle.getVisibility() != View.VISIBLE) {
                    mTitle.setVisibility(View.VISIBLE);
                }
                mTitle.setText(bean.getTitle());
            }


            if (bean.getIsshop() == 1) {
                ImgLoader.display(mContext, R.mipmap.icon_live_shopping, mImgGoodsIcon);
            } else {
                mImgGoodsIcon.setImageResource(0);
            }
            mNum.setText(bean.getNums());
            mType.setImageResource(MainIconUtil.getLiveTypeIcon(bean.getType()));
        }
    }

}

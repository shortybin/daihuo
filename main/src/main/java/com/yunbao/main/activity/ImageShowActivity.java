package com.yunbao.main.activity;

import android.view.View;
import android.widget.ImageView;

import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.activity.WebViewActivity;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.main.R;
import com.yunbao.main.bean.HomeTopTagBean;

public class ImageShowActivity extends AbsActivity {

    public String mString = "data";
    public HomeTopTagBean mHomeTopTagBean;

    public ImageView mImageView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_image_show;
    }

    @Override
    protected void main() {
        mHomeTopTagBean = (HomeTopTagBean) getIntent().getSerializableExtra(mString);

        mImageView = findViewById(R.id.image);

        ImgLoader.display(this, mHomeTopTagBean.getCustom_tag_image(), mImageView);

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewActivity.forward(mContext, mHomeTopTagBean.getCustom_tag_external_links(), false);
            }
        });
    }
}
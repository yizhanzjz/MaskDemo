package com.example.yizhan.maskdemo.mask;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yizhan.maskdemo.R;


public class MaskDialog extends DialogFragment implements View.OnClickListener {

    private MaskRectView maskRectView;

    private int currentType = 0;
    private View[] mViews;
    private BitmapInfo[] mBitmapInfos;
    //默认蒙版默认透明背景的颜色
    private int mMaskColor = 0xb2000000;
    private MaskClickListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MaskDialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View inflateView = inflater.inflate(R.layout.activity_mask_index, container);

        maskRectView = (MaskRectView) inflateView.findViewById(R.id.mask_react_view);
        maskRectView.setFillColor(mMaskColor);
        maskRectView.setOnClickListener(this);

        MaskBean[] maskBeans = getViewData();
        if (maskRectView != null && maskBeans != null && maskBeans.length != 0) {
            maskRectView.setMaskBean(maskBeans);
        }

        return inflateView;
    }

    /**
     * 获取view数组中每个view的数据
     */
    private MaskBean[] getViewData() {
        if (mViews == null) {
            return null;
        }

        int length = mViews.length;
        if (length == 0) {
            return null;
        }

        int infoLength = 0;
        if (mBitmapInfos != null) {
            infoLength = mBitmapInfos.length;
        }

        MaskBean[] maskBeans = new MaskBean[length];
        int[] locations = new int[2];
        for (int i = 0; i < length; i++) {
            MaskBean maskBean = new MaskBean();
            View view = mViews[i];
            view.getLocationOnScreen(locations);
            maskBean.mLeft = locations[0];
            maskBean.mTop = locations[1];
            maskBean.mRight = maskBean.mLeft + view.getWidth();
            maskBean.mBottom = maskBean.mTop + view.getHeight();

            if (i < infoLength) {
                BitmapInfo bitmapInfo = mBitmapInfos[i];
                if (bitmapInfo != null) {
                    Bitmap bitmap = bitmapInfo.bitmap;
                    if (bitmap != null) {
                        maskBean.mBitmap = bitmap;
                        maskBean.bitmapType = bitmapInfo.type;
                        maskBean.mBitmapDx = bitmapInfo.dx;
                        maskBean.mBitmapDy = bitmapInfo.dy;
                    }
                }
            }

            maskBeans[i] = maskBean;
        }

        return maskBeans;
    }

    @Override
    public void onClick(View v) {
        // 点击事件的处理
        if (this.listener != null) {
            this.listener.onClick(currentType++);
        }
    }


    /**
     * 设置蒙版要说明的view，是一个数组，View及其周边的图片一一对应
     */
    public void setViews(View[] args, BitmapInfo[] bitmapInfos) {

        this.mViews = args;
        this.mBitmapInfos = bitmapInfos;
        MaskBean[] maskBeans = getViewData();
        if (maskRectView != null && maskBeans != null && maskBeans.length != 0) {
            maskRectView.setMaskBean(maskBeans);
        }
    }

    /**
     * 设置蒙版透明背景色
     */
    public void setMaskBgColor(int color) {
        this.mMaskColor = color;
    }

    /**
     * 设置蒙版的点击监听
     */
    public void setOnClickMaskListener(MaskClickListener listener) {
        this.listener = listener;
    }

    public interface MaskClickListener {
        void onClick(int type);
    }

    @Override
    public void dismiss() {
        super.dismiss();

        //状态栏恢复
//        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

        //置空数据
        currentType = 0;
        mViews = null;
        mMaskColor = 0xb2000000;
        listener = null;
    }

    public void show(FragmentActivity activity) {

//        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);

        show(activity.getSupportFragmentManager(), "mask");
    }

    public static class BitmapInfo {
        //要显示的图片
        public Bitmap bitmap;

        //相对于矩形，图片的方向：上、下、左、右
        public int type;

        //矩形与图片的相应边距
        public float dx;
        public float dy;
    }
}

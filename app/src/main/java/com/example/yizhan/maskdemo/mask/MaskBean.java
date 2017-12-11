package com.example.yizhan.maskdemo.mask;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * 蒙版相关的数据bean
 * Created by yizhan on 2017/12/4.
 */

public class MaskBean implements Serializable {

    public static final int RECT_LEFT = 0;
    public static final int RECT_TOP = 1;
    public static final int RECT_RIGHT = 2;
    public static final int RECT_BOTTOM = 3;

    public float mLeft = 0;
    public float mTop = 0;
    public float mRight = 0;
    public float mBottom = 0;

    public Bitmap mBitmap = null;
    //矩形与该方向上的图片的间距
    //方向为RECT_LEFT：图片右上角与矩形左上角的间距
    //方向为RECT_TOP：图片左下角与矩形左上角的间距
    //方向为RECT_RIGHT：图片左上角与矩形右上角的间距
    //方向为RECT_BOTTOM：图片左上角与矩形左下角的间距
    //间距均为正值
    public float mBitmapDx = 0;
    public float mBitmapDy = 0;

    //图片在矩形的那个方向上
    public int bitmapType = RECT_LEFT;

}

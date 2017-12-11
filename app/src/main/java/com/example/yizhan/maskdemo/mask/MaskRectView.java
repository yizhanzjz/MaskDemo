package com.example.yizhan.maskdemo.mask;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 显示一个矩形，矩形外为填充色
 * Created by yizhan on 2017/12/1.
 */

public class MaskRectView extends View {

    private Paint mPaint;

    //控件的宽高
    private int mWidth;
    private int mHeight;

    private int fillColor = Color.BLACK;

    private MaskBean[] mMaskBeans;

    private BlurMaskFilter mBlurMaskFilter = new BlurMaskFilter(10, BlurMaskFilter.Blur.OUTER);

    public MaskRectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);//画笔模式为填充
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setColor(fillColor);//默认颜色
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mMaskBeans == null || mMaskBeans.length == 0) {
            return;
        }

        mPaint.setColor(fillColor);//默认颜色

        Path path = new Path();
        path.setFillType(Path.FillType.INVERSE_EVEN_ODD);
        //设置阴影
//        mPaint.setShadowLayer(10, 10, 10, 0xFF000000);

        for (MaskBean bean : mMaskBeans) {

            RectF rectF = new RectF();
            rectF.left = bean.mLeft;
            rectF.top = bean.mTop;
            rectF.right = bean.mRight;
            rectF.bottom = bean.mBottom;

            path.addRoundRect(rectF, 8, 8, Path.Direction.CW);
        }

        canvas.drawPath(path, mPaint);

        mPaint.setColor(0xff000000);//默认颜色

        for (MaskBean bean : mMaskBeans) {
            //画bitmap
            if (bean.mBitmap != null) {

                //图片的宽高
                int width = bean.mBitmap.getWidth();
                int height = bean.mBitmap.getHeight();

                //将要画的bitmap的左上角坐标：（bitmapLeft，bitmapTop）
                float bitmapLeft = 0;
                float bitmapTop = 0;

                switch (bean.bitmapType) {
                    case MaskBean.RECT_LEFT://左边，图片右上角与矩形左上角的间距
                        bitmapLeft = bean.mLeft - bean.mBitmapDx - width;
                        bitmapTop = bean.mTop + bean.mBitmapDy;
                        break;
                    case MaskBean.RECT_TOP://上边，图片左下角与矩形左上角的间距
                        bitmapLeft = bean.mLeft + bean.mBitmapDx;
                        bitmapTop = bean.mTop - bean.mBitmapDy - height;
                        break;
                    case MaskBean.RECT_RIGHT://右边，图片左上角与矩形右上角的间距
                        bitmapLeft = bean.mRight + bean.mBitmapDx;
                        bitmapTop = bean.mTop + bean.mBitmapDy;
                        break;
                    case MaskBean.RECT_BOTTOM://下边，图片左上角与矩形左下角的间距
                        bitmapLeft = bean.mLeft + bean.mBitmapDx;
                        bitmapTop = bean.mBottom + bean.mBitmapDy;
                        break;
                }

                Rect src = new Rect(0, 0, width, height);
                int destLeft = (int) bitmapLeft;
                int destTop = (int) bitmapTop;
                int destRight = (int) (bitmapLeft + width);
                int destBottom = (int) (bitmapTop + height);

                //超出边界的处理
                if (destLeft < 0) {//图片的左边界超出了屏幕左侧
                    if (destRight < 10) {//在图片的左边界超出屏幕左侧的前提下，图片的右边界在屏幕左侧10像素内，那就不显示了
                        break;
                    } else {
                        destLeft = 10;//这里的"10"和上面的"10"，是我手动设置的图片距离屏幕左侧的间距
                        destBottom = destTop + (destRight - destLeft) * height / width;//为了等比例显示，调整高度
                    }
                }

                if (destTop < 0) {
                    if (destBottom < 10) {
                        break;
                    } else {
                        destTop = 10;
                        destRight = destLeft + (destBottom - destTop) * width / height;
                    }
                }

                if (destRight > mWidth) {
                    if (destLeft > mWidth - 10) {
                        break;
                    } else {
                        destRight = mWidth - 10;
                        destBottom = destTop + (destRight - destLeft) * height / width;
                    }
                }

                if (destBottom > mHeight) {
                    if (destTop > mHeight - 10) {
                        break;
                    } else {
                        destBottom = mHeight - 10;
                        destRight = destLeft + (destBottom - destTop) * width / height;
                    }
                }

                //Bitmap目标矩形
                Rect dest = new Rect(destLeft, destTop, destRight, destBottom);

                canvas.drawBitmap(bean.mBitmap, src, dest, mPaint);
            }
        }

        Path path2 = new Path();
        mPaint.setColor(fillColor);//默认颜色
        mPaint.setMaskFilter(mBlurMaskFilter);

        for (MaskBean bean : mMaskBeans) {

            RectF rectF = new RectF();
            rectF.left = bean.mLeft;
            rectF.top = bean.mTop;
            rectF.right = bean.mRight;
            rectF.bottom = bean.mBottom;

            path2.addRoundRect(rectF, 8, 8, Path.Direction.CW);
        }

        canvas.drawPath(path2, mPaint);
        mPaint.setMaskFilter(null);
    }

    public void setFillColor(int color) {
        this.fillColor = color;
        invalidate();
    }

    public void setMaskBean(MaskBean[] maskBeans) {
        this.mMaskBeans = maskBeans;
        invalidate();
    }
}

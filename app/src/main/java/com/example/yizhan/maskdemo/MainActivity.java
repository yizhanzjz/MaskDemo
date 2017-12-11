package com.example.yizhan.maskdemo;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.yizhan.maskdemo.mask.MaskBean;
import com.example.yizhan.maskdemo.mask.MaskDialog;

public class MainActivity extends AppCompatActivity {

    private MaskDialog maskDialog;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private View[] mCurrentViews;
    private MaskDialog.BitmapInfo[] mCurrentBitmapInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //设置Toolbar
        //AppCompatActivity中的findViewById使用了泛型，所以这里不再需要强转
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tv1 = (TextView) findViewById(R.id.tv_1);
        tv2 = (TextView) findViewById(R.id.tv_2);
        tv3 = (TextView) findViewById(R.id.tv_3);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_refresh:

                //显示蒙版
                maskDialog = new MaskDialog();

                mCurrentViews = new View[]{tv1, tv2};
                mCurrentBitmapInfos = new MaskDialog.BitmapInfo[2];
                mCurrentBitmapInfos[0] = getBitmapInfo(R.drawable.mask_up, MaskBean.RECT_TOP, 10, 10);
                mCurrentBitmapInfos[1] = getBitmapInfo(R.drawable.mask_left, MaskBean.RECT_LEFT, 10, tv2.getHeight() / 2);

                maskDialog.setViews(mCurrentViews, mCurrentBitmapInfos);
                maskDialog.setMaskBgColor(0xb2000000);
                maskDialog.setOnClickMaskListener(new MaskDialog.MaskClickListener() {
                    @Override
                    public void onClick(int type) {
                        //type为第几次点击
                        switch (type) {
                            case 0:
                                mCurrentViews = new View[]{tv3};
                                mCurrentBitmapInfos = new MaskDialog.BitmapInfo[1];
                                mCurrentBitmapInfos[0] = getBitmapInfo(R.drawable.mask_down, MaskBean.RECT_BOTTOM, 10, 10);
                                maskDialog.setViews(mCurrentViews, mCurrentBitmapInfos);
                                break;
                            case 1:
                                maskDialog.dismiss();
                                break;
                        }
                    }
                });
                maskDialog.show(this);

                break;
        }

        return true;
    }

    private MaskDialog.BitmapInfo getBitmapInfo(int resIds, int type, float dx, float dy) {
        MaskDialog.BitmapInfo bitmapInfo = new MaskDialog.BitmapInfo();
        bitmapInfo.bitmap = BitmapFactory.decodeResource(getResources(), resIds);
        bitmapInfo.type = type;
        bitmapInfo.dx = dx;
        bitmapInfo.dy = dy;

        return bitmapInfo;
    }
}

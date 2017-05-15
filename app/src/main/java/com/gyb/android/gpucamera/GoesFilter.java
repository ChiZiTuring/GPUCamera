/*
 *
 * AiyaFilter.java
 * 
 * Created by Wuwang on 2016/11/19
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.gyb.android.gpucamera;

import android.content.res.Resources;

import com.gyb.android.gpucamera.filter.OesFilter;


/**
 * Description:
 */
public class GoesFilter extends OesFilter {

    public GoesFilter(Resources mRes) {
        super(mRes);
    }

    @Override
    public void setFlag(int flag) {
        super.setFlag(flag);
        float[] coord;
        if(getFlag()==1){    //前置摄像头
            coord=new float[]{
                0.0f, 1.0f,
                1.0f, 1.0f,
                0.0f, 0.0f,
                1.0f, 0.0f,
            };
        }else{                     //后置摄像头
            coord=new float[]{
                1.0f, 1.0f,
                0.0f, 1.0f,
                1.0f, 0.0f,
                0.0f, 0.0f,
            };
        }
        mTexBuffer.clear();
        mTexBuffer.put(coord);
        mTexBuffer.position(0);
    }
}

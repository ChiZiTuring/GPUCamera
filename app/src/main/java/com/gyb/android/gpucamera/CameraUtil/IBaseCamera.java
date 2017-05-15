package com.gyb.android.gpucamera.CameraUtil;

/**
 * Created by turing on 17/5/10.
 */

public interface IBaseCamera {

    // 相机设置配置
    class Config{
        float rate=1.778f; //宽高比
        int minPreviewWidth;
        int minPictureWidth;
    }

    // 帧回调接口
    interface PreviewFrameCallback{
        void onPreviewFrame(byte[] bytes, int width, int height);
    }
}

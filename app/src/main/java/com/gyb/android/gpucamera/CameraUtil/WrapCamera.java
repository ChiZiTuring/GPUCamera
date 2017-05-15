package com.gyb.android.gpucamera.CameraUtil;

import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by turing on 17/5/10.
 * 封装相机
 */

public class WrapCamera implements IBaseCamera{
    private static final String TAG = "WrapCamera";
    private static int MIN_PREVIEW_WIDTH = 720;
    private static int MIN_PICVIEW_WIDTH = 720;
    private static float WID_HEIGHT_RATE = 1.778f; // 16:9


    private Camera mCamera = null;

    private IBaseCamera.Config mConfig;
    private Camera.Size picSize;
    private Camera.Size preSize;
    private Point mPicSize;
    private Point mPreSize;
    private ByteBuffer mCamByteBuffer;

    public WrapCamera(){
        this.mConfig = new IBaseCamera.Config();
        mConfig.minPictureWidth = MIN_PICVIEW_WIDTH;
        mConfig.minPreviewWidth = MIN_PREVIEW_WIDTH;
        mConfig.rate = WID_HEIGHT_RATE;
    }

    public void open(int cameraId) {
        try{
            mCamera = Camera.open(cameraId);
        }
        catch(RuntimeException e){
            Log.e(TAG,"Camera open()方法有问题");
        }

        if (mCamera != null) {
            Camera.Parameters param = mCamera.getParameters();
            picSize = getPropPictureSize(param.getSupportedPictureSizes(), mConfig.rate,
                    mConfig.minPictureWidth);
            preSize = getPropPreviewSize(param.getSupportedPreviewSizes(), mConfig.rate, mConfig
                    .minPreviewWidth);
            param.setPictureSize(picSize.width, picSize.height);
            param.setPreviewSize(preSize.width, preSize.height);
            if (param.getMaxNumFocusAreas() > 0) {
                Rect areaRect1 = new Rect(-50, -50, 50, 50);
                List<Camera.Area> focusAreas = new ArrayList<>();
                focusAreas.add(new Camera.Area(areaRect1, 1000));
                param.setFocusAreas(focusAreas);
            }
            // if the camera support setting of metering area.
            if (param.getMaxNumMeteringAreas() > 0) {
                List<Camera.Area> meteringAreas = new ArrayList<>();
                Rect areaRect1 = new Rect(-100, -100, 100, 100);
                meteringAreas.add(new Camera.Area(areaRect1, 1000));
                param.setMeteringAreas(meteringAreas);
            }
            mCamera.setParameters(param);
            Camera.Size pre = param.getPreviewSize();
            Camera.Size pic = param.getPictureSize();
            mPicSize = new Point(pic.height, pic.width);
            mPreSize = new Point(pre.height, pre.width);
        }
    }

    private Camera.Size getPropPreviewSize(List<Camera.Size> list, float th, int minWidth){
        Collections.sort(list, sizeComparator);

        int i = 0;
        for(Camera.Size s:list){
            if((s.height >= minWidth) && equalRate(s, th)){
                break;
            }
            i++;
        }
        if(i == list.size()){
            i = 0;
        }
        return list.get(i);
    }

    private Camera.Size getPropPictureSize(List<Camera.Size> list, float th, int minWidth){
        Collections.sort(list, sizeComparator);

        int i = 0;
        for(Camera.Size s:list){
            if((s.height >= minWidth) && equalRate(s, th)){
                break;
            }
            i++;
        }
        if(i == list.size()){
            i = 0;
        }
        return list.get(i);
    }

    private static boolean equalRate(Camera.Size s, float rate){
        float r = (float)(s.width)/(float)(s.height);
        if(Math.abs(r - rate) <= 0.03)
        {
            return true;
        }
        else{
            return false;
        }
    }

    private Comparator<Camera.Size> sizeComparator=new Comparator<Camera.Size>(){
        public int compare(Camera.Size lhs, Camera.Size rhs) {
            // TODO Auto-generated method stub
            if(lhs.height == rhs.height){
                return 0;
            }
            else if(lhs.height > rhs.height){
                return 1;
            }
            else{
                return -1;
            }
        }
    };

    public boolean close() {
        if(mCamera!=null){
            try{
                mCamera.stopPreview();
            }catch (Exception e){
                e.printStackTrace();
            }
            try{
                mCamera.setPreviewCallbackWithBuffer(null);
                mCamera.setPreviewTexture(null);
                mCamera.release();
                mCamera = null;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }

    public Point getPreviewSize() {
        return mPreSize;
    }

    public void addBuffer(byte[] buffer){
        if(mCamera!=null){
            mCamera.addCallbackBuffer(buffer);
        }
    }

    public void setOnPreviewFrameCallbackWithBuffer(Camera.PreviewCallback callback) {
        if(mCamera!=null){
            Log.e(TAG,"Camera set CallbackWithBuffer");
            mCamera.setPreviewCallbackWithBuffer(callback);
            mCamByteBuffer = ByteBuffer.allocateDirect(getPreviewSize().x * getPreviewSize().y * 3 / 2);
            addBuffer(mCamByteBuffer.array());
        }
    }

    public interface PreviewFrameCallback{
        void onPreviewFrame(byte[] bytes, int width, int height);
    }

    public void setPreviewTexture(SurfaceTexture texture){
        if(mCamera!=null){
            try {
                mCamera.setPreviewTexture(texture);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void preview() {
        if(mCamera!=null){
            mCamera.startPreview();
        }
    }

    public Camera getCamera(){
        return mCamera;
    }
}

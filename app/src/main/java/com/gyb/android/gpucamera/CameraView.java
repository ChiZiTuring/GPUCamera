package com.gyb.android.gpucamera;

import android.content.Context;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.gyb.android.gpucamera.CameraUtil.WrapCamera;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by turing on 17/5/8.
 * 继承GlSurfaceView
 */

public class CameraView extends GLSurfaceView implements GLSurfaceView.Renderer,
        Camera.PreviewCallback{
    private static final String TAG = "CameraView";

    private int mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
    private int dataWidth=0,dataHeight=0;

    private WrapCamera mCamera;

    private CameraDrawer mDrawer;

    private SurfaceTexture mSurfaceTexture;

    long mStartTime = 0;

    public CameraView(Context context) {
        this(context,null);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        setEGLContextClientVersion(2);
        setRenderer(this);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
        setPreserveEGLContextOnPause(true);
        setCameraDistance(100);
        mDrawer=new CameraDrawer(getResources());
        mCamera=new WrapCamera();
    }

    /*
    实现Render的接口
     */
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config){
        mDrawer.onSurfaceCreated(gl,config);
        openCamera();
        mStartTime = System.currentTimeMillis();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height){
        mDrawer.onSurfaceChanged(gl,width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl){
        long curTime = System.currentTimeMillis();
        if(curTime - mStartTime > 10000) {
            Log.d(TAG, "onDrawFrame ...");
            mStartTime = curTime;
        }
        mDrawer.onDrawFrame(gl);
    }


    /*
    实现Holder的Pause和Resume
     */

    @Override
    public void onResume() {
        Log.d(TAG,"onResume ...");
        super.onResume();
        openCamera();
    }

    @Override
    public void onPause() {
        Log.d(TAG,"onPause ...");
        super.onPause();
        mCamera.close();
    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
//        Log.d(TAG,"onPreviewFrame ...");
        mDrawer.update(bytes);
        requestRender();
    }

    public void openCamera(){
        mCamera.close();
        mCamera.open(mCameraId);
        final Point previewSize=mCamera.getPreviewSize();
        dataWidth=previewSize.x;
        dataHeight=previewSize.y;
        mCamera.setOnPreviewFrameCallbackWithBuffer(this);
        mDrawer.setCamera(mCamera.getCamera());
        mCamera.setPreviewTexture(mDrawer.getTexture());
        mDrawer.setPreviewSize(dataWidth,dataHeight);
        mDrawer.setCameraId(mCameraId);
        mCamera.preview();
    }

    public void switchCamera(){
        if(mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT){
            mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        }else{
            mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        }
        openCamera();
    }

    public void setTestBigger(boolean btestBigger){
        mDrawer.setBiggerView(btestBigger);
    }
}

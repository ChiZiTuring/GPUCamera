package com.gyb.android.gpucamera.filter;

import android.content.res.Resources;
import android.opengl.GLES20;

/**
 * Created by turing on 17/5/12.
 */

public class TwoInputFilter extends AFilter {
    /**
     * 纹理坐标句柄
     */
    protected int mHCoord2;
    /**
     * 默认纹理贴图句柄
     */
    protected int mHTexture2;

    public TwoInputFilter(Resources res) {
        super(res);
    }

    @Override
    protected void onCreate() {
        createProgramByAssetsFile("shader/other/twoinput_vertex.sh",
                "shader/other/twoinput_fragment.sh");
    }

    @Override
    protected void createProgramAfter(){
        mHCoord2= GLES20.glGetAttribLocation(mProgram,"vCoord2");
        mHTexture2= GLES20.glGetUniformLocation(mProgram,"vTexture2");
    }

    @Override
    protected void onBindTextureAfter(){

    }

    @Override
    protected void onDrawArraysPre() {
        GLES20.glEnableVertexAttribArray(mHCoord2);
        GLES20.glVertexAttribPointer(mHCoord2, 2, GLES20.GL_FLOAT, false, 0, mTexBuffer);
    }

    @Override
    protected void onSizeChanged(int width, int height) {

    }
}
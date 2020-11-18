package com.example.cameratest;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "CameraTest_Preview";
    private SurfaceHolder mHolder;
    private Camera mCamera;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        Log.d(TAG, "surfaceCreated");
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
        Log.d(TAG, "surfaceDestroyed");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.
        Log.d(TAG, "surfaceChanged");
        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here
        Camera.Parameters mCameraParameters = mCamera.getParameters();
        Camera.Size prevSize = mCameraParameters.getPreviewSize();
        List<Camera.Size> supportPreSize = mCameraParameters.getSupportedPreviewSizes();
        Camera.Size picSize = mCameraParameters.getPictureSize();
        List<Camera.Size> supportPicSizes = mCameraParameters.getSupportedPictureSizes();

        Log.d(TAG, "prevSize " + prevSize.width + "x" + prevSize.height + ", picSize " + picSize.width + "x" + picSize.height);
        Log.d(TAG, "supported preview sizes:");
        for(Camera.Size size : supportPreSize)
            Log.d(TAG, size.width + "x" + size.height);
        Log.d(TAG, "supported picture sizes:");
        for(Camera.Size size : supportPicSizes)
            Log.d(TAG, size.width + "x" + size.height);

        mCameraParameters.setPreviewSize(1280, 720);
        mCameraParameters.setPictureSize(1280, 720);
        mCamera.setParameters(mCameraParameters);
        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }
}

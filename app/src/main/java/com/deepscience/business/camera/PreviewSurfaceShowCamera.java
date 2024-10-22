package com.deepscience.business.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Arrays;

public class PreviewSurfaceShowCamera extends CameraBase{
    private static final String TAG = "PreviewSurfaceShowCamera";
    private SurfaceView mPreviewSurfaceView;
    private SurfaceHolder surfaceHolder;

    private final SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            openCamera();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            // 如果需要对Surface大小变化进行处理，可以在这里处理
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // 释放相机资源
            if (cameraDevice != null) {
                cameraDevice.close();
                cameraDevice = null;
            }
        }
    };

    public PreviewSurfaceShowCamera(SurfaceView mPreviewSurfaceView, Context context) {
        super(context);
        this.mPreviewSurfaceView = mPreviewSurfaceView;

        surfaceHolder = mPreviewSurfaceView.getHolder();
        surfaceHolder.addCallback(surfaceCallback);
    }

    // 启动相机预览
    protected void startCameraPreview() {
        try {
            Surface surface = surfaceHolder.getSurface(); // 使用 SurfaceView 的 Surface

            // 创建 CaptureRequest.Builder 并设置预览模板
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);

            // 创建捕获会话
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @SuppressLint("LongLogTag")
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    cameraCaptureSession = session;
                    Log.d(TAG, "onConfigured: ");
                    updatePreview();
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                    Log.d(TAG, "onConfigureFailed: ");
                }
            }, backgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

}
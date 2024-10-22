package com.deepscience.business.camera;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import androidx.core.content.ContextCompat;

public abstract class CameraBase {
    private static final String TAG = "CameraBase";

    protected CameraDevice cameraDevice;
    private String cameraId;
    protected HandlerThread backgroundThread;
    protected Handler backgroundHandler;
    protected CaptureRequest.Builder captureRequestBuilder;
    protected CameraCaptureSession cameraCaptureSession;

    private Context mContext;
    private boolean hasOpened = false;

    public CameraBase(Context context) {
        this.mContext = context;
    }


    protected void openCamera() {
        if (hasOpened) {
            return;
        }
        CameraManager cameraManager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
        try {
            // 获取可用摄像头的 ID 列表
            for (String id : cameraManager.getCameraIdList()) {
                CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(id);

                // 选择后置摄像头
                if (characteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK) {
                    cameraId = id;
                    break;
                }
            }
            if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "openCamera: " + "no ");
            } else {
                // 权限已授予，可以执行相机操作
                cameraManager.openCamera(cameraId, stateCallback, backgroundHandler);
                hasOpened = true;
                Log.d(TAG, "openCamera: ");
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    // CameraDevice 的状态回调
    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            cameraDevice = camera;
            startCameraPreview();
            Log.d(TAG, "onOpened: ");
        }

        @Override
        public void onDisconnected(CameraDevice camera) {
            cameraDevice.close();
            Log.d(TAG, "onDisconnected: ");
            cameraDevice = null;
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            Log.d(TAG, "onError: ");
            cameraDevice.close();
            cameraDevice = null;
        }
    };

    abstract protected  void startCameraPreview();



    protected void updatePreview() {
        if (cameraDevice == null) return;

        // 设置自动控制模式
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

        try {
            // 开始重复的捕获请求
            cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(), null, backgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void startBackgroundThread() {
        backgroundThread = new HandlerThread("Camera Background");
        backgroundThread.start();
        backgroundHandler = new Handler(backgroundThread.getLooper());
    }

    public void stopBackgroundThread() {
        backgroundThread.quitSafely();
        try {
            backgroundThread.join();
            backgroundThread = null;
            backgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

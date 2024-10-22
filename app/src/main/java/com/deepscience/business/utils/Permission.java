package com.deepscience.business.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Permission {
    private static final String TAG = "Permission";
    private static final int CAMERA_REQUEST_CODE = 100;
    public static void checkPermissions(Activity activity) {

        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "openCamera: " + "no permission, try to request");
            ActivityCompat.requestPermissions(activity,
                    new String[]{android.Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }
    }

    public static void requestPermissionsResult(int requestCode,
                                                String[] permissions,
                                                int[] grantResults,
                                                Activity activity) {

        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限被授予
                Log.d(TAG, "onRequestPermissionsResult: " + "camera has permission");
            } else {
                // 权限被拒绝
                Toast.makeText(activity, "相机权限被拒绝", Toast.LENGTH_SHORT).show();
            }
        }
    }



}

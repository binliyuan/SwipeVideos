package com.deepscience.business;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.VideoView;

import com.deepscience.business.camera.CameraBase;
import com.deepscience.business.camera.PreviewSurfaceShowCamera;
import com.deepscience.business.utils.Permission;
import com.deepscience.business.viewmodule.video.VideoItem;
import com.deepscience.business.viewmodule.video.VideosAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
//    ApplicationComponent appComponent;
    CameraBase previewSurfaceShowCamera;
    VideoView video;
    String videoPath = "https://www.w3schools.com/html/mov_bbb.mp4";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Permission.checkPermissions(this);
//        appComponent = DaggerApplicationComponent.builder()
//                .videoModule(new VideoModule())
//                .build();

        final SurfaceView surfaceView = findViewById(R.id.surfaceView);
        video = findViewById(R.id.videoView);
        video.setVideoURI(Uri.parse(videoPath));
        video.setOnPreparedListener(mp -> {
            mp.start();

            float videoRatio = mp.getVideoWidth() / (float)mp.getVideoHeight();
            float screenRatio = video.getWidth() / (float)video.getHeight();
            float scale  = videoRatio / screenRatio;
            if (scale >= 1f){
                video.setScaleX(scale);
            }else {
                video.setScaleY(1f / scale);
            }
            video.start(); // 视频准备好后自动开始播放
        });

        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
            }
        });

        previewSurfaceShowCamera = new PreviewSurfaceShowCamera(surfaceView, this);
        previewSurfaceShowCamera.startBackgroundThread();
        final ViewPager2 videosViewPager = findViewById(R.id.viewPagerVideos);
        List<VideoItem> videoItems = new ArrayList<>();
        VideoItem item = new VideoItem();
        item.videoURL = "https://media.w3.org/2010/05/sintel/trailer.mp4";
        item.videoTitle = "";
        item.videoDesc = "";
        videoItems.add(item);
        videosViewPager.setAdapter(new VideosAdapter(videoItems));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Permission.requestPermissionsResult(requestCode, permissions, grantResults, this);
    }

}
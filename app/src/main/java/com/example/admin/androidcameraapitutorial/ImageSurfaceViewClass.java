package com.example.admin.androidcameraapitutorial;

/**
 * Created by admin on 10/23/2017.
 */
import android.hardware.Camera;
import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public class ImageSurfaceViewClass extends SurfaceView implements SurfaceHolder.Callback  {
    private Camera camera;
    private SurfaceHolder surfaceHolder;

    public ImageSurfaceViewClass(Context context, Camera camera) {
        super(context);
        this.camera = camera;
        this.surfaceHolder = getHolder();
        this.surfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            this.camera.setPreviewDisplay(holder);
            this.camera.setDisplayOrientation(90);
            this.camera.startPreview();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        this.camera.stopPreview();
        this.camera.release();
    }
}

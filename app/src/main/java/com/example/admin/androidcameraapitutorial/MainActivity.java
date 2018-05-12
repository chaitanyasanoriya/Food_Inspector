package com.example.admin.androidcameraapitutorial;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;


public class MainActivity extends ActionBarActivity
{
    Camera.ShutterCallback _pfnShutterCallback = new Camera.ShutterCallback()
    {

        @Override
        public void onShutter()
        {
            // TODO Auto-generated method stub

        }

    };
    PictureCallback _pfnRawPictureCallback = new PictureCallback()
    {

        @Override
        public void onPictureTaken(byte[] data, Camera camera)
        {
            // TODO Auto-generated method stub

        }
    };
    private RandomStringGenerator rsg = new RandomStringGenerator();
    private String str;
    private ImageSurfaceViewClass mImageSurfaceView;
    private Camera camera;
    private FrameLayout cameraPreviewLayout;
    private ImageView capturedImageHolder;
    private Button process_button;
    private Button process_cancel;
    private Bitmap bitmap;
    private Button bar_cancel;
    private Button bar_save;
    private ImageView share_img;
    private Matrix matrix = new Matrix();
    private RelativeLayout rl;
    private TextView tv;
    private DisplayMetrics displayMetrics = new DisplayMetrics();
    private String devicekey;
    private RelativeLayout loadingpanel;
    private ProgressBar progress;
    PictureCallback pictureCallback = new PictureCallback()
    {
        @Override
        public void onPictureTaken(byte[] data, Camera camera)
        {
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            if (bitmap == null)
            {
                Toast.makeText(MainActivity.this, "Captured image is empty", Toast.LENGTH_LONG).show();
                return;
            }
            matrix.postRotate(90);
            bitmap = scaleDownBitmapImage(bitmap, 800, 450);
            //
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            //capturedImageHolder.setImageBitmap(scaleDownBitmapImage(bitmap, 300, 200));
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            int height = displayMetrics.heightPixels;
            width = width / 3;
            height = height / 3;
            width = width * 2;
            height = height * 2;
            capturedImageHolder.setMinimumHeight(height);
            capturedImageHolder.setMinimumWidth(width);
            capturedImageHolder.setScaleType(ImageView.ScaleType.FIT_CENTER);
            capturedImageHolder.setImageBitmap(bitmap);
            //rl.setMinimumHeight(height-300);
            rl.setMinimumHeight(height);
            camera.startPreview();
        }
    };
    private ImageView image_bar;
    private Button captureButton;
    private Camera.AutoFocusCallback _pfnAutoFocusCallback = new Camera.AutoFocusCallback()
    {

        @Override
        public void onAutoFocus(boolean success, Camera camera)
        {
            // TODO Auto-generated method stub
            camera.autoFocus(null);
            camera.takePicture(_pfnShutterCallback, _pfnRawPictureCallback,
                    pictureCallback);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        rl = (RelativeLayout) findViewById(R.id.innerrelative);
        tv = (TextView) findViewById(R.id.content);
        Bundle extras = getIntent().getExtras();
        devicekey = extras.getString("devicekey");
        cameraPreviewLayout = (FrameLayout) findViewById(R.id.camera_preview);
        capturedImageHolder = (ImageView) findViewById(R.id.captured_image);
        loadingpanel = (RelativeLayout) findViewById(R.id.loadingPanel);
        progress = (ProgressBar) findViewById(R.id.progress);
        bar_cancel = (Button) findViewById(R.id.bar_cancel);
        bar_save = (Button) findViewById(R.id.save_bar);
        share_img = (ImageView) findViewById(R.id.share);
        bar_cancel.setVisibility(View.INVISIBLE);
        bar_save.setVisibility(View.INVISIBLE);
        share_img.setVisibility(View.INVISIBLE);
        image_bar = (ImageView) findViewById(R.id.image_bar);
        process_button = (Button) findViewById(R.id.process);
        process_cancel = (Button) findViewById(R.id.process_cancel);
        process_cancel.setVisibility(View.INVISIBLE);
        process_button.setVisibility(View.INVISIBLE);
        image_bar.setVisibility(View.INVISIBLE);
        camera = checkDeviceCamera();
        loadingpanel.setVisibility(View.INVISIBLE);
        Camera.Parameters params = camera.getParameters();
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        camera.setParameters(params);
        mImageSurfaceView = new ImageSurfaceViewClass(MainActivity.this, camera);
        cameraPreviewLayout.addView(mImageSurfaceView);
        captureButton = (Button) findViewById(R.id.button);
        captureButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                camera.autoFocus(_pfnAutoFocusCallback);
                /*cameraPreviewLayout.setBackgroundColor(Color.parseColor("#000000"));
                cameraPreviewLayout.setAlpha((float) 0.5);*/
                captureButton.setVisibility(View.INVISIBLE);
                process_button.setVisibility(View.VISIBLE);
                process_cancel.setVisibility(View.VISIBLE);
            }
        });
    }

    private Camera checkDeviceCamera()
    {
        Camera mCamera = null;
        try
        {
            mCamera = Camera.open();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return mCamera;
    }

    private Bitmap scaleDownBitmapImage(Bitmap bitmap, int newWidth, int newHeight)
    {
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
        return resizedBitmap;
    }

    protected void button_process_cancel(View v)
    {
        //cameraPreviewLayout.setAlpha(1);
        captureButton.setVisibility(View.VISIBLE);
        capturedImageHolder.setVisibility(View.INVISIBLE);
        process_button.setVisibility(View.INVISIBLE);
        process_cancel.setVisibility(View.INVISIBLE);
    }

    protected void button_process(View view)
    {
        String print;
        SendImage si = new SendImage();
        Thread t1 = new Thread(si);
        Toast.makeText(this, "Processing", Toast.LENGTH_LONG).show();
        //capturedImageHolder.setVisibility(View.INVISIBLE);
        //loadingpanel.setVisibility(View.VISIBLE);
        process_cancel.setVisibility(View.INVISIBLE);
        process_button.setVisibility(View.INVISIBLE);
        //bar_save.setVisibility(View.VISIBLE);
        //bar_cancel.setVisibility(View.VISIBLE);
        //share_img.setVisibility(View.VISIBLE);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        final String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        si.setVal(encodedImage,devicekey);
        t1.start();
        while(t1.isAlive())
        {
        }
        print = si.returnmsg();
        //progress.setVisibility(View.GONE);
        tv.setText(print);
        rl.setVisibility(View.VISIBLE);
        tv.setVisibility(View.VISIBLE);
        tv.setAlpha(1);
        Toast.makeText(this, print, Toast.LENGTH_SHORT).show();
    }

    protected void button_bar_cancel(View v)
    {
        //cameraPreviewLayout.setAlpha(1);
        captureButton.setVisibility(View.VISIBLE);
        capturedImageHolder.setVisibility(View.INVISIBLE);
        image_bar.setVisibility(View.INVISIBLE);
        bar_save.setVisibility(View.INVISIBLE);
        bar_cancel.setVisibility(View.INVISIBLE);
        share_img.setVisibility(View.INVISIBLE);
        rl.setVisibility(View.INVISIBLE);
        tv.setVisibility(View.INVISIBLE);
    }
    protected void button_bar_save(View v)
    {

    }
}
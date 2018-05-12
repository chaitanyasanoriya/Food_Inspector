package com.example.admin.androidcameraapitutorial;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity implements GestureDetector.OnGestureListener
{

    private final Activity activity = this;
    private ImageSurfaceViewClass mImageSurfaceView;
    private FrameLayout cameraPreviewLayout;
    private Camera camera;
    private GestureDetectorCompat mDetector;
    private SharedPreferences perfs = null;
    private RandomStringGenerator rsg = new RandomStringGenerator();
    private SQLiteDatabase db;
    private String devicekey;
    @Override
    protected void onResume()
    {
        super.onResume();
        db = openOrCreateDatabase("FoodInspector", Context.MODE_PRIVATE, null);
        if (perfs.getBoolean("firstrun", true))
        {
            Toast.makeText(this, "First RUN", Toast.LENGTH_LONG).show();
            final String devicekey = rsg.getSaltString();
            try

            {
                db.execSQL("create table if not exists users(devicekey varchar(255),device_key_value varchar(255))");
                db.execSQL("insert into users(devicekey, device_key_value) values('key','" + devicekey + "')");
                this.devicekey = devicekey;
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            perfs.edit().putBoolean("firstrun", false).commit();
        }
        else
        {
            this.devicekey = get_key();
            //Toast.makeText(this, devicekey, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        ActivityCompat.requestPermissions(HomeScreen.this, new String[]{Manifest.permission.CAMERA}, 1);
        camera = checkDeviceCamera();
        mDetector = new GestureDetectorCompat(this, this);
        perfs = getSharedPreferences("com.example.admin.androidcameraapitutorial", MODE_PRIVATE);
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

    public void scan_obj(View view)
    {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("devicekey",devicekey);
        startActivity(i);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        this.mDetector.onTouchEvent(event);
        // Be sure to call the superclass implementation
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY)
    {
        Intent a = new Intent(this, MainActivity.class);
        a.putExtra("devicekey",devicekey);
        startActivity(a);
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event)
    {

    }

    @Override
    public void onLongPress(MotionEvent event)
    {

    }

    @Override
    public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX,
                            float distanceY)
    {

        return true;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event)
    {

        return true;
    }

    @Override
    public boolean onDown(MotionEvent event)
    {

        return true;
    }

    protected void scan_bar(View view)
    {
        IntentIntegrator ii = new IntentIntegrator(activity);
        ii.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        ii.setPrompt("Scan");
        ii.setCameraId(0);
        ii.setBeepEnabled(true);
        ii.setBarcodeImageEnabled(false);
        ii.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null)
        {
            if (result.getContents() == null)
            {
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            } else
            {
                Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    protected String get_key()
    {
        String str = "select * from users where devicekey='key'";
        Cursor c = db.rawQuery(str, null);
        c.moveToFirst();
        str = c.getString(1);
        return str;
    }

    protected void history(View view)
    {
        History history = new History();
        ArrayList<ArrayList<String>> details = new ArrayList<ArrayList<String>>();
        GetData getData = new GetData();
        getData.setVal(devicekey);
        Thread t1 = new Thread(getData);
        t1.start();
        try
        {
            Thread.sleep(5000);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        String longmsg = getData.returnmsg();
        int i, j = 1;
        int lastIndex = 0;
        int lastIndex_1 = 0;
        int count = 0;
        ArrayList<String> row = new ArrayList<String>();
        ArrayList<String> row_1 = new ArrayList<String>();
        String findStr = "\\n";
        try //Try block for Exception Handling
        {
            while (lastIndex != -1) //While loop to Separate each Message from str and adding them to details ArrayList
            {
                if ((j - 1) % 4 == 0 && j != 1) //Checking if the the whole message has been read
                {
                    row_1 = (ArrayList<String>) row.clone(); //Cloning the row ArrayList to row_1 ArrayList
                    details.add(row_1); //Adding row_1 ArrayList into details ArrayList
                    row.clear(); //Clearing row ArrayList
                }
                lastIndex = longmsg.indexOf(findStr, lastIndex);  //Getting the lastIndex before the findstr String
                row.add(longmsg.substring(lastIndex_1, lastIndex)); //Adding the messages in row ArrayList
                if (lastIndex != -1) //if condition that is true until the str is not empty
                {
                    count++; //incrementing count
                    lastIndex += findStr.length(); //increasing lastIndex by length of findStr
                    lastIndex_1 = lastIndex; //Inserting the value of lastIndex in lastIndex_1
                }
                j++; //increasing j
            }
        } catch (Exception e) //catch block to catch Exceptions
        {
            //catch block body
        }
        Intent intent = new Intent(HomeScreen.this,History.class);
        startActivity(intent);
    }
}

package com.example.admin.androidcameraapitutorial;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

public class SendImage implements Runnable
{
    private String encodedImage;
    private String returnmsg;
    private String devicekey;

    protected void setVal(String encodedImage,String devicekey)
    {
        this.encodedImage = encodedImage;
        this.devicekey = devicekey;
    }

    StringBuffer str = new StringBuffer("");

    @Override
    public void run()
    {
        try
        {
            URL url = new URL("http://192.168.43.195:8181/Servlet/Servlet");
            URLConnection connection = url.openConnection();
            Log.d("inputString", encodedImage);
            connection.setDoOutput(true);
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            out.write(devicekey+encodedImage);
            out.close();
            Thread.sleep(3500);
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String returnString = "";
            while ((returnString = in.readLine()) != null)
            {
                str.append(returnString);
            }
            in.close();
            returnmsg = String.valueOf(str);
        } catch (Exception e)
        {
            Log.e("Exception", e.toString());
        }
    }

    protected String returnmsg()
    {
        return returnmsg;
    }
}

package com.example.admin.androidcameraapitutorial;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class History extends AppCompatActivity
{
    private RelativeLayout relativeLayout;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history2);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        for(int i=0;i<5;i++)
        {
            textView = new TextView(this);
            textView.setText(i);
            textView.setId(i);
            relativeLayout.addView(textView);
        }
    }
}

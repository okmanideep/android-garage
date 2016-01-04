package com.manidesto.androidgarage.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.manidesto.androidgarage.R;

import org.reflections.Reflections;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Reflections reflections = new Reflections(getPackageName());
        Set<Class<? extends Activity>> activities = reflections.getSubTypesOf(Activity.class);

    }
}

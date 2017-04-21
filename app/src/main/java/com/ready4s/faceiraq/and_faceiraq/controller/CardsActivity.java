package com.ready4s.faceiraq.and_faceiraq.controller;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ready4s.faceiraq.and_faceiraq.R;
import com.ready4s.faceiraq.and_faceiraq.view.CardsNavigationBarFragment;
import com.ready4s.faceiraq.and_faceiraq.view.NavigationBarFragment;
import com.ready4s.faceiraq.and_faceiraq.view.WebViewFragment;

/**
 * Created by Paweł Sałata on 21.04.2017.
 * email: psalata9@gmail.com
 */

public class CardsActivity extends AppCompatActivity{
    
    public static final String TAG = "CardsActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);
        Log.d(TAG, "onCreate:");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



}

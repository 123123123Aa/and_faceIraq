package com.ready4s.faceiraq.and_faceiraq.controller;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ready4s.faceiraq.and_faceiraq.R;
import com.ready4s.faceiraq.and_faceiraq.model.database.opened_pages.OpenedPageModel;
import com.ready4s.faceiraq.and_faceiraq.model.database.opened_pages.OpenedPagesDAO;
import com.ready4s.faceiraq.and_faceiraq.view.CardsFragment;
import com.ready4s.faceiraq.and_faceiraq.view.CardsNavigationBarFragment;
import com.ready4s.faceiraq.and_faceiraq.view.NavigationBarFragment;
import com.ready4s.faceiraq.and_faceiraq.view.WebViewFragment;

import org.parceler.Parcels;

/**
 * Created by Paweł Sałata on 21.04.2017.
 * email: psalata9@gmail.com
 */

public class CardsActivity extends AppCompatActivity
        implements CardsNavigationBarFragment.OnCardsNavigationBarActions {
    
    public static final String TAG = "CardsActivity";
    private OpenedPagesDAO openedPagesDAO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);
        Log.d(TAG, "onCreate:");
        openedPagesDAO = new OpenedPagesDAO();
        initCardsFragment();
    }


    private void initCardsFragment() {
        CardsFragment cardsFragment = new CardsFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.cardsDisplayFragment, cardsFragment, CardsFragment.TAG);
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onNewCardPressed() {
        long cardID = openedPagesDAO.insert(new OpenedPageModel());
        CardsFragment cardsFragment = (CardsFragment) getSupportFragmentManager()
                .findFragmentByTag(CardsFragment.TAG);
        cardsFragment.addBlankPage(cardID);
    }

    @Override
    public void onHomeButtonPressed() {
        //goto home page
        finish();
    }

    @Override
    public void onCardsButtonPressed() {
        finish();
    }

    public void onCardSelected() {
        setResult(RESULT_OK);
        finish();
    }

    public void onCardDeleted(long id) {
        openedPagesDAO.delete(id);
    }
}

package com.ready4s.faceiraq.and_faceiraq.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ready4s.faceiraq.and_faceiraq.R;
import com.ready4s.faceiraq.and_faceiraq.dialog.MainDialogFragment;
import com.ready4s.faceiraq.and_faceiraq.model.SharedPreferencesHelper;
import com.ready4s.faceiraq.and_faceiraq.model.database.opened_pages.OpenedPageModel;
import com.ready4s.faceiraq.and_faceiraq.model.database.opened_pages.OpenedPagesDAO;
import com.ready4s.faceiraq.and_faceiraq.model.utils.ThemeChangeUtil;
import com.ready4s.faceiraq.and_faceiraq.view.CardsFragment;
import com.ready4s.faceiraq.and_faceiraq.view.CardsNavigationBarFragment;

import static com.ready4s.faceiraq.and_faceiraq.controller.BookmarksActivity.BOOKMARKS_REQUEST_CODE;
import static com.ready4s.faceiraq.and_faceiraq.controller.HistoryActivity.HISTORY_REQUEST_CODE;
import static com.ready4s.faceiraq.and_faceiraq.theme.colour.ThemeColourActivity.THEME_COLOUR_REQUEST_CODE;

/**
 * Created by Paweł Sałata on 21.04.2017.
 * email: psalata9@gmail.com
 */

public class CardsActivity extends AppCompatActivity
        implements CardsNavigationBarFragment.OnCardsNavigationBarActions,
                    MainDialogFragment.OnMainDialogActionsListener {
    
    public static final String TAG = "CardsActivity";
    public static final int CARDS_REQUEST_CODE = 10;

    private OpenedPagesDAO openedPagesDAO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChangeUtil.onActivityCreateSetTheme(this);
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
    public void onOpenedNewPage() {
        onNewPageButtonPressed();
    }

    @Override
    public void onNewPageButtonPressed() {
        OpenedPageModel pageModel = new OpenedPageModel();
        pageModel.setUrl(getResources().getString(R.string.HOME_PAGE_ADDRESS));
        long cardID = openedPagesDAO.insert(new OpenedPageModel());
        CardsFragment cardsFragment = (CardsFragment) getSupportFragmentManager()
                .findFragmentByTag(CardsFragment.TAG);
        cardsFragment.openNewPage(cardID);
        updateNavigationBarCardsCount();
    }

    @Override
    public void onHomeButtonPressed() {
        SharedPreferencesHelper.setCardUrl(this, getString(R.string.HOME_PAGE_ADDRESS));
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch(requestCode) {
                case THEME_COLOUR_REQUEST_CODE:
                    ThemeChangeUtil.changeToTheme(this, data.getStringExtra("Colour"));
                    break;
                case HISTORY_REQUEST_CODE:
                case BOOKMARKS_REQUEST_CODE:
                    setResult(RESULT_OK);
                    finish();
                    break;
            }
        }
    }

    @Override
    public void onCardsButtonPressed() {
        finish();
    }

    @Override
    public int getCardsAmount() {
        return openedPagesDAO.getOpenedPages().size();
    }

    public void onCardSelected() {
        setResult(RESULT_OK);
        finish();
    }

    private void updateNavigationBarCardsCount() {
        CardsNavigationBarFragment fragment = (CardsNavigationBarFragment) getSupportFragmentManager()
                .findFragmentById(R.id.cardsNavigationBarFragment);
        fragment.updateCardCount(getCardsAmount());
    }

    public void onCardDeleted(long id) {
        openedPagesDAO.delete(id);
        updateNavigationBarCardsCount();
    }
}

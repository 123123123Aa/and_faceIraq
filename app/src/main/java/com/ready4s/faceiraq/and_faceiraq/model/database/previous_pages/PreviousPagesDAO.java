package com.ready4s.faceiraq.and_faceiraq.model.database.previous_pages;

import android.util.Log;

import com.ready4s.faceiraq.and_faceiraq.model.database.opened_pages.OpenedPageModel;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Paweł Sałata on 26.04.2017.
 * email: psalata9@gmail.com
 */

public class PreviousPagesDAO {

    public static final String TAG = "PreviousPagesDAO";
    private Realm realm;

    public PreviousPagesDAO() {
        realm = Realm.getDefaultInstance();
    }

    public void insert(String url) {
        realm.beginTransaction();
        PreviousPageModel previousPage = new PreviousPageModel();
        previousPage.setUrl(url);
        realm.copyToRealm(previousPage);
        realm.commitTransaction();
    }

    public String getLast() {
        if (getSize() < 1) {
            return "";
        }
        realm.beginTransaction();
        RealmResults<PreviousPageModel> previousPages = realm.where(PreviousPageModel.class).findAll();
        String previousPageUrl = previousPages.last().getUrl();
        previousPages.deleteLastFromRealm();
        realm.commitTransaction();
        return previousPageUrl;
    }

    public void deleteAll() {
        final RealmResults<PreviousPageModel> result = realm.where(PreviousPageModel.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Log.d(TAG, "execute: delete previous pages from realm");
                result.deleteAllFromRealm();
            }
        });
    }

    public int getSize() {
        realm.beginTransaction();
        RealmResults<PreviousPageModel> results = realm.where(PreviousPageModel.class).findAll();
        int size = results.size();
        realm.commitTransaction();
        return size;
    }
}

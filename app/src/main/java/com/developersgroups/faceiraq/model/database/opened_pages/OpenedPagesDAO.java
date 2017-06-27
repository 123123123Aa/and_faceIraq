package com.developersgroups.faceiraq.model.database.opened_pages;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Paweł Sałata on 23.04.2017.
 * email: psalata9@gmail.com
 */

public class OpenedPagesDAO {


    public static final String TAG = "OpenedPagesDAO";
    private Realm realm;

    public OpenedPagesDAO() {
        realm = Realm.getDefaultInstance();
    }


    public long insert(OpenedPageModel openedPage) {
        Log.d(TAG, "insert: ");
        realm.beginTransaction();
        openedPage.setId(getPrimaryKeyValue());
        realm.copyToRealm(openedPage);
        realm.commitTransaction();
        return openedPage.getId();
    }

    public void update(OpenedPageModel openedPage) {
        Log.d(TAG, "update: ");
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(openedPage);
        realm.commitTransaction();
    }

    public void delete(final long id) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<OpenedPageModel> result = realm
                        .where(OpenedPageModel.class)
                        .equalTo(OpenedPageModel.ID, id)
                        .findAll();
                result.deleteAllFromRealm();
            }
        });
    }

    public void deleteAll() {
        final RealmResults<OpenedPageModel> result = realm.where(OpenedPageModel.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Log.d(TAG, "execute: delete opened pages from realm");
                result.deleteAllFromRealm();
            }
        });

    }

    public String getUrlWithPageId(long id) {
        OpenedPageModel pageModel = realm
                .where(OpenedPageModel.class)
                .equalTo(OpenedPageModel.ID, id)
                .findFirst();
        return pageModel.getUrl();
    }

    public List<OpenedPageModel> getOpenedPages() {
        List<OpenedPageModel> resultsList = new ArrayList<>();
        resultsList.addAll(realm.where(OpenedPageModel.class).findAll());
        return resultsList;
    }

    public int getSize() {
        return getOpenedPages().size();
    }

    private long getPrimaryKeyValue() {
        Number lastRecordPKValue = realm.where(OpenedPageModel.class).max(OpenedPageModel.ID);
        long id = (lastRecordPKValue != null) ? lastRecordPKValue.longValue() + 1 : 0;
        Log.d(TAG, "getPrimaryKeyValue id: " + id);
        return id;
    }
}

package com.ready4s.faceiraq.and_faceiraq.model.database.history;

import android.util.Log;

import com.ready4s.faceiraq.and_faceiraq.model.database.BrowserDAO;
import com.ready4s.faceiraq.and_faceiraq.model.database.PageDetails;
import com.ready4s.faceiraq.and_faceiraq.model.utils.TimeUtil;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Paweł Sałata on 18.04.2017.
 * email: psalata9@gmail.com
 */

public class HistoryDAOImplementation implements BrowserDAO {

    public static final String TAG = "HistoryDAO";
    private Realm realm;

    public HistoryDAOImplementation() {
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void insertOrUpdate(PageDetails pageDetails) {
        realm.beginTransaction();
        final HistoryRecord record = new HistoryRecord();
        record.setTimestamp(TimeUtil.getCurrentTimestamp());
        record.setTitle(pageDetails.getTitle());
        record.setAddress(pageDetails.getAddress());
        record.setBase64Logo(pageDetails.getBase64Logo());
        Log.d(TAG, record.toString());
        realm.copyToRealmOrUpdate(record);
        realm.commitTransaction();
    }

    @Override
    public void delete(long id) {

    }

    public RealmResults<HistoryRecord> getHistory() {
        return realm.where(HistoryRecord.class).findAll();
    }
}

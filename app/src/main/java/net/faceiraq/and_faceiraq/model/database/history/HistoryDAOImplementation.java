package net.faceiraq.and_faceiraq.model.database.history;

import android.util.Log;

import net.faceiraq.and_faceiraq.model.PageDetails;
import net.faceiraq.and_faceiraq.model.database.BrowserDAO;

import java.util.ArrayList;
import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Paweł Sałata on 18.04.2017.
 * email: psalata9@gmail.com
 */

public class HistoryDAOImplementation implements BrowserDAO {

    public static final String TAG = "HistoryDAO";
    private Realm realm;
    private long idToUpdate;

    public HistoryDAOImplementation() {
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void insert(PageDetails pageDetails) {
        if (!shouldSaveToHistory(pageDetails.getAddress(), pageDetails.getTimestamp())) {
            return;
        }
        realm.beginTransaction();
        final HistoryRecord record = new HistoryRecord();
        record.setTimestamp(pageDetails.getTimestamp());
        record.setTitle(pageDetails.getTitle());
        record.setAddress(pageDetails.getAddress());
        record.setId(getPrimaryKeyValue(pageDetails));
        Log.d(TAG, "Adding to history: " + record.toString());
        setIdToUpdate(record.getId());
        realm.copyToRealmOrUpdate(record);
        realm.commitTransaction();
    }

    public void update(PageDetails pageDetails) {
        realm.beginTransaction();
        final HistoryRecord record = new HistoryRecord();
        record.setTimestamp(pageDetails.getTimestamp());
        record.setTitle(pageDetails.getTitle());
        record.setAddress(pageDetails.getAddress());
        record.setBase64Logo(pageDetails.getBase64Logo());
        record.setId(idToUpdate);
        realm.copyToRealmOrUpdate(record);
        realm.commitTransaction();
    }

    public String getLast() {
        if (!realm.isEmpty()) {
            realm.beginTransaction();
            RealmResults<HistoryRecord> results = realm.where(HistoryRecord.class).findAll();
            String url = results.last().getAddress();
            realm.commitTransaction();
            return url;
        } else
            return "http://www.faceiraq.net/";
    }

    public long getLastTimestamp() {
        if (!realm.isEmpty()) {
            realm.beginTransaction();
            RealmResults<HistoryRecord> results = realm.where(HistoryRecord.class).findAll();
            long timestamp = results.last().getTimestamp();
            realm.commitTransaction();
            return timestamp;
        } else
            return System.currentTimeMillis();
    }

    @Override
    public void delete(final long id) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<HistoryRecord> result = realm
                        .where(HistoryRecord.class)
                        .equalTo(HistoryRecord.ID, id)
                        .findAll();
                result.deleteAllFromRealm();
            }
        });
    }

    private long getPrimaryKeyValue(PageDetails record) {
        Number lastRecordPKValue = realm.where(HistoryRecord.class).max(HistoryRecord.ID);
        long id = (lastRecordPKValue != null) ? lastRecordPKValue.longValue() + 1 : 0;
        Log.d(TAG, "getPrimaryKeyValue id: " + id);
        return id;
    }

    private void setIdToUpdate(long idToUpdate) {
        this.idToUpdate = idToUpdate;
    }

    public List<HistoryRecord> getHistory() {
        List<HistoryRecord> recordList = new ArrayList<>();
        RealmResults<HistoryRecord> results = realm.where(HistoryRecord.class)
                .findAllSorted(HistoryRecord.TIMESTAMP, Sort.DESCENDING);
        recordList.addAll(realm.copyFromRealm(results));
        return recordList;
    }

    public List<HistoryRecord> getHistory(String text) {
        List<HistoryRecord> recordList = new ArrayList<>();
        RealmResults<HistoryRecord> results = realm.where(HistoryRecord.class)
                .contains(HistoryRecord.ADDRESS, text, Case.INSENSITIVE)
                .findAllSorted(HistoryRecord.TIMESTAMP, Sort.DESCENDING);
        recordList.addAll(realm.copyFromRealm(results));
        return recordList;
    }

    private boolean shouldSaveToHistory(String url, long timestamp) {
        if (!url.equals(getLast())) {
            return true;
        }
        if (timestamp / 1000 != getLastTimestamp() / 1000) {
            Log.d(TAG, "shouldSaveToHistory: equal timestamps");
            return true;
        }
        return false;
    }
}

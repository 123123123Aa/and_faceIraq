package net.faceiraq.and_faceiraq.model.database.bookmarks;

import android.util.Log;

import net.faceiraq.and_faceiraq.model.PageDetails;
import net.faceiraq.and_faceiraq.model.database.BrowserDAO;
import net.faceiraq.and_faceiraq.model.utils.ImageUtil;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Paweł Sałata on 20.04.2017.
 * email: psalata9@gmail.com
 */

public class BookmarksDAOImplementation implements BrowserDAO {

    public static final String TAG = "BookmarksDAO";
    private Realm realm;

    public BookmarksDAOImplementation() {
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void insert(PageDetails pageDetails) {
        realm.beginTransaction();
        final BookmarkRecord record = new BookmarkRecord();
        record.setTimestamp(pageDetails.getTimestamp());
        record.setTitle(pageDetails.getTitle());
        record.setAddress(pageDetails.getAddress());
        record.setId(getPrimaryKeyValue(pageDetails));
        record.setBase64Logo(pageDetails.getBase64Logo());
        Log.d(TAG, "Bookmarking: " + record.toString());
        realm.copyToRealmOrUpdate(record);
        realm.commitTransaction();
    }

    @Override
    public void delete(final long id) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<BookmarkRecord> result = realm
                        .where(BookmarkRecord.class)
                        .equalTo(BookmarkRecord.ID, id)
                        .findAll();
                result.deleteAllFromRealm();
            }
        });
    }

    private long getPrimaryKeyValue(PageDetails record) {
        Number lastRecordPKValue = realm.where(BookmarkRecord.class).max(BookmarkRecord.ID);
        long id = (lastRecordPKValue != null) ? lastRecordPKValue.longValue() + 1 : 0;
        Log.d(TAG, "getPrimaryKeyValue id: " + id);
        return id;
    }

    public List<BookmarkRecord> getBookmarks() {
        List<BookmarkRecord> recordList = new ArrayList<>();
        RealmResults<BookmarkRecord> results = realm.where(BookmarkRecord.class)
                .findAllSorted(BookmarkRecord.TIMESTAMP, Sort.DESCENDING);
        recordList.addAll(realm.copyFromRealm(results));
        return recordList;
    }

}

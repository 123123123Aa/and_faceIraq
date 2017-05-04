package net.faceiraq.and_faceiraq.model.database.previous_pages;

import android.content.Context;
import android.util.Log;

import net.faceiraq.and_faceiraq.model.SharedPreferencesHelper;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Paweł Sałata on 26.04.2017.
 * email: psalata9@gmail.com
 */

public class PreviousPagesDAO {

    public static final String TAG = "PreviousPagesDAO";
    private Realm realm;
    private Context mContext;

    public PreviousPagesDAO(Context context) {
        realm = Realm.getDefaultInstance();
        this.mContext = context;
    }

    public void insert(String url) {
        long id = SharedPreferencesHelper.getCardNumber(mContext);
        realm.beginTransaction();
        PreviousPageModel previousPage = new PreviousPageModel();
        previousPage.setUrl(url);
        previousPage.setCardNumber(id);
        realm.copyToRealm(previousPage);
        realm.commitTransaction();
    }

    public String removeLastAndGetNext() {
        if (isEmpty()) {
            return "";
        }
        long id = SharedPreferencesHelper.getCardNumber(mContext);
        realm.beginTransaction();
        RealmResults<PreviousPageModel> previousPages = realm.where(PreviousPageModel.class).equalTo("id", id).findAll();
        previousPages.last().deleteFromRealm();
        realm.commitTransaction();

        realm.beginTransaction();
        RealmResults<PreviousPageModel> previousPagesAfterDelete = realm.where(PreviousPageModel.class).equalTo("id", id).findAll();
        String previousPageUrl = previousPagesAfterDelete.last().getUrl();
        previousPagesAfterDelete.last().deleteFromRealm();
        realm.commitTransaction();
        return previousPageUrl;
    }


    public String getLast() {
        if (isEmpty()) {
            return "";
        }
        long id = SharedPreferencesHelper.getCardNumber(mContext);
        realm.beginTransaction();
        RealmResults<PreviousPageModel> previousPages = realm.where(PreviousPageModel.class).equalTo("id", id).findAll();
        String previousPageUrl = previousPages.last().getUrl();
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
        long id = SharedPreferencesHelper.getCardNumber(mContext);
        realm.beginTransaction();
        RealmResults<PreviousPageModel> results = realm.where(PreviousPageModel.class).equalTo("id", id).findAll();
        int size = results.size();
        realm.commitTransaction();
        return size;
    }

    public boolean isEmpty() {
        return getSize() < 2;
    }
}

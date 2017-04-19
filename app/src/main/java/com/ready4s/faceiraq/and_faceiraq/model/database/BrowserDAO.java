package com.ready4s.faceiraq.and_faceiraq.model.database;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Paweł Sałata on 18.04.2017.
 * email: psalata9@gmail.com
 */

public interface BrowserDAO {

    public void insertOrUpdate(PageDetails pageDetails);

    /**
     * At the moment we are using record timestamp as primary key
     * @param id - timestamp
     */
    public void delete(long id);

}
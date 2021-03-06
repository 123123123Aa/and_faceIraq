package com.developersgroups.faceiraq.model.database;

import com.developersgroups.faceiraq.model.PageDetails;

/**
 * Created by Paweł Sałata on 18.04.2017.
 * email: psalata9@gmail.com
 */

public interface BrowserDAO {

    public void insert(PageDetails pageDetails);

    /**
     * At the moment we are using record timestamp as primary key
     * @param id - timestamp
     */
    public void delete(long id);


}

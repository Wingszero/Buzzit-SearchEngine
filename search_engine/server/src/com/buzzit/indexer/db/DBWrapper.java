package com.buzzit.indexer.db;

import com.myapp.worker.db.DBConst;
import com.myapp.worker.db.WebPageAccessor;
import com.myapp.worker.db.WebPageEntity;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.EnvironmentFailureException;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.StoreConfig;

import java.io.File;

/**
 *
 */
public class DBWrapper {
    private Environment env;
    private File env_home;
    private EntityStore store;

    private WebPageAccessor webPageEA;

    public DBWrapper(String root) {
        setup(root);
    }

    public void setup(String root) {
        EnvironmentConfig envConfig = new EnvironmentConfig();
        envConfig.setAllowCreate(true);

        env_home = new File(root);
        try {
            env = new Environment(env_home, envConfig);
        } catch (EnvironmentFailureException e) {
            e.printStackTrace();
        }

        StoreConfig sc = new StoreConfig();
        sc.setReadOnly(true);
        store = new EntityStore(env, DBConst.DB_STORE_NAME, sc);
        webPageEA = new WebPageAccessor(store);
    }

    public void close() {
        store.close();
        env.close();
    }

    public PrimaryIndex<String, WebPageEntity> getWebPageIdx() {
        return webPageEA.getAllPages();
    }
}
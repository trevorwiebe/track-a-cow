package com.trevorwiebe.trackacow.dataLoaders;

import android.content.Context;
import android.os.AsyncTask;

import com.trevorwiebe.trackacow.db.AppDatabase;
import com.trevorwiebe.trackacow.db.entities.LoadEntity;

public class InsertLoadEntity extends AsyncTask<Context, Void, Void> {

    private LoadEntity loadEntity;

    public InsertLoadEntity(LoadEntity loadEntity) {
        this.loadEntity = loadEntity;
    }

    @Override
    protected Void doInBackground(Context... contexts) {
        AppDatabase.getAppDatabase(contexts[0]).loadDao().insertLoad(loadEntity);
        return null;
    }
}
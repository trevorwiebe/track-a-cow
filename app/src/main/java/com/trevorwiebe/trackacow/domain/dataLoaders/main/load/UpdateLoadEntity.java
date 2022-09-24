package com.trevorwiebe.trackacow.domain.dataLoaders.main.load;

import android.content.Context;
import android.os.AsyncTask;

import com.trevorwiebe.trackacow.data.local.AppDatabase;
import com.trevorwiebe.trackacow.data.local.entities.LoadEntity;

public class UpdateLoadEntity extends AsyncTask<Context, Void, Void> {

    private LoadEntity loadEntity;

    public UpdateLoadEntity(LoadEntity loadEntity) {
        this.loadEntity = loadEntity;
    }

    @Override
    protected Void doInBackground(Context... contexts) {
        AppDatabase.getAppDatabase(contexts[0]).loadDao().updateLoadByFields(loadEntity.getNumberOfHead(), loadEntity.getDate(), loadEntity.getDescription(), loadEntity.getLoadId());
        return null;
    }
}
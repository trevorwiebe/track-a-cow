package com.trevorwiebe.trackacow.dataLoaders;

import android.content.Context;
import android.os.AsyncTask;

import com.trevorwiebe.trackacow.db.AppDatabase;
import com.trevorwiebe.trackacow.db.entities.LoadEntity;

public class QueryLoadsByLoadId extends AsyncTask<Context, Void, LoadEntity> {

    private String loadId;
    private OnLoadsByLoadIdLoaded onLoadsByLoadIdLoaded;

    public QueryLoadsByLoadId(String loadId, OnLoadsByLoadIdLoaded onLoadsByLoadIdLoaded) {
        this.loadId = loadId;
        this.onLoadsByLoadIdLoaded = onLoadsByLoadIdLoaded;
    }

    public interface OnLoadsByLoadIdLoaded {
        void onLoadsByLoadIdLoaded(LoadEntity loadEntity);
    }

    @Override
    protected LoadEntity doInBackground(Context... contexts) {
        return AppDatabase.getAppDatabase(contexts[0]).loadDao().getLoadByLoadId(loadId);
    }

    @Override
    protected void onPostExecute(LoadEntity loadEntity) {
        super.onPostExecute(loadEntity);
        onLoadsByLoadIdLoaded.onLoadsByLoadIdLoaded(loadEntity);
    }
}

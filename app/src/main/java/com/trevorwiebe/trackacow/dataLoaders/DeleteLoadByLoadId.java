package com.trevorwiebe.trackacow.dataLoaders;

import android.content.Context;
import android.os.AsyncTask;

import com.trevorwiebe.trackacow.db.AppDatabase;

public class DeleteLoadByLoadId extends AsyncTask<Context, Void, Void> {

    private String loadId;

    public DeleteLoadByLoadId(String loadId) {
        this.loadId = loadId;
    }

    @Override
    protected Void doInBackground(Context... contexts) {
        AppDatabase.getAppDatabase(contexts[0]).loadDao().deleteLoadByLoadId(loadId);
        return null;
    }

}

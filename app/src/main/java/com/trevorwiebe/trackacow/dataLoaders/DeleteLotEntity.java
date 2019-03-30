package com.trevorwiebe.trackacow.dataLoaders;

import android.content.Context;
import android.os.AsyncTask;

import com.trevorwiebe.trackacow.db.AppDatabase;

public class DeleteLotEntity extends AsyncTask<Context, Void, Void> {

    private String lotId;

    public DeleteLotEntity(String lotId) {
        this.lotId = lotId;
    }

    @Override
    protected Void doInBackground(Context... contexts) {
        AppDatabase.getAppDatabase(contexts[0]).lotDao().deleteLotEntity(lotId);
        return null;
    }

}
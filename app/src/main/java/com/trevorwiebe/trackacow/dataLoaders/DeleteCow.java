package com.trevorwiebe.trackacow.dataLoaders;

import android.content.Context;
import android.os.AsyncTask;

import com.trevorwiebe.trackacow.db.AppDatabase;
import com.trevorwiebe.trackacow.db.entities.CowEntity;

public class DeleteCow extends AsyncTask<Context, Void, Void> {

    private CowEntity cowEntity;

    public DeleteCow(CowEntity cowEntity){
        this.cowEntity = cowEntity;
    }

    @Override
    protected Void doInBackground(Context... contexts) {
        AppDatabase.getAppDatabase(contexts[0]).cowDao().deleteCow(cowEntity);
        return null;
    }
}
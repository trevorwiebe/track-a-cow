package com.trevorwiebe.trackacow.domain.dataLoaders.main.cow;

import android.content.Context;
import android.os.AsyncTask;

import com.trevorwiebe.trackacow.data.local.AppDatabase;
import com.trevorwiebe.trackacow.data.entities.CowEntity;

@Deprecated(since="Use use-cases instead")
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

package com.trevorwiebe.trackacow.domain.dataLoaders.cache.holdingCow;

import android.content.Context;
import android.os.AsyncTask;

import com.trevorwiebe.trackacow.data.local.AppDatabase;
import com.trevorwiebe.trackacow.data.cacheEntities.CacheCowEntity;

@Deprecated(since="Use use-cases instead")
public class InsertHoldingCow extends AsyncTask<Context, Void, Void> {

    private CacheCowEntity cacheCowEntity;

    public InsertHoldingCow(CacheCowEntity cacheCowEntity){
        this.cacheCowEntity = cacheCowEntity;
    }

    @Override
    protected Void doInBackground(Context... contexts) {
        AppDatabase.getAppDatabase(contexts[0]).cacheCowDao().insertHoldingCow(cacheCowEntity);
        return null;
    }
}

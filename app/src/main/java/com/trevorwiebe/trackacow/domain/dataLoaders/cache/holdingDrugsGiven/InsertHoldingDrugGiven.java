package com.trevorwiebe.trackacow.domain.dataLoaders.cache.holdingDrugsGiven;

import android.content.Context;
import android.os.AsyncTask;

import com.trevorwiebe.trackacow.data.local.AppDatabase;
import com.trevorwiebe.trackacow.data.cacheEntities.CacheDrugsGivenEntity;

@Deprecated(since="Use use-cases instead")
public class InsertHoldingDrugGiven extends AsyncTask<Context, Void, Void> {

    private CacheDrugsGivenEntity cacheDrugsGivenEntity;

    public InsertHoldingDrugGiven(CacheDrugsGivenEntity cacheDrugsGivenEntity){
        this.cacheDrugsGivenEntity = cacheDrugsGivenEntity;
    }

    @Override
    protected Void doInBackground(Context... contexts) {
        AppDatabase.getAppDatabase(contexts[0]).cacheDrugsGivenDao().insertHoldingDrugsGiven(cacheDrugsGivenEntity);
        return null;
    }
}

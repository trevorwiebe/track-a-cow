package com.trevorwiebe.trackacow.domain.dataLoaders.cache.holdingDrug;

import android.content.Context;
import android.os.AsyncTask;

import com.trevorwiebe.trackacow.data.local.AppDatabase;
import com.trevorwiebe.trackacow.data.local.holdingUpdateEntities.HoldingDrugEntity;

public class InsertHoldingDrug extends AsyncTask<Context, Void, Void> {

    private HoldingDrugEntity holdingDrugEntity;

    public InsertHoldingDrug(HoldingDrugEntity holdingDrugEntity){
        this.holdingDrugEntity = holdingDrugEntity;
    }

    @Override
    protected Void doInBackground(Context... contexts) {
        AppDatabase.getAppDatabase(contexts[0]).holdingDrugDao().insertHoldingDrug(holdingDrugEntity);
        return null;
    }

}
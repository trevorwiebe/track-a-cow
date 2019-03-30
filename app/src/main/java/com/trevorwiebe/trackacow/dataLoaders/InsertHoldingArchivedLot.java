package com.trevorwiebe.trackacow.dataLoaders;

import android.content.Context;
import android.os.AsyncTask;

import com.trevorwiebe.trackacow.db.AppDatabase;
import com.trevorwiebe.trackacow.db.holdingUpdateEntities.HoldingArchivedLotEntity;

public class InsertHoldingArchivedLot extends AsyncTask<Context, Void, Void> {

    private HoldingArchivedLotEntity holdingArchivedLotEntity;

    public InsertHoldingArchivedLot(HoldingArchivedLotEntity holdingArchivedLotEntity) {
        this.holdingArchivedLotEntity = holdingArchivedLotEntity;
    }

    @Override
    protected Void doInBackground(Context... contexts) {
        AppDatabase.getAppDatabase(contexts[0]).holdingArchivedLotDao().insertHoldingArchivedLot(holdingArchivedLotEntity);
        return null;
    }
}
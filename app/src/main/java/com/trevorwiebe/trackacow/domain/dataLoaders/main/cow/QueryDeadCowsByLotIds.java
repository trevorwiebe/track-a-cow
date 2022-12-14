package com.trevorwiebe.trackacow.domain.dataLoaders.main.cow;

import android.content.Context;
import android.os.AsyncTask;

import com.trevorwiebe.trackacow.data.local.AppDatabase;
import com.trevorwiebe.trackacow.data.entities.CowEntity;

import java.util.ArrayList;
import java.util.List;

@Deprecated(since="Use use-cases instead")
public class QueryDeadCowsByLotIds extends AsyncTask<Context, Void, ArrayList<CowEntity>> {

    private OnDeadCowsLoaded mOnDeadCowsLoaded;
    private ArrayList<String> mLotIds;

    public QueryDeadCowsByLotIds(OnDeadCowsLoaded onDeadCowsLoaded, ArrayList<String> lotIds) {
        this.mOnDeadCowsLoaded = onDeadCowsLoaded;
        this.mLotIds = lotIds;
    }

    public interface OnDeadCowsLoaded{
        void onDeadCowsLoaded(ArrayList<CowEntity> cowEntities);
    }

    @Override
    protected ArrayList<CowEntity> doInBackground(Context... contexts) {
        List<CowEntity> cowEntities = AppDatabase.getAppDatabase(contexts[0]).cowDao().getDeadCowEntitiesByLotIds(mLotIds);
        return (ArrayList<CowEntity>) cowEntities;
    }

    @Override
    protected void onPostExecute(ArrayList<CowEntity> cowEntities) {
        super.onPostExecute(cowEntities);
        mOnDeadCowsLoaded.onDeadCowsLoaded(cowEntities);
    }
}

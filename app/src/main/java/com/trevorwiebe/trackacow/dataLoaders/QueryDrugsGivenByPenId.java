package com.trevorwiebe.trackacow.dataLoaders;

import android.content.Context;
import android.os.AsyncTask;

import com.trevorwiebe.trackacow.db.AppDatabase;
import com.trevorwiebe.trackacow.db.entities.DrugsGivenEntity;

import java.util.ArrayList;
import java.util.List;

public class QueryDrugsGivenByPenId extends AsyncTask<Context, Void, ArrayList<DrugsGivenEntity>> {

    private OnDrugsLoaded mOnDrugsLoaded;
    private String mPenId;

    public QueryDrugsGivenByPenId(OnDrugsLoaded onDrugsLoaded, String penId){
        this.mOnDrugsLoaded = onDrugsLoaded;
        this.mPenId = penId;
    }

    public interface OnDrugsLoaded{
        void onDrugsLoaded(ArrayList<DrugsGivenEntity> drugsGivenEntities);
    }

    @Override
    protected ArrayList<DrugsGivenEntity> doInBackground(Context... contexts) {
        List<DrugsGivenEntity> drugsGivenEntities = AppDatabase.getAppDatabase(contexts[0]).drugsGivenDao().getDrugsGivenByPenId(mPenId);
        return (ArrayList<DrugsGivenEntity>) drugsGivenEntities;
    }

    @Override
    protected void onPostExecute(ArrayList<DrugsGivenEntity> drugsGivenEntities) {
        super.onPostExecute(drugsGivenEntities);
        mOnDrugsLoaded.onDrugsLoaded(drugsGivenEntities);
    }
}
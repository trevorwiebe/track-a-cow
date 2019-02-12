package com.trevorwiebe.trackacow.dataLoaders;

import android.content.Context;
import android.os.AsyncTask;

import com.trevorwiebe.trackacow.db.AppDatabase;
import com.trevorwiebe.trackacow.db.entities.PenEntity;

public class QueryPenById extends AsyncTask<Context, Void, PenEntity> {

    private String mPenId;
    private OnPenByIdReturned mOnPenByIdReturned;

    public QueryPenById(String penId, OnPenByIdReturned onPenByIdReturned){
        this.mPenId = penId;
        this.mOnPenByIdReturned = onPenByIdReturned;
    }

    public interface OnPenByIdReturned{
        void onPenByIdReturned(PenEntity penEntity);
    }

    @Override
    protected PenEntity doInBackground(Context... contexts) {
        return AppDatabase.getAppDatabase(contexts[0]).penDao().getPenById(mPenId);
    }

    @Override
    protected void onPostExecute(PenEntity penEntity) {
        super.onPostExecute(penEntity);
        mOnPenByIdReturned.onPenByIdReturned(penEntity);
    }
}

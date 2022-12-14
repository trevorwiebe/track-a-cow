package com.trevorwiebe.trackacow.domain.dataLoaders.main.drugsGiven;

import android.content.Context;
import android.os.AsyncTask;

import com.trevorwiebe.trackacow.data.local.AppDatabase;
import com.trevorwiebe.trackacow.data.entities.DrugsGivenEntity;

@Deprecated(since="Use use-cases instead")
public class QueryDrugsGivenByDrugsGivenId extends AsyncTask<Context, Void, DrugsGivenEntity> {

    private String drugGivenId;
    private OnDrugsGivenByDrugsGivenIdLoaded onDrugsGivenByDrugsGivenIdLoaded;

    public QueryDrugsGivenByDrugsGivenId(String drugGivenId, OnDrugsGivenByDrugsGivenIdLoaded onDrugsGivenByDrugsGivenIdLoaded) {
        this.drugGivenId = drugGivenId;
        this.onDrugsGivenByDrugsGivenIdLoaded = onDrugsGivenByDrugsGivenIdLoaded;
    }

    public interface OnDrugsGivenByDrugsGivenIdLoaded {
        void onDrugsGivenByDrugsGivenIdLoaded(DrugsGivenEntity drugsGivenEntity);
    }

    @Override
    protected DrugsGivenEntity doInBackground(Context... contexts) {
        return AppDatabase.getAppDatabase(contexts[0]).drugsGivenDao().getDrugGivenByDrugGivenId(drugGivenId);
    }

    @Override
    protected void onPostExecute(DrugsGivenEntity drugsGivenEntity) {
        super.onPostExecute(drugsGivenEntity);
        onDrugsGivenByDrugsGivenIdLoaded.onDrugsGivenByDrugsGivenIdLoaded(drugsGivenEntity);
    }
}

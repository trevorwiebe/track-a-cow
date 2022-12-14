package com.trevorwiebe.trackacow.domain.dataLoaders.main.drugsGiven;

import android.content.Context;
import android.os.AsyncTask;

import com.trevorwiebe.trackacow.data.local.AppDatabase;
import com.trevorwiebe.trackacow.data.entities.DrugsGivenEntity;

import java.util.ArrayList;
import java.util.List;

@Deprecated(since="Use use-cases instead")
public class QueryDrugsGivenByLotIdAndDateRange extends AsyncTask<Context, Void, ArrayList<DrugsGivenEntity>> {

    private String lotId;
    private long startDate;
    private long endDate;
    private OnDrugsGivenByLotIdAndDateRangeLoaded onDrugsGivenByLotIdAndDateRangeLoaded;

    public QueryDrugsGivenByLotIdAndDateRange(OnDrugsGivenByLotIdAndDateRangeLoaded onDrugsGivenByLotIdAndDateRangeLoaded, String lotId, long startDate, long endDate) {
        this.onDrugsGivenByLotIdAndDateRangeLoaded = onDrugsGivenByLotIdAndDateRangeLoaded;
        this.lotId = lotId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public interface OnDrugsGivenByLotIdAndDateRangeLoaded {
        void onDrugsGivenByLotIdAndDateRangeLoaded(ArrayList<DrugsGivenEntity> drugsGivenEntities);
    }

    @Override
    protected ArrayList<DrugsGivenEntity> doInBackground(Context... contexts) {
        List<DrugsGivenEntity> drugsGivenEntities = AppDatabase.getAppDatabase(contexts[0]).drugsGivenDao().getDrugsGivenByLotIdAndDateRange(lotId, startDate, endDate);
        return (ArrayList<DrugsGivenEntity>) drugsGivenEntities;
    }

    @Override
    protected void onPostExecute(ArrayList<DrugsGivenEntity> drugsGivenEntities) {
        super.onPostExecute(drugsGivenEntities);
        onDrugsGivenByLotIdAndDateRangeLoaded.onDrugsGivenByLotIdAndDateRangeLoaded(drugsGivenEntities);
    }

}

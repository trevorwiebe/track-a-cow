package com.trevorwiebe.trackacow.domain.dataLoaders.main.drugsGiven;

import android.content.Context;
import android.os.AsyncTask;

import com.trevorwiebe.trackacow.data.local.AppDatabase;

@Deprecated(since="Use use-cases instead")
public class UpdateDrugGivenAmountGiven extends AsyncTask<Context, Void, Void> {

    private String drugGivenId;
    private int amountGiven;
    private OnDrugGivenInserted onDrugGivenInserted;

    public UpdateDrugGivenAmountGiven(String drugGivenId, int amountGiven, OnDrugGivenInserted onDrugGivenInserted) {
        this.drugGivenId = drugGivenId;
        this.amountGiven = amountGiven;
        this.onDrugGivenInserted = onDrugGivenInserted;
    }

    public interface OnDrugGivenInserted{
        void onDrugGivenInsert();
    }

    @Override
    protected Void doInBackground(Context... contexts) {
        AppDatabase.getAppDatabase(contexts[0]).drugsGivenDao().updateDrugGivenAmountGiven(amountGiven, drugGivenId);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        onDrugGivenInserted.onDrugGivenInsert();
    }
}

package com.trevorwiebe.trackacow.presentation.activities;

import android.content.Intent;

import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.trevorwiebe.trackacow.R;
import com.trevorwiebe.trackacow.domain.adapters.DrugsGivenRecyclerViewAdapter;
import com.trevorwiebe.trackacow.domain.dataLoaders.main.drugsGiven.DeleteDrugGivenById;
import com.trevorwiebe.trackacow.domain.dataLoaders.main.drugsGiven.UpdateDrugGivenAmountGiven;
import com.trevorwiebe.trackacow.domain.dataLoaders.main.drug.QueryAllDrugs;
import com.trevorwiebe.trackacow.domain.dataLoaders.main.drugsGiven.QueryDrugsGivenByCowId;
import com.trevorwiebe.trackacow.data.entities.DrugEntity;
import com.trevorwiebe.trackacow.data.entities.DrugsGivenEntity;
import com.trevorwiebe.trackacow.domain.utils.ItemClickListener;

import java.util.ArrayList;

public class EditDrugsGivenActivity extends AppCompatActivity implements
        QueryDrugsGivenByCowId.OnDrugsGivenByCowIdLoaded,
        QueryAllDrugs.OnAllDrugsLoaded,
        DeleteDrugGivenById.OnDrugDelete,
        UpdateDrugGivenAmountGiven.OnDrugGivenInserted {

    private RecyclerView mDrugsGivenRv;
    private DrugsGivenRecyclerViewAdapter drugsGivenRecyclerViewAdapter;
    private TextView mNoDrugsGiven;
    private FloatingActionButton mAddNewDrugGiven;

    private ArrayList<DrugsGivenEntity> mDrugsGivenEntities = new ArrayList<>();
    private ArrayList<DrugEntity> mDrugEntities = new ArrayList<>();
    private String cowId;
    private static final int ADD_NEW_DRUG_GIVEN = 737;
    private static final int EDIT_NEW_DRUG_GIVEN = 298;

    private static final String TAG = "EditDrugsGivenActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_drugs_given);

        new QueryAllDrugs(this).execute(this);

        cowId = getIntent().getStringExtra("cowId");

        mNoDrugsGiven = findViewById(R.id.edit_drugs_no_drugs_given);
        mAddNewDrugGiven = findViewById(R.id.edit_drugs_add_new_drug);
        mDrugsGivenRv = findViewById(R.id.drugs_given_rv);
        mDrugsGivenRv.setLayoutManager(new LinearLayoutManager(this));
        drugsGivenRecyclerViewAdapter = new DrugsGivenRecyclerViewAdapter(this, mDrugsGivenEntities, mDrugEntities);
        mDrugsGivenRv.setAdapter(drugsGivenRecyclerViewAdapter);

        mDrugsGivenRv.addOnItemTouchListener(new ItemClickListener(this, mDrugsGivenRv, new ItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                DrugsGivenEntity drugsGivenEntity = mDrugsGivenEntities.get(position);
                Intent editDrugIntent = new Intent(EditDrugsGivenActivity.this, EditDrugsGivenToSpecificCowActivity.class);
                editDrugIntent.putExtra("cowId", cowId);
                editDrugIntent.putExtra("drugGivenId", drugsGivenEntity.getDrugsGivenId());
                startActivityForResult(editDrugIntent, EDIT_NEW_DRUG_GIVEN);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        mAddNewDrugGiven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addNewDrugIntent = new Intent(EditDrugsGivenActivity.this, AddDrugsGivenToSpecificCowActivity.class);
                addNewDrugIntent.putExtra("cowId", cowId);
                startActivityForResult(addNewDrugIntent, ADD_NEW_DRUG_GIVEN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == ADD_NEW_DRUG_GIVEN || requestCode == EDIT_NEW_DRUG_GIVEN) && resultCode == RESULT_OK) {
            new QueryAllDrugs(this).execute(this);
        }
    }

    @Override
    public void onAllDrugsLoaded(ArrayList<DrugEntity> drugEntities) {
        mDrugEntities = drugEntities;
        new QueryDrugsGivenByCowId(this, 1, cowId).execute(this);
    }

    @Override
    public void onDrugsLoadedByCowId(ArrayList<DrugsGivenEntity> drugsGivenEntities, int id) {
        mDrugsGivenEntities = drugsGivenEntities;
        instantiateRv(mDrugsGivenEntities, mDrugEntities);
    }

    @Override
    public void onDrugDeleted() {
        new QueryDrugsGivenByCowId(this, 1, cowId).execute(this);
    }

    @Override
    public void onDrugGivenInsert() {
        new QueryDrugsGivenByCowId(this, 1, cowId).execute(this);
    }

    private void instantiateRv(ArrayList<DrugsGivenEntity> drugsGivenEntities, ArrayList<DrugEntity> drugEntities){
        if(drugsGivenEntities.size() == 0){
            mNoDrugsGiven.setVisibility(View.VISIBLE);
            mDrugsGivenRv.setVisibility(View.INVISIBLE);
        }else{
            mNoDrugsGiven.setVisibility(View.INVISIBLE);
            mDrugsGivenRv.setVisibility(View.VISIBLE);
            drugsGivenRecyclerViewAdapter.swapData(drugsGivenEntities, drugEntities);
        }
    }

}

package com.trevorwiebe.trackacow;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trevorwiebe.trackacow.adapters.ManageDrugRecyclerViewAdapter;
import com.trevorwiebe.trackacow.objects.DrugObject;
import com.trevorwiebe.trackacow.utils.ItemClickListener;

import java.util.ArrayList;

public class ManageDrugsActivity extends AppCompatActivity {

    private ManageDrugRecyclerViewAdapter mManageDrugRecyclerViewAdapter;
    private ArrayList<DrugObject> mDrugList = new ArrayList<>();
    private DatabaseReference mDrugRef = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(DrugObject.DRUG_OBJECT);
    private ValueEventListener mDrugListener;
    private static final int UPDATE_DRUG_CALLBACK_CODE = 747;

    private RecyclerView mManageDrugRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_drugs);

        final ProgressBar loadingDrugs = findViewById(R.id.load_drugs);
        final TextView drugListEmpty = findViewById(R.id.drug_list_empty);

        FloatingActionButton addNewDrugFab = findViewById(R.id.add_new_drug);
        addNewDrugFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addNewDrugIntent = new Intent(ManageDrugsActivity.this, AddNewDrugActivity.class);
                startActivity(addNewDrugIntent);
            }
        });

        mManageDrugRv = findViewById(R.id.manage_drug_rv);
        mManageDrugRv.setLayoutManager(new LinearLayoutManager(this));
        mManageDrugRecyclerViewAdapter = new ManageDrugRecyclerViewAdapter(mDrugList, this);
        mManageDrugRv.setAdapter(mManageDrugRecyclerViewAdapter);

        mManageDrugRv.addOnItemTouchListener(new ItemClickListener(this, mManageDrugRv, new ItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                DrugObject selectedDrugObject = mDrugList.get(position);
                Intent editDrugIntent = new Intent(ManageDrugsActivity.this, EditDrugActivity.class);
                editDrugIntent.putExtra("drugObject", selectedDrugObject);
                startActivityForResult(editDrugIntent, UPDATE_DRUG_CALLBACK_CODE);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        mDrugListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mDrugList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    DrugObject drugObject = snapshot.getValue(DrugObject.class);
                    if(drugObject != null){
                        mDrugList.add(drugObject);
                    }
                }
                loadingDrugs.setVisibility(View.INVISIBLE);
                if(mDrugList.size() == 0){
                    // show drug list empty
                    drugListEmpty.setVisibility(View.VISIBLE);
                }else{
                    // hide drug list empty
                    drugListEmpty.setVisibility(View.INVISIBLE);
                }
                mManageDrugRecyclerViewAdapter.swapData(mDrugList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == UPDATE_DRUG_CALLBACK_CODE && resultCode == RESULT_OK){
            switch (data.getStringExtra("event")){
                case "edited":
                    Snackbar.make(mManageDrugRv, "Drug updated successfully!", Snackbar.LENGTH_LONG).show();
                    break;
                case "deleted":
                    Snackbar.make(mManageDrugRv, "Drug deleted successfully!", Snackbar.LENGTH_LONG).show();
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDrugRef.addValueEventListener(mDrugListener);
    }

    @Override
    protected void onPause() {
        mDrugRef.removeEventListener(mDrugListener);
        super.onPause();
    }
}
